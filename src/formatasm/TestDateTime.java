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


public class TestDateTime
{
	
	public static void main( String[] args )
		throws Exception
	{
//		Date now = new Date();
		Float now = 1.0f;
		for( char pre : "tT".toCharArray() )
		{
			for( char post : "AaBbCcDdeFHhIjkLlMmNpQRrSsTYyZz".toCharArray() )
			{
				String pattern = "'%-16" + pre + post + "'";
				try
				{
					String a = String.format( pattern, now );
					String b = FormatASM.printf( pattern, now );
					boolean match = a.equals( b );
					//				if( !match )
					{
						System.out.printf( "'%s' a: %s b: %s => %b \n", pattern, a, b, match );
					}

				}
				catch( Exception e )

				{
					System.out.printf( "'%s' => %s: %s \n", pattern, e.getClass(), e.getMessage() );
//					e.printStackTrace();

				}
			}
		}

	}


}
