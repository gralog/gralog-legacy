package de.hu.gralog.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.VertexFactory;

import de.hu.gralog.beans.DisplayChangeListenable;
import de.hu.gralog.beans.GralogGraphBean;
import de.hu.gralog.beans.PropertyChangeListenable;
import de.hu.gralog.graph.support.GraphDisplayChangeSupport;
import de.hu.gralog.graph.support.GraphPropertyChangeSupport;
import de.hu.gralog.graph.support.GraphSelectionSupport;
import de.hu.gralog.graph.types.GralogGraphTypeInfo;
import de.hu.gralog.jgraph.cellview.DefaultEdgeRenderer;
import de.hu.gralog.jgraph.cellview.DefaultVertexRenderer;

public class GralogGraphSupport<V,E,GB extends GralogGraphBean,G extends GralogGraph<V,E,GB>> {

	private final GraphPropertyChangeSupport propertyChangeSupport = new GraphPropertyChangeSupport();
	private final GraphDisplayChangeSupport<V,E> displayChangeSupport = new GraphDisplayChangeSupport<V,E>();
	private final GraphSelectionSupport<V,E> selectionSupport = new GraphSelectionSupport<V,E>();
	private final GralogGraphTypeInfoSupport<V,E,GB,G> typeInfoSupport;
	private G graph;
	
	
	GralogGraphSupport( GralogGraphTypeInfoSupport<V,E,GB,G> typeInfoSupport ) {
		this.typeInfoSupport = typeInfoSupport;
	}
	
	void setGraph( G graph ) {
		this.graph = graph;
		if ( getGraphBean() != null ) {
			getGraphBean().setGraph( graph );
			registerBean( getGraphBean() );
		}
	}
	
	void registerBean( Object bean ) {
		if ( bean instanceof PropertyChangeListenable )
			propertyChangeSupport.registerBean( (PropertyChangeListenable)bean );
		if ( bean instanceof DisplayChangeListenable )
			displayChangeSupport.registerBean( (DisplayChangeListenable<V,E>)bean );
	}
	
	public GraphPropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}
	
	public GraphDisplayChangeSupport<V,E> getDisplayChangeSupport() {
		return displayChangeSupport;
	}
	
	public GraphSelectionSupport<V,E> getGraphSelectionSupport() {
		return selectionSupport;
	}
	
	public String getName() {
		return typeInfoSupport.getName();
	}

	public GB getGraphBean() {
		return typeInfoSupport.getGraphBean();
	}
	
	public boolean isBeanEditable() {
		boolean editable = getGraphBean() != null && getGraphBean() instanceof PropertyChangeListenable;
		return editable;
	}
	
	public VertexFactory<V> getVertexFactory() {
		return typeInfoSupport.getVertexFactory();
	}
	
	public V createVertex() {
		return getVertexFactory().createVertex();
	}
	
	public boolean isVertexEditable() {
		return createVertex() instanceof PropertyChangeListenable;
	}
	
	public boolean isVertex( Object vertex ) {
		return createVertex().getClass().isInstance( vertex);
	}
	
	public EdgeFactory<V,E> getEdgeFactory() {
		if ( graph == null )
			return typeInfoSupport.getEdgeFactory();
		return graph.getEdgeFactory();
	}
	
	public boolean isEdgeEditable() {
		return getEdgeFactory().createEdge( createVertex(), createVertex() ) instanceof PropertyChangeListenable;
	}
	
	public boolean isEdge( Object edge ) {
		return getEdgeFactory().createEdge( createVertex(), createVertex() ).getClass().isInstance( edge );
	}
	
	public DefaultVertexRenderer getVertexRenderer() {
		return typeInfoSupport.getVertexRenderer();
	}
	
	public DefaultEdgeRenderer getEdgeRenderer() {
		return typeInfoSupport.getEdgeRenderer();
	}
	
	public GralogGraphTypeInfo<V,E,GB,G> getTypeInfo() {
		return typeInfoSupport.getTypeInfo();
	}
}
