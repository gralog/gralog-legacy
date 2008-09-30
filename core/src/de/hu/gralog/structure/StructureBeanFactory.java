/*
 * Created on 2008 by Sebastian Ordyniak
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
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
package de.hu.gralog.structure;

/**
 * This interface defines a Structure-Bean-Factory for GrALoG. Every GrALoG-Structure
 * that wants to define it's own Structure-Bean has to implement this Factory in
 * order to provide this Structure-Beans via 
 * {@link StructureTypeInfo#getStructureBeanFactory()} to this structure.
 * 
 * <h1>Plugin-Developers</h1>
 * 
 * Before you implement your own {@link StructureBeanFactory} you first have to
 * define your Structure-Bean. Like vertices and edges a StructureBean is basically a
 * JavaBean which can have all or none of the following extra functionalities
 * ( please refer to {@link de.hu.gralog.beans} for an introduction to JavaBeans ):
 * 
 * <ul>
 * <li>It can implement
 * {@link de.hu.gralog.beans.event.PropertyChangeListenable} in order to inform
 * the GrALoG-Structure of changes to it's properties - so that GrALoG can provide
 * Undo-Functionality. The easiest way to do this is to extend either
 * {@link de.hu.gralog.beans.support.DefaultPropertyChangeListenableBean} or
 * {@link de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean},
 * where the second option addionally provides functionality to force GrALoG to
 * redraw parts of the structure - this is explained below. </li>
 * <li> It can implement
 * {@link de.hu.gralog.beans.event.DisplayChangeListenable} in order to force
 * GrALoG to redraw parts of or even the whole structure. This has to be used when
 * changing the properties of the Structure-Bean affects the look of it's vertices and or
 * edges. The easiest way to implement
 * {@link de.hu.gralog.beans.event.DisplayChangeListenable} is to subclass
 * {@link de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean},
 * which also provides functionality to signal
 * {@link java.beans.PropertyChangeEvent} to the structure as explained above. </li>
 * <li> It can implement {@link de.hu.gralog.beans.support.StructureBean},
 * which allows the Structure-Bean to have access to the GrALoG-Structure that it is
 * belongingn to and can use this to change it's behaviour ( See
 * {@link de.hu.gralog.beans.support.StructureBean} for details). </li>
 * </ul>
 * 
 * So depending on your needs you can either implement one, two or all three of
 * these features. We found that you normally need to implement all three of
 * these features which can best be accomplished by extending
 * {@link de.hu.gralog.beans.support.DefaultPropertyAndDisplayChangeListenableBean}
 * and implementing {@link de.hu.gralog.beans.support.StructureBean}. Such a
 * bean has full control over the GrALoG-Struture, it's vertices and edges and can
 * force GrALoG at any time to update the display of this structure.
 * 
 * You can now start to implement your bean by providing properties, an optional
 * Customizer and / or PropertyDescriptors respectively PersistenceDelegates to
 * your bean. This is done by using the standard mechanism for JavaBeans. If you
 * are not familar with JavaBeans please have a look at {@link de.hu.gralog.beans}.
 * 
 * @author Sebastian
 * 
 * @param <GB>
 */

public interface StructureBeanFactory<GB> {
	public GB createBean();
}
