package fr.ld36.entities;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.items.Item;
import fr.ld36.items.ItemValuable;
import fr.ld36.render.Animation;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityItem extends Entity{

	Item i;
	Animation a;
	
	float fly;
	boolean flyUp;
	
	float takeDelay = 2;
	
	public EntityItem(Vec2 pos, Item i) {
		super(pos.copy());
		
		this.i = i;
		this.a = i.getAnimation();
	}

	@Override
	public void createEntity() {
		
	}

	@Override
	public void render(Shaders shader) {
		if(a != null)
			a.render(shader, pos.copy().add(0, fly));
	}

	@Override
	public void update(Game game, int tick, double elapse) {
		//Flying animation
		if(flyUp)
			fly -= 0.2;
		else
			fly += 0.2;
		
		if(fly <= -10)
			flyUp = false;
		else if(fly >= -5)
			flyUp = true;
		
		//Delay before item pickup
		if(takeDelay >= 0)
			takeDelay -= elapse;
		//If item is valuable (coin, diamond...) and in range: attract it
		if(i instanceof ItemValuable && this.distance(game.player, new Vec2(16 * i.scale, 16 * i.scale), new Vec2(16, 32))<64 && takeDelay <= 0){
			pos.x += game.player.pos.x +16 > pos.x?2:-2;
			pos.y += game.player.pos.y + 32 > pos.y?2:-2;
			
			//if on player add money to inventory
			if(this.distance(game.player, new Vec2(16 * i.scale, 16 * i.scale), new Vec2(16, 32))<10){
				this.life = 0;
				ItemValuable iv = (ItemValuable) i;
				game.player.addMoney(iv.getValue());
			}
		}
		//Mimus si tu m'entends j'ai refait la gravité pour les pièces juste en dessous lol
		else{
			if (velocity.y == 0)
				velocity.y = 0.2f;
			if (velocity.y > 0)
				velocity.y += 0.04f;
			if (velocity.y < 0)
				velocity.y += 0.1f;
			if (velocity.y > 4)
				velocity.y = 4;
			if (velocity.y < 0.1f && velocity.y > -0.1f)
				velocity.y = 0;
			pos.add(velocity);
			if(velocity.x > 0)
				velocity.x -= 0.03;
			else
				velocity.x += 0.03;
			
			if (Math.abs(velocity.x) < 0.1)
				velocity.x = 0;
			
			if(a != null)
				a.update();
		}
	}
	
	@Override
	public void giveDamage(Entity src, int dmg) {
		
	}

	@Override
	public void giveDamage(int x, int y, int dmg) {
		
	}

	@Override
	public AABB getBox() {
		return new AABB((int) pos.x, (int) pos.y, (int) (32 * i.scale), (int) (32 * i.scale));
	}
	
	public Item getItem(){
		return i;
	}

	public Entity copy()
	{
		return new EntityItem(pos.copy(), i);
	}
}
