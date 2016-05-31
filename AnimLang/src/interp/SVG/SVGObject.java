package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class SVGObject {

    public enum Type {CIRCLE, PATH, POLYGON, POLYLINE, TRIANGLE, OBJ_PACK}; //etc
    private Type type;
    HashMap<String, Data> attr;
    ArrayList<SVGObject> content; //Només per OBJ_PACK

    // CONSTRUCTORS // 

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
        this.type = Type.OBJ_PACK;
        this.attr = new HashMap<>();
        this.content = content;
    }

    public SVGObject(HashMap<String, Data> attr, ArrayList<SVGObject> content) {
        this.type = Type.OBJ_PACK;
        this.attr = attr;
        this.content = content;
    }


    // GETTERS I SETTERS //

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
        String header = "<"+getObjDescriptor();
        for (Map.Entry<String,Data> attribute : attr.entrySet()) {
            header += " "+attribute.getKey()+"=\""+attribute.getValue()+"\"";
        }
        header += ">";
        return header;
    }

    //TODO
    private String getObjDescriptor() {
        return "";
    }

    //TODO
    public boolean comprovacioAtributs() throws RuntimeException {
       return  false;
    }

    public String getSVGEnd() {
        return "</"+getObjDescriptor()+">";
    }

}
