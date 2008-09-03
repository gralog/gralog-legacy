package de.hu.gralog.beans.support;

import org.jgrapht.ListenableGraph;

import de.hu.gralog.graph.GralogGraphSupport;

/**
 * This interface should be implemented by GraphBeans in order
 * to get access to the {@link GralogGraphSupport}-instance they
 * are belonging to.
 * 
 * @author Sebastian
 *
 * @param <V> the vertexType
 * @param <E> the edgeType
 * @param <GB> the GraphBeanType
 * @param <G> the JGraphTType, which has to be a {@link ListenableGraph<V,E>}
 */
public interface GralogGraphBean<V, E, GB extends GralogGraphBean, G extends ListenableGraph<V, E>> {
	/**
	 * This function is called by {@link GralogGraphSupport} during
	 * initialization.
	 * 
	 * @param graphSupport
	 */
	public void setGraphSupport(GralogGraphSupport<V, E, GB, G> graphSupport);
}
