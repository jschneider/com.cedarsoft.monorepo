package com.cedarsoft.photos.tools.imagemagick;

import com.cedarsoft.image.Resolution;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ConvertTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  private File imageFile;
  private Convert convert;

  @Before
  public void setUp() throws Exception {
    URL resource = getClass().getResource("/img1.jpg");
    URI uri = resource.toURI();
    imageFile = new File(uri);
    assertThat(imageFile).exists();
    convert = new Convert();
  }

  @Test
  public void basic() throws Exception {
    File thumbFile = new File(tmp.newFolder(), "thumb.jpg");

    convert.createThumbnail(imageFile, thumbFile, new Resolution(800, 800), "jpg");
    assertThat(thumbFile).exists();
  }
}
