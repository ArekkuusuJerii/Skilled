package arekkuusu.skillful.common.impl.effect;

import arekkuusu.gsl.api.registry.Effect;
import arekkuusu.gsl.api.util.NBTHelper;
import arekkuusu.gsl.api.util.WorldHelper;
import arekkuusu.skillful.common.impl.EffectImpl;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;

public class EffectLockTarget extends Effect {

    public EffectLockTarget() {
        super(EffectImpl.LOCK_TARGET.get());
    }

    public WorldHelper.WeakWorldReference<LivingEntity> user;
    public Vector3d target;

    @Override
    public void apply() {
        LivingEntity user = this.user.get();
        if(this.user.exists()) {
            double dx = user.getPosX() - target.getX();
            double dz = user.getPosZ() - target.getZ();
            double angle = Math.atan2(dz, dx) * 180 / Math.PI;
            float rYaw = (float)(angle - user.rotationYaw);
            while (rYaw > 180) { rYaw -= 360; }
            while (rYaw < -180) { rYaw += 360; }
            rYaw += 90F;
            float prevYaw = user.rotationYaw;
            user.rotationYaw = (float)((double)user.rotationYaw + (double)rYaw * 0.15D);
            user.prevRotationYaw += user.rotationYaw - prevYaw;
            user.setRotationYawHead(user.rotationYaw);
        }
    }

    @Override
    public void writeNBT(CompoundNBT compound) {
        compound.putUniqueId("uuid", this.user.getID());
        NBTHelper.setVector(compound, "target", this.target);
    }

    @Override
    public void readNBT(CompoundNBT compound) {
        this.user = WorldHelper.WeakWorldReference.of(compound.getUniqueId("uuid"));
        this.target = NBTHelper.getVector(compound, "target");
    }
}
