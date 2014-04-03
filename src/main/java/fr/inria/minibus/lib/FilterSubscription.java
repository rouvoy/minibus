package fr.inria.minibus.lib;

import java.util.concurrent.Executor;

import fr.inria.jfilter.Filter;
import fr.inria.jfilter.FilterException;
import fr.inria.jfilter.FilterParser;
import fr.inria.minibus.Listener;

public class FilterSubscription<E, R> extends DefaultSubscription<E, R> {
	private final Filter filter;

	public FilterSubscription(String f, Listener<E, R> l, MiniBus p, Subscribers subscribers, Class<E> type)
			throws FilterException {
		super(l, p, subscribers, type);
		this.filter = FilterParser.instance.parse(f);
	}

	public void apply(E event, Executor executor) {
		if (filter.match(event))
			super.apply(event, executor);
	}
}
