package interp.SVG;

import interp.Data;

import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGTriangle extends SVGObject{

    private static final String RADIUS = "radius";
    private static final String CENTERX = "centerX";
    private static final String CENTERY = "centerY";
    private static final int costats = 3;

    public SVGTriangle(SVGTriangle svgCircle) {
        this(svgCircle.attr);
    }
    public SVGTriangle(HashMap<String, Data> attributes) {
        super();
        type = Type.TRIANGLE;
        attr.put(RADIUS,new Data(1.0f));
        attr.put(CENTERX,new Data((0)));
        attr.put(CENTERY,new Data((0)));
        changeAllAttributes(attributes);
    }

    @Override
    public String getObjDescriptor() {
        return "polygon";
    }

    @Override
    public HashMap<String,String> getSVGAttributes() {
        HashMap<String,String> map = super.getSVGAttributes();
        map.put("points",getPoints());
        return map;
    }

    public String getPoints() {
        String points = "";
        int cx = attr.get(CENTERX).getIntegerValue();
        int cy = attr.get(CENTERY).getIntegerValue();
        float r = attr.get(RADIUS).getFloatValue();

        double angle = 2*Math.PI/costats;
        float alpha = 0;
        points += cx + r*Math.sin(alpha);
        points += ",";
        points += cy + r*Math.cos(alpha);
        for (int i = 1; i < costats; ++i) {
            alpha += angle;
            points += " ";
            points += cx + r*Math.sin(alpha);
            points += ",";
            points += cy + r*Math.cos(alpha);

        }
        return points;
    }

    @Override
    public SVGObject copy() {
        return new SVGCircle(new HashMap<>(attr));
    }
}
