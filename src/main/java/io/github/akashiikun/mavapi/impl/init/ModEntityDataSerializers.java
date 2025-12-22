package io.github.akashiikun.mavapi.impl.init;

import io.github.akashiikun.mavapi.api.v2.AxolotlVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.cow.CowVariant;

public class ModEntityDataSerializers {
	public static final EntityDataSerializer<Holder<AxolotlVariant>> COW_VARIANT = EntityDataSerializer.forValueType(AxolotlVariant.STREAM_CODEC);

	static {
		FabricTrackedDataRegistry.register(Identifier.parse("mavapi:axolotl_variant"), COW_VARIANT);
	}
}
