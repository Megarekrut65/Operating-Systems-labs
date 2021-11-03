package com.boa.lab2.refactored.parser;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class Validator {
    private final DefaultHandler handler;
    private final SAXParser parser;
    public Validator(String schemaPath, DefaultHandler handler) throws SAXException, ParserConfigurationException {
        this.handler = handler;
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(schemaPath));
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setSchema(schema);
        parser = spf.newSAXParser();
    }

    public void validate(String filePath) throws IOException, SAXException {
        parser.parse(filePath, handler);
    }
}
