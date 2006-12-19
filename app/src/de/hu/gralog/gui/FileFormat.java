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

package de.hu.gralog.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFormat {
	
	private final String description;
	private final String extension;
	private FileFilter fileFilter;
	
	public FileFormat( String description, String extension ) {
		this.description = description;
		this.extension = extension;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public boolean acceptsFile( File file ) {
		return file.isFile() && file.getName().toLowerCase().endsWith( "." + getExtension() );
	}
	
	public FileFilter getFileFilter() {
		if ( fileFilter == null ) 
			fileFilter = new FileFilter() {
				public boolean accept( File file ) {
					if ( file.isDirectory() )
						return true;
					if ( acceptsFile( file ) )
						return true;
					return false;
				}
			
				public String getDescription() {
					return FileFormat.this.getDescription();
				}
			};
		return fileFilter;
	}
}
