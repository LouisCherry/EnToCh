package edu.just.demo;

import java.io.Serializable;
import java.util.TreeMap;

public class Dictionary<V> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TreeMap<String, Word> word=new TreeMap<String, Word>();
}
