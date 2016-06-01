package interp.SVG;

import interp.Data;

import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGTranslate extends SVGMove {
    private static final String SCALEFACTOR = "factor";


    public SVGTranslate(HashMap<String, Data> attributes) {
        super();
        type = SVGMove.Type.ROTATE;
        attr.put(SCALEFACTOR, new Data(1f));
        super.changeAllAttributes(attributes);
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
        map.put("from",""+1);
        float sf = attr.get(SCALEFACTOR).getFloatValue();
        map.put("to",sf + " " + sf);
        return map;
    }

    @Override
    public void changeAttribute (String nomAttr, Data newAttribute) {
        super.changeAttribute(nomAttr,newAttribute);

    }

    @Override
    public SVGMove copy() {
        return new SVGRotate(init,end,new HashMap<>(attr));
    }
}
