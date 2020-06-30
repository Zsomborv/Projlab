package objects;

import characters.Character;
/**
 * A játékban megtalálható patron megvalósításáért felelõs osztály. 
 * A pisztolyba töltve a jelzõfénnyel együtt megnyerhetõ a játék.
 */
public class Stencil implements Item{
	/**
	 * A függvény meghívásánál hasonlóan mûködik, mint a Gun use függvénye.
	 * A paraméterként kapott karakter jégtábláján keresztül lekérdezi az aktuális játékteret(GameWorld),
	 * majd ezen játéktér minden játékosán végigiterálva lellenõrzi, hogy valakinél megtalálható-e a fegyver következõ szükséges
	 * építõeleme a patron(Stencil). Amennyiben igen. akkor meghívja annak a void use(Character c) függvényét.
	 * @param c - A karakter amely használja az adott tárgyat. 
	 */
	public void use(Character c) {
		for(int j = 0; j < c.getMyTable().getGameworld().getPlayers().size(); j++) {
			for(int i = 0; i < c.getMyTable().getGameworld().getPlayers().get(j).getItems().size(); i++) {
				if(c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).getClass().equals(objects.Light.class)) {
					c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).use(c);
				}
			}
		}
	}

}