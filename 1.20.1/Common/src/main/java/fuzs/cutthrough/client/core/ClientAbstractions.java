package fuzs.cutthrough.client.core;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.client.Minecraft;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    default double getPickRange(Minecraft minecraft) {
        return minecraft.gameMode.getPickRange();
    }
}
