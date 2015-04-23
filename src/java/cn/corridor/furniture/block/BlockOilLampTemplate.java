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
package cn.corridor.furniture.block;

import cn.corridor.furniture.Furnitures;
import cn.liutils.template.block.BlockMulti;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * 
 * @author KSkun
 */
public class BlockOilLampTemplate extends BlockLampTemplate {

	public BlockOilLampTemplate(Material mat) {
		super(mat);
		name = "oillamp";
	}
	
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int w, 
			float a, float b, float c) {
    	String name = (getLightValue() != 0) ? this.name : this.name + "_a";
		System.err.println(name);
		((BlockMulti) Furnitures.instance.getBlock(name)).setMultiBlock(world, x, y, z, 
				ForgeDirection.NORTH);
    	return true;
    }

}
