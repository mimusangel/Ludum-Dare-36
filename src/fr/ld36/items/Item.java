package fr.ld36.items;

import fr.ld36.render.Animation;

public abstract class Item {
	private String name = "Item";
	//Au cas où on ferait une anim pour l'item drop au sol
	public Float scale = 1f;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	abstract public Animation getAnimation();

	abstract public void setAnimation(Animation anim);
	
}
