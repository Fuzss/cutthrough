package fuzs.cutthrough.client.core;

import net.minecraft.client.Minecraft;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public double getPickRange(Minecraft minecraft) {
        // Forge implements GameRenderer::pick like this, both ranges are separated when probing for entities again
        return Math.max(ClientAbstractions.super.getPickRange(minecraft), minecraft.player.getEntityReach());
    }
}
