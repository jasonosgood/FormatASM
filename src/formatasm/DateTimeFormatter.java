package formatasm;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeFormatter
{

	public static void main( String[] args )
	{
		Date now = new Date();
		System.out.printf( "%tc\n", now );
		DateTimeFormatter dtf = new DateTimeFormatter();
		String msg = dtf.print( now, 'c' );

		System.out.println( msg );

	}



	public String print( Date date, char c )
	{
		Locale l = Locale.getDefault();
		Calendar t = Calendar.getInstance( l );
		t.setTime( date );
		StringBuilder sb = new StringBuilder();
		print( sb, t, c, l );
		return sb.toString();
	}

	public void print( StringBuilder sb, Calendar t, char c, Locale l )
	{
		if( sb == null ) sb = new StringBuilder();
		switch( c )
		{
			case 'H': // hours 00 - 23
			{
				int i = t.get( Calendar.HOUR_OF_DAY );
				zeroPad( sb, i, 2 );
				break;
			}

			case 'k': // hours 0 - 23
			{
				int i = t.get( Calendar.HOUR_OF_DAY );
				zeroPad( sb, i, 0 );
				break;
			}

			case 'I': // hours 01 - 12
			{
				int i = t.get( Calendar.HOUR );
				if( i == 0 ) i = 12;
				zeroPad( sb, i, 2 );
				break;
			}

			case 'l': // hours 1 - 12
			{
				int i = t.get( Calendar.HOUR );
				if( i == 0 ) i = 12;
				zeroPad( sb, i, 0 );
				break;
			}

			case 'M': // minutes 00 - 59
			{
				int i = t.get( Calendar.MINUTE );
				zeroPad( sb, i, 2 );
				break;
			}

			case 'N': // Nanoseconds 000000000 - 999999999
			{
				int i = t.get( Calendar.MILLISECOND ) * 1000000;
				zeroPad( sb, i, 9 );
				break;
			}

			case 'L': // millis 000 - 999
			{
				int i = t.get( Calendar.MILLISECOND );
				zeroPad( sb, i, 2 );
				break;
			}

			case 'Q': // millis since epoch 0 - ??
			{
				long i = t.getTimeInMillis();
				sb.append( i );
				break;
			}

			case 'p': // Ante or post meridian (am or pm)
			{
				int i = t.get( Calendar.AM_PM );
				String ampm = DateFormatSymbols.getInstance().getAmPmStrings()[i];
				ampm = ampm.toLowerCase( l );
				sb.append( ampm );
				break;
			}

			case 's': // seconds since epoch 0 - ??
			{
				long i = t.getTimeInMillis() / 1000;
				sb.append( i );
				break;
			}

			case 'S': // seconds 00 - 59 (60 = leap second)
			{
				int i = t.get( Calendar.SECOND );
				zeroPad( sb, i, 2 );
				break;
			}

			case 'z': // Timezone offset (-|+)HHMM
			{
				int i = t.get( Calendar.ZONE_OFFSET ) + t.get( Calendar.DST_OFFSET );
				if( i < 0 )
				{
					sb.append( '-' );
					i = -i;
				}
				else
				{
					sb.append( '+' );
				}

				// Convert from millis to hours and minutes "HHMM"
				int min = i / 60000;
				int offset = ( min / 60 ) * 100 + ( min % 60 );
				zeroPad( sb, offset, 4 );
				break;
			}

			case 'Z': // Timezone
			{
				TimeZone tz = t.getTimeZone();
				boolean daylight = t.get( Calendar.DST_OFFSET ) != 0;
				String name = tz.getDisplayName( daylight, TimeZone.SHORT );
				sb.append( name );
				break;
			}

			case 'a': // Day of week, short name
			{
				int i = t.get( Calendar.DAY_OF_WEEK );
				String day = DateFormatSymbols.getInstance().getShortWeekdays()[i];
				sb.append( day );
				break;
			}

			case 'A': // Day of week
			{
				int i = t.get( Calendar.DAY_OF_WEEK );
				String day = DateFormatSymbols.getInstance().getWeekdays()[i];
				sb.append( day );
				break;
			}

			case 'B':
			{
				int i = t.get( Calendar.MONTH );
				String month = DateFormatSymbols.getInstance().getMonths()[i];
				sb.append( month );
				break;
			}

			case 'b': // Month, short name
			case 'h':
			{
				int i = t.get( Calendar.MONTH );
				String month = DateFormatSymbols.getInstance().getShortMonths()[i];
				sb.append( month );
				break;
			}

			case 'C': // Century 00 - 99
			{
				int i = t.get( Calendar.YEAR );
				i /= 100;
				zeroPad( sb, i, 2 );
				break;
			}

			case 'y': // Year 00 - 99
			{
				int i = t.get( Calendar.YEAR );
				i %= 100;
				zeroPad( sb, i, 2 );
				break;
			}

			case 'Y': // Year 0000 - 9999
			{
				int i = t.get( Calendar.YEAR );
				zeroPad( sb, i, 4 );
				break;
			}

			case 'd': // Day of month 01 - 31
			{
				int i = t.get( Calendar.DATE );
				zeroPad( sb, i, 2 );
				break;
			}

			case 'e': // Day of month 1 - 31
			{
				int i = t.get( Calendar.DATE );
				zeroPad( sb, i, 0 );
				break;
			}

			case 'j': // day of year 001 - 366
			{
				int i = t.get( Calendar.DAY_OF_YEAR );
				zeroPad( sb, i, 3 );
				break;
			}

			case 'm': // month 00 - 12
			{
				int i = t.get( Calendar.MONTH ) + 1;
				zeroPad( sb, i, 2 );
				break;
			}

			case 'R': // 12 hour timestamp hh:mm, equiv to "%tH:%tM"
			{
				print( sb, t, 'H', l );
				sb.append( ':' );
				print( sb, t, 'M', l );
				break;
			}

			case 'T': // 12 hour timestamp hh:mm:ss, equiv to "%tH:%tM:%tS"
			{
				print( sb, t, 'H', l );
				sb.append( ':' );
				print( sb, t, 'M', l );
				sb.append( ':' );
				print( sb, t, 'S', l );
				break;
			}

			case 'r': // 12 hour timestamp hh:mm:ss [am|pm], equiv to "%tI %M %tS %tp"
			{
				print( sb, t, 'I', l );
				sb.append( ' ' );
				print( sb, t, 'M', l );
				sb.append( ' ' );
				print( sb, t, 'S', l );
				sb.append( ' ' );
				print( sb, t, 'p', l );
				break;
			}
			
			case 'c': // Date and time, equiv to "%ta %tb %td %tT %tZ %tY", eg "Sun Jul 20 16:17:00 EDT 1969"
			{
				print( sb, t, 'a', l );
				sb.append( ' ' );
				print( sb, t, 'b', l );
				sb.append( ' ' );
				print( sb, t, 'd', l );
				sb.append( ' ' );
				print( sb, t, 'T', l );
				sb.append( ' ' );
				print( sb, t, 'Z', l );
				sb.append( ' ' );
				print( sb, t, 'Y', l );
				break;
			}

			case 'D': //  date, equiv to "%tm/%td/%ty"
			{
				print( sb, t, 'm', l );
				sb.append( '/' );
				print( sb, t, 'd', l );
				sb.append( '/' );
				print( sb, t, 'y', l );
				break;
			}

			case 'F': // ISO 8601 date, equiv to %tY-%tm-%td".
			{
				print( sb, t, 'Y', l );
				sb.append( '-' );
				print( sb, t, 'm', l );
				sb.append( '-' );
				print( sb, t, 'd', l );
				sb.append( '-' );
				break;
			}
			
			default:
				break;
		}
	}

	// Assumes non-negative value
	public static void zeroPad( StringBuilder sb, int i, int width )
	{
		char[] buf = new char[32];
		int x = 31;

		int limit = x - width;
		do
		{
			buf[x] = (char)( '0' + ( i % 10 )  );
			x--;
			i = i / 10;

		} while( i > 0 );

		while( x > limit )
		{
			buf[x] = (char) '0';
			x--;
		}

		sb.append( buf, x + 1, 31 - x );
	}


}
