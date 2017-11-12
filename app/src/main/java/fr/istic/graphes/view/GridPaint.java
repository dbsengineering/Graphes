/********************************************************************
 * 						    Classe GridPaint   						*
 *                         Dessine un background                    *
 *                         (décoration ou repère)                   *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ez Ziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 09/11/2017								*
 *******************************************************************/
package fr.istic.graphes.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 *
 */

public class GridPaint  {

    //--- Déclaration des propriétées ---
    private Paint gridP;
    private Path path;
    private int sizeGrid; //Taille de l'espacement de la grille
    private int nbTiles;
    private static final int ROTATION = 0;

    /**
     * Constructeur de la classe.
     * @param height
     * @param width
     */
    public GridPaint(float height, float width){
        //Initialisation
        this.gridP = new Paint();
        this.gridP.setColor(Color.WHITE);
        this.gridP.setAntiAlias(true);
        this.gridP.setAlpha(30);
        this.gridP.setStyle(Paint.Style.STROKE);
        this.gridP.setStrokeJoin(Paint.Join.ROUND);
        this.gridP.setStrokeWidth(4f);

        this.path = new Path();//Init Path
        nbTiles = 10;
        this.sizeGrid = (Math.min((int)height,(int)width)/nbTiles);//150

        //Boucles pour dessiner une grille
        for(int i=0; i < height; i+=this.sizeGrid) {
            this.path.moveTo(0, i);
            this.path.lineTo(width, i+ROTATION);
        }
        for(int i=0; i < width; i+=this.sizeGrid){
            this.path.moveTo(i, 0);
            this.path.lineTo(i+ROTATION, height);
        }
    }

    /**
     * Fonction qui retourne le Path de la classe.
     * @return
     */
    public Path getPath(){
        return this.path;
    }

    /**
     * Fonction qui retourne le Paint de la classe.
     * @return
     */
    public Paint getPaint(){
        return this.gridP;
    }
}
