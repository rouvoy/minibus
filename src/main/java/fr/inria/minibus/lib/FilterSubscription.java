package fr.inria.minibus.lib;

import java.util.concurrent.Executor;

import fr.inria.jfilter.Filter;
import fr.inria.jfilter.FilterException;
import fr.inria.jfilter.FilterParser;
import fr.inria.minibus.Listener;

public class FilterSubscription<E> extends Subscription<E> {
	private final Filter filter;

	public FilterSubscription(String filter, Listener<E> listener) throws FilterException {
		super(listener);
		this.filter = FilterParser.instance.parse(filter);
	}

	public void apply(E event, Executor executor) {
		if (filter.match(event)) {
			super.apply(event, executor);
		}
	}
}
