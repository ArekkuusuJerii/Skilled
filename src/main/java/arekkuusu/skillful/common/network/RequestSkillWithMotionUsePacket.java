package arekkuusu.skillful.common.network;

import arekkuusu.gsl.api.network.RequestSkillUsePacket;
import arekkuusu.gsl.api.util.GSLHelper;
import arekkuusu.gsl.api.util.WorldHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestSkillWithMotionUsePacket {

    RequestSkillUsePacket sub;
    Vector3d motion;

    public static void encoding(RequestSkillWithMotionUsePacket msg, PacketBuffer packetBuffer) {
        RequestSkillUsePacket.encoding(msg.sub, packetBuffer);
        packetBuffer.writeDouble(msg.motion.x);
        packetBuffer.writeDouble(msg.motion.y);
        packetBuffer.writeDouble(msg.motion.z);
    }

    public static RequestSkillWithMotionUsePacket decoding(PacketBuffer packetBuffer) {
        RequestSkillWithMotionUsePacket msg = new RequestSkillWithMotionUsePacket();
        msg.sub = RequestSkillUsePacket.decoding(packetBuffer);
        msg.motion = new Vector3d(packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readDouble());
        return msg;
    }

    public static void handle(RequestSkillWithMotionUsePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LivingEntity entity = WorldHelper.getEntityByUUID(msg.sub.uuid);
            Vector3d prevMotion = entity.getMotion(); //hacks!
            entity.setMotion(msg.motion);
            GSLHelper.triggerSkillOn(entity, msg.sub.skill);
            entity.setMotion(prevMotion);
        });
        ctx.get().setPacketHandled(true);
    }
}
