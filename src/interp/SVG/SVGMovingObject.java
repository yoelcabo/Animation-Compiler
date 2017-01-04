package interp.SVG;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGMovingObject extends SVGSerializableParallelizable {
    SVGObject object;
    SVGMoves moves;

    public SVGMovingObject(SVGObject object, SVGMoves moves) {
        super(moves.getInit(), moves.getEnd());
        this.object = object.copy();
        this.moves = moves.copy();
    }

    public SVGMovingObject(SVGMovingObject movObj) {
        super(movObj);
        this.object = movObj.object.copy();
        this.moves = movObj.moves.copy();
    }

    public SVGMovingObject(SVGObject c, SVGMove r) {
        this(c,new SVGMoves(r));
    }


    public String getSVGCode() {
        String svgcode = "";
        svgcode += "<g transform=\""+object.getSVGIniTransform()+"\">\n";
        svgcode += moves.getSVGHeader();
        svgcode += "\n";
        svgcode += object.getSVGHeader() + "\n";
        svgcode += object.getSubObjects();
        svgcode += moves.getSVGEnd(init,object.getSVGEnd());
        svgcode += "</g>\n";

        return svgcode;
    }

    @Override
    public SVGMovingObject copy () {
        return new SVGMovingObject(this);
    }


}
