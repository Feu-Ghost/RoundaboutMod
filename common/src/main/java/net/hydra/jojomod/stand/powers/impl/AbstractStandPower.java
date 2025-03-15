package net.hydra.jojomod.stand.powers.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.stand.powers.CooldownInstance;
import net.hydra.jojomod.stand.powers.api.StandPower;
import net.hydra.jojomod.stand.powers.api.StandUser;
import net.hydra.jojomod.stand.powers.api.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStandPower implements StandPower {
    /** Reference this Cooldown Instance to change cooldowns for this specific power. */
    private final CooldownInstance roundabout$cooldown = new CooldownInstance();

    /** Where the power will be on the HUD and what keybind will be mapped to it.
     * Ranges from 1-4 (inclusive). */
    private final int roundabout$slotIndex;

    private final ResourceLocation roundabout$icon;
    private final LivingEntity roundabout$standUser;
    private boolean roundabout$isLocked;

    /** Reference this Cooldown Instance to change cooldowns for this specific power. */
    public CooldownInstance getCooldown()
    { return this.roundabout$cooldown; }

    public int getSlotIndex()
    { return this.roundabout$slotIndex; }

    @Override
    public boolean roundabout$getLocked() { return this.roundabout$isLocked; }
    @Override
    public void roundabout$setLocked(boolean value) { this.roundabout$isLocked = value; }

    @Override
    public StandUser roundabout$getUser() { return (StandUser) this.roundabout$standUser; }
    @Override
    public LivingEntity roundabout$getUserEntity() { return this.roundabout$standUser; }

    public AbstractStandPower(int slotIndex, @NotNull ResourceLocation icon, @NotNull LivingEntity standUser)
    {
        assert (slotIndex >= 1 && slotIndex <= 4) : "Failed to create new AbstractStandPower with reason \"slotIndex was not in range 1-4 (inclusive)\"";

        this.roundabout$icon = icon;
        this.roundabout$slotIndex = slotIndex;
        this.roundabout$standUser = standUser;
        this.roundabout$isLocked = true;
    }

    private static Component fixKey(Component textIn)
    {
        String X = textIn.getString();
        if (X.length() > 1){
            String[] split = X.split("\\s");
            if (split.length > 1){
                return Component.nullToEmpty(""+split[0].charAt(0)+split[1].charAt(0));
            } else {
                if (split[0].length() > 1){
                    return Component.nullToEmpty(""+split[0].charAt(0)+split[0].charAt(1));
                } else {
                    return Component.nullToEmpty(""+split[0].charAt(0));
                }
            }
        } else {
            return textIn;
        }
    }

    @Override
    public boolean roundabout$isAttackInept()
    { return this.roundabout$standUser.isUsingItem() || ((StandUser)this.roundabout$standUser).roundabout$isDazed() || (((TimeStop)this.roundabout$standUser.level()).CanTimeStopEntity(this.roundabout$standUser)); }

    @Override
    public boolean roundabout$isAttackIneptVisually()
    { return this.roundabout$getUser().roundabout$isDazed() || (((TimeStop)this.roundabout$standUser.level()).CanTimeStopEntity(this.roundabout$standUser)); }

    @Override public abstract void roundabout$tick();
    @Override public abstract void roundabout$onInputBegin();
    @Override public abstract void roundabout$onInputEnd();

    @Override
    @OnlyIn(Dist.CLIENT)
    public void roundabout$draw(GuiGraphics context) {
        int x = roundabout$slotIndex*25;
        int y = 4;

        if (roundabout$isLocked){
            context.blit(StandIcons.LOCKED_SQUARE_ICON,x-3,y-3,0, 0, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        } else {
            context.blit(StandIcons.SQUARE_ICON,x-3,y-3,0, 0, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            Font renderer = Minecraft.getInstance().font;

            Component specialKey = fixKey(KeyInputRegistry.SLOT_KEYS.get(roundabout$slotIndex-1).getTranslatedKeyMessage());
            context.drawString(renderer, specialKey, x-1, y+11, 0xffffff, true);
        }


        if ((roundabout$cooldown.time >= 0) || roundabout$isAttackIneptVisually()){
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
            context.blit(this.roundabout$icon, x, y, 0, 0, 18, 18, 18, 18);
            if (roundabout$cooldown.time >= 0) {
                float blit = (20*(1-((float) (1+roundabout$cooldown.time) /(1+roundabout$cooldown.maxTime))));
                int b = Math.round(blit);
                RenderSystem.enableBlend();
                context.setColor(1f, 1f, 1f, 1f);

                ResourceLocation COOLDOWN_TEX = StandIcons.COOLDOWN_ICON;

                if (roundabout$cooldown.isFrozen())
                    COOLDOWN_TEX = StandIcons.FROZEN_COOLDOWN_ICON;

                context.blit(COOLDOWN_TEX, x - 1, y - 1 + b, 0, b, 20, 20-b, 20, 20);
                int num = ((int)(Math.floor((double) roundabout$cooldown.time /20)+1));
                int offset = x+3;
                if (num <=9){
                    offset = x+7;
                }

                if (!roundabout$cooldown.isFrozen())
                    context.drawString(Minecraft.getInstance().font, ""+num,offset,y,0xffffff,true);

                RenderSystem.disableBlend();
            }
            context.setColor(1f, 1f, 1f, 0.9f);
        } else {
            context.blit(this.roundabout$icon, x, y, 0, 0, 18, 18, 18, 18);
        }
    }
}