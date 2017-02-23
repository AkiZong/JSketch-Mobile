package example.com.a3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MainActivity extends Activity {

    static public Model model = new Model();
    public Buttons buttons;
    public DrawCanvas drawCanvas;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        buttons = new Buttons(this, model);
        buttons.addButton();
        buttons.addController();
        Log.d("MVC", "onCreate");
        drawCanvas =(DrawCanvas) this.findViewById(R.id.myview);
        drawCanvas.addModel(model);
        model.addObserver(drawCanvas);
        model.addObserver(buttons);
        buttons.lt1_s = true;
        buttons.lt1.setBackgroundResource(R.drawable.thickone_sel);
        buttons.red_s = true;
        buttons.red.setBackgroundResource(R.drawable.red_sel);
        model.buttons = buttons;
    }

    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

    }
}
