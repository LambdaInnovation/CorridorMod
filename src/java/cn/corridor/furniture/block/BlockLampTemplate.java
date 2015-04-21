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
public class BlockLampTemplate extends BlockTemplate {
	
	protected String name;
	protected boolean isActived = false;
	protected BlockMulti change;

	public BlockLampTemplate(Material mat) {
		super(mat);
		name = "lamp";
		if(getLightValue() != 0) {
			System.err.println("Block ac");
			isActived = true;//
			setCreativeTab(null);
		}
		change = (BlockMulti) Furnitures.instance.getBlock(isActived ? name : name + "_a", subID);
	}
	
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int w, 
			float a, float b, float c) {
    	System.err.println(change);
    	change.setMultiBlock(world, x, y, z, ForgeDirection.NORTH);
    	return true;
    }

}
