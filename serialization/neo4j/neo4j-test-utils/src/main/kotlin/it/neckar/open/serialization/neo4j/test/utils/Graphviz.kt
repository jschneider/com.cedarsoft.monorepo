/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package it.neckar.open.serialization.neo4j.test.utils

import org.neo4j.graphdb.GraphDatabaseService
import java.io.File
import java.io.IOException
import javax.annotation.Nonnull

/**
 *
 */
object Graphviz {
  @Throws(IOException::class, InterruptedException::class)
  fun toPng(@Nonnull graphDb: GraphDatabaseService?) {
    val targetFile = File.createTempFile("graphviz", ".png")
    val processBuilder = ProcessBuilder("dot", "-Tpng", "-o", targetFile.absolutePath)
    val process = processBuilder.start()
    throw UnsupportedOperationException()

    ////Now output the dot file
    //Walker walker = new Walker() {
    //  @Override
    //  public <R, E extends Throwable> R accept( Visitor<R, E> visitor ) throws E {
    //    for (Node node : graphDb.getAllNodes()) {
    //      visitor.visitNode( node );
    //      for ( Relationship edge : node.getRelationships( Direction.OUTGOING ) ) {
    //        visitor.visitRelationship( edge );
    //      }
    //    }
    //    return visitor.done();
    //  }
    //};
    //
    //GraphvizWriter writer = new GraphvizWriter();
    //try ( Transaction tx = graphDb.beginTx() ) {
    //  writer.emit( process.getOutputStream(), walker );
    //  tx.success();
    //}
    //
    //process.getOutputStream().close();
    //
    //int result = process.waitFor();
    //if ( result != 0 ) {
    //  byte[] errorOut = ByteStreams.toByteArray( process.getErrorStream() );
    //  throw new IllegalStateException( "Did not work: " + new String( errorOut, StandardCharsets.UTF_8 ) );
    //}
    //
    //Desktop.getDesktop().open( targetFile );
  }
}
