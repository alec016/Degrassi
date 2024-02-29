package es.degrassi.forge.api.core.component;

import java.util.List;

public interface IDumpComponent extends IComponent {

    void dump(List<String> ids);
}
