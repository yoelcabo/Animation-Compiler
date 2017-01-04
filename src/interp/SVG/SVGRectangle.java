package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 6/5/16.
 */
public class SVGRectangle extends SVGAbstractPolygon {


    private static final String HEIGHT = "height";
    private static final String WIDTH = "width";

    public SVGRectangle(SVGRectangle svgRectangle) {
        super(svgRectangle);
        type = Type.RECTANGLE;
    }
    public SVGRectangle(HashMap<String, Data> attributes) {
        super();
        type = Type.RECTANGLE;
        attr.put(HEIGHT,new Data(0));
        attr.put(WIDTH,new Data(0));
        changeAllAttributes(attributes);
    }

    public ArrayList<Point> getPointsArray() {
        ArrayList<Point> points = new ArrayList<>();
        float h = attr.get(HEIGHT).getFloatValue();
        float w = attr.get(WIDTH).getFloatValue();
        points.add(new Point(-w/2,-h/2));
        points.add(new Point(w/2,-h/2));
        points.add(new Point(w/2,h/2));
        points.add(new Point(-w/2,h/2));
        return points;
    }

    @Override
    public SVGRectangle copy() {
        return new SVGRectangle(this);
    }
}
