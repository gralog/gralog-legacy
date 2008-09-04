/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Gralog.
 *
 * Gralog is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Gralog is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Gralog; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */
package de.hu.gralog.app;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.hu.gralog.algorithm.InvalidPropertyValuesException;

/**
 * This Exception is used by Gralog to display errors to the user. It provides a
 * message and a description or a cause (not both of them) that is displayed in
 * gralog. If a description is provided gralog displays this description to the
 * user, if a cause is provided the user is shown the stacktrace of that cause.
 * 
 * @author Sebastian
 * 
 */
public class UserException extends Exception {

	private String description;

	public UserException(String message) {
		this(message, (String) null);
	}

	public UserException(String message, String description) {
		super(message);
		this.description = description;
	}

	public UserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public String getDescription() {
		return description;
	}

	public Component getComponent() {

		if (getCause() instanceof InvalidPropertyValuesException)
			return new JScrollPane(
					((InvalidPropertyValuesException) getCause())
							.getComponent());

		String text = "no description";
		if (getCause() != null) {
			StringWriter stackTrace = new StringWriter();
			getCause().printStackTrace(new PrintWriter(stackTrace));
			text = stackTrace.toString();
		} else {
			if (getDescription() != null)
				text = getDescription();
		}
		JTextArea textArea = new JTextArea(text);
		textArea.setRows(4);
		textArea.setEditable(false);
		textArea.setBackground(new JLabel().getBackground());
		textArea.setForeground(new JLabel().getForeground());

		return new JScrollPane(textArea);
	}
}
