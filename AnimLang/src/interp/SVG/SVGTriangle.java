package interp.SVG;

import interp.Data;

import java.util.HashMap;

import java.io.*;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGTriangle extends SVGObject {

    private static final String RADIUS = "radius";
    private static final int costats = 3;

    public SVGTriangle(SVGTriangle svgTriangle) {
        super(svgTriangle);
    }
    public SVGTriangle(HashMap<String, Data> attributes) {
        super();
        type = Type.TRIANGLE;
        attr.put(RADIUS,new Data(1.0f));
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
        float r = attr.get(RADIUS).getFloatValue();

        double angle = 2*Math.PI/costats;
        float alpha = 0;
        points += r*Math.sin(alpha);
        points += ",";
        points += r*Math.cos(alpha);
        for (int i = 1; i < costats; ++i) {
            alpha += angle;
            points += " ";
            points += r*Math.sin(alpha);
            points += ",";
            points += r*Math.cos(alpha);

        }
        return points;
    }

    @Override
    public SVGTriangle copy() {//throws IOException {
        return new SVGTriangle(this);
    }
}
