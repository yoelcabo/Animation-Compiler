package interp.SVG;

import java.util.ArrayList;


public class SVGMovingCollection extends SVGSerializableParallelizable {
    // abans estava ArrayList<E> moves, pero en principi E sempre sera una subclasse de SVGSerializableParallelizable
    ArrayList<SVGSerializableParallelizable> moves;
    // no s'han de declarar aqui perque ja s'hereden de la classe pare
    //float init;
    //float end;

    // CONSTRUCTORES //

    public SVGMovingCollection(ArrayList<SVGSerializableParallelizable> moves) {
        super(0, 0);
        this.moves = new ArrayList<>(moves);
        for (SVGSerializableParallelizable move : moves) {
            if (move.getEnd() > end) end = move.getEnd();
        }
    }
    public SVGMovingCollection(SVGSerializableParallelizable move) {
        super(move.getInit(), move.getEnd());
        this.moves = new ArrayList<>();
        moves.add(move);
    }

    public SVGMovingCollection (SVGMovingCollection movColl) {
        super(movColl);
        moves = new ArrayList<>();
        for (SVGSerializableParallelizable sps : movColl.moves) {
            moves.add(sps.copy());
        }
    }



    public void serialize(SVGMovingCollection mc) {
        //System.out.println("size ini -> " + moves.size());
      for (SVGSerializableParallelizable move : mc.getMoves()) {
            SVGSerializableParallelizable newMove = move.copy();
            newMove.inc(end);
            moves.add(newMove);
      }
      end += mc.end;
      //System.out.println("size ini -> " + moves.size());
    }

    public void parallelize(SVGMovingCollection mc) {
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

    public ArrayList<SVGSerializableParallelizable> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<SVGSerializableParallelizable> moves) {
        this.moves = moves;
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

    @Override
    public SVGMovingCollection copy() {
        return new SVGMovingCollection(this);
    }
}
