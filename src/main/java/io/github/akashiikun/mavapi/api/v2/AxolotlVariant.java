//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.akashiikun.mavapi.api.v2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public record AxolotlVariant(ClientAsset.ResourceTexture assetInfo, SpawnPrioritySelectors spawnConditions, boolean rare) implements PriorityProvider<SpawnContext, SpawnCondition> {
	public static final Codec<AxolotlVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(AxolotlVariant::assetInfo), SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(AxolotlVariant::spawnConditions), Codec.BOOL.optionalFieldOf("rare", false).forGetter(AxolotlVariant::rare)).apply(instance, AxolotlVariant::new));
	// this is from the server to the client?
	public static final Codec<AxolotlVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(AxolotlVariant::assetInfo)).apply(instance, AxolotlVariant::new));
	public static final Codec<Holder<AxolotlVariant>> CODEC;
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AxolotlVariant>> STREAM_CODEC;

	private AxolotlVariant(ClientAsset.ResourceTexture assetInfo) {
		this(assetInfo, SpawnPrioritySelectors.EMPTY, false);
	}

	public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
		return this.spawnConditions.selectors();
	}

	static {
		CODEC = RegistryFixedCodec.create(MavApiRegistries.AXOLOTL_VARIANT);
		STREAM_CODEC = ByteBufCodecs.holderRegistry(MavApiRegistries.AXOLOTL_VARIANT);
	}

	public static final class VariantTooltipProvider implements TooltipProvider {
		private final @Nullable Holder<AxolotlVariant> variantHolder;
		private final boolean legacy;

		public VariantTooltipProvider(Holder<AxolotlVariant> variantHolder) {
			this.variantHolder = variantHolder;
			this.legacy = false;
		}

		public VariantTooltipProvider(@Nullable HolderLookup.Provider access, Axolotl.Variant legacyVariant) {
			this.variantHolder = access == null ? null : access.get(AxolotlVariants.fromVanilla(legacyVariant)).orElse(null);
			this.legacy = true;
		}

		@Override
		public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
			Optional<ResourceKey<AxolotlVariant>> axolotlVariantResourceKey = variantHolder == null ? Optional.empty() : variantHolder.unwrapKey();
			MutableComponent component;
			@SuppressWarnings("MixinInnerClass")
			enum Age {
				ADULT("mavapi.bucket.translation.adult"),
				BABY("mavapi.bucket.translation.baby");

				private final String translationKey;

				Age(String translationKey) {
					this.translationKey = translationKey;
				}
			}
			CustomData customData = componentGetter.get(DataComponents.BUCKET_ENTITY_DATA);
			if (customData != null && axolotlVariantResourceKey.isPresent()) {
				Optional<Integer> ageInt = customData.copyTag().getInt("Age");
				Age age = ageInt.map(nbtAge -> nbtAge < 0 ? Age.BABY : Age.ADULT).orElse(Age.ADULT);
				Identifier id = axolotlVariantResourceKey.get().identifier();
				component = Component.translatable("mavapi.bucket.format",
						Component.translatable(age.translationKey),
						translateOrFormat(String.format("mavapi.variant.%s.%s", id.getNamespace(), id.getPath()), id.getPath()),
						translateOrFormat(String.format("mavapi.mod.%s", id.getNamespace()), id.getNamespace())
				);
			} else {
				component = Component.literal("missingno");
			}

			component.setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));
			if (legacy) component.withStyle(style -> style.withItalic(true));
			tooltipAdder.accept(component);
		}

		private MutableComponent translateOrFormat(String translation, String toFormat) {
			MutableComponent component = Component.translatable(translation);
			if (!I18n.exists(translation)) {
				component = Component.literal(formatName(toFormat));
			}
			return component;
		}

		private String formatName(String s) {
			s = s.replace("_", " ");
			s = String.valueOf(s.charAt(0)).toUpperCase(Locale.ROOT) + s.substring(1);
			return s;
		}
	}

}
