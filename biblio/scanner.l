%{
	#include <iostream>
	#include <cstdlib>
	#include "scanner.h"
	#include "parser.hpp"

	// Original yyterminate() macro returns int. Since we're using Bison 3 variants
	// as tokens, we must redefine it to change type from `int` to `parser::semantic_type`
	#define yyterminate() bib::parser::make_END();
%}

%option nodefault
%option noyywrap
%option c++
%option yyclass="scanner"
%option prefix="bib_"

%%


[^@{}=,"\n\t \\]+[\t\n ]* { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_STRING(yytext); }
\\.[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_ESCAPE (yytext); }
  @[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_ENTRY  (yytext); }
 \{[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_L_BRACE(yytext); }
 \}[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_R_BRACE(yytext); }
  =[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_EQUALS (yytext); }
  ,[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_COMMA  (yytext); }
 \"[\t\n ]*               { /*std::cout << "LX: " << yytext << std::endl;*/ return bib::parser::make_DQUOTE (yytext); }

[\n\t ]     {}
.           { std::cout << "Scanner: unknown character [" << yytext << "]" << std::endl; }
<<EOF>>     { return yyterminate(); }

%%
