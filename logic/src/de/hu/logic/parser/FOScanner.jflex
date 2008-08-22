package de.hu.logic.parser;

import java_cup.runtime.SymbolFactory;
import java.lang.StringBuffer;
import de.hu.gralog.app.UserException;


%%
%cup

%class FOScanner
%{
	public FOScanner(java.io.InputStream r, SymbolFactory sf){
		this(r);
		this.sf=sf;
	}
	private SymbolFactory sf;
	private StringBuffer string = new StringBuffer();
%}
%eofval{
    return sf.newSymbol("EOF",FOSym.EOF);
%eofval}

%state STRING

%%
<YYINITIAL> 
{
"<"	{ return sf.newSymbol("<",FOSym.LT); }
">"	{ return sf.newSymbol("<",FOSym.GT); }
"[" { return sf.newSymbol("<",FOSym.LP); }
"]" { return sf.newSymbol("<",FOSym.RP); }
"("	{ return sf.newSymbol("<",FOSym.LBRACE); }
")"	{ return sf.newSymbol("<",FOSym.RBRACE); }
"="	{ return sf.newSymbol("<",FOSym.EQ); }
"/"	{ return sf.newSymbol("<",FOSym.SLASH); }
"."	{ return sf.newSymbol("<",FOSym.DOT); }
","	{ return sf.newSymbol("<",FOSym.COMMA); }

"logic"	{ return sf.newSymbol("<",FOSym.LOGIC); }
"formula"	{ return sf.newSymbol("<",FOSym.FORMULA); }
"type"	{ return sf.newSymbol("<",FOSym.TYPE); }
"name"	{ return sf.newSymbol("<",FOSym.NAME); }

"\\sub"  { return sf.newSymbol("<",FOSym.SUB); }
"\\lfp"	{ return sf.newSymbol("<",FOSym.LFP); }
"\\gfp"	{ return sf.newSymbol("<",FOSym.GFP); }
"\\ifp"	{ return sf.newSymbol("<",FOSym.LFP); }
"\\dfp"	{ return sf.newSymbol("<",FOSym.GFP); }
"\\neg"	{ return sf.newSymbol("<",FOSym.NEG); }
"\\and"	{ return sf.newSymbol("<",FOSym.WEDGE); }
"\\or"	{ return sf.newSymbol("<",FOSym.VEE); }
"\\bot"	{ return sf.newSymbol("<",FOSym.BOT); }
"\\top"	{ return sf.newSymbol("<",FOSym.TOP); }
"\\exists"	{ return sf.newSymbol("<",FOSym.EXISTS); }
"\\forall"	{ return sf.newSymbol("<",FOSym.FORALL); }

\"  { string.setLength(0); yybegin(STRING); }
[A-Za-z0-9]+ { string.setLength(0); string.append(yytext()); return sf.newSymbol("simple", FOSym.STRING, string.toString()); }
[ \t\r\n\f] { /* ignore white space. */ }
. { throw new UserException("Illegal character: "+yytext()); }

}


<STRING> {
\"  { yybegin(YYINITIAL); return sf.newSymbol("complex", FOSym.STRING, string.toString()); }
[A-Za-z0-9 ]+ { string.append(yytext()); }
}
