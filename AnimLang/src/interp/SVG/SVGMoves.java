package interp.SVG;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMoves extends SVGMovingCollection<SVGMove>{

    public SVGMoves(ArrayList<SVGMove> moves) {
        super(moves);
    }

    public String getSVGCode(float wait) {
        String svgcode = "";
        for (SVGMove move: moves) {
            svgcode += move.getSVGCode(wait) + "\n";
        }
        return svgcode;
    }
}
