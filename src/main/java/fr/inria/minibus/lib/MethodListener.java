package fr.inria.minibus.lib;

import java.lang.reflect.Method;

public class MethodListener<E> implements fr.inria.minibus.Listener<E> {
	private final Method handler;
	private final Object listener;

	public MethodListener(Object o, Method m) {
		listener = o;
		handler = m;
	}

	public void notify(E event) {
		try {
			this.handler.invoke(listener, event);
		} catch (Exception e) {
			PublicationException.forward(e);
		}
	}
}
