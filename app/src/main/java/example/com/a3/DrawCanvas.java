package example.com.a3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

public class DrawCanvas extends View implements Observer{

    public Model model;
    public boolean resizeing = false;
    public Point start_point;
    public Point end_point;

    private List<Point> allPoints=new ArrayList<Point>();


    public DrawCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setBackgroundColor(Color.WHITE);
        super.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                Point point = new Point((int) event.getX(), (int) event.getY());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    resizeing = false;
                    start_point = new Point((int) event.getX(), (int) event.getY());
                    if (model.draw_or_not()) {
                        model.clear_select();
                        model.drawing_star(start_point, model.current_tool, model.current_thickness, model.current_color);
                    } else {
                        if (model.selector) {
                            if (model.current_tool == "eraser") {
                                if (model.total_shapes() == 0) {
                                    Log.d("eraser:", "There is no shape, you cannot delete anything.");
                                } else {
                                    model.current_tool = "eraser";
                                    model.clear_select();
                                    model.clicked();
                                }
                            } else if ((event.getX() > model.get_comp(model.current_select).end_x) &&
                                    (event.getX() < model.get_comp(model.current_select).end_x + 10)) {
                                if ((event.getY() > model.get_comp(model.current_select).end_y) &&
                                        (event.getY() < model.get_comp(model.current_select).end_y + 10)) {
                                    resizeing = true;
                                    start_point = new Point((int) event.getX(), (int) event.getY());
                                    return true;
                                }
                            }
                        }
                        if (model.current_tool == "selector") {
                            int index = model.select_index((int) event.getX(), (int) event.getY());
                            if (index == -1) {
                                model.clear_select();
                                model.clicked();
                                return true;
                            }
                            model.current_color = model.components.get(index).current_color;
                            model.current_thickness = model.components.get(index).line_thickness;
                            model.clear_select();
                            model.components.get(index).sel = true;
                            model.selector = true;
                            model.current_select = index;

                            model.add_redo();
                            model.unredo_steps.get(model.curr_reundo_index).behavior ="move";
                            model.unredo_steps.get(model.curr_reundo_index).object_index = index;
                            model.unredo_steps.get(model.curr_reundo_index).ori_start_x = model.get_comp(index).start_x;
                            model.unredo_steps.get(model.curr_reundo_index).ori_start_y = model.get_comp(index).start_y;
                            model.unredo_steps.get(model.curr_reundo_index).ori_end_x = model.get_comp(index).end_x;
                            model.unredo_steps.get(model.curr_reundo_index).ori_end_y = model.get_comp(index).end_y;

                            model.clicked();
                        } else if (model.current_tool == "fill") {
                            model.fill((int) event.getX(), (int) event.getY());
                            model.clear_select();
                            model.clicked();
                        } else if (model.current_tool == "eraser") {
                            model.clear_select();
                            int index = model.select_index((int) event.getX(), (int) event.getY());
                            if (index == -1) {
                                model.clicked();
                                return true;
                            } else {
                                model.add_redo();
                                model.unredo_steps.get(model.curr_reundo_index).behavior="delete";
                                model.unredo_steps.get(model.curr_reundo_index).object_index=index;
                                model.unredo_steps.get(model.curr_reundo_index).drawings=model.components.get(index);
                                for (int i = 0; i<model.unredo_steps.size();i++){
                                    if(model.unredo_steps.get(i).object_index>index){
                                        model.unredo_steps.get(i).object_index--;
                                    }
                                }
                                model.num_Drawings--;
                                model.components.remove(index);
                                model.clicked();
                            }
                        }
                    }
                }
    
                else if(event.getAction()==MotionEvent.ACTION_UP){
    
                }else if(event.getAction()==MotionEvent.ACTION_MOVE){

                    if (model.draw_or_not()) {
                        end_point = new Point((int) event.getX(), (int) event.getY());
                        model.drawing_end(end_point);
                        model.clicked();
                    } else {
                        end_point = new Point((int) event.getX(), (int) event.getY());
                        int dx = (int) -start_point.x + (int) end_point.x;
                        int dy = (int) -start_point.y + (int) end_point.y;
                        start_point = new Point((int) event.getX(), (int) event.getY());
                        if (model.current_tool == "selector") {
                            if (resizeing) {
                                model.get_comp(model.current_select).end_x = model.get_comp(model.current_select).end_x + dx;
                                model.get_comp(model.current_select).end_y = model.get_comp(model.current_select).end_y + dy;
                                model.clicked();
                            } else {
                                for (int i = 0; i < model.components.size(); i++) {
                                    if (model.get_comp(i).sel == true) {


                                        model.get_comp(i).start_x = model.get_comp(i).start_x + dx;
                                        model.get_comp(i).start_y = model.get_comp(i).start_y + dy;
                                        model.get_comp(i).end_x = model.get_comp(i).end_x + dx;
                                        model.get_comp(i).end_y = model.get_comp(i).end_y + dy;

                                        model.unredo_steps.get(model.curr_reundo_index).new_start_x = model.get_comp(i).start_x;
                                        model.unredo_steps.get(model.curr_reundo_index).new_start_y = model.get_comp(i).start_y;
                                        model.unredo_steps.get(model.curr_reundo_index).new_end_x = model.get_comp(i).end_x;
                                        model.unredo_steps.get(model.curr_reundo_index).new_end_y = model.get_comp(i).end_y;
                                        model.clicked();
                                    }
                                }
                            }
                        }
                    }
                }
                DrawCanvas.this.postInvalidate();
                return true;

            }

        });
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < model.components.size(); i++) {
            Paint paint=new Paint();
            paint.setColor(model.current_color);
            if ((model.get_comp(i).start_x == model.get_comp(i).end_x) && 
                    (model.get_comp(i).start_y == model.get_comp(i).end_y)) {
            }else {
                if (model.get_comp(i).shape == "circle") {
                    if (model.get_comp(i).fill) {
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(model.get_comp(i).fill_color);
                        canvas.drawCircle(model.get_comp(i).start_x, model.get_comp(i).start_y, model.circle_r(i), paint);
                    }
                    paint.setColor(model.get_comp(i).current_color);
                    paint.setStrokeWidth(model.get_comp(i).line_thickness);
                    if (model.get_comp(i).sel) {
                        paint.setStrokeWidth(model.get_comp(i).line_thickness);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
                }
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(model.get_comp(i).start_x, model.get_comp(i).start_y, model.circle_r(i), paint);
                }   else if (model.get_comp(i).shape == "square") {
                    if (model.get_comp(i).fill) {
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(model.get_comp(i).fill_color);
                        canvas.drawRect((float)model.get_comp(i).start_x,(float)model.get_comp(i).start_y,(float)model.get_comp(i).end_x,(float)model.get_comp(i).end_y,paint);
                    }
                    paint.setStrokeWidth(model.get_comp(i).line_thickness);
                    paint.setColor(model.get_comp(i).current_color);
                    if (model.get_comp(i).sel) {
                        paint.setStrokeWidth(model.get_comp(i).line_thickness);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
                    }
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect((float)model.get_comp(i).start_x,(float)model.get_comp(i).start_y,(float)model.get_comp(i).end_x,(float)model.get_comp(i).end_y,paint);
                } else if (model.get_comp(i).shape == "line") {
                    paint.setStrokeWidth(model.get_comp(i).line_thickness);
                    paint.setColor(model.get_comp(i).current_color);
                    if (model.get_comp(i).sel) {
                        paint.setStrokeWidth(model.get_comp(i).line_thickness);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
                    }
                    canvas.drawLine((float)model.get_comp(i).start_x,(float)model.get_comp(i).start_y,(float)model.get_comp(i).end_x,(float)model.get_comp(i).end_y,paint);
                }
                
            }
        }

    }

    public void addModel(Model m ){
        this.model = m;
    }

    @Override
    public void update(Observable o, Object arg){

        this.invalidate();
    }

}
