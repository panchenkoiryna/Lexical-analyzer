lexer grammar Hello;

IF      : 'if';
ELSE    : 'else';
WHILE   : 'while';
DEF     : 'def';
CLASS   : 'class';
RETURN  : 'return';

PLUS    : '+';
MINUS   : '-';
MULT    : '*';
DIV     : '/';
EQ      : '=';

NUMBER  : [0-9]+;
ID      : [a-zA-Z]+;

STRING  : '"' (~["\r\n])* '"';

WS      : [ \t\r\n]+ -> skip;

ERROR_CHAR : . ;