package com.github.mmichaelis.phodeli.test;

import java.lang.reflect.Method;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.TestInfo;

/**
 * Provides the test name for test factories.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public class TestName implements Function<TestInfo, String> {

  @Override
  @NotNull
  public String apply(@NotNull final TestInfo testInfo) {
    return testInfo.getTestMethod().map(Method::getName).orElse("unknown");
  }
}
