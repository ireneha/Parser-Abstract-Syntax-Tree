

public class MicroAST{
	public static void main(String args[]) throws java.io.IOException{
		System.out.println("Source program");
		System.out.println("--------------");
		System.out.println();

		ParserAST micro = new ParserAST();
		Program program = micro.program();
		System.out.println();
		System.out.println();
		System.out.println ("Abstract Syntax Tree");
    		System.out.println ("--------------------");
    		System.out.println ();
    		System.out.println (program);
	}

}
