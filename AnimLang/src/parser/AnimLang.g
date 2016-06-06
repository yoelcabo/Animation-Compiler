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
  LISTINST; // llista de instruccions de la funcio
  PVALUE;     // Parameter by value in the list of parameters
  PREF;       // Parameter by reference in the list of parameters
  MOV;    // moviment pur
  OBJ;    // objecte pur
  OBJ_PACK; // pack d'objectes
}

@header {
package parser;
import interp.AnimLangTree;
}

@lexer::header {
package parser;
}

// el primer que veu el programa son funcions i dins d'aquestes funcions hi ha les llistes d'instruccions
prog: func+ EOF-> ^(LISTFUNC func+);

list_inst:     inst* -> ^(LISTINST inst*);

inst:  if_stmt
    |  for_stmt
    |  while_stmt
    |  assign
    |  funcall
    |  ret
    |  run
    |  print_stmt
    // modificació d'atributs
    ;

//Fer bé
// un if sol peta quan es crea l'arbre. Si l'acompanyes d'un else ja funciona
if_stmt: IF expr THEN list_inst remainingIfStmt -> ^(COND expr list_inst remainingIfStmt);
remainingIfStmt: ELIF expr THEN list_inst remainingIfStmt -> ^(COND expr list_inst remainingIfStmt) | (ELSE! list_inst)? ENDIF!;

for_stmt: FOR^ for_exp DO! list_inst ENDFOR!;

for_exp	:	ID IN^ '['! INT ('..'! INT ('..'! INT)?)? ']'!;  // for i in [3] -> i=0, i=1, i=2; for i in [10..14..2] -> i=10, i=12, i=14

while_stmt: WHILE^ expr DO! list_inst ENDWHILE!;

assign: var ASSIGN^ expr SEPARATOR!;

expr  : expr_and (OR^ expr_and )*;
expr_and  : expr_not (AND^ expr_not )*;
expr_not  : expr_comp | NOT^ expr_not;
expr_comp : expr_as ((GT^ | LT^ | EQ^ | NE^ | GE^ | LE^) expr_as)?;
expr_as   : expr_par (ASSOC^ (obj | obj_pack | ID))?;
expr_par  : expr_seq (PAR^ expr_seq)*;
expr_seq  : expr_num (SEQ^ expr_num)*;
expr_num  : expr_mult ( (PLUS^ | MINUS^) expr_mult )*;
expr_mult : expr_neg ( (PROD^ | DIV^ | MOD^) expr_neg )*;
expr_neg  : atom | MINUS^ expr_neg;
atom      : num | mov | ID | STRING | BOOLEAN | obj | obj_pack | funcall | '('! expr ')'!;
// ID hauria de ser var, per poder fer cercle1.color = cercle2.color

num : INT | FLOAT;

// crear token OBJ_PACK, que sera l'arrel, i afegir-li la llista d'objectes, com a fills, que formen el pack
obj_pack   : list_obj_pack -> ^(OBJ_PACK list_obj_pack);

list_obj_pack: '{'! (obj | obj_pack | ID) (','! (obj | obj_pack | ID))* '}'!;

// tambe afegir OBJ com a token arrel
obj: typeObj attr -> ^(OBJ typeObj attr);

typeObj: (CIRCLE | POLYGON | POLYLINE | TRIANGLE | PATH | RECTANGLE | REGULARPOLYGON | TEXTOBJECT);

attr: '[' listAttr? ']' -> ^(ATTR listAttr);

listAttr: one_attr  (','! one_attr)* ;

one_attr: ID ASSIGN^ expr;

// per a un moviment, si no es defineixen les coordenades inicials, tot es fa relatiu a les
// coordenades de l'objecte al qual se li aplica
// tambe afegir MOV com a token arrel
mov: typeMov attr -> ^(MOV typeMov attr);

typeMov: (TRANSLATE | ROTATE | SCALE | FOLLOWPATH);

print_stmt: PRINT^ expr SEPARATOR!;


// FUNCTIONS (basicament, copy paste de ASL)
// A function has a name, a list of parameters and a block of instructions  
func  : DEF^ ID params list_inst ENDFUNC!;

// The list of parameters grouped in a subtree (it can be empty)
params  : '(' paramlist? ')' -> ^(PARAMS paramlist?);

// Parameters are separated by commas
paramlist: param (','! param)*
        ;

// Parameters with & as prefix are passed by reference
// Only one node with the name of the parameter is created
param   :   '&' id=ID -> ^(PREF[$id,$id.text])
        |   id=ID -> ^(PVALUE[$id,$id.text])
        ;

// A function call has a lits of arguments in parenthesis (possibly empty)
funcall :   ID '(' expr_list? ')' -> ^(FUNCALL ID ^(ARGLIST expr_list?));

// A list of expressions separated by commas
expr_list:  expr (','! expr)*;

ret: RETURN^ expr SEPARATOR!;


var: ID ('.'^ var)?;

run	:	 RUN^ expr ','! expr ','! expr SEPARATOR!;


ASSIGN    :  '=';
ASSOC     :  '->';

SEQ       :  '&';
PAR       :  '|';

FOR       :  'for';
IN	  :  'in';
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
ENDFUNC	  : 'endfunc';
RETURN    : 'return';

PRINT     : 'print';

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
RECTANGLE  : 'Rectangle';
REGULARPOLYGON   : 'RPolygon';
TEXTOBJECT   : 'Text';

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
BOOLEAN : 'True' | 'False';
ID      :  ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

// Strings (in quotes) with escape sequences
// He hagut de posar les cometes amb la barra (\) al davant perque si no em donava
// error l'interpret de l'antlrWorks. Tambe he canviat l'expressio d'abans perque impedia
// que es construis be l'arbre de sintaxi
STRING  :  '\"' ('a'..'z'|'A'..'Z'|'0'..'9'|'_' | ':' | ' ' )* '\"'
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

