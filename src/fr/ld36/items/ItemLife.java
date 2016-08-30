package fr.ld36.items;

import fr.ld36.entities.Entity;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Vec2;

public class ItemLife extends Item implements IActivable{

	public ItemLife(){
		this.handOffset = new Vec2(-8,-16);
	}
	
	@Override
	public Animation getAnimation() {
		return new Animation(1,32,Res.images.get("heart"),1f);
	}

	@Override
	public void setAnimation(Animation anim) {
	}

	@Override
	public Texture getTexture() {
		return Res.images.get("heart");
	}

	@Override
	public void setTexture(Texture tex) {
	}
	@Override
	public void action(Entity e) {
		
	}

}
