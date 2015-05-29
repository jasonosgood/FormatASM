package formatasm;


/**
 * Copyright 2015 Jason Aaron Osgood 
 * 
 * All rights reserved.
 * 
 * I'll pick a friendly open source license shortly. Probably BSD.
 * 
 **/

class Spec 
{
	Spec() {}
	
	Spec( int begin, int end, String value )
	{
		this.begin = begin;
		this.end = end;
		type = Type.CONTENT;
		this.value = value;
	}
	
	int index = -1;
	
	// For debugging during code generation only
	Object value;
	
	// Used for content
	int begin;
	int end;

	Type type;
	char datetime;

	int width;
	int precision;

	boolean upper;
	boolean leftFlag;
//	boolean altFlag;
	boolean plusFlag;
	boolean spaceFlag;
	boolean zeroFlag;
	boolean groupFlag;
	boolean parensFlag;
	boolean prevFlag;

}