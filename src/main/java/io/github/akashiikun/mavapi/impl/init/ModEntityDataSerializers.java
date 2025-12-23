package io.github.akashiikun.mavapi.impl.init;

import io.github.akashiikun.mavapi.api.v2.AxolotlVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

@SuppressWarnings("NullableProblems")
public class ModEntityDataSerializers {
	public static final EntityDataSerializer<Holder<AxolotlVariant>> AXOLOTL_VARIANT = EntityDataSerializer.forValueType(AxolotlVariant.STREAM_CODEC);

	static {
		FabricTrackedDataRegistry.register(Identifier.parse("mavapi:axolotl_variant"), AXOLOTL_VARIANT);
	}
}
