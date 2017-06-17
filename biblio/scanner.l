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


[a-z]+      { 
                cout << "Scanner: identifier [" << yytext << "]" << endl;
                return bib::parser::make_STRING(yytext); 
            }
            
\(          {
                cout << "Scanner: '('" << endl;
                return bib::parser::make_LEFTPAR();
            }
            
\)          { 
                cout << "Scanner: ')'" << endl;
                return bib::parser::make_RIGHTPAR();
            }
            
;           {
                cout << "Scanner: ';'" << endl;
                return bib::parser::make_SEMICOLON();
            }
            
,           {
                cout << "Scanner: ','" << endl;
                return bib::parser::make_COMMA();
            }
            
[\n\t ]     {
                //cout << "Scanner: whitechar (ignored)" << endl;
            }

[1-9][0-9]* {
                cout << "Scanner: decimal number: " << yytext << endl;
                uint64_t number = strtoull(yytext, 0, 10);
                return bib::parser::make_NUMBER(number);
            }

.           { 
                cout << "Scanner: unknown character [" << yytext << "]" << endl; 
            }
            
<<EOF>>     { return yyterminate(); }


%%
