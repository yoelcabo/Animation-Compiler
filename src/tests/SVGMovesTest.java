package tests;

import interp.SVG.SVGMove;
import interp.SVG.SVGMoves;

import static org.junit.Assert.*;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMovesTest {

    @org.junit.Test
    public void test1() {
        SVGMoves move = new SVGMoves(new SVGMove(25));
        SVGMoves move2 = new SVGMoves(new SVGMove(27));
        SVGMoves move3 = new SVGMoves(new SVGMove(28));
        SVGMoves move4 = new SVGMoves(new SVGMove(29));
        move.parallelize(move2);
        move.parallelize(move4);
        move4.serialize(move);
        System.out.println(move4);
        assertEquals(58.0,move4.getEnd(),0.001);
    }

    @org.junit.Test
    public void test2() {
        SVGMoves move = new SVGMoves(new SVGMove(25));
        SVGMoves move2 = new SVGMoves(new SVGMove(27));
        SVGMoves move3 = new SVGMoves(new SVGMove(28));
        SVGMoves move4 = new SVGMoves(new SVGMove(29));
        move.serialize(move2);
        move3.serialize(move4);
        move.parallelize(move3);
        System.out.println(move);
        assertEquals(57.0,move.getEnd(),0.001);
    }

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
        System.out.print(move.getSVGCode(0));
    }


}