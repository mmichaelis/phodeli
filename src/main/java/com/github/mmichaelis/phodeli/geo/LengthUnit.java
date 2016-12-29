package com.github.mmichaelis.phodeli.geo;

import static java.util.Locale.Category.FORMAT;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents length units.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public enum LengthUnit {
  /*
   * Developer Note: Units must be sorted in decreasing precision order.
   */
  /**
   * Length unit: millimeters.
   *
   * @since 1.0.0
   */
  MILLIMETERS(0.001, "mm", "#,##0.###' mm'"),
  /**
   * Length unit: centimeters.
   *
   * @since 1.0.0
   */
  CENTIMETERS(0.01, "cm", "#,##0.###' cm'"),
  /**
   * Length unit: inches.
   *
   * @since 1.0.0
   */
  INCHES(0.0254, "\"", "#,##0.###'\"'"),
  /**
   * Length unit: decimeters.
   *
   * @since 1.0.0
   */
  DECIMETERS(0.1, "dm", "#,##0.###' dm'"),
  /**
   * Length unit: yards.
   *
   * @since 1.0.0
   */
  YARDS(0.9144, "yd", "#,##0.###' yd'"),
  /**
   * Length unit: meters.
   *
   * @since 1.0.0
   */
  METERS(1.0, "m", "#,##0.###' m'"),
  /**
   * Length unit: kilometers.
   *
   * @since 1.0.0
   */
  KILOMETERS(1000.0, "km", "#,##0.###' km'"),
  /**
   * Length unit: miles.
   *
   * @since 1.0.0
   */
  MILES(1609.344, "mi", "#,##0.###' mi'");

  /**
   * Common base unit used to convert from different units.
   */
  private final double meters;
  @NotNull
  private final String symbol;
  @NotNull
  private final String decimalFormatPattern;

  LengthUnit(final double meters, @NotNull final String symbol,
             @NotNull final String decimalFormatPattern) {
    this.meters = meters;
    this.symbol = symbol;
    this.decimalFormatPattern = decimalFormatPattern;
  }

  /**
   * Converts the given length of this unit to millimeters.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toMillimeters(final double length) {
    return MILLIMETERS.convert(length, this);
  }

  /**
   * Converts the given length of this unit to centimeters.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toCentimeters(final double length) {
    return CENTIMETERS.convert(length, this);
  }

  /**
   * Converts the given length of this unit to inches.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toInches(final double length) {
    return INCHES.convert(length, this);
  }

  /**
   * Converts the given length of this unit to decimeters.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toDecimeters(final double length) {
    return DECIMETERS.convert(length, this);
  }

  /**
   * Converts the given length of this unit to yards.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toYards(final double length) {
    return YARDS.convert(length, this);
  }

  /**
   * Converts the given length of this unit to meters.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toMeters(final double length) {
    return METERS.convert(length, this);
  }

  /**
   * Converts the given length of this unit to kilometers.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toKilometers(final double length) {
    return KILOMETERS.convert(length, this);
  }

  /**
   * Converts the given length of this unit to miles.
   *
   * @param length length to convert
   * @return converted length
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double toMiles(final double length) {
    return MILES.convert(length, this);
  }

  /**
   * Converts the given length with the given unit to a length of this unit.
   *
   * @param sourceLength length to convert
   * @param sourceUnit   unit of the length to convert
   * @return length in this unit
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double convert(final double sourceLength, @NotNull final LengthUnit sourceUnit) {
    if (sourceUnit == this) {
      return sourceLength;
    }
    return sourceLength / meters * sourceUnit.meters;
  }

  /**
   * Return the symbol for the length unit.
   *
   * @return symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public final String getSymbol() {
    return symbol;
  }

  /**
   * Returns the decimal format pattern to be used for a length with symbol as
   * suffix.
   *
   * @return pattern
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public final String getDecimalFormatPattern() {
    return decimalFormatPattern;
  }

  /**
   * Returns the length unit which has the greater precision from this and the given unit.
   *
   * @param other other unit
   * @return length unit with maximum precision
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public final LengthUnit maxPrecision(@NotNull final LengthUnit other) {
    if (this.ordinal() > other.ordinal()) {
      return other;
    }
    return this;
  }

  /**
   * Formats the length using the current default locale.
   *
   * @param length length to format
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @SuppressWarnings("SameParameterValue")
  @NotNull
  @Contract(pure = true)
  public final String format(final double length) {
    return format(length, Locale.getDefault(FORMAT));
  }

  /**
   * Formats the length given the given locale.
   *
   * @param length length to format
   * @param loc    locale
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public final String format(final double length, @NotNull final Locale loc) {
    NumberFormat nf = NumberFormat.getNumberInstance(loc);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern(getDecimalFormatPattern());
    return df.format(length);
  }
}
