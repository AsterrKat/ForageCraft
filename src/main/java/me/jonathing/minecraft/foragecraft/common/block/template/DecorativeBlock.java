package me.jonathing.minecraft.foragecraft.common.block.template;

import me.jonathing.minecraft.foragecraft.common.registry.ForageBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Supplier;

/**
 * This class holds the template for any decorative blocks in ForageCraft. This includes blocks such as
 * {@link ForageBlocks#rock}, {@link ForageBlocks#flat_rock}, and {@link ForageBlocks#stick}.
 *
 * @author Jonathing
 * @see FallingBlock
 * @since 2.0.0
 */
public class DecorativeBlock extends FallingBlock
{
    public static final VoxelShape STICK_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final VoxelShape ROCK_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    public static final VoxelShape FLAT_ROCK_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    private final VoxelShape shape;
    private final Supplier<Item> decorativeItem;

    public DecorativeBlock(Properties properties, VoxelShape shape, Supplier<Item> decorativeItem)
    {
        super(properties);
        this.shape = shape;
        this.decorativeItem = decorativeItem;
    }

    /**
     * This method defines the hitbox for a default decorative block. The placeholder is the hitbox for
     * {@link ForageBlocks#rock}.
     * <p>
     * I'm not really sure what these numbers actually mean, but Bailey and I toyed with these values enough to get
     * exactly what we needed for this item.
     *
     * @see FallingBlock#getShape(BlockState, IBlockReader, BlockPos, ISelectionContext)
     */
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return this.shape;
    }

    /**
     * This method checks if the decorative block can be placed by checking if the given {@link BlockState} is air and
     * if the {@link BlockState} of the {@link BlockPos} right under it is solid.
     *
     * @param blockState The {@link BlockState} to replace with the decorative block.
     * @param world      The {@link IWorldReader} that the {@link BlockState} resides in.
     * @param blockPos   The {@link BlockPos} of the {@link BlockState}.
     * @return The result of the position validity check.
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(@Nonnull BlockState blockState, IWorldReader world, @Nonnull BlockPos blockPos)
    {
        return world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.down()).isSolid();
    }

    /**
     * This essentially just prevents particles from being spawned under a decorative block floating in stasis. They're
     * not really meant to do that.
     *
     * @see FallingBlock#animateTick(BlockState, World, BlockPos, Random)
     */
    @Override
    @ParametersAreNonnullByDefault
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_)
    {
    }

    /**
     * This method contains the logic that takes place when a block is activated by a player right-clicking on it. By
     * default, when right clicking on a decorative block, it will delete itself from the world and drop it's related
     * item given from the {@link #getDecorativeItem()} method.
     *
     * @param blockState          The {@link BlockState} of the decorative block that was activated.
     * @param world               The {@link World} in which the block was activated.
     * @param blockPos            The {@link BlockPos} of the decorative block that was activated.
     * @param player              The {@link PlayerEntity} that activated the block.
     * @param hand                The {@link Hand} of the {@link PlayerEntity} that activated the block.
     * @param blockRayTraceResult The {@link BlockRayTraceResult} given for the method.
     * @return {@link ActionResultType#SUCCESS}
     */
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
    {
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
        Block.spawnAsEntity(world, blockPos, new ItemStack(this.getDecorativeItem(), 1));
        return ActionResultType.SUCCESS;
    }

    /**
     * Each {@link DecorativeBlock} has its own decorative item that it drops wheneven it is right-clicked, broken, or
     * otherwise removed from the world. In this way, other decorative block classes can {@link Override} this method
     * and dictate that that specific class will drop that specific item.
     *
     * @return The {@link Item} that this specific {@link DecorativeBlock} instance portrays itself as.
     */
    public Item getDecorativeItem()
    {
        return this.decorativeItem.get();
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @Override
    @Nonnull
    public OffsetType getOffsetType()
    {
        return OffsetType.XZ;
    }
}
