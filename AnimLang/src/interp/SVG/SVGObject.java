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
        put("colorFill", new Data("256:0:0"));
        put("lineWidth", new Data(5));
        put("opacity", new Data(1.0f));
        //put("xPos", new Data(0));
        //put("yPos", new Data(0));
    }};
    private ArrayList<SVGObject> content; //Només per OBJ_PACK

    // CONSTRUCTORS //

    public SVGObject() {
        content = new ArrayList<>();
    }

    public SVGObject(HashMap<String, Data> attr) {
        changeAllAttributes(attr);
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



    //Mètode ganxo
    public String getObjDescriptor() {
        return "default";
    }


    //TODO traducciones a SVG real
    public String getSVGHeader() {
        String header = "<"+getObjDescriptor();
        for (Map.Entry<String,String> attribute : getSVGAttributes().entrySet()) {
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

    public String toString() {
        return type + ": " + attr;
    }

    // This method is exactly the same as the one in SVGMove, it's here because java's impotence with multi inheritance
    protected void changeAllAttributes(HashMap<String, Data> attr) {
        for (Map.Entry<String,Data> at : attr.entrySet()) {
            changeAttribute(at.getKey(),at.getValue());
        }
    }

    // This method is exactly the same as the one in SVGMove, it's here because java's impotence with multi inheritance
    public void changeAttribute (String nomAttr, Data newAttribute) {
        if (!attr.containsKey(nomAttr)) {
            throw new RuntimeException(nomAttr+" is not a valid attribute for "+type);
        }
        if (attr.get(nomAttr).getType() != newAttribute.getType()) {
            if ((attr.get(nomAttr).getType() != Data.Type.FLOAT || newAttribute.getType() != Data.Type.INTEGER)
                    && (newAttribute.getType() != Data.Type.FLOAT && attr.get(nomAttr).getType() != Data.Type.INTEGER ) )
                throw new RuntimeException("Wrong type for "+type+"."+nomAttr+": expected "+attr.get(nomAttr).getType()+" but got "+newAttribute.getType()); //TODO Podria haver casts implícits
        }
        attr.put(nomAttr,newAttribute);
    }

    protected HashMap<String,String> getSVGAttributes() {
        HashMap<String,String> map = new HashMap<>();
        map.put("stroke",attrToSVGColor("colorLine"));
        map.put("fill",attrToSVGColor("colorFill"));
        map.put("stroke-width",""+attr.get("lineWidth"));
        map.put("opacity",""+attr.get("opacity"));
        return map;
    }

    protected String attrToSVGColor(String attribute) {
        String[] st = attr.get(attribute).getStringValue().split(":");
        if (st.length != 3) throw new RuntimeException("Wrong number of parameters for RGB color.");
        int r,g,b;
        try {
            r = Integer.parseInt(st[0]);
            g = Integer.parseInt(st[1]);
            b = Integer.parseInt(st[2]);
            if (r > 225 || g > 255 || b > 255 || r < 0 || g < 0 || b < 0) throw new Exception();
        }
        catch (Exception e) {
            throw new RuntimeException("Color parameters must be integers between 0 and 255.");
        }

        return "rgb("+r+","+g+","+b+")";
    }

}
