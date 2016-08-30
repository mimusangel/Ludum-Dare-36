package fr.ld36.entities;


import java.util.ArrayList;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.items.Item;
import fr.ld36.items.ItemCoin;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntitySarcophage extends Entity implements IActivable{

	boolean isOpen;
	boolean bossSpawn;
	double lastItemSpawn;
	float itemSpawnDelay = 0.1f;
	ArrayList<Item> itemsToSpawn = new ArrayList<Item>();
	
	public EntitySarcophage(Vec2 pos) {
		super(pos);
		
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(64, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		mesh.addVertices(64, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1f);
		mesh.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1f);
		mesh.buffering();
		
		this.texture = Res.images.get("tomb");
		isOpen = false;
		lastItemSpawn = 0;
		bossSpawn = false;
	}
	
	public void update(Game game, int tick, double elapse){		
		if(itemSpawnable()){
			Entity e = new EntityItem(pos.copy().add(new Vec2(16, 0)), itemsToSpawn.get(0));
			e.velocity.add((float) -(Math.random() * 5 + 1), (float) -(3 + Math.random() * 3));
			game.entities.add(e);
			itemsToSpawn.remove(0);
		}
		if (!bossSpawn && isOpen && itemsToSpawn.size() > 0 && System.currentTimeMillis() - lastItemSpawn > itemSpawnDelay * 5000)
		{
			game.addEntity(new EntityBoss(pos.copy().add(32, -64)));
			bossSpawn = true;
		}
	}
	private boolean itemSpawnable()
	{
		if(isOpen && itemsToSpawn.size() > 0 && System.currentTimeMillis() - lastItemSpawn > itemSpawnDelay * 1000)
		{
			lastItemSpawn = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	@Override
	public void action(Entity e)
	{
		if (isOpen)
			return;
		for(int i = 0; i < 20 + Math.random()  * 20f; i++)
		{
			itemsToSpawn.add(new ItemCoin());
		}
		isOpen = true;
	}

	@Override
	public void createEntity() {
	}
	
	@Override
	public void giveDamage(Entity src, int dmg) {
	}

	@Override
	public void giveDamage(int x, int y, int dmg) {
	}

	@Override
	public AABB getBox() {
		return new AABB((int)pos.x, (int)pos.y, 64, 32);
	}

	@Override
	public Entity copy() {
		return new EntitySarcophage(pos.copy());
	}

	@Override
	public boolean spawnFront() {
		return false;
	}

}
