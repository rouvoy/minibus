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
	<E> void subscribe(Class<E> eventType, Listener<E> listener);
	
	/**
	 * 
	 * @param eventType
	 * @param filter
	 * @param listener
	 */
	<E> void subscribe(Class<E> eventType, String filter, Listener<E> listener);
	
	/**
	 * 
	 * @param subcriber
	 */
	void subscribe(Object subcriber);
}
