package de.xore.util.persistence.hibernate;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <p/>
 * Date: 24.06.2006<br>
 * Time: 14:17:47<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
@Entity()
public class UserMock {
  private String id;

  @Id
  @GeneratedValue( generator = "system-uuid" )
  @GenericGenerator( name = "system-uuid", strategy = "uuid" )
  public String getId() {
    return id;
  }

  public void setId( String id ) {
    this.id = id;
  }
}
