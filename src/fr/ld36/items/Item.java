package fr.ld36.items;

import fr.ld36.render.Animation;
import fr.mimus.jbasicgl.graphics.Texture;

public abstract class Item {
	private String name;
	private Texture tex;
	//Au cas où on ferait une anim pour l'item drop au sol
	private Animation a;
	
	public Item(String name, Texture texture){
		this(name, texture, null);
	}
	
	public Item(String name, Texture texture, Animation animation){
		this.name = name;
		this.tex = texture;
		this.a = animation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Texture getTexure() {
		return tex;
	}

	public void setTexture(Texture tex) {
		this.tex = tex;
	}

	public Animation getAnimation() {
		return a;
	}

	public void setAnimation(Animation a) {
		this.a = a;
	}
	
}
