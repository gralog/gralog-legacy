/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hu.gralog.export;

import org.jgrapht.Graph;

import de.hu.gralog.app.UserException;
import de.hu.gralog.structure.Structure;
import de.hu.gralog.structure.types.elements.LabeledStructureVertex;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author viktor
 */


public class TrivialGraphFormat extends 
    ExportFilter<Graph> {
    
    @Override public void Export(OutputStream stream, Graph graph)
            throws UserException
    {
        try
        {
            int i = 1;
            for(Object v : graph.vertexSet())
            {
                stream.write(("" + i + "\n").getBytes());
                i++;
            }
            stream.write(("#\n").getBytes());
            
            i = 1;
            for(Object v : graph.vertexSet())
            {
                for(Object e : graph.edgesOf(v))
                {
                    Object neighbor = graph.getEdgeTarget(e);
                    if(neighbor == v)
                        continue;
                    
                    // slow and stupid - I want to store the indexes in a map...
                    int j = 1;
                    for(Object w : graph.vertexSet())
                    {
                        if(w == neighbor)
                        {
                            stream.write(("" + i + " " + j + "\n").getBytes());
                            break;
                        }
                        j++;
                    }
                }
                i++;
            }
        
        }
        catch(Throwable e)
        {
            throw new UserException("Error exporting to TGF Format", e);
        }
    }

    
}
