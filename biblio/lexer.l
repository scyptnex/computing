%{
#include "parser.tab.hpp"
#include <iostream>
using namespace std;
%}

%option noyywrap

%%

"%".*$   { }
"@"      { return ENTRY; }
"{"      { return L_BRACE; }
"}"      { return R_BRACE; }
"="      { return EQUALS; }
","      { return COMMA;}
<<EOF>>  { return 0; }
[ \t\n]+ { }
.        { cerr << "Unrecognized token!: " << yytext << endl; exit(1); }
%%
