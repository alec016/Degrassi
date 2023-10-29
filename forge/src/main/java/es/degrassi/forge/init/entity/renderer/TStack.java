package es.degrassi.forge.init.entity.renderer;

public interface TStack<Self> {
  Self pushPose();

  Self popPose();
}
