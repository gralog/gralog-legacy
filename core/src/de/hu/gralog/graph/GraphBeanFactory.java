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
package de.hu.gralog.graph;

/**
 * This interface defines a GraphBeanFactory for Gralog. Every Gralog-Graph-Type
 * that wants to define its own GraphBean has to implement this Factory in
 * order to provide GraphBeans via {@link GralogGraphTypeInfo} to Gralog-Graphs.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Before you implement your own {@link GraphBeanFactory} you first have to
 * define your GraphBean. Like vertices and edges a GraphBean is basically a
 * JavaBean which can have all or none of the following extra functionalities
 * ( please refer to {@link de.hu.gralog.beans} for an introduction to JavaBeans ):
 * 
 * <ul>
 * <li>It can implement
 * {@link de.hu.gralog.beans.event.PropertyChangeListenable} in order to inform
 * the Gralog-Graph of changes to its properties - so that Gralog can provide
 * Undo-Functionality. The easiest way to do this is to extends either
 * {@link de.hu.gralog.beans.support.DefaultPropertyChangeListenableBean} or
 * {@link de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean},
 * where the second option addionaly provides functionality to force Gralog to
 * redraw parts of the graphs - this is explained below. </li>
 * <li> It can implement
 * {@link de.hu.gralog.beans.event.DisplayChangeListenable} in order to force
 * Gralog to redraw parts of or even the whole graph. This has to be used when
 * changing the properties of GraphBean affects the look of its vertices and or
 * edges. The easiest way to implement
 * {@link de.hu.gralog.beans.event.DisplayChangeListenable} is to subclass
 * {@link de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean},
 * which also provides functionality to signal
 * {@link java.beans.PropertyChangeEvent} to the graph as explained above. </li>
 * <li> It can implement {@link de.hu.gralog.beans.support.GralogGraphBean},
 * that allows the GraphBean to have access to the Gralog-Graph that it is
 * contained in and can use this to change its behaviour ( See
 * {@link de.hu.gralog.beans.support.GralogGraphBean} for details). </li>
 * </ul>
 * 
 * So depending on your needs you can either implement one, two or all three of
 * this features. We found that you normally need to implement all three of
 * these features which can best be accomplished by extending
 * {@link de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean}
 * and implementing {@link de.hu.gralog.beans.support.GralogGraphBean}. Such a
 * bean has full control of the Gralog-Graph its vertices and edges and can
 * force the graph at any time to update its display.
 * 
 * You can now start to implement your Bean by providing properties, an optional
 * Customizer and / or PropertyDescriptors respectively PersistenceDelegates to
 * your Bean. This is done using the standard mechanism for JavaBeans. If you
 * are not familar with JavaBeans please have a look at {@link de.hu.gralog.beans}.
 * 
 * @author Sebastian
 * 
 * @param <GB>
 */

public interface GraphBeanFactory<GB> {
	public GB createBean();
}
