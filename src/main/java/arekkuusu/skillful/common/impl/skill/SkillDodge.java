package arekkuusu.skillful.common.impl.skill;

import arekkuusu.gsl.api.GSLChannel;
import arekkuusu.gsl.api.capability.data.Affected;
import arekkuusu.gsl.api.registry.Behavior;
import arekkuusu.gsl.api.registry.Effect;
import arekkuusu.gsl.api.registry.Skill;
import arekkuusu.gsl.api.util.GSLHelper;
import arekkuusu.gsl.api.util.TeamHelper;
import arekkuusu.gsl.api.util.TracerHelper;
import arekkuusu.gsl.api.util.WorldHelper;
import arekkuusu.gsl.common.impl.DefaultBehaviors;
import arekkuusu.skillful.common.impl.BehaviorImpl;
import arekkuusu.skillful.common.impl.EffectImpl;
import arekkuusu.skillful.common.impl.behavior.BehaviorWhileOnAction;
import arekkuusu.skillful.common.network.PacketHandler;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class SkillDodge extends Skill<MasteryData> {

    private static final Predicate<Entity> IS_LIVING = Predicates.and(e -> e instanceof LivingEntity, TeamHelper.NOT_SPECTATING);

    public SkillDodge(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void use(LivingEntity livingEntity, MasteryData masteryData) {
        // Effect values
        double speed = livingEntity.getAttribute(Attributes.field_233821_d_).getValue() * 4D;
        Vector3d look = livingEntity.getLook(1F).normalize();
        Vector3d motion = livingEntity.getMotion().normalize();
        double ab = look.x * motion.x + look.z * motion.z;
        double a = Math.sqrt(Math.pow(look.x, 2.0D) + Math.pow(look.z, 2.0D));
        double b = Math.sqrt(Math.pow(motion.x, 2.0D) + Math.pow(motion.z, 2.0D));
        double angle = Math.acos(ab / (a * b)) * 57.29577951308232D;

        if (angle > 45) {
            if (angle <= 135) {
                RayTraceResult result = TracerHelper.getLookedAt(livingEntity, 10, IS_LIVING);
                if (result.getType() != RayTraceResult.Type.MISS) {
                    double l = 1.2D * (1D - livingEntity.getPositionVec().distanceTo(result.getHitVec()) / 9D);
                    motion = motion.add(look.mul(l, l, l)).normalize();

                    Effect effect = EffectImpl.LOCK_TARGET.get().with(t -> {
                        t.user = WorldHelper.WeakWorldReference.of(livingEntity);
                        t.target = result.getHitVec();
                    });
                    Behavior behavior = BehaviorImpl.WHILE_ACTION.get().with(t -> {
                        t.user = WorldHelper.WeakWorldReference.of(livingEntity);
                        t.action = BehaviorWhileOnAction.Action.AIR;
                    });
                    Affected affected = Affected.Builder
                            .of(effect)
                            .following(behavior)
                            .build("locking");

                    GSLChannel.sendEffectAddSync(livingEntity, affected);
                    GSLHelper.applyEffectOn(livingEntity, affected);
                }
            }

            Vector3d launch = motion.mul(speed, 0D, speed);
            Effect effect = EffectImpl.MOTION.get().with(t -> {
                t.user = WorldHelper.WeakWorldReference.of(livingEntity);
                t.motion = launch;
            });
            Behavior behavior = DefaultBehaviors.ON_START.get().create();
            Affected affected = Affected.Builder
                    .of(effect)
                    .following(behavior)
                    .build("dodging");

            GSLChannel.sendEffectAddSync(livingEntity, affected);
            GSLHelper.applyEffectOn(livingEntity, affected);
        }
    }

    @SubscribeEvent
    public void onSpawn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote() && event.getEntityLiving() instanceof ServerPlayerEntity && !GSLHelper.isSkillOn(event.getEntityLiving(), this)) {
            GSLHelper.applySkillOn(event.getEntityLiving(), this);
            GSLChannel.sendSkillAddSync((ServerPlayerEntity) event.getEntityLiving(), this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean wasTapped;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onJumpOnAir(InputEvent.KeyInputEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        boolean isJumpKey = event.getKey() == Minecraft.getInstance().gameSettings.keyBindJump.getKey().getKeyCode();
        boolean isRelease = event.getAction() == GLFW.GLFW_RELEASE;
        if (player != null && /*!player.isCreative() &&*/ isJumpKey && isRelease) {
            if (!wasTapped && !player.func_233570_aj_()) {
                PacketHandler.sendRequestSkillWithMotionUsePacket(player, this, player.getMotion());
                wasTapped = true;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyTapUpdate(TickEvent.ClientTickEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (wasTapped && player != null && player.func_233570_aj_()) wasTapped = false;
    }

    @Override
    public MasteryData create() {
        return new MasteryData();
    }
}
