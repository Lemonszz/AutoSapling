package autosapling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid=AutoSapling.MODID, version=AutoSapling.VERSION)
public class AutoSapling
{
  public static final String MODID = "autosapling";
  public static final String VERSION = "1.11.2-1.3.5";
  
  public static int despawntime;
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
      Configuration config = new Configuration(event.getSuggestedConfigurationFile());  
	  config.load();
	  despawntime = config.getInt("spawnlingDespawnTime", config.CATEGORY_GENERAL, 1000, 0, Integer.MAX_VALUE, "How long a sapling should take to despawn (and attempt to plant) Default Minecraft is 6000");
	  config.save();
  }
  
  @Mod.EventHandler
  public void load(FMLInitializationEvent event)
  {
	  MinecraftForge.EVENT_BUS.register(this);
  }
  

  @SubscribeEvent
  public void itemDecay(ItemExpireEvent event)
  {
	  if(event.getEntityItem().getEntityItem() != null)
	  {
		  Item item = event.getEntityItem().getEntityItem().getItem();
		  
		  if(Block.getBlockFromItem(item) instanceof BlockSapling)
		  {
			  BlockSapling block = (BlockSapling)Block.getBlockFromItem(item);
			  
			  if(block.canPlaceBlockAt(event.getEntityItem().world, event.getEntity().getPosition()))
			  {
				  event.getEntityItem().world.setBlockState(event.getEntity().getPosition(), block.getStateFromMeta(item.getMetadata(event.getEntityItem().getEntityItem())));
			  }
		  }
	  }
  }
  
  @SubscribeEvent
  public void itemToss(ItemTossEvent event)
  {
	  if(event.getEntityItem().getEntityItem() != null)
	  {
		  Item item = event.getEntityItem().getEntityItem().getItem();
		  if(Block.getBlockFromItem(item) instanceof BlockSapling)
		  {
			  event.getEntityItem().lifespan = AutoSapling.despawntime;
		  }
	  }
  }
}
