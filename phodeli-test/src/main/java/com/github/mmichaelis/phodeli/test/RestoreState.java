package com.github.mmichaelis.phodeli.test;

/**
 * Interface for an auto closeable which restores a specific state on
 * close.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface RestoreState extends AutoCloseable {

  @Override
  void close();
}
