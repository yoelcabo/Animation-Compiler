package interp.SVG;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMovingObject extends SVGSerializableParallelizable {
    SVGObject object;
    SVGMoves moves;

    public SVGMovingObject(SVGObject object, SVGMoves moves) {
        super(moves.getInit(), moves.getEnd());
        this.object = object;
        this.moves = moves;
    }

    public SVGMovingObject(SVGMovingObject movObj) {
        super(movObj);
        this.object = new SVGObject(movObj.object);
        this.moves = new SVGMoves(movObj.moves);
    }


    public String getSVGCode() {
        String svgcode = "";
        svgcode += object.getSVGHeader() + "\n";
        svgcode += moves.getSVGCode(init);
        svgcode += object.getSVGEnd() + "\n";
        return svgcode;
    }

    @Override
    public SVGMovingObject copy () {
        return new SVGMovingObject(this);
    }
}