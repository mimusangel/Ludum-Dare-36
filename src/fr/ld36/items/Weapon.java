package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.ld36.render.Animation;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Vec2;

public abstract class Weapon extends Item{

	float damages;
	
	public Weapon() {
		super();
		this.setName("Weapon");
	}
	
	public void attack(Entity e){
		e.giveDamage(0, 0, (int) damages);
	}

	public abstract float getDamage();
	
	public abstract Animation getAnimation();

	public abstract void setAnimation(Animation anim);

	public abstract Texture getTexture();

	public abstract void setTexture(Texture tex);
	
	public abstract Vec2 getImpact();
}
