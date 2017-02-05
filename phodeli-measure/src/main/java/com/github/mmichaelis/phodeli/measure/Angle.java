package com.github.mmichaelis.phodeli.measure;

import static com.github.mmichaelis.phodeli.measure.AngleUnit.DEGREES;
import static com.github.mmichaelis.phodeli.measure.AngleUnit.RADIANS;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.github.mmichaelis.phodeli.internal.FormatterUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
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
public final class Angle
  implements Comparable<Angle>, Serializable, DoubleMeasure<Angle, AngleUnit> {

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
    this.angleUnit = requireNonNull(angleUnit, "angleUnit must not be null.");
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
    return angle(degrees, DEGREES);
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
    return angle(radians, RADIANS);
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
   * Creates an angle with the given unit.
   *
   * @param amount angle amount
   * @param unit   the angle unit
   * @return angle
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  public static Angle angle(final double amount, @NotNull final AngleUnit unit) {
    return new Angle(amount, unit);
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

  @NotNull
  @Override
  @Contract(pure = true)
  public Angle getThis() {
    return this;
  }

  @Override
  @Contract(pure = true)
  public double get(@NotNull final AngleUnit unit) {
    return unit.convert(angleAmount, angleUnit);
  }

  @NotNull
  @Override
  public Angle transform(@NotNull final AngleUnit unit) {
    if (unit == angleUnit) {
      return getThis();
    }
    return angle(get(unit), unit);
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
    return angle(angleUnit.normalized(angleAmount), angleUnit);
  }

  @Override
  @Contract(pure = true)
  public int compareTo(@NotNull final Angle other) {
    return Double.compare(angleAmount, other.get(angleUnit));
  }

  @Override
  @Contract(pure = true)
  public int hashCode() {
    return hash(angleAmount, angleUnit);
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
    final Angle other = (Angle) obj;
    return Objects.equals(this.angleAmount, other.angleAmount)
           && Objects.equals(this.angleUnit, other.angleUnit);
  }

  @Override
  @Contract(pure = true)
  public String toString() {
    return super.toString() + "{angleAmount=" + angleAmount + ", angleUnit=" + angleUnit + '}';
  }

  @Override
  public void formatTo(@NotNull final Formatter formatter,
                       final int flags,
                       final int width,
                       final int precision) {
    FormatterUtil.formatMeasureTo(formatter,
                                  angleAmount,
                                  angleUnit.getSymbolPostfix(),
                                  flags,
                                  width,
                                  precision);
  }
}
