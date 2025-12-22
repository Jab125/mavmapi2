//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.akashiikun.mavapi.api.v2;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.entity.animal.cow.CowVariant.ModelType;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.biome.Biome;

public class AxolotlVariants {
    public static final ResourceKey<AxolotlVariant> LUCY = createKey(Identifier.withDefaultNamespace("lucy"));
    public static final ResourceKey<AxolotlVariant> WILD = createKey(Identifier.withDefaultNamespace("wild"));
    public static final ResourceKey<AxolotlVariant> GOLD = createKey(Identifier.withDefaultNamespace("gold"));
	public static final ResourceKey<AxolotlVariant> CYAN = createKey(Identifier.withDefaultNamespace("cyan"));
	public static final ResourceKey<AxolotlVariant> BLUE = createKey(Identifier.withDefaultNamespace("blue"));
	public static final ResourceKey<AxolotlVariant> DEFAULT = LUCY;

    private static ResourceKey<AxolotlVariant> createKey(Identifier identifier) {
        return ResourceKey.create(dumptodoremakelater.COW_VARIANT, identifier);
    }

	// this is for data generation, we can skip
//    public static void bootstrap(BootstrapContext<AxolotlVariant> bootstrapContext) {
//        register(bootstrapContext, TEMPERATE, ModelType.NORMAL, "temperate_cow", SpawnPrioritySelectors.fallback(0));
//        register(bootstrapContext, WARM, ModelType.WARM, "warm_cow", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
//        register(bootstrapContext, COLD, ModelType.COLD, "cold_cow", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
//    }

    private static void register(BootstrapContext<AxolotlVariant> bootstrapContext, ResourceKey<AxolotlVariant> resourceKey, AxolotlVariant.ModelType modelType, String string, TagKey<Biome> tagKey) {
        HolderSet<Biome> holderSet = bootstrapContext.lookup(Registries.BIOME).getOrThrow(tagKey);
        register(bootstrapContext, resourceKey, modelType, string, SpawnPrioritySelectors.single(new BiomeCheck(holderSet), 1));
    }

    private static void register(BootstrapContext<AxolotlVariant> bootstrapContext, ResourceKey<AxolotlVariant> resourceKey, AxolotlVariant.ModelType modelType, String string, SpawnPrioritySelectors spawnPrioritySelectors) {
        Identifier identifier = Identifier.withDefaultNamespace("entity/cow/" + string);
        bootstrapContext.register(resourceKey, new AxolotlVariant(new ModelAndTexture(modelType, identifier), spawnPrioritySelectors));
    }
}
