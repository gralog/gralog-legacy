/*
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

package de.hu.logic.general;

/**
 * Exception thrown during evaluation of formulae
 * @author Stephan Kreutzer
 *
 */
public class EvaluationException extends Exception {

	static public final long serialVersionUID = 000100;
	
	static public final int SIGNATURE_MISMATCH = 0;
	
	
	int _type;
	String _descr;
	
	public EvaluationException(int type, String descr)
	{
		super(descr);
		_type = type;
		_descr = descr;
	}
	
	public int type()
	{
		return _type;
	}
	
	public String descr()
	{ return _descr; }

	public String toString()
	{
		return _descr;
	}
}
