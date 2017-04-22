package com.cedarsoft.photos;

import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;
import com.cedarsoft.crypt.HashCalculator;
import com.google.common.io.Files;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImageStorageTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();
  private ImageStorage imageStorage;
  private Hash hash;

  @Before
  public void setUp() throws Exception {
    imageStorage = new ImageStorage(tmp.newFolder(), tmp.newFolder());
    hash = HashCalculator.calculate(ImageStorage.ALGORITHM, "thecontent");
    assertThat(hash.getValueAsHex()).isEqualTo("8ba871f31f3c8ad7d74591859e60f42fe89852ceb407fcd13f32433d37b751db");
  }

  @Test
  public void delete() throws Exception {
    assertThat(imageStorage.getDir(hash)).doesNotExist();
    assertThat(imageStorage.getDeletedDataFile(hash)).doesNotExist();

    File file = imageStorage.getDataFile(hash);
    assertThat(imageStorage.getDir(hash)).doesNotExist();


    //Create the directory
    ImageStorage.ensureDirectoryExists(imageStorage.getDir(hash));
    assertThat(imageStorage.getDir(hash)).exists();

    //Write the file
    Files.write("daContent", file, StandardCharsets.UTF_8);

    imageStorage.delete(hash);
    assertThat(imageStorage.getDir(hash)).doesNotExist();
    assertThat(imageStorage.getDataFile(hash)).doesNotExist();

    assertThat(imageStorage.getDeletedDataFile(hash)).exists();
    assertThat(Files.readFirstLine(imageStorage.getDeletedDataFile(hash), StandardCharsets.UTF_8)).isEqualTo("daContent");
  }

  @Test
  public void basic() throws Exception {
    File file = imageStorage.getDir(hash);

    assertThat(file.getParentFile()).hasName("8b");
    assertThat(file).hasName("8ba871f31f3c8ad7d74591859e60f42fe89852ceb407fcd13f32433d37b751db");
  }

  @Test
  public void dataFile() throws Exception {
    File file = imageStorage.getDataFile(hash);

    assertThat(file.getParentFile().getParentFile()).hasName("8b");
    assertThat(file.getParentFile()).hasName("8ba871f31f3c8ad7d74591859e60f42fe89852ceb407fcd13f32433d37b751db");
    assertThat(file).hasName("data");
  }
}