package de.hu.logic.alg;

import java.io.FileNotFoundException;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContent;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.logic.graph.TransitionSystem;
import de.hu.logic.modal.EvaluationException;
import de.hu.logic.modal.Formula;
import de.hu.logic.parser.FormulaList;
import de.hu.logic.parser.LogicParser;
import de.hu.logic.parser.ParseException;

public class BuildParityGameArena implements Algorithm {

	private TransitionSystem transitionSystem;
	private String formula;// = "\\muX.(X\\or(\\nuX.(X\\andP)))";
	
	public BuildParityGameArena() {
		super();
	}
	
	public TransitionSystem getTransitionSystem() {
		return transitionSystem;
	}

	public void setTransitionSystem(TransitionSystem transitionSystem) {
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
		if ( getFormula() == null )
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
			
			FormulaList list = LogicParser.loadString( sb.toString() );
			
//			list.printList();
			
			Formula f = list.substituteMain();
			
			AlgorithmResult result = new AlgorithmResult();
			result.setSingleContent( new AlgorithmResultContent( (new BuildParityGameArenaAlgorithm(transitionSystem, f)).execute() ) );
						
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
