package arekkuusu.skillful.common.impl;

import arekkuusu.gsl.api.registry.Skill;
import arekkuusu.skillful.Skillful;
import arekkuusu.skillful.common.impl.skill.SkillDodge;
import arekkuusu.skillful.common.impl.skill.SkillSlide;
import arekkuusu.skillful.common.impl.skill.MasteryData;
import net.minecraftforge.fml.RegistryObject;

public class SkillImp {

    public static final RegistryObject<Skill<MasteryData>> SLIDE = Skillful.SKILL_DEFERRED_REGISTER.register(
            "slide", () -> new SkillSlide(new Skill.Properties())
    );
    public static final RegistryObject<Skill<MasteryData>> DODGE = Skillful.SKILL_DEFERRED_REGISTER.register(
            "dodge", () -> new SkillDodge(new Skill.Properties())
    );

    public static void init() {
    }
}
