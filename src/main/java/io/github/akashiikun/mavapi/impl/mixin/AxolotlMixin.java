package io.github.akashiikun.mavapi.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import io.github.akashiikun.mavapi.api.v2.AxolotlVariant;
import io.github.akashiikun.mavapi.api.v2.AxolotlVariants;
import io.github.akashiikun.mavapi.api.v2.MavApiDataComponents;
import io.github.akashiikun.mavapi.api.v2.MavApiRegistries;
import io.github.akashiikun.mavapi.impl.extension.client.AxolotlExtension;
import io.github.akashiikun.mavapi.impl.init.ModEntityDataSerializers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("NullableProblems")
@Mixin(Axolotl.class)
public abstract class AxolotlMixin extends LivingEntity implements AxolotlExtension {
	@SuppressWarnings("WrongEntityDataParameterClass")
	@Unique
	private static final EntityDataAccessor<Holder<AxolotlVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Axolotl.class, ModEntityDataSerializers.AXOLOTL_VARIANT);

	protected AxolotlMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;defineId(Ljava/lang/Class;Lnet/minecraft/network/syncher/EntityDataSerializer;)Lnet/minecraft/network/syncher/EntityDataAccessor;"))
	private static <T> EntityDataAccessor<T> mavm$clinit(Class<? extends SyncedDataHolder> clazz, EntityDataSerializer<T> serializer, Operation<EntityDataAccessor<T>> original) {
		if (serializer == EntityDataSerializers.INT) return null;
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

	@Inject(method = "finalizeSpawn", at = @At("HEAD"))
	void mavapi$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
		VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), MavApiRegistries.AXOLOTL_VARIANT).ifPresent(this::setVariant);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	void mavapi$readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
		VariantUtils.readVariant(input, MavApiRegistries.AXOLOTL_VARIANT).ifPresent(this::setVariant);
		// L1
		//    LINENUMBER 214 L1
		//    ALOAD 1
		//    LDC "Variant"
		//    GETSTATIC net/minecraft/world/entity/animal/axolotl/Axolotl$Variant.LEGACY_CODEC : Lcom/mojang/serialization/Codec;
		//    ALOAD 0
		//    INVOKEVIRTUAL net/minecraft/world/entity/animal/axolotl/Axolotl.getVariant ()Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;
		//    INVOKEINTERFACE net/minecraft/world/level/storage/ValueOutput.store (Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V (itf)
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
}
