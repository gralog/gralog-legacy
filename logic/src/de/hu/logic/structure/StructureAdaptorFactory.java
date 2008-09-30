/**
 * 
 */
package de.hu.logic.structure;

import org.jgrapht.DirectedGraph;
import org.jgrapht.UndirectedGraph;

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
public class StructureAdaptorFactory {

	public static LogicStructure generateAdaptor( Structure structure )
	{
		if(structure.getGraph() instanceof UndirectedGraph)
			return new UndirectedGraphAdaptor(structure);
		else if(structure.getStructureBean() instanceof TransitionSystem)
			return new TransitionSystemAdaptor(structure);
		else if(structure instanceof DirectedGraph)
			return new DirectedGraphAdaptor(structure);
		return null;
	}
}
