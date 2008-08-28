/**
 * 
 * Created on 2008 by Stephan Kreutzer
 *
 * Copyright 2008 Sebastian Ordyniak (sordyniak@googlemail.com) and Stephan Kreutzer (stephan.kreuzer@comlab.ox.ac.uk)
 *
 * This file is part of Games.
 *
 * Games is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * Games is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Games; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 
 *
 */


package de.hu.graphgames.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;






/**
 * @author Stephan Kreutzer 
 *
 * A small helper class. This class takes a set S and an integer k and provides
 * an iterator over all k element subsets of S. The iterator guarantees that all sets
 * are of size exactly k and that no set is returned twice.
 * 
 * The iterator does not support removal of elements.
 */
public class SubsetIterator<V> implements Iterator 
{
	ArrayList<V> S;
	int k;
	int[] pos;		// array of indices. the following invariant is maintained throughout the iterator:
					//		i<j implies pos[i] < pos[j]
					// this guarantees that no duplicate sets are returned and all sets have size k
	boolean hasNext;
	
	public SubsetIterator(Set<V> set, int card)
	{
		k = card; 
		S = new ArrayList<V>(set);
		pos = new int[k];
		hasNext = true;
		for(int i=0;i<k;i++)
		{
			pos[i] = i;
			if(i>=S.size())
				hasNext = false;
		}
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

	public boolean hasNext() 
	{
		return hasNext;
	}

	/**
	 * Increases the tuple pos of indices by one (in lexicographical order) maintaining the invariant
	 * that i>j implies pos[i] > pos[j].
	 * 
	 * @return true if the pos has been increased, false if it was maximal.
	 */
	private boolean increase()		// increases the pos array by 1
	{
		int max = this.S.size()-1;
		if(pos[k-1] != max)
		{
			pos[k-1]++;
			return true;
		}
		else		// some positions have reached max
		{
			if(pos[0]+k-1 == max)	// we are done
				return false;
			for(int i=0;i<k;i++)
			{
				if(pos[i] +k-1-i == max)
				{
					pos[i-1]++;
					for(int j=i;j<k;j++)
						pos[j] = pos[i-1]+j-i+1;
				}
			}
			return true;
		}
	}
	
	public HashSet<V> next() 
	{
		if(!hasNext)
			return null;
		
		HashSet<V> set = new HashSet<V>(k);
		
		for(int i=k-1;i>=0;i--)
			set.add(S.get(pos[i]));
		hasNext = increase();
		return set;
	}

}
