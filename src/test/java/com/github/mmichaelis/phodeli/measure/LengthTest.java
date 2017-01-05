package com.github.mmichaelis.phodeli.measure;

import static com.github.mmichaelis.phodeli.measure.Length.cm;
import static com.github.mmichaelis.phodeli.measure.Length.dm;
import static com.github.mmichaelis.phodeli.measure.Length.inch;
import static com.github.mmichaelis.phodeli.measure.Length.km;
import static com.github.mmichaelis.phodeli.measure.Length.length;
import static com.github.mmichaelis.phodeli.measure.Length.m;
import static com.github.mmichaelis.phodeli.measure.Length.mi;
import static com.github.mmichaelis.phodeli.measure.Length.mm;
import static com.github.mmichaelis.phodeli.measure.Length.yd;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.CENTIMETERS;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.DECIMETERS;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.INCHES;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.KILOMETERS;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.METERS;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.MILES;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.MILLIMETERS;
import static com.github.mmichaelis.phodeli.measure.LengthUnit.YARDS;
import static com.github.mmichaelis.phodeli.test.LocaleHelpers.usingDefaultLocale;
import static com.github.mmichaelis.phodeli.test.SerializableCondition.serializable;
import static java.util.Arrays.stream;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.primitives.Doubles;
import com.google.common.testing.EqualsTester;

import com.github.mmichaelis.phodeli.test.RestoreState;
import com.github.mmichaelis.phodeli.test.SpecificationContract;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

/**
 * Tests {@link Length}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class LengthTest {

  private static final Offset<Double> TOLERANCE = Offset.offset(0.0001D);

  private static final Map<@NotNull LengthUnit, @NotNull Function<@NotNull Double, @NotNull Length>>
    UNIT_TO_CREATE_METHOD
    = Collections
    .unmodifiableMap(Stream.of(
      createMethodEntry(LengthUnit.MILLIMETERS, Length::mm),
      createMethodEntry(LengthUnit.CENTIMETERS, Length::cm),
      createMethodEntry(LengthUnit.INCHES, Length::inch),
      createMethodEntry(LengthUnit.DECIMETERS, Length::dm),
      createMethodEntry(YARDS, Length::yd),
      createMethodEntry(LengthUnit.METERS, Length::m),
      createMethodEntry(LengthUnit.KILOMETERS, Length::km),
      createMethodEntry(LengthUnit.MILES, Length::mi))
                       .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

  private static final Map<@NotNull LengthUnit, @NotNull Function<@NotNull Length, @NotNull Double>>
    UNIT_TO_CONVERT_METHOD
    = Collections
    .unmodifiableMap(Stream.of(
      convertMethodEntry(LengthUnit.MILLIMETERS, Length::toMillimeters),
      convertMethodEntry(LengthUnit.CENTIMETERS, Length::toCentimeters),
      convertMethodEntry(LengthUnit.INCHES, Length::toInches),
      convertMethodEntry(LengthUnit.DECIMETERS, Length::toDecimeters),
      convertMethodEntry(YARDS, Length::toYards),
      convertMethodEntry(LengthUnit.METERS, Length::toMeters),
      convertMethodEntry(LengthUnit.KILOMETERS, Length::toKilometers),
      convertMethodEntry(LengthUnit.MILES, Length::toMiles))
                       .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

  @NotNull
  private static SimpleEntry<@NotNull LengthUnit, @NotNull Function<Double, Length>> createMethodEntry(
    @NotNull final LengthUnit lengthUnit, @NotNull final Function<Double, Length> lengthFunction) {
    return new SimpleEntry<>(lengthUnit, lengthFunction);
  }

  @NotNull
  private static SimpleEntry<@NotNull LengthUnit, @NotNull Function<Length, Double>> convertMethodEntry(
    @NotNull final LengthUnit lengthUnit, @NotNull final Function<Length, Double> lengthFunction) {
    return new SimpleEntry<>(lengthUnit, lengthFunction);
  }

  @Test
  void lengthIsSerializable() {
    Length degrees = mm(1D);
    Length radians = cm(1D);
    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(degrees).is(serializable());
    assertions.assertThat(radians).is(serializable());
    assertions.assertAll();
  }

  @TestFactory
  Stream<@NotNull DynamicTest> staticMethodExistForEveryLengthUnit() {
    Set<@NotNull LengthUnit> keys = UNIT_TO_CREATE_METHOD.keySet();
    Iterator<@NotNull LengthUnit> inputGenerator = stream(LengthUnit.values()).iterator();
    return DynamicTest.stream(inputGenerator, Enum::name, lengthUnit -> {
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(lengthUnit).isIn(keys);
      assertions.assertThat(
        UNIT_TO_CREATE_METHOD.get(lengthUnit).apply(1D).get(lengthUnit)).isEqualTo(1D);
      assertions.assertAll();
    });
  }

  @TestFactory
  Stream<@NotNull DynamicTest> convertWorksAsExpected() {
    LengthUnit[] units = LengthUnit.values();
    Iterator<? extends SpecificationContract> inputGenerator =
      stream(units)
        .map(sourceUnit ->
               stream(units)
                 .map(targetUnit -> new LengthConvertContract(sourceUnit, targetUnit))
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
  void fulfillsEqualsHashCodeContract() {
    new EqualsTester()
      .addEqualityGroup(mm(1D), mm(1D))
      .addEqualityGroup(mm(2D), mm(2D))
      .addEqualityGroup(cm(1D), cm(1D))
      .addEqualityGroup(cm(2D), cm(2D))
      .addEqualityGroup(dm(1D), dm(1D))
      .addEqualityGroup(dm(2D), dm(2D))
      .addEqualityGroup(m(1D), m(1D))
      .addEqualityGroup(m(2D), m(2D))
      .addEqualityGroup(km(1D), km(1D))
      .addEqualityGroup(km(2D), km(2D))
      .addEqualityGroup(yd(1D), yd(1D))
      .addEqualityGroup(yd(2D), yd(2D))
      .addEqualityGroup(mi(1D), mi(1D))
      .addEqualityGroup(mi(2D), mi(2D))
      .addEqualityGroup(inch(1D), inch(1D))
      .addEqualityGroup(inch(2D), inch(2D))
      .testEquals();
  }

  @Test
  void formatByDefaultLocaleWorksAsExpected() {
    // Only probing one given format as other formatting is tested in LengthUnit
    Length length = length(1.23456789D, YARDS);
    String result;
    try (RestoreState ignored = usingDefaultLocale(ROOT)) {
      result = length.format();
    }
    assertThat(result).isEqualTo("1.23457 yd");
  }

  @Test
  void formatByGivenLocaleWorksAsExpected() {
    // Only probing one given format as other formatting is tested in LengthUnit
    Length length = length(1.23456789D, YARDS);
    assertThat(length.format(ROOT)).isEqualTo("1.23457 yd");
  }

  @Test
  void comparisonWorksAcrossLengthUnits() {
    List<@NotNull Length> lengths = Arrays.asList(
      mm(7D).transform(MILES),
      mm(6D).transform(KILOMETERS),
      mm(5D).transform(METERS),
      mm(4D).transform(DECIMETERS),
      mm(3D).transform(INCHES),
      mm(2D).transform(CENTIMETERS),
      mm(1D)
    );
    Collections.sort(lengths);
    assertThat(lengths).isSortedAccordingTo(
      (o1, o2) -> Doubles.compare(o1.get(MILLIMETERS), o2.get(MILLIMETERS))
    );
  }

  @TestFactory
  @NotNull
  Stream<@NotNull DynamicTest> canTransformToOtherUnit() {
    LengthUnit[] units = LengthUnit.values();
    Iterator<? extends SpecificationContract> inputGenerator =
      stream(units)
        .map(sourceUnit ->
               stream(units)
                 .map(targetUnit -> new LengthTransformContract(sourceUnit, targetUnit))
                 .collect(toList()))
        .flatMap(Collection::stream)
        .iterator();
    Function<SpecificationContract, String> displayNameGenerator =
      SpecificationContract::describe;
    ThrowingConsumer<SpecificationContract> testExecutor =
      SpecificationContract::perform;
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  private static final class LengthConvertContract implements SpecificationContract {

    @NotNull
    private final LengthUnit sourceUnit;
    @NotNull
    private final LengthUnit targetUnit;

    private LengthConvertContract(@NotNull final LengthUnit sourceUnit,
                                  @NotNull final LengthUnit targetUnit) {
      this.sourceUnit = sourceUnit;
      this.targetUnit = targetUnit;
    }

    @Override
    public @NotNull String describe() {
      return "Converting " + sourceUnit + " to " + targetUnit + " and vice versa";
    }

    @Override
    public void perform() {
      double lengthToConvert = 1D;
      Length sourceLength = UNIT_TO_CREATE_METHOD.get(sourceUnit)
        .apply(lengthToConvert);
      double convertedLength = UNIT_TO_CONVERT_METHOD.get(targetUnit)
        .apply(sourceLength);
      Length targetLength = UNIT_TO_CREATE_METHOD.get(targetUnit)
        .apply(convertedLength);
      double reconvertedLength = UNIT_TO_CONVERT_METHOD.get(sourceUnit)
        .apply(targetLength);
      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(lengthToConvert).isCloseTo(reconvertedLength, TOLERANCE);
      if (sourceUnit == targetUnit) {
        assertions.assertThat(lengthToConvert).isCloseTo(convertedLength, TOLERANCE);
      } else {
        assertions.assertThat(convertedLength)
          .isNotCloseTo(lengthToConvert, TOLERANCE);
      }
      assertions.assertAll();
    }
  }

  private static final class LengthTransformContract implements SpecificationContract {

    @NotNull
    private final LengthUnit sourceUnit;
    @NotNull
    private final LengthUnit targetUnit;

    private LengthTransformContract(@NotNull final LengthUnit sourceUnit,
                                    @NotNull final LengthUnit targetUnit) {
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
      Length sourceLength = length(sourceAmount, sourceUnit);
      Length targetLength = sourceLength.transform(targetUnit);
      SoftAssertions assertions = new SoftAssertions();
      if (sourceUnit == targetUnit) {
        assertions.assertThat(targetLength).isSameAs(sourceLength);
      } else {
        assertions.assertThat(targetLength).isNotEqualTo(sourceLength);
        assertions.assertThat(targetLength.get(sourceUnit))
          .isCloseTo(sourceAmount, TOLERANCE);
      }
      assertions.assertAll();
    }
  }
}
