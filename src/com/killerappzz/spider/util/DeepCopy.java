package com.killerappzz.spider.util;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import android.util.Log;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of
 * objects. Objects are first serialized and then deserialized. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an error is printed to System.err and
 * null is returned. Depending on your specific application, it might
 * make more sense to have copy(...) re-throw the exception.
 */
public class DeepCopy {

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Object copy(Object orig) throws IOException, ClassNotFoundException {
    	Object obj = null;
    	// Write the object out to a byte array
    	FastByteArrayOutputStream fbos =
    		new FastByteArrayOutputStream();
    	ObjectOutputStream out = new ObjectOutputStream(fbos);
    	out.writeObject(orig);
    	out.flush();
    	out.close();

    	// Retrieve an input stream from the byte array and read
    	// a copy of the object back in.
    	ObjectInputStream in =
    		new ObjectInputStream(fbos.getInputStream());
    	try {
			obj = in.readObject();
		} catch (ClassNotFoundException e) {
			// should not happen 'cause we just serialized an object of this class!
			Log.d("QUIX", "Deep-copy of object " + orig + " error: object class definition unavailable", e);
			throw e;
		}
    	return obj;
    }

}
