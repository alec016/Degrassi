package es.degrassi.forge.core.common.machines.multiblock.uils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.compress.utils.Lists;
import oshi.util.tuples.Pair;

@Getter
@AllArgsConstructor
public class StateMatcher {
  private final List<BlockState> possibleStates = new LinkedList<>();
  private final List<Pair<Property<?>, List<?>>> props = new LinkedList<>();

  /**
   * If no props defined then are used the default props defined in the state param
   * @param state {@link BlockState}
   * @param props list of {@link Pair} of {@link Property} and a list of possible values of that {@link BlockState} property
   */
  @SafeVarargs
  public StateMatcher(BlockState state, Pair<Property<?>, List<?>>... props) {
    if (state == null) throw new IllegalStateException("BlockState to match can not be null");
    possibleStates.add(state);
    if (props.length > 0)
      this.props.addAll(List.of(props));
    else {
      state.getProperties().forEach(prop -> this.props.add(new Pair<>(prop, List.of(state.getValue(prop)))));
    }
  }

  /**
   * To determine the props (if no props are defined in param) with values only the first {@link BlockState} is used
   * @param states list of {@link BlockState} of different blocks that have exactly the same props
   * @param props list of {@link Pair} of {@link Property} and a list of possible values of that {@link BlockState} property
   */
  @SafeVarargs
  public StateMatcher(List<BlockState> states, Pair<Property<?>, List<?>>... props) {
    if (states == null) throw new IllegalStateException("BlockStates to match can not be null");
    if (states.isEmpty()) throw new IllegalStateException("BlockStates to match can not be empty. Please at least add one");
    possibleStates.addAll(states);
    if (props.length > 0)
      this.props.addAll(List.of(props));
    else {
      BlockState state = states.get(0);
      state.getProperties().forEach(prop -> this.props.add(new Pair<>(prop, List.of(state.getValue(prop)))));
    }
  }

  private StateMatcher(List<BlockState> states, List<Pair<Property<?>, List<?>>> props) {
    this.possibleStates.addAll(states);
    this.props.addAll(props);
  }

  public StateMatcher rotate(Rotation rotation) {
    List<BlockState> rotatedStates = new LinkedList<>();
    List<Pair<Property<?>, List<?>>> rotatedProps = new LinkedList<>();
    List<Property<?>> props = Lists.newArrayList(this.props.stream().map(Pair::getA).iterator());

    possibleStates.forEach(state -> {
      if (state.getValues().containsKey(BlockStateProperties.HORIZONTAL_FACING)) {
        AtomicReference<Direction> direction = new AtomicReference<>(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
        props.forEach(prop -> {
          if (prop.equals(BlockStateProperties.HORIZONTAL_FACING)) {
            int index = props.indexOf(BlockStateProperties.HORIZONTAL_FACING);
            direction.set(rotation.rotate(direction.get()));
            rotatedStates.add(state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.get()));
            List<?> values = this.props.get(index).getB();
            if (values.isEmpty() || values.size() == 1){
              rotatedProps.add(new Pair<>(BlockStateProperties.HORIZONTAL_FACING, List.of(direction.get())));
            } else {
              rotatedProps.add(new Pair<>(BlockStateProperties.HORIZONTAL_FACING, Lists.newArrayList(values.iterator())));
            }
          }
        });
      } else if (state.getValues().containsKey(BlockStateProperties.FACING)) {
        AtomicReference<Direction> direction = new AtomicReference<>(state.getValue(BlockStateProperties.FACING));
        props.forEach(prop -> {
          int index = props.indexOf(BlockStateProperties.FACING);
          if (direction.get().getAxis() == Direction.Axis.Y) {
            rotatedStates.add(state);
            rotatedProps.add(this.props.get(index));
            return;
          }
          direction.set(rotation.rotate(direction.get()));
          rotatedStates.add(state.setValue(BlockStateProperties.FACING, direction.get()));
          List<?> values = this.props.get(index).getB();
          if (values.isEmpty() || values.size() == 1){
            rotatedProps.add(new Pair<>(BlockStateProperties.FACING, List.of(direction.get())));
          } else {
            rotatedProps.add(new Pair<>(BlockStateProperties.FACING, Lists.newArrayList(values.iterator())));
          }
        });
      } else {
        rotatedStates.add(state);
      }
    });

    this.props.forEach(pair -> {
      if (rotatedProps.stream().noneMatch(pair2 -> pair2.getA().equals(pair.getA())))
        rotatedProps.add(new Pair<>(pair.getA(), pair.getB()));
    });

    return new StateMatcher(rotatedStates, rotatedProps);
  }

  public boolean matches(BlockState state) {
    if (state == null) return false;
    AtomicBoolean matches = new AtomicBoolean(true);
    if (possibleStates.stream().noneMatch(s -> s.getBlock().equals(state.getBlock()))) return false;
    this.props.forEach(pair -> {
      if (!matches.get()) return;
      Property<?> prop = pair.getA();
      if (!state.hasProperty(prop)) {
        matches.set(false);
        return;
      }
      List<?> possibleValues = pair.getB();
      if (possibleValues.isEmpty()) {
        matches.set(false);
        return;
      }
      AtomicBoolean valueMatches = new AtomicBoolean(false);
      possibleValues.forEach(value -> {
        if (!matches.get() || valueMatches.get()) return;
        if (state.getValue(prop).equals(value)) valueMatches.set(true);
      });
      if (!valueMatches.get()) matches.set(false);
    });
    return matches.get();
  }

  public String toString() {
    return "StateMatcher(states= " +
      possibleStates + ", props=" +
      props.stream().map(prop -> "prop=" + prop.getA().getName() + ", values=" + prop.getB()).toList() +
      ")";
  }
}
