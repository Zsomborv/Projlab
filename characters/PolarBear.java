package characters;

import java.util.Random;

/**Jegesmedve osztály, a Stepable-tõl örököl
 * Felelõs:
 * * a gyilkolásért
 * * egy kör eltelte után lépnie kell a következõ táblára
 * 
 * @author BCSV
 */
public class PolarBear extends Stepable{
	boolean stepped = false;
	
	/**Gyilkolás megvalósítása
	 * A játék véget ér, hiszen megöl egy karaktert
	 */
	public void kill() {
		this.getMyTable().getGameworld().endGame(false);
	}

	/**
	 * Egy kör elteltének definiálása
	 * Egy véletlenszerû irány generálása és lépés az adott irányba
	 * Ha azon található iglu egy másik irány keresése
	 * Ha mindegyiken található iglu, marad a táblán
	 * Ha talált egy iglu nélküli táblát, akkor átlép arra
	 * Azon megnézi van-e karakter
	 * * Ha van, megöli
	 * * Ha nincs, visszetér a függvény
	 */
	@Override
	public void nextRound() {
		if(!stepped)
		{
			Random rand = new Random(); 
			int dir = rand.nextInt(this.getMyTable().getNeighbours().size());// % this.getMyTable().getNeighbours().size(); 
			int counter = 2*this.getMyTable().getNeighbours().size();

			//ez satorra nem jo
			boolean found = false;
			boolean search = true;
			while(search) {
				try {
					if(this.getMyTable().getNeighbour(dir).getIglu().getClass().equals(objects.Iglu.class)) {
						dir = rand.nextInt(this.getMyTable().getNeighbours().size());// % this.getMyTable().getNeighbours().size(); 
						counter--;
						if(counter == 0) search = false;
						if(this.getMyTable().getNeighbour(dir).getIglu()== null)found = true;
					}
					else {
						found = true;
						search = false;
					}
				} catch (Exception e) {
					found = true;
					search = false;
				}
			}
			if(found) this.step(dir); //TODO some error here, eszkimot nem eszi meg
			//this.getMyTable().getPolarbears().remove(this);
			if(this.getMyTable().getCharacters().size()>1) {
				for(Stepable s : this.getMyTable().getCharacters()) {
					if(s.getClass().equals(characters.Researcher.class) || s.getClass().equals(characters.Eskimo.class)) {
						kill();
					}
				}
			}
			stepped = true;
		}
	}
	
	/**
	* A stepped változó értékének beállító metódusa
	* param s - az új érték amire beállítjuk
	*/
	public void setStepped(boolean s)
	{
		stepped = s;
	}

	/**
	 * Viharba került medve
	 * A viharban még egyet lép
	 * nextRound metódus meghívása segítségével
	 */
	@Override
	public void storm() {
		this.nextRound();
	}
}
