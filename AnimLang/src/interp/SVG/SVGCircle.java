package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 5/31/16.
 */
public class SVGCircle extends SVGObject {


    private static final String RADIUS = "radius";
    private static final String CENTERX = "centerX";
    private static final String CENTERY = "centerY";

    public SVGCircle(HashMap<String, Data> attributes) {
        super();
        type = Type.CIRCLE;
        attr.put(RADIUS,new Data(1.0f));
        attr.put(CENTERX,new Data((0)));
        attr.put(CENTERY,new Data((0)));
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
        map.put("cx",""+attr.get(CENTERX).getIntegerValue());
        map.put("cy",""+attr.get(CENTERY).getIntegerValue());
        return map;
    }
}
