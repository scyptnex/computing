%skeleton "lalr1.cc" /* -*- C++ -*- */
%require "3.0"

%defines
%define parser_class_name { parser }
%define api.token.constructor
%define api.value.type variant
%define parse.assert
%define api.namespace { bib }

%code requires
{

    // According to http://www.jonathanbeard.io/tutorials/FlexBisonC++
    // The following definitions are missing when %locations isn't used
    # ifndef YY_NULLPTR
    #  if defined __cplusplus && 201103L <= __cplusplus
    #   define YY_NULLPTR nullptr
    #  else
    #   define YY_NULLPTR 0
    #  endif
    # endif

    #include <iostream>
    #include <string>
    #include <vector>
    #include "bibliography.h"

    namespace bib {
        class scanner;
    }
}

// Bison calls yylex() function that must be provided by us to suck tokens
// from the scanner. This block will be placed at the beginning of IMPLEMENTATION file (cpp).
// We define this function here (function! not method).
// This function is called only inside Bison, so we make it static to limit symbol visibility for the linker
// to avoid potential linking conflicts.
%code top
{
    #include <iostream>
    #include "scanner.h"
    #include "parser.hpp"
    
    // yylex() arguments are defined in parser.y
    static bib::parser::symbol_type yylex(bib::scanner& scanner) {
        return scanner.get_next_token();
    }
}

%lex-param { bib::scanner &scanner }
%parse-param { bib::scanner &scanner }
%parse-param { bib::bibliography &result }

%define api.token.prefix {TOKEN_}

%token <std::string> STRING "string"
%token <std::string> ESCAPE "escape sequence"
%token <std::string> NAME   "named identifier"
%token <std::string> QTEXT  "quoted text"
%token <std::string> BTEXT  "bracketed text"
%token <std::string> ENTRY       "@"
%token <std::string> L_BRACE     "{"
%token <std::string> R_BRACE     "}"
%token <std::string> COMMA       ","
%token <std::string> EQUALS      "="
%token <std::string> DQUOTE      "\""
%token END 0 "end of file"

%type <bib::bibliography>         start
%type <bib::bibliography>         bibliography
%type <bib::entry>                entry
%type <bib::element>              element
%type <std::vector<bib::element>> element_list
%type <std::string>               text
%type <std::string>               ltext

%start start

%%

start : bibliography END
      ;

bibliography : %empty
               { /*do nothing*/ }
             | bibliography entry
               { result.entries.push_back($2); }
             ;

entry : ENTRY STRING L_BRACE STRING element_list R_BRACE
        { $$ = bib::entry($2, $4); $$.elements = $5; }
      ;

element_list : %empty
               { $$ = std::vector<bib::element>(); }
             | element_list COMMA element
               { $1.push_back($3); $$ = $1; }
             | element_list COMMA
               { $$ = $1; }
             ;

element : STRING EQUALS text
          { $$ = bib::element($1, $2.substr(1) + $3); }
        ;

text : STRING
       { $$ = $1; }
     | DQUOTE ltext DQUOTE
       { $$ = $1 + $2 + $3; }
     | L_BRACE ltext R_BRACE
       { $$ = $1 + $2 + $3; }
     ;

ltext : %empty
        { $$ = ""; }
      | ltext text
        { $$ = $1 + $2; }
      | ltext ESCAPE
        { $$ = $1 + $2; }
      | ltext ENTRY
        { $$ = $1 + $2; }
      | ltext EQUALS
        { $$ = $1 + $2; }
      | ltext COMMA
        { $$ = $1 + $2; }
      ;

%%

void bib::parser::error(std::string const& err){
    std::cout << "Error: " << err << std::endl;
}
