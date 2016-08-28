package fr.ld36.entities;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.entities.spe.IActivableLink;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityPlate extends Entity implements IActivable{

	Animation anim;
	IActivableLink link;
	Boolean toggled = false;
	Boolean hasBeenToggled = false;
	
	public EntityPlate(Vec2 pos, IActivableLink link) {
		super(pos);
		this.link = link;
	}

	@Override
	public void createEntity() {
		anim = new Animation(4, 32, Res.images.get("plate"), 0.15f);
		anim.setPause(true);
	}
	
	public void update(Game game, int tick, double elapse)
	{
		if (anim != null)
		{
			anim.update();
		}
		else
			createEntity();
		
		if(getBox().collided(game.player.getBox()) != null  && anim.isReversed()){
			anim.setReverse(false);
			anim.setPause(false);
		}
		else if(getBox().collided(game.player.getBox()) == null && anim.isEnded() && !anim.isReversed()){
			anim.setPause(false);
			anim.setReverse(true);
		}
		
		if(anim.isEnded() && !anim.isReversed())
			toggled = true;
		else if(anim.isEnded() && anim.isReversed())
			toggled = false;
		
		if(link != null){
			if(!hasBeenToggled && toggled)
			{
				link.toggle(this, true);
				hasBeenToggled = true;
			}
			else if(hasBeenToggled && !toggled)
			{
				link.toggle(this, false);
				hasBeenToggled = false;
			}
		}
	}
	
	public void render(Shaders shader)
	{
		if (anim != null)
			anim.render(shader, pos.copy());
	}
	
	@Override
	public void action(Entity e) {
		
	}

	@Override
	public void giveDamage(Entity src, int dmg) {}

	@Override
	public void giveDamage(int x, int y, int dmg) {}

	@Override
	public AABB getBox() {
		return new AABB((int) pos.x + 4, (int) pos.y + 28, 24, 4);
	}

}
