/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.corridor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cn.annoreg.core.RegistrationManager;
import cn.annoreg.core.RegistrationMod;
import cn.annoreg.mc.RegMessageHandler;
import cn.corridor.core.register.CoreItems;
import cn.corridor.furniture.Furnitures;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * The man in the corridor main class.
 * @author WeathFolD
 */
@Mod(modid = "corridor", name = "Corridor")
@RegistrationMod(pkg = "cn.corridor.", res = "corridor", prefix = "cr_")
public class Corridor {

	@RegMessageHandler.WrapperInstance
	public static SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel("Corridor");
	
	@Instance("corridor")
	public static Corridor instance;
	
	public static CreativeTabs cct = new CreativeTabs("corridor") {
		@Override
		public Item getTabIconItem() {
			return CoreItems.logo;
		}
	};
	
	@EventHandler()
	public void preInit(FMLPreInitializationEvent event) {
		RegistrationManager.INSTANCE.registerAll(this, "PreInit");
		Furnitures.init();
	}
	
	@EventHandler()
	public void init(FMLInitializationEvent Init) { }
	
	@EventHandler()
	public void postInit(FMLPostInitializationEvent event) { }

}
