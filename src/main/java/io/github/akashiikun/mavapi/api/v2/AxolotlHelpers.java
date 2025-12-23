package io.github.akashiikun.mavapi.api.v2;

import io.github.akashiikun.mavapi.impl.extension.AxolotlExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class AxolotlHelpers {
	public static void setVariant(Axolotl axolotl, Holder<AxolotlVariant> variantHolder) {
		((AxolotlExtension) axolotl).setVariant(variantHolder);
	}

	public static Holder<AxolotlVariant> getVariant(Axolotl axolotl) {
		return ((AxolotlExtension) axolotl).getVariant();
	}
}
