package io.github.akashiikun.mavapi.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import io.github.akashiikun.mavapi.api.v2.AxolotlVariant;
import io.github.akashiikun.mavapi.api.v2.AxolotlVariants;
import io.github.akashiikun.mavapi.api.v2.MavApiDataComponents;
import io.github.akashiikun.mavapi.api.v2.MavApiRegistries;
import io.github.akashiikun.mavapi.impl.extension.AxolotlExtension;
import io.github.akashiikun.mavapi.impl.extension.AxolotlGroupDataExtension;
import io.github.akashiikun.mavapi.impl.init.ModEntityDataSerializers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("NullableProblems")
@Mixin(Axolotl.class)
public abstract class AxolotlMixin extends LivingEntity implements AxolotlExtension {
	@Shadow
	private static native boolean useRareVariant(RandomSource random);

	@Unique
	private static @Final @Mutable EntityDataAccessor<Holder<AxolotlVariant>> DATA_VARIANT_ID;

	protected AxolotlMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@SuppressWarnings("WrongEntityDataParameterClass")
	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;defineId(Ljava/lang/Class;Lnet/minecraft/network/syncher/EntityDataSerializer;)Lnet/minecraft/network/syncher/EntityDataAccessor;"))
	private static <T> EntityDataAccessor<T> mavm$clinit(Class<? extends SyncedDataHolder> clazz, EntityDataSerializer<T> serializer, Operation<EntityDataAccessor<T>> original) {
		if (serializer == EntityDataSerializers.INT) {
			DATA_VARIANT_ID = SynchedEntityData.defineId(Axolotl.class, ModEntityDataSerializers.AXOLOTL_VARIANT);
			return null;
		}
		return original.call(clazz, serializer);
	}

	@Redirect(method = "defineSynchedData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData$Builder;define(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)Lnet/minecraft/network/syncher/SynchedEntityData$Builder;", ordinal = 0))
	<T> SynchedEntityData.Builder mavapi$defineSynchedData(SynchedEntityData.Builder builder, EntityDataAccessor<T> accessor, T value) {
		return builder.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(this.registryAccess(), AxolotlVariants.LUCY));
	}

	@Redirect(method = "addAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ValueOutput;store(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V")) // watch this just incase mojang adds more
	<T> void mavapi$addAdditionalSaveData(ValueOutput output, String string, Codec<T> tCodec, T t) {
		VariantUtils.writeVariant(output, this.getVariant());
	}

	@Inject(method = "getVariant", at = @At("HEAD"), cancellable = true)
	void mavapi$getVariant(CallbackInfoReturnable<Axolotl.Variant> cir) {
		cir.setReturnValue(Axolotl.Variant.DEFAULT);
	}

	@Inject(method = "setVariant", at = @At("HEAD"), cancellable = true)
	void mavapi$setVariant(Axolotl.Variant variant, CallbackInfo ci) {
		ci.cancel();
	}

	@Redirect(method = "finalizeSpawn", at = @At(value = "NEW", target = "net/minecraft/world/entity/animal/axolotl/Axolotl$AxolotlGroupData"))
	Axolotl.AxolotlGroupData mavapi$finalizeSpawn(Axolotl.Variant[] types) {
		Axolotl.AxolotlGroupData axolotlGroupData = new Axolotl.AxolotlGroupData(null);
		//noinspection unchecked
		((AxolotlGroupDataExtension) axolotlGroupData).setVariants(new Holder[]{AxolotlVariants.getCommonSpawnVariant(registryAccess(), random), AxolotlVariants.getCommonSpawnVariant(registryAccess(), random)});
		return axolotlGroupData;
	}

	@Redirect(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl$AxolotlGroupData;getVariant(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;"))
	Axolotl.Variant mavapi$finalizeSpawn(Axolotl.AxolotlGroupData instance, RandomSource random) {
		return null;
	}

	@Redirect(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;setVariant(Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;)V"))
	void mavapi$finalizeSpawn(Axolotl instance, Axolotl.Variant variant, @Local(argsOnly = true) SpawnGroupData data) {
		((AxolotlExtension) instance).setVariant(((AxolotlGroupDataExtension) data).getVariant(random));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	void mavapi$readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
		VariantUtils.readVariant(input, MavApiRegistries.AXOLOTL_VARIANT).ifPresent(this::setVariant);
	}

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	<T> void mavapi$get(DataComponentType<? extends T> component, CallbackInfoReturnable<T> cir) {
		if (component == MavApiDataComponents.AXOLOTL_VARIANT) {
			cir.setReturnValue(castComponentValue(component, this.getVariant()));
		}
	}

	@Inject(method = "applyImplicitComponents", at = @At("HEAD"))
	void mavapi$applyImplicitComponents(DataComponentGetter componentGetter, CallbackInfo ci) {
		this.applyImplicitComponentIfPresent(componentGetter, MavApiDataComponents.AXOLOTL_VARIANT);
	}

	@Inject(method = "applyImplicitComponent", at = @At("HEAD"), cancellable = true)
	<T> void mavapi$applyImplicitComponent(DataComponentType<T> component, T value, CallbackInfoReturnable<Boolean> cir) {
		if (component == MavApiDataComponents.AXOLOTL_VARIANT) {
			this.setVariant(castComponentValue(MavApiDataComponents.AXOLOTL_VARIANT, value));
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "getBreedOffspring", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;setPersistenceRequired()V"), cancellable = true)
	void mavapi$applyImplicitComponent(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<AgeableMob> cir, @Local Axolotl axolotl) {
		Holder<AxolotlVariant> variant;
		// TODO, this needs to be data driven!
		Optional<Holder.Reference<AxolotlVariant>> blueAxolotlVariant;
		if (useRareVariant(this.random) && (blueAxolotlVariant = registryAccess().get(AxolotlVariants.BLUE)).isPresent()) {
			variant = blueAxolotlVariant.get();
		} else {
			variant = this.random.nextBoolean() ? this.getVariant() : ((AxolotlExtension) otherParent).getVariant();
		}
		((AxolotlExtension) axolotl).setVariant(variant);
	}

	@Redirect(method = "saveToBucketTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyFrom(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/core/component/DataComponentGetter;)V"))
	<T> void mavapi$saveToBucketTag(ItemStack instance, DataComponentType<T> componentType, DataComponentGetter componentGetter) {
		instance.copyFrom(MavApiDataComponents.AXOLOTL_VARIANT, componentGetter);
	}

	@Override
	public void setVariant(Holder<AxolotlVariant> holder) {
		this.entityData.set(DATA_VARIANT_ID, holder);
	}

	@Override
	public Holder<AxolotlVariant> getVariant() {
		return this.entityData.get(DATA_VARIANT_ID);
	}

	@Mixin(Axolotl.AxolotlGroupData.class)
	public static class AxolotlGroupDataMixin implements AxolotlGroupDataExtension {
		@Unique
		public @Mutable @Final Holder<AxolotlVariant>[] variants; // TODO holder?
		@Inject(method = "<init>", at = @At("CTOR_HEAD"))
		void init(Axolotl.Variant[] types, CallbackInfo ci) {
			if (types != null) throw new AssertionError("Use the API!");
		}

		@Override
		public void setVariants(Holder<AxolotlVariant>[] variants) {
			this.variants = variants;
		}

		@Override
		public Holder<AxolotlVariant> getVariant(RandomSource random) {
			return this.variants[random.nextInt(this.variants.length)];
		}
	}

	// Needed since they have clashing names
	@Mixin(Axolotl.AxolotlGroupData.class)
	public static class AxolotlGroupData2Mixin {
		/**
		 * @author Jab125
		 * @reason Make our intentions clear
		 */
		@Overwrite
		public Axolotl.Variant getVariant(RandomSource random) {
			throw new AssertionError("Use the API!");
		}
	}
}
