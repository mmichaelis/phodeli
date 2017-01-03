package com.github.mmichaelis.phodeli.geo;

import static java.util.Locale.Category.FORMAT;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Measure unit for measures represented by double values.
 *
 * @param <T> this measure unit type
 * @author Mark Michaelis
 * @since 1.0.0
 */
public interface DoubleMeasureUnit<T extends DoubleMeasureUnit> extends MeasureUnit {

  /**
   * Converts the given measure in the given unit to this unit.
   *
   * @param sourceAmount the measure amount in the given {@code sourceUnit}
   * @param sourceUnit   the unit of the {@code sourceAmount} argument
   * @return the converted measure in this unit
   * @since 1.0.0
   */
  @Contract(pure = true)
  double convert(double sourceAmount, @NotNull T sourceUnit);

  /**
   * Formats the measure amount using the current default locale.
   *
   * @param measureAmount measure amount to format
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  default String format(final double measureAmount) {
    return format(measureAmount, Locale.getDefault(FORMAT));
  }

  /**
   * Formats the measure amount given the given locale.
   *
   * @param measureAmount measure amount to format
   * @param loc           locale
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  default String format(final double measureAmount, @NotNull final Locale loc) {
    NumberFormat nf = NumberFormat.getNumberInstance(loc);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern(getDecimalFormatPattern());
    return df.format(measureAmount);
  }
}
