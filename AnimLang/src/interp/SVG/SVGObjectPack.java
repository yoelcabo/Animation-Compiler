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
        for (SVGObject object : svgObjectPack.content) {
            content.add(object);
        }
        changeAllAttributes(svgObjectPack.attr);

    }

    public SVGObjectPack(HashMap<String, Data> attributes) {
        throw new RuntimeException("ObjectPack has no attributes");
    }

    public SVGObjectPack(ArrayList<SVGObject> svgObjects) {
        super();
        type = Type.OBJ_PACK;
        content = new ArrayList<>();
        float cx = 0;
        float cy = 0;
        for (SVGObject object : svgObjects) {
            content.add(object);
            cx += object.getAttr().get(CENTERX).getFloatValue();
            cy += object.getAttr().get(CENTERY).getFloatValue();
        }
        cx /= svgObjects.size();
        cy /= svgObjects.size();
        int centerx = Math.round(cx);
        int centery = Math.round(cy);
        changeAttribute(CENTERX,new Data(centerx));
        changeAttribute(CENTERY,new Data(centery));

        for (SVGObject object : this.content) {
            object.moveCenter(-centerx,-centery);
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

    public void moveCenter(int centerx, int centery) {
        super.moveCenter(centerx,centery);
        for (SVGObject object : this.content) {
            object.moveCenter(centerx,centery);
        }
    }

    }
