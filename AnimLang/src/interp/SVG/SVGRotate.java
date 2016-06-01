package interp.SVG;

import interp.Data;

import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGRotate extends SVGMove {
    private static final String ANGULARVELOCITY = "w";


    public SVGRotate(HashMap<String, Data> attributes) {
        super();
        type = Type.ROTATE;
        attr.put(ANGULARVELOCITY, new Data(1f));
        super.changeAllAttributes(attributes);
    }

    public SVGRotate(float init, float end, HashMap<String, Data> stringDataHashMap) {
        super(Type.ROTATE,stringDataHashMap,init,end);
    }

    @Override
    public String getObjDescriptor() {
        return "animateTransform";
    }

    @Override
    public HashMap<String,String> getSVGAttributes(float wait) {
        HashMap<String,String> map = super.getSVGAttributes(wait);
        map.put("type","rotate");
        map.put("attributeName","transform");
        map.put("from",""+0);
        map.put("to",""+attr.get(ANGULARVELOCITY).getFloatValue()*getDur());
        return map;
    }

    @Override
    public void changeAttribute (String nomAttr, Data newAttribute) {
       /* if (nomAttr.equals(ANGULARVELOCITY)) {
            init = 0;
            Data w = newAttribute;
            if (w.getType() != Data.Type.FLOAT && w.getType() != Data.Type.INTEGER) throw new RuntimeException("Angular Velocity("+ANGULARVELOCITY+" must be a number.");
            end = w.getFloatValue();
            return;
        }*/
        super.changeAttribute(nomAttr,newAttribute);

    }

    @Override
    public SVGMove copy() {
        return new SVGRotate(init,end,new HashMap<>(attr));
    }
}

