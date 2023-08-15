package fuzs.cutthrough.client;

import fuzs.cutthrough.CutThrough;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class CutThroughFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(CutThrough.MOD_ID, CutThroughClient::new);
    }
}
