package fr.istic.graphes.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private Paint lignePaint, rectPaint, txtPaint;
    private Path arc;
    private RectF rectangle;
    private static final float TOUCH_TOLERANCE = 4;
    private String txtArc;
    private Node nodeStart, nodeEnd;


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
    }

    /**
     * Constructeur 2 de la classe Arc.
     * @param startX
     * @param startY
     */
    public Arc(float startX, float startY){
        this.pStart = new PointF(startX,startY);
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
     * Fonction qui retourne le nom de l'arc.
     * @return txtArc. De type String.
     */
    public String getTxtArc(){
        return this.txtArc;
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
     *
     * @param canvas
     */
    public void getArc(Canvas canvas){
        float x, y;//Coordonnées milieu arc
        //Test si xDépart < xFin et yDépart < yFin et inversement
        if(this.pStart.x <= this.pEnd.x) {
            x = this.pStart.x + (Math.abs(this.pStart.x - this.pEnd.x) / 2);//Centrer x milieu de l'arc
        }else{
            x = this.pEnd.x + (Math.abs(this.pStart.x - this.pEnd.x) / 2);//Centrer x milieu de l'arc
        }
        if(this.pStart.y <= this.pEnd.y){
            y = this.pStart.y + (Math.abs(this.pStart.y - this.pEnd.y) / 2);//Centrer y milieu de l'arc
        }else{
            y = this.pEnd.y + (Math.abs(this.pStart.y - this.pEnd.y) / 2);//Centrer y milieu de l'arc
        }


        //tracer arc
        this.arc.moveTo(this.pStart.x,this.pStart.y);
        this.arc.quadTo(x+50, y+50, this.pEnd.x, this.pEnd.y);
        //this.arc.lineTo(this.pEnd.x, this.pEnd.y);
        //this.arc.lineTo(this.pEnd.x-50, this.pEnd.y-50);
        //this.arc.lineTo(this.pEnd.x, this.pEnd.y);
        //this.arc.lineTo(this.pEnd.x+50, this.pEnd.y-50);
        canvas.drawPath(this.arc, this.lignePaint);
        //tracer cadre du texte
        this.rectangle = new RectF(x-this.txtArc.length()*20, y - 30, x + this.txtArc.length()*20, y + 10);
        canvas.drawRoundRect(rectangle,6,6,rectPaint);
        //tracer texte
        canvas.drawText(this.txtArc, x, y, this.txtPaint);
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
