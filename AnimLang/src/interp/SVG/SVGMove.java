package interp.SVG;

import interp.Data;

import java.util.HashMap;
import java.util.Map;

public class SVGMove extends SVGSerializableParallelizable {


    private static final String DURATION = "dur";

    public enum Type {WAIT, TRANSLATE, ROTATE, SCALE, FOLLOWPATH}; //etc
    public Type type;
    protected HashMap<String, Data> attr = new HashMap<String, Data>(){{
        //put("xPos", new Data(0)); TODO poner atributos
        //put("yPos", new Data(0));
    }};

    // CONSTRUCTORS //

    public SVGMove() {
        super();
    }
    public SVGMove(float dur) {
        super(dur);
        this.type = Type.WAIT;
        attr = new HashMap<>();
    }

    public SVGMove(HashMap<String, Data> attributes) {
        this();
        changeAllAttributes(attr);
    }

    public SVGMove(interp.SVG.SVGMove.Type type, float dur) {
        super(dur);
        this.type = type;

    }

    public SVGMove(Type type, float init, float end) {
        super(init, end);
        this.type = type;
    }

    public SVGMove(Type type, HashMap<String, Data> attr) {
        this(attr);
        this.type = type;
        this.attr = attr;

    }

    public SVGMove(Type type, HashMap<String, Data> attr, float dur) {
        super(dur);
        this.type = type;
        this.attr = attr;

    }
    public SVGMove(Type type, HashMap<String, Data> attr, float init, float end) {
        super(init, end);
        this.type = type;
        this.attr = attr;
    }

    public SVGMove(SVGMove svgMove) {
        super(svgMove);
        this.type = svgMove.type;
        this.attr = new HashMap<>();
        for (Map.Entry<String,Data> entry : svgMove.attr.entrySet()) {
            attr.put(entry.getKey(),new Data(entry.getValue()));
        }
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

    @Override
    public String toString() {
        return "SVGMove{" +
                "type=" + type +
                ", attr=" + attr +
                ", init=" + init +
                ", end=" + end +
                '}';
    }

    public String getSVGCode(float wait) {
        String svgcode = "<"+getObjDescriptor();
        for (Map.Entry<String,String> attribute : getSVGAttributes(wait).entrySet()) {
            svgcode += " "+attribute.getKey()+"=\""+attribute.getValue()+"\"";
        }
        svgcode += " />";
        return svgcode;
    }


    protected String getObjDescriptor() {
        return "defaultMove";
    }

    @Override
    public SVGMove copy() {
        return new SVGMove(this);
    }

    //TODO atributs comuns de moviment
    protected HashMap<String,String> getSVGAttributes(float wait) {
        HashMap<String,String> map = new HashMap<>();
        map.put("begin",""+(getInit() + wait));
        map.put("dur",""+getDur());
        map.put("fill","freeze");
     //   map.put("calcMode","spline");
        return map;
    }

    // This method is exactly the same as the one in SVGObject, it's here because java's impotence with multi inheritance
    protected void changeAllAttributes(HashMap<String, Data> attr) {
        for (Map.Entry<String,Data> at : attr.entrySet()) {
            changeAttribute(at.getKey(),at.getValue());
        }
    }

    // This method is exactly the same as the one in SVGObject, it's here because java's impotence with multi inheritance
    public void changeAttribute (String nomAttr, Data newAttribute) {
        if (nomAttr.equals(DURATION)) {
            init = 0;
            Data duration = newAttribute;
            if (duration.getType() != Data.Type.FLOAT && duration.getType() != Data.Type.INTEGER) throw new RuntimeException("Duration must be a number.");
            end = duration.getFloatValue();
            return;
        }
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

}
