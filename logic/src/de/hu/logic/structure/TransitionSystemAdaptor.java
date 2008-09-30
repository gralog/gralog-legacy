/**
 * 
 */
package de.hu.logic.structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.structure.Structure;
import de.hu.logic.fo.LogicStructure;

/**
 * Created on 2006 by Stephan Kreutzer
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of GrALoG.
 *
 * GrALoG is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * GrALoG is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GrALoG; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

/**
 * @author Stephan Kreutzer 
 *
 */
public class TransitionSystemAdaptor<V extends TransitionSystemVertex, E extends TransitionSystemEdge, GB extends TransitionSystem, G extends ListenableDirectedGraph<V,E>> implements LogicStructure 
{
	Structure<V ,E, GB, G> _structure;
	HashSet<String> _signature = new HashSet<String>(1);
	
	public TransitionSystemAdaptor( Structure<V,E,GB,G> graph)
	{
		_structure = graph;
		_signature = new HashSet<String>(_structure.getStructureBean().getSignature());	
	}

	public Set getUniverse() {
		return _structure.getGraph().vertexSet();
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
			return _structure.getGraph().containsEdge( (V)elems.get(0), (V)elems.get(1));	
			
		}
		else
		{
			if(_structure.getStructureBean().getSignature().contains(rel))
			{
				Proposition prop = _structure.getStructureBean().getProposition(rel);
				if(elems.size()!=1)
					throw new Exception("Invalid number " + elems.size() + " of arguments for relation " + rel + ".");
				
				return prop.getVertices().contains(elems.get(0));
			}
		}
		throw new Exception("Relation '"+rel+"' unknown.");
//		return _graph.containsEdge((TransitionSystemVertex) elems.get(0), (TransitionSystemVertex) elems.get(1));	
	}
}
