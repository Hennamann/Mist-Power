package com.henrikstabell.mistpower;

import com.henrikstabell.mistpower.block.BlockMistGenerator;
import com.henrikstabell.mistpower.block.TileEntityMistGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = MistPower.MODID, name = MistPower.NAME, version = MistPower.VERSION, dependencies = "required-after:mistcore;after:mistbiomes")
@Mod.EventBusSubscriber
public class MistPower {

    public static final String MODID = "mistpower";
    public static final String NAME = "Mist Power";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static MistPower INSTANCE;

    @SidedProxy(clientSide = "com.henrikstabell.mistpower.ClientProxy", serverSide = "com.henrikstabell.mistpower.CommonProxy")
    public static CommonProxy proxy;

    private static Logger logger;

    public static Block blockMistGenerator;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(blockMistGenerator = new BlockMistGenerator());
        GameRegistry.registerTileEntity(TileEntityMistGenerator.class, blockMistGenerator.getRegistryName().toString());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemBlock(blockMistGenerator).setRegistryName(blockMistGenerator.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerAllModels(ModelRegistryEvent event)
    {
        proxy.doLoadModels();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}