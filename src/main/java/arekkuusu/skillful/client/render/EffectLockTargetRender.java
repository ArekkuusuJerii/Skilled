package arekkuusu.skillful.client.render;

import arekkuusu.gsl.api.render.EffectRenderer;
import arekkuusu.skillful.common.impl.effect.EffectLockTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class EffectLockTargetRender extends EffectRenderer<EffectLockTarget> {

    @Override
    public void render(EffectLockTarget effect, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        effect.apply();
    }
}
