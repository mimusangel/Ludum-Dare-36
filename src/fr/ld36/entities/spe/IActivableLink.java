package fr.ld36.entities.spe;

import fr.ld36.entities.Entity;

public interface IActivableLink
{
	public void toggle(Entity e, boolean toggle);
	public boolean isReady();
}
