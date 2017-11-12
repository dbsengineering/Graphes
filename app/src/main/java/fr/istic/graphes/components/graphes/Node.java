/********************************************************************
 * 						    Classe Node     						*
 * 			                Objet noeud	        				    *
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
package fr.istic.graphes.components.graphes;

import android.graphics.PointF;
import android.graphics.RectF;
import java.io.Serializable;

/**
 *
 */
public class Node implements Serializable {

    //--- Déclaration des propriétées ---
    private static final long serialVersionUID = -29238982928393L;
    private String nameNode, colorInt, colorTxt;
    private PointF pMiddle;
    private int id, size;
    private float coordX, coordY;
    private RectF rectFInt, rectFOut;
    private static int SIZE_MIN_NODE = 6; // Taille minimum d'un noeud
    private static final int POLICE_CHAR = 10; // Police de caractère.

    /**
     * Constructeur de la classe Node.
     * @param nameNode
     */
    public Node(float coordX, float coordY, String nameNode, int id){
        //Initialisation
        this.nameNode = nameNode;
        this.id = id;
        this.size = 60;
        this.colorInt = "bleu";
        this.colorTxt = "blanc";
        this.coordX = coordX;
        this.coordY = coordY;
        this.pMiddle = new PointF();
        setSize(this.size);
    }

    //--- Getters ---

    /**
     * Fonction qui retourne le Rectangle intérieur du noeud.
     * @return RectF
     */
    public RectF getRectFInt(){
        return (!this.rectFInt.isEmpty())?this.rectFInt:null;
    }

    /**
     * Fonction qui retourne le Rectangle exterieur du noeud.
     * @return
     */
    public RectF getRectFOut(){
        return (!this.rectFOut.isEmpty())?this.rectFOut:null;
    }

    public float getCoordX(){
        return this.coordX;
    }

    public float getCoordY(){
        return this.coordY;
    }

    public int getPolice(){
        return this.POLICE_CHAR;
    }

    public String getNameNode(){
        return this.nameNode;
    }

    /**
     * Fonction qui retourne la couleur du noeud.
     * @return colorNode : String
     */
    public String getColorInt(){
        return this.colorInt;
    }

    public int getId(){
        return this.id;
    }

    /**
     * Fonction qui retourne la taille du noeud.
     * @return
     */
    public int getSize(){
        if(this.nameNode.length() == 0 || this.nameNode.length() <= 4){
            return 40;
        }
        return this.nameNode.length()+40;
    }

    /**
     *Fonction qui retourne le milieu du noeud
     * @return PointF
     */
    public PointF getpMiddle(){
        return this.pMiddle;
    }

    //--- Setters ---

    /**
     * Procédure qui change la taille du noeud
     * @param taille : taille du noeud
     */
    public void setSize(int taille){

        int txtSize = this.nameNode.length();//Taille du texte
        int mult = 1;// Coefficient multiplicateur
        this.size = taille;

        //test la longueur du texte pour affecter un coeeficient multiplicateur
        //à la variable "multiplicateur"
        if(this.nameNode.length() <= SIZE_MIN_NODE) {
            mult = 1;
        }else if(this.nameNode.length() <= 10){
            mult = 6;
        }else{
            mult = POLICE_CHAR;
        }

        //Création des formes du noeuds et du centre de gravité
        this.rectFInt = new RectF(this.coordX - (taille+(txtSize*mult)),
                this.coordY - taille,
                this.coordX + (taille+(txtSize*mult)),
                this.coordY + taille);
        this.rectFOut = new RectF(this.coordX - (taille+(txtSize*mult)),
                this.coordY - taille,
                this.coordX + (taille+(txtSize*mult)),
                this.coordY + taille);

        this.pMiddle.x = (this.coordX-(taille+(txtSize*mult)))
                + (((this.coordX+(taille+(txtSize*mult)))
                -(this.coordX-(taille+(txtSize*mult))))/2);
        this.pMiddle.y = (this.coordY-taille) + (((this.coordY+taille)
                -(this.coordY-taille))/2);
    }

    /**
     * Procédure qui modifie le nom du noeud
     * @param name
     */
    public void setNameNode(String name){
        this.nameNode = name;
        this.setSize(this.size);
    }

    /**
     * Modification de la position du noeud
     * @param x
     * @param y
     */
    public void setCoord(float x, float y){
        this.coordX = x;
        this.coordY = y;
        setSize(this.size);
    }

    /**
     * Modifie la couleur du noeud ou du texte.
     */
    public void setColor(int type, String color){
        if(type == 0){
            this.colorInt = color;
        }else{
            this.colorTxt = color;
        }
    }
}
