/**
 * 
 */
package de.hu.logic.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
public class DirectedGraphAdaptor implements Structure 
{
	GralogGraphSupport _graph;
	HashSet<String> _signature = new HashSet<String>(1);
	
	public DirectedGraphAdaptor(GralogGraphSupport graph)
	{
		_graph = graph;
		_signature.add("E");		// Undirected graphs only have the edge relation E
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
		if(!rel.equals("E"))
			throw new Exception("Relation >"+rel+"< unknown.");
		if(elems.size()<2)
			throw new Exception("Not enough arguments to evaluate relation.");
		return _graph.getGraph().containsEdge(elems.get(0), elems.get(1));	
	}
}
