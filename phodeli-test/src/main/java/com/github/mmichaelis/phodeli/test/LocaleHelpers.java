package com.github.mmichaelis.phodeli.test;

import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jetbrains.annotations.NotNull;

/**
 * Helper methods for testing locale specific parts.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public final class LocaleHelpers {

  private LocaleHelpers() {
  }

  @NotNull
  public static RestoreState usingDefaultLocale(final Locale locale) {
    return new UsingDefaultLocale(locale);
  }

  private static final class UsingDefaultLocale implements RestoreState {

    private static final Lock LOCK = new ReentrantLock();
    @NotNull
    private final Locale defaultLocale;

    private UsingDefaultLocale(@NotNull final Locale locale) {
      LOCK.lock();
      defaultLocale = Locale.getDefault();
      Locale.setDefault(locale);
    }

    @Override
    public void close() {
      Locale.setDefault(defaultLocale);
      LOCK.lock();
    }
  }
}
