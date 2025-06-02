package com.krei.cmelectricpump;

import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class WrenchableDirectionalAxisBlock extends WrenchableDirectionalBlock {

    public static final BooleanProperty AXIS_ALONG_FIRST_COORDINATE = BooleanProperty.create("axis_along_first");

    public WrenchableDirectionalAxisBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS_ALONG_FIRST_COORDINATE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection())
                .setValue(AXIS_ALONG_FIRST_COORDINATE, false);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        ElectricPump.LOGGER.debug(rot.ordinal() + "");
        if (rot.ordinal() % 2 == 1)
            state = state.cycle(AXIS_ALONG_FIRST_COORDINATE);
        return super.rotate(state, rot);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        Direction facing = originalState.getValue(FACING);

        if (facing.getAxis() == targetedFace.getAxis())
            return originalState.cycle(AXIS_ALONG_FIRST_COORDINATE);

        if (targetedFace.getAxis() == Direction.Axis.Y)
            originalState = originalState.cycle(AXIS_ALONG_FIRST_COORDINATE);
        return originalState.setValue(FACING, facing.getClockWise(targetedFace.getAxis()));
    }
}
