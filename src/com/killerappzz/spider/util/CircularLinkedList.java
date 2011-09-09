package com.killerappzz.spider.util;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A circular bloody list.
 * The circular effect is obtained simply via a special Iterator
 * OBS: use the {@link CircularListIterator} carefully! It can easily create endless loops...
 * just make sure you get out of the loop via your own conditions!
 * @author florin
 *
 */
public class CircularLinkedList<E> extends LinkedList<E> {

	public Iterator<E> iterator() {
		return new CircularListIterator();
	}
	
	private class CircularListIterator implements Iterator<E> {
		
		private int cursor = 0;
		// in order to prevent endless loops, we set a limit for the iteration count
		private int fuse = 0;
		private static final int MAX_ITER = 1000;

		// always has next!
		@Override
		public boolean hasNext() {
			if(fuse == MAX_ITER)
				return false;
			return true;
		}

		@Override
		public E next() {
			fuse++;
			E elem = get(cursor);
			cursor++;
			if(cursor == size())
				cursor = 0;
			return elem;
		}

		@Override
		public void remove() {
			// we'll not use this one...
		}
		
	}
}
