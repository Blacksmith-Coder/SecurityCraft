package net.geforcemods.securitycraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCUpdateNBTTag implements IMessage{

	private NBTTagCompound stack;
	private String itemName;

	public PacketCUpdateNBTTag(){

	}

	public PacketCUpdateNBTTag(ItemStack par1ItemStack){
		if(par1ItemStack != null && par1ItemStack.hasTagCompound()){
			stack = par1ItemStack.getTagCompound();
			itemName = par1ItemStack.getUnlocalizedName();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		stack = ByteBufUtils.readTag(buf);
		itemName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, stack);
		ByteBufUtils.writeUTF8String(buf, itemName);
	}

	public static class Handler extends PacketHelper implements IMessageHandler<PacketCUpdateNBTTag, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(PacketCUpdateNBTTag packet, MessageContext ctx) {
			if(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem().getUnlocalizedName().matches(packet.itemName)){
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().setTagCompound(packet.stack);;
			}

			return null;
		}
	}

}
