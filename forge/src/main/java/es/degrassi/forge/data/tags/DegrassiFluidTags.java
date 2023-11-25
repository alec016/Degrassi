package es.degrassi.forge.data.tags;

import es.degrassi.forge.data.DegrassiTagProvider;
import es.degrassi.forge.init.registration.FluidRegister;
import es.degrassi.forge.init.registration.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public final class DegrassiFluidTags extends DegrassiTagProvider<Fluid> {
  public DegrassiFluidTags(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, Registry.FLUID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    addCustomTags();
  }

  private void addCustomTags() {
    addFluids();
  }

  private void addFluids() {
    tag(TagRegistry.AllFluidTags.FLUIDS.tag)
      .add(
        FluidRegister.MOLTEN_RED_MATTER.get()
      )
      .replace(false);
  }
}
