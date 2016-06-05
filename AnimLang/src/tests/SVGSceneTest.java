package tests;

import interp.Data;
import interp.SVG.*;

import java.util.ArrayList;
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
        SVGScene scn2 = new SVGScene(c,r);
        scn.serialize(scn2);
        System.out.print(scn.getSVGCode());

    }

    @org.junit.Test
    public void supertest() {

        HashMap<String, Data> attrTriangle = new HashMap<>();
        attrTriangle.put("centerX",new Data(100));
        attrTriangle.put("centerY",new Data(50));
        attrTriangle.put("colorFill",new Data("0:255:0"));
        attrTriangle.put("colorLine",new Data("0:0:255"));
        attrTriangle.put("radius",new Data(50.0f));
        SVGTriangle triangle = new SVGTriangle(attrTriangle);
        HashMap<String, Data> attrEscalat = new HashMap<>();
        attrEscalat.put("factor",new Data(2));
        attrEscalat.put("dur", new Data(1));
        SVGScale escalat = new SVGScale(attrEscalat);
        HashMap<String, Data> attrRotacio = new HashMap<>();
        attrRotacio.put("w",new Data(2));
        attrRotacio.put("dur",new Data(1));
        SVGRotate rotacio = new SVGRotate(attrRotacio);
        SVGMoves mov = (SVGMoves) SVGMoves.parallel(new SVGMoves(escalat),new SVGMoves(rotacio));
        SVGScene objEnMov = new SVGScene(new SVGMovingObject(triangle,mov));
        for (int i = 1; i <= 2; ++i) {
            HashMap<String, Data> attrCircle = new HashMap<>();
            attrCircle.put("centerX", new Data(i*70 + 500));
            attrCircle.put("centerY",new Data(800));
            attrCircle.put("radius",new Data(50.0f));
            SVGCircle circle = new SVGCircle(attrCircle);
            objEnMov = (SVGScene) SVGScene.serial(objEnMov, new SVGScene(new SVGMovingObject(circle,escalat)));
        }
        objEnMov.setWidth(2000);
        objEnMov.setHeight(2000);
        System.out.println(objEnMov.getSVGCode());
    }

    @org.junit.Test
    public void supertest2() {

        HashMap<String, Data> attrTriangle = new HashMap<>();
        attrTriangle.put("centerX",new Data(100));
        attrTriangle.put("centerY",new Data(50));
        attrTriangle.put("colorFill",new Data("0:255:0"));
        attrTriangle.put("colorLine",new Data("0:0:255"));
        attrTriangle.put("radius",new Data(50.0f));
        Data triangle = new Data(new SVGTriangle(attrTriangle));
        HashMap<String, Data> attrEscalat = new HashMap<>();
        attrEscalat.put("factor",new Data(2));
        attrEscalat.put("dur", new Data(1));
        Data escalat = new Data(new SVGMoves(new SVGScale(attrEscalat)));
        HashMap<String, Data> attrRotacio = new HashMap<>();
        attrRotacio.put("w",new Data(2));
        attrRotacio.put("dur",new Data(1));
        Data rotacio = new Data(new SVGMoves(new SVGRotate(attrRotacio)));
        Data mov = new Data((SVGMoves) SVGMoves.parallel(escalat.getMoveValue(),rotacio.getMoveValue()));
        Data objEnMov = new Data(new SVGScene(new SVGMovingObject(triangle.getObjectValue(),mov.getMoveValue())));
        for (int i = 1; i <= 2; ++i) {
            HashMap<String, Data> attrCircle = new HashMap<>();
            attrCircle.put("centerX", new Data(i*70 + 500));
            attrCircle.put("centerY",new Data(800));
            attrCircle.put("radius",new Data(50.0f));
            Data circle = new Data(new SVGCircle(attrCircle));
            objEnMov = new Data((SVGScene) SVGScene.serial(objEnMov.getMovingObjectValue(), new SVGScene(new SVGMovingObject(circle.getObjectValue(),escalat.getMoveValue()))));
        }
        objEnMov.getMovingObjectValue().setWidth(2000);
        objEnMov.getMovingObjectValue().setHeight(2000);
        System.out.println(objEnMov.getMovingObjectValue().getSVGCode());
    }

    @org.junit.Test
    public void supertest3() {

        HashMap<String, Data> attrTriangle = new HashMap<>();
        attrTriangle.put("centerX",new Data(100));
        attrTriangle.put("centerY",new Data(50));
        attrTriangle.put("colorFill",new Data("0:255:0"));
        attrTriangle.put("colorLine",new Data("0:0:255"));
        attrTriangle.put("radius",new Data(50.0f));
        Data triangle = new Data(new SVGTriangle(attrTriangle));
        HashMap<String, Data> attrEscalat = new HashMap<>();
        attrEscalat.put("factor",new Data(2));
        attrEscalat.put("dur", new Data(1));
        Data escalat = new Data(new SVGMoves(new SVGScale(attrEscalat)));
        HashMap<String, Data> attrRotacio = new HashMap<>();
        attrRotacio.put("w",new Data(2));
        attrRotacio.put("dur",new Data(1));
        Data rotacio = new Data(new SVGMoves(new SVGRotate(attrRotacio)));
        HashMap<String, Data> attrTranslacio = new HashMap<>();
        attrTranslacio.put("x",new Data(-300));
        attrTranslacio.put("y",new Data(-300));
        Data translacio = new Data(new SVGMoves(new SVGTranslate(attrTranslacio)));


        Data mov = new Data((SVGMoves) SVGMoves.parallel(escalat.getMoveValue(),rotacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),rotacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),translacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));

        Data objEnMov = new Data(new SVGScene(new SVGMovingObject(triangle.getObjectValue(),mov.getMoveValue())));
        for (int i = 1; i <= 2; ++i) {
            HashMap<String, Data> attrCircle = new HashMap<>();
            attrCircle.put("centerX", new Data(i*70 + 500));
            attrCircle.put("centerY",new Data(800));
            attrCircle.put("radius",new Data(50.0f));
            Data circle = new Data(new SVGCircle(attrCircle));
            objEnMov = new Data((SVGScene) SVGScene.serial(objEnMov.getMovingObjectValue(), new SVGScene(new SVGMovingObject(circle.getObjectValue(),escalat.getMoveValue()))));
        }
        objEnMov.getMovingObjectValue().setWidth(2000);
        objEnMov.getMovingObjectValue().setHeight(2000);
        System.out.println(objEnMov.getMovingObjectValue().getSVGCode());
    }

    @org.junit.Test
    public void supertest4() {

        HashMap<String, Data> attrTriangle = new HashMap<>();
        attrTriangle.put("centerX",new Data(100));
        attrTriangle.put("centerY",new Data(50));
        attrTriangle.put("colorFill",new Data("0:255:0"));
        attrTriangle.put("colorLine",new Data("0:0:255"));
        attrTriangle.put("radius",new Data(50.0f));
        Data triangle = new Data(new SVGTriangle(attrTriangle));
        HashMap<String, Data> attrEscalat = new HashMap<>();
        attrEscalat.put("factor",new Data(2));
        attrEscalat.put("dur", new Data(1));
        Data escalat = new Data(new SVGMoves(new SVGScale(attrEscalat)));
        HashMap<String, Data> attrRotacio = new HashMap<>();
        attrRotacio.put("w",new Data(2));
        attrRotacio.put("dur",new Data(1));
        Data rotacio = new Data(new SVGMoves(new SVGRotate(attrRotacio)));
        HashMap<String, Data> attrTranslacio = new HashMap<>();
        attrTranslacio.put("x",new Data(-300));
        attrTranslacio.put("y",new Data(-300));
        Data translacio = new Data(new SVGMoves(new SVGTranslate(attrTranslacio)));


        Data mov = new Data((SVGMoves) SVGMoves.parallel(escalat.getMoveValue(),rotacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),rotacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),translacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));

        Data objEnMov = new Data(new SVGScene(new SVGMovingObject(triangle.getObjectValue(),mov.getMoveValue())));
        for (int i = 1; i <= 2; ++i) {
            HashMap<String, Data> attrCircle = new HashMap<>();
            attrCircle.put("centerX", new Data(i*70 + 500));
            attrCircle.put("centerY",new Data(800));
            attrCircle.put("radius",new Data(50.0f));
            Data circle = new Data(new SVGCircle(attrCircle));
            objEnMov = new Data((SVGScene) SVGScene.serial(objEnMov.getMovingObjectValue(), new SVGScene(new SVGMovingObject(circle.getObjectValue(),escalat.getMoveValue()))));
        }
        objEnMov.getMovingObjectValue().setWidth(2000);
        objEnMov.getMovingObjectValue().setHeight(2000);
        System.out.println(objEnMov.getMovingObjectValue().getSVGCode());
    }

    @org.junit.Test
    public void objPackTest() {

        HashMap<String, Data> attrTriangle = new HashMap<>();
        attrTriangle.put("centerX",new Data(100));
        attrTriangle.put("centerY",new Data(100));
        attrTriangle.put("colorFill",new Data("0:255:0"));
        attrTriangle.put("colorLine",new Data("0:0:255"));
        attrTriangle.put("radius",new Data(50.0f));
        Data triangle1 = new Data(new SVGTriangle(attrTriangle));
        attrTriangle.put("centerX",new Data(200));
        attrTriangle.put("centerY",new Data(200));
        Data triangle2 = new Data(new SVGTriangle(attrTriangle));
        attrTriangle.put("centerX",new Data(300));
        attrTriangle.put("centerY",new Data(300));
        Data triangle3 = new Data(new SVGTriangle(attrTriangle));
        attrTriangle.put("centerX",new Data(400));
        attrTriangle.put("centerY",new Data(400));
        Data triangle4 = new Data(new SVGTriangle(attrTriangle));
        ArrayList<SVGObject> triangles = new ArrayList<>();
        triangles.add(triangle1.getObjectValue());
        triangles.add(triangle2.getObjectValue());
        triangles.add(triangle3.getObjectValue());
        triangles.add(triangle4.getObjectValue());
        Data triangle = new Data(new SVGObjectPack(triangles));
        HashMap<String, Data> attrEscalat = new HashMap<>();
        attrEscalat.put("factor",new Data(2));
        attrEscalat.put("dur", new Data(1));
        Data escalat = new Data(new SVGMoves(new SVGScale(attrEscalat)));
        HashMap<String, Data> attrRotacio = new HashMap<>();
        attrRotacio.put("w",new Data(2));
        attrRotacio.put("dur",new Data(1));
        Data rotacio = new Data(new SVGMoves(new SVGRotate(attrRotacio)));
        HashMap<String, Data> attrTranslacio = new HashMap<>();
        attrTranslacio.put("x",new Data(-300));
        attrTranslacio.put("y",new Data(-300));
        Data translacio = new Data(new SVGMoves(new SVGTranslate(attrTranslacio)));


        Data mov = new Data((SVGMoves) SVGMoves.parallel(escalat.getMoveValue(),rotacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),rotacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),translacio.getMoveValue()));
        mov = new Data((SVGMoves) SVGMoves.serial(mov.getMoveValue(),escalat.getMoveValue()));

        Data objEnMov = new Data(new SVGScene(new SVGMovingObject(triangle.getObjectPackValue(),mov.getMoveValue())));
        for (int i = 1; i <= 2; ++i) {
            HashMap<String, Data> attrCircle = new HashMap<>();
            attrCircle.put("centerX", new Data(i*70 + 500));
            attrCircle.put("centerY",new Data(800));
            attrCircle.put("radius",new Data(50.0f));
            Data circle = new Data(new SVGCircle(attrCircle));
            objEnMov = new Data((SVGScene) SVGScene.serial(objEnMov.getMovingObjectValue(), new SVGScene(new SVGMovingObject(circle.getObjectValue(),escalat.getMoveValue()))));
        }
        objEnMov.getMovingObjectValue().setWidth(2000);
        objEnMov.getMovingObjectValue().setHeight(2000);
        System.out.println(objEnMov.getMovingObjectValue().getSVGCode());

    }
}

