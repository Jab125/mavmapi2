package io.github.akashiikun.mavapi.impl;

import io.github.akashiikun.mavapi.api.v2.AxolotlVariant;
import io.github.akashiikun.mavapi.api.v2.MavApiDataComponents;
import io.github.akashiikun.mavapi.api.v2.MavApiRegistries;
import io.github.akashiikun.mavapi.impl.init.ModEntityDataSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

import java.lang.invoke.MethodHandles;

public class MavApiInit implements ModInitializer {
	@Override
	public void onInitialize() {
		System.err.println("init");
		//EntityDataSerializers
		try {
			MethodHandles.publicLookup().ensureInitialized(ModEntityDataSerializers.class);
			MethodHandles.publicLookup().ensureInitialized(MavApiDataComponents.class);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		DynamicRegistries.registerSynced(MavApiRegistries.AXOLOTL_VARIANT, AxolotlVariant.DIRECT_CODEC, AxolotlVariant.NETWORK_CODEC);
	}
}
