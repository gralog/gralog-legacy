/**
 * 
 */
package de.hu.logic.general;

import java.util.List;

import de.hu.gralog.app.UserException;
import de.hu.logic.graph.Proposition;

/**
 * @author kreutzer
 *
 */
public interface EvaluationTreeNode 
{
	/**
	 * 
	 * @return Result of the evaluation
	 */
	Proposition getResult()  throws UserException;
	
	/** 
	 * 
	 * @return name of the current node
	 */
	String getName();
	
	/** 
	 * 
	 * @return returns a list containing EvaluationTreeNodes for the children.
	 */
	List<EvaluationTreeNode> getChildren()  throws UserException;
	
	int getChildrenCount()  throws UserException;
	
}
