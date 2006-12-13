package de.hu.logic.parser;

import java_cup.runtime.SymbolFactory;
import java.lang.StringBuffer;

%%
%cup

%class Scanner
%{
	public Scanner(java.io.InputStream r, SymbolFactory sf){
		this(r);
		this.sf=sf;
	}
	private SymbolFactory sf;
	private StringBuffer string = new StringBuffer();
%}
%eofval{
    return sf.newSymbol("EOF",Sym.EOF);
%eofval}

%state STRING

%%
<YYINITIAL> 
{
"<"	{ return sf.newSymbol("<",Sym.LT); }
">"	{ return sf.newSymbol("<",Sym.GT); }
"[" { return sf.newSymbol("<",Sym.LP); }
"]" { return sf.newSymbol("<",Sym.RP); }
"("	{ return sf.newSymbol("<",Sym.LBRACE); }
")"	{ return sf.newSymbol("<",Sym.RBRACE); }
"="	{ return sf.newSymbol("<",Sym.EQ); }
"/"	{ return sf.newSymbol("<",Sym.SLASH); }
"."	{ return sf.newSymbol("<",Sym.DOT); }

"logic"	{ return sf.newSymbol("<",Sym.LOGIC); }
"formula"	{ return sf.newSymbol("<",Sym.FORMULA); }
"type"	{ return sf.newSymbol("<",Sym.TYPE); }
"name"	{ return sf.newSymbol("<",Sym.NAME); }

"\\sub"  { return sf.newSymbol("<",Sym.SUB); }
"\\mu"	{ return sf.newSymbol("<",Sym.MU); }
"\\nu"	{ return sf.newSymbol("<",Sym.NU); }
"\\neg"	{ return sf.newSymbol("<",Sym.NEG); }
"\\and"	{ return sf.newSymbol("<",Sym.WEDGE); }
"\\or"	{ return sf.newSymbol("<",Sym.VEE); }
"\\bot"	{ return sf.newSymbol("<",Sym.BOT); }
"\\top"	{ return sf.newSymbol("<",Sym.TOP); }

\"  { string.setLength(0); yybegin(STRING); }
[A-Za-z0-9]+ { string.setLength(0); string.append(yytext()); return sf.newSymbol("simple", Sym.STRING, string.toString()); }
[ \t\r\n\f] { /* ignore white space. */ }
. { System.err.println("Illegal character: "+yytext()); }

}


<STRING> {
\"  { yybegin(YYINITIAL); return sf.newSymbol("complex", Sym.STRING, string.toString()); }
[A-Za-z0-9 ]+ { string.append(yytext()); }
}
