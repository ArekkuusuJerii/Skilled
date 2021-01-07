package arekkuusu.skillful.common.impl.skill;

import arekkuusu.gsl.api.registry.Skill;
import net.minecraft.entity.LivingEntity;

public class SkillShieldParry extends Skill<MasteryData> {

    public SkillShieldParry(Properties properties) {
        super(properties);
    }

    @Override
    public void use(LivingEntity livingEntity, MasteryData masteryData) {

    }

    @Override
    public MasteryData create() {
        return new MasteryData();
    }
}
