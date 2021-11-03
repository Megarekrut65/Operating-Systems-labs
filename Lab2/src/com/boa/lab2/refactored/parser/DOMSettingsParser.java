package com.boa.lab2.refactored.parser;

import com.boa.lab2.refactored.SchedulingSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DOMSettingsParser {
    private DocumentBuilder docBuilder;
    private Validator validator;
    private SettingsHandler settingsHandler;
    public DOMSettingsParser(String schemaName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
            settingsHandler = new SettingsHandler();
            validator = new Validator(schemaName, settingsHandler);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
    public SchedulingSettings readSettingsFromXmlFile(String filePath){
        try {
            validator.validate(filePath);
            if(settingsHandler.getErrors().size() == 0){
                Document doc;
                doc = docBuilder.parse(filePath);
                Element root = doc.getDocumentElement();
                NodeList settingsList = root.getElementsByTagName("schedulingSettings");
                if(settingsList.getLength() == 1){
                    Element settingsElement = (Element) settingsList.item(0);
                    return buildSettings(settingsElement);
                }
            }
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
    private SchedulingSettings buildSettings(Element element) {
        SchedulingSettings settings = new SchedulingSettings();
        settings.setProcessNum(getElementValue(element,"processNum"));
        settings.setMeanDev(getElementValue(element,"meanDev"));
        settings.setStandardDev(getElementValue(element,"standardDev"));
        settings.setRuntime(getElementValue(element,"runtime"));
        return settings;
    }
    private static int getElementValue(Element element, String elementName) {
        NodeList nList = element.getElementsByTagName(elementName);
        Node node = nList.item(0);
        return Integer.parseInt(node.getTextContent());
    }
}
