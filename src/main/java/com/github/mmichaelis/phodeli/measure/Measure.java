package com.github.mmichaelis.phodeli.measure;

import java.util.Locale;

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
public interface Measure<S extends Measure, U extends MeasureUnit> {

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

  /**
   * Formats the measure using the current default locale.
   *
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  String format();

  /**
   * Formats the measure given the given locale.
   *
   * @param loc locale
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  String format(@NotNull Locale loc);
}
