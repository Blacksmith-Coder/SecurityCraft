package net.geforcemods.securitycraft.blocks.mines;

import java.util.Random;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.api.IOwnable;
import net.geforcemods.securitycraft.api.Owner;
import net.geforcemods.securitycraft.tileentity.TileEntityOwnable;
import net.geforcemods.securitycraft.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockMine extends BlockExplosive {

	public boolean cut;

	public BlockMine(Material par1Material, boolean cut) {
		super(par1Material);
		float f = 0.2F;
		float g = 0.1F;
		this.cut = cut;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, (g * 2.0F) / 2 + 0.1F, 0.5F + f);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5){
		if(par1World.getBlock(par2, par3 - 1, par4).getMaterial() != Material.air)
			return;
		else
			explode(par1World, par2, par3, par4);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4){
		if(par1World.getBlock(par2, par3 - 1, par4).getMaterial() == Material.glass || par1World.getBlock(par2, par3 - 1, par4).getMaterial() == Material.cactus || par1World.getBlock(par2, par3 - 1, par4).getMaterial() == Material.air || par1World.getBlock(par2, par3 - 1, par4).getMaterial() == Material.cake || par1World.getBlock(par2, par3 - 1, par4).getMaterial() == Material.plants)
			return false;
		else
			return true;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest){
		if(!world.isRemote)
			if(player != null && player.capabilities.isCreativeMode && !SecurityCraft.config.mineExplodesWhenInCreative)
				return super.removedByPlayer(world, player, x, y, z, willHarvest);
			else{
				explode(world, x, y, z);
				return super.removedByPlayer(world, player, x, y, z, willHarvest);
			}

		return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
	 */
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity){
		if(par1World.isRemote)
			return;
		else if(par5Entity instanceof EntityCreeper || par5Entity instanceof EntityOcelot || par5Entity instanceof EntityEnderman || par5Entity instanceof EntityItem)
			return;
		else if(par5Entity instanceof EntityLivingBase && !PlayerUtils.isPlayerMountedOnCamera((EntityLivingBase)par5Entity))
			explode(par1World, par2, par3, par4);
	}

	/**
	 * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
	 */
	public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10, World par11World){
		Explosion explosion = new Explosion(par11World, par1Entity, par2, par4, par6, par8);
		if(SecurityCraft.config.shouldSpawnFire)
			explosion.isFlaming = true;
		else
			explosion.isFlaming = false;
		explosion.isSmoking = par10;
		explosion.doExplosionA();


		explosion.doExplosionB(true);
		return explosion;
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3){
		return Item.getItemFromBlock(SCContent.mine);
	}

	@Override
	public Item getItem(World par1World, int par2, int par3, int par4){
		return Item.getItemFromBlock(SCContent.mine);
	}

	@Override
	public void activateMine(World world, int par2, int par3, int par4) {
		if(!world.isRemote){
			Owner owner = ((IOwnable)world.getTileEntity(par2, par3, par4)).getOwner();
			world.setBlock(par2, par3, par4, SCContent.mine);
			((IOwnable)world.getTileEntity(par2, par3, par4)).setOwner(owner.getUUID(), owner.getName());
		}
	}

	@Override
	public void defuseMine(World world, int par2, int par3, int par4) {
		if(!world.isRemote){
			Owner owner = ((IOwnable)world.getTileEntity(par2, par3, par4)).getOwner();
			world.setBlock(par2, par3, par4, SCContent.mineCut);
			((IOwnable)world.getTileEntity(par2, par3, par4)).setOwner(owner.getUUID(), owner.getName());
		}
	}

	@Override
	public void explode(World par1World, int par2, int par3, int par4) {
		if(!cut){
			par1World.breakBlock(par2, par3, par4, false);
			if(SecurityCraft.config.smallerMineExplosion)
				newExplosion((Entity)null, par2, par3, par4,  1.0F, true, true, par1World);
			else
				newExplosion((Entity)null, par2, par3, par4,  3.0F, true, true, par1World);
		}
	}

	@Override
	public boolean isActive(World world, int par2, int par3, int par4) {
		return !cut;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		if(cut)
			blockIcon = par1IconRegister.registerIcon("securitycraft:mineCut");
		else
			blockIcon = par1IconRegister.registerIcon("securitycraft:mine");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityOwnable();
	}

}
