package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.MachineStatus;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.multiblock.IMultiblockController;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.BaseMultiblockPartEntity;
import es.degrassi.forge.core.common.machines.multiblock.uils.Multiblocks;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.recipe.BaseMultiblockControllerRecipe;
import es.degrassi.forge.core.common.machines.multiblock.uils.StateMatcher;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class BaseMultiblockControllerEntity<
  R extends BaseMultiblockControllerRecipe<R>,
  B extends BaseMultiblockControllerBlock,
  E extends BaseMultiblockControllerEntity<R, B, E>
> extends MachineEntity<R> implements IMultiblockController<B, E> {

  private static final int validateTime = 5;

  private final BaseMultiblockControllerBlock block;
  private final Map<BlockPos, StateMatcher> pattern = new HashMap<>();
  private final Map<BlockPos, StateMatcher> patternWithoutRotation = new HashMap<>();
  private Map<BlockPos, StateMatcher> temp;

  private boolean valid = false;
  private int cacheValidate = 0;
  private Direction initialDirection;

  public BaseMultiblockControllerEntity(BlockEntityType<? extends BaseMultiblockControllerEntity<R, B, E>> type, BlockPos pos, BlockState blockState, BaseMultiblockControllerBlock block) {
    super(type, pos, blockState);
    this.block = block;
    Multiblocks.addController(this, pos);
  }

  @Override
  public void init(Direction direction) {
    if (initialDirection != null && initialDirection == direction) return;
    initialDirection = direction;
    temp = new LinkedHashMap<>();
    temp.putAll(patternWithoutRotation);
    pattern.clear();
    pattern.putAll(rotate(temp, switch (direction) {
      case DOWN, UP, NORTH -> Rotation.NONE;
      case SOUTH -> Rotation.CLOCKWISE_180;
      case WEST -> Rotation.COUNTERCLOCKWISE_90;
      case EAST -> Rotation.CLOCKWISE_90;
    }));
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    Multiblocks.removeController(getBlockPos());
  }

  @Override
  public Component getName() {
    return getBlockState().getBlock().getName();
  }

  @Override
  public BaseMultiblockControllerEntity<R, B, E> addToPattern(BlockPos pos, StateMatcher block) {
    if (patternWithoutRotation.containsKey(pos)) throw new IllegalStateException("Current position already exists in pattern");
    patternWithoutRotation.put(pos, block);
    return this;
  }

  @Override
  public Map<BlockPos, StateMatcher> rotate(Map<BlockPos, StateMatcher> blocks, Rotation rotation) {
    Map<BlockPos, StateMatcher> rotated = new HashMap<>();
    blocks.forEach((pos, stateMatcher) -> rotated.put(pos.rotate(rotation), stateMatcher.rotate(rotation)));
    return rotated;
  }

  @Override
  public boolean validate(BlockPos pos, StateMatcher matcher) {
    BlockState state = Objects.requireNonNull(getLevel()).getBlockState(new BlockPos(
      pos.getX() + getBlockPos().getX(),
      pos.getY() + getBlockPos().getY(),
      pos.getZ() + getBlockPos().getZ()
    ));
    return matcher.matches(state);
  }

  @Override
  public void validate() {
    this.valid = pattern.entrySet().stream().allMatch(entry -> this.validate(entry.getKey(), entry.getValue()));
    this.level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BaseMultiblockControllerBlock.VALID, isValid()));
    requestModelDataUpdate();
    setChanged();
  }

  public List<IComponent> getComponents() {
    List<IComponent> components = new LinkedList<>();

    pattern.keySet().forEach(pos -> {
      BlockEntity possiblePart = level.getBlockEntity(getPos(pos));
      if (possiblePart instanceof BaseMultiblockPartEntity<?> entity) {
        components.addAll(entity.getComponentManager().get());
      }
    });

    return components;
  }

  public BlockPos getPos(BlockPos patternPos) {
    return new BlockPos(
      patternPos.getX() + getBlockPos().getX(),
      patternPos.getY() + getBlockPos().getY(),
      patternPos.getZ() + getBlockPos().getZ()
    );
  }

  public List<IComponent> getAllComponents() {
    List<IComponent> components = getComponents();
    components.addAll(super.getComponentManager().get());
    return components;
  }

  public CompoundTag getComponentsNBT() {
    CompoundTag nbt = new CompoundTag();
    getComponents().forEach(type -> type.serialize(nbt));
    return nbt;
  }

  @Override
  public ComponentManager getComponentManager() {
    return new ComponentManager(getAllComponents(), this);
  }

  public static <
    R extends BaseMultiblockControllerRecipe<R>,
    B extends BaseMultiblockControllerBlock,
    E extends BaseMultiblockControllerEntity<R, B, E>
  > void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull BaseMultiblockControllerEntity<R, B, E> entity
  ) {
    if (entity.getBlockState().getValue(BaseMultiblockControllerBlock.FACING) != entity.initialDirection) {
      entity.init(entity.getBlockState().getValue(BaseMultiblockControllerBlock.FACING));
    }

    if (entity.cacheValidate <= 0) {
      entity.validate();
      entity.cacheValidate = validateTime;
      return;
    }
    entity.cacheValidate--;

    if (entity.isValid()) MachineEntity.serverTick(level, pos, state, entity);
    else if (!entity.getStatus().isIdle()) entity.getProcessor().reset();
  }


  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    elementManager.deserializeNBT(tag.getCompound("elementManager"));
    if (tag.contains("processor") && processor != null) processor.deserializeNBT(tag.getCompound("processor"));
    status = MachineStatus.value(tag.getString("status"));
    errorMessage = Component.literal(tag.getString("errorMessage"));
    componentManager.deserializeNBT(tag.getCompound("componentManager"));
    valid = tag.getBoolean("valid");
    initialDirection = Direction.byName(tag.getString("initialDirection"));

    init(getBlockState().getValue(BaseMultiblockControllerBlock.FACING));
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("componentManager", getComponentManager().serializeNBT());
    tag.put("elementManager", getElementManager().serializeNBT());
    if (getProcessor() != null) tag.put("processor", getProcessor().serializeNBT());
    tag.putString("status", getStatus().getSerializedName());
    tag.putString("errorMessage", getErrorMessage().getString());
    tag.putBoolean("valid", isValid());
    tag.putString("initialDirection", getInitialDirection().getSerializedName());
  }
}
