package de.xore.utils;

/**
 * <p/>
 * Date: 08.07.2006<br>
 * Time: 20:26:59<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class StringUtils {
  private StringUtils() {
  }

  public static String wrap( String unwrapped ) {
    return createMultiLineText( unwrapped, 60, "<br>" );
  }

  public static String createMultiLineText( String text, int maxLineLength, String lineBreak ) {
    StringBuilder splitText = new StringBuilder( text.length() + 10 );
    String[] words = text.split( " " );

    int lineLength = 0;
    for ( String word : words ) {
      if ( lineLength + 1 + word.length() > maxLineLength ) {
        splitText.append( lineBreak );
        lineLength = 0;
      }
      splitText.append( word );
      splitText.append( ' ' );
      lineLength += word.length() + 1;
    }
    return splitText.toString();
  }

  /**
   * splits the text into multiple lines
   *
   * @param text          (in one line)
   * @param maxLineLength the maximal length of the resulting line
   * @return the string with "\n" between the words
   */
  public static String createMultiLineText( String text, int maxLineLength ) {
    return createMultiLineText( text, maxLineLength, "\n" );
  }
}
