

program: list_inst;

list_inst^: 	inst*;

inst:  if_stmt
    |  for_stmt
    |  while_stmt
    |  assign
    |  funcall
    |  run
    // modificació d'atributs
    ;

//Fer bé
if_stmt: IF^ boolExp THEN! list_inst (ELIF^ boolExp THEN! list_inst)* (ELSE! list_inst)? ENDIF!;

for_stmt: FOR^ for_exp DO list_inst ENDFOR;

while_stmt: WHILE^ boolExp DO list_inst ENDWHILE;

assign: var ASSIGN^ expr;



expr     : STRING | obj_pack | obj | expr_ac ;

expr_ac    : expr_par (ASSOC^ (obj | obj_pack | ID))?;
expr_par  : expr_seq (PAR^ expr_seq)*;
expr_seq  : expr_simp (SEQ^ expr_simp)*;
expr_simp :
          | mov
          | ID
          | expr_num
          ;
expr_num   : expr_mult ( (SUM^ | DIF^) expr_mult )*;
expr_mult   : num_atom ( (PROD^ | DIV^) num_atom )*;
num_atom    : num | '('! expr_ac ')'!;


num : INT | FLOAT;

obj_pack   : '{'! (obj | obj_pack | ID) (','! (obj | obj_pack | ID))* '}'!;


obj: (CIRCLE | POLYGON | POLYLINE | TRIANGLE | PATH) attr

attr: ID ASSIGN^ (ID | STRING 

mov:



var: ID ('.'^ var)?;

art: //llista de paraules clau

ASSIGN    :  '=';
ASSOC     :  '->';

SEQ       :  '&';
PAR       :  '|';

FOR       :  'for';
DO        :  'do';
ENDFOR    :  'endfor';

WHILE     :  'while';
ENDWHILE  :  'endwhile';

IF        : 'if';
THEN      : 'then';
ELSE      : 'else';
ELIF      : 'elif';
ENDIF     : 'endif';

RUN       :  'run';

TRANSLATE : 'translate';
SCALE     : 'scale';
ROTATE    : 'rotate';

//Objects
CIRCLE    : 'Circle';
POLYGON   : 'Polygon';
POLYLINE  : 'Polyline';
PATH      : 'Path';
TRIANGLE  : 'Triangle';

FOLLOWPATH: 'FollowPath'; 

SEPARATOR: ';';

//Boolean
AND       : 'and';
OR        : 'or';
NOT       : 'not';

GT        : '>';
GE        : '>=';
LT        : '<';
LE        : '<=';
EQ        : '==';
NE        : '!=';

//Atributs dels objectes


INT     : '0'..'9'+;
FLOAT   : '0'..'9'+ '.' '0'..'9'+;
ID      :       ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

// Strings (in quotes) with escape sequences        
STRING  :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
        ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    ;

// White spaces
WS      : ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
        ;

