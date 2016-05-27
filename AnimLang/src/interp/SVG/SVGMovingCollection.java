package interp.SVG;

public class SVGMovingCollection<E extends SVGSerializableParallelizable> {
    ArrayList<E> moves; 
    float init;
    float end;

    public void serialize(SVGMovingCollection<E> moves2) {
      for (move : moves2) {
         moves.add(move.copy().inc(init));
      }
      init += moves2.init;
    }

    public void parallelize(SVGMovingCollection<E> moves2) {
      moves.addAll(moves2);
      init = Math.min(init,moves2.init);
      end = Math.max(end,moves2.end);
    }
}
