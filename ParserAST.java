//Nh0322

import java.io.*;

public class ParserAST {

  protected MicroRLexer lexer;	// lexical analyzer
  protected Token token;	// current token

  public ParserAST () throws IOException {
    lexer = new MicroRLexer (new InputStreamReader (System . in));
    getToken ();
  }

  private void getToken () throws IOException {
    token = lexer . nextToken ();
  }

  // Program ::= source ( â€œList.Râ€ ) {FunctionDef} MainDef

  public Program program () throws IOException {
    Program program;
    if (token . symbol () != Symbol . SOURCE) 	// source
      ErrorMessage . print (lexer . position (), "source EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LPAREN) 	// (
      ErrorMessage . print (lexer . position (), "( EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LISTR) 	// "List.R"
      ErrorMessage . print (lexer . position (), "\"List.R\" EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . RPAREN ) 	// )
      ErrorMessage . print (lexer . position (), ") EXPECTED");
    getToken ();
    while (token . symbol () == Symbol . ID) 	// {FunctionDef}
      program = functionDef ();
    program = mainDef ();                            	// MainDef
    if (token . symbol () != Symbol . EOF) 
      ErrorMessage . print (lexer . position (), "END OF PROGRAM EXPECTED");
    return program;
  }


  // FunctionDef ::= 
  //     id <âˆ’ function ( [id {, id }] ) "{" {Statement} return ( Expr ) ; "}"

  public Statement functionDef () throws IOException {
    Statement stmt = null, stmt1 = null;
    Expression expr;
    if (token . symbol () != Symbol . ID)        	// id
      ErrorMessage . print (lexer . position (), "id EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . ASSIGN)     	// <-
      ErrorMessage . print (lexer . position (), "<- EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . FUNCTION)  	// function
      ErrorMessage . print (lexer . position (), "function EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LPAREN)     	// (
      ErrorMessage . print (lexer . position (), "( EXPECTED");
    getToken ();
    if (token . symbol () == Symbol . ID) {     	// [ id
      getToken ();
      while (token . symbol () == Symbol . COMMA) {	// { ,
        getToken ();
        if (token . symbol () != Symbol . ID)      	// id
          ErrorMessage . print (lexer . position (), "id EXPECTED");
        getToken ();
      }                                          	// }
    }                                             	// ]
    if (token . symbol () != Symbol . RPAREN)   	// )
        ErrorMessage . print (lexer . position (), ") EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LBRACE)      	// "{"
      ErrorMessage . print (lexer . position (), "{ EXPECTED");
    getToken ();
    while (token . symbol () == Symbol . IF        	// { 
        || token . symbol () == Symbol . WHILE
        || token . symbol () == Symbol . ID
        || token . symbol () == Symbol . PRINT) 
      stmt1 = statement ();                             	// Statement }
    if (token . symbol () != Symbol . RETURN)    	// return
      ErrorMessage . print (lexer . position (), "return EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LPAREN)    	// (
      ErrorMessage . print (lexer . position (), "( EXPECTED");
    getToken ();
    expr = expr();    // Expr
    stmt1 = new ReturnStatement (expr);
    if (token . symbol () != Symbol . RPAREN)    	// )
      ErrorMessage . print (lexer . position (), ") EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . SEMICOLON)    	// ;
      ErrorMessage . print (lexer . position (), "; EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . RBRACE)    	// "}"
      ErrorMessage . print (lexer . position (), "} EXPECTED");
    getToken ();
    return stmt;
  }
  // StatementList ::= Statement { Statement }

  public Statement statementList () throws IOException {
    Statement stmt = null, stmt1,stmt2;
    stmt1 = statement ();                          	// Statement
    while (token . symbol () == Symbol . IF  	// {
        || token . symbol () == Symbol . WHILE
        || token . symbol () == Symbol . ID
        || token . symbol () == Symbol . PRINT){
      if (stmt1 == null)
	    stmt1 = statement ();    // Statement }
      else{
	    stmt2 = statement();
	    if (stmt2 !=null)
		    stmt = new Statement (stmt1, stmt2);
      }
    }
    return stmt;
  }

  // MainDef ::= main < âˆ’ function ( ) "{" StatementList "}"

  public Statement mainDef () throws IOException {
    Statement stmt;
    if (token . symbol () != Symbol . MAIN)   	// main
      ErrorMessage . print (lexer . position (), "main EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . ASSIGN)  	// <-
      ErrorMessage . print (lexer . position (), "<- EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . FUNCTION) // function
      ErrorMessage . print (lexer . position (), "function EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LPAREN) 	// (
      ErrorMessage . print (lexer . position (), "( EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . RPAREN) 	// )
      ErrorMessage . print (lexer . position (), ") EXPECTED");
    getToken ();
    if (token . symbol () != Symbol . LBRACE) 	// "{"
      ErrorMessage . print (lexer . position (), "{ EXPECTED");
    getToken ();
    stmt = statementList();                      	// StatementList
    if (token . symbol () != Symbol . RBRACE) 	// "}"
      ErrorMessage . print (lexer . position (), "} EXPECTED");
    getToken ();
    return stmt;
  }

  // Statement ::= if ( Cond ) { StatementList } [else { StatementList }]
  //   | while ( Cond ) { StatementList }
  //   | id < âˆ’ Expr ;
  //   | print ( Expr ) ;

  public Statement statement () throws IOException {
    Statement stmt, stmt1 = null, stmt2 = null;	
    Expression cond;
    switch (token . symbol ()) {

      case IF :                                  	// if
        getToken ();
        if (token . symbol () != Symbol . LPAREN)   	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        cond = cond ();	// Cond
        
	if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . LBRACE) 	// "{"
          ErrorMessage . print (lexer . position (), "{ EXPECTED");
        getToken ();
        
	stmt1 = statementList();                       	// StatementList
	
	if (token . symbol () != Symbol . RBRACE) 	// "}"
          ErrorMessage . print (lexer . position (), "} EXPECTED");
        getToken ();
        if (token . symbol () == Symbol . ELSE) {  	// [ else
          getToken ();
          if (token . symbol () != Symbol . LBRACE) 	// "{"
            ErrorMessage . print (lexer . position (), "{ EXPECTED");
          getToken ();
          
	  stmt2 = statementList();                       	// StatementList

	  if (token . symbol () != Symbol . RBRACE) 	// "}"
            ErrorMessage . print (lexer . position (), "} EXPECTED");
          getToken ();
        }                                        	// ]
	stmt = new IfStatement (cond, stmt1, stmt2);
        break;

      case WHILE :                                  	// while
        getToken ();
        if (token . symbol () != Symbol . LPAREN)   	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        cond = cond ();                                 	// Cond
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . LBRACE) 	// "{"
          ErrorMessage . print (lexer . position (), "{ EXPECTED");
        getToken ();
        stmt1 = statementList();	// StatementList
        
	if (token . symbol () != Symbol . RBRACE) 	// "}"
          ErrorMessage . print (lexer . position (), "} EXPECTED");
        getToken ();
	stmt = new WhileStatement (cond,stmt1);
        break;

      case ID :                                  	// id
        Identifier id;
	id = new Identifier (token.lexeme());
	getToken ();
        if (token . symbol () != Symbol . ASSIGN)  	// <-
          ErrorMessage . print (lexer . position (), "<- EXPECTED");
        getToken ();
        cond = expr ();                                 	// Expr
        stmt1 = new Assignment(id,cond); 
	if (token . symbol () != Symbol . SEMICOLON)  	// ;
          ErrorMessage . print (lexer . position (), "; EXPECTED");
        getToken ();
        break;

      case PRINT :                                  	// print
        getToken ();
        if (token . symbol () != Symbol . LPAREN)  	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        cond = expr ();                                 	// Expr
        stmt1 = new PrintStatement (cond);
	if (token . symbol () != Symbol . RPAREN) 	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . SEMICOLON)  	// ;
          ErrorMessage . print (lexer . position (), "; EXPECTED");
        getToken ();
        break;

      default :
        ErrorMessage . print (lexer . position (), "UNRECOGNIZABLE SYMBOL");

    }
    return stmt1;
  }
  // Cond ::= AndExpr {|| AndExpr}

  public Expression cond () throws IOException {
    Expression expr, expr1 = null;
    String op = null;
    expr = andExpr ();                                	// AndExpr
    while (token . symbol () == Symbol . OR) { 	// { ||
      op = token.lexeme();
      getToken ();
      expr1 = andExpr ();                              	// AndExpr }
    }
    if (expr1!=null)
    	expr = new Binary (op, expr,expr1);
    else
	    expr = new Binary(op, expr, null);
    return expr;
  }

  // AndExpr ::= RelExpr {&& RelExpr}

  public Expression andExpr () throws IOException {
    Expression expr, expr1 = null;
    String op = null;;
    expr = relExpr ();                                	// relExpr
    while (token . symbol () == Symbol . AND) { // { &&
      op = token.lexeme();
      getToken ();
      expr1 = relExpr ();                              	// relExpr }
    }
    expr = new Binary (op,expr,expr1);
    return expr;
  }

  // RelExpr ::= [!] Expr RelOper Expr

  public Expression relExpr () throws IOException {
    Expression expr, expr1 = null;
    String op = null, op1 = null;
    if (token . symbol () == Symbol . NOT){ 	// [!]
      op = token.lexeme();
      getToken ();
    }
    expr = expr ();                             	// Expr
    switch (token . symbol ()) {
      case LT :                          	// <
      case LE :                          	// <=
      case GT :                          	// >
      case GE :                         	// >=
      case EQ :                         	// ==
      case NE :                         	// !=
        op1 = token.lexeme();
	getToken ();
        expr1 = expr ();                           	// Expr
	break;
      default : 
        ErrorMessage . print (lexer . position (), "UNRECOGNIZABLE SYMBOL");
        break;
    }
    
    expr = new CondExpr(expr, expr1, op, op1);
    return expr;
  }
  
  // Expr ::= MulExpr {AddOper MulExpr}
  // AddOper ::= + | âˆ’

  public Expression expr () throws IOException {
    Expression expr, expr1 = null;
    String op = null;
    expr = mulExpr ();                                 	// MulExpr
    while (token . symbol () == Symbol . PLUS   	// { +
	|| token . symbol () == Symbol . MINUS) {      	//   -
      op = token.lexeme();
      getToken ();
      expr1 = mulExpr ();                                 	// MulExpr }
    }
    if (op != null)
    	expr = new Binary (op, expr,expr1);
    return expr;
  }

  // MulExpr ::= PrefixExpr {MulOper PrefixExpr}
  // MulOper ::= * | /

  public Expression  mulExpr () throws IOException {
    Expression expr,expr1 = null; 
    String op = null;
    expr = prefixExpr ();                               	// PrefixExpr
    while (token . symbol () == Symbol . TIMES  	// { *
        || token . symbol () == Symbol . SLASH) {  	//   /
      op = token.lexeme();
      getToken ();
      if (expr == null)
      	expr = prefixExpr ();      // PrefixExpr }
      else
	      expr1 = prefixExpr();
    }
    if(expr1 != null && op != null)
      expr = new Binary(op,expr,expr1);
	   
    return expr;
  }

  // PrefixExpr ::= [AddOper] SimpleExpr
  // SimpleExpr ::= integer | ( Expr ) | as.integer ( readline ( ) ) 
  //   | id [ ( [Expr {, Expr}] ) ] | cons ( Expr , Expr ) | head ( Expr ) 
  //   | tail ( Expr ) | null ( )

  public Expression prefixExpr () throws IOException {
    Expression expr = null, expr1 = null, expr2 = null;
    String op = null;

    if (token . symbol () == Symbol . PLUS     	// [ +
        || token . symbol () == Symbol . MINUS){ //   - ]
        op = token.lexeme();
        	
	getToken ();
    }

    switch (token . symbol ()) {

      case INTEGER :	      // integer
        expr = new IntValue(Integer.parseInt(token.lexeme()));
	getToken ();
        break;

      case LPAREN :                                  	// (
        getToken ();

        expr = expr ();                                	// Expr
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        getToken ();
        break;

      case ASINTEGER :                            	// as.integer
        getToken ();
        if (token . symbol () != Symbol . LPAREN)  	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . READLINE) 	// readline
          ErrorMessage . print (lexer . position (), "readline EXPECTED");
        
	expr = new AsintExpr();
	getToken ();
        if (token . symbol () != Symbol . LPAREN)  	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        getToken ();
        break;

      case ID :                                 	// id
        expr = new Identifier (token.lexeme());
	getToken ();
        if (token . symbol () == Symbol . LPAREN) { 	// [ (
	  getToken ();
          if (token . symbol () != Symbol . RPAREN) { 	// [
            if (expr == null)
      	    	expr = expr ();	    // Expr
	    else 
		expr1 = expr();
            while (token . symbol () == Symbol . COMMA) { // { ,
              getToken ();
	      if (expr == null)
              	expr1 = expr ();	      // Expr }
	      else
		expr2 = expr();
            }                                    	// ]
            if (token . symbol () != Symbol . RPAREN) 	// )
              ErrorMessage . print (lexer . position (), ") EXPECTED");
          }	  // ]
	  if (expr != null && expr1 != null && expr2 != null)
         	expr = new IDExpr(expr,expr1,expr2);

	  getToken ();
        }
        break;

      case CONS :                               	// cons
        getToken ();
        if (token . symbol () != Symbol . LPAREN)  	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        expr1 = expr ();                                 	// Expr
        if (token . symbol () != Symbol . COMMA)   	// ,
          ErrorMessage . print (lexer . position (), ", EXPECTED");
        getToken ();
        expr2 = expr ();                                 	// Expr
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        expr = new ConsExpr (expr1, expr2);
	getToken ();
        break;

      case HEAD :                               	// head
        getToken ();
        if (token . symbol () != Symbol . LPAREN) 	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        expr1 = expr ();                                	// Expr
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        expr = new HeadExpr(expr1);
	getToken ();
        break;

      case TAIL :                                	// tail
        getToken ();
        if (token . symbol () != Symbol . LPAREN) 	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        expr1 = expr ();                                	// Expr
        if (token . symbol () != Symbol . RPAREN)  	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        expr = new TailExpr(expr1);
	getToken ();
        break;

      case NULL :                               	// null
        getToken ();
        if (token . symbol () != Symbol . LPAREN) 	// (
          ErrorMessage . print (lexer . position (), "( EXPECTED");
        getToken ();
        if (token . symbol () != Symbol . RPAREN) 	// )
          ErrorMessage . print (lexer . position (), ") EXPECTED");
        expr = new NullExpr();
	getToken ();
        break;

      default :
        ErrorMessage . print (lexer . position (), "UNRECOGNIZABLE SYMBOL");
    }
    expr = new Unary (op,expr);
    return expr;
  }

}
