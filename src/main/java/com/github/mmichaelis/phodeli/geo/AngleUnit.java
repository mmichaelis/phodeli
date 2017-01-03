package com.github.mmichaelis.phodeli.geo;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the unit of an angle, i. e. either degree or radians.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public enum AngleUnit implements DoubleMeasureUnit<AngleUnit> {
  /**
   * Angle Unit: Degree. Symbol: °.
   *
   * @since 1.0.0
   */
  DEGREES("°") {
    @Override
    @Contract(pure = true)
    public double convert(final double sourceAmount, @NotNull final AngleUnit sourceUnit) {
      return sourceUnit.toDegrees(sourceAmount);
    }

    @Override
    @Contract(pure = true)
    public double toDegrees(final double sourceAngle) {
      return sourceAngle;
    }

    @Override
    @Contract(pure = true)
    public double toRadians(final double sourceAngle) {
      return Math.toRadians(sourceAngle);
    }
  },
  /**
   * Angle Unit: Radian. Symbol: rad.
   *
   * @since 1.0.0
   */
  RADIANS("rad") {
    @Override
    @Contract(pure = true)
    public double convert(final double sourceAmount, @NotNull final AngleUnit sourceUnit) {
      return sourceUnit.toRadians(sourceAmount);
    }

    @Override
    @Contract(pure = true)
    public double toDegrees(final double sourceAngle) {
      return Math.toDegrees(sourceAngle);
    }

    @Override
    @Contract(pure = true)
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
   * Constructor specifying angle unit symbol.
   *
   * @param symbol symbol to use
   * @since 1.0.0
   */
  AngleUnit(@NotNull final String symbol) {
    this.symbol = symbol;
  }

  /**
   * Equivalent to
   * {@link #convert(double, DoubleMeasureUnit) DEGREES.convert(duration, this)}.
   *
   * @param sourceAngle the angle
   * @return the converted angle,
   * @since 1.0.0
   */
  @Contract(pure = true)
  public abstract double toDegrees(double sourceAngle);

  /**
   * Equivalent to
   * {@link #convert(double, DoubleMeasureUnit) RADIANS.convert(duration, this)}.
   *
   * @param sourceAngle the angle
   * @return the converted angle,
   * @since 1.0.0
   */
  @Contract(pure = true)
  public abstract double toRadians(double sourceAngle);

  /**
   * Normalize the given angle for this angle unit.
   *
   * @param sourceAngle angle of this angle unit
   * @return normalized angle
   * @since 1.0.0
   */
  @Contract(pure = true)
  public final double normalized(final double sourceAngle) {
    double asRadians = toRadians(sourceAngle);
    return convert(atan2(sin(asRadians), cos(asRadians)), RADIANS);
  }

  @Override
  @NotNull
  @Contract(pure = true)
  public final String getSymbol() {
    return symbol;
  }

}
