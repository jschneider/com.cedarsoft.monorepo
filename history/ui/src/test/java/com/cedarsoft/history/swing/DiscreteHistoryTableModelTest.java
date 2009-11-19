package com.cedarsoft.history.swing;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import com.cedarsoft.history.ContinuousEntriesInformation;
import com.cedarsoft.history.DefaultContinuousEntry;
import com.cedarsoft.utils.FrameUtils;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.lang.Override;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class DiscreteHistoryTableModelTest {
  private ContinuousEntriesInformation<DefaultContinuousEntry> entriesInformation;

  private LocalDate begin;
  private LocalDate end;

  @BeforeMethod
  protected void setUp() throws Exception {
    begin = new LocalDate( 2007, 1, 1 );
    end = new LocalDate( 2007, 12, 31 );
    entriesInformation = new ContinuousEntriesInformation<DefaultContinuousEntry>( begin, end );

    entriesInformation.addEntry( new DefaultContinuousEntry( begin ) );
    entriesInformation.addEntry( new DefaultContinuousEntry( begin.plusMonths( 2 ) ) );
    entriesInformation.addEntry( new DefaultContinuousEntry( begin.plusMonths( 4 ) ) );
  }

  @Test
  public void testDummy() {
    assertNotNull( entriesInformation );
  }

  private void showIt() throws InterruptedException, InvocationTargetException {
    //    final SortedList<? extends DefaultContinuousEntry> contractsSortable = SortedList.create( entriesInformation.getElements() );
    final SortedList<DefaultContinuousEntry> contractsSortable = new SortedList<DefaultContinuousEntry>( GlazedLists.eventList( entriesInformation.getEntries() ) );
    DefaultContinuousEntryFormat format = new DefaultContinuousEntryFormat( entriesInformation );

    EventTableModel<DefaultContinuousEntry> tableModel = new EventTableModel<DefaultContinuousEntry>( contractsSortable, format );
    JTable table = new JTable( tableModel );

    JPanel contentPane = new JPanel( new BorderLayout() );
    contentPane.add( new JScrollPane( table ) );
    FrameUtils.showFrame( contentPane );
    Thread.sleep( 100000 );
  }

  public static class DefaultContinuousEntryFormat implements TableFormat<DefaultContinuousEntry> {
    private final String[] columnNames = new String[]{"start", "end"};
    private final ContinuousEntriesInformation<DefaultContinuousEntry> entriesInformation;

    public DefaultContinuousEntryFormat( ContinuousEntriesInformation<DefaultContinuousEntry> entriesInformation ) {
      this.entriesInformation = entriesInformation;
    }

    @Override
    public int getColumnCount() {
      return columnNames.length;
    }

    @Override
    public String getColumnName( int column ) {
      return columnNames[column];
    }

    @Override
    public Object getColumnValue( @NotNull DefaultContinuousEntry baseObject, int column ) {
      switch ( column ) {
        case 0:
          return baseObject.getBegin();
        case 1:
          return entriesInformation.getEnd( baseObject );
      }
      throw new IllegalArgumentException( "invalid column " + column );
    }

  }

  public static void main( String[] args ) throws Exception {
    DiscreteHistoryTableModelTest test = new DiscreteHistoryTableModelTest();
    test.setUp();
    test.showIt();
  }
}
