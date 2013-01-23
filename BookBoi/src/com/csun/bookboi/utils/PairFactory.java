package com.csun.bookboi.utils;

public class PairFactory {
	public static <F, S> Pair<F, S> makePair(final F f, final S s) {
		return new Pair<F, S>() {
			public F first() {
				return f;
			}
			
			public S second() {
				return s;
			}
		};
	}
}
