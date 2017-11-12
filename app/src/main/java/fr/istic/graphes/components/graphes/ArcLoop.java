/********************************************************************
 * 						    Classe ArcLoop     						*
 * 			                 Objet ArcLoop                          *
 * 			                 Extends de Arc                         *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ez Ziraiy Nada			*
 *		DateStart : ....... 09/09/2017								*
 *		DateModify : ...... 12/11/2017								*
 *******************************************************************/
package fr.istic.graphes.components.graphes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 *
 */
public class ArcLoop extends Arc {

    //--- Déclaration des propriétées ---
    private float x;
    private float centerX;
    private float centerY;
    private float radius;
    private float xStop, yStop, xStart, yStart;

    /**
     * Constructeur de la classe.
     * @param nodeStart
     * @param nodeEnd
     * @param nameArc
     * @param id
     */
    public ArcLoop(Node nodeStart, Node nodeEnd, String nameArc, int id){
        super(nodeStart, nodeEnd, nameArc, id);
        this.centerX = 0;
        this.centerY = 0;
        this.xStop = 0;
        this.yStop = 0;
        initArcLoop();
    }

    /**
     * Procédure d'initialisation
     */
    public void initArcLoop(){
        super.pthArc = new Path();
        super.pMiddle = new PointF();
        super.pthArrow = new Path();

        //Modification de la boucle
        if(this.xStop != 0 && this.yStop != 0) {

            centerX = (nodeStart.getpMiddle().x + xStop)/2;
            centerY = (nodeStart.getpMiddle().y + yStop)/2;
            this.radius = (float) Math.sqrt((xStop - nodeStart.getpMiddle().x)
                    * (xStop - nodeStart.getpMiddle().x) + (yStop - nodeStart.getpMiddle().y)
                    * (yStop - nodeStart.getpMiddle().y));
            super.pthArc.addCircle(centerX, centerY, radius / 2, Path.Direction.CCW);
            super.rectTxt = new RectF(xStop - this.tailleEti * this.sizeL,
                    yStop - (30*(this.sizeL/10)),
                    xStop + this.tailleEti * this.sizeL, yStop + this.sizeL);
            //this.pM = new PathMeasure(this.pthArc, false);//Donne longueur avec chaque point de l'arc
            //this.tab = new float[2];
            //this.pM.getPosTan(pM.getLength() / 2, tab, null);
            super.pMiddle.x = super.rectTxt.centerX();
            super.pMiddle.y = super.rectTxt.centerY()+10;
        }else{
            //Création de la boucle
            float radius = nodeStart.getRectFOut().width() / 2;
            float x = (nodeStart.getRectFOut().width() / 2) + nodeStart.getpMiddle().x;
            super.pMiddle.x = (radius + x);
            super.pMiddle.y = nodeStart.getpMiddle().y;
            super.pthArc.addCircle(x,
                    nodeStart.getpMiddle().y, radius , Path.Direction.CCW);
            if (super.tailleEti == 0) {
                super.tailleEti = 2;
            }
            super.rectTxt = new RectF(super.pMiddle.x - this.tailleEti * this.sizeL,
                    nodeStart.getpMiddle().y - (30*(this.sizeL/10)),
                    super.pMiddle.x + this.tailleEti * this.sizeL, nodeStart.getpMiddle().y + this.sizeL);

        }
    }

    //--- Getters ---

    /**
     * Fonction qui retourne le Path de l'arc
     * @return
     */
    @Override
    public Path getPthArc(){
        initArcLoop();
        return super.pthArc;
    }

    /**
     * Fonction qui retourne le milieu de l'étiiquette.
     * @return
     */
    @Override
    public PointF getpMiddle(){
        return pMiddle;
    }


//--- Setters ---

    /**
     * Procédure qui modifie l'arc.
     * @param xStart : y point de départ.
     * @param yStart : x point de départ.
     * @param xStop : x point d'arrivé.
     * @param yStop : y point d'arrivé.
     */
    @Override
    public void setPath(float xStart, float yStart, float xStop, float yStop){
        this.xStop = xStop;
        this.yStop = yStop;
        this.xStart = nodeStart.getpMiddle().x;
        this.yStart = nodeStart.getpMiddle().y;
    }
}
