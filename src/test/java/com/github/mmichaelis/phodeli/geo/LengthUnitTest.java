package com.github.mmichaelis.phodeli.geo;

import static com.github.mmichaelis.phodeli.test.LocaleHelpers.usingDefaultLocale;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.mmichaelis.phodeli.test.RestoreState;
import com.github.mmichaelis.phodeli.test.SpecificationContract;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

/**
 * Tests {@link LengthUnit}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class LengthUnitTest {

  private static final Pattern VALID_FORMAT_PATTERN = Pattern
    .compile("^[\\p{Digit}\\p{Punct}]+\\p{Space}?\\P{Digit}+");

  private static Function<Double, Double> getDirectConvertFunction(
    @NotNull final LengthUnit sourceUnit,
    @NotNull final LengthUnit targetUnit) {
    Function<Double, Double> convertFunction;
    switch (targetUnit) {
      case MILLIMETERS:
        convertFunction = sourceUnit::toMillimeters;
        break;
      case CENTIMETERS:
        convertFunction = sourceUnit::toCentimeters;
        break;
      case INCHES:
        convertFunction = sourceUnit::toInches;
        break;
      case DECIMETERS:
        convertFunction = sourceUnit::toDecimeters;
        break;
      case YARDS:
        convertFunction = sourceUnit::toYards;
        break;
      case METERS:
        convertFunction = sourceUnit::toMeters;
        break;
      case KILOMETERS:
        convertFunction = sourceUnit::toKilometers;
        break;
      case MILES:
        convertFunction = sourceUnit::toMiles;
        break;
      default:
        throw new UnsupportedOperationException("Unknown target unit " + targetUnit);
    }
    return convertFunction;
  }

  @NotNull
  private static Function<Double, Double> getIndirectConvertFunction(final LengthUnit sourceUnit,
                                                                     final LengthUnit targetUnit) {
    return value -> targetUnit.convert(value, sourceUnit);
  }

  @TestFactory
  Stream<DynamicTest> convertWorksAsExpected() {
    LengthUnit[] units = LengthUnit.values();
    Iterator<? extends SpecificationContract> inputGenerator =
      stream(units)
        .map(sourceUnit ->
               stream(units)
                 .map(targetUnit -> new DoubleMeasureConversionContract<>(sourceUnit,
                                                                          targetUnit,
                                                                          getIndirectConvertFunction(
                                                                            sourceUnit, targetUnit),
                                                                          getIndirectConvertFunction(
                                                                            targetUnit, sourceUnit),
                                                                          getDirectConvertFunction(
                                                                            sourceUnit, targetUnit)
                 ))
                 .collect(toList()))
        .flatMap(Collection::stream)
        .iterator();
    Function<SpecificationContract, String> displayNameGenerator =
      SpecificationContract::describe;
    ThrowingConsumer<SpecificationContract> testExecutor =
      SpecificationContract::perform;
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> determinesCorrectMaxPrecision() {
    LengthUnit[] units = LengthUnit.values();
    Iterator<? extends SpecificationContract> inputGenerator =
      stream(units)
        .map(sourceUnit ->
               stream(units)
                 .map(targetUnit -> new MaxPrecisionContract(sourceUnit, targetUnit))
                 .collect(toList()))
        .flatMap(Collection::stream)
        .iterator();
    Function<SpecificationContract, String> displayNameGenerator =
      SpecificationContract::describe;
    ThrowingConsumer<SpecificationContract> testExecutor =
      SpecificationContract::perform;
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> providesSymbol() {
    Iterator<LengthUnit> inputGenerator = stream(LengthUnit.values()).iterator();
    Function<LengthUnit, String> nameGenerator = Enum::name;
    ThrowingConsumer<LengthUnit> testExecutor = input -> assertThat(input.getSymbol()).isNotEmpty();
    return DynamicTest.stream(inputGenerator, nameGenerator, testExecutor);
  }

  @Test
  void providesUniqueSymbol() {
    List<String> symbols = Arrays.stream(LengthUnit.values()).map(LengthUnit::getSymbol)
      .collect(Collectors.toList());
    assertThat(symbols).doesNotHaveDuplicates();
  }

  @TestFactory
  Stream<DynamicTest> providesDecimalFormatPattern() {
    Iterator<LengthUnit> inputGenerator = stream(LengthUnit.values()).iterator();
    Function<LengthUnit, String> nameGenerator = Enum::name;
    ThrowingConsumer<LengthUnit> testExecutor = input -> assertThat(input.getDecimalFormatPattern())
      .isNotEmpty();
    return DynamicTest.stream(inputGenerator, nameGenerator, testExecutor);
  }

  @Test
  void providesUniqueDecimalFormatPattern() {
    List<String> symbols = Arrays.stream(LengthUnit.values())
      .map(LengthUnit::getDecimalFormatPattern)
      .collect(Collectors.toList());
    assertThat(symbols).doesNotHaveDuplicates();
  }

  @TestFactory
  Stream<DynamicTest> formatByDefaultLocaleWorksAsExpected() {
    Iterator<LengthUnit> inputGenerator = stream(LengthUnit.values()).iterator();
    Function<LengthUnit, String> nameGenerator = Enum::name;
    ThrowingConsumer<LengthUnit> testExecutor = input -> {
      SoftAssertions assertions = new SoftAssertions();
      try (RestoreState ignored = usingDefaultLocale(Locale.ROOT)) {
        String result = input.format(1.23456D);
        assertions.assertThat(result).startsWith("1");
        assertions.assertThat(result).contains("23");
        assertions.assertThat(result).matches(VALID_FORMAT_PATTERN);
        assertions.assertAll();
      }
    };
    return DynamicTest.stream(inputGenerator, nameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> formatByGivenLocaleWorksAsExpected() {
    Iterator<LengthUnit> inputGenerator = stream(LengthUnit.values()).iterator();
    Function<LengthUnit, String> nameGenerator = Enum::name;
    ThrowingConsumer<LengthUnit> testExecutor = input -> {
      SoftAssertions assertions = new SoftAssertions();
      String result = input.format(2.12345D, Locale.ROOT);
      assertions.assertThat(result).startsWith("2");
      assertions.assertThat(result).contains("12");
      assertions.assertThat(result).matches(VALID_FORMAT_PATTERN);
      assertions.assertAll();
    };
    return DynamicTest.stream(inputGenerator, nameGenerator, testExecutor);
  }

  private static final class MaxPrecisionContract implements SpecificationContract {

    @NotNull
    private final LengthUnit unit1;
    @NotNull
    private final LengthUnit unit2;

    private MaxPrecisionContract(@NotNull final LengthUnit unit1, @NotNull final LengthUnit unit2) {
      this.unit1 = unit1;
      this.unit2 = unit2;
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String describe() {
      return "max precision contract should be fulfilled for " + unit1 + " and " + unit2;
    }

    @Override
    public void perform() {
      LengthUnit maxUnit = unit1.maxPrecision(unit2);
      LengthUnit otherMaxUnit = unit2.maxPrecision(unit1);
      double amount = 1D;
      LengthUnit minUnit;

      // Hard assertion as this is the requirement for all other assertions.
      assertThat(maxUnit).isSameAs(otherMaxUnit);

      if (unit1 == maxUnit) {
        minUnit = unit2;
      } else {
        minUnit = unit1;
      }

      if (minUnit == maxUnit) {
        // nothing more to test
        return;
      }

      SoftAssertions assertions = new SoftAssertions();
      assertions.assertThat(maxUnit.convert(amount, minUnit)).isGreaterThan(amount);
      assertions.assertThat(minUnit.convert(amount, maxUnit)).isLessThan(amount);
      assertions.assertAll();
    }
  }

}
