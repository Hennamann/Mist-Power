package com.henrikstabell.mistpower.block;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyBuffer extends EnergyStorage
{
    public EnergyBuffer(int capacity, int maxReceive, int maxExtract, int energy)
    {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }
}