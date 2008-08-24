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

package de.hu.logic.fo;

import java.util.ArrayList;
import java.util.List;

import de.hu.gralog.app.UserException;
import de.hu.logic.general.EvaluationTreeNode;
import de.hu.logic.graph.Proposition;


/**
 * @author Stephan Kreutzer 
 *
 */
public class FOTreeNode implements FOEvaluationTreeNode {

	Formula _f;
	FOEvaluation _eval;
	boolean _root=false;
	
	public FOTreeNode(Formula f, FOEvaluation eval)
	{
	   _f = f;
	   _eval = eval;
	}
	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildren()
	 */
	public List<FOEvaluationTreeNode> getChildren() throws UserException {
		// TODO Auto-generated method stub
		return new ArrayList<FOEvaluationTreeNode>();
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getChildrenCount()
	 */
	public int getChildrenCount() throws UserException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return _f.toString();
	}

	/* (non-Javadoc)
	 * @see de.hu.logic.general.EvaluationTreeNode#getResult()
	 */
	public Relation getResult() throws UserException {
		try
		{
			return _eval.evaluate(_f, new Interpretation(_eval.getStructure()));
		}
		catch(Exception e)
		{
			throw new UserException("Program error: " + e.getMessage());
		}
	}
	
	void root(boolean root)
	{
		_root = root;
	}


}
