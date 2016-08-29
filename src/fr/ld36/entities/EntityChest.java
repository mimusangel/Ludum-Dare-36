package fr.ld36.entities;

import java.util.ArrayList;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.items.Item;
import fr.ld36.items.ItemCoin;
import fr.ld36.items.ItemFlashlight;
import fr.ld36.items.ItemTest;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityChest extends Entity implements IActivable{

	Animation anim;
	
	boolean isOpen;
		
	double lastItemSpawn;
	float itemSpawnDelay = 0.1f;
	ArrayList<Item> itemsToSpawn = new ArrayList<Item>();
	
	@Override
	public void createEntity() {
		
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
		
		if(itemSpawnable()){
			Entity e = new EntityItem(pos.copy().add(new Vec2(16, 0)), itemsToSpawn.get(0));
			e.velocity.add((float) (Math.random() * 4 - 2), -2);
			game.entities.add(e);
			
			itemsToSpawn.remove(0);
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
			
			for(int i = 0; i < Math.random()  * 20f; i++)
			{
				itemsToSpawn.add(new ItemCoin());
			}
			itemsToSpawn.add(new ItemFlashlight());
			itemsToSpawn.add(new ItemTest());
		}
	}

	@Override
	public boolean spawnFront() {
		return false;
	}
	
	private boolean itemSpawnable(){
		if(itemsToSpawn.size() > 0 && System.currentTimeMillis() - lastItemSpawn > itemSpawnDelay * 1000)
		{
			lastItemSpawn = System.currentTimeMillis();
			return true;
		}
		return false;
	}

}
