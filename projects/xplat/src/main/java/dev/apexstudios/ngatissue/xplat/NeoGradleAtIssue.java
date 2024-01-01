package dev.apexstudios.ngatissue.xplat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public interface NeoGradleAtIssue
{
    String ID = "ng_at_issue";
    Logger LOGGER = LoggerFactory.getLogger("NeoGradle-AT-Issue");

    GameRules.Key<GameRules.IntegerValue> registerBoundIntGameRule(String id, GameRules.Category category, int defaultValue, int minValue, int maxValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener);

    default void setup()
    {
        var myBoundInt = registerBoundIntGameRule("test_bound_int",
                                                  GameRules.Category.MISC,
                                                  5,
                                                  0,
                                                  10,
                                                  (server, value) -> {
                                                  }
        );
    }
}
