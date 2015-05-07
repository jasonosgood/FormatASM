package asm;
class Spec 
{
	Spec() {}
	
	Spec( int begin, int end, String text )
	{
		System.out.printf( "%d %d %s\n", begin, end, text );
		this.begin = begin;
		this.end = end;
		type = Type.CONTENT;
		value = text;
	}
	
	int index = -1;
	
	Object value;
	
	// Used for content
	int begin;
	int end;

	Type type;
	
	int width;
	int precision;
	boolean datetime;
	
	boolean upper;
	boolean leftFlag;
	boolean altFlag;
	boolean plusFlag;
	boolean spaceFlag;
	boolean zeroFlag;
	boolean groupFlag;
	boolean parensFlag;
	boolean prevFlag;

}