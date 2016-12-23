package com.github.mmichaelis.phodeli.geo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Michaelis
 * @since 1.0.0
 */
public enum AngleUnit {
  DEGREE("°") {
    @Override
    public double convert(double sourceAngle, @NotNull AngleUnit sourceUnit) {
      switch (sourceUnit) {
        case DEGREE:
          return sourceAngle;
        case RADIAN:
          return Math.toDegrees(sourceAngle);
      }
      return super.convert(sourceAngle, sourceUnit);
    }
  },
  RADIAN("rad") {
    @Override
    public double convert(double sourceAngle, @NotNull AngleUnit sourceUnit) {
      switch (sourceUnit) {
        case DEGREE:
          return Math.toRadians(sourceAngle);
        case RADIAN:
          return sourceAngle;
      }
      return super.convert(sourceAngle, sourceUnit);
    }
  };

  @NonNls
  private final @NotNull String symbol;

  AngleUnit(@NonNls @NotNull String symbol) {
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
   */
  public double convert(double sourceAngle, @NotNull AngleUnit sourceUnit) {
    throw new AbstractMethodError(
      "Conversion from angle unit " + sourceUnit + " not supported yet.");
  }

  public @NotNull String getSymbol() {
    return symbol;
  }

}
