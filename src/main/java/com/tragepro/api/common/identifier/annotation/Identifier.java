package com.tragepro.api.common.identifier.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Identifier {
  String value() default "default";
}
