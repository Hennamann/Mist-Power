package com.henrikstabell.mistpower;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void doLoadModels()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MistPower.blockMistGenerator), 0, new ModelResourceLocation(MistPower.blockMistGenerator.getRegistryName(), "inventory"));
    }
}