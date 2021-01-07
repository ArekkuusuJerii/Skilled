package arekkuusu.skillful.common.impl.behavior;

import arekkuusu.gsl.api.registry.Behavior;
import arekkuusu.gsl.api.registry.data.BehaviorContext;
import arekkuusu.gsl.api.util.NBTHelper;
import arekkuusu.gsl.api.util.WorldHelper;
import arekkuusu.skillful.common.impl.BehaviorImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IStringSerializable;

import java.util.function.Function;

public class BehaviorWhileOnAction extends Behavior {

    public BehaviorWhileOnAction() {
        super(BehaviorImpl.WHILE_ACTION.get());
    }

    public WorldHelper.WeakWorldReference<LivingEntity> user;
    public Action action;

    @Override
    public void update(BehaviorContext context) {
        context.effect.apply();
    }

    @Override
    public boolean isAlive() {
        LivingEntity user = this.user.get();
        return this.user.exists() && this.action.fn.apply(user);
    }

    @Override
    public void writeNBT(CompoundNBT compound) {
        compound.putUniqueId("uuid", this.user.getID());
        NBTHelper.setEnum(compound, this.action, "action");
    }

    @Override
    public void readNBT(CompoundNBT compound) {
        this.user = WorldHelper.WeakWorldReference.of(compound.getUniqueId("uuid"));
        this.action = NBTHelper.getEnum(Action.class, compound, "action");
    }

    public enum Action implements IStringSerializable {
        SHIFT(Entity::isSneaking),
        GROUND(Entity::func_233570_aj_),
        AIR(e -> !e.func_233570_aj_());

        final Function<LivingEntity, Boolean> fn;

        Action(Function<LivingEntity, Boolean> fn) {
            this.fn = fn;
        }

        @Override
        public String func_176610_l() {
            return name();
        }
    }
}
