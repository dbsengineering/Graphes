/********************************************************************
 * 						Classe MainActivity							*
 * 			            Initialise la vue					        *
 * 					Départ de l'application							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ezziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 16/10/2017								*
 *******************************************************************/
package fr.istic.graphes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import fr.istic.graphes.view.GraphVue;
import static fr.istic.graphes.R.id.canvas;

public class MainActivity extends AppCompatActivity {

    private GraphVue graphVue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphVue = (GraphVue) findViewById(canvas);
        graphVue.setNbNode(9);
    }

    /**
     *
     * @param v
     */
    public void clearArcs(View v){
        graphVue.clearArcs();
    }

    /**
     *
     * @param v
     */
    public void clearNodes(View v){
        graphVue.clearNodes();
    }
}
