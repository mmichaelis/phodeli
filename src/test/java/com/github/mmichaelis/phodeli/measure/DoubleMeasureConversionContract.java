package com.github.mmichaelis.phodeli.measure;

import com.github.mmichaelis.phodeli.test.SpecificationContract;

import java.util.function.Function;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A contract to validate for implementations of DoubleMeasure and DoubleMeasureUnit.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
final class DoubleMeasureConversionContract<T extends Comparable<T>>
  implements SpecificationContract {

  private static final Offset<Double> TOLERANCE = Offset.offset(0.0001D);

  @NotNull
  private final T sourceUnit;
  @NotNull
  private final T targetUnit;
  @NotNull
  private final Function<Double, Double> indirectConvertFunction;
  @NotNull
  private final Function<Double, Double> reverseConvertFunction;
  @NotNull
  private final Function<Double, Double> directConvertFunction;

  DoubleMeasureConversionContract(
    @NotNull final T sourceUnit,
    @NotNull final T targetUnit,
    @NotNull final Function<Double, Double> indirectConvertFunction,
    @NotNull final Function<Double, Double> reverseConvertFunction,
    @NotNull final Function<Double, Double> directConvertFunction) {
    this.sourceUnit = sourceUnit;
    this.targetUnit = targetUnit;
    this.indirectConvertFunction = indirectConvertFunction;
    this.reverseConvertFunction = reverseConvertFunction;
    this.directConvertFunction = directConvertFunction;
  }

  @NotNull
  @Contract(pure = true)
  public String describe() {
    return "Converting " + sourceUnit + " to " + targetUnit;
  }

  public void perform() {
    SoftAssertions assertions = new SoftAssertions();
    canConvertDirectlyToTargetUnit(assertions);
    canConvertIndirectlyToTargetUnit(assertions);
    backAndForthProvidesSimilarResult(assertions);
    assertions.assertAll();
  }

  private void assertCanConvertToTargetUnit(
    @NotNull final SoftAssertions assertions,
    @NotNull final Function<Double, Double> convertFunction) {
    double amount = 1D;
    double result = convertFunction.apply(amount);
    float orderUnitPrecision = Math.signum(sourceUnit.compareTo(targetUnit));
    if (orderUnitPrecision < 0D) {
      assertions.assertThat(result).isLessThan(amount);
    } else if (orderUnitPrecision > 0D) {
      assertions.assertThat(result).isGreaterThan(amount);
    } else {
      assertions.assertThat(result).isCloseTo(amount, TOLERANCE);
    }
  }

  private void canConvertDirectlyToTargetUnit(@NotNull final SoftAssertions assertions) {
    assertCanConvertToTargetUnit(assertions, directConvertFunction);
  }

  private void canConvertIndirectlyToTargetUnit(@NotNull final SoftAssertions assertions) {
    assertCanConvertToTargetUnit(assertions, indirectConvertFunction);
  }

  private void backAndForthProvidesSimilarResult(@NotNull final SoftAssertions assertions) {
    double amount = 1D;
    double result1 = indirectConvertFunction.apply(amount);
    double result2 = reverseConvertFunction.apply(result1);
    assertions.assertThat(result2).isCloseTo(amount, TOLERANCE);
  }

}
