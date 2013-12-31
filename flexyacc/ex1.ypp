/********************************************************
 * 
 ********************************************************/
%{
#include <iostream>
#include <string>
#include <map>
#include <cstdlib> //-- I need this for atoi
using namespace std;

//-- Lexer prototype required by bison, aka getNextToken()
int yylex(); 
int yyerror(const char *p) { cerr << "Parse error!" << endl; }
%}

//-- SYMBOL SEMANTIC VALUES -----------------------------
//-- In bison, every symbol, whether it be token or non-terminal
//-- can have a "semantic value".  A NUM token "4.35", has
//-- semantic value 4.35.  A "term" symbol representing 
//-- "-3.5*2*5" has semantic value 35.0, which you get by
//-- evaluating -3.5*2*5.  An OPA symbol's semantic value
//-- is '+' or '-' depending on which symbol we actually read.
//-- Some tokens, like STOP or RP don't need a semantic value.
//-- The %union statement lists the type-and-name for each
//-- semantic value.  The %token and %type statements let you
//-- you specify semantic value types for tokens and 
//-- non-terminals.
%union {
  int val; 
  char sym;
};
%token <val> NUM
%token <sym> OPA OPM LP RP STOP
%type  <val> exp term sfactor factor res

//-- GRAMMAR RULES ---------------------------------------
%%
run: res run | res    /* forces bison to process many stmts */

res: exp STOP         { cout << $1 << endl; }

exp: exp OPA term     { $$ = ($2 == '+' ? $1 + $3 : $1 - $3); }
| term                { $$ = $1; }

term: term OPM factor { $$ = ($2 == '*' ? $1 * $3 : $1 / $3); }
| sfactor             { $$ = $1; }

sfactor: OPA factor   { $$ = ($1 == '+' ? $2 : -$2); }
| factor              { $$ = $1; }

factor: NUM           { $$ = $1; }
| LP exp RP           { $$ = $2; }

%%
//-- FUNCTION DEFINITIONS ---------------------------------
int main()
{
  yyparse();
  return 0;
}

