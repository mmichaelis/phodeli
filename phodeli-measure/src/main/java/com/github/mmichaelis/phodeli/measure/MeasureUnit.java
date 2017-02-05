package com.github.mmichaelis.phodeli.measure;

import java.util.regex.Pattern;

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

  Pattern SYMBOL_REQUIRES_SEPARATOR_PATTERN = Pattern.compile("^[\\w].*");
  String SYMBOL_SEPARATOR = " ";

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
   * <p>
   * Returns the symbol as postfix, possibly prefixed by a separator char.
   * The default implementation determines if the symbol is an alphabetic
   * character, which results in {@link #SYMBOL_SEPARATOR} to be prepended
   * before the symbol. Otherwise the symbol is returned unmodified.
   * </p>
   * <p>
   * For frequent use it is recommended that implementations caches the result of this method
   * or provides an own implementation.
   * </p>
   *
   * @return symbol, possibly prefixed by separator
   */
  @NotNull
  @Contract(pure = true)
  default String getSymbolPostfix() {
    String symbol = getSymbol();
    if (SYMBOL_REQUIRES_SEPARATOR_PATTERN.matcher(symbol).matches()) {
      return SYMBOL_SEPARATOR + symbol;
    }
    return symbol;
  }

}
