package net.hydra.jojomod.entity.corpses;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class FallenZombieModel <T extends FallenMob> extends HumanoidModel<T> {
    protected FallenZombieModel(ModelPart $$0) {
        super($$0);
    }

    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        super.setupAnim($$0, $$1, $$2, $$3, $$4, $$5);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive($$0), this.attackTime, $$3);
    }

    public boolean isAggressive(T $$0) {
        return $$0.isAggressive();
    }
}
