package net.geforcemods.securitycraft.imc.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;

@JEIPlugin
public class SCJEIPlugin implements IModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		registry.addAdvancedGuiHandlers(new SlotMover());
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime){}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}
}