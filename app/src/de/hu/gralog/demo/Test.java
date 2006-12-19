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

package de.hu.gralog.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;


public class Test {

	public static final String IDENTIFIER = "TEST";

	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JarFile jarFile = new JarFile( "/vol/fob-vol5/mi00/ordyniak/workspace/games/dist/games-test.jar" );
		
		Enumeration entries = jarFile.entries();
		while ( entries.hasMoreElements() )
			System.out.println( "entry: " + entries.nextElement() );
		
		InputStream in = jarFile.getInputStream( jarFile.getJarEntry( "ToDo" ) );
		byte[] buffer = new byte[1024];
		while ( in.read( buffer ) != -1 ) {
			System.out.println( new String( buffer ) );
		}
		
	}
	
	

}
