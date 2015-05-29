package formatasm;

import java.text.DateFormatSymbols;
import java.util.*;

public class DateTimeFormatter
{

	public static void main( String[] args )
	{
//		Date now = new Date();
		long now = System.currentTimeMillis();
//		String now = "123";
		System.out.printf( "%tr\n", now );
		DateTimeFormatter dtf = new DateTimeFormatter();
		String msg = dtf.formatDateTime( now, false, 'r' );

		System.out.println( msg );

	}



	public static String formatDateTime( Object value, boolean upper, char c )
	{
		Locale l = Locale.getDefault();
		Calendar t = null;
		if( value instanceof Calendar )
		{
			t = (Calendar) value;
		}
		else if( value instanceof Date )
		{
			t = Calendar.getInstance( l );
			t.setTime( (Date) value );
		}
		else if( value instanceof Long )
		{
			t = Calendar.getInstance( l );
			t.setTimeInMillis( (Long) value );
		}
		else
		{
			throw new IllegalFormatConversionException( c, value.getClass() );
		}

		StringBuilder sb = new StringBuilder();
		formatDateTime( t, c, upper, l, sb );
		return sb.toString();
	}

	public static void formatDateTime( Calendar t, char c, boolean upper, Locale l, StringBuilder sb )
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
				ampm = upper ? ampm.toUpperCase( l ) : ampm.toLowerCase( l );
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
				name = upper ? name.toUpperCase( l ) : name;
				sb.append( name );
				break;
			}

			case 'a': // Day of week, short name
			{
				int i = t.get( Calendar.DAY_OF_WEEK );
				String day = DateFormatSymbols.getInstance().getShortWeekdays()[i];
				day = upper ? day.toUpperCase( l ) : day;
				sb.append( day );
				break;
			}

			case 'A': // Day of week
			{
				int i = t.get( Calendar.DAY_OF_WEEK );
				String day = DateFormatSymbols.getInstance().getWeekdays()[i];
				day = upper ? day.toUpperCase( l ) : day;
				sb.append( day );
				break;
			}

			case 'B':
			{
				int i = t.get( Calendar.MONTH );
				String month = DateFormatSymbols.getInstance().getMonths()[i];
				month = upper ? month.toUpperCase( l ) : month;
				sb.append( month );
				break;
			}

			case 'b': // Month, short name
			case 'h':
			{
				int i = t.get( Calendar.MONTH );
				String month = DateFormatSymbols.getInstance().getShortMonths()[i];
				month = upper ? month.toUpperCase( l ) : month;
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
				formatDateTime( t, 'H', upper, l, sb );
				sb.append( ':' );
				formatDateTime( t, 'M', upper, l, sb );
				break;
			}

			case 'T': // 12 hour timestamp hh:mm:ss, equiv to "%tH:%tM:%tS"
			{
				formatDateTime( t, 'H', upper, l, sb );
				sb.append( ':' );
				formatDateTime( t, 'M', upper, l, sb );
				sb.append( ':' );
				formatDateTime( t, 'S', upper, l, sb );
				break;
			}

			case 'r': // 12 hour timestamp hh:mm:ss [am|pm], equiv to "%tI %M %tS %Tp"
			{
				formatDateTime( t, 'I', upper, l, sb );
				sb.append( ':' );
				formatDateTime( t, 'M', upper, l, sb );
				sb.append( ':' );
				formatDateTime( t, 'S', upper, l, sb );
				sb.append( ' ' );
				formatDateTime( t, 'p', true, l, sb );
				break;
			}
			
			case 'c': // Date and time, equiv to "%ta %tb %td %tT %tZ %tY", eg "Sun Jul 20 16:17:00 EDT 1969"
			{
				formatDateTime( t, 'a', upper, l, sb );
				sb.append( ' ' );
				formatDateTime( t, 'b', upper, l, sb );
				sb.append( ' ' );
				formatDateTime( t, 'd', upper, l, sb );
				sb.append( ' ' );
				formatDateTime( t, 'T', upper, l, sb );
				sb.append( ' ' );
				formatDateTime( t, 'Z', upper, l, sb );
				sb.append( ' ' );
				formatDateTime( t, 'Y', upper, l, sb );
				break;
			}

			case 'D': //  date, equiv to "%tm/%td/%ty"
			{
				formatDateTime( t, 'm', upper, l, sb );
				sb.append( '/' );
				formatDateTime( t, 'd', upper, l, sb );
				sb.append( '/' );
				formatDateTime( t, 'y', upper, l, sb );
				break;
			}

			case 'F': // ISO 8601 date, equiv to %tY-%tm-%td".
			{
				formatDateTime( t, 'Y', upper, l, sb );
				sb.append( '-' );
				formatDateTime( t, 'm', upper, l, sb );
				sb.append( '-' );
				formatDateTime( t, 'd', upper, l, sb );
				break;
			}
			
			default:
				break;
		}
	}

	// Assumes non-negative value. Used to format simple datetime integers.
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
