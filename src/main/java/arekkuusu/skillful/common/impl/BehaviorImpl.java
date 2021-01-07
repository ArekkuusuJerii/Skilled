package arekkuusu.skillful.common.impl;

import arekkuusu.gsl.api.registry.BehaviorType;
import arekkuusu.skillful.Skillful;
import arekkuusu.skillful.common.impl.behavior.BehaviorWhileOnAction;
import net.minecraftforge.fml.RegistryObject;

public class BehaviorImpl {

    public static final RegistryObject<BehaviorType<BehaviorWhileOnAction>> WHILE_ACTION = Skillful.BEHAVIOR_TYPE_DEFERRED_REGISTER.register(
            "while_action", () -> BehaviorType.Builder.create(BehaviorWhileOnAction::new).build()
    );

    public static void init() {
    }
}
