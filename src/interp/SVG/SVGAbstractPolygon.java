package interp.SVG;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 6/6/16.
 */
public abstract class SVGAbstractPolygon extends SVGObject {
    public SVGAbstractPolygon() {
        super();
    }

    protected class Point {
        protected float x;
        protected float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Point(double x, double y) {
            this.x = (float) x;
            this.y = (float) y;
        }

        @Override
        public String toString() {
            return x + "," +y;
        }
    }

    public SVGAbstractPolygon(SVGAbstractPolygon svgAbstractPolygon) {
        super(svgAbstractPolygon);
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
        ArrayList<Point> array = getPointsArray();
        String points = "";
        if (array.size() <= 0) return "";
        points += array.get(0);
        for (int i = 1; i < array.size(); ++i) {
            points += " ";
            points += array.get(i);
        }
        return points;

    }

    public abstract ArrayList<Point> getPointsArray();

}
