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

import de.hu.games.graph.alg.Algorithm;

public class RandomGraphGeneratorAlgorithm extends Algorithm {

	public AlgorithmResultOld execute(  ) {
		
//		graph.removeAllVertices( graph.vertexSet() );
//		
//		new RandomGraphGenerator( CONFIGURATION.getSize(), CONFIGURATION.getEdgePropability() ).generateGraph( graph, new DirectedGraphVertexFactory( graph ), null );
//		
//		JGraphLayoutAlgorithm layout = new SugiyamaLayoutAlgorithm();
//		SugiyamaLayoutSettings settings = (SugiyamaLayoutSettings)layout.createSettings();
//		settings.setFlushToOrigin( true );
//		settings.setVerticalDirection( false );
//		settings.setVerticalSpacing( "50" );
//		settings.setIndention( "200" );
//		settings.apply();
//		
//		try {
//			JGraphLayoutAlgorithm.applyLayout( MainPad.getInstance().getDesktop().getCurrentGraph(), layout, DefaultGraphModel.getAll( MainPad.getInstance().getDesktop().getCurrentGraph().getModel()) ) ;
//		} catch( Throwable e ) {
////			MainPad.getInstance().handleUserException( new UserException( "cannot apply Sugiyamalayout", e ) );
//			layout = new CircleGraphLayout();
//			JGraphLayoutAlgorithm.applyLayout( MainPad.getInstance().getDesktop().getCurrentGraph(), layout, DefaultGraphModel.getAll( MainPad.getInstance().getDesktop().getCurrentGraph().getModel()) ) ;
//		}


		return null;
	}

	public static class Configuration {
		private int size;
		private double edgePropability;
		
		public Configuration( int size, double edgePropability ) {
			this.size = size;
			this.edgePropability = edgePropability;
		}

		public double getEdgePropability() {
			return edgePropability;
		}

		public void setEdgePropability(double edgePropability) {
			this.edgePropability = edgePropability;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
	}

}
