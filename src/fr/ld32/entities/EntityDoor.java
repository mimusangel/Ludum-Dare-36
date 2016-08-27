package fr.ld32.entities;

import fr.ld32.AABB;
import fr.ld32.Game;
import fr.ld32.render.Animation;
import fr.ld32.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityDoor extends Entity implements IBlock, IActivableLink {

	Animation anim;
	int lock;
	int nbLock;
	boolean lastStat;
	public EntityDoor(Vec2 pos, int nbLock)
	{
		super(pos);
		this.nbLock = nbLock;
		lock = 0;
		lastStat = false;
	}

	public void toggle(Entity e, boolean toggle)
	{
		if (toggle)
			lock++;
		else
			lock--;
		boolean islock = (lock == nbLock);
		System.out.println(lock + " : " + nbLock + " = " + islock);
		if (lastStat != islock)
		{
			lastStat = islock;
			anim.setReverse(!islock);
			anim.setPause(false);
		}
	}

	public boolean isReady()
	{
		if (anim != null)
			return anim.isEnded();
		return (false);
	}

	public void createEntity()
	{
		anim = new Animation(24, 32, 64, Res.images.get("vDoor"), 0.05f);
		anim.setPause(true);
	}
	
	public void update(Game game, int tick, double elapse)
	{
		if (anim != null)
		{
			anim.update();
		}
		else
			createEntity();
	}
	
	public void render(Shaders shader, Vec2 offset)
	{
		if (anim != null)
			anim.render(shader, pos.copy().add(offset));
	}

	public boolean entityAlive()
	{
		return true;
	}

	public AABB getBox()
	{
		if (anim != null)
			return new AABB((int)pos.x + 12, (int)pos.y, 8, 64 - (anim.getFrame() * 59 / 24));
		return new AABB((int)pos.x + 12, (int)pos.y, 8, 64);
	}

}
