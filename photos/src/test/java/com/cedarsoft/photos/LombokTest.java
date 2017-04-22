package com.cedarsoft.photos;

import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.junit.*;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LombokTest {
  @Test
  public void run() throws Exception {
    Address address = new Address("max mustermann");
    assertThat(address.name()).isEqualTo("max mustermann");
  }

  @Test
  public void equals() throws Exception {
    assertThat(new Address("asdf")).isEqualTo(new Address("asdf"));
  }

  @Test
  public void testToString() throws Exception {
    assertThat(new Address("asdf").toString()).isEqualTo("LombokTest.Address(name=asdf)");
  }

  @Test(expected = IllegalAccessException.class)
  public void sneak() throws Exception {
    sneaky();
  }

  @SneakyThrows
  public void sneaky() {
    throw new IllegalAccessException();
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  @ToString
  @EqualsAndHashCode
  public static class Address {
    @Getter
    @Nonnull
    private final String name;

    public Address(@Nonnull String name) {
      this.name = name;
    }

    public void doit() throws IOException {
      @Cleanup ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      System.out.println("asdfasdf");
    }
  }
}
