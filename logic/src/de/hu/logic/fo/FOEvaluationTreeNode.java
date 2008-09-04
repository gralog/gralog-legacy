/**
 * 
 */
package de.hu.logic.fo;

import java.util.List;

import de.hu.gralog.app.UserException;

/**
 * @author kreutzer
 *
 */
public interface FOEvaluationTreeNode 
{
	/**
	 * 
	 * @return Result of the evaluation
	 */
	Relation getResult()  throws UserException;
	
	/** 
	 * 
	 * @return name of the current node
	 */
	String getName();
	
	/** 
	 * 
	 * @return returns a list containing EvaluationTreeNodes for the children.
	 */
	List<FOEvaluationTreeNode> getChildren()  throws UserException;
	
	int getChildrenCount()  throws UserException;
	
	
}
