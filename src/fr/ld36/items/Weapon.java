package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.ld36.render.Animation;

public class Weapon extends Item{

	float damages;
	
	private static Animation a;
	
	public Weapon(String name, float damages) {
		super();
		this.setName(name);
		this.damages = damages;
	}
	
	public void attack(Entity e, int dmg){
		e.giveDamage(0, 0, dmg);
	}

	@Override
	public Animation getAnimation() {
		return a;
	}

	@Override
	public void setAnimation(Animation anim) {
		a = anim;
	}

}
