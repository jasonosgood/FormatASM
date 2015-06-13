package formatasm;


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Copyright 2015 Jason Aaron Osgood 
 * 
 * All rights reserved.
 * 
 * I'll pick a friendly open source license shortly. Probably BSD.
 * 
 **/

public class PrototypeFormatASM extends FormatASM
{

	public static void main( String[] args )
	{
		BigInteger bi = new BigInteger( String.valueOf( 100L ) );
		BigDecimal bd = new BigDecimal( 100.0 );

		PrototypeFormatASM exec = new PrototypeFormatASM();
		exec.template = "%d|%c|%8B|\n";

		Object[] many = new Object[] { 1234, 'z', false };
		String result = exec.format( many );
		
		System.out.print( result );
		
		System.out.printf( exec.template, many );
		
	}
	
	/**
	 * This implementation is used to determine how the generated code should look. Get this to work as desired,
	 * then use ASMify plugin to infer the code to generate this code.
	 **/
	
	public String format( Object... args )
	{
		StringBuilder sb = new StringBuilder();
		
		String text;
//		Character c;
//		sb.append( '%' );
		text = formatInteger( (Integer) args[0], false, false, false, false ); 
		sb.append( text );
		
		sb.append( template.substring( 2, 3 ));
		
//		text = (String) args[1];
//		sb.append( text );

		text = args[1].toString();
		text = text.toUpperCase();
		sb.append( text );
		
		
		sb.append( template.substring( 5, 6 ));
		
		text = args[2].toString();
		text = text.toUpperCase();
		addWidth( sb, 8, text );
		sb.append( text );
		
		sb.append( template.substring( 9, 11 ));
		
		return sb.toString();
	}
}
