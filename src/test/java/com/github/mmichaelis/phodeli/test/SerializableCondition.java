package com.github.mmichaelis.phodeli.test;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.assertj.core.api.Condition;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;

/**
 * Tests serializable features of an object.
 *
 * @author Mark Michaelis
 * @since 1.0.0
 */
public final class SerializableCondition<T> extends Condition<T> {

  private static final Logger LOG = getLogger(lookup().lookupClass());

  private SerializableCondition() {
    super("serializable");
  }

  @Contract(pure = true)
  public static <T> Condition<T> serializable() {
    return new SerializableCondition<>();
  }

  @Override
  public boolean matches(final T value) {
    if (value instanceof Serializable) {
      Serializable serializable = (Serializable) value;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
        new ObjectOutputStream(out).writeObject(serializable);
      } catch (IOException e) {
        LOG.debug("Failure serializing object: {}.", serializable, e);
        return false;
      }
      try {
        Object
          deserialized =
          new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
        return value.equals(deserialized);
      } catch (IOException | ClassNotFoundException e) {
        LOG.debug("Failure deserializing object: {}.", serializable, e);
        return false;
      }
    }
    return false;
  }
}
