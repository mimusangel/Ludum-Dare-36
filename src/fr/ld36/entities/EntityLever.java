package fr.ld36.entities;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityLever extends Entity implements IActivable
{
	Animation anim;
	boolean toggle;
	IActivableLink link;
	public EntityLever(Vec2 pos, IActivableLink link)
	{
		super(pos);
		this.link = link;
	}

	public void createEntity()
	{
		anim = new Animation(20, 32, Res.images.get("leverAnimation"), 0.05f);
		anim.setPause(true);
		toggle = false;
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
		return new AABB((int)pos.x + 12, (int)pos.y + 9, 8, 15);
	}

	public void action(Entity e)
	{
		if (anim.isEnded())
		{
			if (link != null)
			{
				if (link.isReady())
				{
					toggle = !toggle;
					link.toggle(this, toggle);
					anim.start();
				}
			}
			else
			{
				toggle = !toggle;
				anim.start();
			}
		}
	}
	
	public boolean spawnFront()
	{
		return (false);
	}
}
