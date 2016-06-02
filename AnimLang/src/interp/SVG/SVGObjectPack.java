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
        content = new ArrayList<>();
        type = Type.OBJ_PACK;
        float cx = 0;
        float cy = 0;
        for (SVGObject object : svgObjectPack.content) {
            content.add(object);
            cx += object.getAttr().get(CENTERX).getFloatValue();
            cy += object.getAttr().get(CENTERY).getFloatValue();
        }
        cx /= svgObjectPack.content.size();
        cy /= svgObjectPack.content.size();
        changeAttribute(CENTERX,new Data(cx));
        changeAttribute(CENTERY,new Data(cy));

    }

    public SVGObjectPack(HashMap<String, Data> attributes) {
        throw new RuntimeException("ObjectPack has no attributes");
    }

    public SVGObjectPack(ArrayList<SVGObject> svgObjects) {
        super();
        type = Type.OBJ_PACK;
        for (SVGObject object : svgObjects) {
            content.add(object);
        }
    }

    @Override
    public String getObjDescriptor() {
        return "g";
    }

    @Override
    public SVGObjectPack copy() {//throws IOException {
        return new SVGObjectPack(this);
    }

    @Override
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
