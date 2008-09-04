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

package de.hu.logic.parser;

import java.io.FileNotFoundException;
import java.io.StringBufferInputStream;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import de.hu.gralog.app.UserException;

/**
 * This class implements a few static methods used to load formulas from a file or standard in. It's 
 * mainly a wrapper for Parser functions.
 * @author Stephan Kreutzer
 *
 */
public class FOLogicParser 
{
	/**
	 * Loads a formula from a file. 
	 * @param filename The name of the file to be read.
	 * @return A FormulaList object with the formulas contained in the file.
	 * @throws ParseException Exception thrown if the file contains a syntax error. Use descr() function to get the decription of the exception and type() function to get an integer value describing the cause of the error.
	 * @throws FileNotFoundException Thrown if the file can't be read by the system.
	 * @throws Exception This are exception thrown by the parser indicating that the input file contains syntax errors.
	 */
	public static FOFormulaList loadFile(String filename) throws ParseException, FileNotFoundException, Exception 
	{
		SymbolFactory sf = new ComplexSymbolFactory();
		FOScanner scan = new de.hu.logic.parser.FOScanner(new java.io.FileInputStream(filename), sf);
		FOParser p = new FOParser(scan, sf);;
		Symbol s=null;
		try
		{
			s = p.parse();
		}
		catch(Exception e)
		{
			if(p.hasError())
				throw new UserException(p.getErrorMsg());
			else
				throw new UserException(e.getMessage());
		}
		return (FOFormulaList) s.value;
	}
	
	public static FOFormulaList loadString(String formula) throws ParseException, FileNotFoundException, Exception 
	{
		SymbolFactory sf = new ComplexSymbolFactory();
		
		FOScanner scan = new de.hu.logic.parser.FOScanner(new StringBufferInputStream(formula), sf);
		FOParser p = new FOParser(scan, sf);;
		Symbol s=null;
		try
		{
			s = p.parse();
		}
		catch(Exception e)
		{
			if(p.hasError())
				throw new UserException(p.getErrorMsg());
			else
				throw new UserException(e.getMessage());
		}
		return (FOFormulaList) s.value;
/*		FOParser p = new FOParser(scan, sf);
		Symbol s = p.parse();
		return (FOFormulaList) s.value;*/
	}
}
