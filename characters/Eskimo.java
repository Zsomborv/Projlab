package characters;

import objects.Iglu;
/**Eszkimó osztály a karaktertõl örököl
 * Iglu építéséért felelõs
 * Minden mást a characteren keresztül,
 * * örökölt metódusokkal valósít meg
 * 
 * @author BCSV
 */
public class Eskimo extends Character{

	/**Paraméter nélkül konstruktor
	 * Létrehoz egy 5 testhõjû karaktert
	 * * az örökölt konstuktor segítségével
	 */
	public Eskimo() {
		super(5);
	}

	/**Iglu építéséért felelõs függvény
	 * paraméter nélkül az aktuális jégtáblára felépít egy iglut
	 * * ha már van iglu a jégtáblán, azt máshol kezeljük 
	 */
	public void buildIglu() {
		Iglu iglu = new Iglu();
		iglu.setMyTable(this.getMyTable());
		iglu.build();
		this.setEnergy(this.getEnergy()-1);
	}
}
