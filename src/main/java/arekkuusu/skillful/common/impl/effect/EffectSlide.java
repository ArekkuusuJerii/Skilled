package arekkuusu.skillful.common.impl.effect;

import arekkuusu.gsl.api.registry.Effect;
import arekkuusu.gsl.api.util.NBTHelper;
import arekkuusu.gsl.api.util.WorldHelper;
import arekkuusu.skillful.common.impl.EffectImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;

public class EffectSlide extends Effect {

    public EffectSlide() {
        super(EffectImpl.SLIDE.get());
    }

    public WorldHelper.WeakWorldReference<LivingEntity> user;
    public Vector3d motion;

    @Override
    public void apply() {
        LivingEntity user = this.user.get();
        if (this.user.exists()) {
            user.setMotion(user.getMotion().add(motion.x, 0D, motion.z));
            motion = motion.mul(0.95D, 0D, 0.95D);
        }
    }

    @Override
    public void writeNBT(CompoundNBT compound) {
        compound.putUniqueId("uuid", this.user.getID());
        NBTHelper.setVector(compound, "motion", this.motion);
    }

    @Override
    public void readNBT(CompoundNBT compound) {
        this.user = WorldHelper.WeakWorldReference.of(compound.getUniqueId("uuid"));
        this.motion = NBTHelper.getVector(compound, "motion");
    }
}
