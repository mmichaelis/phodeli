package com.github.mmichaelis.phodeli.measure;

import com.github.mmichaelis.phodeli.internal.FormatterUtil;

import java.io.Serializable;
import java.util.Formatter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a certain length of a certain unit.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
@SuppressWarnings("WeakerAccess")
public final class Length
  implements Comparable<Length>, Serializable, DoubleMeasure<Length, LengthUnit> {

  private static final long serialVersionUID = -4451024189014010633L;
  private final double lengthAmount;
  @NotNull
  private final LengthUnit lengthUnit;

  private Length(final double lengthAmount, @NotNull final LengthUnit lengthUnit) {
    this.lengthAmount = lengthAmount;
    this.lengthUnit = lengthUnit;
  }

  /**
   * Creates a length instance with the given length amount and unit.
   *
   * @param lengthAmount length amount
   * @param lengthUnit   length unit
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length length(final double lengthAmount, @NotNull final LengthUnit lengthUnit) {
    return new Length(lengthAmount, lengthUnit);
  }

  /**
   * Creates a length in millimeters.
   *
   * @param lengthAmount length in millimeters
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length mm(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.MILLIMETERS);
  }

  /**
   * Creates a length in centimeters.
   *
   * @param lengthAmount length in centimeters
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length cm(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.CENTIMETERS);
  }

  /**
   * Creates a length in decimeters.
   *
   * @param lengthAmount length in decimeters
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length dm(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.DECIMETERS);
  }

  /**
   * Creates a length in meters.
   *
   * @param lengthAmount length in meters
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  @SuppressWarnings("MethodName")
  public static Length m(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.METERS);
  }

  /**
   * Creates a length in kilometers.
   *
   * @param lengthAmount length in kilometers
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length km(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.KILOMETERS);
  }

  /**
   * Creates a length in miles.
   *
   * @param lengthAmount length in miles
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length mi(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.MILES);
  }

  /**
   * Creates a length in inches.
   *
   * @param lengthAmount length in inches
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length inch(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.INCHES);
  }

  /**
   * Creates a length in yards.
   *
   * @param lengthAmount length in yards
   * @return length representation
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Length yd(final double lengthAmount) {
    return length(lengthAmount, LengthUnit.YARDS);
  }

  /**
   * Returns this length as millimeters.
   *
   * @return length in millimeters
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toMillimeters() {
    return get(LengthUnit.MILLIMETERS);
  }

  /**
   * Returns this length as centimeters.
   *
   * @return length in centimeters
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toCentimeters() {
    return get(LengthUnit.CENTIMETERS);
  }

  /**
   * Returns this length as miles.
   *
   * @return length in miles
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toMiles() {
    return get(LengthUnit.MILES);
  }

  /**
   * Returns this length as kilometers.
   *
   * @return length in kilometers
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toKilometers() {
    return get(LengthUnit.KILOMETERS);
  }

  /**
   * Returns this length as meters.
   *
   * @return length in meters
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toMeters() {
    return get(LengthUnit.METERS);
  }

  /**
   * Returns this length as yards.
   *
   * @return length in yards
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toYards() {
    return get(LengthUnit.YARDS);
  }

  /**
   * Returns this length as decimeters.
   *
   * @return length in decimeters
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toDecimeters() {
    return get(LengthUnit.DECIMETERS);
  }

  /**
   * Returns this length as inches.
   *
   * @return length in inches
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toInches() {
    return get(LengthUnit.INCHES);
  }


  @NotNull
  @Override
  @Contract(pure = true)
  public Length getThis() {
    return this;
  }

  @Override
  @Contract(pure = true)
  public double get(@NotNull final LengthUnit unit) {
    return unit.convert(lengthAmount, lengthUnit);
  }

  @NotNull
  @Override
  public Length transform(@NotNull final LengthUnit unit) {
    if (unit == lengthUnit) {
      return getThis();
    }
    return length(get(unit), unit);
  }

  @Override
  @Contract(pure = true)
  public int compareTo(@NotNull final Length other) {
    return Double.compare(lengthAmount, other.get(lengthUnit));
  }

  @Override
  @Contract(pure = true)
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(lengthAmount);
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + lengthUnit.hashCode();
    return result;
  }

  @Override
  @Contract(pure = true)
  public boolean equals(final Object obj) {
    if (getThis() == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Length length = (Length) obj;

    return Double.compare(length.lengthAmount, lengthAmount) == 0
           && lengthUnit == length.lengthUnit;
  }

  @Override
  @Contract(pure = true)
  public String toString() {
    return super.toString() + "{" + "lengthAmount=" + lengthAmount + ", lengthUnit=" + lengthUnit
           + '}';
  }

  @Override
  public void formatTo(@NotNull final Formatter formatter,
                       final int flags,
                       final int width,
                       final int precision) {
    FormatterUtil.formatMeasureTo(formatter,
                                  lengthAmount,
                                  lengthUnit.getSymbolPostfix(),
                                  flags,
                                  width,
                                  precision);
  }
}
