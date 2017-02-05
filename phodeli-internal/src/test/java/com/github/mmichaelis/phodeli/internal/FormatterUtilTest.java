package com.github.mmichaelis.phodeli.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Formattable;
import java.util.Formatter;
import java.util.Locale;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FormatterUtil}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
@DisplayName("Tests for com.github.mmichaelis.phodeli.internal.FormatterUtil")
class FormatterUtilTest {

  private static final class MockFormattable implements Formattable {

    @NotNull
    private final Supplier<Double> amountSupplier;
    @NotNull
    private final Supplier<String> symbolPostfixSupplier;

    private MockFormattable(@NotNull final Supplier<Double> amountSupplier,
                            @NotNull final Supplier<String> symbolPostfixSupplier) {
      this.amountSupplier = amountSupplier;
      this.symbolPostfixSupplier = symbolPostfixSupplier;
    }

    private MockFormattable(final double amount, @NotNull final String symbolPostfix) {
      this(() -> amount, () -> symbolPostfix);
    }

    @Override
    public void formatTo(final Formatter formatter, final int flags, final int width,
                         final int precision) {
      FormatterUtil
        .formatMeasureTo(formatter, amountSupplier.get(), symbolPostfixSupplier.get(), flags, width,
                         precision);
    }
  }

  /**
   * Tests through Java-API to access formattables.
   */
  @Nested
  @DisplayName("Integration Tests")
  class IntegrationTest {

    private static final String SYMBOL_POSTFIX = "°";

    @Test
    void defaultUseCaseWorks() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%s<", mock);
      assertThat(result).isEqualTo(">1.234568°<");
    }

    @Test
    void simpleRightAdjustmentWorks() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%15s<", mock);
      assertThat(result).isEqualTo(">      1.234568°<");
    }

    @Test
    void simpleLeftAdjustmentWorks() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%-15s<", mock);
      assertThat(result).isEqualTo(">1.234568°      <");
    }

    @Test
    void precisionIsRespected() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%.4s<", mock);
      assertThat(result).isEqualTo(">1.2°<");
    }

    @Test
    void decimalSeparatorIgnoredIfDecimalDigitsDoNotFit() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%.3s<", mock);
      assertThat(result).isEqualTo(">1°<");
    }

    @Test
    void resultIsTruncatedForSmallPrecision() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%.1s<", mock);
      assertThat(result).isEqualTo(">1<");
    }

    @Test
    void highPrecisionAddsMoreDecimalDigitsThanDefault() {
      MockFormattable mock = new MockFormattable(1.23456789D, SYMBOL_POSTFIX);
      String result = String.format(Locale.ROOT, ">%.20s<", mock);
      assertThat(result).isEqualTo(">1.23456789000000000°<");
    }
  }
}
