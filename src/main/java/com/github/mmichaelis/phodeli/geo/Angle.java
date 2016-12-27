package com.github.mmichaelis.phodeli.geo;

import static com.github.mmichaelis.phodeli.geo.AngleUnit.DEGREES;
import static com.github.mmichaelis.phodeli.geo.AngleUnit.RADIANS;
import static java.util.Objects.hash;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an angle.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
@SuppressWarnings("WeakerAccess")
public final class Angle implements Comparable<Angle>, Serializable {

  private static final long serialVersionUID = 5653567757650975666L;
  /**
   * The angle value/amount.
   */
  private final double angleAmount;
  /**
   * The unit of the angle.
   */
  @NotNull
  private final AngleUnit angleUnit;

  /**
   * Constructor.
   *
   * @param angleAmount amount of the angle
   * @param angleUnit   unit
   */
  private Angle(final double angleAmount, @NotNull final AngleUnit angleUnit) {
    this.angleAmount = angleAmount;
    this.angleUnit = angleUnit;
  }

  /**
   * Creates an angle given as degrees.
   *
   * @param degrees degrees of angle
   * @return angle
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Angle degrees(final double degrees) {
    return new Angle(degrees, DEGREES);
  }

  /**
   * Creates an angle given as radians.
   *
   * @param radians degrees of angle
   * @return angle
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Angle radians(final double radians) {
    return new Angle(radians, RADIANS);
  }

  /**
   * Available units.
   *
   * @return list of available units
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static List<AngleUnit> getUnits() {
    return Arrays.asList(AngleUnit.values());
  }

  /**
   * Returns this angle in radians.
   *
   * @return radians
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toRadians() {
    return get(RADIANS);
  }

  /**
   * Returns this angle in degrees.
   *
   * @return degrees
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double toDegrees() {
    return get(DEGREES);
  }

  /**
   * Returns this angle in the given unit.
   *
   * @param unit the unit to convert to
   * @return angle in given unit
   * @since 1.0.0
   */
  @Contract(pure = true)
  public double get(@NotNull final AngleUnit unit) {
    return unit.convert(angleAmount, angleUnit);
  }

  /**
   * Provides a normalized angle.
   *
   * @return normalized angle
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public Angle normalized() {
    return new Angle(angleUnit.normalized(angleAmount), angleUnit);
  }

  /**
   * Formats the angle using the current default locale.
   *
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public String format() {
    return angleUnit.format(angleAmount);
  }

  /**
   * Formats the angle given the given locale.
   *
   * @param loc locale
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public String format(@NotNull final Locale loc) {
    return angleUnit.format(angleAmount, loc);
  }

  @Override
  public int compareTo(@NotNull final Angle other) {
    return Double.compare(angleAmount, other.get(angleUnit));
  }

  @Override
  public int hashCode() {
    return hash(angleAmount, angleUnit);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Angle other = (Angle) obj;
    return Objects.equals(this.angleAmount, other.angleAmount)
           && Objects.equals(this.angleUnit, other.angleUnit);
  }

  @Override
  public String toString() {
    return super.toString() + "{angleAmount=" + angleAmount + ", angleUnit=" + angleUnit + '}';
  }

}
