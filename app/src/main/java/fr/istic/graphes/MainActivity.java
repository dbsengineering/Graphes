package fr.istic.graphes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fr.istic.graphes.component.GraphVue;

import static fr.istic.graphes.R.id.canvas;

public class MainActivity extends AppCompatActivity {

    private GraphVue graphVue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphVue = (GraphVue) findViewById(canvas);
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
