package com.cedarsoft.photos;

import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;
import com.google.common.collect.ImmutableList;
import org.junit.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImportReportTest {
  private static final Logger LOG = Logger.getLogger(ImportReportTest.class.getName());

  @Test
  public void log() throws Exception {
    LOG.warning("asdf");
  }

  @Test
  public void basic() throws Exception {
    ImportReport importReport = new ImportReport(ImmutableList.of(), ImmutableList.of(), ImmutableList.of());
    assertThat(importReport.getAlreadyExisting()).isEmpty();
  }

  @Test
  public void builder() throws Exception {
    ImportReport importReport = ImportReport.builder()
      .withImportedHash(new Hash(Algorithm.SHA256, "asdf".getBytes(StandardCharsets.UTF_8)))
      .withAlreadyExisting(new Hash(Algorithm.SHA256, "2".getBytes(StandardCharsets.UTF_8)))
      .withCreatedLink(new File("asdf"))
      .build();

    assertThat(importReport.getAlreadyExisting()).hasSize(1);
    assertThat(importReport.getImportedHashes()).hasSize(1);
    assertThat(importReport.getCreatedLinks()).hasSize(1);
    assertThat(importReport.getCreatedLinks()).isInstanceOf(ImmutableList.class);

    assertThat(importReport.toString()).isEqualTo("ImportReport(importedHashes=[[SHA256: 61736466]], alreadyExisting=[[SHA256: 32]], createdLinks=[asdf])");
  }
}
