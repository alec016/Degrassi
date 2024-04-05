package es.degrassi.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import net.minecraftforge.fml.LogicalSide;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface UseOnly {
  LogicalSide value();
}
