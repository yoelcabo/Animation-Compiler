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

    @Override
    public SVGSerializableParallelizable copy() {

        return new SVGMovingObject(object.copy(),(SVGMoves) moves.copy());
    }
}
