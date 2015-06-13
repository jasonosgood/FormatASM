package formatasm;


import java.util.Formattable;
import java.util.Formatter;

/**
 * Copyright 2015 Jason Aaron Osgood 
 * 
 * All rights reserved.
 * 
 * I'll pick a friendly open source license shortly. Probably BSD.
 * 
 **/


public class Olympus
{
	
	public static void main( String[] args )
		throws Exception
	{

//		System.out.printf( "%,12.12g\n", 123.5678900 );
//		System.out.printf( "%,12.12g\n", 123567890.0 );

//		System.out.printf( "whatever %#s\n", new BearClaw() );
		System.out.printf( "%f\n", 1234.5678900 );
		System.out.printf( "'%8f'\n", 1234.5678900 );
		System.out.printf( "'%8f'\n", 1234.56 );
		System.out.printf( "'%020f'\n", 1234.56 );
		System.out.printf( "'%,20f'\n", 1234.56 );
		System.out.printf( "'%(20f'\n", 1234.56 );
		System.out.printf( "'%(,020f'\n", -1234.56 );
		System.out.printf( "'%+,020f'\n", -1234.56 );
		System.out.printf( "'%,020f'\n", 1234.56 );
		System.out.printf( "'%f'\n", 1234.560000 );
		System.out.printf( "'%.2f'\n", 1234.56789 );
		System.out.printf( "%e\n", 1234567890.0 );
		System.out.printf( "%g\n", 1234567890.0 );
		System.out.printf( "%#f\n", 1234567890.0 );
		System.out.printf( "%#f\n", (float) 123 );
		System.out.printf( "%f\n", (float) 123 );


//		System.out.printf( "%x\n", (byte) 0xEE );
//		System.out.printf( "%x\n", (short) 0xEEEE );
//		System.out.printf( "%x\n", (int) 0xEEEEEEEE );
//		System.out.printf( "%x\n", (long) 0x7FFFFFFF );
//		System.out.printf( "%x\n", (long) -1 );
//		System.out.printf( "%x\n", (long) -2 );
//
//		System.out.printf( "%x\n", Byte.MIN_VALUE );
//		System.out.printf( "%x\n", Short.MIN_VALUE );
//		System.out.printf( "%x\n", Integer.MIN_VALUE );
//		System.out.printf( "%x\n", Long.MIN_VALUE );
//
//		System.out.printf( "%X\n", Byte.MAX_VALUE );
//		System.out.printf( "%X\n", Short.MAX_VALUE );
//		System.out.printf( "%X\n", Integer.MAX_VALUE );
//		System.out.printf( "%X\n", Long.MAX_VALUE );
//		System.out.printf( "%X\n", (long) -2 );


	}


	public static class BearClaw implements Formattable
	{

		@Override
		public void formatTo( Formatter formatter, int flags, int width, int precision )
		{
			formatter.format( "bearclaw" );
		}
	}
}
