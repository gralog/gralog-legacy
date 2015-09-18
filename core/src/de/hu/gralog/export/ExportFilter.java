/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hu.gralog.export;

import org.jgraph.JGraph;

import de.hu.gralog.app.UserException;
import java.io.OutputStream;

/**
 *
 * @author viktor
 */
//public abstract class ExportFilter<V, E, GB, G extends ListenableGraph<V, E>> {   
public abstract class ExportFilter<G extends JGraph> {   
    public abstract void Export(OutputStream stream, G graph) throws UserException;
}

