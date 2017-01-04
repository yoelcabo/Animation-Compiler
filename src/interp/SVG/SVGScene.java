package interp.SVG;

import java.util.ArrayList;

/**
 * Created by yoel on 5/27/16.
 */
public class SVGScene extends SVGMovingCollection {
    private static final String FINAL = "</svg>";
    private int width;
    private int height;

    public SVGScene(ArrayList<SVGMovingObject> movingObjects) {
        super(new ArrayList<SVGSerializableParallelizable>(movingObjects));
    }

    public SVGScene(SVGMovingObject move) {
        super(move);
    }

    public SVGScene(SVGObject c, SVGMove r) {
        super(new SVGMovingObject(c,r));
    }

    public SVGScene(SVGScene newScene) {
        super(newScene);
        width = newScene.width;
        height = newScene.height;
    }

    public SVGScene(int width, int height, ArrayList<SVGSerializableParallelizable> svgSerializableParallelizables) {
        super(new ArrayList<>());
        for (SVGSerializableParallelizable ssp : svgSerializableParallelizables) {
            moves.add(ssp.copy());
        }
        this.width = width;
        this.height = height;
    }

    public String getSVGCode() {
        String svg = "";
        svg += getCabecera() + "\n";
        for (SVGSerializableParallelizable mv : moves) {
            SVGMovingObject move = (SVGMovingObject) mv;
            svg += move.getSVGCode() + "\n";
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public SVGScene copy() {
        return new SVGScene(this);
    }
}
