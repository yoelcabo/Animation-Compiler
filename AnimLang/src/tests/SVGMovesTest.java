package tests;

import interp.SVG.SVGMove;
import interp.SVG.SVGMoves;

import static org.junit.Assert.*;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMovesTest {

    public void create() {
        SVGMoves move = new SVGMoves(new SVGMove(25));
        SVGMoves move2 = new SVGMoves(new SVGMove(27));
        SVGMoves move3 = new SVGMoves(new SVGMove(28));
        SVGMoves move4 = new SVGMoves(new SVGMove(29));
        move.serialize(move2);
        move3.serialize(move4);
        move.parallelize(move3);
        System.out.print(move);





    }
    @org.junit.Test
    public void serialize() throws Exception {

    }

    @org.junit.Test
    public void parallelize() throws Exception {

    }

}