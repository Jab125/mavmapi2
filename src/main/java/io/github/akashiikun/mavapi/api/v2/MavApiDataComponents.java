package io.github.akashiikun.mavapi.api.v2;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.resources.Identifier;

@SuppressWarnings("NullableProblems")
public class MavApiDataComponents {
	public static final DataComponentType<Holder<AxolotlVariant>> AXOLOTL_VARIANT = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.parse("mavapi:axolotl/variant"), DataComponentType.<Holder<AxolotlVariant>>builder().persistent(AxolotlVariant.CODEC).networkSynchronized(AxolotlVariant.STREAM_CODEC).build());
}
