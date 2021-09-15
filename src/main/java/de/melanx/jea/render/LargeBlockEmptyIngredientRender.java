package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class LargeBlockEmptyIngredientRender extends LargeBlockIngredientRender {

    @Nullable
    private final Consumer<BlockState> stateConsumer;

    public LargeBlockEmptyIngredientRender(@Nullable Consumer<BlockState> stateConsumer) {
        this.stateConsumer = stateConsumer;
    }

    @Override
    protected void renderBlock(@Nonnull PoseStack poseStack, BlockState state) {
        if (this.stateConsumer != null) {
            this.stateConsumer.accept(state);
        }
    }
}
