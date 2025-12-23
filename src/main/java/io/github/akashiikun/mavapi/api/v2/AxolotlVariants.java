//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.akashiikun.mavapi.api.v2;

import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.biome.Biome;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("NullableProblems")
public class AxolotlVariants {
    public static final ResourceKey<AxolotlVariant> LUCY = createKey(Identifier.withDefaultNamespace("lucy"));
    public static final ResourceKey<AxolotlVariant> WILD = createKey(Identifier.withDefaultNamespace("wild"));
    public static final ResourceKey<AxolotlVariant> GOLD = createKey(Identifier.withDefaultNamespace("gold"));
	public static final ResourceKey<AxolotlVariant> CYAN = createKey(Identifier.withDefaultNamespace("cyan"));
	public static final ResourceKey<AxolotlVariant> BLUE = createKey(Identifier.withDefaultNamespace("blue"));
	public static final ResourceKey<AxolotlVariant> DEFAULT = LUCY;

    private static ResourceKey<AxolotlVariant> createKey(Identifier identifier) {
        return ResourceKey.create(MavApiRegistries.AXOLOTL_VARIANT, identifier);
    }

	public static Holder<AxolotlVariant> getCommonSpawnVariant(RegistryAccess registryAccess, RandomSource random) {
		return getSpawnVariant(registryAccess, random, true);
	}

	public static Holder<AxolotlVariant> getRareSpawnVariant(RegistryAccess registryAccess, RandomSource random) {
		return getSpawnVariant(registryAccess, random, false);
	}

	private static Holder<AxolotlVariant> getSpawnVariant(RegistryAccess registryAccess, RandomSource random, boolean common) {
		//noinspection unchecked
		Holder<AxolotlVariant>[] array = registryAccess.lookupOrThrow(MavApiRegistries.AXOLOTL_VARIANT).entrySet().stream().filter(variant -> common != variant.getValue().rare()).map(a -> registryAccess.getOrThrow(a.getKey())).toArray(Holder.Reference[]::new);
		if (false) for (Map.Entry<ResourceKey<AxolotlVariant>, AxolotlVariant> resourceKeyAxolotlVariantEntry : registryAccess.lookupOrThrow(MavApiRegistries.AXOLOTL_VARIANT).entrySet()) {
			System.err.println(resourceKeyAxolotlVariantEntry);
		}
		return Util.getRandom(array, random);
	}

	public static ResourceKey<AxolotlVariant> fromVanilla(Axolotl.Variant variant) {
		return switch (variant) {
			case LUCY -> LUCY;
			case GOLD -> GOLD;
			case WILD -> WILD;
			case CYAN -> CYAN;
			case BLUE -> BLUE;
		};
	}
}
