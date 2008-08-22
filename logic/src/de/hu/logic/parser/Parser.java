
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Fri Aug 22 13:33:32 BST 2008
//----------------------------------------------------

package de.hu.logic.parser;

import java_cup.runtime.*;
import de.hu.logic.modal.Formula;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Fri Aug 22 13:33:32 BST 2008
  */
public class Parser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\035\000\002\002\005\000\002\002\004\000\002\004" +
    "\010\000\002\005\006\000\002\003\004\000\002\003\003" +
    "\000\002\006\005\000\002\007\005\000\002\007\010\000" +
    "\002\010\006\000\002\011\003\000\002\011\003\000\002" +
    "\011\003\000\002\011\006\000\002\011\005\000\002\011" +
    "\004\000\002\011\005\000\002\011\005\000\002\011\006" +
    "\000\002\011\006\000\002\011\006\000\002\011\006\000" +
    "\002\012\003\000\002\012\005\000\002\012\005\000\002" +
    "\014\005\000\002\014\003\000\002\013\005\000\002\013" +
    "\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\113\000\004\004\004\001\002\000\004\013\111\001" +
    "\002\000\004\004\010\001\002\000\004\002\007\001\002" +
    "\000\004\002\000\001\002\000\004\014\020\001\002\000" +
    "\026\004\042\006\030\017\036\020\032\021\033\022\034" +
    "\025\041\026\031\030\037\031\035\001\002\000\004\004" +
    "\ufffc\001\002\000\004\004\015\001\002\000\004\002\001" +
    "\001\002\000\006\011\017\014\020\001\002\000\004\004" +
    "\ufffd\001\002\000\004\013\026\001\002\000\006\005\021" +
    "\016\022\001\002\000\026\004\ufffa\006\ufffa\017\ufffa\020" +
    "\ufffa\021\ufffa\022\ufffa\025\ufffa\026\ufffa\030\ufffa\031\ufffa" +
    "\001\002\000\004\010\023\001\002\000\004\031\024\001" +
    "\002\000\004\005\025\001\002\000\026\004\ufff9\006\ufff9" +
    "\017\ufff9\020\ufff9\021\ufff9\022\ufff9\025\ufff9\026\ufff9\030" +
    "\ufff9\031\ufff9\001\002\000\004\005\027\001\002\000\004" +
    "\002\ufffe\001\002\000\006\007\105\031\104\001\002\000" +
    "\026\004\042\006\030\017\036\020\032\021\033\022\034" +
    "\025\041\026\031\030\037\031\035\001\002\000\004\031" +
    "\064\001\002\000\012\004\ufff6\023\ufff6\024\ufff6\027\ufff6" +
    "\001\002\000\012\004\ufff5\023\ufff5\024\ufff5\027\ufff5\001" +
    "\002\000\012\004\ufff7\023\ufff7\024\ufff7\027\ufff7\001\002" +
    "\000\004\031\061\001\002\000\004\026\056\001\002\000" +
    "\004\004\051\001\002\000\026\004\042\006\030\017\036" +
    "\020\032\021\033\022\034\025\041\026\031\030\037\031" +
    "\035\001\002\000\006\005\044\031\043\001\002\000\004" +
    "\005\046\001\002\000\026\004\042\006\030\017\036\020" +
    "\032\021\033\022\034\025\041\026\031\030\037\031\035" +
    "\001\002\000\012\004\ufff1\023\ufff1\024\ufff1\027\ufff1\001" +
    "\002\000\026\004\042\006\030\017\036\020\032\021\033" +
    "\022\034\025\041\026\031\030\037\031\035\001\002\000" +
    "\012\004\uffef\023\uffef\024\uffef\027\uffef\001\002\000\012" +
    "\004\ufff2\023\ufff2\024\ufff2\027\ufff2\001\002\000\004\011" +
    "\053\001\002\000\004\004\ufffb\001\002\000\004\014\054" +
    "\001\002\000\004\005\055\001\002\000\004\004\ufff8\001" +
    "\002\000\004\031\057\001\002\000\004\027\060\001\002" +
    "\000\012\004\ufff4\023\ufff4\024\ufff4\027\ufff4\001\002\000" +
    "\004\012\062\001\002\000\026\004\042\006\030\017\036" +
    "\020\032\021\033\022\034\025\041\026\031\030\037\031" +
    "\035\001\002\000\012\004\uffed\023\uffed\024\uffed\027\uffed" +
    "\001\002\000\004\012\065\001\002\000\026\004\042\006" +
    "\030\017\036\020\032\021\033\022\034\025\041\026\031" +
    "\030\037\031\035\001\002\000\012\004\uffec\023\uffec\024" +
    "\uffec\027\uffec\001\002\000\004\027\103\001\002\000\010" +
    "\023\071\024\072\027\uffeb\001\002\000\026\004\042\006" +
    "\030\017\036\020\032\021\033\022\034\025\041\026\031" +
    "\030\037\031\035\001\002\000\026\004\042\006\030\017" +
    "\036\020\032\021\033\022\034\025\041\026\031\030\037" +
    "\031\035\001\002\000\006\024\075\027\uffe5\001\002\000" +
    "\004\027\uffe9\001\002\000\026\004\042\006\030\017\036" +
    "\020\032\021\033\022\034\025\041\026\031\030\037\031" +
    "\035\001\002\000\004\027\uffe6\001\002\000\006\023\101" +
    "\027\uffe7\001\002\000\004\027\uffea\001\002\000\026\004" +
    "\042\006\030\017\036\020\032\021\033\022\034\025\041" +
    "\026\031\030\037\031\035\001\002\000\004\027\uffe8\001" +
    "\002\000\012\004\ufff3\023\ufff3\024\ufff3\027\ufff3\001\002" +
    "\000\004\007\107\001\002\000\026\004\042\006\030\017" +
    "\036\020\032\021\033\022\034\025\041\026\031\030\037" +
    "\031\035\001\002\000\012\004\ufff0\023\ufff0\024\ufff0\027" +
    "\ufff0\001\002\000\026\004\042\006\030\017\036\020\032" +
    "\021\033\022\034\025\041\026\031\030\037\031\035\001" +
    "\002\000\012\004\uffee\023\uffee\024\uffee\027\uffee\001\002" +
    "\000\004\015\112\001\002\000\004\010\113\001\002\000" +
    "\004\031\114\001\002\000\004\005\115\001\002\000\004" +
    "\004\uffff\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\113\000\006\002\005\004\004\001\001\000\002\001" +
    "\001\000\010\003\012\006\011\007\010\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\004\011" +
    "\037\001\001\000\002\001\001\000\010\005\013\006\015" +
    "\007\010\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\006\011\067\012\066\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\004\010" +
    "\051\001\001\000\004\011\047\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\011\044\001\001\000\002\001" +
    "\001\000\004\011\046\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\011\062\001\001\000\002\001\001\000\002\001\001" +
    "\000\004\011\065\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\006\011\076\014\077\001\001" +
    "\000\006\011\072\013\073\001\001\000\002\001\001\000" +
    "\002\001\001\000\006\011\072\013\075\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\006\011" +
    "\076\014\101\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\011\105\001\001\000\002\001" +
    "\001\000\004\011\107\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}




	FormulaList flist = new FormulaList();	
	String errorMsg = null;

	public static void main(String args[]) throws Exception {
		SymbolFactory sf = new DefaultSymbolFactory();
		if (args.length==0) new Parser(new Scanner(System.in,sf),sf).parse();
		else new Parser(new Scanner(new java.io.FileInputStream(args[0]),sf),sf).parse();
	}
	
		public Boolean hasError()
	{
	  return errorMsg == null;
	}
	
	public String getErrorMsg()
	{
	   if(errorMsg == null)
	      return "no error";
	   else
	      return errorMsg;
	}
	
	 public void syntax_error(Symbol cur_token)
	 {
	   if(errorMsg == null)
	       errorMsg = "Syntax Error: " + cur_token.toString();
	 }

	 public void report_fatal_error(String message, Object info) throws Exception
	 {
	   throw new Exception("Fatal parsing error: " + message + "\n" + info.toString());
	 }

	 
	

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$Parser$actions {
  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Parser$result;

      /* select the action based on the action number */
      switch (CUP$Parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 28: // veelist ::= formula 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = f; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("veelist",9, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 27: // veelist ::= formula VEE veelist 
            {
              Formula RESULT =null;
		int leftleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int leftright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Formula left = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rightleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rightright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula right = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.or, left, right); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("veelist",9, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 26: // wedgelist ::= formula 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = f; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("wedgelist",10, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 25: // wedgelist ::= formula WEDGE wedgelist 
            {
              Formula RESULT =null;
		int leftleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int leftright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Formula left = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rightleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rightright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula right = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.and, left, right); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("wedgelist",10, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 24: // booleanlist ::= formula VEE veelist 
            {
              Formula RESULT =null;
		int leftleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int leftright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Formula left = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rightleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rightright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula right = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.or, left, right); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("booleanlist",8, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // booleanlist ::= formula WEDGE wedgelist 
            {
              Formula RESULT =null;
		int leftleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int leftright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Formula left = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rightleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rightright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula right = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.and, left, right); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("booleanlist",8, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // booleanlist ::= formula 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = f; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("booleanlist",8, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // formula ::= NU STRING DOT formula 
            {
              Formula RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.nu, s,f);  
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // formula ::= MU STRING DOT formula 
            {
              Formula RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.mu, s,f); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // formula ::= LP STRING RP formula 
            {
              Formula RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.box, s, f); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // formula ::= LT STRING GT formula 
            {
              Formula RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.diamond, s, f); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // formula ::= LP RP formula 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.box, "", f); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // formula ::= LT GT formula 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.diamond, "", f); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // formula ::= NEG formula 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.neg, f); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // formula ::= LBRACE booleanlist RBRACE 
            {
              Formula RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = f; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // formula ::= SUB LBRACE STRING RBRACE 
            {
              Formula RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new Formula(Formula.sub, s); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // formula ::= TOP 
            {
              Formula RESULT =null;
		 RESULT = new Formula(Formula.top, ""); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // formula ::= BOT 
            {
              Formula RESULT =null;
		 RESULT = new Formula(Formula.bottom, ""); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // formula ::= STRING 
            {
              Formula RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new Formula(Formula.proposition, s); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula",7, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // formulatagclose ::= LT SLASH FORMULA GT 
            {
              Object RESULT =null;

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formulatagclose",6, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // formulatagopen ::= LT FORMULA NAME EQ STRING GT 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = s; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formulatagopen",5, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // formulatagopen ::= LT FORMULA GT 
            {
              String RESULT =null;
		 RESULT = "main"; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formulatagopen",5, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // formulatag ::= formulatagopen formula formulatagclose 
            {
              FormulaList RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Formula f = (Formula)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 FormulaList fl = new FormulaList(); fl.addFormula(s,f); RESULT = fl; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formulatag",4, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // formula_list ::= formulatag 
            {
              FormulaList RESULT =null;
		int flleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int flright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		FormulaList fl = (FormulaList)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = fl; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula_list",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // formula_list ::= formula_list formulatag 
            {
              FormulaList RESULT =null;
		int flleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int flright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		FormulaList fl = (FormulaList)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		FormulaList f = (FormulaList)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 fl.addAll(f); RESULT = fl; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("formula_list",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // logictagclose ::= LT SLASH LOGIC GT 
            {
              Object RESULT =null;

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("logictagclose",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // logictagopen ::= LT LOGIC TYPE EQ STRING GT 
            {
              String RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = s; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("logictagopen",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= file EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		FormulaList start_val = (FormulaList)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // file ::= logictagopen formula_list logictagclose 
            {
              FormulaList RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int flleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int flright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		FormulaList fl = (FormulaList)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 fl.setType(s); RESULT = fl;  
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("file",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

