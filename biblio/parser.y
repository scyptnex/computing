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

%define api.token.prefix {TOKEN_}

%token <std::string> STRING "string"
%token ENTRY_START "@"
%token L_BRACE     "{"
%token R_BRACE     "}"
%token COMMA       ","
%token EQUALS      "="
%token END 0 "end of file"

%type <bib::bibliography>         bibliography
%type <bib::entry>                entry
%type <bib::element>              element
%type <std::vector<bib::element>> element_list

%start start

%%

start : bibliography END
        { std::cout << $1 << std::endl; }

bibliography : %empty
               { $$ = bib::bibliography(); }
             | bibliography entry
               { $1.entries.push_back($2); $$ = $1; }
             ;

entry : ENTRY_START STRING L_BRACE STRING element_list R_BRACE
        { $$ = bib::entry($2, $4); $$.elements = $5; }
      ;

element_list : %empty
               { $$ = std::vector<bib::element>(); }
             | element_list COMMA element
               { $1.push_back($3); $$ = $1; }
             ;

element : STRING EQUALS STRING
          { $$ = bib::element($1, $3); }
        ;
%%

void bib::parser::error(std::string const& err){
    std::cout << "Error: " << err << std::endl;
}
