package interp.SVG;

import java.util.ArrayList;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGScene extends SVGMovingCollection<SVGMovingObject> {
    public SVGScene(ArrayList<SVGMovingObject> moves) {
        super(moves);
    }

    public SVGScene(SVGMovingObject move) {
        super(move);
    }
}
