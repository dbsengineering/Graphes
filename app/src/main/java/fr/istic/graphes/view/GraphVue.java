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
 *		DateModify : ...... 24/10/2017								*
 *******************************************************************/
package fr.istic.graphes.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
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
    public boolean single, blocked, blockedArc,btnClick;
    public String m_Text;
    private float initialTouchX, initialTouchY;
    private int numNoeud;

    private AlertDialog dlMenuNode;
    private AlertDialog dlSizeNode;
    private  Dialog dlCoul;



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
        blockedArc = false;
        btnClick = false;


        //Récupération de la taille de l'écran
        int sizeH = getResources().getDisplayMetrics().heightPixels;
        int sizeW = getResources().getDisplayMetrics().widthPixels;

        //Insertion des noeuds
        int eCx;
        int eCy = (sizeH/3)/2;
        numNoeud = 1;
        for (int i = 0; i < 3; i++) {
            eCx = (sizeW/3)/2;
            for (int j = 0; j < 3; j++){
                lstNode.add(new Node(this.context, eCx, eCy, String.valueOf(numNoeud), numNoeud));
                eCx += sizeW/3;
                numNoeud++;
            }
            eCy += sizeH/4;
        }
    }

    //Declare this flag globally
    boolean goneFlag = false;
    //Put this into the class
    final Handler handlerArc = new Handler();
    final Handler handlerNode = new Handler();
    final Handler handlerNColor = new Handler();

    Runnable mLongPressedAddNode = new Runnable() {
        public void run() {
            goneFlag = true;
            addNode();
        }
    };

    Runnable mLongPressMenuNode = new Runnable(){
        public void run(){
            goneFlag = true;
            afficheMenuNode();
        }
    };


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
        if(!blockedArc) {
            if (startX != 0 && startY != 0) {
                canvas.drawLine(startX, startY, endX, endY, pPaint);
            }
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
     * Procédure qui commence à dessiner un arc
     * @param pointF
     */
    private void startTouchDrawArc(PointF pointF, Node nodeStart){
        this.startX = pointF.x;
        this.startY = pointF.y;
        this.nodeStart = nodeStart;
    }

    private void startTouchModifyArc(PointF pointF, Arc arc){
        this.startX = pointF.x;
        this.startY = pointF.y;
        this.arc = arc;
    }


    /**
     *
     * @param x
     * @param y
     */
    private void moveTouchArc(float x, float y) {
        if(x != startX && y != startY) {
            endX = x;
            endY = y;
        }
    }

    private void moveModTouchArc(float x , float y){
        endX = x;
        endY = y;
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

        /**
         * click -> options noeud
         * 1 move direct -> creation arc
         * 2 secondes d'attente -> active move noeud
         */
        if(!btnClick) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    //Si la liste des noeuds n'est pas vide, on vérifie qu'on touche un noeud
                    //pour soit , le déplacer, soit ajouter un arc
                    //Sinon on crée un noeud
                    if (lstNode.size() != 0) {
                       // Node n = new Node();
                        Node n = touchNode(x, y);
                        if (n != null) {
                            handlerNColor.postDelayed(mLongPressMenuNode, 2000);//delais pour afficher le menu du noeud

                            startTouchDrawArc(n.getPMilieu(), n);
                            if ((Math.abs(x - event.getRawX()) < 5) && (Math.abs(y - event.getRawY()) < 5)) {

                            } else {

                            }
                        } else {
                            nX = x;
                            nY = y;
                            handlerNode.postDelayed(mLongPressedAddNode, 2000);//delais pour ajouter un noeud
                        }
                        Arc a = new Arc();
                        a = touchArc(x, y);
                        if (a != null) {
                            this.blockedArc = true;
                            startTouchModifyArc(a.getpMilieu(), a);
                        }
                    } else {
                        nX = x;
                        nY = y;
                        handlerNode.postDelayed(mLongPressedAddNode, 2000);//delais pour ajouter un noeud
                    }
                    invalidate();
                    break;

                case MotionEvent.ACTION_MOVE:
                    //Tolérance si mover
                    if (Math.pow(x - (int) event.getRawX(), 2) > Math.pow(5, 2) || Math.pow(y - (int) event.getRawY(), 2) > Math.pow(5, 2)) {
                        //handlerNColor.removeCallbacks(mLongPressNColor);
                    }
                    //handlerNColor.removeCallbacks(mLongPressNColor);

                    moveTouchArc(x, y);

                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    handlerNode.removeCallbacks(mLongPressedAddNode);//détruire le handler d'ajout de noeud
                    handlerNColor.removeCallbacks(mLongPressMenuNode);// Détruire le handler du menu noeud
                    if (!this.blockedArc) {
                        Node n = touchNode(x, y);
                        if (n != null) {
                            upTouch(n.getPMilieu(), n);
                        } else {
                            startX = 0;
                            startY = 0;
                            endX = 0;
                            endY = 0;
                        }
                    } else {
                        PointF point = new PointF(endX, endY);

                        this.arc.setpMilieu(point);
                        this.blockedArc = false;
                    }
                    invalidate();
                    break;
            }
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
     * Fonction qui permet de savoir si on touche l'étiquette
     * d'un arc. Retourne l'arc touché sinon null.
     * @param x
     * @param y
     * @return Arc
     */
    private Arc touchArc(float x , float y){
        for(Arc a : lstArc){
            if(a.getRectangle().contains(x, y) && x != 0 && y != 0){
                return a;
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

            final EditText txtName = (EditText) mView.findViewById(R.id.editTxtArc); // EditText

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
                            if(numNoeud != 0 ) {
                                numNoeud ++;
                                lstNode.add(new Node(context, nX, nY, txtName.getText().toString(), numNoeud));

                            }else{
                                lstNode.add(new Node(context, nX, nY, txtName.getText().toString(), numNoeud=1));
                            }
                            invalidate();
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

            alertDialog.setTitle("Créer un noeud");

            // Montrer le dialogBox
            alertDialog.show();
        }
        single = false;
    }

    public void onBackPress(){
        btnClick = false;
        single = false;
    }


    /**
     * Procédure qui permet d'afficher le menu du noeuds touché.
     */
    private void afficheMenuNode(){

        btnClick = true;
        //mNode = new MenuNode();
        //btnClick = mNode.showDialMenu(context);
        showDialMenu(context);
    }

    /**
     *
     * @param nbNode
     */
    public void setNbNode(int nbNode){
        this.nbNode = nbNode;
    }


    /**
     * Procédure qui permet de lancer un AlertDialog pour le changement de couleur d'un noeud.
     */
    public void clicMenuCoul(){
        dlMenuNode.dismiss();
        dlCoul = new Dialog(context);
        dlCoul.setTitle("Couleur");
        dlCoul.setContentView(R.layout.changecoul);
        dlCoul.show();
    }

    /**
     * Procédure de suppression d'un noeud
     */
    public void clicNodeSupp(){
        dlMenuNode.dismiss();
        Iterator itArc = lstArc.iterator();
        while(itArc.hasNext()){
            Arc arc = (Arc) itArc.next();
            if(arc.getNodeStart() == nodeStart || arc.getNodeEnd() == nodeStart){
                itArc.remove();
            }
        }
        lstNode.remove(nodeStart);
        invalidate();
        btnClick = false;
    }

    /**
     * Procédure qui permet d'afficher un AlertDialog sur le click du menu Font.
     */
    public void clicFont(){
        dlMenuNode.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.messageboxnode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText txtName = (EditText) mView.findViewById(R.id.editTxtNode);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Modifier Texte Noeud", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        nodeStart.setNameNoeud(txtName.getText().toString());
                        invalidate();
                        btnClick = false;
                    }
                })
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                btnClick = false;
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        // Afficher directement le clavier
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setTitle("Modifier un noeud");

        // Montrer le dialogBox
        alertDialog.show();
    }


    /**
     * Procédure qui affiche un AlertDialogue pour modifier la taille du noeud.
     */
    public void clicSize(){
        dlMenuNode.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.sizenode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final NumberPicker np = (NumberPicker) mView.findViewById(R.id.np);//Récupération du NumberPicker
        int taille = nodeStart.getSize();
        np.setMinValue(nodeStart.getSize());
        np.setMaxValue(200);
        np.setValue(nodeStart.getSize());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        nodeStart.setSize(np.getValue());
                        btnClick = false;
                        invalidate();
                    }
                })
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                btnClick = false;
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        //Titre de l'AlertDialog
        alertDialog.setTitle("Modifier taille du noeud");

        // Montrer le dialogBox
        alertDialog.show();
    }

    /**
     *  Procédure qui permet d'assigner la couleur d'un noeud suivant le choix de couleur
     * @param couleur
     */
    public void clicCoul(String couleur){
        switch (couleur){
            case "rouge":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorRedNoeud));
                break;
            case "vert":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorGreenNoeud));
                break;
            case "jaune":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorYellowNoeud));
                nodeStart.setCoulFont(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "orange":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorOrangeNoeud));
                break;
            case "rose":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorRoseNoeud));
                nodeStart.setCoulFont(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "cyan":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.cyan));
                nodeStart.setCoulFont(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            case "noir":
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorBlack));
                break;
            default:
                nodeStart.setCoulIntern(ContextCompat.getColor(context, R.color.colorBlueNoeud));

        }
        dlCoul.dismiss();
        invalidate();// Application des changement sur la vue
        btnClick = false;
    }

    /**
     * Fonction qui affiche le menu d'un noeud.
     * @param context
     * @return
     */
    public boolean showDialMenu(Context context){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.optsnode, null);
        AlertDialog.Builder dlgBuild = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            btnClick = false;
                            dialog.cancel();
                        }
                        return false;
                    }
                })
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                btnClick = false;
                                dialogBox.cancel();
                            }
                        });
        dlgBuild.setView(mView);
        dlMenuNode = dlgBuild.create();
        dlMenuNode.setTitle("Options Noeud");
        dlMenuNode.show();
        return false;
    }


}
