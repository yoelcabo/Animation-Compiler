package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGRegularPolygon extends SVGAbstractPolygon {

    private static final String RADIUS = "radius";
    protected static final String COSTATS = "n";

    public SVGRegularPolygon(SVGRegularPolygon svgRegularPolygon) {
        super(svgRegularPolygon);
    }
    public SVGRegularPolygon(HashMap<String, Data> attributes) {
        super();
        type = Type.REGULARPOLYGON;
        attr.put(RADIUS,new Data(1.0f));
        attr.put(COSTATS,new Data(3));
        changeAllAttributes(attributes);
    }

    public ArrayList<Point> getPointsArray() {
        int costats = attr.get(COSTATS).getIntegerValue();
        ArrayList<Point> points = new ArrayList<>();
        float r = attr.get(RADIUS).getFloatValue();

        double angle = 2*Math.PI/costats;
        float alpha = 0;
        for (int i = 0; i < costats; ++i) {
            points.add(new Point(r*Math.sin(alpha),r*Math.cos(alpha)));
            alpha += angle;
        }
        return points;
    }

    @Override
    public SVGRegularPolygon copy() {//throws IOException {
        return new SVGRegularPolygon(this);
    }
}
