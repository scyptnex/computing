%{

#undef YY_DECL
#define YY_DECL yy::Parser::symbol_type yylex(void)

#define yyterminate() yy::Parser::make_END()

#include "grammar_parse.hpp"
#include <iostream>
using namespace std;
%}

%option noyywrap

%%

"%".*$   { }
"@"      { return yy::Parser::make_ENTRY(); }
"{"      { return yy::Parser::make_L_BRACE(); }
"}"      { return yy::Parser::make_R_BRACE(); }
"="      { return yy::Parser::make_EQUALS(); }
","      { return yy::Parser::make_COMMA();}
<<EOF>>  { return yyterminate(); }
[ \t\n]+ { }
.        { cerr << "Unrecognized token!: " << yytext << endl; exit(1); }
%%
