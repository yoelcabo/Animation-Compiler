package interp.SVG;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMoves extends SVGMovingCollection {


    public SVGMoves(SVGMoves svgMoves) {
        super(svgMoves);
    }

    public SVGMoves(SVGMove svgMove) {
        super(svgMove);
    }

    public SVGMoves(ArrayList<SVGSerializableParallelizable> moves) {
        super(new ArrayList<>());
        ArrayList<SVGSerializableParallelizable> auxMoves = new ArrayList<>();
        for (SVGSerializableParallelizable m : moves) {
            auxMoves.add(m.copy());
        }
        setMoves(auxMoves);
    }

    public String getSVGCode(float wait) {
        String svgcode = "";
        for (SVGSerializableParallelizable move : moves) {
            svgcode += ((SVGMove) move).getSVGCode(wait) + "\n";
        }
        return svgcode;
    }

    public String getSVGHeader() {
        String head = "";
        for (int i = 0; i < moves.size(); ++i) {
            head+="<g>";
        }
        return head;
    }

    public String getSVGEnd(float wait, String objEnd) {
        ArrayList<SVGMove> moves = sortedArray();
        String svgCode = "";
        svgCode += objEnd + "\n";
        for (int i = 0; i < moves.size(); ++i) {
            svgCode += ((SVGMove) moves.get(i)).getSVGCode(wait) + "\n";
            svgCode+="</g>\n";
        }
        //svgCode += objEnd + "\n";

        svgCode += "\n";
        return svgCode;
    }

    private ArrayList<SVGMove> sortedArray() {
        ArrayList<SVGMove> ret = new ArrayList<>();
        for (SVGSerializableParallelizable mvx : moves) {
            SVGMove move = (SVGMove) mvx;
            if (move.getType() != SVGMove.Type.ROTATE && move.getType() != SVGMove.Type.TRANSLATE) ret.add(move);
        }
        for (SVGSerializableParallelizable mvx : moves) {
            SVGMove move = (SVGMove) mvx;
            if (move.getType() == SVGMove.Type.ROTATE) ret.add(move);
        }
        for (SVGSerializableParallelizable mvx : moves) {
            SVGMove move = (SVGMove) mvx;
            if (move.getType() == SVGMove.Type.TRANSLATE) ret.add(move);
        }
         return ret;
    }

    @Override
    public SVGMoves copy() {
        return new SVGMoves(this);
    }

}
