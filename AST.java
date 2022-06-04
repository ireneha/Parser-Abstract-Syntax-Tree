//NH0322
//abstract syntax tree for microR

abstract class Program{}

class Statement extends Program{
	protected Statement stmt1, stmt2;

	public Statement() {}

	public Statement (Statement stmt1, Statement stmt2){
		this.stmt1 = stmt1;
		this.stmt2 = stmt2;
	}
	
	public String toString() {
		if (stmt1 == null)
			return "()";
		else if (stmt2 == null)
			return "(" + stmt2 + ")";
		else
			return "(:" + stmt1 + " " + stmt2 + ")";
	}

}

class Assignment extends Statement{
	protected Identifier lhs;
	protected Expression rhs;
	
	public Assignment(){}

	public Assignment (Identifier lhs, Expression rhs){
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public String toString(){
		return "(<- " + lhs + " " + rhs +")";
	}
}

class PrintStatement extends Statement{
	protected Expression expr;
	public PrintStatement (Expression expr){
		this.expr = expr;
	}
	public String toString(){
		return "(print " + expr + ")";
	}
}
class IfStatement extends Statement{
        protected Expression cond;
        protected Statement stm1;
	protected Statement stm2;
	
	public IfStatement() {}

	public IfStatement (Expression cond, Statement stm1, Statement stm2){
                this.cond = cond;
                this.stm1 = stm1;
		this.stm2 = stm2;
        }
        public String toString(){
                return "(if " + cond + " " + stm1 + " " + stm2 + ")";
        }
}
class ElseStatement extends Statement{
        protected Statement stm;
        public ElseStatement(){}

	public ElseStatement ( Statement stm){
               
                this.stm = stm;
        }
        public String toString(){
                return "(else " + stm + ")";
        }
}
class WhileStatement extends Statement{
	protected Expression cond;
	protected Statement body;
	public WhileStatement(){}
	public WhileStatement (Expression cond, Statement body){
		this.cond = cond;
		this.body = body;
	}
	public String toString(){
		return "(while " + cond +" "+ body +")";
	}
}
class ReturnStatement extends Statement{
        protected Expression expr;
	public ReturnStatement(){}
        public ReturnStatement (Expression expr){
                this.expr = expr;
        }
        public String toString(){
                return "(return " + expr + ")";
        }
}


abstract class Expression{}

class Identifier extends Expression{
	protected String id;
	public Identifier() {}
	public Identifier (String id){
		this.id = id;
	}
	public String toString(){
		return "(id " + id + ")";
	}
}
class IntValue extends Expression{
	protected  int intValue;
	public IntValue (){}
	public IntValue (int intValue){
		this.intValue = intValue;
	}
	public String toString(){
		return "(integer " + intValue + ")";
	}
}
class ConsExpr extends Expression{
        protected Expression expr;
        protected Expression expr2;
        public ConsExpr(){}
        public ConsExpr (Expression expr, Expression expr2){
                this.expr = expr;
                this.expr2 = expr2;
        }
        public String toString(){
                return "(cons " + expr +" "+ expr2 + ")";
        }
}
class HeadExpr extends Expression{
        protected Expression expr;
        public HeadExpr(){}
        public HeadExpr (Expression expr){
                this.expr = expr;
        }
        public String toString(){
                return "(head " + expr + ")";
        }
}
class TailExpr extends Expression{
        protected Expression expr;
        public TailExpr(){}
        public TailExpr (Expression expr){
                this.expr = expr;
        }
        public String toString(){
                return "(tail " + expr + ")";
        }
}
class NullExpr extends Expression{
        public NullExpr(){}

        public String toString(){
                return "(null)";
        }
}
class AsintExpr extends Expression{
	public AsintExpr(){}
	public String toString(){
		return "(readline)";
	}
}

class Binary extends Expression {
  	protected String op;
  	protected Expression term1, term2;

  	public Binary () { }

  	public Binary (String op, Expression term1, Expression term2) {
    		this . op = op;
    		this . term1 = term1;
    		this . term2 = term2;
  	}

 	 public String toString () {
    		return "(" + op + " " + term1 + " " + term2 + ")";
  	}

}
class Unary extends Expression {
  	protected String op;
  	protected Expression term;

  	public Unary () { }

  	public Unary (String op, Expression term) {
    		this . op = op;
    		this . term = term;
  	}	

  	public String toString () {
    		return "(" + op + " " + term + ")";
  	}

}
class CondExpr extends Expression{
	protected Expression term1, term2;
	protected String str1, str2;
	public CondExpr(){}
	public CondExpr (Expression term1, Expression term2, String str1, String str2){
		this.term1 = term1;
		this.term2 = term2;
		this.str1 = str1;
		this.str2 = str2;
	}
	public String toString(){
		return "(" + str2 + " "+ str1 + term1 +" " + term2 +")";
	}
}
	

class IDExpr extends Expression{
	protected Expression term1,term2,term3;

	public IDExpr(){}
	public IDExpr(Expression term1, Expression term2, Expression term3){
		this.term1 = term1;
		this.term2 = term2;
		this.term3 = term3;
	}
	public String toString (){
		return "(" +term1 + " "+ term2 + ", " + term3 + ")";
	}
}
