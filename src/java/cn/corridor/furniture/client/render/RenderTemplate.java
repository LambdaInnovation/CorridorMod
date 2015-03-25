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
package cn.corridor.furniture.client.render;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import cn.corridor.furniture.block.BlockTemplate;
import cn.liutils.api.render.model.ITileEntityModel;
import cn.liutils.template.block.RenderBlockMultiModel;

/**
 * @author WeathFolD
 */
public class RenderTemplate extends RenderBlockMultiModel {

	public RenderTemplate() {}
	
	@Override
	public void drawAtOrigin(TileEntity te) {
		Block blockType = te.getBlockType();
		if(!(blockType instanceof BlockTemplate))
			return;
		//Setup the render information by blockType.
		BlockTemplate template = (BlockTemplate) blockType;
		this.scale = template.getInfo().scale;
		this.mdl = (ITileEntityModel) template.model;
		this.tex = template.texture;
		
		super.drawAtOrigin(te);
	}

}
