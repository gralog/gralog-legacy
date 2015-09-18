/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hu.gralog.export;

import org.jgrapht.Graph;
import de.hu.gralog.app.UserException;
import java.io.OutputStream;

/**
 *
 * @author viktor
 */
public abstract class ExportFilter<G extends Graph> {   
    public abstract void Export(OutputStream stream, G graph) throws UserException;
}

