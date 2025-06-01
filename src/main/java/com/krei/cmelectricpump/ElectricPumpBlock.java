package com.krei.cmelectricpump;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

//public class ElectricPumpBlock extends WrenchableDirectionalBlock implements SimpleWaterloggedBlock, IBE<PumpBlockEntity> {
public class ElectricPumpBlock extends WrenchableDirectionalBlock implements SimpleWaterloggedBlock {

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

//    @Override
//    public Class<PumpBlockEntity> getBlockEntityClass() {
//        return null;
//    }
//
//    @Override
//    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
//        return null;
//    }
}
