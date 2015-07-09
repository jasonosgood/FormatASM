package formatasm;

/**
 * Copyright 2015 Jason Aaron Osgood 
 * 
 * All rights reserved.
 * 
 * I'll pick a friendly open source license shortly. Probably BSD.
 * 
 **/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * References:
 *
 * 	http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
 * 	https://en.wikipedia.org/wiki/Printf_format_string#Format_placeholders
 *
 */

public abstract class
	FormatASM
extends
	DateTimeFormatter
implements
	Opcodes
{
	public static String printf( String template, Object... args ) {
		
		FormatASM formatter;
		
		synchronized( lock ) 
		{
			if( map.containsKey( template )) 
			{
				formatter = map.get( template );
			}
			else
			{
				String klazz = FormatASM.class.getName();
				String subklazz = klazz + "_" + id++;
	
				AccessClassLoader loader = AccessClassLoader.get( FormatASM.class );
	
				List<Spec> specs = parse( template, args );
				byte[] data = generateBytes( klazz, subklazz, specs );
				Class<FormatASM> accessClass = (Class<FormatASM>) loader.defineClass( subklazz, data );
		
				try
				{
					formatter = accessClass.newInstance();
					formatter.template = template;
					formatter.specs = specs;
					map.put( template, formatter );
					
				}
				catch( Throwable t )
				{
					throw new RuntimeException( "Error constructing FormatASM subclass: " + subklazz, t );
				}
			}
		}
		
		String result = formatter.format( args );
		return result;
	}
	
	public abstract String format( Object... args );

	
	//
	// %[argument_index$][flags][width][.precision][datetime|conversion]
	//
	// This regex attempts to balance correctness, concision, expression complexity, ease of
	// value extraction.
	//
	// Regex's groups (for extraction):
	//
	// 0:whole ( 1:index 2:flags 3:width 4:precision 5:notused ( 6:datetime 7:conv ))

	private static final String	formatSpecifier	= "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?(([tT][AaBbCcDdeFHhIjkLlMmNpQRrSsTYyZz])|([aAbBcCdeEfgGhHonsSxX%]))";

	private static Pattern pattern = Pattern.compile( formatSpecifier );

	public static List<Spec> parse( String input, Object... args )
	{
		ArrayList<Spec> specs = new ArrayList<Spec>();
		
		Matcher m = pattern.matcher( input );
		
		int autoIndex = 0;
		int i = 0;
		
		Spec prev = null;
		
		while( m.find() )
		{
			
			String whole = m.group( 0 );
//			System.out.printf( "whole %s\n", whole);
//			for( int n = 0; n < m.groupCount()+1; n++ )
//			{
//				System.out.printf( "group %d value %s\n", n, m.group( n ));
//			}

			if( m.start() > i ) 
			{
				String text = input.substring( i, m.start() );
				Spec spec = new Spec( i, m.start(), text );
				specs.add( spec );
			}
			
			// Remember varargs index for next iteration
			i = m.end();

			Spec spec = new Spec();
			specs.add( spec );

			if( "%%".equals( whole ))
			{
				spec.type = Type.PERCENT;
				continue;
			}				

			String index = m.group( 1 );
			String flags = m.group( 2 );
			String width = m.group( 3 );
			String precision = m.group( 4 );
			String datetime = m.group( 6 );
			String conv = m.group( 7 );

			// Maybe assign values in second pass
			if( index == null )
			{
				spec.index = autoIndex;
				spec.value = args[ autoIndex ];
			}
			else
			{
				spec.index = Integer.valueOf( index);
				spec.value = args[ spec.index ];
			}
			autoIndex++;
		
			
			if( flags != null )
			{
				for( char c : flags.toCharArray() )
				{
					switch( c ) {
						case '-': spec.leftFlag = true; break;
						case '#': spec.altFlag = true; break;
						case '+': spec.plusFlag = true; break;
						case ' ': spec.spaceFlag = true; break;
						case '0': spec.zeroFlag = true; break;
						case ',': spec.groupFlag = true; break;
						case '(': spec.parensFlag = true; break;
						
						case '<': 
							spec.prevFlag = true;
							spec.value = prev.value;
							break;
							
						default: break;
					}
				}
			}
			
			if( width != null )
			{
				spec.width = Integer.valueOf( width );
			}
			
			if( precision != null )
			{
				spec.precision = Integer.valueOf( precision );
			}


			if( datetime != null )
			{
				decode( spec, datetime );
			}
			else if( conv != null )
			{
				decode( spec, conv );
			}

			prev = spec;

		}
		
		if( i < input.length() ) 
		{
			String text = input.substring( i );
			Spec spec = new Spec( i, input.length(), text );
			specs.add( spec );
		}
		
//		showme( specs, args );
		return specs;
	}

//	public static String showme( List<Spec> specs, Object... args )
//	{
//		StringBuilder sb = new StringBuilder();
//		for( Spec spec : specs ) {
//			System.out.println( spec.value );
//		}
//		return sb.toString();
//	}
	
	public static void decode( Spec spec, String conv )
	{
		char type = conv.charAt( 0 );
				
		switch( type ) 
		{
			case 'A':
				spec.upper = true;
			case 'a':
				spec.type = Type.FLOAT_HEX;
				break;
				
			case 'B':
				spec.upper = true;
			case 'b':
				spec.type = Type.BOOL;
				break;
				
			case 'C':
				spec.upper = true;
			case 'c':
				spec.type = Type.CHAR;
				break;
				
			case 'd':
				spec.type = Type.INT;
				break;
				
			case 'E':
				spec.upper = true;
			case 'e':
				spec.type = Type.SCIENTIFIC;
				break;
				
			case 'f':
				spec.type = Type.FLOAT;
				break;
				
			case 'G':
				spec.upper = true;
			case 'g':
				spec.type = Type.GENERAL;
				break;
				
			case 'H':
				spec.upper = true;
			case 'h':
				spec.type = Type.HASHCODE;
				break;
				
			case 'o':
				spec.type = Type.OCTAL;
				break;
				
			case 'S':
				spec.upper = true;
			case 's':
				spec.type = Type.STRING;
				break;
				
			case 'X':
				spec.upper = true;
			case 'x':
				spec.type = Type.INT_HEX;
				break;

			case 'T':
				spec.upper = true;
			case 't':
				spec.type = Type.DATETIME;
				spec.datetime = conv.charAt( 1 );
				break;

			default:
				break;
		}
	}

	public static byte[] generateBytes( String klazz, String subklazz, List<Spec> specs ) 
	{
		klazz = klazz.replace( '.', '/' );
		subklazz = subklazz.replace( '.', '/' );
		
		ClassWriter cw = new ClassWriter( ClassWriter.COMPUTE_MAXS );
		MethodVisitor mv;

		cw.visit( V1_7, ACC_PUBLIC + ACC_SUPER, subklazz, null, klazz, null );

		{
			mv = cw.visitMethod( ACC_PUBLIC, "<init>", "()V", null, null );
			mv.visitCode();
			mv.visitVarInsn( ALOAD, 0 );
			mv.visitMethodInsn( INVOKESPECIAL, klazz, "<init>", "()V" );
			mv.visitInsn( RETURN );
			mv.visitMaxs( 0, 0 );
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod( ACC_PUBLIC + ACC_VARARGS, "format", "([Ljava/lang/Object;)Ljava/lang/String;", null, null );

			/// StringBuilder sb = new StringBuilder();
			mv.visitCode();
			mv.visitTypeInsn( NEW, "java/lang/StringBuilder" );
			mv.visitInsn( DUP );
			mv.visitMethodInsn( INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V" );
			mv.visitVarInsn( ASTORE, 2 );

			for( Spec spec : specs )
			{
				switch( spec.type )
				{
					case CONTENT:
						copyTemplateASM( klazz, mv, spec.begin, spec.end );
						// Skip further processing
						continue;
						
					case PERCENT:
						mv.visitVarInsn( ALOAD, 2 );
						mv.visitIntInsn( BIPUSH, '%' );
						mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(C)Ljava/lang/StringBuilder;" );
						mv.visitInsn( POP );
						// Skip further processing
						continue;

					case INT:
						formatIntegerASM( mv, spec );
						break;
						
					case INT_HEX:
						formatHexidecimalASM( mv, spec );
						break;
						
					case OCTAL:
						formatOctalASM( mv, spec );
						break;
						
					case BOOL:
					case CHAR:
						// text = args[?].toString();
						mv.visitVarInsn( ALOAD, 1 );
						mv.visitIntInsn( BIPUSH, spec.index );
						mv.visitInsn( AALOAD );
						mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;" );
						mv.visitVarInsn( ASTORE, 3 );
						break;
						
					case STRING:
						// text = (String) args[?]
						mv.visitVarInsn( ALOAD, 1 );
						mv.visitIntInsn( BIPUSH, spec.index );
						mv.visitInsn( AALOAD );
						mv.visitTypeInsn( CHECKCAST, "java/lang/String" );
						mv.visitVarInsn( ASTORE, 3 );
						break;

					case DATETIME:
						formatDateTimeASM( mv, spec );
						break;
						
					default:
						break;
				}
				
				if( spec.upper && spec.type != Type.DATETIME )
				{
					toUpperCaseASM( mv );
				}
				
				if( spec.width > 0 && !spec.leftFlag )
				{
					addWidthASM( mv, spec );
				}
				
				appendTextASM( mv );

				if( spec.width > 0 && spec.leftFlag )
				{
					addWidthASM( mv, spec );
				}
			}

			/// return sb.toString();
			mv.visitVarInsn( ALOAD, 2 );
			mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;" );
			mv.visitInsn( ARETURN );

			mv.visitMaxs( 0, 0 );
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}

	// / sb.append( template.substring( begin, end ));
	public static void copyTemplateASM( String klazz, MethodVisitor mv, int begin, int end )
	{
		mv.visitVarInsn( ALOAD, 2 );
		mv.visitVarInsn( ALOAD, 0 );
		mv.visitFieldInsn( GETFIELD, klazz, "template", "Ljava/lang/String;" );
		mv.visitIntInsn( BIPUSH, begin );
		mv.visitIntInsn( BIPUSH, end );
		mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/String", "substring", "(II)Ljava/lang/String;" );
		mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;" );
		mv.visitInsn( POP );
	}
	
	// text = formatInteger( (Integer) args[?], false, false, false, false );
	public static void formatIntegerASM( MethodVisitor mv, Spec spec ) 
	{
		mv.visitVarInsn( ALOAD, 1 );
		mv.visitIntInsn( BIPUSH, spec.index );
		mv.visitInsn( AALOAD );
		mv.visitTypeInsn( CHECKCAST, "java/lang/Integer" );
		mv.visitInsn( spec.parensFlag ? ICONST_1 : ICONST_0 );
		mv.visitInsn( spec.groupFlag ? ICONST_1 : ICONST_0 );
		mv.visitInsn( spec.plusFlag ? ICONST_1 : ICONST_0 );
		mv.visitInsn( spec.spaceFlag ? ICONST_1 : ICONST_0 );
		mv.visitMethodInsn( INVOKESTATIC, "formatasm/FormatASM", "formatInteger", "(Ljava/lang/Integer;ZZZZ)Ljava/lang/String;" );
		mv.visitVarInsn( ASTORE, 3 );
	}

	// text = formatDateTime( (Date) args[?], dateTimeChar );
	public static void formatDateTimeASM( MethodVisitor mv, Spec spec )
	{
		mv.visitVarInsn( ALOAD, 1 );
		mv.visitIntInsn( BIPUSH, spec.index );
		mv.visitInsn( AALOAD );
		mv.visitInsn( spec.upper ? ICONST_1 : ICONST_0 );
		mv.visitIntInsn( BIPUSH, spec.datetime );
		mv.visitMethodInsn( INVOKESTATIC, "formatasm/FormatASM", "formatDateTime", "(Ljava/lang/Object;ZC)Ljava/lang/String;" );
		mv.visitVarInsn( ASTORE, 3 );
	}

	// text = formatHexidecimal( (Integer) args[0] );
	public static void formatHexidecimalASM( MethodVisitor mv, Spec spec ) 
	{
		mv.visitVarInsn( ALOAD, 1 );
		mv.visitIntInsn( BIPUSH, spec.index );
		mv.visitInsn( AALOAD );
		mv.visitTypeInsn( CHECKCAST, "java/lang/Integer" );
		mv.visitMethodInsn( INVOKESTATIC, "formatasm/FormatASM", "formatHexidecimal", "(Ljava/lang/Integer;)Ljava/lang/String;" );
		mv.visitVarInsn( ASTORE, 3 );
	}
	
	// text = formatOctal( (Integer) args[0] );
	public static void formatOctalASM( MethodVisitor mv, Spec spec ) 
	{
		mv.visitVarInsn( ALOAD, 1 );
		mv.visitIntInsn( BIPUSH, spec.index );
		mv.visitInsn( AALOAD );
		mv.visitTypeInsn( CHECKCAST, "java/lang/Integer" );
		mv.visitMethodInsn( INVOKESTATIC, "formatasm/FormatASM", "formatOctal", "(Ljava/lang/Integer;)Ljava/lang/String;" );
		mv.visitVarInsn( ASTORE, 3 );
	}
	
	// sb.append( text );
	public static void appendTextASM( MethodVisitor mv ) 
	{
		mv.visitVarInsn( ALOAD, 2 );
		mv.visitVarInsn( ALOAD, 3 );
		mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;" );
		mv.visitInsn( POP );
	}
	
	// text = text.toUpperCase();
	public static void toUpperCaseASM( MethodVisitor mv )
	{
		mv.visitVarInsn( ALOAD, 3 );
		mv.visitMethodInsn( INVOKEVIRTUAL, "java/lang/String", "toUpperCase", "()Ljava/lang/String;" );
		mv.visitVarInsn( ASTORE, 3 );
	}
	
	public static void addWidthASM( MethodVisitor mv, Spec spec )
	{
		mv.visitVarInsn( ALOAD, 2 );
		mv.visitIntInsn( BIPUSH, spec.width );
		mv.visitVarInsn( ALOAD, 3 );
		mv.visitMethodInsn( INVOKESTATIC, "formatasm/FormatASM", "addWidth", "(Ljava/lang/StringBuilder;ILjava/lang/String;)V" );
	}
	
	// Convert integer to String, right-to-left
	public static String formatInteger( Integer ii, boolean parens, boolean comma, boolean plus, boolean space )
	{
		char[] buf = new char[32];
		int i = ii.intValue();
		int x = 31;
		boolean neg = false;
		if( i < 0 )
		{
			neg = true;
			i = -i;
			if( parens ) buf[x--] = ')';
		}
		
		byte nth = 0;
		do
		{
			if( comma && ( nth++ % 3 ) == 0 && nth > 1 )
			{
				buf[x--] = ',';
			}
			buf[x] = (char)( '0' + ( i % 10 )  );
			x--;
			i = i / 10;
			
		} while( i > 0 );
		
		if( neg ) 
		{
			buf[x--] = parens ? '(' : '-';
		}
		else 
		{
			if( plus ) 
			{
				buf[x--] = '+';
			}
			else if( space ) 
			{
				buf[x--] = ' ';
			}
		}
		String result = new String( buf, x + 1, 31 - x );
		return result;
	}
	
	public static String formatHexidecimal( Integer i )
	{
		return Integer.toHexString( i );
	}		
	
	public static String formatOctal( Integer i )
	{
		return Integer.toOctalString( i );
	}		
	
	public static void addWidth( StringBuilder sb, int width, String text )
	{
		for( int nth = width - text.length(); nth > 0; nth-- )
		{
			sb.append( ' ' );
		}
	}

	protected String template;
	
	// Keep copy of parsed format specification for debugging
	protected List<Spec> specs;
	
	private static int id = 0;
	
	private static Object lock = new Object();
	
	private static HashMap<String,FormatASM> map = new HashMap<>();
	

}
