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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.algorithm.result.AlgorithmResultContentTreeNode;
import de.hu.gralog.algorithm.result.DisplaySubgraph;
import de.hu.gralog.algorithm.result.DisplaySubgraphMode;
import de.hu.gralog.algorithm.result.DisplaySubgraph.DisplayMode;
import de.hu.gralog.app.UserException;
import de.hu.gralog.graph.GralogGraphSupport;
import de.hu.logic.fo.FOEvaluation;
import de.hu.logic.fo.FOEvaluationTreeNode;
import de.hu.logic.fo.Formula;
import de.hu.logic.fo.Relation;
import de.hu.logic.graph.StructureAdaptorFactory;
import de.hu.logic.parser.FOFormulaList;
import de.hu.logic.parser.FOLogicParser;
import de.hu.logic.parser.ParseException;

public class FirstOrderLogicsSimple implements Algorithm {

	private static final String EVALUATION_SG = "evaluation";
	private GralogGraphSupport structure;
	private String formula;
	
	public FirstOrderLogicsSimple() {
		super();
	}
	
	public GralogGraphSupport getStructure() {
		return structure;
	}

	public void setStructure(GralogGraphSupport structure) {
		this.structure = structure;
	}
	
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public AlgorithmResult execute() throws InvalidPropertyValuesException, UserException {
		InvalidPropertyValuesException pe = new InvalidPropertyValuesException();
		
		if ( getStructure() == null )
			pe.addPropertyError( "structure",  InvalidPropertyValuesException.PROPERTY_REQUIRED );
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
			
			FOFormulaList list = FOLogicParser.loadString( sb.toString() );
			//list.printList();
			
			Formula f = list.getMainFormula();
			if(f.width() > 1)
				throw new UserException("Sorry, currently we only support formulas with at most one free variable");
			
			AlgorithmResult result = new AlgorithmResult( getStructure() );
			
			DisplaySubgraphMode displayMode = new DisplaySubgraphMode();
			displayMode.setVertexDisplayMode( DisplayMode.HIGH1, DisplayMode.SHOW );
			result.addDisplaySubgraphMode( EVALUATION_SG, displayMode );
			
			result.setContentTree( new SimpleFOAlgorithmResultContentTreeNode(  getStructure(), f ) );
			
			return result;
			/*} catch (EvaluationException e) {
			throw new UserException( "Cannot evaluate Formula: " + getFormula() + " on the given transitionSystem.", e );
		} catch (FileNotFoundException e) {
			throw new UserException( "should not happen", e );*/
		} catch (ParseException e) {
			throw new UserException( "The formuala: " + getFormula() + " has a wrong syntax.", e );
		} catch (Exception e) {
			throw new UserException( "unknown error", e );
		} 
	}
	
	public static class SimpleFOAlgorithmResultContentTreeNode extends AlgorithmResultContentTreeNode {
		
		private FOEvaluationTreeNode evaluationTreeNode;
		private GralogGraphSupport transitionSystem;
		private Formula formula;
		private transient boolean childrenBuild = false;
		
		static {
			try {
				Introspector.getBeanInfo( SimpleFOAlgorithmResultContentTreeNode.class ).getBeanDescriptor().setValue( "persistenceDelegate", new SimpleFOAlgorithmResultContentTreeNodePersistenceDelegate() );
				Introspector.getBeanInfo( Formula.class ).getBeanDescriptor().setValue( "persistenceDelegate", new FormulaPersistenceDelegate() );
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
		}

		public SimpleFOAlgorithmResultContentTreeNode( GralogGraphSupport transitionSystem, Formula formula ) throws UserException {
//			this.transitionSystem = transitionSystem;
			
			this( transitionSystem, new FOEvaluation().initialise( StructureAdaptorFactory.generateAdaptor(transitionSystem), formula ) );
			this.formula = formula;
		}
		
		public SimpleFOAlgorithmResultContentTreeNode( GralogGraphSupport transitionSystem, FOEvaluationTreeNode evaluationTreeNode ) {
			this.evaluationTreeNode = evaluationTreeNode;
			this.transitionSystem = transitionSystem;
		}
		
		private void computeSubgraphs() throws UserException {
			subgraphs = new Hashtable<String, SubgraphInfo>();
			Relation rp = evaluationTreeNode.getResult();
			
			if ( rp == null )
				rp = new Relation("",true);
			SubgraphInfo subgraphInfo = new SubgraphInfo( new HashSet( rp.getVertexSet() ), new HashSet() );
			subgraphs.put( EVALUATION_SG, subgraphInfo );
		}
		
		@Override
		protected Hashtable<String, DisplaySubgraph> getDisplaySubgraphs(Hashtable<String, DisplaySubgraphMode> modes, GralogGraphSupport graphSupport) throws UserException {
			if ( subgraphs == null )
				computeSubgraphs();
			return super.getDisplaySubgraphs(modes, transitionSystem);
		}

		@Override
		public ArrayList<AlgorithmResultContentTreeNode> getChildren() throws UserException 
		{
			if ( !childrenBuild ) {
				for ( FOEvaluationTreeNode child : evaluationTreeNode.getChildren() )
					addChild( new SimpleFOAlgorithmResultContentTreeNode( transitionSystem, child ) );
				childrenBuild = true;
			}
			return super.getChildren();
		}

		
		@Override
		protected Set<GralogGraphSupport> getAllGraphs() throws UserException {
			HashSet<GralogGraphSupport> graphs = new HashSet<GralogGraphSupport>();
			if ( getGraphSupport() != null )
				graphs.add( getGraphSupport() );
			return graphs;
		}

		public String toString() {
			return evaluationTreeNode.getName();
		}
	}
	
	public static class SimpleFOAlgorithmResultContentTreeNodePersistenceDelegate extends DefaultPersistenceDelegate {

		
		@Override
		protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
			//super.initialize(type, oldInstance, newInstance, out);
		}

		@Override
		protected Expression instantiate(Object oldInstance, Encoder out) {
			SimpleFOAlgorithmResultContentTreeNode node = (SimpleFOAlgorithmResultContentTreeNode)oldInstance;
			System.out.println( node.toString() );
			return new Expression( oldInstance, oldInstance.getClass(), "new", new Object[] { node.transitionSystem, node.formula } );
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
