package com.github.mmichaelis.phodeli.measure;

import static com.github.mmichaelis.phodeli.measure.AngleUnit.DEGREES;
import static com.github.mmichaelis.phodeli.measure.AngleUnit.RADIANS;
import static com.github.mmichaelis.phodeli.test.SerializableCondition.serializable;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Math.PI;
import static java.lang.Math.round;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Locale.Category.FORMAT;
import static java.util.Locale.getDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.slf4j.Logger;

/**
 * Tests {@link AngleUnit}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class AngleUnitTest {

  private static final Logger LOG = getLogger(lookup().lookupClass());
  /**
   * Using fixed seed to provide reproducible test runs.
   */
  private static final long RANDOM_SEED = 0L;

  private static final Offset<Double> TOLERANCE = Offset.offset(0.0001D);
  private static final long TEST_RUNS = 20L;
  private static final Function<TestInfo, String> TEST_NAME =
    input -> input.getTestMethod().map(Method::getName).orElse("unknown");

  @Test
  void degreesIsSerializable() {
    assertThat(DEGREES).is(serializable());
  }

  @Test
  void radiansIsSerializable() {
    assertThat(RADIANS).is(serializable());
  }

  @Test
  void radianHasExpectedSymbol() {
    assertThat(RADIANS)
      .extracting(AngleUnit::getSymbol)
      .describedAs("Radian should specify 'rad' as symbol.")
      .containsExactly("rad");
  }

  @Test
  void degreeHasExpectedSymbol() {
    assertThat(DEGREES)
      .extracting(AngleUnit::getSymbol)
      .describedAs("Degree should specify '°' as symbol.")
      .containsExactly("°");
  }

  @TestFactory
  Stream<DynamicTest> degreeConvertingToItselfHasEqualValue(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -720D, 720D).iterator();
    Function<Double, String>
      displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + DEGREES.format(input);
    ThrowingConsumer<Double> testExecutor = (input) -> {
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(DEGREES.convert(input, DEGREES)).isEqualTo(input);
      assertions.assertThat(DEGREES.toDegrees(input)).isEqualTo(input);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> degreeCorrectlyConvertedToRadian(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -720D, 720D).iterator();
    Function<Double, String>
      displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + DEGREES.format(input);
    ThrowingConsumer<Double> testExecutor = (input) -> {
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(RADIANS.convert(input, DEGREES)).isCloseTo(toRadians(input), TOLERANCE);
      assertions.assertThat(DEGREES.toRadians(input)).isCloseTo(toRadians(input), TOLERANCE);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radianConvertingToItselfHasEqualValue(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -PI * 4D, PI * 4D).iterator();
    Function<Double, String>
      displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + RADIANS.format(input);
    ThrowingConsumer<Double> testExecutor = (input) -> {
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(RADIANS.convert(input, RADIANS)).isEqualTo(input);
      assertions.assertThat(RADIANS.toRadians(input)).isEqualTo(input);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radianCorrectlyConvertedToDegree(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -PI * 4D, PI * 4D).iterator();
    Function<Double, String> displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + RADIANS.format(input);
    ThrowingConsumer<Double> testExecutor = (input) -> {
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(DEGREES.convert(input, RADIANS)).isCloseTo(toDegrees(input), TOLERANCE);
      assertions.assertThat(RADIANS.toDegrees(input)).isCloseTo(toDegrees(input), TOLERANCE);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> degreesCorrectlyNormalized(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      Arrays
        .asList(-MAX_VALUE,
                -361D, -360D, -359D,
                -271D, -270D, -269D,
                -181D, -180D, -179D,
                -91D, -90D, -89D,
                -1D, 0D, Double.MIN_VALUE, 1D,
                89D, 90D, 91D,
                269D, 270D, 271D,
                359D, 360D, 361D,
                MAX_VALUE)
        .iterator();
    Function<Double, String> displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + DEGREES.format(input);
    ThrowingConsumer<Double> testExecutor =
      input -> {
        SoftAssertions assertions = new SoftAssertions();
        double normalized = DEGREES.normalized(input);
        String inputAsString = DEGREES.format(input);
        LOG.info("Degrees: {} normalized to {}", inputAsString, DEGREES.format(normalized));
        assertions.assertThat(normalized)
          .describedAs("Normalized value of %s must be greater than or equal to -180°",
                       inputAsString)
          .isGreaterThanOrEqualTo(-180D);
        assertions.assertThat(normalized)
          .describedAs("Normalized value of %s must be less than or equal to 180°", inputAsString)
          .isLessThanOrEqualTo(180D);
        if (input >= -180D && input <= 180D) {
          assertions.assertThat(normalized)
            .describedAs("Already normalized input %s should not change too much.", inputAsString)
            .isCloseTo(input, TOLERANCE);
        }
        assertions.assertThat(normalized)
          .describedAs("Normalizing %s again should provide a similar result.", inputAsString)
          .isCloseTo(DEGREES.normalized(normalized), TOLERANCE);
        assertions.assertAll();
      };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radiansCorrectlyNormalized(final TestInfo testInfo) {
    double offset = 0.01D;
    Iterator<Double> inputGenerator =
      Arrays
        .asList(-MAX_VALUE,
                -PI * 2 - offset, -PI * 2, -PI * 2 + offset,
                -PI - offset, -PI, -PI + offset,
                -PI / 2D - offset, -PI / 2D, -PI / 2D + offset,
                -offset, 0D, Double.MIN_VALUE, offset,
                PI / 2D - offset, PI / 2D, PI / 2D + offset,
                PI - offset, PI, PI + offset,
                PI * 2 - offset, PI * 2, PI * 2 + offset,
                MAX_VALUE)
        .iterator();
    Function<Double, String> displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + RADIANS.format(input);
    ThrowingConsumer<Double> testExecutor =
      input -> {
        SoftAssertions assertions = new SoftAssertions();
        double normalized = RADIANS.normalized(input);
        String inputAsString = RADIANS.format(input);
        LOG.info("Radians: {} normalized to {}", inputAsString, RADIANS.format(normalized));
        assertions.assertThat(normalized)
          .describedAs("Normalized value of %s must be greater than or equal to -π rad",
                       inputAsString)
          .isGreaterThanOrEqualTo(-PI);
        assertions.assertThat(normalized)
          .describedAs("Normalized value of %s must be less than or equal to π rad", inputAsString)
          .isLessThanOrEqualTo(PI);
        if (input >= -PI && input <= PI) {
          assertions.assertThat(normalized)
            .describedAs("Already normalized input %s should not change too much.", inputAsString)
            .isCloseTo(input, TOLERANCE);
        }
        assertions.assertThat(normalized)
          .describedAs("Normalizing %s again should provide a similar result.", inputAsString)
          .isCloseTo(DEGREES.normalized(normalized), TOLERANCE);
        assertions.assertAll();
      };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radiansFormatDefaultsToDefaultLocale(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -PI * 4D, PI * 4D).iterator();
    Function<Double, String>
      displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + RADIANS.format(input);
    ThrowingConsumer<Double> testExecutor = (input) ->
      assertThat(RADIANS.format(input)).isEqualTo(RADIANS.format(input, getDefault(FORMAT)));
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> degreesFormatDefaultsToDefaultLocale(final TestInfo testInfo) {
    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -720D, 720D).iterator();
    Function<Double, String>
      displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + RADIANS.format(input);
    ThrowingConsumer<Double> testExecutor = (input) ->
      assertThat(DEGREES.format(input)).isEqualTo(DEGREES.format(input, getDefault(FORMAT)));
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> radiansFormattedCorrectly(final TestInfo testInfo) {
    Locale testLocale = Locale.ROOT;
    NumberFormat nf = NumberFormat.getNumberInstance(testLocale);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern("#,##0.##");

    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -PI * 4D, PI * 4D).iterator();
    Function<Double, String> displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + RADIANS.format(input);
    ThrowingConsumer<Double> testExecutor = (input) -> {
      String formatted = RADIANS.format(input, testLocale);
      String expectedNumber = df.format(input);
      // ignore possibly rounded last number
      if (round(input) != input) {
        expectedNumber = expectedNumber.substring(0, expectedNumber.length() - 1);
      }
      LOG.info("Radians: {} formatted to {}", input, formatted);
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(formatted).endsWith(RADIANS.getSymbol());
      assertions.assertThat(formatted).startsWith(expectedNumber);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> degreesFormattedCorrectly(final TestInfo testInfo) {
    Locale testLocale = Locale.ROOT;
    NumberFormat nf = NumberFormat.getNumberInstance(testLocale);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern("#,##0.##");

    Iterator<Double> inputGenerator =
      new Random(RANDOM_SEED).doubles(TEST_RUNS, -PI * 4D, PI * 4D).iterator();
    Function<Double, String> displayNameGenerator =
      (input) -> TEST_NAME.apply(testInfo) + ", input: " + DEGREES.format(input);
    ThrowingConsumer<Double> testExecutor = (input) -> {
      String formatted = DEGREES.format(input, testLocale);
      String expectedNumber = df.format(input);
      // ignore possibly rounded last number
      if (round(input) != input) {
        expectedNumber = expectedNumber.substring(0, expectedNumber.length() - 1);
      }
      LOG.info("Degrees: {} formatted to {}", input, formatted);
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(formatted).endsWith(DEGREES.getSymbol());
      assertions.assertThat(formatted).startsWith(expectedNumber);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

}
