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
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import cn.corridor.furniture.block.BlockTemplate;

/**
 * This is just a gate that redirects the renderer to the one specified in BlockTemplate.
 * @author WeathFolD
 */
public class RenderTemplateGate extends TileEntitySpecialRenderer {

	public RenderTemplateGate() {}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		Block bt = te.getBlockType();
		if(!(bt instanceof BlockTemplate))
			return;
		BlockTemplate template = (BlockTemplate) bt;
		template.getRender().renderTileEntityAt(te, x, y, z, f);
	}

}
