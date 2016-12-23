package com.github.mmichaelis.phodeli.geo;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the unit of an angle, i. e. either degree or radians.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public enum AngleUnit {
  /**
   * Angle Unit: Degree.
   *
   * @since 1.0.0
   */
  DEGREE("°") {
    @Override
    public double convert(final double sourceAngle, @NotNull final AngleUnit sourceUnit) {
      switch (sourceUnit) {
        case DEGREE:
          return sourceAngle;
        case RADIAN:
          return Math.toDegrees(sourceAngle);
        default:
          return super.convert(sourceAngle, sourceUnit);
      }
    }
  },
  /**
   * Angle Unit: Radian.
   *
   * @since 1.0.0
   */
  RADIAN("rad") {
    @Override
    public double convert(final double sourceAngle, @NotNull final AngleUnit sourceUnit) {
      switch (sourceUnit) {
        case DEGREE:
          return Math.toRadians(sourceAngle);
        case RADIAN:
          return sourceAngle;
        default:
          return super.convert(sourceAngle, sourceUnit);
      }
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
   * Converts the given angle in the given unit to this unit.
   *
   * <p>For example, to convert 0.5 rad to degree, use:
   * {@code AngleUnit.DEGREE.convert(0.5D, TimeUnit.RADIAN)}
   *
   * @param sourceAngle the angle in the given {@code sourceUnit}
   * @param sourceUnit  the unit of the {@code sourceAngle} argument
   * @return the converted angle in this unit
   * @since 1.0.0
   */
  public double convert(final double sourceAngle, @NotNull final AngleUnit sourceUnit) {
    throw new AbstractMethodError(
      "Conversion from angle unit " + sourceUnit + " not supported yet.");
  }

  /**
   * Return the symbol for the angle unit.
   *
   * @return symbol
   * @since 1.0.0
   */
  @NotNull
  public String getSymbol() {
    return symbol;
  }

}
