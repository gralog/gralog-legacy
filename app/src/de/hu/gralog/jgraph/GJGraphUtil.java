package de.hu.gralog.jgraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hu.gralog.app.InputOutputException;
import de.hu.gralog.graph.io.XMLDecoderIO;

public class GJGraphUtil {

	public static InputStream getGJGraphAsStream( GJGraph graph ) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new XMLDecoderIO().writeGraph( graph, out );
		return new ByteArrayInputStream( out.toByteArray() );
	}
	
	public static GJGraph getGJGraphFromStream( InputStream in ) throws InputOutputException {
		try {
			GJGraph graph = new XMLDecoderIO().readGraph( in );
			return graph;
		} catch( Throwable t ) {
			throw new InputOutputException( "unable to get graph from screen: ", t );
		}
	}
	
	public static GJGraph getGJGraphCopy( GJGraph graph ) throws InputOutputException {
		return getGJGraphFromStream( getGJGraphAsStream( graph ) );
	}
}
