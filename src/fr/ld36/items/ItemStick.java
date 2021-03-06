package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Vec2;

public class ItemStick extends Weapon implements IActivable
{
	public static Texture t;
	public static Animation a;
	
	public ItemStick() {
		super();
		this.setName("Stick");
		if(t == null)
			t = Res.images.get("stick");
		if(a == null)
			a = new Animation(1, 32, Res.images.get("stick"), 1f);
		
		this.handOffset = new Vec2(-16, -27f);
		this.rotate = -45f;
	}

	@Override
	public Animation getAnimation() {
		return a;
	}

	@Override
	public void setAnimation(Animation anim) {
		a = anim;
	}

	@Override
	public Texture getTexture() {
		return t;
	}

	@Override
	public void setTexture(Texture tex) {
		t = tex;
	}

	@Override
	public float getDamage() {
		return 3;
	}

	@Override
	public void action(Entity e) {
	}

	@Override
	public Vec2 getImpact()
	{
		return new Vec2(-5, -20f);
	}

}
