package fr.inria.minibus;

public interface Listener<E, R> {
	R notify(E event);
}
