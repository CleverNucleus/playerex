package com.github.clevernucleus.playerex.api.attribute;

import java.util.function.Supplier;

/**
 * A functional interface that acts as a supplier for player attributes.
 * 
 * @author CleverNucleus
 *
 */
@FunctionalInterface
public interface IAttribute extends Supplier<IPlayerAttribute> {}
