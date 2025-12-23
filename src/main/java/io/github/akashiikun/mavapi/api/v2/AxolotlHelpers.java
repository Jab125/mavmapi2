package io.github.akashiikun.mavapi.api.v2;

import io.github.akashiikun.mavapi.impl.extension.AxolotlExtension;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class AxolotlHelpers {
	public static void setVariant(Axolotl axolotl, Holder<AxolotlVariant> variantHolder) {
		((AxolotlExtension) axolotl).setVariant(variantHolder);
	}

	public static Holder<AxolotlVariant> getVariant(Axolotl axolotl) {
		return ((AxolotlExtension) axolotl).getVariant();
	}

	public static ResourceKey<AxolotlVariant> fromVanilla(Axolotl.Variant variant) {
		return switch (variant) {
			case LUCY -> AxolotlVariants.LUCY;
			case GOLD -> AxolotlVariants.GOLD;
			case WILD -> AxolotlVariants.WILD;
			case CYAN -> AxolotlVariants.CYAN;
			case BLUE -> AxolotlVariants.BLUE;
		};
	}
}
