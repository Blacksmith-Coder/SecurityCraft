package net.geforcemods.securitycraft.blocks.reinforced;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.geforcemods.securitycraft.tileentity.TileEntityOwnable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockReinforcedStainedGlass extends BlockBreakable implements ITileEntityProvider {

	private static final IIcon[] iicons = new IIcon[16];

	public BlockReinforcedStainedGlass(Material par1Material) {
		super("glass", par1Material, false);
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5Block, int par6){
		super.breakBlock(par1World, par2, par3, par4, par5Block, par6);
		par1World.removeTileEntity(par2, par3, par4);
	}

	@SideOnly(Side.CLIENT)
	public static int func_149997_b(int par1){
		return ~par1 & 15;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1Item, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < iicons.length; ++i)
			par3List.add(new ItemStack(par1Item, 1, i));
	}

	@Override
	public int damageDropped(int par1){
		return par1;
	}

	@Override
	public int quantityDropped(Random par1Random){
		return 1;
	}

	@Override
	protected boolean canSilkHarvest(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2){
		return iicons[par2 % iicons.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IIconRegister){
		for(int i = 0; i < iicons.length; ++i)
			iicons[i] = par1IIconRegister.registerIcon(getTextureName() + "_" + ItemDye.dyeIcons[func_149997_b(i)]);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityOwnable();
	}

}
