/********************************************************************
 * 						Classe Arc      							*
 * 			     Objet physique et graphique					    *
 * 					       d'un arc		        					*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ezziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 16/10/2017								*
 *******************************************************************/
package fr.istic.graphes.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

/**
 * Created by cavronjeremy on 04/10/2017.
 */
public class Arc {

    //--- Déclaration des propriétées ---
    private PointF pStart, pEnd;
    private Paint lignePaint, rectPaint, txtPaint, arrowPaint;
    private Path arc, arrow;
    private RectF rectangle;
    private static final float TOUCH_TOLERANCE = 4;
    private String txtArc;
    private Node nodeStart, nodeEnd;
    private PointF pMilieu;
    private float x,y;

    public Arc(){}


    /**
     * Constructeur 1 de la classe Arc.
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param nodeStart
     * @param nodeEnd
     * @param txtArc
     */
    public Arc(float startX, float startY, float endX, float endY,
               Node nodeStart, Node nodeEnd, String txtArc){
        init(startX, startY, endX, endY, nodeStart, nodeEnd, txtArc);

    }

    /**
     * Constructeur 2 de la classe Arc.
     * @param startX
     * @param startY
     */
    public Arc(float startX, float startY){

        this.pStart = new PointF(startX,startY);
    }

    /**
     * Procédure d'initialisation de l'arc.
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param nodeStart
     * @param nodeEnd
     * @param txtArc
     */
    private void init(float startX, float startY, float endX, float endY,
                      Node nodeStart, Node nodeEnd, String txtArc){
        this.pStart = new PointF(startX,startY);
        this.pEnd = new PointF(endX,endY);
        this.txtArc = txtArc;
        this.nodeStart = nodeStart;
        this.nodeEnd = nodeEnd;

        this.lignePaint = new Paint();
        this.lignePaint.setColor(Color.RED);
        this.lignePaint.setAntiAlias(true);
        this.lignePaint.setStyle(Paint.Style.STROKE);
        this.lignePaint.setStrokeWidth(5f);

        this.arrowPaint = new Paint();
        this.arrowPaint.setColor(Color.RED);
        this.arrowPaint.setAntiAlias(true);
        this.arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.rectPaint = new Paint();
        this.rectPaint.setColor(Color.WHITE);
        this.rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.rectPaint.setAntiAlias(true);

        this.txtPaint = new TextPaint();
        this.txtPaint.setTextSize(30);
        this.txtPaint.setTextAlign(Paint.Align.CENTER);
        this.txtPaint.setColor(Color.BLACK);
        this.txtPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        this.arc = new Path();
        this.arrow = new Path();

        //Test si xDépart < xFin et yDépart < yFin et inversement
        if(this.pStart.x <= this.pEnd.x ) {
            x = this.pStart.x + (Math.abs(this.pStart.x - this.pEnd.x) / 2);//Centrer x milieu de l'arc
        }else{
            x = this.pEnd.x + (Math.abs(this.pStart.x - this.pEnd.x) / 2);//Centrer x milieu de l'arc
        }
        if(this.pStart.y <= this.pEnd.y){
            y = this.pStart.y + (Math.abs(this.pStart.y - this.pEnd.y) / 2);//Centrer y milieu de l'arc
        }else{
            y = this.pEnd.y + (Math.abs(this.pStart.y - this.pEnd.y) / 2);//Centrer y milieu de l'arc

        }
        this.pMilieu = new PointF(0,0);
    }

    //--- Getters ---

    /**
     *
     * @return
     */
    public PointF getPointStart() {
        return this.pStart;
    }

    /**
     *
     * @return
     */
    public PointF getPointEnd() {
        return this.pEnd;
    }


    /**
     * Fonction qui retourne le rectangle de l'étiquette
     * de l'arc
     * @return
     */
    public RectF getRectangle(){
        return this.rectangle;
    }

    /**
     * Fonction qui retourne le milieu de l'arc
     * @return
     */
    public PointF getpMilieu(){
        return this.pMilieu;
    }

    public void setpMilieu(PointF point){
        this.pMilieu = point;
    }

    /**
     * Fonction qui retourne le nom de l'arc.
     * @return txtArc. De type String.
     */
    public String getTxtArc(){
        return this.txtArc;
    }

    public Node getNodeStart(){
        return this.nodeStart;
    }

    public Node getNodeEnd(){
        return this.nodeEnd;
    }

    //--- Setters ---

    /**
     *
     * @param pStart
     */
    public void setPointStart(PointF pStart) {
        this.pStart = pStart;
    }



    /**
     *
     * @param pEnd
     */
    public void setPointEnd(PointF pEnd) {
        this.pEnd = pEnd;
    }

    /**
     * Procédure qui permet de modifier le nom de l'arc.
     * @param txtArc : est le nome de l'arc. De type String.
     */
    public void setTxtArc(String txtArc){
        this.txtArc = txtArc;
    }

    /**
     * Procédure qui dessine l'arc.
     * @param canvas
     */
    public void drawArc(Canvas canvas){

        //System.out.println("bbbb");

        // --- Création de la flêche
        float deltaX =   pEnd.x-pStart.x;
        float deltaY =   pEnd.y-pStart.y;
        //float frac = (float) 0.1;
        int ARROWHEAD = 50;
        float sideZ = (float) Math.sqrt(deltaX*deltaX+deltaY*deltaY);
        float frac = ARROWHEAD < sideZ ? ARROWHEAD / sideZ: 1.0f;

        float point_x_1 = pStart.x + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = pStart.y + (float) ((1 - frac) * deltaY - frac * deltaX);

        float point_x_3 = pStart.x + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = pStart.y + (float) ((1 - frac) * deltaY + frac * deltaX);

        float point_x_2 = pEnd.x;
        float point_y_2 = pEnd.y;


       //pMilieu = new PointF(x,y);

        this.arrow.moveTo(point_x_1, point_y_1);
        this.arrow.lineTo(point_x_2, point_y_2);
        this.arrow.lineTo(point_x_3, point_y_3);
        this.arrow.lineTo(point_x_1, point_y_1);
        //this.arrow.lineTo(point_x_1, point_y_1);

        canvas.drawPath(this.arrow, this.arrowPaint);


        //tracer arc
        this.arc.moveTo(this.pStart.x,this.pStart.y);
        if(this.pMilieu.x == 0 && this.pMilieu.y == 0){
            this.arc.quadTo(x+50, y+50, this.pEnd.x, this.pEnd.y);
        }else{
            this.arc.quadTo(this.pMilieu.x+50, this.pMilieu.y+50, this.pEnd.x, this.pEnd.y);
        }
        this.arc.quadTo(x+50, y+50, this.pEnd.x, this.pEnd.y);

        //Récupération du milieu de l'arc
        PathMeasure pM = new PathMeasure(this.arc, false);//Donne longueur avec chaque point de l'arc
        float[] tab  = new float[2];
        pM.getPosTan(pM.getLength()/2, tab, null);
        this.pMilieu.x = tab[0];
        this.pMilieu.y = tab[1];


        //this.arc.lineTo(this.pEnd.x, this.pEnd.y);
        //this.arc.lineTo(this.pEnd.x-50, this.pEnd.y-50);
        //this.arc.lineTo(this.pEnd.x, this.pEnd.y);
        //this.arc.lineTo(this.pEnd.x+50, this.pEnd.y-50);

        canvas.drawPath(this.arc, this.lignePaint);
        //tracer cadre du texte
        this.rectangle = new RectF(this.pMilieu.x-this.txtArc.length()*15, this.pMilieu.y - 30,
                this.pMilieu.x + this.txtArc.length()*15, this.pMilieu.y + 10);
        canvas.drawRoundRect(rectangle,6,6,rectPaint);
        //tracer texte
        canvas.drawText(this.txtArc, this.pMilieu.x, this.pMilieu.y, this.txtPaint);
    }



    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Arc{" +
                "pStart.x = " + this.pStart.x +
                ", pStart.y = " + this.pStart.y +
                ", pEnd.x = " + this.pEnd.x +
                ", pEnd.y = " + this.pEnd.y +
                ", txtArc = "+ this.txtArc +
                '}';
    }

}
