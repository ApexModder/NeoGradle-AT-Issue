package dev.apexstudios.ngatissue.neoforge.mixin;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.apexstudios.ngatissue.xplat.NeoGradleAtIssue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.BiConsumer;

@Mod(NeoGradleAtIssue.ID)
public final class NeoForgeEntryPoint implements NeoGradleAtIssue
{
    public NeoForgeEntryPoint(IEventBus bus)
    {
        bus.addListener(FMLCommonSetupEvent.class, event -> setup());
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerBoundIntGameRule(String id, GameRules.Category category, int defaultValue, int minValue, int maxValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        var type = newBoundInt(defaultValue, minValue, maxValue, changeListener);
        return GameRules.register(new ResourceLocation(ID, id).toString(), category, type);
    }

    private GameRules.Type<GameRules.IntegerValue> newBoundInt(int defaultValue, int minValue, int maxValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        // constructor for GameRules.Type should remain package-private
        return new GameRules.Type<>(() -> IntegerArgumentType.integer(minValue, maxValue),
                                    type -> new BoundedIntRule(type, defaultValue, minValue, maxValue),
                                    changeListener,
                                    // VisitorCaller functional interface should also remain package-private
                                    GameRules.GameRuleTypeVisitor::visitInteger
        );
    }

    private static final class BoundedIntRule extends GameRules.IntegerValue
    {
        private final int minValue;
        private final int maxValue;

        private BoundedIntRule(GameRules.Type<GameRules.IntegerValue> type, int defaultValue, int minValue, int maxValue)
        {
            super(type, defaultValue);

            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        protected void deserialize(String input)
        {
            var value = safeParse(input);

            if(minValue > value || maxValue < value)
            {
                LOGGER.warn("Failed to parse integer {}. Was out of bounds {} - {}", value, minValue, maxValue);
                return;
            }

            set(value, null);
        }

        @Override
        public boolean tryDeserialize(String input)
        {
            try
            {
                var value = Integer.parseInt(input);

                if(minValue > value || maxValue < value)
                    return false;

                set(value, null);
                return true;
            }
            catch(NumberFormatException e)
            {
                return false;
            }
        }

        @Override
        protected BoundedIntRule copy()
        {
            return new BoundedIntRule(type, get(), minValue, maxValue);
        }

        private static int safeParse(String input)
        {
            if(!input.isEmpty())
            {
                try
                {
                    return Integer.parseInt(input);
                }
                catch(NumberFormatException e)
                {
                    LOGGER.warn("Failed to parse integer {}", input);
                }
            }

            return 0;
        }
    }
}
