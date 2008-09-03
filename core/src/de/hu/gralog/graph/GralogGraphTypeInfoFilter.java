package de.hu.gralog.graph;

/**
 * This interface allows to filter
 * {@link GralogGraphTypeInfo GralogGraphTypeInfos}
 * 
 * @author Sebastian
 * 
 */
public interface GralogGraphTypeInfoFilter {
	/**
	 * 
	 * 
	 * @param typeInfo
	 * @return if true the typeInfo is considered as not applicable
	 */
	public boolean filterTypeInfo(GralogGraphTypeInfo typeInfo);
}
