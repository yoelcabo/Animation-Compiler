package interp.SVG;

import interp.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yoel on 6/1/16.
 */
public class SVGObjectPack extends SVGObject {
    private ArrayList<SVGObject> content; //Només per OBJ_PACK

    public SVGObjectPack(SVGObjectPack svgObjectPack) {
        super(svgObjectPack);
    }
    public SVGObjectPack(HashMap<String, Data> attributes) {
        super();
        type = Type.OBJ_PACK;
        changeAllAttributes(attributes);
    }

    @Override
    public String getObjDescriptor() {
        return "g";
    }

    @Override
    public SVGObjectPack copy() {//throws IOException {
        return new SVGObjectPack(this);
    }

    public ArrayList<SVGObject> getContent() {
        return content;
    }

    public void setContent(ArrayList<SVGObject> content) {
        this.content = content;
    }

    @Override
    public String getSubObjects() {
        String svgcode = "";
        if (type == Type.OBJ_PACK) {
            for (SVGObject subobject : content) {
                svgcode += subobject.getSVGSingleCode() + "\n";
            }
        }
        return svgcode;
    }
}
