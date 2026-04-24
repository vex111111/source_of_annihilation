package net.mcreator.sourceofannihilation.api.client.render.pipeline.attribute;


import net.mcreator.sourceofannihilation.api.client.render.CCRenderState;
import net.mcreator.sourceofannihilation.api.client.render.pipeline.VertexAttribute;
import net.mcreator.sourceofannihilation.util.client.colour.ColourRGBA;

/**
 * Sets colour in CCRS to the specified colour in the model.
 */
public final class ColourAttribute extends VertexAttribute<int[]> {
    public static final AttributeKey<int[]> attributeKey = AttributeKey.create("colour", int[]::new);

    private int[] colourRef;

    public ColourAttribute() {
        super(attributeKey);
    }

    @Override
    public boolean load(CCRenderState ccrs) {
        colourRef = ccrs.model.getAttribute(attributeKey);
        return colourRef != null || !ccrs.model.hasAttribute(attributeKey);
    }

    @Override
    public void operate(CCRenderState ccrs) {
        if (colourRef != null) {
            ccrs.colour = ColourRGBA.multiply(ccrs.baseColour, colourRef[ccrs.vertexIndex]);
        } else {
            ccrs.colour = ccrs.baseColour;
        }
    }
}
