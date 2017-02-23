package example.com.a3;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
public class Model extends Observable {
    public class Drawings {
        public int index, start_x, start_y, end_x, end_y, line_thickness;
        public int current_color= Color.RED;
        public int fill_color = Color.WHITE;
        public boolean fill = false;
        public boolean sel = false;
        public String shape;

        public Drawings() {}
    }


    public class un_redo{
        public int object_index;
        public String behavior;
        public int ori_color;
        public int new_color;
        public int ori_line, new_line;
        public int ori_start_x, ori_start_y, ori_end_x, ori_end_y;
        public int new_start_x, new_start_y, new_end_x, new_end_y;
        public Drawings drawings;
    }

    public int num_steps = -1;
    public int curr_reundo_index = -1;
    public List<un_redo> unredo_steps = new ArrayList<un_redo>();

    public void add_redo(){
        if(curr_reundo_index != num_steps){
            int tmp = num_steps;
            for (int i = curr_reundo_index+1; i < tmp; i++){
                unredo_steps.remove(i);
            }
            unredo_steps.add(new un_redo());
            curr_reundo_index++;
            num_steps =curr_reundo_index;
            return;
        }
        else if(num_steps <4 ){
            curr_reundo_index++;
            num_steps++;
            unredo_steps.add(new un_redo());
        }else{
            unredo_steps.remove(0);
            unredo_steps.add(new un_redo());
        }
    }

    public Buttons buttons;

    public int num_Drawings = 0;
    public List<Drawings> components = new ArrayList<Drawings>();
    public int current_thickness, current_select;
    public boolean selector;
    public String current_tool;
    public int current_color;


    public Model() {
        current_thickness = 3;
        current_color = Color.RED;
        current_select = -1;
        selector = false;
        current_tool = "happy";
        setChanged();
    }

    public int total_shapes() {
        return components.size();
    }

    public void clicked(){
        setChanged();
        notifyObservers();
    }

    public void clear_select(){
        for (int i = 0; i < components.size(); i++){
            components.get(i).sel = false;
        }
        current_select = -1;
        selector = false;
    }

    public boolean draw_or_not() {
        if ((current_tool == "circle") || (current_tool == "line") || (current_tool == "square")) return true;
        else return false;
    }

    public void drawing_star(Point start_point, String tool, int line_thick, int fill_color) {
        num_Drawings++;
        components.add(new Drawings());
        components.get(num_Drawings - 1).start_x = start_point.x;
        components.get(num_Drawings - 1).start_y = start_point.y;
        components.get(num_Drawings - 1).end_x = start_point.x;
        components.get(num_Drawings - 1).end_y = start_point.y;
        components.get(num_Drawings - 1).shape = tool;
        components.get(num_Drawings - 1).line_thickness = line_thick;
        components.get(num_Drawings - 1).current_color = current_color;
        components.get(num_Drawings - 1).index = num_Drawings;
    }

    public Drawings get_comp(int i) {
        return components.get(i);
    }


    public int select_index(int x, int y) {
        for (int i = components.size() - 1; i >= 0; i--) {
            Drawings tmp = components.get(i);
            if (tmp.shape == "circle") {
                int csx = circle_start_x(i);
                int cr = circle_r(i);
                int csy = circle_start_y(i);
                float dis =(float) Math.sqrt(( x-csx-cr)*(x-csx-cr) + (y-csy-cr)*(y-csy-cr));
                if(dis < circle_r(i)) {
                    selector = true;
                    current_select = i;
                    if(current_tool == "selector") {
                        current_thickness = get_comp(i).line_thickness;
                        buttons.line_SetFalse();
                        if (current_thickness == 3) {
                            buttons.lt1_s = true;
                            buttons.lt1.setBackgroundResource(R.drawable.thickone_sel);
                        } else if (current_thickness == 7) {
                            buttons.lt2_s = true;
                            buttons.lt2.setBackgroundResource(R.drawable.thicktwo_sel);
                        } else if (current_thickness == 11) {
                            buttons.lt3_s = true;
                            buttons.lt3.setBackgroundResource(R.drawable.thickthree_sel);
                        } else if (current_thickness == 15) {
                            buttons.lt4_s = true;
                            buttons.lt4.setBackgroundResource(R.drawable.thickfour_sel);
                        }
                        current_color = get_comp(i).current_color;
                        buttons.color_SetFalse();
                        if (current_color == Color.RED) {
                            buttons.red_s = true;
                            buttons.red.setBackgroundResource(R.drawable.red_sel);
                        } else if (current_color == Color.GREEN) {
                            buttons.green_s = true;
                            buttons.green.setBackgroundResource(R.drawable.green_sel);
                        } else if (current_color == Color.BLUE) {
                            buttons.blue_s = true;
                            buttons.blue.setBackgroundResource(R.drawable.blue_sel);
                        }
                    }
                    return i;
                }

            }else if(tmp.shape == "square"){
                if ((x>drawing_start_x(i)) && (x<drawing_start_x(i)+drawing_width(i)) &&
                        (y>drawing_start_y(i)) && (y<drawing_start_y(i)+drawing_height(i))){
                    get_comp(i).sel = true;
                    selector = true;
                    current_select = i;
                    if(current_tool == "selector") {
                        current_thickness = get_comp(i).line_thickness;
                        buttons.line_SetFalse();
                        if (current_thickness == 3) {
                            buttons.lt1_s = true;
                            buttons.lt1.setBackgroundResource(R.drawable.thickone_sel);
                        } else if (current_thickness == 7) {
                            buttons.lt2_s = true;
                            buttons.lt2.setBackgroundResource(R.drawable.thicktwo_sel);
                        } else if (current_thickness == 11) {
                            buttons.lt3_s = true;
                            buttons.lt3.setBackgroundResource(R.drawable.thickthree_sel);
                        } else if (current_thickness == 15) {
                            buttons.lt4_s = true;
                            buttons.lt4.setBackgroundResource(R.drawable.thickfour_sel);
                        }

                        current_color = get_comp(i).current_color;
                        buttons.color_SetFalse();
                        if (current_color == Color.RED) {
                            buttons.red_s = true;
                            buttons.red.setBackgroundResource(R.drawable.red_sel);
                        } else if (current_color == Color.GREEN) {
                            buttons.green_s = true;
                            buttons.green.setBackgroundResource(R.drawable.green_sel);
                        } else if (current_color == Color.BLUE) {
                            buttons.blue_s = true;
                            buttons.blue.setBackgroundResource(R.drawable.blue_sel);
                        }
                    }
                    return i;
                }
            }else if(tmp.shape == "line"){
                if(line_sel_len(i,x,y)) {
                    get_comp(i).sel = true;
                    selector = true;
                    current_select = i;
                    if(current_tool == "selector") {
                        current_thickness = get_comp(i).line_thickness;
                        buttons.line_SetFalse();
                        if (current_thickness == 3) {
                            buttons.lt1_s = true;
                            buttons.lt1.setBackgroundResource(R.drawable.thickone_sel);
                        } else if (current_thickness == 7) {
                            buttons.lt2_s = true;
                            buttons.lt2.setBackgroundResource(R.drawable.thicktwo_sel);
                        } else if (current_thickness == 11) {
                            buttons.lt3_s = true;
                            buttons.lt3.setBackgroundResource(R.drawable.thickthree_sel);
                        } else if (current_thickness == 15) {
                            buttons.lt4_s = true;
                            buttons.lt4.setBackgroundResource(R.drawable.thickfour_sel);
                        }

                        current_color = get_comp(i).current_color;
                        buttons.color_SetFalse();
                        if (current_color == Color.RED) {
                            buttons.red_s = true;
                            buttons.red.setBackgroundResource(R.drawable.red_sel);
                        } else if (current_color == Color.GREEN) {
                            buttons.green_s = true;
                            buttons.green.setBackgroundResource(R.drawable.green_sel);
                        } else if (current_color == Color.BLUE) {
                            buttons.blue_s = true;
                            buttons.blue.setBackgroundResource(R.drawable.blue_sel);
                        }
                    }
                    return i;
                }
            }
        }
        current_select = -1;
        return -1;
    }


    public void drawing_end(Point end_point) {
        components.get(num_Drawings - 1).end_x = end_point.x;
        components.get(num_Drawings - 1).end_y = end_point.y;
    }

    public int drawing_width(int i) {
        if (get_comp(i).end_x > get_comp(i).start_x) return get_comp(i).end_x - get_comp(i).start_x;
        else return get_comp(i).start_x - get_comp(i).end_x;
    }

    public int drawing_height(int i) {
        if (get_comp(i).end_y > get_comp(i).start_y) return get_comp(i).end_y - get_comp(i).start_y;
        else return get_comp(i).start_y - get_comp(i).end_y;
    }

    public int drawing_start_x(int i) {
        if (get_comp(i).start_x > get_comp(i).end_x) return get_comp(i).end_x;
        else return get_comp(i).start_x;
    }

    public int drawing_start_y(int i) {
        if (get_comp(i).start_y > get_comp(i).end_y) return get_comp(i).end_y;
        else return get_comp(i).start_y;
    }


    public int circle_r(int i) {
        int sx = get_comp(i).start_x;
        int ex = get_comp(i).end_x;
        int sy = get_comp(i).start_y;
        int ey = get_comp(i).end_y;
        int ans = (int) Math.sqrt((sx - ex) * (sx - ex) + (sy- ey) * (sy - ey));
        return ans;
    }

    public int circle_start_x(int i) {
        int ans = get_comp(i).start_x - circle_r(i);
        if (ans < 0) ans = 0;
        return ans;
    }

    public int circle_start_y(int i) {
        int ans = get_comp(i).start_y - circle_r(i);
        if (ans < 0) ans = 0;
        return ans;
    }

    public boolean line_sel_len(int i, int x, int y){
        float line_y = (float) (x-get_comp(i).start_x)/(get_comp(i).end_x-get_comp(i).start_x);
        if ((line_y < 0) || (line_y > 1)) return false;
        line_y = (float) get_comp(i).start_y + line_y*(get_comp(i).end_y-get_comp(i).start_y);
        if ((y<(line_y+10))&&(y>line_y-10)){
            return true;
        }else{
            return false;
        }
    }

    public void fill(int x, int y){
        int index = select_index(x,y);
        if (index == -1) return;
        else{
            add_redo();
            unredo_steps.get(curr_reundo_index).behavior="fill";
            unredo_steps.get(curr_reundo_index).object_index=index;
            unredo_steps.get(curr_reundo_index).ori_color=get_comp(index).fill_color;
            unredo_steps.get(curr_reundo_index).new_color=get_comp(index).current_color;
            get_comp(index).fill = true;
            get_comp(index).fill_color = current_color;
        }
    }

    public void new_can(){
        components = new ArrayList<Drawings>();
    }

    public void new_model() {
        current_thickness = 1;
        current_color = Color.BLACK;
        current_select = -1;
        selector = false;
        current_tool = "";
        //current_tool = "selector";
        setChanged();
    }

    public void setChanged_diy(){
        setChanged();
    }

    public void clear_all(){
        components = new ArrayList<Drawings>();
        num_Drawings = 0;
    }



    public void undo(){
        if(curr_reundo_index<0){return;}
        if(unredo_steps.get(curr_reundo_index).behavior == "move"){
            get_comp(unredo_steps.get(curr_reundo_index).object_index).start_x = unredo_steps.get(curr_reundo_index).ori_start_x;
            get_comp(unredo_steps.get(curr_reundo_index).object_index).start_y = unredo_steps.get(curr_reundo_index).ori_start_y;
            get_comp(unredo_steps.get(curr_reundo_index).object_index).end_x = unredo_steps.get(curr_reundo_index).ori_end_x;
            get_comp(unredo_steps.get(curr_reundo_index).object_index).end_y = unredo_steps.get(curr_reundo_index).ori_end_y;
            curr_reundo_index--;
            setChanged_diy();
            notifyObservers();
        }else if (unredo_steps.get(curr_reundo_index).behavior == "color"){
            get_comp(unredo_steps.get(curr_reundo_index).object_index).current_color = unredo_steps.get(curr_reundo_index).ori_color;
            current_color = unredo_steps.get(curr_reundo_index).ori_color;
            buttons.color_SetFalse();
            if (current_color == Color.RED) {
                buttons.red_s = true;
                buttons.red.setBackgroundResource(R.drawable.red_sel);
            } else if (current_color == Color.GREEN) {
                buttons.green_s = true;
                buttons.green.setBackgroundResource(R.drawable.green_sel);
            } else if (current_color == Color.BLUE) {
                buttons.blue_s = true;
                buttons.blue.setBackgroundResource(R.drawable.blue_sel);
            }
            curr_reundo_index--;
            setChanged_diy();
            notifyObservers();
        }else if (unredo_steps.get(curr_reundo_index).behavior == "fill"){
            get_comp(unredo_steps.get(curr_reundo_index).object_index).fill_color = unredo_steps.get(curr_reundo_index).ori_color;
            if(unredo_steps.get(curr_reundo_index).ori_color == Color.WHITE){
                get_comp(unredo_steps.get(curr_reundo_index).object_index).fill = false;
            }else current_color = unredo_steps.get(curr_reundo_index).ori_color;
            buttons.color_SetFalse();
            if (current_color == Color.RED) {
                buttons.red_s = true;
                buttons.red.setBackgroundResource(R.drawable.red_sel);
            } else if (current_color == Color.GREEN) {
                buttons.green_s = true;
                buttons.green.setBackgroundResource(R.drawable.green_sel);
            } else if (current_color == Color.BLUE) {
                buttons.blue_s = true;
                buttons.blue.setBackgroundResource(R.drawable.blue_sel);
            }
            curr_reundo_index--;
            setChanged_diy();
            notifyObservers();
        }else if(unredo_steps.get(curr_reundo_index).behavior == "delete"){
            components.add(unredo_steps.get(curr_reundo_index).object_index,unredo_steps.get(curr_reundo_index).drawings);
            for(int i = 0; i < unredo_steps.size();i++){
                if(unredo_steps.get(i).object_index>=unredo_steps.get(curr_reundo_index).object_index){
                    unredo_steps.get(i).object_index++;
                }
            }
            curr_reundo_index--;
            setChanged_diy();
            notifyObservers();
        }
    }



    public void redo(){
        if(curr_reundo_index>=unredo_steps.size()){
            curr_reundo_index= unredo_steps.size()-1;
            return;}
        curr_reundo_index++;
        if(unredo_steps.get(curr_reundo_index).behavior == "move"){
            get_comp(unredo_steps.get(curr_reundo_index).object_index).start_x = unredo_steps.get(curr_reundo_index).new_start_x;
            get_comp(unredo_steps.get(curr_reundo_index).object_index).start_y = unredo_steps.get(curr_reundo_index).new_start_y;
            get_comp(unredo_steps.get(curr_reundo_index).object_index).end_x = unredo_steps.get(curr_reundo_index).new_end_x;
            get_comp(unredo_steps.get(curr_reundo_index).object_index).end_y = unredo_steps.get(curr_reundo_index).new_end_y;

            setChanged_diy();
            notifyObservers();
        }else if (unredo_steps.get(curr_reundo_index).behavior == "color"){
            get_comp(unredo_steps.get(curr_reundo_index).object_index).current_color = unredo_steps.get(curr_reundo_index).new_color;
            current_color = unredo_steps.get(curr_reundo_index).new_color;
            buttons.color_SetFalse();
            if (current_color == Color.RED) {
                buttons.red_s = true;
                buttons.red.setBackgroundResource(R.drawable.red_sel);
            } else if (current_color == Color.GREEN) {
                buttons.green_s = true;
                buttons.green.setBackgroundResource(R.drawable.green_sel);
            } else if (current_color == Color.BLUE) {
                buttons.blue_s = true;
                buttons.blue.setBackgroundResource(R.drawable.blue_sel);
            }
            //curr_reundo_index++;
            setChanged_diy();
            notifyObservers();
        }else if (unredo_steps.get(curr_reundo_index).behavior == "fill"){
            get_comp(unredo_steps.get(curr_reundo_index).object_index).fill_color = unredo_steps.get(curr_reundo_index).new_color;
            if(unredo_steps.get(curr_reundo_index).new_color == Color.WHITE){
                get_comp(unredo_steps.get(curr_reundo_index).object_index).fill = false;
            }else current_color = unredo_steps.get(curr_reundo_index).new_color;
            buttons.color_SetFalse();
            if (current_color == Color.RED) {
                buttons.red_s = true;
                buttons.red.setBackgroundResource(R.drawable.red_sel);
            } else if (current_color == Color.GREEN) {
                buttons.green_s = true;
                buttons.green.setBackgroundResource(R.drawable.green_sel);
            } else if (current_color == Color.BLUE) {
                buttons.blue_s = true;
                buttons.blue.setBackgroundResource(R.drawable.blue_sel);
            }
            //curr_reundo_index++;
            setChanged_diy();
            notifyObservers();
        }else if(unredo_steps.get(curr_reundo_index).behavior == "delete"){
            components.remove(unredo_steps.get(curr_reundo_index).object_index);
            for(int i = 0; i < unredo_steps.size();i++){
                if(unredo_steps.get(i).object_index>=unredo_steps.get(curr_reundo_index).object_index){
                    unredo_steps.get(i).object_index--;
                }
            }
            //curr_reundo_index++;
            setChanged_diy();
            notifyObservers();
        }
    }



}
