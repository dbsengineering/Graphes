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
 *		Authors : ......... Cavron Jérémy                   		*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 09/11/2017								*
 *******************************************************************/
package fr.istic.graphes.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 *
 */

public class GridPaint  {

    //--- Déclaration des propriétées ---
    private Paint gridP, tilesImp, tilesP;
    private Path path;
    private RectF tiles; // tuiles
    private int sizeGrid; //Taille de l'espacement de la grille
    private float debGrid, finGrid;
    private int nbTiles;
    private static final int ROTATION = 0;
    private float width, height;
    private float div;

    /**
     * Constructeur de la classe.
     * @param height
     * @param width
     */
    public GridPaint(float height, float width){
        //Initialisation
        this.width = width;
        this.height = height;

        this.gridP = new Paint();
        this.gridP.setColor(Color.WHITE);
        this.gridP.setAntiAlias(true);
        this.gridP.setAlpha(30);
        this.gridP.setStyle(Paint.Style.STROKE);
        this.gridP.setStrokeJoin(Paint.Join.ROUND);
        this.gridP.setStrokeWidth(4f);

        this.tilesP = new Paint();
        this.tilesP.setColor(Color.WHITE);
        this.tilesP.setAntiAlias(true);
        this.tilesP.setAlpha(30);
        this.tilesP.setStyle(Paint.Style.FILL);
        this.tilesP.setStrokeJoin(Paint.Join.ROUND);
        this.tilesP.setStrokeWidth(4f);

        this.tilesImp = new Paint();
        this.tilesImp.setColor(Color.BLACK);
        this.tilesImp.setAntiAlias(true);
        this.tilesImp.setAlpha(30);
        this.tilesImp.setStyle(Paint.Style.FILL);
        this.tilesImp.setStrokeJoin(Paint.Join.ROUND);
        this.tilesImp.setStrokeWidth(4f);

        this.path = new Path();//Init Path
        nbTiles = 10;

        this.sizeGrid = (Math.min((int)height,(int)width));//150
        this.sizeGrid = this.sizeGrid/nbTiles;

        //Boucles pour dessiner une grille
        for(int i=0; i < height; i+=this.sizeGrid) {
            this.path.moveTo(0, i);
            this.path.lineTo(width, i+ROTATION);
        }
        for(int i=0; i < width; i+=this.sizeGrid){
            this.path.moveTo(i, 0);
            this.path.lineTo(i+ROTATION, height);
        }

        debGrid = (this.height/3)/2;
        finGrid = this.debGrid*4+sizeGrid;
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

    public void getTileP(Canvas canvas){
        int k = 0;
        for(int i=0; i < this.width; i+=this.sizeGrid) {
            for(int j=(int)this.debGrid; j < this.finGrid; j+=this.sizeGrid) {
                if(k%2 == 0) {
                    canvas.drawRect(i, j, this.sizeGrid + i, this.sizeGrid+j, this.tilesP);
                }else{
                    canvas.drawRect(i, j, this.sizeGrid + i, this.sizeGrid+j, this.tilesImp);
                }
                k++;
            }
            k++;
        }
    }
}
