package arekkuusu.skillful.client.render;

import arekkuusu.gsl.api.registry.Effect;
import arekkuusu.gsl.api.registry.Skill;
import arekkuusu.gsl.api.registry.data.SerDes;
import arekkuusu.gsl.api.render.EffectRenderer;
import arekkuusu.gsl.api.render.EffectRendererDispatcher;
import arekkuusu.gsl.api.render.SkillRenderer;
import arekkuusu.gsl.api.render.SkillRendererDispatcher;
import arekkuusu.skillful.common.impl.effect.EffectLockTarget;

public class ModRenders {

    public static void init() {
        registerEffect(EffectLockTarget.class, new EffectLockTargetRender());
    }

    public static <T extends Effect> void registerEffect(Class<T> cl, EffectRenderer<T> render) {
        EffectRendererDispatcher.INSTANCE.registerRenderer(cl, render);
    }

    public static <T extends Skill<E>, E extends SerDes> void registerSkill(Class<T> cl, SkillRenderer<E> render) {
        SkillRendererDispatcher.INSTANCE.registerRenderer(cl, render);
    }
}
