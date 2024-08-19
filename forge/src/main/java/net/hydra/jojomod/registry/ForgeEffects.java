package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeEffects{

    public static final DeferredRegister<MobEffect> POTION_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Roundabout.MOD_ID);
    public static final RegistryObject<MobEffect> BLEED =
            POTION_EFFECTS.register("bleed", () ->
                    new Effect(MobEffectCategory.HARMFUL, 11994666)
            );
    public static final RegistryObject<MobEffect> HEX =
            POTION_EFFECTS.register("hex", () ->
                    new Effect(MobEffectCategory.HARMFUL, 16762706)
            );

    public static class Effect extends MobEffect{
        public Effect(MobEffectCategory typeIn, int liquidColorIn) {

            super(typeIn, liquidColorIn);
        }

    }

}
