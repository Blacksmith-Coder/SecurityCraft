package net.geforcemods.securitycraft.util;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.geforcemods.securitycraft.entity.EntitySecurityCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class PlayerUtils{

	/**
	 * Sets the given player's position and rotation. <p>
	 * 
	 * Args: player, x, y, z, yaw, pitch.
	 */
	public static void setPlayerPosition(EntityPlayer player, double x, double y, double z, float yaw, float pitch){
		player.setPositionAndRotation(x, y, z, yaw, pitch);
		player.setPositionAndUpdate(x, y, z);
	}

	/**
	 * Gets the EntityPlayer instance of a player (if they're online) using their name. <p>
	 * 
	 * Args: playerName.
	 */
	public static EntityPlayer getPlayerFromName(String par1){
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
			List<?> players = Minecraft.getMinecraft().theWorld.playerEntities;
			Iterator<?> iterator = players.iterator();

			while(iterator.hasNext()){
				EntityPlayer tempPlayer = (EntityPlayer) iterator.next();
				if(tempPlayer.getCommandSenderName().matches(par1)){
					return tempPlayer;
				}
			}

			return null;
		}else{
			List<?> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
			Iterator<?> iterator = players.iterator();

			while(iterator.hasNext()){
				EntityPlayer tempPlayer = (EntityPlayer) iterator.next();
				if(tempPlayer.getCommandSenderName().matches(par1)){
					return tempPlayer;
				}
			}

			return null;
		}
	}

	/**
	 * Returns true if a player with the given name is in the world.
	 * 
	 * Args: playerName.
	 */
	public static boolean isPlayerOnline(String par1) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
			for(int i = 0; i < Minecraft.getMinecraft().theWorld.playerEntities.size(); i++){
				EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().theWorld.playerEntities.get(i);

				if(player != null && player.getCommandSenderName().matches(par1)){
					return true;
				}
			}

			return false;
		}else{
			return (MinecraftServer.getServer().getConfigurationManager().func_152612_a(par1) != null);  	
		}
	}

	/**
	 * Sends the given player a chat message. <p>
	 * 
	 * Args: player, prefix, text, color.
	 */
	public static void sendMessageToPlayer(EntityPlayer player, String prefix, String text, EnumChatFormatting color){
		player.addChatComponentMessage(new ChatComponentText("[" + color + prefix + EnumChatFormatting.WHITE + "] " + text));
	}

	/**
	 * Sends the given {@link ICommandSender} a chat message. <p>
	 * 
	 * Args: sender, prefix, text, color.
	 */
	public static void sendMessageToPlayer(ICommandSender sender, String prefix, String text, EnumChatFormatting color){
		sender.addChatMessage(new ChatComponentText("[" + color + prefix + EnumChatFormatting.WHITE + "] " + text));
	}

	/**
	 * Returns true if the player is holding the given item.
	 * 
	 * Args: player, item.
	 */
	public static boolean isHoldingItem(EntityPlayer player, Item item){
		if(item == null && player.getCurrentEquippedItem() == null){
			return true;
		}

		return (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == item);
	}

	/**
	 * Is the entity mounted on to a security camera?
	 * 
	 * Args: entity.
	 */
	public static boolean isPlayerMountedOnCamera(EntityLivingBase entity) {
		return entity.ridingEntity != null && entity.ridingEntity instanceof EntitySecurityCamera;
	}
}