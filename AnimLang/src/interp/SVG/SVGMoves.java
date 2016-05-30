package interp.SVG;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMoves extends SVGMovingCollection {

    // Guarrada de constructor
    public SVGMoves(ArrayList<SVGMove> moves) {
        super(new ArrayList<>());
        ArrayList<SVGSerializableParallelizable> auxMoves = new ArrayList<>();
        for (SVGMove m : moves) {
            auxMoves.add(m);
        }
        setMoves(auxMoves);
    }

    public SVGMoves(SVGMoves svgMoves) {
        super(svgMoves);
    }

    public String getSVGCode(float wait) {
        String svgcode = "";
        for (SVGSerializableParallelizable move : moves) {
            svgcode += ((SVGMove) move).getSVGCode(wait) + "\n";
        }
        return svgcode;
    }
}
