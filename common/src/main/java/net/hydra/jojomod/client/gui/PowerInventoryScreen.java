package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;


public class PowerInventoryScreen
        extends EffectRenderingInventoryScreen<InventoryMenu> {
    /**Currently unfinished, this is when you press the stand power inventory key.
     * It should render your current stand, as well as its moves and stuff.*/
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
    private float mouseX;
    private float mouseY;
    private boolean narrow;
    private boolean mouseDown;

    private StandEntity stand = null;

    public PowerInventoryScreen(Player player) {
        super(player.inventoryMenu, player.getInventory(), Component.translatable("container.crafting"));
        this.titleLabelX = 97;
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        Player pl = Minecraft.getInstance().player;
        if (pl != null) {
            stand = ((StandUser)pl).roundabout$getStand();
            if (stand != null) {
                int i = this.leftPos;
                int j = this.topPos;
                context.blit(INVENTORY_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
                InventoryScreen.renderEntityInInventoryFollowsMouse(context, i + 51, j + 75, 30,
                        (float) (i + 51) - this.mouseX, (float) (j + 75 - 50) - this.mouseY, (LivingEntity) pl);
            }
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}
