/********************************************************************
 * 						Classe Node     							*
 * 			      Objet physique et graphique	    		        *
 * 					       d'un noeud   							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ezziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 26/10/2017								*
 *******************************************************************/
package fr.istic.graphes.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import fr.istic.graphes.R;

/**
 * Created by cavronjeremy on 04/10/2017.
 */

public class Node  {

    //--- Déclaration des propriétées ---

    private int coulIntern, coulCont, coulFont; //Couleur interne et contour du noeud
    private RectF rectFInt, rectFExt;
    private Paint pCont, pInt; //Peinture interne et contour du noeud
    private TextPaint txtPaint;
    private Context context;
    private String nameNoeud;// Nom du noeud
    private float cordX; //coordonnées x
    private float cordY; // Coordonnées y
    private PointF pMilieu;
    private int id;//Identifiant du noeud
    private int taille; // Taille du noeud
    private static final int SIZE_MIN_NODE = 4; // Taille minimum d'un noeud
    private static final int POLICE_CHAR = 10; // Police de caractère.

    /**
     * Constructeur 1 de la classe Noeud.
     */
    public Node(){}


    /**
     * Constructeur 2 de la classe Noeud.
     * @param context
     */
    public Node(Context context, float x, float y, String nameNoeud, int id){
        this.context = context;
        init(x, y, nameNoeud, id);
    }

    /**
     * Procédure d'initialisation du noeud.
     * @param x : coordonnée x
     * @param y : coordonée y
     * @param nameNoeud : nom du noeud
     * @param id : id du noeud
     */
    private void init(float x, float y, String nameNoeud, int id){
        this.nameNoeud = String.valueOf(nameNoeud);
        this.cordX = x;
        this.cordY = y;
        this.id = id;//Affectation de l'identifiant
        this.taille = 40;
        coulIntern = ContextCompat.getColor(context, R.color.colorBlueNoeud); //Couleur bleue interne du noeud
        coulCont = ContextCompat.getColor(context, R.color.colorGreyBord); //Couleur grise externe du noeud
        coulFont = ContextCompat.getColor(context, R.color.colorWhite); //Couleur blanche du texte

        pMilieu = new PointF();
        setSize(this.taille);//Création des formes du noeud

        //Initialisation des Paint (peinture)
        //Intérieur
        pInt = new Paint();
        pInt.setAntiAlias(true); //Anti aliasing interne (courbe plus douce)
        pInt.setStyle(Paint.Style.FILL); //Style rempli
        pInt.setColor(coulIntern); //Couleur interne
        pInt.setStrokeJoin(Paint.Join.ROUND); //Adoucir le trait

        //Contour
        pCont = new Paint();
        pCont.setAntiAlias(true); //Anti aliasing externe (courbe plus douce)
        pCont.setStyle(Paint.Style.STROKE); //Style de trait
        pCont.setColor(coulCont); //Couleur contour
        pCont.setStrokeJoin(Paint.Join.ROUND); //Adoucir le trait
        pCont.setStrokeWidth(8f); //Epaisseur du trait

        txtPaint = new TextPaint();
        txtPaint.setTextSize(30);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        txtPaint.setColor(coulFont);
        txtPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
    }

    /**
     * Procédure qui permet de modifier la couleur interne d'un noeud.
     * @param couleur : nouvelle couleur. De type entier.
     */
    public void setCoulIntern(int couleur){
        this.coulIntern = couleur;
        this.pInt.setColor(couleur);
    }

    /**
     * Procédure qui permet de changer la couleur du texte d'un noeud.
     * @param couleur : nouvelle couleur. De type entier.
     */
    public void setCoulFont(int couleur){
        this.coulFont = couleur;
        this.txtPaint.setColor(couleur);
    }

    /**
     * Procédure qui modifie le texte du noeud.
     * @param nameNoeud : texte du noeud.
     */
    public void setNameNoeud(String nameNoeud){
        this.nameNoeud = String.valueOf(nameNoeud);
        this.setSize(this.taille);
    }

    /**
     * Procédure qui change la taille du noeud
     * @param taille : taille du noeud
     */
    public void setSize(int taille){

        int txtSize = this.nameNoeud.length();//Taille du texte
        int mult = 1;// Coefficient multiplicateur
        this.taille = taille;

        //test la longueur du texte pour affecter un coeeficient multiplicateur
        //à la variable "multiplicateur"
        if(this.nameNoeud.length() <= SIZE_MIN_NODE) {
            mult = 1;
        }else if(this.nameNoeud.length() <= 10){
            mult = 6;
        }else{
            mult = POLICE_CHAR;
        }

        //Création des formes du noeuds et du centre de gravité
        rectFInt = new RectF(this.cordX - (taille+(txtSize*mult)),
                this.cordY - taille,
                this.cordX + (taille+(txtSize*mult)),
                this.cordY + taille);
        rectFExt = new RectF(this.cordX - (taille+(txtSize*mult)),
                this.cordY - taille,
                this.cordX + (taille+(txtSize*mult)),
                this.cordY + taille);

        pMilieu.x = (this.cordX-(taille+(txtSize*mult)))
                + (((this.cordX+(taille+(txtSize*mult)))
                -(this.cordX-(taille+(txtSize*mult))))/2);
        pMilieu.y = (this.cordY-taille) + (((this.cordY+taille)
                -(this.cordY-taille))/2);
    }

    /**
     * Fonction qui retourne la taille du noeud.
     * @return
     */
    public int getSize(){
        if(this.nameNoeud.length() == 0 || this.nameNoeud.length() <= 4){
            return 40;
        }
        return this.nameNoeud.length()+40;
    }


    /**
     * Fonction qui retourne le centre de gravité du noeud.
     * @param pMilieu
     */
    public void setPMilieu(PointF pMilieu){
        this.pMilieu = pMilieu;
    }


    /**
     * Fonction qui retourne le contour du noeuds (Graphique).
     * @return contour : de type Path.
     */
    public RectF getExtRectF(){
        return this.rectFExt;
    }

    /**
     * Fonction qui retourne l'intérieur du noeud (Graphique).
     * @return intern : de type Path
     */
    /*public RectF getIntRectF(){
        return this.rectFInt;
    }*/

    /**
     * Fonction qui retourne la peinture du contour du noeud (Graphique).
     * @return pCont : de type Paint.
     */
    public Paint getExtPaint(){
        return this.pCont;
    }

    /**
     * Fonction qui retourne la peinture interne du noeud (Graphique).
     * @return pInt : de type Paint.
     */
    public Paint getInternPaint(){
        return this.pInt;
    }

    /**
     * Fonction qui retourne le nom du noeud.
     * @return nameNoeud : de typechaîne de caractères.
     */
    public String getNameNoeud(){
        return this.nameNoeud;
    }

    /**
     * Fonction qui retourne la coordonnée X du noeud.
     * @return cordX : de type entier.
     */
    public float getCordX(){
        return this.cordX;
    }

    /**
     * Fonction qui retourne la coordonnée Y du noeud.
     * @return cordY : de type entier.
     */
    public float getCordY(){
        return this.cordY;
    }

    /**
     * Fonction qui retourne l'id du noeud
     * @return
     */
    public int getId(){
        return this.id;
    }


    /**
     *
     * @return
     */
    public PointF getPMilieu(){
        return this.pMilieu;
    }

    /**
     * Fonction qui retourne le noeud complet pour un Canva (Graphique).
     * @param canvas : de type Canvas.
     * @return Canvas : Canvas avec ajour du noeud : de type Canvas.
     */
    public void getNoeud(Canvas canvas){
        canvas.drawOval(this.rectFInt, pInt);
        canvas.drawOval(this.rectFExt, pCont);
        canvas.drawText(this.nameNoeud,this.cordX,this.cordY+POLICE_CHAR, this.txtPaint);
    }



    @Override
    public String toString() {
        return "Node{" +
                "coulIntern=" + coulIntern +
                ", coulCont=" + coulCont +
                ", rectFInt=" + rectFInt +
                ", rectFExt=" + rectFExt +
                ", pCont=" + pCont +
                ", pInt=" + pInt +
                ", txtPaint=" + txtPaint +
                ", context=" + context +
                ", nameNoeud='" + nameNoeud + '\'' +
                ", cordX=" + cordX +
                ", cordY=" + cordY +
                ", pMilieu=" + pMilieu +
                '}';
    }

}
