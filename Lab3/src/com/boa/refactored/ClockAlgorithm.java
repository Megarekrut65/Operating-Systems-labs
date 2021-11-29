package com.boa.refactored;

import java.util.Comparator;
import java.util.Vector;

public class ClockAlgorithm extends Kernel {
    private PageFaultClock pageFaultClock;

    @Override
    public void init(String commands, String config) {
        super.init(commands, config);
        pageFaultClock = new PageFaultClock(memVector);
        log("Clock algorithm");
    }

    @Override
    protected void doInstruction(Instruction instruct) {
        Page page = memVector.elementAt(converter.virtualToPhysical(instruct.getAddr()));
        if (page.physical == -1) {
            log(instruct.getInst() + Long.toString(instruct.getAddr(), addressradix) + " ... page fault");
            pageFaultClock.replacePage(memVector,
                    converter.virtualToPhysical(instruct.getAddr()), controlPanel);
            controlPanel.pageFaultValueLabel.setText("YES");
        } else {
            if (instruct.isRead()) page.R = 1;
            else if (instruct.isWrite()) page.M = 1;
            page.lastTouchTime = 0;
            log(instruct.getInst() + Long.toString(instruct.getAddr(), addressradix) + " ... okay");
        }
    }

    public static class PageFaultClock {
        private final Vector<Page> cyclicList;
        private int arrow = 0;

        public PageFaultClock(Vector<Page> list) {
            this.cyclicList = new Vector<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.elementAt(i).physical != -1) cyclicList.add(list.elementAt(i));
            }
            cyclicList.sort(new PageComparator());
        }

        private void arrowNext() {
            arrow++;
            if (arrow >= cyclicList.size()) arrow = 0;
        }

        public void replacePage(Vector<Page> mem, int replacePageIndex, ControlPanel controlPanel) {
            do {
                Page current = cyclicList.elementAt(arrow);
                if (current.R == 1) {
                    current.R = 0;
                    current.inMemTime = 0;//like add new page
                    current.lastTouchTime = 0;
                    arrowNext();
                } else if (current.R == 0) {
                    if (current.M == 1) writeToDisk(current);
                    Page nextPage = mem.elementAt(replacePageIndex);
                    controlPanel.removePhysicalPage(mem.indexOf(current));
                    nextPage.physical = current.physical;
                    controlPanel.addPhysicalPage(nextPage.physical, replacePageIndex);
                    current.inMemTime = 0;
                    current.lastTouchTime = 0;
                    current.physical = -1;
                    cyclicList.set(arrow, nextPage);
                    arrowNext();
                    return;
                }
            } while (true);

        }

        /**
         * Pseudo write to disk
         *
         * @param page to remove from virtual memory and write to disk
         */
        private void writeToDisk(Page page) {
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
