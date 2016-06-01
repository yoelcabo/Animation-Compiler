package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 5/31/16.
 */
public class SVGCircle extends SVGObject {



    public SVGCircle(HashMap<String, Data> attributes) {
        super();
        type = Type.CIRCLE;
        attr.put("radius",new Data(1.0f));
        attr.put("centerX",new Data((0)));
        attr.put("centerY",new Data((0)));
        System.out.println(attr);

        changeAllAttributes(attributes);
    }

    @Override
    public String getObjDescriptor() {
        return "circle";
    }

    @Override
    public HashMap<String,String> getSVGAttributes() {
        HashMap<String,String> map = super.getSVGAttributes();
        map.put("r",""+attr.get("radius").getFloatValue());
        map.put("cx",""+attr.get("centerX").getIntegerValue());
        map.put("cy",""+attr.get("centerY").getIntegerValue());
        return map;
    }
}
