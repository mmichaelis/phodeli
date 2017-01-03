package com.github.mmichaelis.phodeli.geo;

import static com.github.mmichaelis.phodeli.geo.Angle.angle;
import static com.github.mmichaelis.phodeli.geo.Angle.degrees;
import static com.github.mmichaelis.phodeli.geo.Angle.radians;
import static com.github.mmichaelis.phodeli.geo.AngleUnit.DEGREES;
import static com.github.mmichaelis.phodeli.geo.AngleUnit.RADIANS;
import static com.github.mmichaelis.phodeli.test.LocaleHelpers.usingDefaultLocale;
import static com.github.mmichaelis.phodeli.test.SerializableCondition.serializable;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.toRadians;
import static java.util.Arrays.stream;
import static java.util.Collections.sort;
import static java.util.Comparator.comparingDouble;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.testing.EqualsTester;

import com.github.mmichaelis.phodeli.test.RestoreState;
import com.github.mmichaelis.phodeli.test.SpecificationContract;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

/**
 * Tests {@link Angle}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class AngleTest {

  private static final Offset<Double> TOLERANCE = Offset.offset(0.0001D);
  /**
   * Using fixed seed to provide reproducible test runs.
   */
  private static final long RANDOM_SEED = 0L;

  @Test
  void angleIsSerializable() {
    Angle degrees = degrees(273D);
    Angle radians = radians(0.273D);
    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(degrees).is(serializable());
    assertions.assertThat(radians).is(serializable());
    assertions.assertAll();
  }

  @TestFactory
  @NotNull
  Stream<@NotNull DynamicTest> canTransformToOtherUnit() {
    AngleUnit[] units = AngleUnit.values();
    Iterator<? extends SpecificationContract> inputGenerator =
      stream(units)
        .map(sourceUnit ->
               stream(units)
                 .map(targetUnit -> new AngleTransformContract(sourceUnit, targetUnit))
                 .collect(toList()))
        .flatMap(Collection::stream)
        .iterator();
    Function<SpecificationContract, String> displayNameGenerator =
      SpecificationContract::describe;
    ThrowingConsumer<SpecificationContract> testExecutor =
      SpecificationContract::perform;
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @Test
  void canCreateInstanceOfUnitDegrees() {
    double amount = 273D;
    Angle degrees = degrees(amount);
    assertThat(degrees.toDegrees()).isCloseTo(amount, TOLERANCE);
  }

  @Test
  void canCreateInstanceOfUnitRadians() {
    double amount = 0.273D;
    Angle radians = radians(amount);
    assertThat(radians.toRadians()).isCloseTo(amount, TOLERANCE);
  }

  @Test
  void canConvertDegreesToRadians() {
    double amountDegrees = 180D;
    double amountRadians = toRadians(amountDegrees);
    Angle degrees = degrees(amountDegrees);
    assertThat(degrees.toRadians()).isCloseTo(amountRadians, TOLERANCE);
  }

  @Test
  void canConvertRadiansToDegrees() {
    double amountDegrees = 180D;
    double amountRadians = toRadians(amountDegrees);
    Angle radians = radians(amountRadians);
    assertThat(radians.toDegrees()).isCloseTo(amountDegrees, TOLERANCE);
  }

  @Test
  void canConvertToDifferentUnitsOnTheFly() {
    double amountDegrees = 180D;
    double amountRadians = toRadians(amountDegrees);
    SoftAssertions assertions = new SoftAssertions();

    Angle radians = radians(amountRadians);
    assertions.assertThat(radians.get(RADIANS)).isCloseTo(amountRadians, TOLERANCE);
    assertions.assertThat(radians.get(DEGREES)).isCloseTo(amountDegrees, TOLERANCE);
    assertions.assertAll();
  }

  @Test
  void canNormalizeDegrees() {
    double baseAmount = 180D;
    double amount = baseAmount + 720D;
    Angle angle = degrees(amount);
    assertThat(angle.normalized().toDegrees()).isCloseTo(baseAmount, TOLERANCE);
  }

  @Test
  void canNormalizeRadians() {
    double baseAmount = PI;
    double amount = baseAmount + PI * 4D;
    Angle angle = radians(amount);
    assertThat(angle.normalized().toRadians()).isCloseTo(baseAmount, TOLERANCE);
  }

  @Test
  void angleProvidesAvailableUnits() {
    List<AngleUnit> units = Angle.getUnits();
    assertThat(units).containsExactlyInAnyOrder(DEGREES, RADIANS);
  }

  @Test
  void canCreateStringRepresentationOfRadiansWithDefaultLocale() {
    double amount = PI / 2D;
    Angle angle = radians(amount);
    try (RestoreState ignored = usingDefaultLocale(ROOT)) {
      assertThat(angle.format()).isEqualTo("1.5708 rad");
    }
  }

  @Test
  void canCreateStringRepresentationOfDegreesWithDefaultLocale() {
    double amount = 90.123D;
    Angle angle = degrees(amount);
    try (RestoreState ignored = usingDefaultLocale(ROOT)) {
      assertThat(angle.format()).isEqualTo("90.123°");
    }
  }

  @Test
  void canCreateStringRepresentationOfRadiansWithGivenLocale() {
    double amount = PI / 2D;
    Angle angle = radians(amount);
    assertThat(angle.format(ROOT)).isEqualTo("1.5708 rad");
  }

  @Test
  void canCreateStringRepresentationOfDegreesWithGivenLocale() {
    double amount = 90.123D;
    Angle angle = degrees(amount);
    assertThat(angle.format(ROOT)).isEqualTo("90.123°");
  }

  @Test
  void canSortAnglesOfSameUnitDegrees() {
    List<Angle> angles = new Random(RANDOM_SEED)
      .doubles(20L, -720D, 720D)
      .boxed()
      .map(Angle::degrees)
      .collect(toList());
    sort(angles);
    assertThat(angles).isSortedAccordingTo(comparingDouble(Angle::toDegrees));
  }

  @Test
  void canSortAnglesOfSameUnitRadians() {
    List<Angle> angles = new Random(RANDOM_SEED)
      .doubles(20L, -PI * 4D, PI * 4D)
      .boxed()
      .map(Angle::radians)
      .collect(toList());
    sort(angles);
    assertThat(angles).isSortedAccordingTo(comparingDouble(Angle::toRadians));
  }

  @Test
  void canSortAnglesOfDifferentUnits() {
    List<Angle> angles = new Random(RANDOM_SEED)
      .doubles(20L, -PI * 2D, PI * 2D)
      .boxed()
      .map(input -> {
        if (abs(input) > PI) {
          return degrees(input);
        }
        return radians(input);
      })
      .collect(toList());
    sort(angles);
    assertThat(angles).isSortedAccordingTo(comparingDouble(Angle::toRadians));
  }

  @Test
  void fulfillsEqualsHashCodeContract() {
    new EqualsTester()
      .addEqualityGroup(Angle.degrees(180D), Angle.degrees(180D))
      .addEqualityGroup(Angle.radians(180D), Angle.radians(180D))
      .addEqualityGroup(Angle.degrees(PI), Angle.degrees(PI))
      .addEqualityGroup(Angle.radians(PI), Angle.radians(PI))
      .testEquals();
  }

  private static final class AngleTransformContract implements SpecificationContract {

    @NotNull
    private final AngleUnit sourceUnit;
    @NotNull
    private final AngleUnit targetUnit;

    private AngleTransformContract(@NotNull final AngleUnit sourceUnit, @NotNull final AngleUnit targetUnit) {
      this.sourceUnit = sourceUnit;
      this.targetUnit = targetUnit;
    }

    @Override
    public @NotNull String describe() {
      return "Transforming " + sourceUnit + " to " + targetUnit;
    }

    @Override
    public void perform() {
      double sourceAmount = 1D;
      Angle sourceAngle = angle(sourceAmount, sourceUnit);
      Angle targetAngle = sourceAngle.transform(targetUnit);
      SoftAssertions assertions = new SoftAssertions();
      if (sourceUnit == targetUnit) {
        assertions.assertThat(targetAngle).isSameAs(sourceAngle);
      } else {
        assertions.assertThat(targetAngle).isNotEqualTo(sourceAngle);
        assertions.assertThat(targetAngle.get(sourceUnit))
          .isCloseTo(sourceAmount, TOLERANCE);
      }
      assertions.assertAll();
    }
  }
}
