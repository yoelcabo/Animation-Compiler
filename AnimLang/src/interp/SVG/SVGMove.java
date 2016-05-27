package interp.SVG;

import interp.Data;

import java.util.HashMap;

public class SVGMove extends SVGSerializableParallelizable {
    public enum Type {WAIT}; //etc
    public Type type;
    HashMap<String,Data> attr;

    public SVGMove(float dur) {
        super(dur);
        this.type = Type.WAIT;
        attr = new HashMap<>();
    }

    @Override
    public SVGSerializableParallelizable copy() {
        return new SVGMove(type,new HashMap<>(attr),init,end);
    }

    public SVGMove(interp.SVG.SVGMove.Type type, float dur) {
        super(dur);
        this.type = type;

    }

    public SVGMove(Type type, float init, float end) {
        super(init,end);
        this.type = type;
    }

    public SVGMove(Type type, HashMap<String, Data> attr, float dur) {
        super(dur);
        this.type = type;
        this.attr = attr;

    }
    public SVGMove(Type type, HashMap<String, Data> attr, float init, float end) {
        super(init,end);
        this.type = type;
        this.attr = attr;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public HashMap<String, Data> getAttr() {
        return attr;
    }

    public void setAttr(HashMap<String, Data> attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return "SVGMove{" +
                "type=" + type +
                ", attr=" + attr +
                ", init=" + init +
                ", end=" + end +
                '}';
    }
}
