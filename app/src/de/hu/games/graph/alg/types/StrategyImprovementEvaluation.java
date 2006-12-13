/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.games.graph.alg.types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import de.hu.games.app.UserException;
import de.hu.games.graph.alg.Algorithm;
import de.hu.games.graph.alg.AlgorithmResultOld;
import de.hu.games.jgraph.GJGraph;

public class StrategyImprovementEvaluation extends Algorithm {

	public AlgorithmResultOld execute(  ) {
//		File path = new File( CONFIGURATION.getGraphDirectory() );
//		FileWriter infoFile;
//		try {
//			infoFile = new FileWriter( CONFIGURATION.getInfoFile() );
//			infoFile.write( "<infos>\n" );
//		} catch (IOException e1) {
//			throw new RuntimeException( "cannot open infofile: " + CONFIGURATION.getInfoFile(), e1 );
//		}
//		
//		Document infoDoc = new Document();
//		Element root = new Element( "root" );
//		infoDoc.addContent( root );
//
//		int totalIt = 0;
//		for ( int size = CONFIGURATION.getStart_size(); size <= CONFIGURATION.getEnd_size(); size = size + CONFIGURATION.getInc_size() ) {
//			for ( double edgeP = CONFIGURATION.getStart_edgeP(); edgeP <= CONFIGURATION.getEnd_edgeP(); edgeP = edgeP + CONFIGURATION.getInc_edgeP() ) {
//				for ( double player0P = CONFIGURATION.getStart_player0P(); player0P <= CONFIGURATION.getEnd_player0P(); player0P = player0P + CONFIGURATION.getInc_player0P() ) {
//					for ( int it = 0; it < CONFIGURATION.getIt_per_setting(); it ++ ) {
//						totalIt++;
//						String fileName = getFileName( size, edgeP, player0P, it );
//						ParityGameGraph graph = new ParityGameGraph();
//						new RandomGraphGenerator( size, edgeP ).generateGraph( graph, new RandomParityGameVertexFactory( graph, player0P ), null );
//						
//						de.hu.games.graph.alg.StrategyImprovementAlgorithm alg = new de.hu.games.graph.alg.StrategyImprovementAlgorithm( graph );
//						Throwable error = null;
//						int maxcountit = 0;
//						for ( int itg = 0; itg < CONFIGURATION.getIt_per_graph();itg ++ ) {
//							int countit = 0;
//							try {
//								while ( alg.iterate() != null )
//									countit++;
//							} catch( Throwable t ) {
//								error = t;
//							}
//							if ( error != null )
//								break;
//							if ( countit > maxcountit )
//								maxcountit = countit;
//							alg.reset();
//						}
//						
//						if ( CONFIGURATION.isWriteGraphs() && Math.pow( size, CONFIGURATION.getWrite_graph_exponent() ) <= maxcountit ) {
//							try {
//								writeGraphToFile( path, fileName, graph );
//							} catch (UserException e) {
//								throw new RuntimeException( e );
//							}
//						} else
//							fileName = null;
//						
//						try {
//							addIterationInfo( infoFile, graph, fileName, maxcountit, error );
//							if ( totalIt % 50 == 0 )
//								infoFile.flush();
//						} catch( IOException e ) {
//							throw new RuntimeException( e );
//						}
//
//					}
//				}
//			}
//		}
//		try {
//			infoFile.write( "</infos>");
//			infoFile.close();
//		} catch( IOException e ) {
//			throw new RuntimeException( e );
//		}
///*		try {
//			writeDocumentToFile( infoDoc, CONFIGURATION.getInfoFile() );
//		} catch (UserException e) {
//			throw new RuntimeException( e );
//		}*/
		return null;
	}

	private void addIterationInfo(FileWriter infoFile, ParityGameGraph graph, String fileName, int countit, Throwable error ) throws IOException {
		String out = "<info vertexes=\"" + Integer.toString( graph.vertexSet().size() ) + "\" edges=\"" + Integer.toString( graph.edgeSet().size() ) + "\"";
		if ( error == null )
			out += " iterations=\"" + Integer.toString( countit ) + "\"";
		else
			out += " error=\"" + error.toString() + "\"";
		if ( fileName != null )
			out += " fileName=\"" + fileName + "\"";
		out += "/>\n";
		infoFile.write( out );
	}
	
	private void writeGraphToFile( File path, String fileName, ParityGameGraph graph ) throws UserException {
		new XMLIOOld().writeGraphToFile( new GJGraph( graph ), new File( path.getAbsolutePath() + "\\" + fileName ) );
	}
	
	private void writeDocumentToFile( Document doc, String fileName ) throws UserException {
		try {
			XMLOutputter xmlOut = new XMLOutputter();
			
			xmlOut.output( doc, new FileOutputStream( fileName ) );
		} catch(IOException e) {
			throw new UserException( "could not write to file: " + fileName, e);
		}

	}
	
	private String getFileName( int size, double edgeP, double player0P, int it ) {
		return Integer.toString( size ) +  "-" + Double.toString( edgeP ) + "-" + Double.toString( player0P ) + "-" + Integer.toString( it ) + ".xml";
	}
	
	public static class Configuration {
		private int start_size;
		private int end_size;
		private int inc_size;
		private double start_edgeP;
		private double end_edgeP;
		private double inc_edgeP;
		private double start_player0P;
		private double end_player0P;
		private double inc_player0P;
		
		private int it_per_graph;
		private int it_per_setting;
		
		private String infoFile;
		private String graphDirectory;
		private double write_graph_exponent;
		
		private boolean writeGraphs;
		
		public Configuration() {
			start_size = 5;
			end_size = 5;
			inc_size = 1;
			start_edgeP = 0.5;
			end_edgeP = 0.5;
			inc_edgeP = 0.1;
			start_player0P = 0.5;
			end_player0P = 0.5;
			inc_player0P = 0.1;
			
			it_per_graph = 5;
			it_per_setting = 10;
			
			infoFile = "d:\\dev\\hiwi\\evaluation\\info.xml";
			graphDirectory = "d:\\dev\\hiwi\\evaluation\\graphs";
			writeGraphs = true;
			write_graph_exponent = 0;
		}

		public boolean isWriteGraphs() {
			return writeGraphs;
		}

		public void setWriteGraphs(boolean writeGraphs) {
			this.writeGraphs = writeGraphs;
		}

		public double getEnd_edgeP() {
			return end_edgeP;
		}

		public void setEnd_edgeP(double end_edgeP) {
			this.end_edgeP = end_edgeP;
		}

		public double getEnd_player0P() {
			return end_player0P;
		}

		public void setEnd_player0P(double end_player0P) {
			this.end_player0P = end_player0P;
		}

		public int getEnd_size() {
			return end_size;
		}

		public void setEnd_size(int end_size) {
			this.end_size = end_size;
		}

		public String getGraphDirectory() {
			return graphDirectory;
		}

		public void setGraphDirectory(String graphDirectory) {
			this.graphDirectory = graphDirectory;
		}

		public double getInc_edgeP() {
			return inc_edgeP;
		}

		public void setInc_edgeP(double inc_edgeP) {
			this.inc_edgeP = inc_edgeP;
		}

		public double getInc_player0P() {
			return inc_player0P;
		}

		public void setInc_player0P(double inc_player0P) {
			this.inc_player0P = inc_player0P;
		}

		public int getInc_size() {
			return inc_size;
		}

		public void setInc_size(int inc_size) {
			this.inc_size = inc_size;
		}

		public String getInfoFile() {
			return infoFile;
		}

		public void setInfoFile(String infoFile) {
			this.infoFile = infoFile;
		}

		public int getIt_per_graph() {
			return it_per_graph;
		}

		public void setIt_per_graph(int it_per_graph) {
			this.it_per_graph = it_per_graph;
		}

		public int getIt_per_setting() {
			return it_per_setting;
		}

		public void setIt_per_setting(int it_per_setting) {
			this.it_per_setting = it_per_setting;
		}

		public double getStart_edgeP() {
			return start_edgeP;
		}

		public void setStart_edgeP(double start_edgeP) {
			this.start_edgeP = start_edgeP;
		}

		public double getStart_player0P() {
			return start_player0P;
		}

		public void setStart_player0P(double start_player0P) {
			this.start_player0P = start_player0P;
		}

		public int getStart_size() {
			return start_size;
		}

		public void setStart_size(int start_size) {
			this.start_size = start_size;
		}

		public double getWrite_graph_exponent() {
			return write_graph_exponent;
		}

		public void setWrite_graph_exponent(double write_graph_exponent) {
			this.write_graph_exponent = write_graph_exponent;
		}
		
		
	}
}
