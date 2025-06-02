package com.krei.cmelectricpump;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;

import javax.annotation.Nullable;
import java.util.*;

public class ElectricPumpBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IHaveHoveringInformation {

    Couple<MutableBoolean> sidesToUpdate;
    boolean pressureUpdate;

    public ElectricPumpBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        sidesToUpdate = Couple.create(MutableBoolean::new);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
//        behaviours.add(new PumpFluidTransferBehaviour(this));
//        registerAwardables(behaviours, FluidPropagator.getSharedTriggers());
//        registerAwardables(behaviours, AllAdvancements.PUMP);
    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        if (level.isClientSide && !isVirtual())
//            return;
//
//        sidesToUpdate.forEachWithContext((update, isFront) -> {
//            if (update.isFalse())
//                return;
//            update.setFalse();
//            distributePressureTo(isFront ? getFront() : getFront().getOpposite());
//        });
//    }
//
//    @Nullable
//    protected Direction getFront() {
//        BlockState blockState = getBlockState();
//        if (!(blockState.getBlock() instanceof PumpBlock))
//            return null;
//        return blockState.getValue(PumpBlock.FACING);
//    }
//
//    protected void distributePressureTo(Direction side) {
//        if (getSpeed() == 0)
//            return;
//
//        BlockFace start = new BlockFace(worldPosition, side);
//        boolean pull = isPullingOnSide(isFront(side));
//        Set<BlockFace> targets = new HashSet<>();
//        Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph = new HashMap<>();
//
//        if (!pull)
//            FluidPropagator.resetAffectedFluidNetworks(level, worldPosition, side.getOpposite());
//
//        if (!hasReachedValidEndpoint(level, start, pull)) {
//
//            pipeGraph.computeIfAbsent(worldPosition, $ -> Pair.of(0, new IdentityHashMap<>()))
//                    .getSecond()
//                    .put(side, pull);
//            pipeGraph.computeIfAbsent(start.getConnectedPos(), $ -> Pair.of(1, new IdentityHashMap<>()))
//                    .getSecond()
//                    .put(side.getOpposite(), !pull);
//
//            List<Pair<Integer, BlockPos>> frontier = new ArrayList<>();
//            Set<BlockPos> visited = new HashSet<>();
//            int maxDistance = FluidPropagator.getPumpRange();
//            frontier.add(Pair.of(1, start.getConnectedPos()));
//
//            while (!frontier.isEmpty()) {
//                Pair<Integer, BlockPos> entry = frontier.remove(0);
//                int distance = entry.getFirst();
//                BlockPos currentPos = entry.getSecond();
//
//                if (!level.isLoaded(currentPos))
//                    continue;
//                if (visited.contains(currentPos))
//                    continue;
//                visited.add(currentPos);
//                BlockState currentState = level.getBlockState(currentPos);
//                FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, currentPos);
//                if (pipe == null)
//                    continue;
//
//                for (Direction face : FluidPropagator.getPipeConnections(currentState, pipe)) {
//                    BlockFace blockFace = new BlockFace(currentPos, face);
//                    BlockPos connectedPos = blockFace.getConnectedPos();
//
//                    if (!level.isLoaded(connectedPos))
//                        continue;
//                    if (blockFace.isEquivalent(start))
//                        continue;
//                    if (hasReachedValidEndpoint(level, blockFace, pull)) {
//                        pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
//                                .getSecond()
//                                .put(face, pull);
//                        targets.add(blockFace);
//                        continue;
//                    }
//
//                    FluidTransportBehaviour pipeBehaviour = FluidPropagator.getPipe(level, connectedPos);
//                    if (pipeBehaviour == null)
//                        continue;
//                    if (pipeBehaviour instanceof PumpBlockEntity.PumpFluidTransferBehaviour)
//                        continue;
//                    if (visited.contains(connectedPos))
//                        continue;
//                    if (distance + 1 >= maxDistance) {
//                        pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
//                                .getSecond()
//                                .put(face, pull);
//                        targets.add(blockFace);
//                        continue;
//                    }
//
//                    pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
//                            .getSecond()
//                            .put(face, pull);
//                    pipeGraph.computeIfAbsent(connectedPos, $ -> Pair.of(distance + 1, new IdentityHashMap<>()))
//                            .getSecond()
//                            .put(face.getOpposite(), !pull);
//                    frontier.add(Pair.of(distance + 1, connectedPos));
//                }
//            }
//        }
//
//        // DFS
//        Map<Integer, Set<BlockFace>> validFaces = new HashMap<>();
//        searchForEndpointRecursively(pipeGraph, targets, validFaces,
//                new BlockFace(start.getPos(), start.getOppositeFace()), pull);
//
//        float pressure = Math.abs(getSpeed());
//        for (Set<BlockFace> set : validFaces.values()) {
//            int parallelBranches = Math.max(1, set.size() - 1);
//            for (BlockFace face : set) {
//                BlockPos pipePos = face.getPos();
//                Direction pipeSide = face.getFace();
//
//                if (pipePos.equals(worldPosition))
//                    continue;
//
//                boolean inbound = pipeGraph.get(pipePos)
//                        .getSecond()
//                        .get(pipeSide);
//                FluidTransportBehaviour pipeBehaviour = FluidPropagator.getPipe(level, pipePos);
//                if (pipeBehaviour == null)
//                    continue;
//
//                pipeBehaviour.addPressure(pipeSide, inbound, pressure / parallelBranches);
//            }
//        }
//
//    }
//
//
//
//
//
//    public boolean isSideAccessible(Direction side) {
//        BlockState blockState = getBlockState();
//        if (!(blockState.getBlock() instanceof PumpBlock))
//            return false;
//        return blockState.getValue(PumpBlock.FACING)
//                .getAxis() == side.getAxis();
//    }
//
//    public float getSpeed() {
//        if (level != null && level.tickRateManager().isFrozen())
//            return 0;
//        return 16;
//    }
//
//    protected boolean isFront(Direction side) {
//        BlockState blockState = getBlockState();
//        if (!(blockState.getBlock() instanceof PumpBlock))
//            return false;
//        Direction front = blockState.getValue(PumpBlock.FACING);
//        return side == front;
//    }
//
//    public boolean isPullingOnSide(boolean front) {
//        return !front;
//    }
//
//    class PumpFluidTransferBehaviour extends FluidTransportBehaviour {
//
//        public PumpFluidTransferBehaviour(SmartBlockEntity be) {
//            super(be);
//        }
//
//        @Override
//        public void tick() {
//            super.tick();
//            for (Map.Entry<Direction, PipeConnection> entry : interfaces.entrySet()) {
//                boolean pull = isPullingOnSide(isFront(entry.getKey()));
//                Couple<Float> pressure = entry.getValue().getPressure();
//                pressure.set(pull, Math.abs(getSpeed()));
//                pressure.set(!pull, 0f);
//            }
//        }
//
//        @Override
//        public boolean canHaveFlowToward(BlockState state, Direction direction) {
//            return isSideAccessible(direction);
//        }
//
//        @Override
//        public AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state,
//                                                        Direction direction) {
//            AttachmentTypes attachment = super.getRenderedRimAttachment(world, pos, state, direction);
//            if (attachment == AttachmentTypes.RIM)
//                return AttachmentTypes.NONE;
//            return attachment;
//        }
//
//    }


}
