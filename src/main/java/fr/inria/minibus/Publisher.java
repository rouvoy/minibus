package fr.inria.minibus;

public interface Publisher {
	 <E> void publish(E event);
}
