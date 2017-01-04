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

/**
 * Class to represent data in the interpreter.
 * Each data item has a type and a value. The type can be integer
 * or Boolean. Each operation asserts that the operands have the
 * appropriate types.
 * All the arithmetic and Boolean operations are calculated in-place,
 * i.e., the result is stored in the same data.
 * The type VOID is used to represent void values on function returns.
 */

import parser.*;
import interp.SVG.*;

public class Data {
    /** Types of data */
    public enum Type {VOID, BOOLEAN, INTEGER, FLOAT, STRING, OBJECT, OBJ_PACK, MOVES, MOVINGOBJECT};

    /** Type of data*/
    private Type type;

    /** Value of the data when it is boolean or integer*/
    private int value; 
    
    /** Value of the data when it is float*/
    private float fvalue; 
    
    /** Value of the data when it is a string*/
    private String strvalue; 

    private SVGObject objValue;
    private SVGMoves movesValue;
    private SVGScene objMoveValue;
    // falta movingCollection
  
    // CONSTRUCTORS //

    /** Constructor for integers */
    public Data(int v) { type = Type.INTEGER; value = v; }

    /** Constructor for Booleans */
    public Data(boolean b) { type = Type.BOOLEAN; value = b ? 1 : 0; }

    /** Constructor for Floats */
    public Data(float v) { type = Type.FLOAT; fvalue = v; }

    /** Constructor for Strings */
    public Data(String s) { type = Type.STRING; strvalue = s; }

    /** Constructor for void data */
    public Data() {type = Type.VOID; }

    /** Copy constructor */
    public Data (Data d) {
        type = d.type;
        switch (type) {
            case BOOLEAN:
            case INTEGER:
                value = d.value;
                break;
            case FLOAT:
                fvalue = d.fvalue;
                break;
            case STRING:
                strvalue = new String(d.strvalue);
                break;
            case OBJECT:
            case OBJ_PACK:
                objValue = d.objValue.copy();
                break;
            case MOVES:
                movesValue = d.movesValue.copy();
                break;
            case MOVINGOBJECT:
                objMoveValue = d.objMoveValue.copy();
                break;
            default: assert false;
        }
    }

    // Our constructors //

    // OBJECT or OBJ_PACK
    public Data(SVGObject objValue) {
        if (objValue.getType() == SVGObject.Type.OBJ_PACK) {
            this.type = Type.OBJ_PACK;
        } else {
            this.type = Type.OBJECT;
        }
        this.objValue = objValue;
    }

    // MOVES
    public Data(SVGMoves movesValue) { this.type = Type.MOVES; this.movesValue = movesValue; }

    // MOVINGOBJECT
    public Data(SVGScene objMoveValue) { this.type = Type.MOVINGOBJECT; this.objMoveValue = objMoveValue; }


    // CONSULTORES //

    /** Returns the type of data */
    public Type getType() { return type; }

    /** Indicates whether the data is Boolean */
    public boolean isBoolean() { return type == Type.BOOLEAN; }

    /** Indicates whether the data is integer */
    public boolean isInteger() { return type == Type.INTEGER; }

    /** Indicates whether the data is integer */
    public boolean isString() { return type == Type.STRING; }

    /** Indicates whether the data is integer */
    public boolean isFloat() { return type == Type.FLOAT; }

    /** Indicates whether the data is void */
    public boolean isVoid() { return type == Type.VOID; }

    public boolean isObject() { return type == Type.OBJECT; }

    public boolean isObjectPack() { return type == Type.OBJ_PACK; }

    public boolean isMove() { return type == Type.MOVES; }

    public boolean isMovingObject() { return type == Type.MOVINGOBJECT; }


    // GETTERS // 

    /**
     * Gets the value of an integer data. The method asserts that
     * the data is an integer.
     */
    public int getIntegerValue() {
        assert type == Type.INTEGER;
        return value;
    }

    /**
     * Gets the value of an float data. The method asserts that
     * the data is a float.
     */
    public float getFloatValue() {
        switch (type) {
            case FLOAT:
                return fvalue;
            case INTEGER:
                return Float.valueOf(value);
            default: assert false;
        }
        return -1;
    }

    /**
     * Gets the value of a Boolean data. The method asserts that
     * the data is a Boolean.
     */
    public boolean getBooleanValue() {
        assert type == Type.BOOLEAN;
        return value == 1;
    }

    /**
     * Gets the value of a String data. The method asserts that
     * the data is a Boolean.
     */
    public String getStringValue() {
        assert type == Type.STRING;
        return strvalue;
    }

    public SVGObject getObjectValue() {
        assert type == Type.OBJECT;
        return objValue;
    }

    public SVGObject getObjectPackValue() {
        assert type == Type.OBJ_PACK;
        return objValue;
    }

    public SVGMoves getMoveValue() {
        switch (type) {
            case MOVES:
                return movesValue;
            case FLOAT:
                return new SVGMoves(new SVGMove(fvalue));
            case INTEGER:
                return new SVGMoves(new SVGMove(value));
            default: assert false;
        }
        return movesValue;
    }

    public SVGScene getMovingObjectValue() {
        switch (type) {
            case MOVINGOBJECT:
                return objMoveValue;
            case FLOAT:
                return new SVGScene(new SVGMovingObject(new SVGObject(),new SVGMoves(new SVGMove(fvalue))));
            case INTEGER:
                return new SVGScene(new SVGMovingObject(new SVGObject(),new SVGMoves(new SVGMove(value))));
            default: assert false;
        }        return objMoveValue;
    }


    // SETTERS //

    /** Defines a Boolean value for the data */
    public void setValue(boolean b) { type = Type.BOOLEAN; value = b ? 1 : 0; }

    /** Defines an integer value for the data */
    public void setValue(int v) { type = Type.INTEGER; value = v; }

    /** Defines a float value for the data */
    public void setValue(float v) { type = Type.FLOAT; fvalue = v; }
    
    /** Defines a String value for the data */
    public void setValue(String v) { type = Type.STRING; strvalue = v; }

    // Object or OBJ_PACK
    public void setValue(SVGObject v)  {
        if (v.getContent() == null || v.getContent().size() == 0) {
            this.type = Type.OBJECT;
        } else {
            this.type = Type.OBJ_PACK;
        }
        this.objValue = v;
    }

    // MOVES
    public void setValue(SVGMoves v) { this.type = Type.MOVES; this.movesValue = v; }

    // Moving object
    public void setValue(SVGScene v) { this.type = Type.MOVINGOBJECT; this.objMoveValue = v; }


    /** Copies the value from another data */
    public void setData(Data d) {
        type = d.type;
        switch (type) {
            case BOOLEAN:
            case INTEGER:
                value = d.value;
                break;
            case FLOAT:
                fvalue = d.fvalue;
                break;
            case STRING:
                strvalue = new String(d.strvalue);
                break;
            case OBJECT:
            case OBJ_PACK:
                objValue = d.objValue.copy();
                break;
            case MOVES:
                movesValue = d.movesValue.copy();
                break;
            case MOVINGOBJECT:
                objMoveValue = d.objMoveValue.copy();
                break;
        }

    }


    // TOSTRING // 
    
    /** Returns a string representing the data in textual form. */
    // S'ha d'afegir, si fa falta, els metodes per fet toString de MOVES, Obj, etc.
    public String toString() {
        if (type == Type.BOOLEAN) return value == 1 ? "true" : "false";
        if (type == Type.INTEGER) return Integer.toString(value);
        if (type == Type.FLOAT)   return Float.toString(fvalue);
        return strvalue;
    }
    

    // OPERACIONS // 

    /**
     * Checks for zero (for division). It raises an exception in case
     * the value is zero.
     */
    private void checkDivZero(Data d) {
        if (d.type == Type.INTEGER && d.value == 0 || d.type == Type.FLOAT && d.fvalue == 0) throw new RuntimeException ("Division by zero");
    }

    /**
     * Evaluation of arithmetic expressions. The evaluation is done
     * "in place", returning the result on the same data.
     * @param op Type of operator (token).
     * @param d Second operand.
     */
     
    public void evaluateArithmetic (int op, Data d) {
        if (type == Type.INTEGER && d.type == Type.INTEGER) {
            switch (op) {
                case AnimLangLexer.PLUS: value += d.value; break;
                case AnimLangLexer.MINUS: value -= d.value; break;
                case AnimLangLexer.PROD: value *= d.value; break;
                case AnimLangLexer.DIV: checkDivZero(d); value /= d.value; break;
                case AnimLangLexer.MOD: checkDivZero(d); value %= d.value; break;
                default: assert false;
            }
        }
        else if (type == Type.FLOAT && d.type == Type.FLOAT) {
            switch (op) {
                case AnimLangLexer.PLUS: fvalue += d.fvalue; break;
                case AnimLangLexer.MINUS: fvalue -= d.fvalue; break;
                case AnimLangLexer.PROD: fvalue *= d.fvalue; break;
                case AnimLangLexer.DIV: checkDivZero(d); fvalue /= d.fvalue; break;
                case AnimLangLexer.MOD: checkDivZero(d); fvalue %= d.fvalue; break;
                default: assert false;
            }
        }
        else if (type == Type.FLOAT && d.type == Type.INTEGER) {
            switch (op) {
                case AnimLangLexer.PLUS: fvalue += d.value; break;
                case AnimLangLexer.MINUS: fvalue -= d.value; break;
                case AnimLangLexer.PROD: fvalue *= d.value; break;
                case AnimLangLexer.DIV: checkDivZero(d); fvalue /= d.value; break;
                case AnimLangLexer.MOD: checkDivZero(d); fvalue %= d.value; break;
                default: assert false;
            }
        }
        else if (type == Type.INTEGER && d.type == Type.FLOAT) {
            switch (op) {
                case AnimLangLexer.PLUS: fvalue = value + d.fvalue; break;
                case AnimLangLexer.MINUS: fvalue = value - d.fvalue; break;
                case AnimLangLexer.PROD: fvalue = value * d.fvalue; break;
                case AnimLangLexer.DIV: checkDivZero(d); fvalue = value / d.fvalue; break;
                case AnimLangLexer.MOD: checkDivZero(d); fvalue = value % d.fvalue; break;
                default: assert false;
            }
        }
        else assert false;
    }

    /**
     * Evaluation of expressions with relational operators.
     * @param op Type of operator (token).
     * @param d Second operand.
     * @return A Boolean data with the value of the expression.
     */
    public Data evaluateRelational (int op, Data d) {
        assert type != Type.VOID && type == d.type;
        if (type == Type.INTEGER) {
            switch (op) {
                case AnimLangLexer.EQ: return new Data(value == d.value);
                case AnimLangLexer.NE: return new Data(value != d.value);
                case AnimLangLexer.LT: return new Data(value < d.value);
                case AnimLangLexer.LE: return new Data(value <= d.value);
                case AnimLangLexer.GT: return new Data(value > d.value);
                case AnimLangLexer.GE: return new Data(value >= d.value);
                default: assert false; 
            }
        }
        else if (type == Type.FLOAT) {
            switch (op) {
                case AnimLangLexer.EQ: return new Data(fvalue == d.fvalue);
                case AnimLangLexer.NE: return new Data(fvalue != d.fvalue);
                case AnimLangLexer.LT: return new Data(fvalue < d.fvalue);
                case AnimLangLexer.LE: return new Data(fvalue <= d.fvalue);
                case AnimLangLexer.GT: return new Data(fvalue > d.fvalue);
                case AnimLangLexer.GE: return new Data(fvalue >= d.fvalue);
                default: assert false; 
            }
        }
        else if (type == Type.STRING) {
            switch (op) {
                case AnimLangLexer.EQ: return new Data(strvalue.compareTo(d.strvalue) == 0);
                case AnimLangLexer.NE: return new Data(strvalue.compareTo(d.strvalue) != 0);
                case AnimLangLexer.LT: return new Data(strvalue.compareTo(d.strvalue) < 0);
                case AnimLangLexer.LE: return new Data(strvalue.compareTo(d.strvalue) <= 0);
                case AnimLangLexer.GT: return new Data(strvalue.compareTo(d.strvalue) > 0);
                case AnimLangLexer.GE: return new Data(strvalue.compareTo(d.strvalue) >= 0);
                default: assert false; 
            }
        }
        else assert false;
        return null;
    }

    // Simplement es per poder compilar el programa
    public Data evaluateOrchestration (int op, Data d) {
        assert type != Type.VOID; // && type == d.type; (es poden fer operacions entre mov i obj)
        if (type == Type.MOVES) {
            switch (op) {
                case AnimLangLexer.ASSOC:
                    if (d.type == Type.OBJECT) {
                        return new Data(new SVGScene(new SVGMovingObject(d.getObjectValue().copy(), getMoveValue().copy())));
                    }
                    else if (d.type == Type.OBJ_PACK) {
                        return new Data(new SVGScene(new SVGMovingObject(d.getObjectPackValue().copy(), getMoveValue().copy())));

                    }
                    else assert false;
                    break;
                case AnimLangLexer.PAR:
                    if (d.type == Type.MOVES) {
                        Data movPar = new Data((SVGMoves) SVGMoves.parallel(this.getMoveValue(),d.getMoveValue()));
                        return movPar;
                    } else if (d.type == Type.INTEGER || d.type == Type.FLOAT) {
                        Data movPar = new Data((SVGMoves) SVGMoves.parallel(this.getMoveValue(),new SVGMoves(new SVGMove(d.getFloatValue()))));
                        return movPar;
                    } else assert false;
                    // cast implicit de int o float a moviment
                    break;
                case AnimLangLexer.SEQ:
                    if (d.type == Type.MOVES) {
                        Data movSeq = new Data((SVGMoves) SVGMoves.serial(this.getMoveValue(),d.getMoveValue()));
                        return movSeq;
                    } else if (d.type == Type.INTEGER || d.type == Type.FLOAT) {
                        Data movSeq = new Data((SVGMoves) SVGMoves.serial(this.getMoveValue(),new SVGMoves(new SVGMove(d.getFloatValue()))));
                        return  movSeq;
                    } else assert false;
                    // cast implicit de int o float a moviment
                    break;
            }

        } else if (type == Type.MOVINGOBJECT) {
            switch (op) {
                case AnimLangLexer.PAR:
                    if (d.type == Type.MOVINGOBJECT) {
                        Data collPar = new Data((SVGScene) SVGScene.parallel(this.getMovingObjectValue(),d.getMovingObjectValue()));
                        return collPar;
                    } else assert false;
                    break;
                case AnimLangLexer.SEQ:
                    if (d.type == Type.MOVINGOBJECT) {
                        Data collSeq = new Data((SVGScene) SVGScene.serial(this.getMovingObjectValue(),d.getMovingObjectValue()));
                        //System.out.println("" +  collSeq.getMovingObjectValue());
                        //System.out.println("size" + collSeq.getMovingObjectValue().getMoves().size());
                        return collSeq;
                    } else assert false;
                    break;
            }

        } else assert false;
        
        return null;
    }
}
