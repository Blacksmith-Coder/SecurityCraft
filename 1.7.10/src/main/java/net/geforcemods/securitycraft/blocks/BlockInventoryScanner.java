package net.geforcemods.securitycraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.api.CustomizableSCTE;
import net.geforcemods.securitycraft.gui.GuiHandler;
import net.geforcemods.securitycraft.tileentity.TileEntityInventoryScanner;
import net.geforcemods.securitycraft.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon furnaceIconTop;
	@SideOnly(Side.CLIENT)
	private IIcon furnaceIconFront;

	public BlockInventoryScanner(Material par1Material) {
		super(par1Material);
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4){
		super.onBlockAdded(par1World, par2, par3, par4);
		setDefaultDirection(par1World, par2, par3, par4);
	}

	/**
	 * set a blocks direction
	 */
	private void setDefaultDirection(World par1World, int par2, int par3, int par4){
		if(!par1World.isRemote){
			Block block = par1World.getBlock(par2, par3, par4 - 1);
			Block block1 = par1World.getBlock(par2, par3, par4 + 1);
			Block block2 = par1World.getBlock(par2 - 1, par3, par4);
			Block block3 = par1World.getBlock(par2 + 1, par3, par4);
			byte b0 = 3;

			if (block.isFullBlock() && !block1.isFullBlock())
				b0 = 3;

			if (block1.isFullBlock() && !block.isFullBlock())
				b0 = 2;

			if (block2.isFullBlock() && !block3.isFullBlock())
				b0 = 5;

			if (block3.isFullBlock() && !block2.isFullBlock())
				b0 = 4;

			par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
		}
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if(par1World.isRemote)
			return true;
		else{
			if(isFacingAnotherScanner(par1World, par2, par3, par4))
				par5EntityPlayer.openGui(SecurityCraft.instance, GuiHandler.INVENTORY_SCANNER_GUI_ID, par1World, par2, par3, par4);
			else
				PlayerUtils.sendMessageToPlayer(par5EntityPlayer, StatCollector.translateToLocal("tile.inventoryScanner.name"), StatCollector.translateToLocal("messages.invScan.notConnected"), EnumChatFormatting.RED);

			return true;
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		if(par1World.isRemote)
			return;

		int l = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (l == 0)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);

		if (l == 1)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);

		if (l == 2)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);

		if (l == 3)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);

		checkAndPlaceAppropriately(par1World, par2, par3, par4);
	}

	private void checkAndPlaceAppropriately(World par1World, int par2, int par3, int par4)
	{
		TileEntityInventoryScanner connectedScanner = getConnectedInventoryScanner(par1World, par2, par3, par4);

		if(connectedScanner == null)
			return;

		if(par1World.getBlockMetadata(par2, par3, par4) == 4)
		{
			for(int j = 1; j < Math.abs(par2 - connectedScanner.xCoord); j++)
			{
				par1World.setBlock(par2 - j, par3, par4, SCContent.inventoryScannerField, 1, 3);
			}
		}
		else if(par1World.getBlockMetadata(par2, par3, par4) == 5)
		{
			for(int j = 1; j < Math.abs(par2 - connectedScanner.xCoord); j++)
			{
				par1World.setBlock(par2 + j, par3, par4, SCContent.inventoryScannerField, 1, 3);
			}
		}
		else if(par1World.getBlockMetadata(par2, par3, par4) == 2)
		{
			for(int j = 1; j < Math.abs(par4 - connectedScanner.zCoord); j++)
			{
				par1World.setBlock(par2, par3, par4 - j, SCContent.inventoryScannerField, 2, 3);
			}
		}
		else if(par1World.getBlockMetadata(par2, par3, par4) == 3)
		{
			for(int j = 1; j < Math.abs(par4 - connectedScanner.zCoord); j++)
			{
				par1World.setBlock(par2, par3, par4 + j, SCContent.inventoryScannerField, 2, 3);
			}
		}

		CustomizableSCTE.link((CustomizableSCTE)par1World.getTileEntity(par2, par3, par4), connectedScanner);
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5Block, int par6)
	{
		if(par1World.isRemote)
			return;

		TileEntityInventoryScanner connectedScanner = null;

		for(int i = 1; i <= SecurityCraft.config.inventoryScannerRange; i++)
		{
			if(par1World.getBlock(par2 - i, par3, par4) == SCContent.inventoryScanner)
			{
				for(int j = 1; j < i; j++)
				{
					if(par1World.getBlock(par2 - j, par3, par4) == SCContent.inventoryScannerField)
						par1World.breakBlock(par2 - j, par3, par4, false);
				}

				connectedScanner = (TileEntityInventoryScanner) par1World.getTileEntity(par2 - i, par3, par4);
				break;
			}
		}

		for(int i = 1; i <= SecurityCraft.config.inventoryScannerRange; i++)
		{
			if(par1World.getBlock(par2 + i, par3, par4) == SCContent.inventoryScanner)
			{
				for(int j = 1; j < i; j++)
				{
					if(par1World.getBlock(par2 + j, par3, par4) == SCContent.inventoryScannerField)
						par1World.breakBlock(par2 + j, par3, par4, false);
				}

				connectedScanner = (TileEntityInventoryScanner) par1World.getTileEntity(par2 + i, par3, par4);
				break;
			}
		}

		for(int i = 1; i <= SecurityCraft.config.inventoryScannerRange; i++)
		{
			if(par1World.getBlock(par2, par3, par4 - i) == SCContent.inventoryScanner)
			{
				for(int j = 1; j < i; j++)
				{
					if(par1World.getBlock(par2, par3, par4 - j) == SCContent.inventoryScannerField)
						par1World.breakBlock(par2, par3, par4 - j, false);
				}

				connectedScanner = (TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4 - i);
				break;
			}
		}

		for(int i = 1; i <= SecurityCraft.config.inventoryScannerRange; i++)
		{
			if(par1World.getBlock(par2, par3, par4 + i) == SCContent.inventoryScanner)
			{
				for(int j = 1; j < i; j++)
				{
					if(par1World.getBlock(par2, par3, par4 + j) == SCContent.inventoryScannerField)
						par1World.breakBlock(par2, par3, par4 + j, false);
				}

				connectedScanner = (TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4 + i);
				break;
			}
		}

		for(int i = 0; i < ((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4)).getContents().length; i++)
		{
			if(((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4)).getContents()[i] != null)
				par1World.spawnEntityInWorld(new EntityItem(par1World, par2, par3, par4, ((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4)).getContents()[i]));
		}

		if(connectedScanner != null)
		{
			for(int i = 0; i < connectedScanner.getContents().length; i++)
			{
				connectedScanner.getContents()[i] = null;
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5Block, par6);
	}

	private boolean isFacingAnotherScanner(World world, int x, int y, int z)
	{
		return getConnectedInventoryScanner(world, x, y, z) != null;
	}

	/**
	 * @return the scanner, null if none found
	 */
	public static TileEntityInventoryScanner getConnectedInventoryScanner(World world, int x, int y, int z)
	{
		switch(world.getBlockMetadata(x, y, z))
		{
			case 4:
				if(world.getBlock(x, y, z) == SCContent.inventoryScanner)
				{
					for(int i = 0; i <= SecurityCraft.config.inventoryScannerRange; i++)
					{
						if(world.getBlock(x - i, y, z) != Blocks.air && world.getBlock(x - i, y, z) != SCContent.inventoryScannerField && world.getBlock(x - i, y, z) != SCContent.inventoryScanner)
							return null;

						if(world.getBlock(x - i, y, z) == SCContent.inventoryScanner && world.getBlockMetadata(x - i, y, z) == 5)
							return (TileEntityInventoryScanner)world.getTileEntity(x - i, y, z);
					}
				}

				return null;
			case 5:
				if(world.getBlock(x, y, z) == SCContent.inventoryScanner)
				{
					for(int i = 0; i <= SecurityCraft.config.inventoryScannerRange; i++)
					{
						if(world.getBlock(x + i, y, z) != Blocks.air && world.getBlock(x + i, y, z) != SCContent.inventoryScannerField && world.getBlock(x + i, y, z) != SCContent.inventoryScanner)
							return null;

						if(world.getBlock(x + i, y, z) == SCContent.inventoryScanner && world.getBlockMetadata(x + i, y, z) == 4)
							return (TileEntityInventoryScanner)world.getTileEntity(x + i, y, z);
					}
				}

				return null;
			case 2:
				if(world.getBlock(x, y, z) == SCContent.inventoryScanner)
				{
					for(int i = 0; i <= SecurityCraft.config.inventoryScannerRange; i++)
					{
						if(world.getBlock(x, y, z - i) != Blocks.air && world.getBlock(x, y, z - i) != SCContent.inventoryScannerField && world.getBlock(x, y, z - i) != SCContent.inventoryScanner)
							return null;

						if(world.getBlock(x, y, z - i) == SCContent.inventoryScanner && world.getBlockMetadata(x, y, z - i) == 3)
							return (TileEntityInventoryScanner)world.getTileEntity(x, y, z - i);
					}

					return null;
				}
				else if(world.getBlock(x, y, z) == SCContent.inventoryScannerField)
				{
					for(int i = 0; i < SecurityCraft.config.inventoryScannerRange; i++)
					{
						if(world.getBlock(x, y, z - i) == SCContent.inventoryScanner)
							return (TileEntityInventoryScanner)world.getTileEntity(x, y, z - i);
					}
				}

				break;
			case 3:
				if(world.getBlock(x, y, z) == SCContent.inventoryScanner)
				{
					for(int i = 0; i <= SecurityCraft.config.inventoryScannerRange; i++)
					{
						if(world.getBlock(x, y, z + i) != Blocks.air && world.getBlock(x, y, z + i) != SCContent.inventoryScannerField && world.getBlock(x, y, z + i) != SCContent.inventoryScanner)
							return null;

						if(world.getBlock(x, y, z + i) == SCContent.inventoryScanner && world.getBlockMetadata(x, y, z + i) == 2)
							return (TileEntityInventoryScanner)world.getTileEntity(x, y, z + i);
					}
				}

				return null;
			case 1:
				if(world.getBlock(x, y, z) == SCContent.inventoryScannerField)
				{
					for(int i = 0; i < SecurityCraft.config.inventoryScannerRange; i++)
					{
						if(world.getBlock(x - i, y, z) == SCContent.inventoryScanner)
							return (TileEntityInventoryScanner)world.getTileEntity(x - i, y, z);
					}
				}
		}

		return null;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	@Override
	public boolean canProvidePower(){
		return true;
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
	 * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){
		if(!(par1IBlockAccess.getTileEntity(par2, par3, par4) instanceof TileEntityInventoryScanner) || ((TileEntityInventoryScanner) par1IBlockAccess.getTileEntity(par2, par3, par4)).getType() == null)
			return 0 ;

		return (((TileEntityInventoryScanner) par1IBlockAccess.getTileEntity(par2, par3, par4)).getType().matches("redstone") && ((TileEntityInventoryScanner) par1IBlockAccess.getTileEntity(par2, par3, par4)).shouldProvidePower())? 15 : 0;
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){
		if(((TileEntityInventoryScanner) par1IBlockAccess.getTileEntity(par2, par3, par4)).getType() == null)
			return 0;

		return (((TileEntityInventoryScanner) par1IBlockAccess.getTileEntity(par2, par3, par4)).getType().matches("redstone") && ((TileEntityInventoryScanner) par1IBlockAccess.getTileEntity(par2, par3, par4)).shouldProvidePower())? 15 : 0;
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		if(par1 == 3 && par2 == 0)
			return furnaceIconFront;

		return par1 == 1 ? furnaceIconTop : (par1 == 0 ? furnaceIconTop : (par1 != par2 ? blockIcon : furnaceIconFront));
	}

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		blockIcon = par1IconRegister.registerIcon("furnace_side");
		furnaceIconFront = par1IconRegister.registerIcon("securitycraft:inventoryScanner");
		furnaceIconTop = par1IconRegister.registerIcon("furnace_top");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityInventoryScanner();
	}

}
