package de.hu.gralog.gui.data;

import java.util.ArrayList;
import java.util.List;

import de.hu.gralog.gui.data.Plugin.AlgorithmInfo;
import de.hu.gralog.structure.StructureTypeInfo;

public class PluginsTree {

	public static PluginsTreeNode buildTree( ArrayList<Plugin> plugins ) {
		PluginsTreeNode root = new PluginsTreeNode( "root", PluginsTreeNodeType.ROOT, plugins );
		buildTree( root );
		return root;
	}
	
	protected static void buildTree( PluginsTreeNode node ) {
		switch ( node.getType() ) {
		case ROOT:
				ArrayList<Plugin> plugins = (ArrayList<Plugin>)node.getData();
				for ( Plugin plugin : plugins ) {
					if (!( (plugin.getAlgorithms() == null || plugin.getAlgorithms().size() == 0) &&
						(plugin.getStructureTypeClasses() == null || plugin.getStructureTypeClasses().size() == 0 ) ) ) {
						PluginsTreeNode child = new PluginsTreeNode( plugin.getName(), PluginsTreeNodeType.PLUGIN, plugin );
						node.addChild( child );
						buildTree( child );
					}
				}
				break;
		case PLUGIN:
			Plugin plugin = (Plugin)node.getData();
			if ( plugin.getStructureTypeClasses() != null && plugin.getStructureTypeClasses().size() != 0 ) {
				PluginsTreeNode graphsNode = new PluginsTreeNode( "structures", PluginsTreeNodeType.STRUCTURES, plugin.getStructureTypeClasses() );
				node.addChild( graphsNode );
				buildTree( graphsNode );
			}
			if ( plugin.getAlgorithms() != null && plugin.getAlgorithms().size() != 0 ) {
				PluginsTreeNode algorithmsNode = new PluginsTreeNode( "algorithms", PluginsTreeNodeType.ALGORITHMS, plugin.getAlgorithms() );
				node.addChild( algorithmsNode );
				buildTree( algorithmsNode );
			}
			break;
		case STRUCTURES:
			ArrayList<StructureTypeInfo> graphs = (ArrayList<StructureTypeInfo>)node.getData();
			for ( StructureTypeInfo graph : graphs ) {
				PluginsTreeNode graphNode = new PluginsTreeNode( graph.getName(), PluginsTreeNodeType.STRUCTURE, graph );
				node.addChild( graphNode );
				buildTree( graphNode );
			}
			break;
		case ALGORITHMS:
			ArrayList<AlgorithmInfo> algorithms = (ArrayList<AlgorithmInfo>)node.getData();
			for ( AlgorithmInfo algorithm : algorithms ) {
				PluginsTreeNode algorithmNode = new PluginsTreeNode( algorithm.getName(), PluginsTreeNodeType.ALGORITHM, algorithm );
				node.addChild( algorithmNode );
				buildTree( algorithmNode );
			}
			break;
		}
	}
	
	public static class PluginsTreeNode {
		private final List<PluginsTreeNode> children = new ArrayList<PluginsTreeNode>();
		private final String name;
		private final PluginsTreeNodeType type;
		private final Object data;
		
		public PluginsTreeNode( String name, PluginsTreeNodeType type ) {
			this( name, type, null );
		}
		
		public PluginsTreeNode( String name, PluginsTreeNodeType type, Object data ) {
			this.name = name;
			this.type = type;
			this.data = data;
		}
		
		public PluginsTreeNodeType getType() {
			return type;
		}
		
		public List<PluginsTreeNode> getChildren() {
			return children;
		}
		
		public void addChild( PluginsTreeNode child ) {
			children.add( child );
		}
		
		public Object getData() {
			return data;
		}
		
		public String toString() {
			return name;
		}
	}
	
	public static enum PluginsTreeNodeType {
		ROOT, PLUGIN, STRUCTURES, ALGORITHMS, STRUCTURE, ALGORITHM;
	}
}
