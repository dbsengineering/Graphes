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
 *		DateModify : ...... 24/10/2017								*
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
import android.view.View;
import android.view.View.OnClickListener;

import fr.istic.graphes.R;

/**
 * Created by cavronjeremy on 04/10/2017.
 */

public class Node implements OnClickListener {

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
    private static final int SIZE_MIN_NODE = 4; // Taille minimum d'un noeud
    private static final int POLICE_CHAR = 10; // Police de caractère.
   // private Map<Integer,Node> mapNeightNode;//Liste des noeuds voisins


    /**
     *
     */
    public Node(){}


    /**
     * Constructeur de la classe Noeud
     * @param context
     */
    public Node(Context context, float x, float y, String nameNoeud, int id){
        this.context = context;
        this.nameNoeud = String.valueOf(nameNoeud);
        this.cordX = x;
        this.cordY = y + POLICE_CHAR;
        this.id = id;//Initialise l'identifiant
        coulIntern = ContextCompat.getColor(context, R.color.colorBlueNoeud); //Couleur bleue interne du noeud
        coulCont = ContextCompat.getColor(context, R.color.colorGreyBord); //Couleur grise externe du noeud
        coulFont = ContextCompat.getColor(context, R.color.colorWhite); //Couleur blanche du texte

        pMilieu = new PointF();


        //mapNeightNode = new HashMap<Integer,Node>();//Initialisation de la map des voisins

        //Si le nombre de caractères dans le texte est plus petit ou égal à 4, alors on applique
        //une taille standard, sinon on applique la taille des caractères + un décalement sur l'abscisse.
        if(this.nameNoeud.length() <= SIZE_MIN_NODE) {
            rectFInt = new RectF(x - 40, y - 40, x + 40, y + 40);
            rectFExt = new RectF(x - 40, y - 40, x + 40, y + 40);

            pMilieu.x = (x-40) +(((x+40)-(x-40))/2);
            pMilieu.y = (y-40) +(((y+40)-(y-40))/2);
        }else{
            rectFInt = new RectF(x - (20*this.nameNoeud.length()) / 2,
                    y - 40,
                    x + (20*this.nameNoeud.length()) / 2,
                    y + 40);
            rectFExt = new RectF(x - (20*this.nameNoeud.length()) / 2,
                    y - 40,
                    x + (20*this.nameNoeud.length()) / 2,
                    y + 40);

            pMilieu.x = (x-(20*this.nameNoeud.length()))
                    +(((x+(20*this.nameNoeud.length())/2)
                    -(x-(20*this.nameNoeud.length())))/2) + (4*this.nameNoeud.length());
            pMilieu.y = (y-40) +(((y+40)-(y-40))/2);
        }


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
    * Procédure qui permet d'afficher le menu d'options sur le noeud.
    */
    @Override
    public void onClick(View v){
        System.out.println("coucou");

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

    public void setNameNoeud(String nameNoeud){
        this.nameNoeud = String.valueOf(nameNoeud);
        this.setSize(nameNoeud.length());
    }

    /**
     * Procédure qui change la taille du noeud
     * @param taille
     */
    public void setSize(int taille){
        float y = this.cordY - POLICE_CHAR;
        pMilieu = new PointF();
        rectFInt = new RectF(this.cordX - (20*taille) / 2,
                y - 40,
                this.cordX + (20*taille) / 2,
                y + 40);
        rectFExt = new RectF(this.cordX - (20*taille) / 2,
                y - 40,
                this.cordX + (20*taille) / 2,
                y + 40);

        pMilieu.x = (this.cordX-(20*taille))
                +(((this.cordX+(20*taille)/2)
                -(this.cordX-(20*taille)))/2) + (4*taille);
        pMilieu.y = (y-40) +(((y+40)-(y-40))/2);
    }


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
        canvas.drawText(this.nameNoeud,this.cordX,this.cordY, this.txtPaint);
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
