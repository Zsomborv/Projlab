package game;

import java.util.ArrayList;

import objects.Item;
/**
 * Ez az interface definiálja az itemek tulajdonosainak függvényeit. 
 * 
 * @author BCSV
 */
public interface ItemOwner {
	/**
	 * Item hozzáadása a tulajdonoshoz. A tulajdonos vagy egy darabot, vagy egy listát tárol,
	 * azzal teszi egyenlõvé vagy adja hozzá.
	 * @param i - az Item, amit kaptunk
	 */
	public void addItem(Item i);
	/**
	 * Item eltávolítása a tulajdonostól.
	 * @param i - az az item objektum, amit el akarunk távolítani
	 */
	public void removeItem(Item i);
	/**
	 * Getter függvény a tulajdonos Itemeit tároló listára.
	 * @return - visszaadja a tulajdonos Itemlistáját
	 */
	public ArrayList<Item> getItems();
}
