package interp.SVG;

public class SVGObject {
    public enum Type {CIRCLE,PATH,OBJPACK}; //etc
    public Type type;
    HashMap <String,Data> attr;
    ArrayList<SVGObject> content; //Només per objpack
}
