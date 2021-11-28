package com.boa.refactored;

import java.util.Objects;

public class ClockAlgorithm extends Kernel{
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
            if ( page.physical == -1 )
            {
                if ( doFileLog )
                {
                    printLogFile( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
                }
                if ( doStdoutLog )
                {
                    System.out.println( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
                }
                PageFault.replacePage( memVector , virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );
                controlPanel.pageFaultValueLabel.setText( "YES" );
            }
            else
            {
                page.R = 1;
                page.lastTouchTime = 0;
                if ( doFileLog )
                {
                    printLogFile( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
                }
                if ( doStdoutLog )
                {
                    System.out.println( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
                }
            }
        }
        if ( instruct.inst.startsWith( "WRITE" ) )
        {
            Page page = (Page) memVector.elementAt( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
            if ( page.physical == -1 )
            {
                if ( doFileLog )
                {
                    printLogFile( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
                }
                if ( doStdoutLog )
                {
                    System.out.println( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
                }
                PageFault.replacePage( memVector , virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );          controlPanel.pageFaultValueLabel.setText( "YES" );
            }
            else
            {
                page.M = 1;
                page.lastTouchTime = 0;
                if ( doFileLog )
                {
                    printLogFile( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
                }
                if ( doStdoutLog )
                {
                    System.out.println( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
                }
            }
        }
        for ( i = 0; i < virtPageNum; i++ )
        {
            Page page = (Page) memVector.elementAt( i );
            if ( page.R == 1 && page.lastTouchTime == 10 )
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
        controlPanel.timeValueLabel.setText( Integer.toString( runs*10 ) + " (ns)" );
    }
}
