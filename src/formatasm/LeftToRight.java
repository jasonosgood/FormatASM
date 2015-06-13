package formatasm;

public class LeftToRight
{
	public static void main( String[] args )
	{
//		int victim = 1234512345;
		int victim = 1234512345;
		int digits = (int) Math.floor( Math.log10( victim ));
		int woot = Math.getExponent( victim );
		System.out.println( woot );
		int argh = (int) Math.pow( 10, digits );
		char[] blah = new char[digits + 1];

		for( int nth = 0; nth < digits +1; nth++ )
		{
			System.out.print( victim / argh );
			victim %= argh;
			argh /= 10;
		}


	}
}
