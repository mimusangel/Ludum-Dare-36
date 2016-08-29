package fr.ld36;

import fr.ld36.entities.*;
import fr.mimus.jbasicgl.maths.Vec2;

public class EditorSelectEntity
{
	int select = 0;
	Entity list[];
	public EditorSelectEntity()
	{
		list = new Entity[] {
			new EntityBones(new Vec2()),
			new EntityBox(new Vec2()),
			new EntityChest(new Vec2()),
			new EntityDoor(new Vec2(), 0),
			new EntityTrap(new Vec2(), 0),
			new EntityLever(new Vec2(), null),
			new EntityPlate(new Vec2(), null),
			new EntitySign(new Vec2(), ""),
			new EntityMummy(new Vec2()),
			new EntitySarcophage(new Vec2())
		};
		for (Entity e : list)
			e.createEntity();
	}
	
	public int getSelectId()
	{
		return (select);
	}
	
	public void setSelect(int id)
	{
		if (id < 0)
			select = list.length - 1;
		else if(id >= list.length)
			select = 0;
		else
			select = id;
	}
	
	public Entity getSelect()
	{
		return (list[select]);
	}
	
	public void dispose()
	{
		for (Entity e : list)
			e.dispose();
	}
}
