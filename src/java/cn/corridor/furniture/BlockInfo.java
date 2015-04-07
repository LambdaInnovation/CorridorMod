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
package cn.corridor.furniture;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import cn.corridor.furniture.block.BlockTemplate;
import cn.corridor.furniture.client.render.RenderTemplate;
import cn.liutils.util.DebugUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * The block info object deserialized from json.
 * @author WeathFolD
 */
public class BlockInfo {
	
	static GsonBuilder gb;
	static Gson gson;
	static {
		gb = new GsonBuilder();
		gson = gb.create();
	}
	
	//Meta&Core info
	@Inherit
	public String 
	    blockType,
	    tileType,
	    renderType,
	    name;
	
	//Multiblock Struct Info
	@Inherit
	public int[][] structure;
	
	@Inherit
	public double[] center;
	
	public double scale = 1.0;
	
	public int 
	    texCount = 1, 
	    modelCount = 1;
	
	//Block properties
	@Inherit
	public String 
	    soundType,
	    material;
	
	@Inherit
	public Float 
	    lightLevel,
	    hardness;
	
	//Internally derived props
	public String 
	    textureName,
	    modelName;
	
	/**
	 * To be called by gson. Do not explicitly call this.
	 */
	public BlockInfo() {}
	
	public static BlockInfo create(JsonElement elem) {
		BlockInfo ret = gson.fromJson(elem, BlockInfo.class);
		ret.finishLoading();
		return ret;
	}
	
	public Class<? extends BlockTemplate> getBlockClass() {
		return (Class<? extends BlockTemplate>) getPropClass(blockType);
	}
	
	public Class<? extends TileEntity> getTileClass() {
		return (Class<? extends TileEntity>) getPropClass(tileType);
	}
	
	public Class<? extends RenderTemplate> getRenderClass() {
		return (Class<? extends RenderTemplate>) getPropClass(renderType);
	}
	
	public SoundType getStepSound() {
	    SoundType ret = null;
	    try {
	        ret = (SoundType) Block.class.getField("soundType" + soundType).get(null);
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ret;
	}
	
	public Material getMaterial() {
	    Material ret = null;
        try {
            ret = (Material) Material.class.getField(material).get(null);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ret;
	}
	
	private void finishLoading() {
	    if(textureName == null) textureName = name;
	    if(modelName == null) modelName = name;
	}
	
	private Class<?> getPropClass(String str) {
		Class ret = null;
		try {
			ret = Class.forName("cn.corridor.furniture." + str);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	void printDebug() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ name: " + name + "\n");
		sb.append("blockType: " + blockType + "\n");
		sb.append("tileType: " + tileType + "\n");
		sb.append("renderType: " + renderType + "\n");
		sb.append("structure:\n");
		for(int[] a : structure) {
			sb.append("\t-" + DebugUtils.formatArray(a) + "\n");
		}
		sb.append("name: " + name + "\n");
		sb.append("count: " + texCount + " " + modelCount + "\n");
		sb.append("scale: " + scale + "\n");
		System.out.print(sb.toString());
		System.out.println("}");
	}
}
