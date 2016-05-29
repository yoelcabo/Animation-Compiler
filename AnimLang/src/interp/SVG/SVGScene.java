package interp.SVG;

import java.util.ArrayList;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGScene extends SVGMovingCollection<SVGMovingObject> {
    private static final String CABECERA = ;
    private static final String FINAL = ;

    public SVGScene(ArrayList<SVGMovingObject> moves) {
        super(moves);
    }

    public SVGScene(SVGMovingObject move) {
        super(move);
    }
    
    public String getSVGCode() {
        String svg = "";
        svg += CABECERA + "\n";
        for (SVGMovingObject move : moves) {
            svg += move.getSVGCode() + "\n";           
        }
        svg += FINAL + "\n";
        return svg;
    }
}
