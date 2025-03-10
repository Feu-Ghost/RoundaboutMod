package net.hydra.jojomod.registry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IBlockEntityWithoutLevelRenderer;
import net.hydra.jojomod.access.IItemRenderer;
import net.hydra.jojomod.entity.FogCloneRenderer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class FabricEntityClient {

    public static <T extends Entity> EntityType<T> register(BuiltInRegistries entityType, String name, EntityType<T> builder) {
        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(Roundabout.MOD_ID, "terrier"),
                builder);
    }

    public static void register() {
        /*Renderers*/
        EntityRendererRegistry.register(FabricEntities.TERRIER_DOG, TerrierEntityRenderer::new);
        EntityRendererRegistry.register(FabricEntities.STAR_PLATINUM, StarPlatinumRenderer::new);
        EntityRendererRegistry.register(FabricEntities.STAR_PLATINUM_BASEBALL, StarPlatinumBaseballRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THE_WORLD, TheWorldRenderer::new);
        EntityRendererRegistry.register(FabricEntities.JUSTICE, JusticeRenderer::new);
        EntityRendererRegistry.register(FabricEntities.JUSTICE_PIRATE, JusticePirateRenderer::new);
        EntityRendererRegistry.register(FabricEntities.MAGICIANS_RED, MagiciansRedRenderer::new);
        EntityRendererRegistry.register(FabricEntities.DARK_MIRAGE, DarkMirageRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_HARPOON, HarpoonRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_KNIFE, KnifeRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_MATCH, ThrownItemRenderer::new);
        EntityRendererRegistry.register(FabricEntities.GASOLINE_SPLATTER, ThrownItemRenderer::new);
        EntityRendererRegistry.register(FabricEntities.GASOLINE_CAN, GasolineCanRenderer::new);
        EntityRendererRegistry.register(FabricEntities.STAND_ARROW, StandArrowRenderer::new);
        EntityRendererRegistry.register(FabricEntities.THROWN_OBJECT, ThrownObjectRenderer::new);
        EntityRendererRegistry.register(FabricEntities.OVA_ENYA, OVAEnyaRenderer::new);
        EntityRendererRegistry.register(FabricEntities.JOTARO, JotaroRenderer::new);
        EntityRendererRegistry.register(FabricEntities.STEVE_NPC, PlayerNPCRenderer::new);
        EntityRendererRegistry.register(FabricEntities.ALEX_NPC, PlayerAlexRenderer::new);
        EntityRendererRegistry.register(FabricEntities.FOG_CLONE, FogCloneRenderer::new);
        EntityRendererRegistry.register(FabricEntities.FALLEN_ZOMBIE, FallenZombieRenderer::new);
        EntityRendererRegistry.register(FabricEntities.FALLEN_SKELETON, FallenSkeletonRenderer::new);
        EntityRendererRegistry.register(FabricEntities.FALLEN_SPIDER, FallenSpiderRenderer::new);
        EntityRendererRegistry.register(FabricEntities.FALLEN_VILLAGER, FallenVillagerRenderer::new);
        EntityRendererRegistry.register(FabricEntities.FALLEN_CREEPER, FallenCreeperRenderer::new);
        /*Models*/
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.STAR_PLATINUM_LAYER, StarPlatinumModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.STAR_PLATINUM_BASEBALL_LAYER, StarPlatinumBaseballModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.JUSTICE_LAYER, JusticeModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.MAGICIANS_RED_LAYER, MagiciansRedModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.JUSTICE_PIRATE_LAYER, JusticePirateModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.DARK_MIRAGE_LAYER, DarkMirageModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.HARPOON_LAYER, HarpoonModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.GASOLINE_LAYER, GasolineCanModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.OVA_ENYA_LAYER, OVAEnyaModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.JOTARO_LAYER, JotaroModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.STEVE_LAYER, PlayerNPCModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityRendererClient.ALEX_LAYER, PlayerAlexModel::getTexturedModelData);
    }
}