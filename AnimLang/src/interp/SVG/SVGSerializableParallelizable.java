package interp.SVG;

public abstract class SVGSerializableParallelizable {
    float init;
    float end;

    void inc(float offset) {
        init += offset;
        end += offset;
    }
}

