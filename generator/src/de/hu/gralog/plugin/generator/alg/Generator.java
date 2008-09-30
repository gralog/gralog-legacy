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
import de.hu.gralog.jgrapht.util.JGraphTUtils;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureFactory;
import de.hu.gralog.structure.StructureTypeInfo;
import de.hu.gralog.structure.StructureTypeInfoFilter;
import de.hu.gralog.structure.types.elements.LabeledStructureVertex;


public class Generator<V extends LabeledStructureVertex, E,GB,G extends ListenableGraph<V,E>> implements Algorithm {

	private StructureTypeInfo structureType;
	private GraphGeneratorTypes generatorType = GraphGeneratorTypes.RANDOM;
	private int numOfVertices = 0;
	private int numOfEdges = 0;
	private String vertexLabelPrefix;

	public StructureTypeInfo getStructureType() {
		return structureType;
	}

	public void setStructureType(StructureTypeInfo structureTypeInfo) {
		this.structureType = structureTypeInfo;
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
        if ( getStructureType() == null )
            e.addPropertyError( "structureType", InvalidPropertyValuesException.PROPERTY_REQUIRED );
        if ( e.hasErrors() )
                throw e;

        Structure<V,E,GB,G> structure = StructureFactory.createStructure( getStructureType() );
        
        GraphGenerator<V,E,V> generator;
        switch ( getGeneratorType() ) {
        case RANDOM:
        	generator = new RandomGraphGenerator<V,E>( getNumOfVertices(), getNumOfEdges() );
        	Structure copyStructure = StructureFactory.createStructure( structure.getTypeInfo() );
        	generator.generateGraph( JGraphTUtils.getListenableBaseGraph( copyStructure.getGraph() ), new NumberedLabeledGraphVertexFactory<V>( structure, getVertexLabelPrefix() ), null );
        	Graphs.addGraph( structure.getGraph(), copyStructure.getGraph() );
        	break;
        case EMPTY:
        	generator = new EmptyGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( structure.getGraph(), new NumberedLabeledGraphVertexFactory<V>( structure, getVertexLabelPrefix() ), null );
        	break;
        case LINEAR:
        	generator = new LinearGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( structure.getGraph(), new NumberedLabeledGraphVertexFactory<V>( structure, getVertexLabelPrefix() ), null );
        	break;
        case RING:
        	generator = new RingGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( structure.getGraph(), new NumberedLabeledGraphVertexFactory<V>( structure, getVertexLabelPrefix() ), null );
        	break;
        case WHEEL:
        	generator = new WheelGraphGenerator<V,E>( getNumOfVertices() );
        	generator.generateGraph( structure.getGraph(), new NumberedLabeledGraphVertexFactory<V>( structure, getVertexLabelPrefix() ), null );
        	break;
        default:
        }

        AlgorithmResult result = new AlgorithmResult(  );
        result.setSingleContent( new AlgorithmResultContent( structure ) );
        result.setOpenContentsAsStructures( true );
        return result;
	}
	
	private static class NumberedLabeledGraphVertexFactory<V extends LabeledStructureVertex> implements VertexFactory<V> {

		private int nextVertexNumber = 0;
		private Structure<V, ?,?,?> structure;
		private String vertexLabelPrefix;
		
		public NumberedLabeledGraphVertexFactory( Structure<V,?,?,?> structure, String vertexLabelPrefix ) {
			this.structure = structure;
			this.vertexLabelPrefix = vertexLabelPrefix;
		}
		
		public V createVertex() {
			V vertex = structure.createVertex();
			vertex.setLabel( getNextVertexLabel() );
			return vertex;
		}
		
		private String getNextVertexLabel() {
			if ( vertexLabelPrefix != null )
				return vertexLabelPrefix + Integer.toString( nextVertexNumber++ );
			return Integer.toString( nextVertexNumber++ );
		}
		
	}
	
	public static class LabeledGraphVertexGraphTypeInfoFilter implements StructureTypeInfoFilter {

		public boolean filterTypeInfo(StructureTypeInfo structureTypeInfo) {
			if ( structureTypeInfo.getVertexFactory().createVertex() instanceof LabeledStructureVertex )
				return false;
			return true;
		}
		
	}
	
	public enum GraphGeneratorTypes {
		EMPTY, LINEAR, RANDOM, RING, WHEEL
	}

}
