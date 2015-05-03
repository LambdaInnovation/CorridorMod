/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import cn.corridor.furniture.block.BlockTemplate;
import cn.corridor.furniture.client.render.RenderTemplate;
import cn.liutils.api.render.model.ITileEntityModel;
import cn.liutils.api.render.model.TileEntityModelCustom;
import cn.liutils.template.block.ItemBlockMulti;
import cn.liutils.util.GenericUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class parses the json property file and creates the block instances.
 * @author WeathFolD
 */
public class BlockLoader {
    
    static Map<Class<? extends RenderTemplate>, RenderTemplate> createdRenders = new HashMap();
	
	static final String DEFAULT_PROP_NAME = "default";
	static final JsonParser parser = new JsonParser();
	
	JsonObject root; //The base JsonObject.
	
	BlockInfo base;
	Map<String, BlockInfo> blocks = new HashMap<String, BlockInfo>();
	
	Map<String, Block[]> regged = new HashMap<String, Block[]>();

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
		return create(name, inf);
	}
	
	public void loadAll() {
		for(Entry<String, BlockInfo> ent : blocks.entrySet()) {
			create(ent.getKey(), ent.getValue());
		}
		System.out.println("CM BlockLoader loaded " + blocks.size() + " block templates.");
	}
	
	/**
	 * Get the registered block instance by its name specified in Json. (Key)
	 */
	public Block getBlock(String name) {
		return getBlock(name, 0);
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
	
	private Object create(String id, BlockInfo inf) {
		Block[] ret = new Block[inf.modelCount * inf.texCount];
		int i = 0;
		for(int im = 0; im < inf.modelCount; ++im) {
		    for(int it = 0; it < inf.texCount; ++it) {
		        ret[i++] = createInstance(id, inf, im, it, "" + (i - 1));
		    }
		}
		regged.put(id, ret);
		return ret;
	}
	
	private BlockTemplate createInstance(String id, BlockInfo inf, int mid, int tid, String pfix) {
	    BlockTemplate ret = null;
	    try {
            Constructor<? extends BlockTemplate> ctor = inf.getBlockClass().getConstructor(Material.class);
            ret = ctor.newInstance(inf.getMaterial());
            setupBlock(ret, inf, mid, tid, pfix);
            GameRegistry.registerBlock(ret, ItemBlockMulti.class, id + pfix);
        } catch(Exception e) {
        	System.err.println("An error occured when creating block with id " + id);
            e.printStackTrace();
        }
	    return ret;
	}
	
	private void setupBlock(BlockTemplate block, BlockInfo inf, int mid, int tid, String pfix) {
	    block.parent = this;
	    block.subID = mid * inf.texCount + tid;
	    block.tileType = inf.getTileClass();
	    block.addSubBlock(inf.structure);
	    block.setStepSound(inf.getStepSound());
	    block.setLightLevel(inf.lightLevel);
	    block.setHardness(inf.hardness);
	    block.setBlockName(inf.name + pfix);
	    block.setBlockTextureName("corridor:" + inf.name + pfix);
	    if(GenericUtils.getSide() == Side.CLIENT)
	        setupClient(block, inf, mid, tid);
	    block.finishInit();
	}
	
	@SideOnly(Side.CLIENT)
	private void setupClient(BlockTemplate block, BlockInfo inf, int mid, int tid) {
	     block.renderType = getRender(inf);
	     if(inf.modelCount == 1) {
	         block.model = model(inf.modelName);
	     } else {
	         block.model = model(inf.modelName + mid);
	     }
	     if(inf.texCount == 1) {
	         block.texture = texture(inf.textureName);
	     } else {
	         block.texture = texture(inf.textureName + tid);
	     }
	     block.center = inf.center;
	     block.scale = inf.scale;
	}
	
	private ResourceLocation texture(String name) {
	    return new ResourceLocation("corridor:textures/models/" + name + ".png");
	}
	
	private ITileEntityModel model(String name) {
	    return new TileEntityModelCustom(AdvancedModelLoader.loadModel(new ResourceLocation("corridor:models/" + name + ".obj")));
	}
	
	private RenderTemplate getRender(BlockInfo inf) {
	    Class<? extends RenderTemplate> clazz = inf.getRenderClass();
	    RenderTemplate ret = createdRenders.get(clazz);
	    if(ret != null)
	        return ret;
	    try {
	        ret = clazz.newInstance();
	        createdRenders.put(clazz, ret);
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ret;
	}
	
	private void init() {
		try {
			for(Entry<String, JsonElement> ent : root.entrySet()) {
				if(ent.getKey().equals(DEFAULT_PROP_NAME)) {
					base = BlockInfo.create(ent.getValue());
					continue;
				}
				BlockInfo inf = BlockInfo.create(ent.getValue());
				blocks.put(ent.getKey(), inf);
			}
		} catch(Exception e) {
			System.err.println("Unexpected error occured while parsing block property file.");
			e.printStackTrace();
		}
		
		checkInherit();
	}
	
	private static List<Field> inheritFields = new ArrayList();
	static {
		for(Field f : BlockInfo.class.getFields()) {
			if(f.getAnnotation(Inherit.class) != null) {
				inheritFields.add(f);
			}
		}
	}
	
	private void checkInherit() {
		for(BlockInfo inf : blocks.values()) {
			for(Field f : inheritFields) {
				try {
					if(f.get(inf) == null) f.set(inf, f.get(base));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			inf.printDebug();
		}
	}

}
