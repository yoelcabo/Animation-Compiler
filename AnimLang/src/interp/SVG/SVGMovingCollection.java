package interp.SVG;

import java.util.ArrayList;


public class SVGMovingCollection<E extends SVGSerializableParallelizable> {
    ArrayList<E> moves;
    float init;
    float end;

    public SVGMovingCollection(ArrayList<E> moves) {
        this.moves = moves;
        init = 0;
        end = 0;
        for (E move : moves) {
            if (move.getEnd() > end) end = move.getEnd();
        }
    }
    public SVGMovingCollection(E move) {
        this.moves = new ArrayList<>();
        moves.add(move);
        init = move.getInit();
        end = move.getEnd();
    }

    public SVGMovingCollection(SVGMovingCollection<E> orig) {
        this.moves = new ArrayList<>(orig.moves);
        this.init = orig.init;
        this.end = orig.end;
    }

    public void serialize(SVGMovingCollection<E> mc) {
      for (E move : mc.getMoves()) {
          E newMove = (E) move.copy();
          newMove.inc(end);
         moves.add(newMove);
      }
      end += mc.end;
    }

    public void parallelize(SVGMovingCollection<E> mc) {
      moves.addAll(mc.getMoves());
      init = Math.min(init,mc.init);
      end = Math.max(end,mc.end);
    }

    public float getInit() {
        return init;
    }

    public void setInit(float init) {
        this.init = init;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public ArrayList<E> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<E> moves) {
        this.moves = moves;
    }
    public SVGMovingCollection<E> copy() {
        return new SVGMovingCollection<E>(this);
    }

    @Override
    public String toString() {
        return "SVGMovingCollection{" +
                "moves=" + moves +
                ", init=" + init +
                ", end=" + end +
                '}';
    }

    public static SVGMovingCollection serial(SVGMovingCollection mvs1, SVGMovingCollection mvs2) {
        SVGMovingCollection ret = mvs1.copy();
        ret.serialize(mvs2);
        return ret;
    }

    public static SVGMovingCollection parallel(SVGMovingCollection mvs1, SVGMovingCollection mvs2) {
        SVGMovingCollection ret = mvs1.copy();
        ret.parallelize(mvs2);
        return ret;
    }
}
