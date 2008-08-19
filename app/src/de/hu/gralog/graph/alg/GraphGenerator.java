package de.hu.gralog.graph.alg;

import java.util.Random;

import org.jgrapht.graph.DefaultEdge;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.DirectedGraph;
import de.hu.gralog.graph.LabeledDirectedGraphTypeInfo;
import de.hu.gralog.graph.LabeledGraphVertex;

public class GraphGenerator implements Algorithm {
	private int counter;
	private DirectedGraph<? extends LabeledGraphVertex, ? extends DefaultEdge> graph;
	
	public void setCounter( int counter ) {
		this.counter = counter;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public AlgorithmResult execute() throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		
		if ( getCounter() <= 0 )
			e.addPropertyError( "counter", InvalidPropertyValuesException.GREATER_ZERO ); 
		if ( e.hasErrors() )
			throw e;

		DirectedGraph<LabeledGraphVertex, DefaultEdge> graph = (DirectedGraph<LabeledGraphVertex, DefaultEdge>)new LabeledDirectedGraphTypeInfo().newInstance();
		
		for ( int i = 0;i < getCounter();i++ )
			graph.addVertex( new LabeledGraphVertex( Integer.toString( i ) ) );
		
		Random random = new Random();
		for ( LabeledGraphVertex from : graph.vertexSet() ) {
			for ( LabeledGraphVertex to : graph.vertexSet() ) {
				if ( from != to && random.nextBoolean() ) {
					graph.addEdge( from, to );
				}
			}
		}
		
		AlgorithmResult result = new AlgorithmResult( graph );
		result.setSingleContent( new AlgorithmResultContent () );
		return result;
	}
}

