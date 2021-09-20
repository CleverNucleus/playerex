package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.util.NameLevelPair;

import dev.onyxstudios.cca.api.v3.component.Component;

/**
 * Interface providing access to the PlayerEx persistent player cache. For any player that has ever joined the server.
 * 
 * @author CleverNucleus
 *
 */
public interface PersistentPlayerCache extends Component {
	
	/**
	 * Clears the cache of all players.
	 */
	void clear();
	
	/**
	 * @return Gets the name-level pair for every player.
	 */
	NameLevelPair[] get();
}
