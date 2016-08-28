package fr.ld36.entities;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityChest extends Entity implements IActivable{

	Animation anim;
	
	boolean isOpen;
	
	@Override
	public void createEntity() {
		// TODO Auto-generated method stub
		
	}
	
	public EntityChest(Vec2 pos) {
		super(pos);
	}
	
	public void update(Game game, int tick, double elapse){
		if(anim == null){
			anim = new Animation(9, 32, Res.images.get("chest"),0.1f);
			anim.setPause(true);
		}else{
			anim.update();
		}
	}
	
	public void render(Shaders shader){
		anim.render(shader, pos);
	}

	@Override
	public void giveDamage(Entity src, int dmg) {}

	@Override
	public void giveDamage(int x, int y, int dmg) {}

	@Override
	public AABB getBox() {
		return new AABB((int) pos.x + 2, (int) pos.y + 14, 30, 18);
	}

	@Override
	public void action(Entity e) {
		if(!isOpen){
			isOpen = true;
			anim.restart();
			
			//Donner item au joueur ICI
		}
	}

}
