package com.github.mmichaelis.phodeli.geo;

import static com.github.mmichaelis.phodeli.geo.AngleUnit.DEGREE;
import static com.github.mmichaelis.phodeli.geo.AngleUnit.RADIAN;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Tests {@link AngleUnit}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class AngleUnitTest {

  private static final int TEST_RUNS = 100;

  @Test
  void radianHasExpectedSymbol() {
    assertThat(RADIAN)
      .extracting(AngleUnit::getSymbol)
      .describedAs("Radian should specify 'rad' as symbol.")
      .containsExactly("rad");
  }

  @Test
  void degreeHasExpectedSymbol() {
    assertThat(DEGREE)
      .extracting(AngleUnit::getSymbol)
      .describedAs("Degree should specify '°' as symbol.")
      .containsExactly("°");
  }

  @TestFactory
  Stream<DynamicTest> degreeConvertingToItselfHasEqualValue() {
    Iterator<Double> inputGenerator = new ValueGenerator(TEST_RUNS);
    Function<Double, String>
      displayNameGenerator =
      (input) -> "input: " + input + DEGREE.getSymbol();
    ThrowingConsumer<Double> testExecutor = (input) ->
      assertThat(DEGREE.convert(input, DEGREE))
        .isEqualTo(input);
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> degreeCorrectlyConvertedToRadian() {
    Iterator<Double> inputGenerator = new ValueGenerator(TEST_RUNS);
    Function<Double, String>
      displayNameGenerator =
      (input) -> "input: " + input + DEGREE.getSymbol();
    ThrowingConsumer<Double> testExecutor = (input) ->
      assertThat(RADIAN.convert(input, DEGREE))
        .isCloseTo(toRadians(input), withinPercentage(0.0001D));
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radianConvertingToItselfHasEqualValue() {
    Iterator<Double> inputGenerator = new ValueGenerator(TEST_RUNS);
    Function<Double, String>
      displayNameGenerator =
      (input) -> "input: " + input + RADIAN.getSymbol();
    ThrowingConsumer<Double> testExecutor = (input) ->
      assertThat(RADIAN.convert(input, RADIAN))
        .isEqualTo(input);
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radianCorrectlyConvertedToDegree() {
    Iterator<Double> inputGenerator = new ValueGenerator(TEST_RUNS);
    Function<Double, String>
      displayNameGenerator =
      (input) -> "input: " + input + RADIAN.getSymbol();
    ThrowingConsumer<Double> testExecutor = (input) ->
      assertThat(DEGREE.convert(input, RADIAN))
        .isCloseTo(toDegrees(input), withinPercentage(0.0001D));
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  private static final class ValueGenerator implements Iterator<Double> {

    private final int runs;
    Random random;
    int current;

    private ValueGenerator(int runs) {
      this.runs = runs;
      random = new Random(0L);
      current = 0;
    }

    @Override
    public boolean hasNext() {
      return current < runs;
    }

    @Override
    public Double next() {
      current++;
      return random.nextDouble();
    }
  }
}
