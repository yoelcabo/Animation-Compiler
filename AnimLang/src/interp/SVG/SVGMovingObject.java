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

    public SVGMovingObject(SVGObject c, SVGMove r) {
        this(c,new SVGMoves(r));
    }


    public String getSVGCode() {
        String svgcode = "";
        svgcode += moves.getSVGHeader();
        svgcode += "\n";
        svgcode += object.getSVGHeader() + "\n";
        svgcode += object.getSubObjects();
        svgcode += moves.getSVGEnd(init,object.getSVGEnd());

        return svgcode;
    }

    @Override
    public SVGMovingObject copy () {
        return new SVGMovingObject(this);
    }
}
