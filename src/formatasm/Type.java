package formatasm;


/**
 * Copyright 2015 Jason Aaron Osgood 
 * 
 * All rights reserved.
 * 
 * I'll pick a friendly open source license shortly. Probably BSD.
 * 
 **/


enum Type
{
	BOOL,
	CHAR,
	DATETIME,
	FLOAT,
	FLOAT_HEX,
	GENERAL,
	HASHCODE,
	INT,
	INT_HEX,
	OCTAL,
	SCIENTIFIC,
	STRING,
	PERCENT, // %%
	CONTENT; // Original content of printf template
}