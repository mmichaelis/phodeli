package com.github.mmichaelis.phodeli.test;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A contract for tests driven by {@link org.junit.jupiter.api.TestFactory}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public interface SpecificationContract {

  /**
   * Provides a description of the test case.
   *
   * @return description
   */
  @NotNull
  @Contract(pure = true)
  String describe();

  /**
   * Runs the assertions.
   */
  void perform();

}
