package cn.corridor.furniture.block;

import cn.corridor.furniture.Furnitures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockLampActivedTemplate extends BlockLampTemplate {
	
	public BlockLampActivedTemplate(Material material) {
		this();
	}
	
	public BlockLampActivedTemplate() {
		setCreativeTab(null);
	}
    
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int w, float a, 
			float b, float c) {
    	Block block = Furnitures.instance.getBlock("lamp", isBloody() ? 0 : 1);
    	world.setBlock(x, y, z, block);
        return true;
    }
	
}
