package com.cedarsoft.io;

import com.google.common.io.Files;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FileOutputStreamWithMoveTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void basics() throws Exception {
    File targetOut = new File(tmp.newFolder(), "asdf");
    assertThat(targetOut).doesNotExist();

    FileOutputStreamWithMove out = new FileOutputStreamWithMove(targetOut);
    assertThat(targetOut).doesNotExist();

    out.write("asdf".getBytes(StandardCharsets.UTF_8));
    assertThat(targetOut).doesNotExist();

    out.close();
    assertThat(targetOut).exists();

    out.close();
    out.close();
    out.close();
    out.close();
  }

  @Test
  public void exception() throws Exception {
    File targetOut = new File(tmp.newFolder(), "asdf");

    Files.write("daContent".getBytes(StandardCharsets.UTF_8), targetOut);
    assertThat(targetOut).exists();

    FileOutputStreamWithMove out = new FileOutputStreamWithMove(targetOut);
    out.write("asdf".getBytes(StandardCharsets.UTF_8));

    assertThat(targetOut).exists();
    //old content
    assertThat(Files.toString(targetOut, StandardCharsets.UTF_8)).isEqualTo("daContent");

    out.close();

    assertThat(targetOut).exists();
    assertThat(Files.toString(targetOut, StandardCharsets.UTF_8)).isEqualTo("asdf");
  }
}
