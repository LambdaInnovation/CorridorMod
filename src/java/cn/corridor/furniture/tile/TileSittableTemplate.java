package cn.corridor.furniture.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegTileEntity;
import cn.corridor.furniture.client.render.RenderTemplateGate;
import cn.liutils.template.entity.EntitySittable;

/**
 * TileEntity for sittable block (Copy from OTF)
 * @author WeAthFolD
 */
@RegistrationClass
@RegTileEntity
@RegTileEntity.HasRender
public class TileSittableTemplate extends TileTemplate {
	
	@RegTileEntity.Render
	@SideOnly(Side.CLIENT)
	public static RenderTemplateGate renderer;
	
	/**
	 * Y Offset (Default is the center of the block)
	 */
	protected float offsetY = 0.0F;
	
	/**
	 * Invisible Entity which managing inside
	 */
	private EntitySittable ent = null;
	
	@Override
	public void updateEntity() {
		if(!worldObj.isRemote && ent == null) {
			ent = new EntitySittable(worldObj, this.xCoord + .5F, this.yCoord + .5F + offsetY, this.zCoord + .5F,
					xCoord, yCoord, zCoord);
			worldObj.spawnEntityInWorld(ent);
		}
		
		super.updateEntity();
	}
	
	public void onTileActivated(EntityPlayer player) {
		if(ent == null) return;
		if(player.equals(ent.mountedPlayer)) {
			ent.disMount();
		} else if(!ent.isMounted()) {
			ent.mount(player);
		}
	}
	
	@Override
    public void invalidate() {
		//Player leave
		if(ent != null) {
			if(ent.mountedPlayer != null)
				ent.mountedPlayer.mountEntity((Entity) null);
			ent.setDead();
		}
		super.invalidate();
    }
	
}
