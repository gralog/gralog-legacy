package de.hu.gralog.plugin.generator.alg;

import org.jgrapht.Graphs;
import org.jgrapht.ListenableGraph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.EmptyGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.generate.LinearGraphGenerator;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.generate.RingGraphGenerator;
import org.jgrapht.generate.WheelGraphGenerator;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphFactory;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.gralog.graph.GralogGraphTypeInfo;
import de.hu.gralog.graph.GralogGraphTypeInfoFilter;
import de.hu.gralog.graph.types.elements.LabeledGraphVertex;
import de.hu.gralog.jgrapht.util.JGraphTUtils;


public class Generator<V extends LabeledGraphVertex, E,GB,G extends ListenableGraph<V,E>> implements Algorithm {

	private GralogGraphTypeInfo graphType;
	private GraphGeneratorTypes generatorType = GraphGeneratorTypes.RANDOM;
	private int numOfVertices = 0;
	private int numOfEdges = 0;
	private String vertexLabelPrefix;

	public GralogGraphTypeInfo getGraphType() {
		return graphType;
	}

	public void setGraphType(GralogGraphTypeInfo graphTypeInfo) {
		this.graphType = graphTypeInfo;
	}
	
	public GraphGeneratorTypes getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(GraphGeneratorTypes generatorType) {
		this.generatorType = generatorType;
	}

	public int getNumOfVertices() {
		return numOfVertices;
	}

	public void setNumOfVertices(int numOfVertices) {
		this.numOfVertices = numOfVertices;
	}
	
	public int getNumOfEdges() {
		return numOfEdges;
	}

	public void setNumOfEdges(int numOfEdges) {
		this.numOfEdges = numOfEdges;
	}

	public String getVertexLabelPrefix() {
		return vertexLabelPrefix;
	}

	public void setVertexLabelPrefix(String vertexLabelPrefix) {
		this.vertexLabelPrefix = vertexLabelPrefix;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();

		if ( getGeneratorType() == null )
            e.addPropertyError( "generatorType", InvalidPropertyValuesException.PROPERTY_REQUIRED );
        if ( ! (getNumOfVertices() >= 0) )
                e.addPropertyError( "numOfVertices", InvalidPropertyValuesException.GREATER_EQUAL_ZERO );
        if ( getGraphType() == null )
            e.addPropertyError( "graphType", InvalidPropertyValuesException.PROPERTY_REQUIRED );
        if ( e.hasErrors() )
                throw e;

        GralogGraphSupport<V,E,GB,G> graph = GralogGraphFactory.createGraphSupport( getGraphType() );
        
        GraphGenerator<V,E,V> generator;
        switch ( getGeneratorType() ) {
        case RANDOM:
        	generator = new RandomGraphGenerator<V,E>( getNumOfVertices(), getNumOfEdges() );
        	GralogGraphSupport copyGraph = GralogGraphFactory.createGraphSupport( graph.getTypeInfo() );
        	generator.generateGraph( JGraphTUtils.getListenableBaseGraph( copyGraph.getGraph() ), new NumberedLabeledGraphVertexFactory<V>( graph, getVertexLabelPrefix() ), null );
        	Graphs.addGraph( graph.getGraph(), copyGraph.getGraph() );
        	break;
        case EMPTY:
        	generator = new EmptyGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( graph.getGraph(), new NumberedLabeledGraphVertexFactory<V>( graph, getVertexLabelPrefix() ), null );
        	break;
        case LINEAR:
        	generator = new LinearGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( graph.getGraph(), new NumberedLabeledGraphVertexFactory<V>( graph, getVertexLabelPrefix() ), null );
        	break;
        case RING:
        	generator = new RingGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( graph.getGraph(), new NumberedLabeledGraphVertexFactory<V>( graph, getVertexLabelPrefix() ), null );
        	break;
        case WHEEL:
        	generator = new WheelGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( graph.getGraph(), new NumberedLabeledGraphVertexFactory<V>( graph, getVertexLabelPrefix() ), null );
        	break;
        default:
        }

        AlgorithmResult result = new AlgorithmResult(  );
        result.setSingleContent( new AlgorithmResultContent( graph ) );
        result.setOpenContentsAsGraphs( true );
        return result;
	}
	
	private static class NumberedLabeledGraphVertexFactory<V extends LabeledGraphVertex> implements VertexFactory<V> {

		private int nextVertexNumber = 0;
		private GralogGraphSupport<V, ?,?,?> graph;
		private String vertexLabelPrefix;
		
		public NumberedLabeledGraphVertexFactory( GralogGraphSupport<V,?,?,?> graph, String vertexLabelPrefix ) {
			this.graph = graph;
			this.vertexLabelPrefix = vertexLabelPrefix;
		}
		
		public V createVertex() {
			V vertex = graph.createVertex();
			vertex.setLabel( getNextVertexLabel() );
			return vertex;
		}
		
		private String getNextVertexLabel() {
			if ( vertexLabelPrefix != null )
				return vertexLabelPrefix + Integer.toString( nextVertexNumber++ );
			return Integer.toString( nextVertexNumber++ );
		}
		
	}
	
	public static class LabeledGraphVertexGraphTypeInfoFilter implements GralogGraphTypeInfoFilter {

		public boolean filterTypeInfo(GralogGraphTypeInfo graphTypeInfo) {
			if ( graphTypeInfo.getVertexFactory().createVertex() instanceof LabeledGraphVertex )
				return false;
			return true;
		}
		
	}
	
	public enum GraphGeneratorTypes {
		EMPTY, LINEAR, RANDOM, RING, WHEEL
	}

}
