package de.hu.example.alg;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;

/**
 * This example show you how to use 
 * {@link de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent}
 * to allow user-interaction after the {@link AlgorithmResult} is
 * displayed in GrALoG. This example also shows how
 * to define a {@link java.beans.Customizer} that is
 * used as a Customizer for 
 * {@link de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent}.
 * 
 * The example takes an arbitrary GrALoG-Structure as a parameter 
 * and produces an {@link AlgorithmResult} with
 * a single content {@link ShortestPathInteractiveContent} that
 * extends {@link de.hu.gralog.algorithm.result.AlgorithmResultInteractiveContent}.
 * 
 * Please see {@link de.hu.example.alg.ShortestPathInteractiveContent} for
 * the implementation of this content.
 * 
 * @author Sebastian
 *
 */
public class ShortestPathInteractive implements Algorithm {

	private Structure structure;
	
	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getStructure() == null )
			e.addPropertyError( "structure", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( e.hasErrors() )
			throw e;
		
		AlgorithmResult result = new AlgorithmResult();
		DisplaySubgraphMode mode = new DisplaySubgraphMode();
		mode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		mode.setEdgeDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
		result.addDisplaySubgraphMode( ShortestPathInteractiveContent.DM_SHORTEST_PATH, mode );
		
		/**
		 * Now we add our {@link ShortestPathInteractiveContent} to
		 * the result. Please see {@link ShortestPathInteractiveContent}
		 * for how the rest works.
		 * 
		 */
		ShortestPathInteractiveContent content = new ShortestPathInteractiveContent( getStructure() );
		result.setSingleContent( content );
		return result;
	}
	

}
