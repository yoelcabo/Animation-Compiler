package interp.SVG;

import interp.Data;

import java.util.HashMap;

/**
 * Created by yoel on 6/6/16.
 */
public class SVGText extends SVGObject {


    private static final String CONTENT = "content";
    private static final String SIZE = "size";

    public SVGText(SVGText SVGText) {
        super(SVGText);
        type = Type.TEXT;
    }

    public SVGText(HashMap<String, Data> attributes) {
        super();
        type = Type.TEXT;
        attr.put(CONTENT,new Data("Default Text"));
        attr.put(SIZE,new Data(20));
        changeAllAttributes(attributes);
    }

    @Override
    public String getObjDescriptor() {
        return "text";
    }

    public String getSVGContent() {
        return attr.get(CONTENT).getStringValue();
    }

    @Override
    public HashMap<String,String> getSVGAttributes() {
        HashMap<String,String> map = super.getSVGAttributes();
        map.put("text-anchor","middle");
        map.put("alignment-baseline","middle");
        map.put("font-size",attr.get(SIZE).getIntegerValue()+"");
        return map;
    }

    @Override
    public SVGText copy() {
        return new SVGText(this);
    }
}
