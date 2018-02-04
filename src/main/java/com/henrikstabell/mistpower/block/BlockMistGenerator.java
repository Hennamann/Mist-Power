package com.henrikstabell.mistpower.block;

import com.henrikstabell.mistcore.api.IBiomeMist;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMistGenerator extends BlockContainer {

    public BlockMistGenerator() {
        super(Material.ROCK);
        setUnlocalizedName("mistgenerator");
        setRegistryName("mistgenerator");
        setCreativeTab(CreativeTabs.MISC);
        setHardness(1.5f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!player.getHeldItem(hand).isEmpty())
        {
            return false;
        }
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityMistGenerator)
            {
                player.sendMessage(new TextComponentString(I18n.translateToLocal("chat.mistgenerator.power") + " " + ((TileEntityMistGenerator) tile).powerStorage.getEnergyStored() + " RF"));
            }

            if (tile instanceof TileEntityMistGenerator && !(world.getBiome(pos) instanceof IBiomeMist)) {
                player.sendMessage(new TextComponentString(I18n.translateToLocal("chat.mistgenerator.nomist")));
            }
        }
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMistGenerator();
    }
}