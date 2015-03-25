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

import net.minecraft.tileentity.TileEntity;
import cn.corridor.furniture.block.BlockTemplate;
import cn.liutils.template.block.RenderBlockMulti;
import cn.liutils.util.DebugUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonToken;

/**
 * The block info object to be deserialized from json.
 * @author WeathFolD
 */
public class BlockInfo {
	
	static GsonBuilder gb;
	static Gson gson;
	static {
		gb = new GsonBuilder();
		gson = gb.create();
	}
	
	//Type-Primitive info
	public String 
		typeStr,
		blockType,
		tileType,
		renderType;
	
	public enum Type {
		SINGLE, MULTI
	}
	
	//Structure
	public int[][] structure;
	public double[] center;
	
	//Block info
	public String name, soundType;
	public int count;
	
	//Render info
	public double scale;
	public float brightness;
	
	/**
	 * To be called by gson. Do not explicitly call this.
	 */
	public BlockInfo() {}
	
	public static BlockInfo create(JsonElement elem) {
		BlockInfo ret = gson.fromJson(elem, BlockInfo.class);
		ret.printDebug();
		return ret;
	}
	
	public Class<? extends BlockTemplate> getBlockClass() {
		return (Class<? extends BlockTemplate>) getPropClass(blockType);
	}
	
	public Class<? extends TileEntity> getTileClass() {
		return (Class<? extends TileEntity>) getPropClass(tileType);
	}
	
	public Class<? extends RenderBlockMulti> getRenderClass() {
		return (Class<? extends RenderBlockMulti>) getPropClass(renderType);
	}
	
	private Class<?> getPropClass(String str) {
		Class ret = null;
		try {
			ret = Class.forName("cn.corridor." + str);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private void printDebug() {
		System.out.println("---parse prop---");
		StringBuilder sb = new StringBuilder();
		sb.append("type: " + typeStr + "\n");
		sb.append("blockType: " + blockType + "\n");
		sb.append("tileType: " + tileType + "\n");
		sb.append("renderType: " + renderType + "\n");
		sb.append("structure: " + structure + "\n");
		for(int[] a : structure) {
			sb.append("-" + DebugUtils.formatArray(a) + "\n");
		}
		sb.append("name: " + name + "\n");
		sb.append("count: " + count + "\n");
		sb.append("scale: " + scale + "\n");
		System.out.print(sb.toString());
		System.out.println("---parse prop end---");
	}
	
	public Type getType() {
		return typeStr.equalsIgnoreCase("single") ? Type.SINGLE : Type.MULTI;
	}

}
