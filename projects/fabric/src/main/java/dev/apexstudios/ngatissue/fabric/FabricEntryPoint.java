package dev.apexstudios.ngatissue.fabric;

import dev.apexstudios.ngatissue.xplat.NeoGradleAtIssue;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

public final class FabricEntryPoint implements ModInitializer, NeoGradleAtIssue
{
    @Override
    public void onInitialize()
    {
        setup();
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerBoundIntGameRule(String id, GameRules.Category category, int defaultValue, int minValue, int maxValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        var type = GameRuleFactory.createIntRule(defaultValue, minValue, maxValue, changeListener);
        return GameRuleRegistry.register(new ResourceLocation(ID, id).toString(), category, type);
    }
}
