package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Texture;

public class ItemLoupe extends Item implements IActivable{

	private static Animation a = new Animation(1,32,Res.images.get("loupe"),1f);
	private static Texture t = Res.images.get("loupe");
	
	public ItemLoupe()
	{
		this.setName("Magnifying Glass");
		this.handOffset.set(-16, -23);
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
	public void action(Entity e) {
	}

	@Override
	public Texture getTexture() {
		return t;
	}

	@Override
	public void setTexture(Texture tex) {
		t = tex;		
	}
}
