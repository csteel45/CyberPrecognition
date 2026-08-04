package com.saggs.cyberprecog.cache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * Wrapper class for interacting with the Terracotta cache.
 * 
 * @author Fabien Sanglier
 * @author Christopher Steel - Software AG Government Solutions
 * @version 1.0
 * 
 */
public class CacheUtils {
	private static Logger log = Logger.getLogger(CacheUtils.class);
	public static final String ENV_CACHE_CONFIGPATH = "ehcache.config.path";
	public static final String CACHE_NAME="BigMemory"; //Name must match name in ehcache.xml
	private static CacheUtils instance = null;
	private static Cache cache = null;
	private boolean loaded = false;
	
	/**
	 * Default private constructor.
	 * 
	 * @throws RuntimeException if a connection can't be made to the cache.
	 */
	private CacheUtils() throws RuntimeException {
		try {
			log.debug("Getting the cache... (this may take several seconds)");
			cache = getCache();
			//log.debug("Got the cache");
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR: Unable to connect to cache: " + e.getMessage());
		}
	}
	
	/**
	 * Singleton method
	 * 
	 * @return CacheUtils singleton instance
	 */
	public static synchronized CacheUtils getCacheUtils() {
		if(instance == null) {
			instance = new CacheUtils();
		}
		return instance;
	}
	
	/**
	 * Gets the default cache specified in the CACHE_NAME attribute.
	 * 
	 * @return #net.sf.ehcache.Cache
	 */
	public Cache getCache() throws Exception {
		return getCache(CACHE_NAME);
	}
	
	/**
	 * Gets a specified cache.
	 * 
	 * @return Singleton instance of CacheUtils
	 */
	public Cache getCache(String cacheName) throws Exception {
		return getCache(cacheName, null);
	}

	/**
	 * Gets a named Cache using an ehcache.xml on the specified resource path.
	 * 
	 * @param cacheName Name of the cache as specified in the ehache.xml file
	 * @param cacheMgrResourcePath Path to the ehcache.xml file
	 * @return Cache object
	 * @throws Exception If anything goes wrong finding the file or loading the cache
	 */
	private Cache getCache(String cacheName, String cacheMgrResourcePath) throws Exception {
		if(isLoaded()) {
			return cache;
		}
		CacheManager cacheManager = getCacheManager(cacheMgrResourcePath);
		if (null == cacheName || !cacheManager.cacheExists(cacheName)){
			log.info("EhCache.xml not found or cache not found.");
			return null;
		}
		cache = cacheManager.getCache(cacheName);
		log.info("Cache loaded");
		setLoaded(true);
		return cache;
	}
	
	/**
	 * Puts the value Object into the cache using the specified key Object as
	 * the index. Constructs an Element object using the key and value and
	 * then does a cache.put(element).
	 * 
	 * @param key The object to use as an index to store and retrieve objects in the cache
	 * @param value The Object to store in the cache.
	 */
	public void put(Object key, Object value) {
		cache.put(getElement(key, value));
	}
	
	/**
	 * Puts all Elements in the Collection, into the cache.
	 * 
	 * @param elements The Collection of Elements to put in the cache.
	 */
	public void putAll(Collection<Element> elements) {
		cache.putAll(elements);
	}
	
	/**
	 * Gets an object from the cache.
	 * 
	 * @param key The key that was used to put the object in the cache.
	 * @return The object indexed by the specified key.
	 */
	public Object get(Object key) {
		Element element = cache.get(key);
		if(null == element)
			return null;
		return element.getObjectValue();
	}
	
	/**
	 * Removes an object from the cache.
	 * 
	 * @param key The key to the object to be removed.
	 * @return True if the object was successfully removed, false if it was not found.
	 */
	public boolean remove(Object key) {
		return cache.remove(key);
	}
	
	/**
	 * Constructs an Element to be put in the cache using a key and a value.
	 * 
	 * @param key The object used to search the cache
	 * @param value The object to retrieve from the cache
	 * @return An Element that can be put into the cache.
	 */
	public Element getElement(Object key, Object value) {
		return new Element(key, value);		
	}
	
	/**
	 *  Clears the cache by removing all the Elements in the cache.
	 */
	public void clear() {
		cache.removeAll();
		log.debug("Cache cleared.");
	}

	/**
	 * Returns the number of Elements in the cache.
	 * 
	 * @return Number of Elements in the cache
	 */
	public long getSize() {
		return cache.getSize();
	}
	
	/**
	 * Gets the CacheManager by loading the ehcache.xml on the specified resource path.
	 * 
	 * @param resourcePath The path to the ehcache.xml file.
	 * @return The loaded CacheManager
	 */
	private static CacheManager getCacheManager(String resourcePath){
		String configLocationToLoad = null;
		if(null != resourcePath && !"".equals(resourcePath)){
			configLocationToLoad = resourcePath;
		} else if(null != System.getProperty(ENV_CACHE_CONFIGPATH)){
			configLocationToLoad = System.getProperty(ENV_CACHE_CONFIGPATH);
		} else {
			log.info("Trying to load Cache manager from default classpath");
			configLocationToLoad = "classpath:ehcache.xml";
		}
		
		if(null != configLocationToLoad){
			InputStream inputStream = null;
			try {
				if(configLocationToLoad.indexOf("file:") > -1){
					inputStream = new FileInputStream(configLocationToLoad.substring("file:".length()));
				}else if(configLocationToLoad.indexOf("classpath:") > -1){
					inputStream = CacheUtils.class.getClassLoader().getResourceAsStream(configLocationToLoad.substring("classpath:".length()));
				} else { //default to classpath if no prefix is specified
					inputStream = CacheUtils.class.getClassLoader().getResourceAsStream(configLocationToLoad);
				}

				if (inputStream == null) {
					throw new FileNotFoundException("File at '" + configLocationToLoad	+ "' not found");
				}
				
				log.info("Loading Cache manager from " + configLocationToLoad);
				return CacheManager.create(inputStream);
			} catch(IOException ioe) {
				throw new CacheException(ioe);
			}
			finally{
				if(null != inputStream){
					try {
						inputStream.close();
					} catch (IOException e) {
						throw new CacheException(e);
					}
					inputStream = null;
				}
			}
		} 
		else{
			log.info("Loading Cache manager from default classpath");
			return CacheManager.getInstance();
		}
	}
	
	/**
	 * Convenience method to verify that all the Elements are still in the cache.
	 * It assumes that the keys are longs in order from 0 to cache size-1. 
	 */
	public void checkElements() {
		try {
			List<?> list = cache.getKeys();
			long count = 0;
			while(count < cache.getSize()) {
				if(! list.contains(count)) {
					log.debug("Missing key: " + count);
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Test harness for testing access to the cache and the ability to read and
	 * write from it.
	 * 
	 * @param args Not Used
	 */
	public static void main(String[] args) {

		log.info("Starting...");
		CacheUtils cache = null;
		try {
			cache = CacheUtils.getCacheUtils();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		log.info("Cache size before puts = " + cache.getSize());

		cache.put("key1", "Hello");
		cache.put("key2", "World");
		log.info("Cache size after puts = " + cache.getSize());
		log.info("Key 1 = " + cache.get("key1"));		
		log.info("Key 2 = " + cache.get("key2"));
		cache.remove("key1");
		cache.remove("key2");
		log.info("Cache size after remove = " + cache.getSize());
		log.info("Test complete. Good Bye...");
	}
}
