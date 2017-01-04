package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class SVGObject {


    public static final String LINECOLOR = "colorLine";
    public static final String FILLCOLOR = "colorFill";
    public static final String LINEWIDTH = "lineWidth";
    public static final String OPACITY = "opacity";
    public static final String CENTERX = "centerX";
    public static final String CENTERY = "centerY";

    public void moveCenter(int centerx, int centery) {
        changeAttribute(CENTERX,new Data(attr.get(CENTERX).getIntegerValue() + centerx));
        changeAttribute(CENTERY,new Data(attr.get(CENTERY).getIntegerValue() + centery));
    }


    public enum Type {CIRCLE, RECTANGLE, REGULARPOLYGON, PATH, POLYGON, POLYLINE, TRIANGLE, TEXT, OBJ_PACK};
    protected Type type;
    protected HashMap<String, Data> attr = new HashMap<String, Data>(){{
        put(LINECOLOR, new Data("0:0:0"));
        put(FILLCOLOR, new Data("256:0:0"));
        put(LINEWIDTH, new Data(5));
        put(OPACITY, new Data(1.0f));
        put(CENTERX,new Data(0));
        put(CENTERY,new Data(0));
        //put("xPos", new Data(0));
        //put("yPos", new Data(0));
    }};

    // CONSTRUCTORS //

    public SVGObject() {

    }

    public SVGObject(HashMap<String, Data> attr) {
        this();
        changeAllAttributes(attr);
    }


    public SVGObject(SVGObject svgObject) {
        type = svgObject.type;
        attr = new HashMap<>(svgObject.attr);
    }
    public SVGObject(interp.SVG.SVGObject.Type type, HashMap<String, Data> attr) {
        this(attr);
        this.type = type;
    }

    public SVGObject(interp.SVG.SVGObject.Type type) {
        this();
        this.type = type;
    }

    public SVGObject(ArrayList<SVGObject> content) {
        this.type = Type.OBJ_PACK;
    }

    public SVGObject(HashMap<String, Data> attr, ArrayList<SVGObject> content) {
        this(attr);
        this.type = Type.OBJ_PACK;
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

    //Mètode ganxo
    public String getObjDescriptor() {
        return "default";
    }


    public String getSVGHeader() {
        String header = "<"+getObjDescriptor();
        for (Map.Entry<String,String> attribute : getSVGAttributes().entrySet()) {
            header += " "+attribute.getKey()+"=\""+attribute.getValue()+"\"";
        }
        header += ">";
        return header;
    }

    public String getSVGSingleCode() {
        String header = "<"+getObjDescriptor();
        HashMap<String,String> map = new HashMap<>(getSVGAttributes());
        map.put("transform",getSVGIniTransform());
        for (Map.Entry<String,String> attribute : map.entrySet()) {
            header += " "+attribute.getKey()+"=\""+attribute.getValue()+"\"";
        }
        if (!getSVGContent().equals("")) {
            header += ">";
            header += getSVGContent();
            header += "</"+getObjDescriptor()+">";
        }
        else
            header += "/>";
        return header;
    }


    public String getSubObjects() {
        return "";
    }

    public String getSVGEnd() {

        return getSVGContent()+"</"+getObjDescriptor()+">";
    }

    public String toString() {
        return type + ": " + attr;
    }

    // This method is exactly the same as the one in SVGMove, it's here because java's impotence with multi inheritance
    protected void changeAllAttributes(HashMap<String, Data> attr) {
        for (Map.Entry<String,Data> at : attr.entrySet()) {
            changeAttribute(at.getKey(),new Data(at.getValue()));
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
        map.put("stroke",attrToSVGColor(LINECOLOR));
        map.put("fill",attrToSVGColor(FILLCOLOR));
        map.put("stroke-width",""+attr.get(LINEWIDTH));
        map.put("opacity",""+attr.get(OPACITY));
        return map;
    }



    protected String getSVGIniTransform(){
        return "translate("+attr.get(CENTERX).getIntegerValue()+","+attr.get(CENTERY).getIntegerValue()+")";
    }

    protected String attrToSVGColor(String attribute) {
        String[] st = attr.get(attribute).getStringValue().split(":");
        if (st.length != 3) throw new RuntimeException("Wrong number of parameters for RGB color.");
        int r,g,b;
        try {
            r = Integer.parseInt(st[0]);
            g = Integer.parseInt(st[1]);
            b = Integer.parseInt(st[2]);
        }
        catch (Exception e) {
            throw new RuntimeException("Color parameters must be integers.");
        }
        if (r > 225 || g > 255 || b > 255 || r < 0 || g < 0 || b < 0) new RuntimeException("Color parameters must be integers between 0 and 255.");


        return "rgb("+r+","+g+","+b+")";
    }

    public SVGObject copy() {
        return new SVGObject(this);
    }


    public ArrayList<SVGObject> getContent() {
        return null;
    }

    public String getSVGContent() {
        return "";
    }


}
