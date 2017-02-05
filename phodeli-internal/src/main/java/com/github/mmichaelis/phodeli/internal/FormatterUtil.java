package com.github.mmichaelis.phodeli.internal;

import static java.lang.String.format;
import static java.util.FormattableFlags.LEFT_JUSTIFY;
import static java.util.Locale.ROOT;

import java.util.FormattableFlags;
import java.util.Formatter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities for formatted output based on {@link java.util.Formattable}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public final class FormatterUtil {

  /**
   * Length of the decimal separator. As it is a char, it is always 1.
   */
  private static final int DECIMAL_SEPARATOR_LENGTH = 1;
  /**
   * Length of the minus sign. As it is a char, it is always 1.
   */
  private static final int MINUS_SIGN_LENGTH = 1;
  private static final String DEFAULT_EMPTY_TRUNCATED_RESULT = "";
  private static final String UNLIMITED_WIDTH_PATTERN = "%1$f%2$s";
  private static final String RESTRICTED_WIDTH_PATTERN_PATTERN = "%%1$.%df%%2$s";
  private static final String LEFT_JUSTIFY_PATTERN = "%%-%ds";
  private static final String RIGHT_JUSTIFY_PATTERN = "%%%ds";

  private FormatterUtil() {
  }

  /**
   * <p>
   * Helps to implement {@link java.util.Formattable#formatTo(Formatter, int, int, int)} for
   * measures with a symbol at the end. Mind that the given symbol postfix requires to include
   * a possibly wanted separator between amount and symbol.
   * </p>
   *
   * @param formatter     formatter to use
   * @param amount        the amount to output
   * @param symbolPostfix the symbol, possibly prefixed by a separator
   * @param flags         flags as specified by {@link FormattableFlags}
   * @param width         minimum width; result will be filled with additional spaces if minimum
   *                      width is
   *                      not reached; negative for no minimum width
   * @param precision     maximum width; result might be truncated if the maximum width is less
   *                      then
   *                      the minimum required space. Precision also influences indirectly how many
   *                      decimal digits will follow as any remaining space will be used for the
   *                      decimal digits. Negative for no limit which will result in using the
   *                      default
   */
  public static void formatMeasureTo(@NotNull final Formatter formatter, final double amount,
                                     @NotNull final String symbolPostfix,
                                     final int flags,
                                     final int width,
                                     final int precision) {
    String amountPrefix = format(formatter.locale(), "%.0f", amount);
    String symbol = adjustedSymbol(symbolPostfix, formatter, flags);
    int minusSignLength = amount > 0D ? 0 : MINUS_SIGN_LENGTH;
    int minimumLength = symbol.length()
                        + amountPrefix.length()
                        + DECIMAL_SEPARATOR_LENGTH
                        + minusSignLength;
    FormatSettings formatSettings;
    if (precision < 0) {
      formatSettings = new FormatSettings();
    } else if (minimumLength >= precision) {
      // Even the minimum does not fit.
      StringBuilder builder = new StringBuilder()
        .append(amountPrefix)
        .append(symbol);
      if (precision < builder.length()) {
        builder.delete(precision, minimumLength);
      }
      formatSettings = new FormatSettings("%3$s", builder.toString());
    } else {
      int remainingDecimalDigits = precision - minimumLength;
      formatSettings = new FormatSettings(
        format(ROOT, RESTRICTED_WIDTH_PATTERN_PATTERN,
               remainingDecimalDigits));
    }
    if (width < 0) {
      formatUnlimitedWidth(amount, symbol, formatter, formatSettings);
    } else {
      formatLimitedWidth(amount, symbol, formatter, flags, width, formatSettings);
    }
  }

  @NotNull
  private static String adjustedSymbol(@NotNull final String symbolPostfix,
                                       @NotNull final Formatter formatter,
                                       final int flags) {
    String symbol = symbolPostfix;
    if (isUppercase(flags)) {
      symbol = symbol.toUpperCase(formatter.locale());
    }
    return symbol;
  }

  private static void formatUnlimitedWidth(final double amount,
                                           final String symbol,
                                           @NotNull final Formatter formatter,
                                           @NotNull final FormatSettings formatSettings) {
    String formatPattern = formatSettings.getFormatPattern();
    String truncatedResult = formatSettings.getTruncatedResult();
    formatter.format(formatPattern,
                     amount, // $1
                     symbol, // $2
                     truncatedResult // $3
    );
  }

  private static void formatLimitedWidth(final double amount,
                                         final String symbol,
                                         @NotNull final Formatter formatter,
                                         final int flags,
                                         final int width,
                                         @NotNull final FormatSettings formatSettings) {
    String formatPattern = formatSettings.getFormatPattern();
    String truncatedResult = formatSettings.getTruncatedResult();
    String representation = format(formatter.locale(),
                                   formatPattern,
                                   amount, // $1
                                   symbol, // $2
                                   truncatedResult // $3
    );
    String padPatternPattern;
    if (isLeftJustified(flags)) {
      padPatternPattern = LEFT_JUSTIFY_PATTERN;
    } else {
      padPatternPattern = RIGHT_JUSTIFY_PATTERN;
    }
    formatter.format(format(ROOT, padPatternPattern, width), representation);
  }

  @Contract(pure = true)
  private static boolean isLeftJustified(final int flags) {
    return (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY;
  }

  @Contract(pure = true)
  private static boolean isUppercase(final int flags) {
    return (flags & FormattableFlags.UPPERCASE) == FormattableFlags.UPPERCASE;
  }

  private static final class FormatSettings {

    @NotNull
    private final String formatPattern;
    @NotNull
    private final String truncatedResult;

    private FormatSettings() {
      this(UNLIMITED_WIDTH_PATTERN);
    }

    private FormatSettings(@NotNull final String formatPattern) {
      this(formatPattern, DEFAULT_EMPTY_TRUNCATED_RESULT);
    }

    private FormatSettings(@NotNull final String formatPattern,
                           @NotNull final String truncatedResult) {
      this.formatPattern = formatPattern;
      this.truncatedResult = truncatedResult;
    }

    @NotNull
    private String getFormatPattern() {
      return formatPattern;
    }

    @NotNull
    private String getTruncatedResult() {
      return truncatedResult;
    }
  }

}
