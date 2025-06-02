package com.krei.cmelectricpump;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.redstone.contact.ContactMovementBehaviour;
import com.simibubi.create.content.redstone.contact.RedstoneContactBlock;
import com.simibubi.create.content.redstone.contact.RedstoneContactItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@Mod(ElectricPump.MODID)
public class ElectricPump
{
    public static final String MODID = "cmelectricpump";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MODID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public static final BlockEntry<ElectricPumpBlock> BLOCK =
        REGISTRATE.block("electric_pump", ElectricPumpBlock::new)
                .initialProperties(SharedProperties::copperMetal)
                .properties(p -> p.mapColor(MapColor.STONE))
                .transform(pickaxeOnly())
                .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
                .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                .item()
                .transform(customItemModel())
                .register();

    public static final BlockEntityEntry<ElectricPumpBlockEntity> BLOCK_ENTITY = REGISTRATE
            .blockEntity("electric_pump", ElectricPumpBlockEntity::new)
            .validBlocks(BLOCK)
//            .renderer(() -> PumpRenderer::new)
            .register();

    public ElectricPump(IEventBus modEventBus, ModContainer modContainer)
    {
        REGISTRATE.registerEventListeners(modEventBus);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


}
