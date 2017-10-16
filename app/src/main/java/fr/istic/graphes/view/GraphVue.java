/********************************************************************
 * 						Classe GrapheVue							*
 * 			            Vue sur le graphe					        *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ezziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 16/10/2017								*
 *******************************************************************/
package fr.istic.graphes.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import fr.istic.graphes.R;
import fr.istic.graphes.component.Arc;
import fr.istic.graphes.component.Node;

/**
 * Created by Cavron Jérémy on 04/10/2017.
 */
public class GraphVue extends View  {

    //--- Déclaration des propriétées ---

    private Context context;
    private Bitmap bMap;
    private Canvas canvas;
    public int width;
    public int height;
    private List<Node> lstNode;
    private List<Arc> lstArc;
    private int nbNode;
    private float startX, startY, endX, endY, nX, nY;
    private Node nodeStart, nodeEnd;

    private Arc arc;
    private Path path;
    private Paint pPaint;
    private static final float TOUCH_TOLERANCE = 4;
    public boolean single, blocked;
    public String m_Text;


    /**
     * Constructeur de la classe
     * @param context
     * @param attrs
     */
    public GraphVue(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;

        System.out.println(nbNode);

        nodeStart = new Node();
        nodeEnd = new Node();

        nbNode = 9;
        lstNode = new ArrayList<Node>();
        lstArc = new ArrayList<Arc>();

        this.pPaint = new Paint();
        this.pPaint.setColor(Color.RED);
        this.pPaint.setAntiAlias(true);
        this.pPaint.setStyle(Paint.Style.STROKE);
        this.pPaint.setStrokeWidth(5f);


        path = new Path();
        single = true;
        blocked = false;


        //Récupération de la taille de l'écran
        int sizeH = getResources().getDisplayMetrics().heightPixels;
        int sizeW = getResources().getDisplayMetrics().widthPixels;

        //Insertion des noeuds
        int eCx;
        int eCy = (sizeH/3)/2;
        int numNoeud = 1;
        for (int i = 0; i < 3; i++) {
            eCx = (sizeW/3)/2;
            for (int j = 0; j < 3; j++){
                lstNode.add(new Node(this.context, eCx, eCy, String.valueOf(numNoeud)));
                eCx += sizeW/3;
                numNoeud++;
            }
            eCy += sizeH/3;
        }
    }

    //Declare this flag globally
    boolean goneFlag = false;
    //Put this into the class
    final Handler handlerArc = new Handler();
    final Handler handlerNode = new Handler();

    /*Runnable mLongPressed = new Runnable() {
        public void run() {
            goneFlag = true;

            if(single) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                View mView = layoutInflaterAndroid.inflate(R.layout.messageboxnode, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                final EditText txtName = (EditText) mView.findViewById(R.id.editTxtNode);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Créer Noeud", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                lstNode.add(new Node(context, nX, nY, txtName.getText().toString()));
                                single = true;
                            }
                        })

                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                        single = true;
                                    }
                                });

                // Assigner le builderBox au dialogBox.
                final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

                // Afficher directement le clavier
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                // Montrer le dialogBox
                alertDialog.show();
            }
            single = false;

            Log.w("Noeud : ", String.valueOf("long click"));
        }
    };*/


    /**
     * Procédure qui permet de changer la taille de l'image dans le canvas.
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        bMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bMap);
    }

    /**
     * Procédure qui permet d'effacer le canvas.
     */
    public void clearArcs(){
        if(lstArc.size() != 0) {
            lstArc.clear();
        }
        path.reset();
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
        invalidate();
    }

    /**
     * Procédure qui permet d'effacer le canvas.
     */
    public void clearNodes(){
        if(lstNode.size() != 0) {
            lstNode.clear();
        }
        clearArcs();
        invalidate();
    }

    /**
     * Procédure qui dessine les arcs et les noeuds sur le canvas
     * @param canvas : type Canvas
     */
    @Override
    protected void onDraw(Canvas canvas){

        //On dessine le trait courant
        if(startX != 0 && startY != 0) {
            canvas.drawLine(startX, startY, endX, endY, pPaint);
        }

        // Si la liste d'arc est égale à 0 alors on affiche pas les arcs
        //Sinon on affiche les arcs
        if(lstArc.size() != 0){
            Iterator<Arc> itArc = lstArc.iterator();
            while(itArc.hasNext()){
                itArc.next().getArc(canvas);
            }
        }
        // Si la liste de noeuds est égale à 0 alors on affiche pas les noeuds
        //Sinon on affiche les noeuds
        if(lstNode.size() != 0) {
            Iterator<Node> itN = lstNode.iterator();
            while (itN.hasNext()) {
                itN.next().getNoeud(canvas);
            }
        }
    }

    /**
     *
     * @param pointF
     */
    private void startTouch(PointF pointF, Node nodeStart){
        this.startX = pointF.x;
        this.startY = pointF.y;
        this.nodeStart = nodeStart;
    }

    /**
     *
     * @param x
     * @param y
     */
    private void moveTouch(float x, float y) {
        if(x != startX && y != startY) {
            endX = x;
            endY = y;
        }
    }

    /**
     *
     * @param pointF
     */
    private void upTouch(PointF pointF, Node nodeEnd){
        this.endX = pointF.x;
        this.endY = pointF.y;
        this.nodeEnd = nodeEnd;
        //Demander le nom de l'arc
        setTextArc(startX, startY, pointF.x, pointF.y, this.nodeStart, this.nodeEnd);
        startX = 0;
        startY = 0;
    }

    /**
     * Fonction évennement sur l'écran.
     * @param event
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //Si la liste des noeuds n'est pas vide, on vérifie qu'on touche un noeud
                //pour soit , le déplacer, soit ajouter un arc
                //Sinon on crée un noeud
                if (lstNode.size() != 0) {
                    Node n = new Node();
                    n = touchNode(x, y);
                    if (n != null) {

                        startTouch(n.getPMilieu(), n);
                            /*for (Node n : lstNode) {

                                if (n.getExtRectF().contains(x, y) && x != 0 && y != 0) {

                                    startTouch(n.getPMilieu(), n);
                                    System.out.println("dedans");
                                    //return false;

                                }*/ /*else {

                                    nX = x;
                                    nY = y;
                                    handlerNode.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            blocked = false;
                                            //setNode();
                                        }
                                    }, 1000);
                                    //handler.postDelayed(mLongPressed, 3000);
                                }*/
                    } else {

                        nX = x;
                        nY = y;
                        handlerNode.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //blocked = false;
                                addNode();
                            }
                        }, 2000);
                        //handler.postDelayed(mLongPressed, 3000);
                    }
                } else {
                    nX = x;
                    nY = y;
                    handlerNode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            blocked = false;
                            addNode();

                        }
                    }, 1000);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                //handler.removeCallbacks(mLongPressed);

                moveTouch(x, y);

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Node n = new Node();
                n = touchNode(x, y);
                if (n != null) {
                    upTouch(n.getPMilieu(), n);
                } else {
                    startX = 0;
                    startY = 0;
                    endX = 0;
                    endY = 0;
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * Fonction qui permet de savoir si on touche un noeud.
     * Retourne le noeud touché sinon null
     * @param x
     * @param y
     * @return Node
     */
    private Node touchNode(float x, float y){
        for (Node n : lstNode) {
            if (n.getExtRectF().contains(x, y) && x != 0 && y != 0) {
                return n;
            }
        }
        return null;
    }

    /**
     * Procédure qui demande le nom d'un arc à insérer
     */
    private void setTextArc(final float debX, final float debY, final float finX, final float finY,
                            final Node nodeStart, final Node nodeEnd){

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.messageboxarc, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);

            alertDialogBuilderUserInput.setView(mView);
            //EditText
            final EditText txtName = (EditText) mView.findViewById(R.id.editTxtArc);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            single = true;
                            if (!txtName.getText().toString().isEmpty()) {
                                lstArc.add(new Arc(debX, debY, finX, finY,
                                        nodeStart, nodeEnd, txtName.getText().toString()));
                            } else {
                                Toast.makeText(context, String.valueOf("Vous avez oublié de nommer l'arc"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })

                    .setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    single = true;
                                    dialogBox.cancel();

                                }
                            });

            // Assigner le builderBox au dialogBox.
            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

            // Afficher directement le clavier
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            // Montrer le dialogBox
            alertDialog.show();
    }


    private void addNode(){
        if(single) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.messageboxnode, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
            alertDialogBuilderUserInput.setView(mView);

            final EditText txtName = (EditText) mView.findViewById(R.id.editTxtNode);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Créer Noeud", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            lstNode.add(new Node(context, nX, nY, txtName.getText().toString()));
                            single = true;
                        }
                    })
                    .setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                    single = true;
                                }
                            });
            // Assigner le builderBox au dialogBox.
            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

            // Afficher directement le clavier
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            // Montrer le dialogBox
            alertDialog.show();
        }
        single = false;
    }

    public void setNbNode(int nbNode){
        this.nbNode = nbNode;
    }


}
