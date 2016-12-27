package com.github.mmichaelis.phodeli.geo;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Locale.Category.FORMAT;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Represents the unit of an angle, i. e. either degree or radians.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public enum AngleUnit {
  /**
   * Angle Unit: Degree. Symbol: °.
   *
   * @since 1.0.0
   */
  DEGREES("°", "#,##0.###'°'") {
    @Override
    public double convert(final double sourceAngle, @NotNull final AngleUnit sourceUnit) {
      return sourceUnit.toDegrees(sourceAngle);
    }

    @Override
    public double toDegrees(final double sourceAngle) {
      return sourceAngle;
    }

    @Override
    public double toRadians(final double sourceAngle) {
      return Math.toRadians(sourceAngle);
    }
  },
  /**
   * Angle Unit: Radian. Symbol: rad.
   *
   * @since 1.0.0
   */
  RADIANS("rad", "#,##0.#####' rad'") {
    @Override
    public double convert(final double sourceAngle, @NotNull final AngleUnit sourceUnit) {
      return sourceUnit.toRadians(sourceAngle);
    }

    @Override
    public double toDegrees(final double sourceAngle) {
      return Math.toDegrees(sourceAngle);
    }

    @Override
    public double toRadians(final double sourceAngle) {
      return sourceAngle;
    }
  };

  /**
   * The symbol for the angle unit.
   */
  @NotNull
  private final String symbol;
  /**
   * Format pattern to format angles.
   */
  @NotNull
  private final String decimalFormatPattern;

  /**
   * Constructor specifying angle unit symbol.
   *
   * @param symbol symbol to use
   * @since 1.0.0
   */
  AngleUnit(@NotNull final String symbol, @NotNull final String decimalFormatPattern) {
    this.symbol = symbol;
    this.decimalFormatPattern = decimalFormatPattern;
  }

  /**
   * Converts the given angle in the given unit to this unit.
   *
   * <p>For example, to convert 0.5 rad to degree, use:
   * {@code AngleUnit.DEGREES.convert(0.5D, TimeUnit.RADIANS)}
   *
   * @param sourceAngle the angle in the given {@code sourceUnit}
   * @param sourceUnit  the unit of the {@code sourceAngle} argument
   * @return the converted angle in this unit
   * @since 1.0.0
   */
  public abstract double convert(final double sourceAngle, @NotNull final AngleUnit sourceUnit);

  /**
   * Equivalent to
   * {@link #convert(double, AngleUnit) DEGREES.convert(duration, this)}.
   *
   * @param sourceAngle the angle
   * @return the converted angle,
   * @since 1.0.0
   */
  public abstract double toDegrees(double sourceAngle);

  /**
   * Equivalent to
   * {@link #convert(double, AngleUnit) RADIANS.convert(duration, this)}.
   *
   * @param sourceAngle the angle
   * @return the converted angle,
   * @since 1.0.0
   */
  public abstract double toRadians(double sourceAngle);

  /**
   * Formats the angle given the current default locale.
   *
   * @param angle angle to format
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  public final String format(final double angle) {
    return format(angle, Locale.getDefault(FORMAT));
  }

  /**
   * Formats the angle given the given locale.
   *
   * @param angle angle to format
   * @param loc   locale
   * @return formatted string with unit symbol
   * @since 1.0.0
   */
  @NotNull
  public final String format(final double angle, @NotNull final Locale loc) {
    NumberFormat nf = NumberFormat.getNumberInstance(loc);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern(getDecimalFormatPattern());
    return df.format(angle);
  }

  /**
   * Normalize the given angle for this angle unit.
   *
   * @param sourceAngle angle of this angle unit
   * @return normalized angle
   * @since 1.0.0
   */
  public final double normalized(final double sourceAngle) {
    double asRadians = toRadians(sourceAngle);
    return convert(atan2(sin(asRadians), cos(asRadians)), RADIANS);
  }

  /**
   * Return the symbol for the angle unit.
   *
   * @return symbol
   * @since 1.0.0
   */
  @NotNull
  public final String getSymbol() {
    return symbol;
  }

  /**
   * Returns the decimal format pattern to be used for an angle with symbol as
   * suffix.
   *
   * @return pattern
   * @since 1.0.0
   */
  @NotNull
  public final String getDecimalFormatPattern() {
    return decimalFormatPattern;
  }
}
