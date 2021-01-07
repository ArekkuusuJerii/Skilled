package arekkuusu.skillful;

import arekkuusu.gsl.api.registry.BehaviorType;
import arekkuusu.gsl.api.registry.EffectType;
import arekkuusu.gsl.api.registry.Skill;
import arekkuusu.skillful.client.ClientProxy;
import arekkuusu.skillful.client.render.ModRenders;
import arekkuusu.skillful.common.ServerProxy;
import arekkuusu.skillful.common.impl.BehaviorImpl;
import arekkuusu.skillful.common.impl.EffectImpl;
import arekkuusu.skillful.common.impl.SkillImp;
import arekkuusu.skillful.common.network.PacketHandler;
import arekkuusu.skillful.common.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Skillful.ID)
public class Skillful {

    public static final DeferredRegister<Skill<?>> SKILL_DEFERRED_REGISTER = DeferredRegister.create(Skill.getType(), Skillful.ID);
    public static final DeferredRegister<EffectType<?>> EFFECT_TYPE_DEFERRED_REGISTER = DeferredRegister.create(EffectType.getType(), Skillful.ID);
    public static final DeferredRegister<BehaviorType<?>> BEHAVIOR_TYPE_DEFERRED_REGISTER = DeferredRegister.create(BehaviorType.getType(), Skillful.ID);

    //Useful names
    public static final String ID = "skillful";
    public static final String NAME = "Skillful";
    public static final Logger LOG = LogManager.getLogger(NAME);
    private static IProxy proxy;

    public static IProxy getProxy() {
        return proxy;
    }

    public Skillful() {
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, IPMConfig.Holder.CLIENT_SPEC);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IPMConfig.Holder.COMMON_SPEC);
        //Mod Bus
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        SkillImp.init();
        EffectImpl.init();
        BehaviorImpl.init();
        modBus.addListener(this::setup);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::onFingerprintViolation);
        modBus.addListener(this::onModConfigEvent);
        SKILL_DEFERRED_REGISTER.register(modBus);
        EFFECT_TYPE_DEFERRED_REGISTER.register(modBus);
        BEHAVIOR_TYPE_DEFERRED_REGISTER.register(modBus);
        //Forge Bus
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::setupServer);
    }

    public void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    public void setupClient(final FMLClientSetupEvent event) {
        ModRenders.init();
    }

    public void setupServer(final FMLServerStartingEvent event) {

    }

    public void onModConfigEvent(ModConfig.ModConfigEvent event) {

    }

    // Bazinga *laugh track.wav*
    public void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
        LOG.warn("Invalid fingerprint detected!");
    }
}
