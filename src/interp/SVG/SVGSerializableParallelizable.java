package interp.SVG;

public abstract class SVGSerializableParallelizable {
    protected float init;
    protected float end;

    public SVGSerializableParallelizable() {
        init = 0;
        end = 0.01f;
    }
    public SVGSerializableParallelizable(float init, float end) {
        this.init = init;
        this.end = end;
    }

    public SVGSerializableParallelizable(float dur) {
        this();
        this.end = dur;
    }

    public SVGSerializableParallelizable(SVGSerializableParallelizable serialPar) {
        this.init = serialPar.init;
        this.end = serialPar.end;
    }

    void inc(float offset) {
        init += offset;
        end += offset;
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

    public abstract SVGSerializableParallelizable copy();

    public float getDur() {
        return end -init;
    }

}