/**
 * 
 */
package de.hu.logic.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.logic.fo.Structure;

/**
 * Created on 2006 by Stephan Kreutzer
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

/**
 * @author Stephan Kreutzer 
 *
 */
public class TransitionSystemAdaptor<V extends TransitionSystemVertex, E extends TransitionSystemEdge, GB extends TransitionSystem, G extends ListenableDirectedGraph<V,E>> implements Structure 
{
	GralogGraphSupport<V ,E, GB, G> _graph;
	HashSet<String> _signature = new HashSet<String>(1);
	
	public TransitionSystemAdaptor( GralogGraphSupport<V,E,GB,G> graph)
	{
		_graph = graph;
		_signature = new HashSet<String>(_graph.getGraphBean().getSignature());	
	}

	public Set getUniverse() {
		return _graph.getGraph().vertexSet();
	}

	public Set<String> getSignature() 
	{
		return _signature;
	}
	
	public boolean contains(String rel, ArrayList<Object> elems) throws Exception
	{
		if(rel.equals("E"))		// E is the edge relation of the transition system.
		{
			if(elems.size()!=2)
				throw new Exception("Invalid number " + elems.size() + " of arguments for relation 'E'.");
			return _graph.getGraph().containsEdge( (V)elems.get(0), (V)elems.get(1));	
			
		}
		else
		{
			if(_graph.getGraphBean().getSignature().contains(rel))
			{
				Proposition prop = _graph.getGraphBean().getProposition(rel);
				if(elems.size()!=1)
					throw new Exception("Invalid number " + elems.size() + " of arguments for relation " + rel + ".");
				
				return prop.getVertices().contains(elems.get(0));
			}
		}
		throw new Exception("Relation '"+rel+"' unknown.");
//		return _graph.containsEdge((TransitionSystemVertex) elems.get(0), (TransitionSystemVertex) elems.get(1));	
	}
}
