package br.com.brainweb.interview.core.toggle;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatureToggle {
  String feature();

  boolean expectedToBeOn() default true;
}