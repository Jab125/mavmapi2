package io.github.akashiikun.mavapi.api.v2;

import io.github.akashiikun.mavapi.impl.AxolotlMigrationsImpl;
import net.minecraft.resources.Identifier;

/// Migrate an axolotl variant from MavApi v1. Does not work with an existing v2 variant!
public class V1AxolotlMigrations {
	public static void migrateToV2(Identifier oldName, Identifier newName) {
		AxolotlMigrationsImpl.namesToMigrate.put(oldName, newName);
	}
}
