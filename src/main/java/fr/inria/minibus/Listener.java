package fr.inria.minibus;

public interface Listener<E> {
	void notify(E event);
}
