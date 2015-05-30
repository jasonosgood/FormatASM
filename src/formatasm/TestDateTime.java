package formatasm;


import java.util.Date;
import java.util.Locale;

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
		Date now = new Date();
		for( Locale l : Locale.getAvailableLocales() )
		{
//		Locale l = Locale.FRENCH;
		// Upper and lower case
		for( char pre : "Tt".toCharArray() )
		{
			// Every possible conversion
			for( char post : "AaBbCcDdeFHhIjkLlMmNpQRrSsTYyZz".toCharArray() )
			{
//				String pattern = "'%-16" + pre + post + "'";
				String pattern = "%" + pre + post;
				try
				{
					String a = String.format( l, pattern, now );
//					String b = FormatASM.printf( pattern, now );
					boolean upper = pre == 'T';
					String b = DateTimeFormatter.formatDateTime( now, upper, post, l );
					boolean match = a.equals( b );
					if( !match )
					{
						System.out.printf( "locale: %s '%s' a: %s b: %s => %b \n", l.toString(), pattern, a, b, match );
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


}
