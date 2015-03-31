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

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cn.corridor.Corridor;
import cn.corridor.furniture.BlockLoader;
import cn.liutils.api.render.model.ITileEntityModel;
import cn.liutils.template.block.BlockMulti;
import cn.liutils.template.block.RenderBlockMulti;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 */
public class BlockTemplate extends BlockMulti {
    
    public BlockLoader parent;
	
	public Class<? extends TileEntity> tileType;
	
	@SideOnly(Side.CLIENT) 
    public RenderBlockMulti renderType;
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation texture;
	
	@SideOnly(Side.CLIENT)
	public ITileEntityModel model;
	
	@SideOnly(Side.CLIENT)
	public double[] center;
	
	@SideOnly(Side.CLIENT)
	public double scale;

	/**
	 * Simple template version Ctor.
	 */
	public BlockTemplate(Material mat) {
		super(mat);
		setCreativeTab(Corridor.cct);
	}
	
	public RenderBlockMulti getRender() {
		return renderType;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		TileEntity te = null;
		try {
			te = tileType.newInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return te;
	}

	@Override
	public double[] getRotCenter() {
		return center;
	}

}
