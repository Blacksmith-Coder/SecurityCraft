package net.geforcemods.securitycraft.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class PacketCSetPlayerPositionAndRotation implements IMessage{

	private double x, y, z;
	private float rotationYaw, rotationPitch;

	public PacketCSetPlayerPositionAndRotation(){

	}

	public PacketCSetPlayerPositionAndRotation(double par1, double par2, double par3, float par4, float par5){
		x = par1;
		y = par2;
		z = par3;
		rotationYaw = par4;
		rotationPitch = par5;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		rotationYaw = buf.readFloat();
		rotationPitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(rotationYaw);
		buf.writeFloat(rotationPitch);
	}

	public static class Handler extends PacketHelper implements IMessageHandler<PacketCSetPlayerPositionAndRotation, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(PacketCSetPlayerPositionAndRotation message, MessageContext ctx) {
			Minecraft.getMinecraft().thePlayer.setPositionAndRotation(message.x, message.y, message.z, message.rotationYaw, message.rotationPitch);
			return null;
		}

	}

}
