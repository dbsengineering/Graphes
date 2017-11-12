/********************************************************************
 * 						    Classe Arc       						*
 * 			                 Objet Arc                              *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ez Ziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 12/11/2017								*
 *******************************************************************/
package fr.istic.graphes.components.graphes;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

import java.io.Serializable;

/**
 *
 */
public class Arc implements Serializable {

    //--- Déclaration des propriétées ---
    private static final long serialVersionUID = -29238982928392L;
    protected String nameArc;
    protected RectF rectTxt; // RectF pour l'étiquette de l'arc
    protected String color, colorTxt; // Couleur de l'arc
    protected int id, tailleEti; // id de l'arc, taille etiquette
    protected PathMeasure pM; //Mesure la longueur de l'arc
    protected PointF pMiddle;
    protected Path pthArc, pthArrow; //Path Arc, arrow
    protected Node nodeStart, nodeEnd; //Noeuds de départ et d'arrivé
    protected float[] tab; //Récupération milieu de l'arc
    protected Region region; //Région
    protected float posX, posY;
    protected int thickness; // Epaisseur du trait
    protected int sizeL; // taille étiquette


    /**
     * Constructeur 1 de l'arc.
     */
    public Arc() {

    }

    /**
     * Constructeur 2 de la classe.
     *
     * @param nodeStart
     * @param nodeEnd
     */
    public Arc(Node nodeStart, Node nodeEnd, String nameArc, int id) {
        //Initialisation
        this.nodeStart = nodeStart;
        this.nodeEnd = nodeEnd;
        this.nameArc = nameArc;
        this.tailleEti = nameArc.length();
        this.id = id;
        this.color = "rougeArc";
        this.colorTxt = "blanc";
        this.thickness = 8;
        this.sizeL = 10;

        posX = 0;
        posY = 0;

        //Initialisation du path arc
        initPath();
    }

    private void initPath() {
        this.pthArc = new Path();
        this.pthArrow = new Path();
        this.pMiddle = new PointF();
        this.region = new Region();

        this.pthArc.moveTo(nodeStart.getpMiddle().x, nodeStart.getpMiddle().y);

        if (posX == 0 && posY == 0) {
            this.pthArc.quadTo(nodeStart.getpMiddle().x, nodeStart.getpMiddle().y
                    , nodeEnd.getpMiddle().x, nodeEnd.getpMiddle().y);
        } else {
            this.pthArc.quadTo(posX, posY
                    , nodeEnd.getpMiddle().x, nodeEnd.getpMiddle().y);
        }

        this.pM = new PathMeasure(this.pthArc, false);//Donne longueur avec chaque point de l'arc
        this.tab = new float[2];
        this.pM.getPosTan(pM.getLength() / 2, tab, null);



        // --- Création de la flêche
        float deltaX = nodeEnd.getpMiddle().x - nodeStart.getpMiddle().x;
        float deltaY = nodeEnd.getpMiddle().y - nodeStart.getpMiddle().y;

        int ARROWHEAD = 50;
        float sideZ = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float frac = ARROWHEAD < sideZ ? ARROWHEAD / sideZ : 1.0f;

        float point_x_1 = nodeStart.getpMiddle().x + ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = nodeStart.getpMiddle().y + ((1 - frac) * deltaY - frac * deltaX);

        float point_x_3 = nodeStart.getpMiddle().x + ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = nodeStart.getpMiddle().y + ((1 - frac) * deltaY + frac * deltaX);

        float point_x_2 = nodeEnd.getpMiddle().x;
        float point_y_2 = nodeEnd.getpMiddle().y;

        this.pthArrow.moveTo(point_x_1, point_y_1);
        this.pthArrow.lineTo(point_x_2, point_y_2);
        this.pthArrow.lineTo(point_x_3, point_y_3);
        this.pthArrow.lineTo(point_x_1, point_y_1);

        if (this.tailleEti == 0) {
            this.tailleEti = 2;
        }

        if (posX == 0 & posY == 0) {
            this.rectTxt = new RectF(this.tab[0] - this.tailleEti * this.sizeL,
                    this.tab[1] - (30*(this.sizeL/10)),
                    this.tab[0] + this.tailleEti * this.sizeL, this.tab[1] + this.sizeL);
        } else {
            this.rectTxt = new RectF(getpMiddle().x - this.tailleEti * this.sizeL,
                    getpMiddle().y - (30*(this.sizeL/10)),
                    getpMiddle().x + this.tailleEti * this.sizeL, getpMiddle().y + this.sizeL);
        }
    }

    //--- Getters ---

    /**
     * Fonction qui retourne la couleur de l'arc.
     *
     * @return color : String
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Fonction qui retourne le noeud de départ de l'arc.
     *
     * @return Node
     */
    public Node getNodeStart() {
        return this.nodeStart;
    }

    /**
     * Fonction qui retourne le noeud d'arrivé de l'arc.
     *
     * @return Node
     */
    public Node getNodeEnd() {
        return this.nodeEnd;
    }

    /**
     * Fonction qui retourne le nom de l'arc.
     *
     * @return NameArc : String
     */
    public String getNameArc() {
        return this.nameArc;
    }

    /**
     * Fonction qui retourne le milieu de l'étiiquette.
     *
     * @return
     */
    public PointF getpMiddle() {
        this.pMiddle.x = this.tab[0];
        this.pMiddle.y = this.tab[1];
        return pMiddle;
    }

    /**
     * Fonction qui retourne le cadre du texte.
     *
     * @return
     */
    public RectF getRectTxt() {
        return this.rectTxt;
    }

    /**
     * Fonction qui retourne le path de la flêche
     *
     * @return
     */
    public Path getPthArrow() {
        return this.pthArrow;
    }

    /**
     * Fonction qui retourne le Path de l'arc.
     *
     * @return Path
     */
    public Path getPthArc() {
        initPath();
        return this.pthArc;
    }

    /**
     * Fonction qui retourne l'épaisseur du trait.
     * @return thickness : int
     */
    public int getThickness(){
        return this.thickness;
    }

    /**
     * Fonction qui retourne la longueur de l'étiquette.
     * @return
     */
    public int getSizeLabel(){
        return this.sizeL;
    }

    //--- Setter ---

    public void setPath(float xStart, float yStart, float xStop, float yStop) {
        posX = xStop;
        posY = yStop;
    }

    /**
     * Procédure qui modifie la couleur de l'arc ou du texte.
     */
    public void setColor(int type, String color) {
        if (type == 0) {
            this.color = color;
        } else {
            this.colorTxt = color;
        }
    }

    /**
     * Procédure qui modifie le nom de l'étiquette.
     *
     * @param name
     */
    public void setNameArc(String name) {
        this.nameArc = name;
        this.tailleEti = name.length();
    }

    /**
     * Procédure qui modifie l'épaisseur du trait.
     * @param thickness
     */
    public void setThickness(int thickness){
        this.thickness = thickness;
    }

    /**
     * Procédure qui modifie la longueur de l'étiquette
     * @param size
     */
    public void setSizeLabel(int size){
        this.sizeL = size;
    }

}
