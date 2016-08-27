package fr.ld32.entities;

import fr.ld32.AABB;
import fr.ld32.Game;
import fr.ld32.render.Animation;
import fr.ld32.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityLever extends Entity {

	Animation anim;
	public EntityLever(Vec2 pos)
	{
		super(pos);
	}

	public void createEntity()
	{
		anim = new Animation(20, 32, Res.images.get("leverAnimation"), 0.05f);
	}

	public void update(Game game, int tick, double elapse)
	{
		if (anim != null)
			anim.update();
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
		return null;
	}

}
