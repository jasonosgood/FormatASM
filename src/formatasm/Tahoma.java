package formatasm;


import java.util.Date;

/**
 * Copyright 2015 Jason Aaron Osgood 
 * 
 * All rights reserved.
 * 
 * I'll pick a friendly open source license shortly. Probably BSD.
 * 
 **/


public class Tahoma
{
	
	public static void main( String[] args )
		throws Exception
	{
		String input = "%tr\n";
		Object[] blah = { new Date() };
//		String input = "a float %f\n";
//		Object[] blah = { 1.0 };
//		String input = "ma%%ke a wish '%d' '%-8S' wonderbar ! '%d' '%,d' '%(d' '%8B' '%c' '%x' '%o' ###\n";
//		Object[] blah = { 1234, "abc", -10, 2000, -30, false, 'Z', 0xAB, 0xAB };

		System.out.printf( input, blah );

		String first = FormatASM.printf( input, blah );
		System.out.print( first );
//		String second = FormatASM.printf( input, blah );
//		System.out.formatDateTime( second );

	}


}
