package com.henrikstabell.mistpower.block;

import com.henrikstabell.mistcore.api.IBiomeMist;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityMistGenerator extends TileEntity implements ITickable {

    public static int POWER_CAP = 2000;
    public static int POWER_TRANSFER = 50;
    public static int POWER_CREATION = 125;

    public static int CHECK_MIST_DELAY = 50;
    public static int CONSUME_MIST_DELAY = 10 * 40;
    public static int CONSUME_MIST_DELAY_RANDOM = 100;

    public static final String NBT_ENERGY = "energy";

    private int mistCheckTick = -1;
    private int mistConsumeTick = -1;

    public EnergyBuffer powerStorage;

    boolean isMistBiome = false;

    @Override
    public void onLoad()
    {
        powerStorage = new EnergyBuffer(POWER_CAP, POWER_TRANSFER, POWER_TRANSFER, 0);
    }

    @Override
    public void update() {
        if(!world.isRemote)
        {
            if (mistCheckTick-- <= 0)
            {
                mistCheckTick = CHECK_MIST_DELAY + world.rand.nextInt(10);

                BlockPos pos = getPos();
                Biome biome = world.getBiome(pos);

                isMistBiome = biome instanceof IBiomeMist;
            }

            if (isMistBiome)
            {
                createPower();
            }

            if (powerStorage.getEnergyStored() > 0)
            {
                outputPower();
            }
        }
    }

    protected void createPower()
    {
        if (mistCheckTick-- <= 0)
        {
            mistConsumeTick = CONSUME_MIST_DELAY + world.rand.nextInt(CONSUME_MIST_DELAY_RANDOM);

            BlockPos pos = getPos();
            Biome biome = world.getBiome(pos);

            if (biome instanceof IBiomeMist) {
                powerStorage.receiveEnergy(POWER_CREATION, false);
            }
            else
            {
                return;
            }
        }
    }

    protected void outputPower()
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            BlockPos pos = getPos().add(facing.getDirectionVec());

            if (world.isBlockLoaded(pos))
            {
                TileEntity tile = world.getTileEntity(pos);

                if (tile != null)
                {
                    if(tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
                    {
                        IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                        if (storage != null)
                        {
                            int power = powerStorage.extractEnergy(Integer.MAX_VALUE, true);

                            int drained = storage.receiveEnergy(power, false);

                            powerStorage.extractEnergy(drained, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        compound.setInteger(NBT_ENERGY, powerStorage.getEnergyStored());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        powerStorage.setEnergy(compound.getInteger(NBT_ENERGY));
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            return powerStorage != null;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            return (T) powerStorage;
        }
        return super.getCapability(capability, facing);
    }
}