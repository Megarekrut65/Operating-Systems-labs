package com.boa.refactored;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;

public class ClockAlgorithm extends Kernel{
    private PageFaultClock pageFaultClock;

    @Override
    public void init(String commands, String config) {
        super.init(commands, config);
        pageFaultClock = new PageFaultClock(memVector);
    }

    private void log(String text){
        if ( doFileLog ) {
            printLogFile( text);
        }
        if ( doStdoutLog ) {
            System.out.println( text);
        }
    }
    @Override
    public void step() {
        int i = 0;
        Instruction instruct = (Instruction) instructVector.elementAt( runs );
        controlPanel.instructionValueLabel.setText( instruct.inst );
        controlPanel.addressValueLabel.setText( Long.toString( instruct.addr , addressradix ) );
        getPage( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
        if (Objects.equals(controlPanel.pageFaultValueLabel.getText(), "YES"))
        {
            controlPanel.pageFaultValueLabel.setText( "NO" );
        }
        if ( instruct.inst.startsWith( "READ" ) )
        {
            Page page = (Page) memVector.elementAt( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
            if ( page.physical == -1 )//fault
            {
                log( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
                pageFaultClock.replacePage( memVector,virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );
                controlPanel.pageFaultValueLabel.setText( "YES" );
            }
            else
            {
                page.R = 1;
                page.lastTouchTime = 0;
                log( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
            }
        }
        if ( instruct.inst.startsWith( "WRITE" ) )
        {
            Page page = (Page) memVector.elementAt( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
            if ( page.physical == -1 )
            {
                log( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
                pageFaultClock.replacePage(memVector,virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );          controlPanel.pageFaultValueLabel.setText( "YES" );
            }
            else
            {
                page.M = 1;
                page.lastTouchTime = 0;
                log( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
            }
        }
        for ( i = 0; i < virtPageNum; i++ )
        {
            Page page = (Page) memVector.elementAt( i );
            if ( page.R == 1 && page.lastTouchTime == 30 )
            {
                page.R = 0;
            }
            if ( page.physical != -1 )
            {
                page.inMemTime = page.inMemTime + 10;
                page.lastTouchTime = page.lastTouchTime + 10;
            }
        }
        runs++;
        controlPanel.timeValueLabel.setText(runs * 10 + " (ns)" );
    }
    public static class PageFaultClock{
        private final Vector<Page> cyclicList;
        private int arrow = 0;

        public PageFaultClock(Vector<Page> list) {
            this.cyclicList = new Vector<>();
            for(int i = 0; i < list.size(); i++){
                if(list.elementAt(i).physical != -1 ) cyclicList.add(list.elementAt(i));
            }
            cyclicList.sort(new PageComparator());
            printList();
        }
        private void arrowNext(){
            arrow++;
            if(arrow >= cyclicList.size()) arrow = 0;
        }

        /**
         *
         * @param virtPageCount - number of pages
         * @param replacePageIndex - page to add to virtual memory
         * @param controlPanel
         */
        public void replacePage (Vector<Page> mem, int virtPageCount , int replacePageIndex , ControlPanel controlPanel){
            do{
                Page current = cyclicList.elementAt(arrow);
                if(current.R == 1){
                    current.R = 0;
                    current.inMemTime = 0;//like add new page
                    current.lastTouchTime = 0;
                    arrowNext();
                }else if(current.R == 0){
                    if(current.M == 1) writeToDisk(current);
                    Page nextPage = mem.elementAt(replacePageIndex);
                    controlPanel.removePhysicalPage( current.physical );
                    nextPage.physical = current.physical;
                    controlPanel.addPhysicalPage( nextPage.physical , replacePageIndex );
                    current.inMemTime = 0;
                    current.lastTouchTime = 0;
                    current.physical = -1;
                    cyclicList.set(arrow, nextPage);
                    arrowNext();
                    printList();
                    return;
                }
            }while (true);

        }
        private void printList(){
            for(int i = 0; i < cyclicList.size(); i++){
                System.out.println(i + ")" + cyclicList.elementAt(i));
            }
        }
        /**
         * Pseudo write to disk
         * @param page to remove from virtual memory and write to disk
         */
        private void writeToDisk(Page page){
            //write
            page.M = 0;
        }
        public static class PageComparator implements Comparator<Page> {
            @Override
            public int compare(Page o1, Page o2) {
                return o2.inMemTime - o1.inMemTime;
            }
        }
    }
}
