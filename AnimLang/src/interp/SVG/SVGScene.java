package interp.SVG;

import java.util.ArrayList;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGScene extends SVGMovingCollection {
    private static final String CABECERA = "";
    private static final String FINAL = "";

    // Abans era SVGMovingObject, pero perque compili d'una vegada ho he canviat a SVGSerializa...
    public SVGScene(ArrayList<SVGSerializableParallelizable> moves) {
        super(moves);
    }

    public SVGScene(SVGMovingObject move) {
        super(move);
    }
    
    public String getSVGCode() {
        String svg = "";
        svg += CABECERA + "\n";
        for (SVGSerializableParallelizable move : moves) {
            svg += ((SVGMovingObject) move).getSVGCode() + "\n";           
        }
        svg += FINAL + "\n";
        return svg;
    }
}
