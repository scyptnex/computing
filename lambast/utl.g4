grammar utl;

LAMBDA : '\\';
DOT : '.';
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
fragment ID_START : [A-Za-z+\-*/_];
fragment ID_BODY : ID_START | DIGIT;
fragment DIGIT : [0-9];
ID : ID_START ID_BODY*;
NUMBER : DIGIT+ (DOT DIGIT+)?;
WS : [ \t\r\n]+ -> skip;

parse : expr EOF;

expr : variable                     #VariableExpr
     | number                       #ConstantExpr
     | function_def                 #FunctionDefinition
     | expr expr                    #FunctionApplication
     | OPEN_PAREN expr CLOSE_PAREN  #ParenExpr
     ;

function_def : LAMBDA ID DOT expr;
number : NUMBER; 
variable : ID;
