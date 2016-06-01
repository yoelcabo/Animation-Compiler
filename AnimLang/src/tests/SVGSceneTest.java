package tests;

import interp.Data;
import interp.SVG.*;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGSceneTest {
    @org.junit.Test
    public void test3() {
        SVGMoves move = new SVGMoves(new SVGMove(25));
        SVGMoves move2 = new SVGMoves(new SVGMove(27));
        SVGMoves move3 = new SVGMoves(new SVGMove(28));
        SVGMoves move4 = new SVGMoves(new SVGMove(29));
        move.serialize(move2);
        move3.serialize(move4);
        move.parallelize(move3);
        System.out.println(move);
        assertEquals(57.0,move.getEnd(),0.001);
        SVGObject object = new SVGObject(SVGObject.Type.CIRCLE);
        SVGScene scn = new SVGScene(new SVGMovingObject(object,move));
        scn.setHeight(200);
        scn.setWidth(200);
        System.out.print(scn.getSVGCode());
    }
    @org.junit.Test
    public void test5() {
        HashMap<String, Data> attributes = new HashMap<>();
        attributes.put("w",new Data(1.6f));
        attributes.put("dur",new Data(25));
        SVGRotate r = new SVGRotate(attributes);

        attributes = new HashMap<>();
        attributes.put("radius",new Data(1.6f));
        attributes.put("centerX",new Data(54));
        attributes.put("centerY",new Data(4));
        attributes.put("colorLine",new Data("0:100:0"));
        SVGTriangle c = new SVGTriangle(attributes);
        SVGScene scn = new SVGScene(c,r);
        System.out.print(scn.getSVGCode());

    }


}