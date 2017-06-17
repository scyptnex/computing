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


[a-z]+      { return bib::parser::make_STRING(yytext); }
@           { return bib::parser::make_ENTRY_START(); }
\{          { return bib::parser::make_L_BRACE(); }
\}          { return bib::parser::make_R_BRACE(); }
=           { return bib::parser::make_EQUALS(); }
,           { return bib::parser::make_COMMA(); }

[\n\t ]     {}
.           { std::cout << "Scanner: unknown character [" << yytext << "]" << std::endl; }
<<EOF>>     { return yyterminate(); }


%%
