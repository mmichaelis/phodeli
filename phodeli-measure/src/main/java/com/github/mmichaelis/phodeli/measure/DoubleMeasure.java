package com.github.mmichaelis.phodeli.measure;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A measure for amounts given in double.
 *
 * @param <S> type of this measure
 * @param <U> unit of this measure
 * @author Mark Michaelis
 * @since 1.0.0
 */
@SuppressWarnings("WeakerAccess")
public interface DoubleMeasure<S extends DoubleMeasure<S, U>, U extends DoubleMeasureUnit<U>>
  extends Measure<S, U> {

  /**
   * Returns this measure in the given unit.
   *
   * @param unit the unit to convert to
   * @return angle in given unit
   * @since 1.0.0
   */
  @Contract(pure = true)
  double get(@NotNull U unit);
}
