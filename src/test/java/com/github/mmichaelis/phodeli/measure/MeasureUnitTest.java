package com.github.mmichaelis.phodeli.measure;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests {@link MeasureUnit}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class MeasureUnitTest {

  private static final double PROBE_AMOUNT = 1.23456789;
  private static final String EXPECTED_PROBE_AMOUNT_STRING = "1.23457";
  private static final Locale PROBE_LOCALE = Locale.ROOT;
  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private MeasureUnit measureUnit;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @TestFactory
  Stream<DynamicTest> decimalFormatPatternProvidesCorrectOutput() {
    return DynamicTest.stream(asList(
      "'",
      " 0",
      "#",
      ".",
      "-",
      ",",
      " E",
      "%",
      "\u2030",
      "\u00A4",
      " km",
      "°",
      "\"",
      " 2",
      " m²",
      "",
      "€",
      " EUR"
      ).iterator(),
                              input -> "Expected postfix: '" + input + "'",
                              input -> {
                                when(measureUnit.getSymbol()).thenReturn(input.trim());
                                String result = format();
                                assertThat(result).isEqualTo(EXPECTED_PROBE_AMOUNT_STRING + input);
                              });
  }

  private String format() {
    NumberFormat nf = NumberFormat.getNumberInstance(PROBE_LOCALE);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern(measureUnit.getDecimalFormatPattern());
    return df.format(PROBE_AMOUNT);
  }
}
