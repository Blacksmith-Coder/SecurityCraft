package net.geforcemods.securitycraft.tileentity;

import net.geforcemods.securitycraft.api.CustomizableSCTE;
import net.geforcemods.securitycraft.misc.EnumCustomModules;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class TileEntitySecurityCamera extends CustomizableSCTE {
	
	private final float CAMERA_SPEED = 0.0180F;
	
	public float cameraRotation = 0.0F;
	private boolean addToRotation = true;

	public void update(){
		super.update();
		
		if(addToRotation && cameraRotation <= 1.55F){
			cameraRotation += CAMERA_SPEED;
		}else{
			addToRotation = false;
		}
		
		if(!addToRotation && cameraRotation >= -1.55F){
			cameraRotation -= CAMERA_SPEED;
		}else{
			addToRotation = true;
		}
	}
   
	public EnumCustomModules[] getCustomizableOptions(){
		return new EnumCustomModules[] { EnumCustomModules.REDSTONE };
	}

	public String[] getOptionDescriptions() {
		return new String[]{EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("item.redstoneModule.name") + ":" + EnumChatFormatting.RESET + "\n\n" + StatCollector.translateToLocal("module.description.camera.redstone")};
	}
}