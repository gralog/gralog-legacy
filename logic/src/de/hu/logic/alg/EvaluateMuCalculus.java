/*
 * Created on 2006 by Sebastian Ordyniak
 *
 * Copyright 2006 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (kreutzer.stephan@googlemail.com)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */

package de.hu.logic.alg;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Hashtable;

import org.jgrapht.graph.Subgraph;

import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.alg.Algorithm;
import de.hu.gralog.graph.alg.AlgorithmResult;
import de.hu.gralog.graph.alg.AlgorithmResultContentTreeNode;
import de.hu.gralog.graph.alg.InvalidPropertyValuesException;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph;
import de.hu.gralog.jgrapht.graph.DisplaySubgraphMode;
import de.hu.gralog.jgrapht.graph.SubgraphFactory;
import de.hu.gralog.jgrapht.graph.DisplaySubgraph.DisplayMode;
import de.hu.logic.general.EvaluationException;
import de.hu.logic.graph.Proposition;
import de.hu.logic.graph.TransitionSystem;
import de.hu.logic.modal.Formula;
import de.hu.logic.modal.SetEvaluation;
import de.hu.logic.parser.FormulaList;
import de.hu.logic.parser.ModalLogicParser;
import de.hu.logic.parser.ParseException;

public class EvaluateMuCalculus implements Algorithm {

	private static final String EVALUATION_SG = "evaluation";
	private TransitionSystem transitionSystem;
	private String formula;
	
	public EvaluateMuCalculus() {
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
			
			FormulaList list = ModalLogicParser.loadString( sb.toString() );
			
			list.printList();
			
			Formula f = list.substituteMain();
			
			AlgorithmResult result = new AlgorithmResult( getTransitionSystem() );
			
			DisplaySubgraphMode displayMode = new DisplaySubgraphMode();
			displayMode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
			result.addDisplaySubgraphMode( EVALUATION_SG, displayMode );
			
			result.setContentTree( buildContentTree( f ) );
			
			return result;
		} catch (EvaluationException e) {
			throw new UserException( "Cannot evaluate Formula: " + getFormula() + " on the given transitionSystem.", e );
		} catch (FileNotFoundException e) {
			throw new UserException( "should not happen", e );
		} catch (ParseException e) {
			throw new UserException( "The formuala: " + getFormula() + " has a wrong syntax.", e );
		} catch (Exception e) {
			throw new UserException( "unknown error", e );
		} 
	}
	
	private MueKalkulAlgorithmResultContentTreeNode buildContentTree( Formula formula ) {
		MueKalkulAlgorithmResultContentTreeNode node = new MueKalkulAlgorithmResultContentTreeNode( formula, transitionSystem );
		if ( formula.type() == Formula.bottom || formula.type() == Formula.top || formula.type() == Formula.proposition )
			return node;
		if ( formula.type() == Formula.and || formula.type() == Formula.or ) {
			node.addChild( buildContentTree( formula.leftSubf() ) );
			node.addChild( buildContentTree( formula.rightSubf() ) );
			return node;
		} 
		node.addChild( buildContentTree( formula.subf() ) );
		return node;
	}
	
	
	public static class MueKalkulAlgorithmResultContentTreeNode extends AlgorithmResultContentTreeNode {
		
		Formula formula;
		TransitionSystem transitionSystem;
		
		static {
			try {
				Introspector.getBeanInfo( MueKalkulAlgorithmResultContentTreeNode.class ).getBeanDescriptor().setValue( "persistenceDelegate", new MueKalkulAlgorithmResultContentTreeNodePersistenceDelegate() );
				Introspector.getBeanInfo( Formula.class ).getBeanDescriptor().setValue( "persistenceDelegate", new FormulaPersistenceDelegate() );
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
		}
		
		public MueKalkulAlgorithmResultContentTreeNode( Formula formula, TransitionSystem transitionSystem ) {
			this.formula = formula;
			this.transitionSystem = transitionSystem;
		}

		private void computeSubgraphs() {
			try {
				subgraphs = new Hashtable<String, Subgraph>();
				SetEvaluation eval = new SetEvaluation();
				Proposition rp = eval.evaluate( transitionSystem, formula );
				
				Subgraph subgraph = SubgraphFactory.createSubgraph( transitionSystem, new HashSet( rp.getVertices() ), new HashSet() );
				subgraphs.put( EVALUATION_SG, subgraph );
			} catch (EvaluationException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes) throws UserException {
			if ( subgraphs == null )
				computeSubgraphs();
			return super.getDisplaySubgraphs(modes);
		}
		
		protected String getName() throws UserException {
			return formula.toString();
		}
		
	}
	
	public static class MueKalkulAlgorithmResultContentTreeNodePersistenceDelegate extends DefaultPersistenceDelegate {

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			MueKalkulAlgorithmResultContentTreeNode node = (MueKalkulAlgorithmResultContentTreeNode)oldInstance;
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { node.formula,  node.transitionSystem } );
		}
		
	}
	
	public static class FormulaPersistenceDelegate extends DefaultPersistenceDelegate {
		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			Formula formula = (Formula)oldInstance;
			
			if ( formula.type() == Formula.bottom || formula.type() == Formula.top || formula.type() == Formula.proposition )
				return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { formula.type(), formula.ident() } );
			if ( formula.type() == Formula.and || formula.type() == Formula.or )
				return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { formula.type(), formula.leftSubf(), formula.rightSubf() } );
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { formula.type(), formula.ident(), formula.subf() } );			
		}
	}
}
