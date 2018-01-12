/********************************************************************
 * 						Classe DrawableGraph						*
 * 			                Vue du graphe   					    *
 * 			        Permet de dessiner le graphe                    *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy               			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 12/11/2017								*
 *******************************************************************/
package fr.istic.graphes.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import bzh.dbs.graph.R;
import bzh.dbs.graph.components.graphes.Arc;
import bzh.dbs.graph.components.graphes.Graph;
import bzh.dbs.graph.components.graphes.Node;

/**
 *
 */
public class DrawableGraph extends View {

    //--- Déclaration des propriétées ---
    private Paint paintBackground, paintArc, paintNode, paintNodeExt, rectPaint, paintArrow;
    private TextPaint txtPaintArc, paintTxtNode;
    private Graph graph;
    private float height, width;
    private Context context;
    private Bitmap bmp;
    private Canvas canvas;
    private PointF pStart, pEnd;
    private Path pthArc, pthArrow; // Path de l'arc, Arrow

    /**
     * Constructeur de la classe.
     * @param context
     */
    public DrawableGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSaveEnabled(false);
        init(context);
    }

    /**
     * Procédure d'initialisation de la vue du graphe
     */
    private void init(Context context){
        //Initialisation
        this.context = context;
        this.height = context.getResources().getDisplayMetrics().heightPixels;//getBounds().height();
        this.width = context.getResources().getDisplayMetrics().widthPixels;//getBounds().width();

        //Paint du Background
        this.paintBackground = new Paint();
        this.paintBackground.setAntiAlias(true);
        this.paintBackground.setShader(new LinearGradient(0, 0, 0, this.height,
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary), Shader.TileMode.MIRROR));
        this.paintBackground.setStyle(Paint.Style.FILL);

        //Paint du noeud intérieur
        this.paintNode = new Paint();
        this.paintNode.setAntiAlias(true); //Anti aliasing interne (courbe plus douce)
        this.paintNode.setStyle(Paint.Style.FILL); //Style rempli
        this.paintNode.setStrokeJoin(Paint.Join.ROUND); //Adoucir le trait

        //Paint du noeud extérieur
        this.paintNodeExt = new Paint();// Nouvel objet Paint pour la couleur du contour
        this.paintNodeExt.setAntiAlias(true); //Anti aliasing externe (courbe plus douce)
        this.paintNodeExt.setStyle(Paint.Style.STROKE); //Style de trait
        this.paintNodeExt.setColor(ContextCompat.getColor(context, R.color.colorDarkGrey)); //Couleur contour
        this.paintNodeExt.setStrokeJoin(Paint.Join.ROUND); //Adoucir le trait
        this.paintNodeExt.setStrokeWidth(8f); //Epaisseur du trait

        //Paint text Node
        this.paintTxtNode = new TextPaint();// Nouvel objet TextPaint pour la couleur du texte
        this.paintTxtNode.setTextSize(30);// Taille du texte
        this.paintTxtNode.setTextAlign(Paint.Align.CENTER);// Alignement centré
        this.paintTxtNode.setTypeface(Typeface.create("Arial", Typeface.BOLD));//Type de texte (Arial, GRAS)

        //Paint Arcs
        this.paintArc = new Paint();
        this.paintArc.setAntiAlias(true);
        this.paintArc.setStyle(Paint.Style.STROKE);
        this.paintArc.setStrokeWidth(8);

        //Paint Arrow
        this.paintArrow = new Paint();
        this.paintArrow.setAntiAlias(true);
        this.paintArrow.setStyle(Paint.Style.FILL);
        this.paintArrow.setStrokeJoin(Paint.Join.ROUND);


        // Paint Rectangle de l'étiquette
        this.rectPaint = new Paint();
        this.rectPaint.setColor(Color.WHITE);
        this.rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.rectPaint.setAntiAlias(true);

        // Paint texte de l'étiquette
        this.txtPaintArc = new TextPaint();
        this.txtPaintArc.setTextSize(30);
        this.txtPaintArc.setTextAlign(Paint.Align.CENTER);
        this.txtPaintArc.setColor(Color.BLACK);
        this.txtPaintArc.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        //Initialisation de la classe Graph (non graphique)
        this.graph = new Graph(this.height, this.width);

        //Initialisation PointF arc
        pStart = new PointF();
        pEnd = new PointF();
    }

    /**
     * Procédure qui permet de changer la taille de l'image dans le canvas.
     * @param w : longueur du canvas
     * @param h : hauteur du canvas
     * @param oldw : ancienne longueur
     * @param oldh : ancienne hauteur
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
    }

    /**
     * Procédure qui dessine
     * @param canvas : surface de dessin
     */
    @Override
    public void onDraw(Canvas canvas) {
        //--- Décoration ---
        //Background
        RectF rect = new RectF(0.0f, 0.0f, this.width, this.height);
        canvas.drawRect(rect, paintBackground);

        //Grille de décoration
        GridPaint gP = new GridPaint(this.height, this.width);
        canvas.drawPath(gP.getPath(),gP.getPaint());
        gP.getTileP(canvas);

        //--- Dessine le graph ---
        //Affiche arc temporaire
        try{
            setColor("rougeArc");
            Path p = new Path();
            this.paintArc.setStrokeWidth(8);
            this.txtPaintArc.setTextSize(30);
            p.moveTo(pStart.x, pStart.y);
            p.quadTo(pStart.x, pStart.y, pEnd.x, pEnd.y);
            canvas.drawPath(p, paintArc);
        }catch(Exception e){
            Log.d("DGraph_onDraw", "arc temporaire");
        }

        //Affiche les Arcs
        if (this.graph.getLstArcs() != null) {
            for (Arc arc : this.graph.getLstArcs()) {
                try {
                    setColor(arc.getColor());
                    this.paintArc.setStrokeWidth(arc.getThickness());
                    this.txtPaintArc.setTextSize(arc.getSizeLabel()*3);
                    this.paintArrow.setStrokeWidth(arc.getThickness());
                    pthArc = new Path();
                    pthArrow = new Path();
                    pthArc = arc.getPthArc();
                    pthArrow = arc.getPthArrow();
                    setColor(arc.getColor());//Chargement de la couleur de l'arc et du texte
                    canvas.drawPath(pthArc, paintArc);
                    canvas.drawPath(pthArrow, paintArrow);
                    canvas.drawRoundRect(arc.getRectTxt(),80,6,rectPaint);
                    canvas.drawText(arc.getNameArc(),arc.getpMiddle().x, arc.getpMiddle().y, txtPaintArc);
                    pthArc = null;
                }catch(Exception e){
                    Log.d("DGraph_OnDraw", "Arc");
                }

            }
        }

        //Affiche les Noeuds
        if (this.graph.getLstNodes() != null) {
            for (Node node : this.graph.getLstNodes()) {
                setColor(node.getColorInt());//Chargement de la couleur du noeud et du texte
                canvas.drawOval(node.getRectFInt(), paintNode);
                canvas.drawOval(node.getRectFOut(), paintNodeExt);
                canvas.drawText(node.getNameNode(), node.getCoordX(), node.getCoordY() + node.getPolice(), paintTxtNode);
            }
        }
    }

    /**
     * Procédure qui dessine un arc temporaire
     * @param pStart : PointF start Arc
     * @param pEnd : PointF end Arc
     */
    public void draw(PointF pStart, PointF pEnd){
        this.pStart = pStart;
        this.pEnd = pEnd;
        invalidate();
    }

    /**
     * Procédure qui initialise le graphe. (utiliser lors du SaveInstanceState)
     * @param graph
     */
    public void setGraph(Graph graph){
        this.graph = graph;
    }

    /**
     * Fonction qui retourne le graphe en cours.
     * @return Graph
     */
    public Graph getGraph(){
        return this.graph;
    }

    public PointF getSize(){
       return new PointF(this.height, this.width);
    }

    public Bitmap getBmp(){
        return this.bmp;
    }

    /**
     * Procédure qui retourne une couleur en entier en donnant un paramètre
     * de couleur en chaîne de caractères.
     * @param color : couleur
     */
    private void setColor(String color){
        switch (color){
            case "rouge":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorRedNoeud));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "vert":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorGreenNoeud));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "jaune":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorYellowNoeud));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "orange":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorOrangeNoeud));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "rose":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorRoseNoeud));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "cyan":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.cyan));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "noir":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "colorMove":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.deep_purple));
                break;
            case "bleu":
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorBlue));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "rougeArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorRedNoeud));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorRedNoeud));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "vertArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorGreenNoeud));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorGreenNoeud));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "jauneArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorYellowNoeud));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorYellowNoeud));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "orangeArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorOrangeNoeud));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorOrangeNoeud));
               //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "roseArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorRoseNoeud));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorRoseNoeud));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "cyanArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.cyan));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.cyan));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "noirArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorBlack));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case "colorMoveArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.deep_purple));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.deep_purple));
                break;
            case "bleuArc":
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorBlue));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorBlue));
                //this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            default:
                this.paintNode.setColor(ContextCompat.getColor(context, R.color.colorBlue));
                this.paintTxtNode.setColor(ContextCompat.getColor(context, R.color.colorWhite));
                this.paintArc.setColor(ContextCompat.getColor(context, R.color.colorRedNoeud));
                this.paintArrow.setColor(ContextCompat.getColor(context, R.color.colorRedNoeud));
        }
    }
}
