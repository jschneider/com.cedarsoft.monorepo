package com.cedarsoft.utils.configuration;

import com.cedarsoft.utils.configuration.xml.XmlConfigurationManager;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * <p/>
 * Date: Jul 9, 2007<br>
 * Time: 11:05:40 PM<br>
 */
@Deprecated
public class MockConfigurationManager extends XmlConfigurationManager {
  public MockConfigurationManager() {
    super( new XMLConfiguration() );
  }
}
