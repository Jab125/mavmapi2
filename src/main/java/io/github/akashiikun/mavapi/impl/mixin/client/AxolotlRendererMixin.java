package io.github.akashiikun.mavapi.impl.mixin.client;

import io.github.akashiikun.mavapi.api.v2.AxolotlHelpers;
import io.github.akashiikun.mavapi.impl.extension.AxolotlExtension;
import io.github.akashiikun.mavapi.impl.extension.client.AxolotlRenderStateExtension;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AxolotlRenderer.class)
public class AxolotlRendererMixin {
	/**
	 * @author Jab125
	 * @reason There's no keeping compat here, this keeps our intentions clear.
	 */
	@Overwrite
	public Identifier getTextureLocation(AxolotlRenderState axolotlRenderState) {
		return ((AxolotlRenderStateExtension) axolotlRenderState).getVariant() == null ? MissingTextureAtlasSprite.getLocation() : ((AxolotlRenderStateExtension) axolotlRenderState).getVariant().assetInfo().texturePath();
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/axolotl/Axolotl;Lnet/minecraft/client/renderer/entity/state/AxolotlRenderState;F)V", at = @At("RETURN"))
	void extract(Axolotl axolotl, AxolotlRenderState axolotlRenderState, float f, CallbackInfo ci) {
		((AxolotlRenderStateExtension) axolotlRenderState).setVariant(AxolotlHelpers.getVariant(axolotl).value());
	}
}
