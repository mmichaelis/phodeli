package com.github.mmichaelis.phodeli.geo;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents length units.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public enum LengthUnit implements DoubleMeasureUnit<LengthUnit> {
  /*
   * Developer Note: Units must be sorted in decreasing precision order.
   */
  /**
   * Length unit: millimeters.
   *
   * @since 1.0.0
   */
  MILLIMETERS(0.001, "mm"),
  /**
   * Length unit: centimeters.
   *
   * @since 1.0.0
   */
  CENTIMETERS(0.01, "cm"),
  /**
   * Length unit: inches.
   *
   * @since 1.0.0
   */
  INCHES(0.0254, "\""),
  /**
   * Length unit: decimeters.
   *
   * @since 1.0.0
   */
  DECIMETERS(0.1, "dm"),
  /**
   * Length unit: yards.
   *
   * @since 1.0.0
   */
  YARDS(0.9144, "yd"),
  /**
   * Length unit: meters.
   *
   * @since 1.0.0
   */
  METERS(1.0, "m"),
  /**
   * Length unit: kilometers.
   *
   * @since 1.0.0
   */
  KILOMETERS(1000.0, "km"),
  /**
   * Length unit: miles.
   *
   * @since 1.0.0
   */
  MILES(1609.344, "mi");

  /**
   * Common base unit used to convert from different units.
   */
  private final double meters;
  @NotNull
  private final String symbol;

  LengthUnit(final double meters, @NotNull final String symbol) {
    this.meters = meters;
    this.symbol = symbol;
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
   * @param sourceAmount length to convert
   * @param sourceUnit   unit of the length to convert
   * @return length in this unit
   * @since 1.0.0
   */
  @Override
  @Contract(pure = true)
  public final double convert(final double sourceAmount, @NotNull final LengthUnit sourceUnit) {
    if (sourceUnit == this) {
      return sourceAmount;
    }
    return sourceAmount / meters * sourceUnit.meters;
  }

  @Override
  @NotNull
  @Contract(pure = true)
  public final String getSymbol() {
    return symbol;
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

}
