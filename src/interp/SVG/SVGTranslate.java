package interp.SVG;

import interp.Data;

import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGTranslate extends SVGMove {
    private static final String X = "x";
    private static final String Y = "y";


    public SVGTranslate(HashMap<String, Data> attributes) {
        super();
        type = Type.TRANSLATE;
        attr.put(X, new Data(0));
        attr.put(Y, new Data(0));
        super.changeAllAttributes(attributes);
    }

    public SVGTranslate(SVGTranslate sc) {
        super(sc);
        type  = Type.TRANSLATE;
    }

    @Override
    public String getObjDescriptor() {
        return "animateTransform";
    }

    @Override
    public HashMap<String,String> getSVGAttributes(float wait) {
        HashMap<String,String> map = super.getSVGAttributes(wait);
        map.put("type","translate");
        map.put("attributeName","transform");
        float x = attr.get(X).getFloatValue();
        float y = attr.get(Y).getFloatValue();
        map.put("by",x + " " + y);
        return map;
    }

    @Override
    public void changeAttribute (String nomAttr, Data newAttribute) {
        super.changeAttribute(nomAttr,newAttribute);

    }

    @Override
    public SVGMove copy() {
        return new SVGTranslate(this);
    }
}
