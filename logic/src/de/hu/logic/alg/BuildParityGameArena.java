package de.hu.logic.alg;

import java.io.FileNotFoundException;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContent;
import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.StructureFactory;
import de.hu.logic.general.EvaluationException;
import de.hu.logic.modal.Formula;
import de.hu.logic.parser.FormulaList;
import de.hu.logic.parser.ModalLogicParser;
import de.hu.logic.parser.ParseException;
import de.hu.logic.structure.TransitionSystem;
import de.hu.logic.structure.TransitionSystemEdge;
import de.hu.logic.structure.TransitionSystemVertex;
import de.hu.parity.structure.ParityGameArenaTypeInfo;
import de.hu.parity.structure.ParityGameVertex;

public class BuildParityGameArena<V extends TransitionSystemVertex, E extends TransitionSystemEdge, GB extends TransitionSystem<V,E,G>, G extends ListenableDirectedGraph<V,E>> implements Algorithm {

	private Structure<V,E,GB,G> transitionSystem;
	private String formula;// = "\\muX.(X\\or(\\nuX.(X\\andP)))";
	
	public BuildParityGameArena() {
		super();
	}
	
	public Structure<V,E,GB,G> getTransitionSystem() {
		return transitionSystem;
	}

	public void setTransitionSystem(Structure<V,E,GB,G> transitionSystem) {
		this.transitionSystem = transitionSystem;
	}
	
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
		
		if ( getTransitionSystem() == null )
			pe.addPropertyError( "transitionSystem",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( getFormula() == null || getFormula().length() == 0 )
			pe.addPropertyError( "formula",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
		
		if ( pe.hasErrors() )
			throw pe;
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append( "<logic type=\"modal\">\n" );
			sb.append( "<formula name=\"main\">\n" );
			sb.append( getFormula() + "\n" );
			sb.append( "</formula>\n" );
			sb.append( "</logic>" );
			
			FormulaList list = ModalLogicParser.loadString( sb.toString() );
			
//			list.printList();
			
			Formula f = list.substituteMain();
			
			AlgorithmResult result = new AlgorithmResult();
			result.setSingleContent( new AlgorithmResultContent( StructureFactory.createStructure( new ParityGameArenaTypeInfo(), new ListenableDirectedGraph<ParityGameVertex, DefaultEdge>( new BuildParityGameArenaAlgorithm(transitionSystem, f).execute() ) ) ) );
						
			return result;
		} catch (UserException e) {
			throw e;			
		} catch (EvaluationException e) {
			throw new UserException( "Cannot evaluate Formula: " + getFormula() + " on the given transitionSystem.", e );
		} catch (FileNotFoundException e) {
			throw new UserException( "should not happen", e );
		} catch (ParseException e) {
			throw new UserException( "The formula: " + getFormula() + " has a wrong syntax.", e );
		} catch (Exception e) {
			throw new UserException( "unknown error", e );
		} 
	}
}
