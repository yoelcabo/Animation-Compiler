package interp.SVG;

import java.util.ArrayList;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGScene extends SVGMovingCollection {
    private static final String FINAL = "";
    private String width;
    private String height;

    public SVGScene(ArrayList<SVGMovingObject> movingObjects) {
        super(new ArrayList<SVGSerializableParallelizable>(movingObjects));
        //super(movingObjects);
    }

    public SVGScene(SVGMovingObject move) {
        super(move);
    }

    public String getSVGCode() {
        String svg = "";
        svg += getCabecera() + "\n";
        for (SVGSerializableParallelizable mv : moves) {
            SVGMove move = (SVGMove) mv;
            svg += move.getSVGCode(init) + "\n";
        }
        svg += FINAL + "\n";
        return svg;
    }

    private String getCabecera() {
        return "<svg \n " +
                "  width=\""+width+"\"\n" +
                "  height=\""+height+"\"\n" +
                "  xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "  xmlns:xlink=\"http://www.w3.org/1999/xlink\" >";
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
