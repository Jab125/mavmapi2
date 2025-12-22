package io.github.akashiikun.mavapi.impl.extension.client;

import io.github.akashiikun.mavapi.api.v2.AxolotlVariant;
import net.minecraft.core.Holder;

public interface AxolotlExtension {
	public Holder<AxolotlVariant> getVariant();
}
