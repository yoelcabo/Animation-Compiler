grammar Animation;

options {
    output = AST;
    ASTLabelType = AnimationTree;
}

// Tokens imaginaris

tokens {
  ATTR; // llista d'atributs d'un objecte (p.e. centre i radi d'un cercle)
  PARAMS; // List of parameters in the declaration of a function
  FUNCALL; // Function call
  ARGLIST; // List of arguments passed in a function call
  COND;       // Token per a condicionals (IF y ELIF)
}

@header {
package parser;
import interp.AnimationTree;
}

@lexer::header {
package parser;
}


program: list_inst;

list_inst^:     inst*;

inst:  if_stmt
    |  for_stmt
    |  while_stmt
    |  assign
    |  funcall
    |  run
    // modificació d'atributs
    ;

//Fer bé
if_stmt: IF expr THEN list_inst remainingIfStmt -> ^(COND expr list_inst remainningIfStmt)
remainingIfStmt: ELIF expr THEN list_inst remainingIfStmt -> ^(COND expr list_inst remainningIfStmt) | (ELSE! list_inst)? ENDIF!;

for_stmt: FOR^ for_exp DO! list_inst ENDFOR!;

while_stmt: WHILE^ expr DO! list_inst ENDWHILE!;

assign: var ASSIGN^ expr SEPARATOR;

expr  : expr_and (OR^ expr_and )*;
expr_and  : expr_not (AND^ expr_not )*;
expr_not  : expr_comp | NOT^ expr_not;
expr_comp : expr_as ((GT^ | LT^ | EQ^ | NE^ | GE^ | LE^) expr_as)?;
expr_as   : expr_par (ASSOC^ (obj | obj_pack | ID))?;
expr_par  : expr_seq (PAR^ expr_seq)*;
expr_seq  : expr_num (SEQ^ expr_num)*;
expr_num  : expr_mult ( (SUM^ | DIF^) expr_mult )*;
expr_mult : expr_neg ( (PROD^ | DIV^) expr_neg )*;
expr_neg  : atom | NEG expr_neg;
atom      : num | mov | ID | STRING | obj | obj_pack | '('! expr ')'!;

num : INT | FLOAT;

obj_pack   : '{'! (obj | obj_pack | ID) (','! (obj | obj_pack | ID))* '}'!;


obj: (CIRCLE^ | POLYGON^ | POLYLINE^ | TRIANGLE^ | PATH^) attr;

attr: '['! listAttr ']'! -> ^(ATTR listAttr);

listAttr: (ID ASSIGN^ (ID | STRING | INT | FLOAT))*;

// per a un moviment, si no es defineixen les coordenades inicials, tot es fa relatiu a les
// coordenades de l'objecte al qual se li aplica
movEncadenat: mov ((PAR^ | SEQ^) mov)*;
// obj -> path; attr -> velocitat, durada...; 
// (INT | FLOAT) -> interval d'espera entre dos moviments consecutius
// mov pot ser un moviment pur o un moviment associat a un objecte
mov: INT | FLOAT | (obj attr) | ID | ('('! movEncadenat ')'!);


// FUNCTIONS (basicament, copy paste de ASL)
// A function has a name, a list of parameters and a block of instructions  
func  : DEF^ ID params list_inst ENDFUNC!;

// The list of parameters grouped in a subtree (it can be empty)
params  : '(' paramlist? ')' -> ^(PARAMS paramlist?);

// Parameters are separated by commas
paramlist: ID (','! ID)*;

// A function call has a lits of arguments in parenthesis (possibly empty)
funcall :   ID '(' expr_list? ')' -> ^(FUNCALL ID ^(ARGLIST expr_list?));

// A list of expressions separated by commas
expr_list:  expr (','! expr)*



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

DEF       : 'def'; // notacio Python funcions
RETURN    : 'return';

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


//Arithmetic
NEG       : '-';

SUM       : '+';
DIF       : '-';
DIV       : '/';
PROD      : '*';


//Atributs dels objectes


FLOAT   : '0'..'9'+ '.' '0'..'9'+;
INT     : '0'..'9'+;
ID      :  ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

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

