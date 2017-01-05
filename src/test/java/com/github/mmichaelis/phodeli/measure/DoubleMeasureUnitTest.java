package com.github.mmichaelis.phodeli.measure;

import static com.github.mmichaelis.phodeli.test.LocaleHelpers.usingDefaultLocale;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.mmichaelis.phodeli.test.RestoreState;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Tests {@link DoubleMeasureUnit}.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
class DoubleMeasureUnitTest {

  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private DoubleMeasureUnit<DoubleMeasureUnit> measureUnit;

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    Mockito.doReturn("#,##0.#####''").when(measureUnit).getDecimalFormatPattern();
  }

  @Test
  void formatByDefaultLocaleWorksAsExpected() {
    try (RestoreState ignored = usingDefaultLocale(Locale.ROOT)) {
      String result = measureUnit.format(1.23456789D);
      assertThat(result).isEqualTo("1.23457'");
    }
  }

  @Test
  void formatByGivenLocaleWorksAsExpected() {
    String result = measureUnit.format(1.23456789D, Locale.ROOT);
    assertThat(result).isEqualTo("1.23457'");
  }

}
