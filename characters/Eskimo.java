package characters;

import objects.Iglu;
/**Eszkim� oszt�ly a karaktert�l �r�k�l
 * Iglu �p�t�s��rt felel�s
 * Minden m�st a characteren kereszt�l,
 * * �r�k�lt met�dusokkal val�s�t meg
 * 
 * @author BCSV
 */
public class Eskimo extends Character{

	/**Param�ter n�lk�l konstruktor
	 * L�trehoz egy 5 testh�j� karaktert
	 * * az �r�k�lt konstuktor seg�ts�g�vel
	 */
	public Eskimo() {
		super(5);
	}

	/**Iglu �p�t�s��rt felel�s f�ggv�ny
	 * param�ter n�lk�l az aktu�lis j�gt�bl�ra fel�p�t egy iglut
	 * * ha m�r van iglu a j�gt�bl�n, azt m�shol kezelj�k 
	 */
	public void buildIglu() {
		Iglu iglu = new Iglu();
		iglu.setMyTable(this.getMyTable());
		iglu.build();
		this.setEnergy(this.getEnergy()-1);
	}
}
