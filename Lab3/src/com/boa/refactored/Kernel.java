package com.boa.refactored;

import java.io.*;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;

public class Kernel extends Thread {
    // The number of virtual pages must be fixed at 63 due to
    // dependencies in the GUI
    protected static int virtPageNum = 63;

    protected String output = null;
    protected static final String lineSeparator =
            System.getProperty("line.separator");
    protected String command_file;
    protected String config_file;
    protected ControlPanel controlPanel;
    protected Vector<Page> memVector = new Vector<>();
    protected Vector<Instruction> instructVector = new Vector<>();
    protected boolean doStdoutLog = false;
    protected boolean doFileLog = false;
    public int runs;
    public int runcycles;
    public long block = (int) Math.pow(2, 12);
    public static byte addressradix = 10;
    private final int readDuration = 30;
    protected AddressConverter converter;

    public void init(String commands, String config) {
        File f;
        command_file = commands;
        config_file = config;
        String line;
        String tmp = null;
        String command = "";
        byte R = 0;
        byte M = 0;
        int i = 0;
        int j = 0;
        int id = 0;
        int physical = 0;
        int physical_count = 0;
        int inMemTime = 0;
        int lastTouchTime = 0;
        int map_count = 0;
        double power = 14;
        long high = 0;
        long low = 0;
        long addr = 0;
        long address_limit = (block * virtPageNum + 1) - 1;
        if (config != null) {
            f = new File(config);
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("numpages")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            tmp = st.nextToken();
                            virtPageNum = Common.s2i(st.nextToken()) - 1;
                            if (virtPageNum < 2 || virtPageNum > 63) {
                                System.out.println("MemoryManagement: numpages out of bounds.");
                                System.exit(-1);
                            }
                            address_limit = (block * virtPageNum + 1) - 1;
                        }
                    }
                }
                in.close();
            } catch (IOException e) { /* Handle exceptions */ }
            for (i = 0; i <= virtPageNum; i++) {
                high = (block * (i + 1)) - 1;
                low = block * i;
                memVector.addElement(new Page(i, -1, R, M, 0, 0, high, low));
            }
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("memset")) {
                        StringTokenizer st = new StringTokenizer(line);
                        st.nextToken();
                        while (st.hasMoreTokens()) {
                            id = Common.s2i(st.nextToken());
                            tmp = st.nextToken();
                            if (tmp.startsWith("x")) {
                                physical = -1;
                            } else {
                                physical = Common.s2i(tmp);
                            }
                            if ((0 > id || id > virtPageNum) || (-1 > physical || physical > ((virtPageNum - 1) / 2))) {
                                System.out.println("MemoryManagement: Invalid page value in " + config);
                                System.exit(-1);
                            }
                            R = Common.s2b(st.nextToken());
                            if (R < 0 || R > 1) {
                                System.out.println("MemoryManagement: Invalid R value in " + config);
                                System.exit(-1);
                            }
                            M = Common.s2b(st.nextToken());
                            if (M < 0 || M > 1) {
                                System.out.println("MemoryManagement: Invalid M value in " + config);
                                System.exit(-1);
                            }
                            inMemTime = Common.s2i(st.nextToken());
                            if (inMemTime < 0) {
                                System.out.println("MemoryManagement: Invalid inMemTime in " + config);
                                System.exit(-1);
                            }
                            lastTouchTime = Common.s2i(st.nextToken());
                            if (lastTouchTime < 0) {
                                System.out.println("MemoryManagement: Invalid lastTouchTime in " + config);
                                System.exit(-1);
                            }
                            Page page = memVector.elementAt(id);
                            page.physical = physical;
                            page.R = R;
                            page.M = M;
                            page.inMemTime = inMemTime;
                            page.lastTouchTime = lastTouchTime;
                        }
                    }
                    if (line.startsWith("enable_logging")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            if (st.nextToken().startsWith("true")) {
                                doStdoutLog = true;
                            }
                        }
                    }
                    if (line.startsWith("log_file")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            tmp = st.nextToken();
                        }
                        assert tmp != null;
                        if (tmp.startsWith("log_file")) {
                            doFileLog = false;
                            output = "tracefile";
                        } else {
                            doFileLog = true;
                            doStdoutLog = false;
                            output = tmp;
                        }
                    }
                    if (line.startsWith("pagesize")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            st.nextToken();
                            tmp = st.nextToken();
                            if (tmp.startsWith("power")) {
                                power = Integer.parseInt(st.nextToken());
                                block = (int) Math.pow(2, power);
                            } else {
                                block = Long.parseLong(tmp, 10);
                            }
                            address_limit = (block * virtPageNum + 1) - 1;
                        }
                        if (block < 64 || block > Math.pow(2, 26)) {
                            System.out.println("MemoryManagement: pagesize is out of bounds");
                            System.exit(-1);
                        }
                        for (i = 0; i <= virtPageNum; i++) {
                            Page page = memVector.elementAt(i);
                            page.high = (block * (i + 1)) - 1;
                            page.low = block * i;
                        }
                    }
                    if (line.startsWith("addressradix")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            st.nextToken();
                            tmp = st.nextToken();
                            addressradix = Byte.parseByte(tmp);
                            if (addressradix < 0 || addressradix > 20) {
                                System.out.println("MemoryManagement: addressradix out of bounds.");
                                System.exit(-1);
                            }
                        }
                    }
                }
                in.close();
            } catch (IOException e) { /* Handle exceptions */ }
        }
        f = new File(commands);
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            while ((line = in.readLine()) != null) {
                if (line.startsWith("READ") || line.startsWith("WRITE")) {
                    if (line.startsWith("READ")) {
                        command = "READ";
                    }
                    if (line.startsWith("WRITE")) {
                        command = "WRITE";
                    }
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    tmp = st.nextToken();
                    if (tmp.startsWith("random")) {
                        instructVector.addElement(new Instruction(command, Common.randomLong(address_limit)));
                    } else {
                        if (tmp.startsWith("bin")) {
                            addr = Long.parseLong(st.nextToken(), 2);
                        } else if (tmp.startsWith("oct")) {
                            addr = Long.parseLong(st.nextToken(), 8);
                        } else if (tmp.startsWith("hex")) {
                            addr = Long.parseLong(st.nextToken(), 16);
                        } else {
                            addr = Long.parseLong(tmp);
                        }
                        if (0 > addr || addr > address_limit) {
                            System.out.println("MemoryManagement: " + addr + ", Address out of range in " + commands);
                            System.exit(-1);
                        }
                        instructVector.addElement(new Instruction(command, addr));
                    }
                }
            }
            in.close();
        } catch (IOException e) { /* Handle exceptions */ }
        runcycles = instructVector.size();
        if (runcycles < 1) {
            System.out.println("MemoryManagement: no instructions present for execution.");
            System.exit(-1);
        }
        if (doFileLog) {
            File trace = new File(output);
            trace.delete();
        }
        runs = 0;
        for (i = 0; i < virtPageNum; i++) {
            Page page = memVector.elementAt(i);
            if (page.physical != -1) {
                map_count++;
            }
            for (j = 0; j < virtPageNum; j++) {
                Page tmp_page = memVector.elementAt(j);
                if (tmp_page.physical == page.physical && page.physical >= 0) {
                    physical_count++;
                }
            }
            if (physical_count > 1) {
                System.out.println("MemoryManagement: Duplicate physical page's in " + config);
                System.exit(-1);
            }
            physical_count = 0;
        }
        if (map_count < (virtPageNum + 1) / 2) {
            for (i = 0; i < virtPageNum; i++) {
                Page page = memVector.elementAt(i);
                if (page.physical == -1 && map_count < (virtPageNum + 1) / 2) {
                    page.physical = i;
                    map_count++;
                }
            }
        }
        for (i = 0; i < virtPageNum; i++) {
            Page page = memVector.elementAt(i);
            if (page.physical == -1) {
                controlPanel.removePhysicalPage(i);
            } else {
                controlPanel.addPhysicalPage(i, page.physical);
            }
        }
        for (i = 0; i < instructVector.size(); i++) {
            high = block * virtPageNum;
            Instruction instruct = instructVector.elementAt(i);
            if (instruct.getAddr() < 0 || instruct.getAddr() > high) {
                System.out.println("MemoryManagement: Instruction (" + instruct.getInst() + " " + instruct.getAddr() + ") out of bounds.");
                System.exit(-1);
            }
        }
        converter = new AddressConverter(virtPageNum, block);
    }

    public void setControlPanel(ControlPanel newControlPanel) {
        controlPanel = newControlPanel;
    }

    public void getPage(int pageNum) {
        Page page = memVector.elementAt(pageNum);
        controlPanel.paintPage(page);
    }

    protected void printLogFile(String message) {
        try {
            new BufferedWriter(new FileWriter(output, true)).
                    append(message).
                    append(lineSeparator).
                    close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        step();
        while (runs != runcycles && !Thread.interrupted()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            step();
        }
    }

    protected void log(String text) {
        if (doFileLog) {
            printLogFile(text);
        }
        if (doStdoutLog) {
            System.out.println(text);
        }
    }

    protected void doInstruction(Instruction instruct) {
        Page page = memVector.elementAt(converter.virtualToPhysical(instruct.getAddr()));
        if (page.physical == -1) {
            log(instruct.getInst() + Long.toString(instruct.getAddr(), addressradix) + " ... page fault");
            PageFault.replacePage(memVector, virtPageNum, converter.virtualToPhysical(instruct.getAddr()), controlPanel);
            controlPanel.pageFaultValueLabel.setText("YES");
        } else {
            if (instruct.isRead()) page.R = 1;
            else if (instruct.isWrite()) page.M = 1;
            page.lastTouchTime = 0;
            log(instruct.getInst() + Long.toString(instruct.getAddr(), addressradix) + " ... okay");
        }
    }

    private void nextTime() {
        for (int i = 0; i < virtPageNum; i++) {
            Page page = memVector.elementAt(i);
            if (page.R == 1 && page.lastTouchTime == readDuration) {
                page.R = 0;
            }
            if (page.physical != -1) {
                page.inMemTime = page.inMemTime + 10;
                page.lastTouchTime = page.lastTouchTime + 10;
            }
        }
        runs++;
        controlPanel.timeValueLabel.setText(runs * 10 + " (ns)");
    }

    public void step() {
        Instruction instruct = instructVector.elementAt(runs);
        controlPanel.instructionValueLabel.setText(instruct.getInst());
        controlPanel.addressValueLabel.setText(Long.toString(instruct.getAddr(), addressradix));
        getPage(converter.virtualToPhysical(instruct.getAddr()));
        controlPanel.pageFaultValueLabel.setText("NO");
        doInstruction(instruct);
        nextTime();
    }

    public void reset() {
        memVector.removeAllElements();
        instructVector.removeAllElements();
        controlPanel.statusValueLabel.setText("STOP");
        controlPanel.timeValueLabel.setText("0");
        controlPanel.instructionValueLabel.setText("NONE");
        controlPanel.addressValueLabel.setText("NULL");
        controlPanel.pageFaultValueLabel.setText("NO");
        controlPanel.virtualPageValueLabel.setText("x");
        controlPanel.physicalPageValueLabel.setText("0");
        controlPanel.RValueLabel.setText("0");
        controlPanel.MValueLabel.setText("0");
        controlPanel.inMemTimeValueLabel.setText("0");
        controlPanel.lastTouchTimeValueLabel.setText("0");
        controlPanel.lowValueLabel.setText("0");
        controlPanel.highValueLabel.setText("0");
        init(command_file, config_file);
    }
}
