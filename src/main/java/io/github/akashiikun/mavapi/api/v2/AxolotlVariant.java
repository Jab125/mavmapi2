//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.akashiikun.mavapi.api.v2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public record AxolotlVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
	public static final Codec<AxolotlVariant> DIRECT_CODEC = RecordCodecBuilder.create((p_481302_) -> p_481302_.group(ModelAndTexture.codec(AxolotlVariant.ModelType.CODEC, AxolotlVariant.ModelType.NORMAL).forGetter(AxolotlVariant::modelAndTexture), SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(AxolotlVariant::spawnConditions)).apply(p_481302_, AxolotlVariant::new));
	public static final Codec<AxolotlVariant> NETWORK_CODEC = RecordCodecBuilder.create((p_479441_) -> p_479441_.group(ModelAndTexture.codec(AxolotlVariant.ModelType.CODEC, AxolotlVariant.ModelType.NORMAL).forGetter(AxolotlVariant::modelAndTexture)).apply(p_479441_, AxolotlVariant::new));
	public static final Codec<Holder<AxolotlVariant>> CODEC;
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AxolotlVariant>> STREAM_CODEC;

	private AxolotlVariant(ModelAndTexture<ModelType> modelAndTexture) {
		this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
	}

	public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
		return this.spawnConditions.selectors();
	}

	static {
		CODEC = RegistryFixedCodec.create(dumptodoremakelater.COW_VARIANT);
		STREAM_CODEC = ByteBufCodecs.holderRegistry(dumptodoremakelater.COW_VARIANT);
	}

	// todo: really unsure about this one
	public static enum ModelType implements StringRepresentable {
		NORMAL("normal");
		//COLD("cold"),
		//WARM("warm");

		public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
		private final String name;

		private ModelType(String name) {
			this.name = name;
		}

		public String getSerializedName() {
			return this.name;
		}
	}
}
