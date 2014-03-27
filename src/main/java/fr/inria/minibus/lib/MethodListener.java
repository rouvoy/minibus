package fr.inria.minibus.lib;

import java.lang.reflect.Method;

import fr.inria.minibus.Listener;

public class MethodListener<E,R> implements Listener<E,R> {
	private final Method handler;
	private final Object listener;

	public MethodListener(Object o, Method m) {
		listener = o;
		handler = m;
	}

	@SuppressWarnings("unchecked")
	public R notify(E event) {
		try {
			return (R) this.handler.invoke(listener, event);
		} catch (Exception e) {
			PublicationException.forward(e);
		}
		return null;
	}
}
