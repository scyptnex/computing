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
    #include <stdint.h>

    using namespace std;

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

%token END 0 "end of file"
%token <std::string> STRING  "string";
%token <uint64_t> NUMBER "number";
%token LEFTPAR "leftpar";
%token RIGHTPAR "rightpar";
%token SEMICOLON "semicolon";
%token COMMA "comma";

%type <std::string> command;
%type <std::vector<uint64_t>> arguments;

%start program

%%

program :   {
                cout << "*** RUN ***" << endl;
                cout << "Type function with list of parmeters. Parameter list can be empty" << endl
                     << "or contain positive integers only. Examples: " << endl
                     << " * function()" << endl
                     << " * function(1,2,3)" << endl
                     << "Terminate listing with ; to see parsed AST" << endl
                     << "Terminate parser with Ctrl-D" << endl;
                
                cout << endl << "prompt> ";
            }
        | program command
            {
                cout << "command parsed, updating AST" << endl;
                cout << endl << "prompt> ";
            }
        | program SEMICOLON
            {
                cout << "*** STOP RUN ***" << endl;
            }
        ;


command : STRING LEFTPAR RIGHTPAR
        {
            cout << "cmd" << endl;
            $$ =  "hello";
        }
    | STRING LEFTPAR arguments RIGHTPAR
        {
            string &id = $1;
            const std::vector<uint64_t> &args = $3;
            cout << "function: " << id << ", " << args.size() << endl;
            $$ = "world";
        }
    ;

arguments : NUMBER
        {
            uint64_t number = $1;
            $$ = std::vector<uint64_t>();
            $$.push_back(number);
            cout << "first argument: " << number << endl;
        }
    | arguments COMMA NUMBER
        {
            uint64_t number = $3;
            std::vector<uint64_t> &args = $1;
            args.push_back(number);
            $$ = args;
            cout << "next argument: " << number << ", arg list size = " << args.size() << endl;
        }
    ;
    
%%

void bib::parser::error(std::string const& err){
    std::cout << "Error: " << err << std::endl;
}

// Bison expects us to provide implementation - otherwise linker complains
//void bib::parser::error(const bib::parser::syntax_error& er) {
 //       cout << "Error: " << er << endl;
//}
