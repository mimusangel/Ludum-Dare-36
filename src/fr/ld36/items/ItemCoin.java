package fr.ld36.items;

import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Texture;

public class ItemCoin extends ItemValuable{

	static Animation a;
	
	public ItemCoin() {
		this.setName("Coin");
		this.scale = 0.2f;
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
	public int getValue() {
		return 10;
	}

	@Override
	public Texture getTexture() {
		return null;
	}

	@Override
	public void setTexture(Texture tex) {
		
	}

}
