package fuzs.cutthrough.neoforge;

import fuzs.cutthrough.CutThrough;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(CutThrough.MOD_ID)
public class CutThroughNeoForge {

    public CutThroughNeoForge() {
        ModConstructor.construct(CutThrough.MOD_ID, CutThrough::new);
    }
}
