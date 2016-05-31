package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 5/31/16.
 */
public class SVGCircle extends SVGObject {



    public SVGCircle(HashMap<String, Data> attr) {
        super();
        type = Type.CIRCLE;
        attr.put("radius",new Data(1.0f));
        changeAllAttributes(attr);
    }

    @Override
    public String getObjDescriptor() {
        return "circle";
    }
}
