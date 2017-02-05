package com.github.mmichaelis.phodeli.measure;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Measure unit for measures represented by double values.
 *
 * @param <T> this measure unit type
 * @author Mark Michaelis
 * @since 1.0.0
 */
public interface DoubleMeasureUnit<T extends DoubleMeasureUnit<T>> extends MeasureUnit {

  /**
   * Self-referential resolution.
   *
   * @return self-reference
   * @see <a href="http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206">AngelikaLanger.com
   * - Java Generics FAQs - Programming With Java Generics - Angelika Langer
   * Training/Consulting</a>
   * @since 1.0.0
   */
  @NotNull
  @Contract(pure = true)
  T getThis();

  /**
   * Converts the given measure in the given unit to this unit.
   *
   * @param sourceAmount the measure amount in the given {@code sourceUnit}
   * @param sourceUnit   the unit of the {@code sourceAmount} argument
   * @return the converted measure in this unit
   * @since 1.0.0
   */
  @Contract(pure = true)
  double convert(double sourceAmount, @NotNull T sourceUnit);
}
