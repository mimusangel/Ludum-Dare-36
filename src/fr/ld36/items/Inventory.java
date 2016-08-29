package fr.ld36.items;

import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.entities.Entity;
import fr.ld36.entities.EntityItem;
import fr.ld36.entities.EntityPlayer;
import fr.mimus.jbasicgl.maths.Vec2;

public class Inventory {
	
	Item slot1, slot2;
	EntityPlayer p;
	
	public Inventory(EntityPlayer p){
		this.p = p;
	}
	
	public boolean isFreeSlot(){
		return (slot1 == null || slot2 == null);
	}
	
	public void addItem(Item i){
		if(isFreeSlot()){
			if(slot1 == null)
				slot1 = i;
			else
				slot2 = i;
		}
	}
	
	public void invertItems(){
		Item tmp = slot1;
		slot1 = slot2;
		slot2 = tmp;
	}
	
	public void removeItem(int index){
		if(index == 0)
			slot1 = null;
		else if(index == 1)
			slot2 = null;
	}
	
	public void replaceItem(int index, Item i){
		if(index == 0)
			slot1 = i;
		else if(index == 1)
			slot2 = i;
	}
	
	public Item getItem(int index){
		if(index == 0)
			return slot1;
		if(index == 1)
			return slot2;
		return null;
	}
	
	public boolean isItem(int slot){
		if(slot == 0)
			return slot1 != null;
		if(slot == 1)
			return slot2 != null;
		return false;
	}
	
	public void dropItem(int slot){
		if(isItem(slot)){
			Game g = LD36.getInstance().game;
			Entity e = new EntityItem(p.pos, getItem(slot));
			System.out.println(e);
			g.addEntity(e);
			e.velocity = new Vec2(2 * (p.lookRight() ? 1 : -1),0);
			removeItem(slot);
		}
	}
}
