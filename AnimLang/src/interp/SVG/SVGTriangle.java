package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGTriangle extends SVGRegularPolygon {

    public SVGTriangle(SVGTriangle svgTriangle) {
        super(svgTriangle);
    }
    public SVGTriangle(HashMap<String, Data> attributes) {
        super(attributes);
        attr.put(SVGRegularPolygon.COSTATS,new Data(3));
    }

    @Override
    public SVGTriangle copy() {//throws IOException {
        return new SVGTriangle(this);
    }
}
