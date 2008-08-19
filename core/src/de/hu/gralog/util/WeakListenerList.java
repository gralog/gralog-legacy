package de.hu.gralog.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WeakListenerList<L> {

	private final ArrayList<WeakReference<L>> listenerReferences = new ArrayList<WeakReference<L>>();
	
	protected WeakReference<L> getReference( L listener ) {
		for ( WeakReference<L> lr : listenerReferences ) {
			L l = lr.get();
			if ( l != null && l == listener )
				return lr;
		}
		return null;
	}
	
	public void addListener( L listener ) {
		if ( getReference( listener ) == null )
			listenerReferences.add( new WeakReference<L>( listener ) );
	}
	
	public void removeListener( L listener ) {
		WeakReference<L> ref = getReference( listener );
		listenerReferences.remove( ref );
	}
	
	public ArrayList<L> getListeners() {
		ArrayList<L> listeners = new ArrayList<L>();
		for ( WeakReference<L> lr : new ArrayList<WeakReference<L>>( listenerReferences ) ) {
			L l = lr.get();
			if ( l != null )
				listeners.add( l );
			else
				listenerReferences.remove( lr );
		}
		return listeners;
	}
}
