/**
 * Copyright (c) 2011, Jordi Cortadella
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of the <organization> nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package interp;

import parser.*;
import interp.SVG.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

/** Class that implements the interpreter of the language. */

public class Interp {

    /** Memory of the virtual machine. */
    private Stack Stack;

    /**
     * Map between function names (keys) and ASTs (values).
     * Each entry of the map stores the root of the AST
     * correponding to the function.
     */
    private HashMap<String,AnimLangTree> FuncName2Tree;

    /** Standard input of the interpreter (System.in). */
    private Scanner stdin;

    /**
     * Stores the line number of the current statement.
     * The line number is used to report runtime errors.
     */
    private int linenumber = -1;

    /** File to write the trace of function calls. */
    private PrintWriter trace = null;
    
    /** File to write the SVG file. */
    private PrintWriter svg = null;

    /** Nested levels of function calls. */
    private int function_nesting = -1;
    
    /**
     * Constructor of the interpreter. It prepares the main
     * data structures for the execution of the main program.
     */
    public Interp(AnimLangTree T, String tracefile, String outputFile) {
        assert T != null;
        MapFunctions(T);  // Creates the table to map function names into AST nodes
        PreProcessAST(T); // Some internal pre-processing ot the AST
        Stack = new Stack(); // Creates the memory of the virtual machine
        // Initializes the standard input of the program
        stdin = new Scanner (new BufferedReader(new InputStreamReader(System.in)));
        if (tracefile != null) {
            try {
                trace = new PrintWriter(new FileWriter(tracefile));
            } catch (IOException e) {
                System.err.println(e);
                System.exit(1);
            }
        }
        try {
            svg = new PrintWriter(new FileWriter(outputFile));
        } catch (IOException e) {
             System.err.println(e);
             System.exit(1);
        }

        function_nesting = -1;
    }

    /** Runs the program by calling the main function without parameters. */
    public void Run() {
        executeFunction ("main", null);
    }

    /** Returns the contents of the stack trace */
    public String getStackTrace() {
        return Stack.getStackTrace(lineNumber());
    }

    /** Returns a summarized contents of the stack trace */
    public String getStackTrace(int nitems) {
        return Stack.getStackTrace(lineNumber(), nitems);
    }
    
    /**
     * Gathers information from the AST and creates the map from
     * function names to the corresponding AST nodes.
     */
    private void MapFunctions(AnimLangTree T) {
        assert T != null && T.getType() == AnimLangLexer.LISTFUNC;
        FuncName2Tree = new HashMap<String,AnimLangTree> ();
        int n = T.getChildCount();
        for (int i = 0; i < n; ++i) {
            AnimLangTree f = T.getChild(i);
            assert f.getType() == AnimLangLexer.DEF;
            String fname = f.getChild(0).getText();
            if (FuncName2Tree.containsKey(fname)) {
                throw new RuntimeException("Multiple definitions of function " + fname);
            }
            FuncName2Tree.put(fname, f);
        } 
    }

    /**
     * Performs some pre-processing on the AST. Basically, it
     * calculates the value of the literals and stores a simpler
     * representation. See AnimLangTree.java for details.
     */
    private void PreProcessAST(AnimLangTree T) {
        if (T == null) return;
        switch(T.getType()) {
            case AnimLangLexer.INT: T.setIntValue(); break;
            case AnimLangLexer.STRING: T.setStringValue(); break;
            case AnimLangLexer.BOOLEAN: T.setBooleanValue(); break;
            case AnimLangLexer.FLOAT: T.setFloatValue(); break;
            default: break;
        }
        int n = T.getChildCount();
        for (int i = 0; i < n; ++i) PreProcessAST(T.getChild(i));
    }

    /**
     * Gets the current line number. In case of a runtime error,
     * it returns the line number of the statement causing the
     * error.
     */
    public int lineNumber() { return linenumber; }

    /** Defines the current line number associated to an AST node. */
    private void setLineNumber(AnimLangTree t) { linenumber = t.getLine();}

    /** Defines the current line number with a specific value */
    private void setLineNumber(int l) { linenumber = l;}
    
    /**
     * Executes a function.
     * @param funcname The name of the function.
     * @param args The AST node representing the list of arguments of the caller.
     * @return The data returned by the function.
     */
    private Data executeFunction (String funcname, AnimLangTree args) {
        // Get the AST of the function
        AnimLangTree f = FuncName2Tree.get(funcname);
        if (f == null) throw new RuntimeException(" function " + funcname + " not declared");

        // Gather the list of arguments of the caller. This function
        // performs all the checks required for the compatibility of
        // parameters.
        ArrayList<Data> Arg_values = listArguments(f, args);

        // Dumps trace information (function call and arguments)
        if (trace != null) traceFunctionCall(f, Arg_values);
        
        // List of parameters of the callee
        AnimLangTree p = f.getChild(1);
        int nparam = p.getChildCount(); // Number of parameters

        // Create the activation record in memory
        Stack.pushActivationRecord(funcname, lineNumber());

        // Track line number
        setLineNumber(f);
         
        // Copy the parameters to the current activation record
        for (int i = 0; i < nparam; ++i) {
            String param_name = p.getChild(i).getText();
            Stack.defineVariable(param_name, Arg_values.get(i));
        }

        // Execute the instructions
        Data result = executeListInstructions (f.getChild(2));

        // If the result is null, then the function returns void
        if (result == null) result = new Data();
        
        // Dumps trace information
        if (trace != null) traceReturn(f, result, Arg_values);
        
        // Destroy the activation record
        Stack.popActivationRecord();

        return result;
    }

    /**
     * Executes a block of instructions. The block is terminated
     * as soon as an instruction returns a non-null result.
     * Non-null results are only returned by "return" statements.
     * @param t The AST of the block of instructions.
     * @return The data returned by the instructions (null if no return
     * statement has been executed).
     */
    private Data executeListInstructions (AnimLangTree t) {
        assert t != null;
        Data result = null;
        int ninstr = t.getChildCount();
        for (int i = 0; i < ninstr; ++i) {
            result = executeInstruction (t.getChild(i));
            if (result != null) return result;
        }
        return null;
    }

    private void printTrace(AnimLangTree t) {
        System.out.println("Line: " + linenumber);
        System.out.println("Instr: " + t.getText() + "; Type: " + t.getType());
        System.out.println("");
    }

    private void print(String s) {
        //System.err.println(s);
    }
    
    /**
     * Executes an instruction. 
     * Non-null results are only returned by "return" statements.
     * @param t The AST of the instruction.
     * @return The data returned by the instruction. The data will be
     * non-null only if a return statement is executed or a block
     * of instructions executing a return.
     */
    private Data executeInstruction (AnimLangTree t) {
        assert t != null;
        
        setLineNumber(t);
        //printTrace(t);
        Data value; // The returned value

        // A big switch for all type of instructions
        switch (t.getType()) {

            // Assignment
            case AnimLangLexer.ASSIGN:
                value = evaluateExpression(t.getChild(1));
                // La variable no sempre s'ha de definir. De vegades pot ser la modificacio d'un camp
                if (t.getChild(0).getText().equals(".")) {
                    AnimLangTree tAux = t.getChild(0);
                    Data dataAux = Stack.getVariable(tAux.getChild(0).getText());
                    if (! dataAux.isObject()) {
                        throw new RuntimeException ("Required object, found " + dataAux.getType());
                    }
                    dataAux.getObjectValue().changeAttribute(tAux.getChild(1).getText(), value);
                } else {
                    Stack.defineVariable(t.getChild(0).getText(), value);
                }
                return null;

            // If-then-else
            case AnimLangLexer.COND:
                value = evaluateExpression(t.getChild(0));
                checkBoolean(value);
                if (value.getBooleanValue()) return executeListInstructions(t.getChild(1));
                // Is there else statement ?
                if (t.getChildCount() == 3) return executeListInstructions(t.getChild(2));
                return null;

            // While
            case AnimLangLexer.WHILE:
                while (true) {
                    value = evaluateExpression(t.getChild(0));
                    checkBoolean(value);
                    if (!value.getBooleanValue()) return null;
                    Data r = executeListInstructions(t.getChild(1));
                    if (r != null) return r;
                }

            // For
            case AnimLangLexer.FOR:
                // forExpr[0] -> iniValue; forExpr[1] -> endValue; forExpr[2] -> increment; 
                int[] forExpr = evaluateForExpr(t.getChild(0));  // [x,y,y]
                // agafa la variable que itera el for
                Data itVar = Stack.getVariable(t.getChild(0).getChild(0).getText());
                int i = 0;
                print("init -> " + itVar.getIntegerValue() + "; end -> " + forExpr[1]);
                while (itVar.getIntegerValue() < forExpr[1]) {
                    Data r = executeListInstructions(t.getChild(1));
                    if (r != null) return r;
                    itVar.setValue(itVar.getIntegerValue() + forExpr[2]);
                }
                return null;

            // Return
            case AnimLangLexer.RETURN:
                if (t.getChildCount() != 0) {
                    return evaluateExpression(t.getChild(0));
                }
                return new Data(); // No expression: returns void data

            // Function call
            case AnimLangLexer.FUNCALL:
                executeFunction(t.getChild(0).getText(), t.getChild(1));
                return null;

            case AnimLangLexer.PRINT:
                Data printData = evaluateExpression(t.getChild(0));
                System.out.println("" + printData);
                return null;

            // Run animation
            case AnimLangLexer.RUN:
                evaluateRun(t.getChild(0), t.getChild(1), t.getChild(2));
                break;

            default: assert false; // Should never happen
        }

        // All possible instructions should have been treated.
        assert false;
        return null;
    }

    /**
     * Evaluates the expression represented in the AST t.
     * @param t The AST of the expression
     * @return The value of the expression.
     */
     
    private Data evaluateExpression(AnimLangTree t) {
        assert t != null;

        int previous_line = lineNumber();
        setLineNumber(t);
        int type = t.getType();

        Data value = null;
        // Atoms
        switch (type) {
            // A variable
            case AnimLangLexer.ID:
                value = new Data(Stack.getVariable(t.getText()));
                break;
            // An integer literal
            case AnimLangLexer.INT:
                value = new Data(t.getIntValue());
                break;
            // A float literal
            case AnimLangLexer.FLOAT:
                value = new Data(t.getFloatValue()); // crear el getFloatValue()
                break;
            // A Boolean literal
            case AnimLangLexer.BOOLEAN:
                value = new Data(t.getBooleanValue());
                break;
            // A function call. Checks that the function returns a result.
            case AnimLangLexer.FUNCALL:
                value = executeFunction(t.getChild(0).getText(), t.getChild(1));
                assert value != null;
                if (value.isVoid()) {
                    throw new RuntimeException ("function expected to return a value");
                }
                break;
            case AnimLangLexer.STRING:
                value = new Data(t.getStringValue());
                break;
            // atoms especials del nostre llenguatge
            case AnimLangLexer.OBJ:
                value = generateObject(t.getChild(0), t.getChild(1));
                break;
            case AnimLangLexer.MOV:
                value = generateMov(t.getChild(0), t.getChild(1));
                break;
            case AnimLangLexer.OBJ_PACK:
                value = construeixPack(t);
                break;

            default: break;
        }

        // Retrieve the original line and return
        if (value != null) {
            setLineNumber(previous_line);
            return value;
        }
        
        // Unary operators
        value = evaluateExpression(t.getChild(0));
        if (t.getChildCount() == 1) {
            switch (type) {
                case AnimLangLexer.PLUS:
                    checkInteger(value); // potser hauria de ser checkNumerical
                    break;
                case AnimLangLexer.MINUS:
                    checkInteger(value); // potser hauria de ser checkNumerical
                    value.setValue(-value.getIntegerValue());
                    break;
                case AnimLangLexer.NOT:
                    checkBoolean(value);
                    value.setValue(!value.getBooleanValue());
                    break;
                default: assert false; // Should never happen
            }
            setLineNumber(previous_line);
            return value;
        }

        // Two operands
        Data value2;
        switch (type) {
            // Relational operators
            case AnimLangLexer.EQ:
            case AnimLangLexer.NE:
            case AnimLangLexer.LT:
            case AnimLangLexer.LE:
            case AnimLangLexer.GT:
            case AnimLangLexer.GE:
                value2 = evaluateExpression(t.getChild(1));
                if (value.getType() != value2.getType()) {
                  throw new RuntimeException ("Incompatible types in relational expression");
                }
                value = value.evaluateRelational(type, value2);
                break;

            // Arithmetic operators
            case AnimLangLexer.PLUS:
            case AnimLangLexer.MINUS:
            case AnimLangLexer.PROD:
            case AnimLangLexer.DIV:
            case AnimLangLexer.MOD:
                value2 = evaluateExpression(t.getChild(1));
                checkInteger(value); checkInteger(value2); // mirar si es float tambe
                value.evaluateArithmetic(type, value2); // fer cast implicit si hi ha int i float
                break;

            // Boolean operators
            case AnimLangLexer.AND:
            case AnimLangLexer.OR:
                // The first operand is evaluated, but the second
                // is deferred (lazy, short-circuit evaluation).
                checkBoolean(value);
                value = evaluateBoolean(type, value, t.getChild(1));
                break;

            // Operadors propis
            case AnimLangLexer.PAR:
            case AnimLangLexer.SEQ:
            case AnimLangLexer.ASSOC:
                value2 = evaluateExpression(t.getChild(1));
                // comprova tipus dels dos operants
                value = value.evaluateOrchestration(type, value2);
                break;

            default: assert false; // Should never happen
        }
        
        setLineNumber(previous_line);
        return value;
    }
    
    /**
     * Evaluation of Boolean expressions. This function implements
     * a short-circuit evaluation. The second operand is still a tree
     * and is only evaluated if the value of the expression cannot be
     * determined by the first operand.
     * @param type Type of operator (token).
     * @param v First operand.
     * @param t AST node of the second operand.
     * @return An Boolean data with the value of the expression.
     */
    private Data evaluateBoolean (int type, Data v, AnimLangTree t) {
        // Boolean evaluation with short-circuit

        switch (type) {
            case AnimLangLexer.AND:
                // Short circuit if v is false
                if (!v.getBooleanValue()) return v;
                break;
        
            case AnimLangLexer.OR:
                // Short circuit if v is true
                if (v.getBooleanValue()) return v;
                break;
                
            default: assert false;
        }

        // Return the value of the second expression
        v = evaluateExpression(t);
        checkBoolean(v);
        return v;
    }

    // Comprova que els atributs de la definicio d'un objecte o moviment son correctes
    private HashMap<String, Data> getAttr (AnimLangTree attr) {
        // Els atributs poden representar-se en una classe
        // Aquesta classe tindria un HashMap amb els noms dels atributs com a clau i un array de longitud 2 com a valor
        // Aquest array contindria el tipus que ha de tenir aquell atribut concret i si pertany a MOV, OBJ o a tots dos
        // A mesura que es van comprovant els atributs, tambe es va construint l'OBJ o MOv en si
        HashMap<String, Data> llAttr = new HashMap<>();
        for (int i = 0; i < attr.getChildCount(); ++i) {
            String key = attr.getChild(i).getChild(0).getText(); // clau
            Data value = evaluateExpression(attr.getChild(i).getChild(1)); // valor
            if (llAttr.containsKey(key)) {
                throw new RuntimeException ("Duplicated key in attribute definition");
            } else {
                llAttr.put(key, value);
            }
        }
        return llAttr;
    }

    private void evaluateRun (AnimLangTree coordX, AnimLangTree coordY, AnimLangTree scene) {
        Data x = evaluateExpression(coordX);
        if (! x.isInteger()) {
            throw new RuntimeException ("Need integer, found " + x.getType());
        }
        Data y = evaluateExpression(coordY);
        if (! y.isInteger()) {
            throw new RuntimeException ("Need integer, found " + y.getType());
        }
        Data sc = evaluateExpression(scene);
        if (! sc.isMovingObject()) {
            throw new RuntimeException ("Need moving object, found " + sc.getType());
        }
        sc.getMovingObjectValue().setWidth(x.getIntegerValue());
        sc.getMovingObjectValue().setHeight(y.getIntegerValue());
        String svgCode = sc.getMovingObjectValue().getSVGCode();
        print(svgCode);
        svg.print(svgCode);
        svg.close();
        System.exit(0);
    }

    private Data generateObject (AnimLangTree typeObj, AnimLangTree attr) {
        // afegir els atributs
        HashMap<String, Data> llAttr = getAttr(attr);
        SVGObject newObject = null;
        //(CIRCLE | POLYGON | POLYLINE | TRIANGLE | PATH)
        switch (typeObj.getType()) {
            case AnimLangLexer.CIRCLE:
                newObject = new SVGCircle(llAttr);
                break;
            case AnimLangLexer.POLYGON:
                newObject = new SVGObject(SVGObject.Type.POLYGON, llAttr);
                break;
            case AnimLangLexer.POLYLINE:
                newObject = new SVGObject(SVGObject.Type.POLYLINE, llAttr);
                break;
            case AnimLangLexer.TRIANGLE:
                newObject = new SVGTriangle(llAttr);
                break;
            case AnimLangLexer.RECTANGLE:
                newObject = new SVGRectangle(llAttr);
                break;
            case AnimLangLexer.REGULARPOLYGON:
                newObject = new SVGRegularPolygon(llAttr);
                break;
            case AnimLangLexer.TEXTOBJECT:
                newObject = new SVGText(llAttr);
                break;
            case AnimLangLexer.PATH:
                newObject = new SVGObject(SVGObject.Type.PATH, llAttr);
                break;
            default:
                break;
        }
        return new Data(newObject);
    }

    private Data generateMov (AnimLangTree typeMov, AnimLangTree attr) {
        HashMap<String, Data> llAttr = getAttr(attr);
        SVGMove newMove = null;
        // (TRANSLATE | ROTATE | SCALE | FOLLOWPATH)
        switch (typeMov.getType()) {
            case AnimLangLexer.TRANSLATE:
                newMove = new SVGTranslate(llAttr);
                break;
            case AnimLangLexer.ROTATE:
                newMove = new SVGRotate(llAttr);
                break;
            case AnimLangLexer.SCALE:
                newMove = new SVGScale(llAttr);
                break;
            case AnimLangLexer.FOLLOWPATH:
                newMove = new SVGMove(SVGMove.Type.FOLLOWPATH, llAttr);
                break;
        }
        // newMove.comprovacioAtributs(); // throws exception if wrong attribute
        return new Data(new SVGMoves(newMove)); // provisional, perque compili
    }

    private Data construeixPack (AnimLangTree t) {
        ArrayList<SVGObject> newPack = new ArrayList<>();
        for (int i = 0; i < t.getChildCount(); ++i) {
            switch (t.getChild(i).getType()) {
                case AnimLangLexer.ID:
                    // comprova que l'ID es refereix a un element de tipus obj o obj_pack
                    Data valueID = new Data(Stack.getVariable(t.getChild(i).getText()));
                    if (valueID.isObject()) {
                        newPack.add(valueID.getObjectValue());
                    } else if (valueID.isObjectPack()) {
                        newPack.add(valueID.getObjectPackValue());
                    } else {
                        throw new RuntimeException("Required Object or ObjectPack, found " + valueID.getType());
                    }
                    break;
                case AnimLangLexer.OBJ:
                    // executa les instruccions per generar l'objecte
                    Data auxObj = generateObject(t.getChild(i).getChild(0), t.getChild(i).getChild(1));
                    newPack.add(auxObj.getObjectValue().copy());
                    break;
                case AnimLangLexer.OBJ_PACK:
                    // executa les instruccions per generar l'obj_pack
                    Data auxObjPack = construeixPack(t.getChild(i));
                    newPack.add(auxObjPack.getObjectPackValue().copy());
                    break;
                default:
                    // excepcio de tipus
                    throw new RuntimeException("Required Object or ObjectPack, found " + t.getChild(i).getType());
            }
        }

        return new Data(new SVGObjectPack(newPack));
    }

    /** Checks that the data is Boolean and raises an exception if it is not. */
    private void checkBoolean (Data b) {
        if (!b.isBoolean()) {
            throw new RuntimeException ("Expecting Boolean expression");
        }
    }
    
    /** Checks that the data is integer and raises an exception if it is not. */
    private void checkInteger (Data b) {
        if (!b.isInteger()) {
            throw new RuntimeException ("Expecting numerical expression");
        }
    }

    // Comprova si la dada es un valor numeric (int o float)
    private void checkNumerical (Data b) {
        if (!b.isInteger() && !b.isFloat()) {
            throw new RuntimeException ("Expecting numerical expression");
        }
    }

    /**
     * Gathers the list of arguments of a function call. It also checks
     * that the arguments are compatible with the parameters. In particular,
     * it checks that the number of parameters is the same and that no
     * expressions are passed as parametres by reference.
     * @param AstF The AST of the callee.
     * @param args The AST of the list of arguments passed by the caller.
     * @return The list of evaluated arguments.
     */
     
    private ArrayList<Data> listArguments (AnimLangTree AstF, AnimLangTree args) {
        if (args != null) setLineNumber(args);
        AnimLangTree pars = AstF.getChild(1);   // Parameters of the function
        
        // Create the list of parameters
        ArrayList<Data> Params = new ArrayList<Data> ();
        int n = pars.getChildCount();

        // Check that the number of parameters is the same
        int nargs = (args == null) ? 0 : args.getChildCount();
        if (n != nargs) {
            throw new RuntimeException ("Incorrect number of parameters calling function " +
                                        AstF.getChild(0).getText());
        }

        // Checks the compatibility of the parameters passed by
        // reference and calculates the values and references of
        // the parameters.
        for (int i = 0; i < n; ++i) {
            AnimLangTree p = pars.getChild(i); // Parameters of the callee
            AnimLangTree a = args.getChild(i); // Arguments passed by the caller
            setLineNumber(a);
            if (p.getType() == AnimLangLexer.PVALUE) {
                // Pass by value: evaluate the expression
                Params.add(i,evaluateExpression(a));
            } else {
                // Pass by reference: check that it is a variable
                if (a.getType() != AnimLangLexer.ID) {
                    throw new RuntimeException("Wrong argument for pass by reference");
                }
                // Find the variable and pass the reference
                Data v = Stack.getVariable(a.getText());
                Params.add(i,v);
            }
        }
        return Params;
    }

    private int[] evaluateForExpr (AnimLangTree t) {
        int[] valRet = new int[3];
        // evalua en rang d'iteracio del for
        switch (t.getChildCount() - 1) {
            case 1:
                valRet[0] = 0; valRet[1] = t.getChild(1).getIntValue(); valRet[2] = 1;
                break;
            case 2:
                valRet[0] = t.getChild(1).getIntValue(); 
                valRet[1] = t.getChild(2).getIntValue(); 
                valRet[2] = 1;
                break;
            case 3:
                valRet[0] = t.getChild(1).getIntValue(); 
                valRet[1] = t.getChild(2).getIntValue();
                valRet[2] = t.getChild(3).getIntValue();
                break;
        }
        Stack.defineVariable (t.getChild(0).getText(), new Data(valRet[0]));
        return valRet;
    }

    /**
     * Writes trace information of a function call in the trace file.
     * The information is the name of the function, the value of the
     * parameters and the line number where the function call is produced.
     * @param f AST of the function
     * @param arg_values Values of the parameters passed to the function
     */
    private void traceFunctionCall(AnimLangTree f, ArrayList<Data> arg_values) {
        function_nesting++;
        AnimLangTree params = f.getChild(1);
        int nargs = params.getChildCount();
        
        for (int i=0; i < function_nesting; ++i) trace.print("|   ");

        // Print function name and parameters
        trace.print(f.getChild(0) + "(");
        for (int i = 0; i < nargs; ++i) {
            if (i > 0) trace.print(", ");
            AnimLangTree p = params.getChild(i);
            if (p.getType() == AnimLangLexer.PREF) trace.print("&");
            trace.print(p.getText() + "=" + arg_values.get(i));
        }
        trace.print(") ");
        
        if (function_nesting == 0) trace.println("<entry point>");
        else trace.println("<line " + lineNumber() + ">");
    }

    /**
     * Writes the trace information about the return of a function.
     * The information is the value of the returned value and of the
     * variables passed by reference. It also reports the line number
     * of the return.
     * @param f AST of the function
     * @param result The value of the result
     * @param arg_values The value of the parameters passed to the function
     */
    private void traceReturn(AnimLangTree f, Data result, ArrayList<Data> arg_values) {
        for (int i=0; i < function_nesting; ++i) trace.print("|   ");
        function_nesting--;
        trace.print("return");
        if (!result.isVoid()) trace.print(" " + result);
        
        // Print the value of arguments passed by reference
        AnimLangTree params = f.getChild(1);
        int nargs = params.getChildCount();
        for (int i = 0; i < nargs; ++i) {
            AnimLangTree p = params.getChild(i);
            if (p.getType() == AnimLangLexer.PVALUE) continue;
            trace.print(", &" + p.getText() + "=" + arg_values.get(i));
        }
        
        trace.println(" <line " + lineNumber() + ">");
        if (function_nesting < 0) trace.close();
    }
}
