package formatasm;


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
		
		PrototypeFormatASM exec = new PrototypeFormatASM();
		exec.template = "%d|%s|%8B|\n";

		Object[] many = new Object[] { 1234, "xyzzy", false };
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
		text = formatInteger( (Integer) args[0], false, false, false, false ); 
		sb.append( text );
		
		sb.append( template.substring( 2, 3 ));
		
		text = (String) args[1];
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
