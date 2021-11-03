package com.boa.lab2.refactored;

import com.boa.lab2.refactored.parser.DOMSettingsParser;

public class Main {
    public static void main(String[] args) {
        DOMSettingsParser parser = new DOMSettingsParser("src/com/boa/lab2/refactored/data/scheduling.xsd");
        var settings = parser.readSettingsFromXmlFile("src/com/boa/lab2/refactored/data/scheduling.xml");
        if(settings != null){
            System.out.println(settings);
        }
    }
}
