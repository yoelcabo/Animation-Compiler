package tests;

import interp.Data;
import interp.SVG.SVGRotate;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGRotateTest {
    @org.junit.Test
    public void test4() {
        HashMap<String, Data> attributes = new HashMap<>();
        attributes.put("w",new Data(1.6f));
        attributes.put("dur",new Data(25));
        SVGRotate c = new SVGRotate(attributes);
        System.out.println(c.getSVGAttributes(0));
    }


}