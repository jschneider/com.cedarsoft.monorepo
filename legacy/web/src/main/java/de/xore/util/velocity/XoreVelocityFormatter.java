package de.xore.util.velocity;

import de.xore.util.SessionContext;
import org.apache.velocity.app.tools.VelocityFormatter;
import org.apache.velocity.context.Context;
import org.jetbrains.annotations.NonNls;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 11.05.2004
 * Time: 16:19:10
 *
 */

/**
 * <p/>
 * Date: 11.05.2004<br> Time: 16:19:10<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreVelocityFormatter extends VelocityFormatter {
  private HttpServletResponse response;
  private SessionContext sessionContext;
  @NonNls
  private SimpleDateFormat monthYearFormatter = new SimpleDateFormat( "MMMM yyyy" );

  public XoreVelocityFormatter( Context ctx, HttpServletRequest request, HttpServletResponse response, SessionContext sessionContext ) {
    super( ctx );
    this.response = response;
    this.sessionContext = sessionContext;
  }

  public String formatLink( String url ) {
    return encodeUrl( url );
  }

  public String encodeUrl( String url ) {
    return response.encodeURL( sessionContext.getRoot() + '/' + url );
  }

  public String formatNumber( long bytes ) {
    return NumberFormat.getNumberInstance().format( bytes );
  }

  public String formatMonthYear( Calendar calendar ) {
    return monthYearFormatter.format( calendar.getTime() );
  }
}
