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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cn.corridor.Corridor;
import cn.corridor.furniture.BlockInfo;
import cn.corridor.furniture.BlockInfo.Type;
import cn.liutils.api.render.model.TileEntityModelCustom;
import cn.liutils.core.proxy.LIClientProps;
import cn.liutils.template.block.BlockMulti;
import cn.liutils.template.block.RenderBlockMulti;
import cn.liutils.util.GenericUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 */
public class BlockTemplate extends BlockMulti {
	
	final BlockInfo inf;
	
	Map<Class<? extends RenderBlockMulti>, RenderBlockMulti> createdRenderers = new HashMap();
	
	Class<? extends TileEntity> tileType;
	
	@SideOnly(Side.CLIENT) 
	Class<? extends RenderBlockMulti> renderType;
	
	public final Type type;
	int id; //Sub block ID, if is multi type.
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation texture;
	
	@SideOnly(Side.CLIENT)
	public TileEntityModelCustom model;

	/**
	 * Simple template version Ctor.
	 */
	public BlockTemplate(BlockInfo _inf) {
		super(Material.rock);
		assert(_inf.getType() == Type.SINGLE);
		
		inf = _inf;
		type = Type.SINGLE;
		deriveProps();
	}
	
	/**
	 * Multi template version.
	 */
	public BlockTemplate(BlockInfo _inf, int id) {
		super(Material.rock);
		assert(_inf.getType() == Type.MULTI);
		
		inf = _inf;
		type = Type.MULTI;
		deriveProps();
	}
	
	private void deriveProps() {
		try {
			SoundType snd = (SoundType) Block.class.getField("soundType" + inf.soundType).get(null);
			this.setStepSound(snd);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		tileType = inf.getTileClass();
		
		setCreativeTab(Corridor.cct);
		setBlockName("cr_" + inf.name);
		setBlockTextureName("corridor:" + inf.name + (type == Type.SINGLE ? "" : id));
		setLightLevel(inf.brightness);
		
		this.addSubBlock(inf.structure);
		
		this.finishInit();
		
		if(GenericUtils.getSide() == Side.CLIENT) {
			loadClient();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void loadClient() {
		texture = new ResourceLocation("corridor:textures/models/" + inf.name + (type == Type.SINGLE ? "" : id) + ".png");
		model = new TileEntityModelCustom(
			AdvancedModelLoader.loadModel(new ResourceLocation("corridor:models/" + inf.name + ".obj")));
		
		renderType = inf.getRenderClass();
		if(!createdRenderers.containsKey(renderType)) {
			try {
				createdRenderers.put(renderType, renderType.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public BlockInfo getInfo() {
		return inf;
	}
	
	public RenderBlockMulti getRender() {
		return createdRenderers.get(renderType);
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
		return inf.center;
	}

}
