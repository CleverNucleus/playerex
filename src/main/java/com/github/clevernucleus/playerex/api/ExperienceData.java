package com.github.clevernucleus.playerex.api;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface ExperienceData extends ServerTickingComponent {
	boolean updateExperienceNegationFactor(final int amount);
	void resetExperienceNegationFactor();
}
