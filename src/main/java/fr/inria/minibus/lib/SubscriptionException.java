package fr.inria.minibus.lib;


/**
 * @author rouvoy
 * 
 */
public class SubscriptionException extends RuntimeException {
	private static final long serialVersionUID = -9158829764854871263L;

	private SubscriptionException(String cause) {
		super(cause);
	}

	private SubscriptionException(Throwable e) {
		super(e);
	}

	public static final void raise(String cause) {
		throw new SubscriptionException(cause);
	}

	public static final void checkNotNull(Object obj, String cause) {
		if (obj == null)
			raise(cause);
	}

	public static void forward(Throwable e) {
		throw new SubscriptionException(e);
	}
}
