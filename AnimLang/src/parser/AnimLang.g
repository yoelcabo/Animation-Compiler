grammar AnimLang;

options {
    output = AST;
    ASTLabelType = AnimLangTree;
}

// Tokens imaginaris

tokens {
  ATTR; // llista d'atributs d'un objecte (p.e. centre i radi d'un cercle)
  PARAMS; // List of parameters in the declaration of a function
  FUNCALL; // Function call
  ARGLIST; // List of arguments passed in a function call
  COND;       // Token per a condicionals (IF y ELIF)
  LISTFUNC; // llista de funcions del programa
}

@header {
package parser;
import interp.AnimLangTree;
}

@lexer::header {
package parser;
}

// el primer que veu el programa son funcions i dins d'aquestes funcions hi ha les llistes d'instruccions
prog: funcions;

funcions	:	list_func+ -> ^(LISTFUNC list_func);
list_func	:	func+;

list_inst:     inst*;

inst:  if_stmt
    |  for_stmt
    |  while_stmt
    |  assign
    |  funcall
    |  ret
    |  run
    |  func // aixo permet definir funcions dins d'una funcio
    // modificació d'atributs
    ;

//Fer bé
if_stmt: IF expr THEN list_inst remainingIfStmt -> ^(COND expr list_inst remainingIfStmt);
remainingIfStmt: ELIF expr THEN list_inst remainingIfStmt -> ^(COND expr list_inst remainingIfStmt) | (ELSE! list_inst)? ENDIF!;

for_stmt: FOR^ for_exp DO! list_inst ENDFOR!;

for_exp	:	ID IN^ '['! INT ('..'! INT ('..'! INT)?)? ']'!;  // for i in [3] -> i=0, i=1, i=2; for i in [10..14..2] -> i=10, i=12, i=14

while_stmt: WHILE^ expr DO! list_inst ENDWHILE!;

assign: var ASSIGN^ expr SEPARATOR;

expr  : expr_and (OR^ expr_and )*;
expr_and  : expr_not (AND^ expr_not )*;
expr_not  : expr_comp | NOT^ expr_not;
expr_comp : expr_as ((GT^ | LT^ | EQ^ | NE^ | GE^ | LE^) expr_as)?;
expr_as   : expr_par (ASSOC^ (obj | obj_pack | ID))?;
expr_par  : expr_seq (PAR^ expr_seq)*;
expr_seq  : expr_num (SEQ^ expr_num)*;
expr_num  : expr_mult ( (PLUS^ | MINUS^) expr_mult )*;
expr_mult : expr_neg ( (PROD^ | DIV^ | MOD^) expr_neg )*;
expr_neg  : atom | MINUS expr_neg;
atom      : num | mov | ID | STRING | obj | obj_pack | funcall | '('! expr ')'!;

num : INT | FLOAT;

obj_pack   : '{'! (obj | obj_pack | ID) (','! (obj | obj_pack | ID))* '}'!;


obj: (CIRCLE^ | POLYGON^ | POLYLINE^ | TRIANGLE^ | PATH^) attr;

attr: '[' listAttr? ']' -> ^(ATTR listAttr);

listAttr: ID ASSIGN^ (ID | STRING | INT | FLOAT) (','! ID ASSIGN^ (ID | STRING | INT | FLOAT))* ;

// per a un moviment, si no es defineixen les coordenades inicials, tot es fa relatiu a les
// coordenades de l'objecte al qual se li aplica
mov: (TRANSLATE^ | ROTATE^ | SCALE^ | FOLLOWPATH^) attr;


// FUNCTIONS (basicament, copy paste de ASL)
// A function has a name, a list of parameters and a block of instructions  
func  : DEF! ID params list_inst ENDFUNC!;

// The list of parameters grouped in a subtree (it can be empty)
params  : '(' paramlist? ')' -> ^(PARAMS paramlist?);

// Parameters are separated by commas
paramlist: ID (','! ID)*;

// A function call has a lits of arguments in parenthesis (possibly empty)
funcall :   ID '(' expr_list? ')' -> ^(FUNCALL ID ^(ARGLIST expr_list?));

// A list of expressions separated by commas
expr_list:  expr (','! expr)*;

ret: RETURN^ expr SEPARATOR!;


var: ID ('.'^ var)?;

run	:	 RUN;


ASSIGN    :  '=';
ASSOC     :  '->';

SEQ       :  '&';
PAR       :  '|';

FOR       :  'for';
IN	:	'in';
DO        :  'do';
ENDFOR    :  'endfor';

WHILE     :  'while';
ENDWHILE  :  'endwhile';

IF        : 'if';
THEN      : 'then';
ELSE      : 'else';
ELIF      : 'elif';
ENDIF     : 'endif';

DEF       : 'def'; // notacio Python funcions
ENDFUNC	:	'endfunc';
RETURN    : 'return';

RUN       :  'run';

TRANSLATE : 'Translate';
SCALE     : 'Scale';
ROTATE    : 'Rotate';

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


//Arithmetic

PLUS      : '+';
MINUS     : '-';
DIV       : '/';
PROD      : '*';
MOD       : '%';


//Atributs dels objectes


FLOAT   : '0'..'9'+ '.' '0'..'9'+;
INT     : '0'..'9'+;
ID      :  ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

// Strings (in quotes) with escape sequences
// He hagut de posar les cometes amb la barra (\) al davant perque si no em donava
// error l'interpret de l'antlrWorks. Tambe he canviat l'expressio d'abans perque impedia
// que es construis be l'arbre de sintaxi
STRING  :  '\"' ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* '\"'
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

