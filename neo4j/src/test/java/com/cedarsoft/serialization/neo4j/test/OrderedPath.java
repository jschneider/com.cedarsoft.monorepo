package com.cedarsoft.serialization.neo4j.test;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

import java.util.ArrayList;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class OrderedPath {
  private static final RelationshipType REL1 = withName( "REL1" ), REL2 = withName( "REL2" ), REL3 = withName( "REL3" );
  static final String DB_PATH = "target/neo4j-orderedpath-db";
  GraphDatabaseService db;

  public OrderedPath( GraphDatabaseService db ) {
    this.db = db;
  }

  public static void main( String[] args ) {
    GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
    OrderedPath op = new OrderedPath( db );
    Node A = op.createTheGraph();
    TraversalDescription traversalDescription = op.findPaths();
    System.out.println( op.printPaths( traversalDescription, A ) );
    op.shutdownGraph();
  }

  public Node createTheGraph() {
    Transaction tx = db.beginTx();
    // START SNIPPET: createGraph
    Node A = db.createNode();
    Node B = db.createNode();
    Node C = db.createNode();
    Node D = db.createNode();

    A.createRelationshipTo( C, REL2 );
    C.createRelationshipTo( D, REL3 );
    A.createRelationshipTo( B, REL1 );
    B.createRelationshipTo( C, REL2 );
    // END SNIPPET: createGraph
    A.setProperty( "name", "A" );
    B.setProperty( "name", "B" );
    C.setProperty( "name", "C" );
    D.setProperty( "name", "D" );
    tx.success();
    tx.finish();
    return A;
  }

  public void shutdownGraph() {
    try {
      if ( db != null ) db.shutdown();
    } finally {
      db = null;
    }
  }

  public TraversalDescription findPaths() {
    // START SNIPPET: walkOrderedPath
    final ArrayList<RelationshipType> orderedPathContext = new ArrayList<RelationshipType>();
    orderedPathContext.add( REL1 );
    orderedPathContext.add( withName( "REL2" ) );
    orderedPathContext.add( withName( "REL3" ) );
    TraversalDescription td = Traversal.description()
      .evaluator( new Evaluator() {
        @Override
        public Evaluation evaluate( final Path path ) {
          if ( path.length() == 0 ) {
            return Evaluation.EXCLUDE_AND_CONTINUE;
          }
          RelationshipType expectedType = orderedPathContext.get( path.length() - 1 );
          boolean isExpectedType = path.lastRelationship()
            .isType( expectedType );
          boolean included = path.length() == orderedPathContext.size() && isExpectedType;
          boolean continued = path.length() < orderedPathContext.size() && isExpectedType;
          return Evaluation.of( included, continued );
        }
      } )
      .uniqueness( Uniqueness.NONE );
    // END SNIPPET: walkOrderedPath
    return td;
  }

  String printPaths( TraversalDescription td, Node A ) {
    try ( Transaction tx = db.beginTx() ) {
      String output = "";
      // START SNIPPET: printPath
      Traverser traverser = td.traverse( A );
      PathPrinter pathPrinter = new PathPrinter( "name" );
      for ( Path path : traverser ) {
        output += Traversal.pathToString( path, pathPrinter );
      }
      // END SNIPPET: printPath
      output += "\n";
      return output;
    }
  }

  // START SNIPPET: pathPrinter
  static class PathPrinter implements Traversal.PathDescriptor<Path> {
    private final String nodePropertyKey;

    public PathPrinter( String nodePropertyKey ) {
      this.nodePropertyKey = nodePropertyKey;
    }

    @Override
    public String nodeRepresentation( Path path, Node node ) {
      return "(" + node.getProperty( nodePropertyKey, "" ) + ")";
    }

    @Override
    public String relationshipRepresentation( Path path, Node from, Relationship relationship ) {
      String prefix = "--", suffix = "--";
      if ( from.equals( relationship.getEndNode() ) ) {
        prefix = "<--";
      } else {
        suffix = "-->";
      }
      return prefix + "[" + relationship.getType().name() + "]" + suffix;
    }
  }
  // END SNIPPET: pathPrinter
}