package com.github.mmichaelis.phodeli.measure;

import java.util.Formattable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A measure.
 *
 * @param <U> unit of the measure
 * @param <S> self, this type
 * @author Mark Michaelis
 * @since 1.0.0
 */
public interface Measure<S extends Measure, U extends MeasureUnit> extends Formattable {

  /**
   * Transforms this measure to a measure with the given unit. The
   * amount will be converted accordingly.
   *
   * @param unit unit to convert to
   * @return new measure representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  S transform(@NotNull U unit);

}
