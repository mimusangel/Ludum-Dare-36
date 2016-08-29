package fr.ld36.items;

import fr.ld36.render.Animation;
import fr.ld36.utils.Res;

public class ItemCoin extends ItemValuable{

	static Animation a;
	
	public ItemCoin() {
		this.setName("Coin");
		this.scale = 0.2f;
		if(getAnimation() == null)
			setAnimation(new Animation(1, 32, Res.images.get("coin"), 0.1f, scale));
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

}
