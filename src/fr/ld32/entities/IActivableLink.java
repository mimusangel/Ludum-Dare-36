package fr.ld32.entities;

public interface IActivableLink
{
	public void toggle(Entity e, boolean toggle);
	public boolean isReady();
}
