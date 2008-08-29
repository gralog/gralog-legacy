package de.hu.gralog.graph;

import org.jgrapht.ListenableGraph;

import de.hu.gralog.beans.GralogGraphBean;

public interface GralogGraph<V,E,GB extends GralogGraphBean> extends ListenableGraph<V,E> {
	public GralogGraphSupport<V,E,GB,? extends GralogGraph<V,E,GB>> getGralogSupport();
}
