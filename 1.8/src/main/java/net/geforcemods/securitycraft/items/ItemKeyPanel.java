package net.geforcemods.securitycraft.items;

import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.blocks.IPasswordConvertible;
import net.geforcemods.securitycraft.misc.SCSounds;
import net.geforcemods.securitycraft.network.packets.PacketCPlaySoundAtPos;
import net.geforcemods.securitycraft.util.BlockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemKeyPanel extends Item {

	public ItemKeyPanel(){
		super();
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing par5EnumFacing, float hitX, float hitY, float hitZ){
		if(!par3World.isRemote){
			IPasswordConvertible.BLOCKS.forEach((pc) -> {
				if(BlockUtils.getBlock(par3World, pos) == ((IPasswordConvertible)pc).getOriginalBlock())
				{
					if(((IPasswordConvertible)pc).convert(par2EntityPlayer, par3World, pos) && !par2EntityPlayer.capabilities.isCreativeMode)
						par1ItemStack.stackSize--;
					SecurityCraft.network.sendToAll(new PacketCPlaySoundAtPos(par2EntityPlayer.posX, par2EntityPlayer.posY, par2EntityPlayer.posZ, SCSounds.LOCK.path, 1.0F));
				}
			});
			return true;
		}

		return false;
	}
}
