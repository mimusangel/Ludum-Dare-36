package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;

public class ItemTest extends Item implements IActivable{

	private static Animation a = new Animation(1,32,Res.images.get("heart"),1f);
	
	@Override
	public Animation getAnimation() {
		return a;
	}

	@Override
	public void setAnimation(Animation anim) {
		a = anim;
	}

	@Override
	public void action(Entity e) {
	}

}
