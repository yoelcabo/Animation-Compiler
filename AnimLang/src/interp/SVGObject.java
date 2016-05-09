package interp;

public class SVGObject {
    public enum Type {CIRCLE,PATH,OBJPACK}; //etc
    public Type type;
    HashMap <String,Data> attr;
    SVGMove move;
}
