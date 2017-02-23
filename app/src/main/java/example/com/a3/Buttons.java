package example.com.a3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.app.Activity;
import android.os.Bundle;

import java.util.Observable;
import java.util.Observer;

public class Buttons implements Observer {
    public Button selector;
    public Button eraser;
    public Button square;
    public Button circle;
    public Button fill;
    public Button red;
    public Button blue;
    public Button green;
    public Button lt1;
    public Button lt2;
    public Button lt3;
    public Button lt4;
    public Button line;
    public Button undo;
    public Activity activity;

    public boolean selector_s = false;
    public boolean eraser_s= false;
    public boolean square_s= false;
    public boolean circle_s= false;
    public boolean fill_s= false;
    public boolean line_s= false;

    public boolean red_s= false;
    public boolean blue_s= false;
    public boolean green_s= false;

    public boolean lt1_s= false;
    public boolean lt2_s= false;
    public boolean lt3_s= false;
    public boolean lt4_s= false;
    public boolean undo_s= false;


    Model model;

    public Buttons(Activity _act, Model m){
        activity = _act;
        model = m;
    }

    public void addButton(){
        line = (Button) activity.findViewById(R.id.line);
        selector = (Button) activity.findViewById(R.id.selector);
        eraser = (Button) activity.findViewById(R.id.eraser);
        square = (Button) activity.findViewById(R.id.square);
        circle = (Button) activity.findViewById(R.id.circle);
        fill = (Button) activity.findViewById(R.id.fill);
        red = (Button) activity.findViewById(R.id.red);
        blue = (Button) activity.findViewById(R.id.blue);
        green = (Button) activity.findViewById(R.id.green);
        lt1 = (Button) activity.findViewById(R.id.thickone);
        lt2 = (Button) activity.findViewById(R.id.thicktwo);
        lt3 = (Button) activity.findViewById(R.id.thickthree);
        lt4 = (Button) activity.findViewById(R.id.thickfour);
        undo = (Button) activity.findViewById(R.id.undo);
    }

    public void addController(){
        line.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("Line", "Clicked");
                tool_SetFalse();
                model.clear_select();
                line.setBackgroundResource(R.drawable.line_sel);
                model.current_tool = "line";
                line_s = true;
                model.clicked();
            }
        });

        selector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (model.total_shapes() == 0) {
                    Log.d("Selector: ", "There is no shape, you cannot select anything.");
                } else {
                    model.current_tool = "selector";
                    tool_SetFalse();
                    selector_s = true;
                    Log.d("selector", "Clicked");
                    selector.setBackgroundResource(R.drawable.selector_sel);
                    model.current_tool = "selector";
                    model.clicked();
                }
            }
        });

        eraser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (model.total_shapes() == 0) {
                    Log.d("eraser: ", "There is no shape, you cannot delete anything.");
                } else {
                    model.current_tool = "eraser";
                    model.clear_select();
                    tool_SetFalse();
                    eraser_s = true;
                    Log.d("eraser", "Clicked");
                    eraser.setBackgroundResource(R.drawable.eraser_sel);
                    model.current_tool = "eraser";
                    model.clicked();
                }
            }
        });

        eraser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("eraser: ","Long press fired");
                model.clear_all();
                model.setChanged_diy();
                model.notifyObservers();
                return true;
            }
        });


        square.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("square", "Clicked");
                tool_SetFalse();
                model.clear_select();
                square.setBackgroundResource(R.drawable.square_sel);
                model.current_tool = "square";
                model.clear_select();
                square_s = true;
                model.clicked();
            }
        });

        circle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tool_SetFalse();
                Log.d("circle", "Clicked");
                model.current_tool = "circle";
                model.clear_select();
                circle_s = true;
                model.clicked();
                circle.setBackgroundResource(R.drawable.circle_sel);
                model.current_tool = "circle";
            }
        });


        fill.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (model.total_shapes() == 0) {
                    Log.d("fill: ", "There is no shape, you cannot fill anything.");
                } else {

                    model.clear_select();
                    tool_SetFalse();
                    fill_s = true;
                    model.clicked();
                    Log.d("fill", "Clicked");
                    fill.setBackgroundResource(R.drawable.fill_sel1);
                    model.current_tool = "fill";
                }
            }
        });


        red.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("red", "Clicked");
                color_SetFalse();
                red.setBackgroundResource(R.drawable.red_sel);
                model.current_color = Color.RED;
                if (model.selector) {
                    model.add_redo();
                    model.unredo_steps.get(model.curr_reundo_index).behavior="color";
                    model.unredo_steps.get(model.curr_reundo_index).object_index=model.current_select;
                    model.unredo_steps.get(model.curr_reundo_index).new_color=model.current_color;
                    model.unredo_steps.get(model.curr_reundo_index).ori_color= model.get_comp(model.current_select).current_color;

                    model.get_comp(model.current_select).current_color = Color.RED;

                    model.setChanged_diy();
                    model.notifyObservers();
                }
            }
        });


        blue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("blue", "Clicked");
                color_SetFalse();
                blue.setBackgroundResource(R.drawable.blue_sel);
                model.current_color = Color.BLUE;
                if (model.selector) {
                    model.add_redo();
                    model.unredo_steps.get(model.curr_reundo_index).behavior="color";
                    model.unredo_steps.get(model.curr_reundo_index).object_index=model.current_select;
                    model.unredo_steps.get(model.curr_reundo_index).new_color=model.current_color;
                    model.unredo_steps.get(model.curr_reundo_index).ori_color= model.get_comp(model.current_select).current_color;


                    model.get_comp(model.current_select).current_color = Color.BLUE;
                    model.setChanged_diy();
                    model.notifyObservers();
                }
            }
        });


        green.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("green", "Clicked");
                color_SetFalse();
                green.setBackgroundResource(R.drawable.green_sel);
                model.current_color = Color.GREEN;
                if (model.selector) {
                    model.add_redo();
                    model.unredo_steps.get(model.curr_reundo_index).behavior="color";
                    model.unredo_steps.get(model.curr_reundo_index).object_index=model.current_select;
                    model.unredo_steps.get(model.curr_reundo_index).new_color=model.current_color;
                    model.unredo_steps.get(model.curr_reundo_index).ori_color= model.get_comp(model.current_select).current_color;


                    model.get_comp(model.current_select).current_color = Color.GREEN;
                    model.setChanged_diy();
                    model.notifyObservers();
                }
            }
        });


        undo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("undo", "curr_index: "+model.curr_reundo_index);
                model.undo();
            }

        });

        undo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("undo: ","Long press fired");
                model.redo();
                return true;
            }
        });


        lt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("lt1", "Clicked");
                line_SetFalse();
                lt1.setBackgroundResource(R.drawable.thickone_sel);
                model.current_thickness = 3;
                if(model.selector){
                    model.get_comp(model.current_select).line_thickness = 3;
                }
                model.setChanged_diy();
                model.notifyObservers();
            }
        });


        lt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("Line", "Clicked");
                line_SetFalse();
                lt2.setBackgroundResource(R.drawable.thicktwo_sel);
                model.current_thickness = 7;
                if(model.selector){
                    model.get_comp(model.current_select).line_thickness = 7;
                }
                model.setChanged_diy();
                model.notifyObservers();
            }
        });


        lt3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("lt3", "Clicked");
                line_SetFalse();
                lt3.setBackgroundResource(R.drawable.thickthree_sel); 
                model.current_thickness = 11;
                if(model.selector){
                    model.get_comp(model.current_select).line_thickness = 11;
                }
                model.setChanged_diy();
                model.notifyObservers();
            }
        });


        lt4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                line_SetFalse();
                Log.d("lt4", "Clicked");
                lt4.setBackgroundResource(R.drawable.thickfour_sel);
                model.current_thickness = 15;
                if(model.selector){
                    model.get_comp(model.current_select).line_thickness = 15;
                }
                model.setChanged_diy();
                model.notifyObservers();
            }
        });
    }

    public void tool_SetFalse() {
        selector_s = false;
        eraser_s= false;
        square_s= false;
        circle_s= false;
        fill_s= false;
        line_s= false;
        selector.setBackgroundResource(R.drawable.selector);
        eraser.setBackgroundResource(R.drawable.eraser);
        square.setBackgroundResource(R.drawable.square);
        circle.setBackgroundResource(R.drawable.circle);
        fill.setBackgroundResource(R.drawable.fill);
        line.setBackgroundResource(R.drawable.line);
    }

    public void color_SetFalse(){
        red_s = false;
        blue_s = false;
        green_s = false;
        red.setBackgroundResource(R.drawable.red);
        blue.setBackgroundResource(R.drawable.blue);
        green.setBackgroundResource(R.drawable.green);
    }


    public void line_SetFalse(){
        lt1_s = false;
        lt2_s = false;
        lt3_s = false;
        lt4_s = false;
        lt1.setBackgroundResource(R.drawable.thickone);
        lt2.setBackgroundResource(R.drawable.thicktwo);
        lt3.setBackgroundResource(R.drawable.thickthree);
        lt4.setBackgroundResource(R.drawable.thickfour);
    }

    public void update(Observable o, Object arg){

    }
}
