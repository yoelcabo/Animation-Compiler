package interp.SVG;

public class SVGMove extends SVGSerializableParallelizable {
    public enum Type {CIRCLE,PATH,OBJPACK}; //etc
    public Type type;
    HashMap <String,Data> attr;
}
