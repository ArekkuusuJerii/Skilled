package arekkuusu.skillful.common.impl.skill;

import arekkuusu.gsl.api.GSLChannel;
import arekkuusu.gsl.api.capability.data.Affected;
import arekkuusu.gsl.api.registry.Behavior;
import arekkuusu.gsl.api.registry.Effect;
import arekkuusu.gsl.api.registry.Skill;
import arekkuusu.gsl.api.util.GSLHelper;
import arekkuusu.gsl.api.util.WorldHelper;
import arekkuusu.gsl.common.network.PacketHandler;
import arekkuusu.skillful.common.impl.BehaviorImpl;
import arekkuusu.skillful.common.impl.EffectImpl;
import arekkuusu.skillful.common.impl.behavior.BehaviorWhileOnAction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SkillSlide extends Skill<MasteryData> {

    public SkillSlide(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public final double mod = 0.13000000312924387D * 0.5D;

    @Override
    public void use(LivingEntity livingEntity, MasteryData masteryData) {
        // Effect values
        double speed = livingEntity.getAttribute(Attributes.field_233821_d_).getValue();
        speed += mod * livingEntity.fallDistance;
        Vector3d motion = livingEntity.getMotion().normalize().mul(speed, 0, speed);

        Effect effect = EffectImpl.SLIDE.get().with(t -> {
            t.user = WorldHelper.WeakWorldReference.of(livingEntity);
            t.motion = motion;
        });
        Behavior behavior = BehaviorImpl.WHILE_ACTION.get().with(t -> {
            t.user = WorldHelper.WeakWorldReference.of(livingEntity);
            t.action = BehaviorWhileOnAction.Action.SHIFT;
        });
        Affected affected = Affected.Builder
                .of(effect)
                .following(behavior)
                .build("sliding");

        GSLChannel.sendEffectAddSync(livingEntity, affected);
        GSLHelper.applyEffectOn(livingEntity, affected);
    }

    @SubscribeEvent
    public void onSpawn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote() && event.getEntityLiving() instanceof ServerPlayerEntity && !GSLHelper.isSkillOn(event.getEntityLiving(), this)) {
            GSLHelper.applySkillOn(event.getEntityLiving(), this);
            GSLChannel.sendSkillAddSync((ServerPlayerEntity) event.getEntityLiving(), this);
        }
    }

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote()
                && isMoving(event.getEntityLiving())
                && event.getEntity().isSneaking()
                && event.getEntity().func_233570_aj_()
                && event.getEntityLiving().fallDistance <= 2) {
            if(!GSLHelper.isEffectWithIdOn(event.getEntityLiving(), "sliding")) {
                GSLHelper.triggerSkillOn(event.getEntityLiving(), this);
            }
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote()
                && isMoving(event.getEntityLiving())
                && event.getEntity().isSneaking()
                && event.getEntity().fallDistance <= 5) {
            if(!GSLHelper.isEffectWithIdOn(event.getEntityLiving(), "sliding")) {
                GSLHelper.triggerSkillOn(event.getEntityLiving(), this);
                event.setCanceled(true);
            }
        }
    }

    private boolean isMoving(LivingEntity entity) {
        Vector3d motion = entity.getMotion();
        return Math.abs(motion.x) > 0.01D || Math.abs(motion.z) > 0.01D;
    }

    @Override
    public MasteryData create() {
        return new MasteryData();
    }
}
