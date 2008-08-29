package de.hu.gralog.graph.types;

import de.hu.gralog.beans.GralogGraphBean;

public interface GraphBeanFactory<GB extends GralogGraphBean>{
	public GB createBean();
}
