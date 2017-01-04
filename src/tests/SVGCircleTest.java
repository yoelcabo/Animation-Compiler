package tests;

import interp.Data;
import interp.SVG.*;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by yoel on 5/31/16.
 */
public class SVGCircleTest {
    @org.junit.Test
    public void test3() {
        SVGCircle c = new SVGCircle(new HashMap<>());
        System.out.println(c);
    }

    @org.junit.Test
    public void test4() {
        HashMap<String, Data> attributes = new HashMap<>();
        attributes.put("radius",new Data(1.6f));
        attributes.put("centerX",new Data(54));
        attributes.put("centerY",new Data(4));
        attributes.put("colorLine",new Data("0:256:0"));

        SVGCircle c = new SVGCircle(attributes);
        System.out.println(c.getSVGAttributes());
    }


}