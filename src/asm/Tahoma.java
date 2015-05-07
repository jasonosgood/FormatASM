package asm;

public class Tahoma
{
	
	public static void main( String[] args )
		throws Exception
	{
		
//		System.out.printf( "tada %s", "magic" );
		
//		String input = "pure %11$-#+ 0,(\\<9.9d";

//		String input = "ma%%ke a wish '%s' '%d' '%tF' !";
//		List<Spec> specs = parse( input, "abc", 100, new Date() );
		String input = "ma%%ke a wish '%d' '%s' wonderbar ! '%d' '%,d' '%(d' '%8B' ###\n";
		Object[] blah = { 1234, "abc", -10, 2000, -30, false };
		
		String first = FormatASM.printf( input, blah );
		System.out.print( first );
		String second = FormatASM.printf( input, blah );
		System.out.print( second );
		System.out.printf( input, blah );
		
	}


}
