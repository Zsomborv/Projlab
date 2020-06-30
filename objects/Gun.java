package objects;

import characters.Character;
/**
 * Ez az osztály valósítja meg a pisztolyt a játékban. 
 * A pisztoly a játék megnyeréséhez szükséges a patron és a jelzõfény mellett.
 */
public class Gun implements Item{
	/**
	 * A függvény meghívásánál a paraméterként kapott karakter jégtábláján keresztül lekérdezi az aktuális játékteret(GameWorld),
	 * majd ezen játéktér minden játékosán végigiterálva leellenõrzi, hogy valakinél megtalálható-e a fegyver következõ szükséges
	 * építõeleme a patron(Stencil). Amennyiben igen. akkor meghívja annak a void use(Character c) függvényét.
	 * @param c - A karakter amely használja az adott tárgyat. 
	 */
	public void use(Character c) {
		for(int j = 0; j < c.getMyTable().getGameworld().getPlayers().size(); j++) {
			for(int i = 0; i < c.getMyTable().getGameworld().getPlayers().get(j).getItems().size(); i++) {
				if(c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).getClass().equals(objects.Stencil.class)) {
					c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).use(c);
				}
			}
		}
	}
}