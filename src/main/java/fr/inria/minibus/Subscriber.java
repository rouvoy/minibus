package fr.inria.minibus;

/**
 * @author Romain Rouvoy
 *
 */
public interface Subscriber {
	/**
	 * 
	 * @param eventType
	 * @param listener
	 */
	<E,R> void subscribe(Class<E> eventType, Listener<E,R> listener);
	
	/**
	 * 
	 * @param eventType
	 * @param filter
	 * @param listener
	 */
	<E,R> void subscribe(Class<E> eventType, String filter, Listener<E,R> listener);
	
	/**
	 * 
	 * @param subcriber
	 */
	void subscribe(Object subcriber);
}
