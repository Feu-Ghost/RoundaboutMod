package net.hydra.jojomod.stand.powers.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.stand.powers.CooldownInstance;
import net.hydra.jojomod.stand.powers.api.StandPower;
import net.hydra.jojomod.stand.powers.api.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractStandPower implements StandPower {
    /** Reference this Cooldown Instance to change cooldowns for this specific power. */
    private final CooldownInstance cooldown = new CooldownInstance();

    /** Where the power will be on the HUD and what keybind will be mapped to it.
     * Ranges from 1-4 (inclusive). */
    private int slotIndex = -1;

    /** Reference this Cooldown Instance to change cooldowns for this specific power. */
    public CooldownInstance getCooldown()
    { return this.cooldown; }

    public int getSlotIndex()
    { return this.slotIndex; }

    public AbstractStandPower(int slotIndex)
    {
        assert (slotIndex >= 1 && slotIndex <= 4) : "Failed to create new AbstractStandPower with reason \"slotIndex was not in range 1-4 (inclusive)\"";
        
        this.slotIndex = slotIndex;
    }

    public static Component fixKey(Component textIn){

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
    public boolean isAttackInept()
    {
        return false;//this.self.isUsingItem() || this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }

    @Override
    public boolean isAttackIneptVisually(){
        return false;//this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
    }

    @Override
    public void roundabout$tick() {

    }

    @Override
    public void roundabout$draw(GuiGraphics context, int y, ResourceLocation rl, boolean locked) {
        int x = slotIndex*25;
        y-=1;

        if (locked){
            context.blit(StandIcons.LOCKED_SQUARE_ICON,x-3,y-3,0, 0, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        } else {
            context.blit(StandIcons.SQUARE_ICON,x-3,y-3,0, 0, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            Font renderer = Minecraft.getInstance().font;

            Component specialKey = fixKey(KeyInputRegistry.SLOT_KEYS.get(slotIndex-1).getTranslatedKeyMessage());
            context.drawString(renderer, specialKey, x-1, y+11, 0xffffff, true);
        }


        if ((cooldown.time >= 0) || isAttackIneptVisually()){
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
            if (cooldown.time >= 0) {
                float blit = (20*(1-((float) (1+cooldown.time) /(1+cooldown.maxTime))));
                int b = (int) Math.round(blit);
                RenderSystem.enableBlend();
                context.setColor(1f, 1f, 1f, 1f);

                ResourceLocation COOLDOWN_TEX = StandIcons.COOLDOWN_ICON;

                if (cooldown.isFrozen())
                    COOLDOWN_TEX = StandIcons.FROZEN_COOLDOWN_ICON;

                context.blit(COOLDOWN_TEX, x - 1, y - 1 + b, 0, b, 20, 20-b, 20, 20);
                int num = ((int)(Math.floor((double) cooldown.time /20)+1));
                int offset = x+3;
                if (num <=9){
                    offset = x+7;
                }

                if (!cooldown.isFrozen())
                    context.drawString(Minecraft.getInstance().font, ""+num,offset,y,0xffffff,true);

                RenderSystem.disableBlend();
            }
            context.setColor(1f, 1f, 1f, 0.9f);
        } else {
            context.blit(rl, x, y, 0, 0, 18, 18, 18, 18);
        }
    }
}