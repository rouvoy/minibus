package fr.inria.minibus.lib;

import java.util.concurrent.Executor;

import fr.inria.jfilter.Filter;
import fr.inria.jfilter.FilterException;
import fr.inria.jfilter.FilterParser;
import fr.inria.minibus.Listener;
import fr.inria.minibus.Publisher;

public class FilterSubscription<E, R> extends Subscription<E, R> {
	private final Filter filter;

	public FilterSubscription(String f, Listener<E, R> l, Publisher p)
			throws FilterException {
		super(l, p);
		this.filter = FilterParser.instance.parse(f);
	}

	public void apply(E event, Executor executor) {
		if (filter.match(event))
			super.apply(event, executor);
	}
}
