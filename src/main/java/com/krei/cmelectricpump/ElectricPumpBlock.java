package com.krei.cmelectricpump;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class ElectricPumpBlock extends WrenchableDirectionalAxisBlock implements SimpleWaterloggedBlock, IBE<ElectricPumpBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ElectricPumpBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    // Temp
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return AllShapes.PUMP.get(state.getValue(FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos,
                                boolean isMoving) {
        DebugPackets.sendNeighborsUpdatePacket(world, pos);
        Direction d = FluidPropagator.validateNeighbourChange(state, world, pos, otherBlock, neighborPos, isMoving);
        if (d == null)
            return;
        if (!isOpenAt(state, d))
            return;
        world.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    public static boolean isOpenAt(BlockState state, Direction d) {
        return d.getAxis() == state.getValue(FACING)
                .getAxis();
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        if (world.isClientSide)
            return;
        if (state != oldState)
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);

        if (isPump(state) && isPump(oldState) && state.getValue(FACING) == oldState.getValue(FACING)
                .getOpposite()) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            if (!(blockEntity instanceof PumpBlockEntity pump))
//                return;
//            pump.pressureUpdate = true;
        }
    }

    public static boolean isPump(BlockState state) {
        return state.getBlock() instanceof PumpBlock;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean blockTypeChanged = !state.is(newState.getBlock());
        if (blockTypeChanged && !world.isClientSide)
            FluidPropagator.propagateChangedPipe(world, pos, state);
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public Class<ElectricPumpBlockEntity> getBlockEntityClass() {
        return ElectricPumpBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElectricPumpBlockEntity> getBlockEntityType() {
        return ElectricPump.BLOCK_ENTITY.get();
    }
}
