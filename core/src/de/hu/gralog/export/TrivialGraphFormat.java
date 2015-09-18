/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hu.gralog.export;

import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.types.elements.LabeledStructureVertex;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Vector;

import org.jgraph.JGraph;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author viktor
 */


public class TrivialGraphFormat extends 
        //ExportFilter<SimpleGraph<LabeledStructureVertex, DefaultEdge>> {
    ExportFilter<JGraph> {
    
//    @Override public void Export(OutputStream stream, UndirectedGraph<LabeledStructureVertex, DefaultEdge> graph)
    @Override public void Export(OutputStream stream, JGraph graph)
            throws UserException
    {
        try
        {
        /*        
            graph.
            int i = 1;
            for(LabeledStructureVertex v : graph..vertexSet())
            {
                stream.write(("" + i + " # " + v.getLabel() + "\n").getBytes());
                i++;
            }
            stream.write(("#\n").getBytes());
            
            for(DefaultEdge e : graph.edgeSet())
            {
                e.
            }
        */
        }
        catch(Throwable e)
        {
            throw new UserException("Error exporting to TGF Format", e);
        }
    }

    
}
