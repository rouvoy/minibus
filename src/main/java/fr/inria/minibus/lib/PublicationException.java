package fr.inria.minibus.lib;

public class PublicationException extends RuntimeException {
	private static final long serialVersionUID = -2243380476409954150L;

	private PublicationException(String cause) {
		super(cause);
	}

	private PublicationException(Throwable e) {
		super(e);
	}

	public static final void raise(String cause) {
		throw new PublicationException(cause);
	}

	public static final void checkNotNull(Object obj, String cause) {
		if (obj == null)
			raise(cause);
	}

	public static void forward(Throwable e) {
		throw new PublicationException(e);
	}
}
