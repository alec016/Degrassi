package es.degrassi.forge.core.common.machines.multiblock.uils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import oshi.util.tuples.Pair;

@Getter
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
