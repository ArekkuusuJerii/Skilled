package arekkuusu.skillful.common.network;

import arekkuusu.gsl.api.network.RequestSkillUsePacket;
import arekkuusu.gsl.api.registry.Skill;
import arekkuusu.skillful.Skillful;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Skillful.ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void sendRequestSkillWithMotionUsePacket(LivingEntity entity, Skill<?> skill, Vector3d motion) {
        RequestSkillWithMotionUsePacket msg = new RequestSkillWithMotionUsePacket();
        msg.sub = new RequestSkillUsePacket();
        msg.sub.skill = skill;
        msg.sub.uuid = entity.getUniqueID();
        msg.motion = motion;
        INSTANCE.sendToServer(msg);
    }

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, RequestSkillWithMotionUsePacket.class, RequestSkillWithMotionUsePacket::encoding, RequestSkillWithMotionUsePacket::decoding, RequestSkillWithMotionUsePacket::handle);
    }
}
