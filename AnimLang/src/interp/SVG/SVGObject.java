package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class SVGObject {



    public enum Type {CIRCLE,PATH,OBJPACK}; //etc
    private Type type;
    HashMap<String, Data> attr;
    ArrayList<SVGObject> content; //Només per objpack

    public SVGObject(SVGObject svgObject) {
        type = svgObject.type;
        attr = new HashMap<>(attr);
        content = new ArrayList<>(content);
    }
    public SVGObject(interp.SVG.SVGObject.Type type, HashMap<String, Data> attr) {
        this.type = type;
        this.attr = attr;
    }

    public SVGObject(interp.SVG.SVGObject.Type type) {
        this.type = type;
        this.attr = new HashMap<>();
    }

    public SVGObject(ArrayList<SVGObject> content) {
        this.type = Type.OBJPACK;
        this.attr = new HashMap<>();
        this.content = content;
    }

    public SVGObject(HashMap<String, Data> attr, ArrayList<SVGObject> content) {
        this.type = Type.OBJPACK;
        this.attr = attr;
        this.content = content;
    }

    public SVGObject copy() {
        return new SVGObject(this);
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

    public ArrayList<SVGObject> getContent() {
        return content;
    }

    public void setContent(ArrayList<SVGObject> content) {
        this.content = content;
    }

    public String getSVGHeader() {
        return null;
    }

}
