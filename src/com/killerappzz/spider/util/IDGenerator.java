package com.killerappzz.spider.util;

/**
 * (very) simple tagging systems for object ID
 * Don't really need UUIDs....
 * @author florin
 *
 */
public class IDGenerator {

	private static long nextID = 0L;
	
	public static final long generate() {
		return nextID++;
	}
}
