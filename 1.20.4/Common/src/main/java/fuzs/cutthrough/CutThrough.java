package fuzs.cutthrough;

import fuzs.cutthrough.config.ClientConfig;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CutThrough implements ModConstructor {
    public static final String MOD_ID = "cutthrough";
    public static final String MOD_NAME = "Cut Through";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
