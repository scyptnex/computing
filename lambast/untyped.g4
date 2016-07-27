grammar untyped;

program
    :   command program     # concise
    |   '@' command program # verbose
    |   EOF                 # end
    ;

command
    :   '=' ID expr         # def
    |   '!' expr            # eval
    |   '?'                 # help
    ;

expr
    :   lhs=expr rhs=term   # application
    |   term                # unit
    ;

term
    :   ID                  # variable
    |   '\\' ID+ '.' term   # function
    |   '(' expr ')'        # subexpression
    ;

ID  :   [a-zA-Z0-9_]+ ; // identifiers
WS  :   [ \t\n\r]+ -> skip ; // toss out whitespace
