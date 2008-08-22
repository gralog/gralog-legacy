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
">"	{ return sf.newSymbol(">",FOSym.GT); }
"[" { return sf.newSymbol("[",FOSym.LP); }
"]" { return sf.newSymbol("]",FOSym.RP); }
"("	{ return sf.newSymbol("(",FOSym.LBRACE); }
")"	{ return sf.newSymbol(")",FOSym.RBRACE); }
"{"	{ return sf.newSymbol("{",FOSym.LCB); }
"}"	{ return sf.newSymbol("}",FOSym.RCB); }
"="	{ return sf.newSymbol("=",FOSym.EQ); }
"/"	{ return sf.newSymbol("/",FOSym.SLASH); }
"."	{ return sf.newSymbol(".",FOSym.DOT); }
","	{ return sf.newSymbol(",",FOSym.COMMA); }
"_"	{ return sf.newSymbol("-",FOSym.US); }

"logic"	{ return sf.newSymbol("logic",FOSym.LOGIC); }
"formula"	{ return sf.newSymbol("formula",FOSym.FORMULA); }
"type"	{ return sf.newSymbol("type",FOSym.TYPE); }
"name"	{ return sf.newSymbol("name",FOSym.NAME); }

"\\sub"  { return sf.newSymbol("\\sub",FOSym.SUB); }
"\\lfp"	{ return sf.newSymbol("\\lfp",FOSym.LFP); }
"\\gfp"	{ return sf.newSymbol("\\gfp",FOSym.GFP); }
"\\ifp"	{ return sf.newSymbol("\\ifp",FOSym.IFP); }
"\\dfp"	{ return sf.newSymbol("\\dfp",FOSym.DFP); }
"\\neg"	{ return sf.newSymbol("\\neg",FOSym.NEG); }
"\\and"	{ return sf.newSymbol("\\and",FOSym.WEDGE); }
"\\or"	{ return sf.newSymbol("\\or",FOSym.VEE); }
"\\bot"	{ return sf.newSymbol("\\bot",FOSym.BOT); }
"\\top"	{ return sf.newSymbol("\\top",FOSym.TOP); }
"\\exists"	{ return sf.newSymbol("\\exists",FOSym.EXISTS); }
"\\forall"	{ return sf.newSymbol("\\forall",FOSym.FORALL); }

\"  { string.setLength(0); yybegin(STRING); }
[A-Za-z0-9]+ { string.setLength(0); string.append(yytext()); return sf.newSymbol("simple", FOSym.STRING, string.toString()); }
[ \t\r\n\f] { /* ignore white space. */ }
. { throw new UserException("Illegal character: "+yytext()); }

}


<STRING> {
\"  { yybegin(YYINITIAL); return sf.newSymbol("complex", FOSym.STRING, string.toString()); }
[A-Za-z0-9 ]+ { string.append(yytext()); }
}
