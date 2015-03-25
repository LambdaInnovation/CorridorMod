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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import cn.corridor.furniture.BlockInfo.Type;
import cn.corridor.furniture.block.BlockTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * This class parses the json property file and creates the block instances.
 * @author WeathFolD
 */
public class BlockLoader {
	
	static final String BASE_PROP_NAME = "base";
	static final JsonParser parser = new JsonParser();
	
	JsonObject root; //The base JsonObject.
	
	BlockInfo base;
	Map<String, BlockInfo> blocks = new HashMap<String, BlockInfo>();
	
	Map<String, Object> regged = new HashMap<String, Object>();

	public BlockLoader(String json) {
		root = parser.parse(json).getAsJsonObject();
		init();
	}
	
	/**
	 * Load a block within the table.
	 * @return block if single, block[] if multi, or null if such setting doesn't exist.
	 */
	public Object loadBlock(String name) {
		BlockInfo inf = blocks.get(name);
		if(inf == null) {
			return null;
		}
		return inf.getType() == Type.SINGLE ? 
			createSingle(name, inf) : createMulti(name, inf);
	}
	
	public void loadAll() {
		for(Entry<String, BlockInfo> ent : blocks.entrySet()) {
			if(ent.getValue().getType() == Type.SINGLE) {
				createSingle(ent.getKey(), ent.getValue());
			} else {
				createMulti(ent.getKey(), ent.getValue());
			}
		}
	}
	
	/**
	 * Get the registered block instance by its name specified in Json. (Key)
	 */
	public Block getBlock(String name) {
		return (Block) regged.get(name); //This asserts that the type is correct.
	}
	
	/**
	 * Multi template version of getBlock.
	 */
	public Block getBlock(String name, int id) {
		return getBlockArray(name)[id];
	}
	
	public Block[] getBlockArray(String name) {
		return (Block[]) regged.get(name);
	}
	
	private Block createSingle(String id, BlockInfo inf) {
		Block ret = null;
		try {
			Constructor<? extends BlockTemplate> ctor = inf.getBlockClass().getConstructor(BlockInfo.class);
			ret = ctor.newInstance(inf);
			GameRegistry.registerBlock(ret, id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(ret != null) {
			regged.put(id, ret);
		}
		return ret;
	}
	
	private Block[] createMulti(String id, BlockInfo inf) {
		Block[] ret = new Block[inf.count];
		for(int i = 0; i < inf.count; ++i) {
			try {
				Constructor<? extends BlockTemplate> ctor = inf.getBlockClass().getConstructor(BlockInfo.class, Integer.TYPE);
				ret[i] = ctor.newInstance(inf, i);
				GameRegistry.registerBlock(ret[i], id + i);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		regged.put(id, ret);
		return ret;
	}
	
	private void init() {
		try {
			for(Entry<String, JsonElement> ent : root.entrySet()) {
				if(ent.getKey().equals(BASE_PROP_NAME)) {
					base = BlockInfo.create(ent.getValue());
					continue;
				}
				blocks.put(ent.getKey(), BlockInfo.create(ent.getValue()));
			}
		} catch(Exception e) {
			System.err.println("Unexpected error occured while parsing block property file.");
			e.printStackTrace();
		}
		
		checkInherit();
	}
	
	private void checkInherit() {
		for(BlockInfo inf : blocks.values()) { //Hard coded, but doesn't matter now?
			if(inf.typeStr == null) inf.typeStr = base.typeStr;
			if(inf.blockType == null) inf.blockType = base.blockType;
			if(inf.tileType == null) inf.tileType = base.tileType;
			if(inf.renderType == null) inf.renderType = base.renderType;
			if(inf.center == null) inf.center = base.center;
		}
	}

}
