package com.boa.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class NRUAlgorithm {
    public static void replacePage (Vector mem , int virtPageNum , int replacePageNum , ControlPanel controlPanel )
    {
        List<Page> class0 = new ArrayList<>(), class1 = new ArrayList<>(), class2 = new ArrayList<>(),
                class3 = new ArrayList<>();
        for(int i = 0; i < virtPageNum; i++){
            Page page = ( Page ) mem.elementAt(i);
            if(page.physical == -1) continue;
            if(page.R == 0){
                if(page.M == 0){
                    class0.add(page);
                    break;
                }else{
                    class1.add(page);
                }
            }else{
                if(page.M == 0){
                    class2.add(page);
                }else{
                    class3.add(page);
                }
            }
        }
        Page toRemove;
        if(class0.size() != 0) toRemove = class0.get(0);
        else if(class1.size() != 0) toRemove = class1.get(0);
        else if(class2.size() != 0) toRemove = class2.get(0);
        else if(class3.size() != 0) toRemove = class3.get(0);
        else throw new IllegalArgumentException("mem can't be empty!");
        int index = mem.indexOf(toRemove);
        Page nextpage = ( Page ) mem.elementAt( replacePageNum );
        controlPanel.removePhysicalPage( index );
        nextpage.physical = toRemove.physical;
        controlPanel.addPhysicalPage( nextpage.physical , replacePageNum );
        toRemove.inMemTime = 0;
        toRemove.lastTouchTime = 0;
        toRemove.R = 0;
        toRemove.M = 0;
        toRemove.physical = -1;
    }
}
