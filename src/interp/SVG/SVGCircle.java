package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 5/31/16.
 */
public class SVGCircle extends SVGObject {


    private static final String RADIUS = "radius";

    public SVGCircle(SVGCircle svgCircle) {
        super(svgCircle);
        type = Type.CIRCLE;
    }

    public SVGCircle(HashMap<String, Data> attributes) {
        super();
        type = Type.CIRCLE;
        attr.put(RADIUS,new Data(1.0f));

        changeAllAttributes(attributes);
    }

    @Override
    public String getObjDescriptor() {
        return "circle";
    }

    @Override
    public HashMap<String,String> getSVGAttributes() {
        HashMap<String,String> map = super.getSVGAttributes();
        map.put("r",""+attr.get(RADIUS).getFloatValue());
        return map;
    }

    @Override
    public SVGCircle copy() {
        return new SVGCircle(this);
    }
}

