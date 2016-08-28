package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.mimus.jbasicgl.graphics.Texture;

public class Weapon extends Item{

	float damages;
	
	public Weapon(String name, Texture texture, float damages) {
		super(name, texture);
		this.damages = damages;
	}
	
	public void attack(Entity e, int dmg){
		e.giveDamage(0, 0, dmg);
	}

}
