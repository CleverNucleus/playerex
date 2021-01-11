package com.github.clevernucleus.playerex.api;

/**
 * An operation that accepts three input arguments and returns a result.
 * 
 * @param <K> type of the first argument
 * @param <V> type of the second argument
 * @param <S> type of the third argument
 * @param <R> type of the result
 */
@FunctionalInterface
public interface TriFunction<K, V, S, R> {
	
	/**
	 * Applies this function to the given arguments.
	 * @param k the first function argument
	 * @param v the second function argument
	 * @param s the third function argument
	 * @return the function result
	 */
	R apply(K k, V v, S s);
}
