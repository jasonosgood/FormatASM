package asm;

public class PrototypeFormatASM extends FormatASM
{

	public static void main( String[] args )
	{
		
		PrototypeFormatASM exec = new PrototypeFormatASM();
		exec.template = "%d|%s|%8B|\n";
//		exec.specs = new Spec[] { new Spec(), new Spec(), new Spec(), new Spec(), new Spec() };
//		exec.specs[0].index = 0;
//		exec.specs[0].type = Type.INT;
//		exec.specs[1].index = -1;
//		exec.specs[1].type = Type.CONTENT;
//		exec.specs[2].index = 1;
//		exec.specs[2].type = Type.STRING;
//		exec.specs[3].index = -1;
//		exec.specs[3].type = Type.CONTENT;
//		exec.specs[4].index = 2;
//		exec.specs[4].type = Type.BOOL;
		
//		exec.template = "---";

		Object[] many = new Object[] { 1234, "xyzzy", false };
		String result = exec.format( many );
		
		System.out.print( result );
		
		System.out.printf( exec.template, many );
		
	}
	
	public String format( Object... args )
	{
		StringBuilder sb = new StringBuilder();
		
		String text;
		text = formatInteger( (Integer) args[0], false, false, false, false ); 
		sb.append( text );
		
		sb.append( template.substring( 2, 3 ));
		
		text = (String) args[1];
		sb.append( text );
		
		sb.append( template.substring( 5, 6 ));
		
		text = args[2].toString();
		text = text.toUpperCase();
		addWidth( sb, 8, text );
		sb.append( text );
		
		sb.append( template.substring( 9, 11 ));
		
		return sb.toString();
	}
}
