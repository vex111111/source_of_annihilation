package net.mcreator.sourceofannihilation.api.client.render.pipeline.attribute;

import net.mcreator.sourceofannihilation.api.client.render.CCRenderState;
import net.mcreator.sourceofannihilation.api.client.render.lighting.LC;
import net.mcreator.sourceofannihilation.api.client.render.pipeline.VertexAttribute;
import net.mcreator.sourceofannihilation.util.vec.Transformation;
import net.mcreator.sourceofannihilation.util.vec.Vector3;

/**
 * Uses the position of the lightmatrix to compute LC if not provided
 */
public final class LightCoordAttribute extends VertexAttribute<LC[]> {
    public static final AttributeKey<LC[]> attributeKey = AttributeKey.create("light_coord", LC[]::new);

    private final Vector3 vec = new Vector3();//for computation

    private LC[] lcRef;

    public LightCoordAttribute() {
        super(attributeKey);
    }

    @Override
    public boolean load(CCRenderState ccrs) {
        lcRef = ccrs.model.getAttribute(attributeKey);
        if (ccrs.model.hasAttribute(attributeKey)) {
            return lcRef != null;
        }

        ccrs.pipeline.addDependency(ccrs.sideAttrib);
        ccrs.pipeline.addRequirement(Transformation.operationIndex);
        return true;
    }

    @Override
    public void operate(CCRenderState ccrs) {
        if (lcRef != null) {
            ccrs.lc.set(lcRef[ccrs.vertexIndex]);
        } else {
            ccrs.lc.compute(vec.set(ccrs.vert.vec), ccrs.side);
        }
    }
}
