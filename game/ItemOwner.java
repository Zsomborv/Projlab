package game;

import java.util.ArrayList;

import objects.Item;
/**
 * Ez az interface defini�lja az itemek tulajdonosainak f�ggv�nyeit. 
 * 
 * @author BCSV
 */
public interface ItemOwner {
	/**
	 * Item hozz�ad�sa a tulajdonoshoz. A tulajdonos vagy egy darabot, vagy egy list�t t�rol,
	 * azzal teszi egyenl�v� vagy adja hozz�.
	 * @param i - az Item, amit kaptunk
	 */
	public void addItem(Item i);
	/**
	 * Item elt�vol�t�sa a tulajdonost�l.
	 * @param i - az az item objektum, amit el akarunk t�vol�tani
	 */
	public void removeItem(Item i);
	/**
	 * Getter f�ggv�ny a tulajdonos Itemeit t�rol� list�ra.
	 * @return - visszaadja a tulajdonos Itemlist�j�t
	 */
	public ArrayList<Item> getItems();
}
