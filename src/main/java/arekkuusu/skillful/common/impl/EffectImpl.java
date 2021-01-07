package arekkuusu.skillful.common.impl;

import arekkuusu.gsl.api.registry.EffectType;
import arekkuusu.skillful.Skillful;
import arekkuusu.skillful.common.impl.effect.EffectMotion;
import arekkuusu.skillful.common.impl.effect.EffectLockTarget;
import arekkuusu.skillful.common.impl.effect.EffectSlide;
import net.minecraftforge.fml.RegistryObject;

public class EffectImpl {

    public static final RegistryObject<EffectType<EffectSlide>> SLIDE = Skillful.EFFECT_TYPE_DEFERRED_REGISTER.register(
            "slide", () -> EffectType.Builder.create(EffectSlide::new, BehaviorImpl.WHILE_ACTION.get()).build()
    );
    public static final RegistryObject<EffectType<EffectMotion>> MOTION = Skillful.EFFECT_TYPE_DEFERRED_REGISTER.register(
            "motion", () -> EffectType.Builder.create(EffectMotion::new).build()
    );
    public static final RegistryObject<EffectType<EffectLockTarget>> LOCK_TARGET = Skillful.EFFECT_TYPE_DEFERRED_REGISTER.register(
            "lock_target", () -> EffectType.Builder.create(EffectLockTarget::new).build()
    );

    public static void init() {
    }
}
