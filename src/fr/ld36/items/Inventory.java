package fr.ld36.items;


public class Inventory {
	
	Item slot1, slot2;
	
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
		System.out.println(slot1 +" "+slot2);
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
}
