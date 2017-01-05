package com.github.mmichaelis.phodeli.measure;

import static java.lang.String.format;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The unit of a measure.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface MeasureUnit {

  /**
   * Return the symbol for the unit.
   *
   * @return symbol
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  String getSymbol();

  /**
   * Returns the decimal format pattern to be used for measures of this type.
   *
   * @return pattern
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  default String getDecimalFormatPattern() {
    String rawSymbol = getSymbol();
    String symbol = rawSymbol
      .replaceAll("(')", "'$1")
      .replaceAll("([0#.,E;%\\u2030\\u00A4-])", "'$1'");
    String separator;
    if (rawSymbol.trim().isEmpty() || rawSymbol.matches("^\\P{Alnum}.*")) {
      separator = "";
    } else {
      separator = " ";
    }
    return format("#,##0.#####%s%s", separator, symbol);
  }
}
