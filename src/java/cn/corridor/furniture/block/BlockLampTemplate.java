package cn.corridor.furniture.block;

import cn.corridor.furniture.Furnitures;
import cn.liutils.template.block.TileMulti;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockLampTemplate extends BlockTemplate {
	
	public static class Tile extends TileMulti {
	    @Override
		@SideOnly(Side.CLIENT)
	    public AxisAlignedBB getRenderBoundingBox()
	    {
	        return INFINITE_EXTENT_AABB;
	    }
	}
	
	public BlockLampTemplate(Material material) {
		this();
	}
	
	public BlockLampTemplate() {
		super(Material.wood);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new Tile();
	}
    
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int w, float a, 
			float b, float c) {
    	Block block = Furnitures.instance.getBlock("lampa", isBloody() ? 0 : 1);
    	world.setBlock(x, y, z, block);
        return true;
    }
    
    protected boolean isBloody() {
    	return subID == 1;
    }
	
}
