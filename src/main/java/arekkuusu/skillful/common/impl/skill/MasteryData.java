package arekkuusu.skillful.common.impl.skill;

import arekkuusu.gsl.api.registry.data.SerDes;
import net.minecraft.nbt.CompoundNBT;

public class MasteryData extends SerDes {

    public int mastery;
    public int repetition;

    @Override
    public void writeNBT(CompoundNBT compoundNBT) {
        compoundNBT.putInt("mastery", mastery);
        compoundNBT.putInt("repetition", repetition);
    }

    @Override
    public void readNBT(CompoundNBT compoundNBT) {
        mastery = compoundNBT.getInt("mastery");
        repetition = compoundNBT.getInt("repetition");
    }
}
