package fr.ld36.items;

import fr.ld36.render.Animation;
import fr.mimus.jbasicgl.graphics.Texture;

public abstract class Item {
	private String name = "Item";
	public Float scale = 1f;
	//L'animation et la texture sont statiques dans la classe fille pour pas quelle soit recr�e � chaque item, 
	//du coup j'ai fait les deux fonctions abstract get/set anim et texture
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	abstract public Animation getAnimation();
	abstract public void setAnimation(Animation anim);
	
	abstract public Texture getTexture();
	abstract public void setTexture(Texture tex);
}
