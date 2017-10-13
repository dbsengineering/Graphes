package fr.istic.graphes.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private int coulIntern, coulCont; //Couleur interne et contour du noeud
    private RectF rectFInt, rectFExt;
    private Paint pCont, pInt; //Peinture interne et contour du noeud
    private TextPaint txtPaint;
    private Context context;
    private String numNoeud;
    private String nameNoeud;
    private float cordX;
    private float cordY;
    private PointF pMilieu;


    /**
     *
     */
    public Node(){

    }

    /**
     * Constructeur de la classe Noeud
     * @param context
     */
    public Node(Context context, float x, float y, String numNoeud){
        //super(context);
        this.context = context;
        this.numNoeud = numNoeud;
        this.nameNoeud = String.valueOf(numNoeud);
        this.cordX = x;
        this.cordY = y+7;
        coulIntern = ContextCompat.getColor(context, R.color.colorBlueNoeud); //Couleur bleue interne du noeud
        coulCont = ContextCompat.getColor(context, R.color.colorGreyBord); //Couleur grise externe du noeud

        pMilieu = new PointF();

        if(this.nameNoeud.length() <= 4) {
            rectFInt = new RectF(x - 40, y - 40, x + 40, y + 40);
            rectFExt = new RectF(x - 40, y - 40, x + 40, y + 40);

            pMilieu.x = (x-40) +(((x+40)-(x-40))/2);
            pMilieu.y = (y-40) +(((y+40)-(y-40))/2);
        }else{
            System.out.println("plus grand");
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
                    -(x-(20*this.nameNoeud.length())))/2);
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
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
    }


    /**
     * Procédure qui permet de modifier la couleur interne d'un noeud.
     * @param couleur : nouvelle couleur. De type entier.
     */
    public void setCoulIntern(int couleur){
        this.coulIntern = couleur;
    }

    /**
     *  Procédure qui permet de modifier la couleur du contour d'un noeud.
     * @param couleur : nouvelle couleur. De type entier.
     */
    public void setCoulContour(int couleur){
        this.coulCont = couleur;
    }

    public void setPMilieu(PointF pMilieu){
        this.pMilieu = pMilieu;
    }

    /**
     * Fonction qui retourne le couleur interne du noeud.
     * @return coulIntern : de type entier.
     */
    public int getCoulIntern(){
        return this.coulIntern;
    }

    /**
     * Fonction qui retourne la couleur du contour du noeud.
     * @return coulCont : de type entier.
     */
    public int getCoulCont(){
        return this.coulCont;
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
    public RectF getIntRectF(){
        return this.rectFInt;
    }

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
     * Fonction qui retourne le numéro du noeud.
     * @return numNoeud : de type entier.
     */
    public String getNumNoeud(){
        return this.numNoeud;
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
     *
     * @return
     */
    public PointF getPMilieu(){
        return this.pMilieu;
    }

    /**@Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //canvas.drawOval(this.circleExt, pCont);
        //canvas.drawOval(this.circleInt, pInt);
        canvas.drawPath(contour,pCont);
        canvas.drawPath(intern,pInt);
    }*/

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
                ", numNoeud='" + numNoeud + '\'' +
                ", nameNoeud='" + nameNoeud + '\'' +
                ", cordX=" + cordX +
                ", cordY=" + cordY +
                ", pMilieu=" + pMilieu +
                '}';
    }

}
