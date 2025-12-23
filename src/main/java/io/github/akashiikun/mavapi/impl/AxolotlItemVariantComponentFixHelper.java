package io.github.akashiikun.mavapi.impl;

import com.mojang.serialization.Dynamic;

import java.util.Optional;

public class AxolotlItemVariantComponentFixHelper {
	public static <T> Dynamic<T> fixAxolotlBucket(Dynamic<T> data, Dynamic<T> entityData) {
		Optional<Number> optional = entityData.get("Variant").asNumber().result();
		if (optional.isEmpty()) {
			// It's not a number
			Optional<String> optional1 = entityData.get("Variant").asString().result();
			if (optional1.isEmpty()) return data;
			String s = AxolotlMigrationsImpl.migrateName(optional1.get());
			return data.update("minecraft:bucket_entity_data", p_397463_ -> p_397463_.remove("Variant"))
					.set("mavapi:axolotl/variant", data.createString(s));
		} else {
			// Vanilla numerical ids
			String s = switch (optional.get().intValue()) {
				case 1 -> "wild";
				case 2 -> "gold";
				case 3 -> "cyan";
				case 4 -> "blue";
				default -> "lucy";
			};
			return data.update("minecraft:bucket_entity_data", p_397463_ -> p_397463_.remove("Variant"))
					.set("minecraft:axolotl/variant", data.createString(s));
		}
	}
}
