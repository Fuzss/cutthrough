package fuzs.cutthrough.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Only allow targeting alive entities, as opposed to entities that have already died and only remain to render a death animation. Greatly helps in combat as dead entities will no longer absorb hits.")
    public boolean targetAliveOnly = true;
}
