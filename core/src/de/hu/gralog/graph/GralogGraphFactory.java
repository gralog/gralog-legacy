package de.hu.gralog.graph;

import org.jgrapht.ListenableGraph;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.support.GralogGraphTypeInfoSupport;

/**
 * This class acts as a factory to construct Gralog-Graphs ({@link GralogGraphSupport}.
 * 
 * @author Sebastian
 *
 */
public class GralogGraphFactory {

	/*
	 * constructs a {@link GralogGraphTypeInfoSupport} that can be seen as an Adapter
	 * to {@link GralogGraphTypeInfo}
	 * 
	 */
	private static <V, E, GB, G extends ListenableGraph<V, E>> GralogGraphTypeInfoSupport<V, E, GB, G> createTypeInfoSupport(
			GralogGraphTypeInfo<V, E, GB, G> typeInfo, G graph, GB bean) {
		return new GralogGraphTypeInfoSupport<V, E, GB, G>(typeInfo, graph,
				bean);
	}

	/**
	 * This function creates a Gralog-Graph ({@link GralogGraphSupport} from
	 * a {@link GralogGraphTypeInfo}-object. 
	 * 
	 * @param <V> the vertexType
	 * @param <E> the edgeType
	 * @param <GB> the GraphBeanType
	 * @param <G> the JGraphT-Graph that has to implement {@link org.jgrapht.graph.ListenableGraph}
	 * @param typeInfo the {@link GralogGraphTypeInfo}-instance that defines all properties needed
	 * to construct the Gralog-Graph
	 * @return the Gralog-Graph
	 * @throws UserException
	 */
	public static <V, E, GB, G extends ListenableGraph<V, E>> GralogGraphSupport createGraphSupport(
			GralogGraphTypeInfo<V, E, GB, G> typeInfo) throws UserException {
		return createGraphSupport(typeInfo, null);
	}

	/**
	 * This function creates a Gralog-Graph ({@link GralogGraphSupport} from
	 * a {@link GralogGraphTypeInfo}-object and a given JGraphT-Graph.  
	 * 
	 * @param <V> the vertexType
	 * @param <E> the edgeType
	 * @param <GB> the GraphBeanType
	 * @param <G> the JGraphT-Graph that has to implement {@link org.jgrapht.graph.ListenableGraph}
	 * @param typeInfo the {@link GralogGraphTypeInfo}-instance that defines all properties needed
	 * to construct the Gralog-Graph
	 * @param graph the JGraphT-Graph that is used to initialize the Gralog-Graph
	 * @return the Gralog-Graph
	 * @throws UserException
	 */
	public static <V, E, GB, G extends ListenableGraph<V, E>> GralogGraphSupport<V, E, GB, G> createGraphSupport(
			GralogGraphTypeInfo<V, E, GB, G> typeInfo, G graph)
			throws UserException {
		return createGraphSupport(typeInfo, graph, null);
	}

	/**
	 * This function creates a Gralog-Graph ({@link GralogGraphSupport} from
	 * a {@link GralogGraphTypeInfo}-object, a given JGraphT-Graph and a GraphBean.  
	 * 
	 * @param <V> the vertexType
	 * @param <E> the edgeType
	 * @param <GB> the GraphBeanType
	 * @param <G> the JGraphT-Graph that has to implement {@link org.jgrapht.graph.ListenableGraph}
	 * @param typeInfo the {@link GralogGraphTypeInfo}-instance that defines all properties needed
	 * to construct the Gralog-Graph
	 * @param graph the JGraphT-Graph that is used to initialize the Gralog-Graph
	 * @param the bean that is used to initialize the Gralog-Graph
	 * 
	 * @return the Gralog-Graph
	 * @throws UserException
	 */
	public static <V, E, GB, G extends ListenableGraph<V, E>> GralogGraphSupport<V, E, GB, G> createGraphSupport(
			GralogGraphTypeInfo<V, E, GB, G> typeInfo, G graph, GB bean)
			throws UserException {
		GralogGraphTypeInfoSupport<V, E, GB, G> typeInfoSupport = createTypeInfoSupport(
				typeInfo, graph, bean);
		GralogGraphSupport<V, E, GB, G> gralogGraphSupport = new GralogGraphSupport<V, E, GB, G>(
				typeInfoSupport);
		return gralogGraphSupport;
	}

}
