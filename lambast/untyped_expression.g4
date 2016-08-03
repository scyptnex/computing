grammar untyped_expression;

start
    : expr EOF              # nonempty
    | EOF                   # empty
    ;

expr
    :   lhs=expr rhs=term   # application
    |   term                # unit
    ;

term
    :   var                 # variable
    |   '\\' var+ '.' term  # function
    |   '(' expr ')'        # subexpression
    ;

var
    : ID
    ;

ID  :   [a-zA-Z0-9_+*/=&|!]+ ; // identifiers
WS  :   [ \t\n\r]+ -> skip ; // toss out whitespace
