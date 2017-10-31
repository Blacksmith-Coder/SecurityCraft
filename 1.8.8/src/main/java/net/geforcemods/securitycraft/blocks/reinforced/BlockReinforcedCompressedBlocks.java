package net.geforcemods.securitycraft.blocks.reinforced;

import java.util.List;

import net.geforcemods.securitycraft.blocks.BlockOwnable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReinforcedCompressedBlocks extends BlockOwnable
{
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockReinforcedCompressedBlocks.EnumType.class);

    public BlockReinforcedCompressedBlocks()
    {
        super(Material.rock, true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockReinforcedCompressedBlocks.EnumType.LAPIS));
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return ((BlockReinforcedCompressedBlocks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockReinforcedCompressedBlocks.EnumType[] aenumtype = BlockReinforcedCompressedBlocks.EnumType.values();

        for (BlockReinforcedCompressedBlocks.EnumType var3 : aenumtype)
        {
            list.add(new ItemStack(itemIn, 1, var3.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockReinforcedCompressedBlocks.EnumType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockReinforcedCompressedBlocks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {VARIANT});
    }

    public static enum EnumType implements IStringSerializable
    {
        LAPIS(0, "lapis", "lapis"),
        COAL(1, "coal", "coal");
        
        private static final BlockReinforcedCompressedBlocks.EnumType[] META_LOOKUP = new BlockReinforcedCompressedBlocks.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int meta, String name, String unlocalizedName)
        {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public static BlockReinforcedCompressedBlocks.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for(BlockReinforcedCompressedBlocks.EnumType var3 : values())
            {
            	META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }
}