package es.degrassi.forge.api.core.component;


import es.degrassi.forge.api.impl.core.components.config.SideConfig;

public interface ISideConfigComponent extends IComponent {

    SideConfig getConfig();

    String getId();
}
