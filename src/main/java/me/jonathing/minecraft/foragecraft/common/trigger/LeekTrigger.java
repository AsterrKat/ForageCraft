package me.jonathing.minecraft.foragecraft.common.trigger;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import me.jonathing.minecraft.foragecraft.ForageCraft;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.LogManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * This is the custom trigger that is exclusive to the triple baka goal advancement.
 *
 * @author Jonathing
 * @see me.jonathing.minecraft.foragecraft.common.item.LeekItem#hitEntity(ItemStack, LivingEntity, LivingEntity)
 * @since 2.1.2
 */
@MethodsReturnNonnullByDefault
public class LeekTrigger extends AbstractCriterionTrigger<LeekTrigger.Instance>
{
    private static final ResourceLocation ID = ForageCraft.locate("leek_trigger");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected LeekTrigger.Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser)
    {
        return new Instance(entityPredicate);
    }

    /**
     * Triggers the {@link LeekTrigger} for a specific {@link ServerPlayerEntity} when the
     * {@link me.jonathing.minecraft.foragecraft.common.item.LeekItem#hitEntity(ItemStack, LivingEntity, LivingEntity)}
     * method is called.
     *
     * @param playerEntity The {@link ServerPlayerEntity} that caused the trigger by hitting an entity with a leek.
     * @see me.jonathing.minecraft.foragecraft.common.item.LeekItem#hitEntity(ItemStack, LivingEntity, LivingEntity)
     */
    public void trigger(ServerPlayerEntity playerEntity)
    {
        LogManager.getLogger().debug("Triggering the leek trigger.");
        this.triggerListeners(playerEntity, (instance) -> true);
    }

    public static class Instance extends CriterionInstance
    {
        public Instance(EntityPredicate.AndPredicate playerCondition)
        {
            super(ID, playerCondition);
        }

        /**
         * Creates a raw {@link LeekTrigger.Instance} for use in data generation.
         *
         * @return The created {@link LeekTrigger.Instance} with the given parameters.
         * @see LeekTrigger.Instance
         */
        @ParametersAreNonnullByDefault
        public static Instance create()
        {
            return new Instance(EntityPredicate.AndPredicate.ANY_AND);
        }
    }
}
