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
        attr.put("centerX",new Data((0)));
        attr.put("centerY",new Data((0)));
        System.out.println(attr);

        changeAllAttributes(attributes);
    }

    @Override
    public String getObjDescriptor() {
        return "animateTransform";
    }

    @Override
    public HashMap<String,String> getSVGAttributes() {
        HashMap<String,String> map = super.getSVGAttributes();
        map.put("r",""+attr.get("radius").getFloatValue());
        map.put("cx",""+attr.get("centerX").getIntegerValue());
        map.put("cy",""+attr.get("centerY").getIntegerValue());
        return map;
    }

    @Override
    public void changeAttribute (String nomAttr, Data newAttribute) {
        if (nomAttr.equals(ANGULARVELOCITY)) {
            init = 0;
            Data duration = newAttribute;
            if (duration.getType() != Data.Type.FLOAT && duration.getType() != Data.Type.INTEGER) throw new RuntimeException("Angular Velocity("+ANGULARVELOCITY+" must be a number.");
            end = duration.getFloatValue();
            return;
        }
        super.changeAttribute(nomAttr,newAttribute);

    }
 }

