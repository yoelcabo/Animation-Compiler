package interp.SVG;

import java.util.ArrayList;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMoves extends SVGMovingCollection<SVGMove>{

    public SVGMoves(ArrayList<SVGMove> moves) {
        super(moves);
    }

    public SVGMoves(SVGMove move) {
        super(move);
    }
}
