package fr.ld36.entities;

import java.util.ArrayList;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.entities.spe.IActivableLink;
import fr.ld36.entities.spe.IWalkable;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityPlate extends Entity implements IActivable, IWalkable{

	Animation anim;
	public IActivableLink link;
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
		
		//Colision avec le joueur
		boolean colide = getBox().collided(game.player.getBox()) != null;
		//Colision avec les entités se trouvant à - de 32px
		if(!colide){
			ArrayList<Entity> entities = game.entities;
			for(int i = 0; i < entities.size(); i++){
				if(entities.get(i).getBox() != null && entities.get(i) != this){
					if(this.distance(entities.get(i))<=32){
						colide = getBox().collided(entities.get(i).getBox()) != null;
						if(colide) break;
					}
				}
			}
		}
		
		if(colide  && anim.isReversed()){
			anim.setReverse(false);
			anim.setPause(false);
		}
		else if(!colide && anim.isEnded() && !anim.isReversed()){
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
		if (anim != null)
			return new AABB((int) pos.x + 4, (int) pos.y + 28 + anim.getFrame(), 24, 4 - anim.getFrame());
		return new AABB((int) pos.x + 4, (int) pos.y + 28, 24, 4);
	}
	
	public Entity copy()
	{
		return new EntityPlate(pos.copy(), link);
	}

}
