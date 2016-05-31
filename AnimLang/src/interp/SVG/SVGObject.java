package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class SVGObject {



    public enum Type {CIRCLE, PATH, POLYGON, POLYLINE, TRIANGLE, OBJ_PACK};
    protected Type type;
    protected HashMap<String, Data> attr = new HashMap<String, Data>(){{
        put("colorLine", new Data("0:0:0"));
        put("colorFilled", new Data("256:0:0"));
        put("lineWidth", new Data(5));
        put("opacity", new Data(1.0f));
        put("xPos", new Data(0));
        put("yPos", new Data(0));
    }};
    private ArrayList<SVGObject> content; //Només per OBJ_PACK

    // CONSTRUCTORS //

    public SVGObject() {
        content = new ArrayList<>();
    }

    public SVGObject(HashMap<String, Data> attr) {
        changeAllAttributes(attr);
    }

    protected void changeAllAttributes(HashMap<String, Data> attr) {
        for (Map.Entry<String,Data> at : attr.entrySet()) {
            changeAttribute(at.getKey(),at.getValue());
        }
    }

    public SVGObject(SVGObject svgObject) {
        type = svgObject.type;
        attr = new HashMap<>(attr);
        content = new ArrayList<>(content);
    }
    public SVGObject(interp.SVG.SVGObject.Type type, HashMap<String, Data> attr) {
        this(attr);
        this.type = type;
        content = new ArrayList<>();
    }

    public SVGObject(interp.SVG.SVGObject.Type type) {
        this.type = type;
        content = new ArrayList<>();
    }

    public SVGObject(ArrayList<SVGObject> content) {
        this.type = Type.OBJ_PACK;
        this.content = content;
    }

    public SVGObject(HashMap<String, Data> attr, ArrayList<SVGObject> content) {
        this(attr);
        this.type = Type.OBJ_PACK;
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



    public void changeAttribute (String nomAttr, Data newAttribute) {
        if (!attr.containsKey(nomAttr)) throw new RuntimeException(nomAttr+" is not a valid attribute for "+type+".");
        if (attr.get(nomAttr).getType() != newAttribute.getType()) throw new RuntimeException("Wrong type for "+type+"."+nomAttr+"."); //TODO Podria haver casts implícits
        attr.put(nomAttr,newAttribute);
    }

    //Mètode ganxo
    public String getObjDescriptor() {
        return "default";
    }


    //TODO traducciones a SVG real
    public String getSVGHeader() {
        String header = "<"+getObjDescriptor();
        for (Map.Entry<String,Data> attribute : attr.entrySet()) {
            header += " "+attribute.getKey()+"=\""+attribute.getValue()+"\"";
        }
        header += ">";
        return header;
    }

    public String getSubObjects() {
        String svgcode = "";
        if (type == Type.OBJ_PACK) {
            for (SVGObject subobject : content) {
                svgcode += subobject.getSVGHeader();
                svgcode += subobject.getSVGEnd() + "\n";
            }
        }
        return svgcode;
    }

    public String getSVGEnd() {
        return "</"+getObjDescriptor()+">";
    }

}
