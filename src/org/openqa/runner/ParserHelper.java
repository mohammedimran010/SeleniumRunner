/*
 * Copyright 2011 Aleksey Shilin
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.openqa.runner;


import org.apache.log4j.Logger;
import org.openqa.runner.parserHandlers.TestHandler;
import org.openqa.runner.parserHandlers.TestSuiteHandler;
import org.openqa.runner.tests.*;
import org.xml.sax.SAXException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lex
 * Date: 09.06.11
 * To change this template use File | Settings | File Templates.
 */
public class ParserHelper {

    private static SAXParserFactory spf;

    /**
     * Parse Test from file
     *
     * @param path String
     * @return Test object
     */
    public static Test parseTest(String path) throws IOException, SAXException {

        File testFile = checkFile(path);

        TestHandler handler = new TestHandler();

        if (spf == null)
            spf = SAXParserFactory.newInstance();

        Test result = new Test();

        try {
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(testFile, handler);
            Command[] commands = handler.getCommands();
            result.setBaseUrl(handler.getBaseUrl());
            State state = result.getState();
            state.setTestName(handler.getTitle());
            for (int i = 0; i < commands.length; i++)
                result.addCommand(commands[i]);

            if (handler.getBeforeTest() != null) {
                Fixture beforeTest = parseFixture(testFile.getParent() + File.separator + handler.getBeforeTest());
                result.setBeforeTest(beforeTest);
            }

            if (handler.getAfterTest() != null) {
                Fixture afterTest = parseFixture(testFile.getParent() + File.separator + handler.getAfterTest());
                result.setAfterTest(afterTest);
            }

        } catch (ParserConfigurationException t) {
            Logger.getLogger(ParserHelper.class).error("Error in parsingTest", t);
        }

        return result;
    }

    public static Suite parseTestSuite(String path) throws IOException, SAXException {

        File testSuiteFile = checkFile(path);

        TestSuiteHandler handler = new TestSuiteHandler();

        if (spf == null)
            spf = SAXParserFactory.newInstance();

        Suite result = null;

        try {
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(testSuiteFile, handler);
            String[] commands = handler.getTests();
            Test[] tests = new Test[commands.length];
            for (int i = 0; i < commands.length; i++)
                tests[i] = parseTest(testSuiteFile.getParent() + File.separator + commands[i]);

            result = new Suite(tests, handler.getTitle());

            if (handler.getBeforeSuite() != null) {
                Fixture beforeSuite = parseFixture(testSuiteFile.getParent() + File.separator + handler.getBeforeSuite());
                result.setBeforeSuite(beforeSuite);
            }

            if (handler.getAfterSuite() != null) {
                Fixture afterSuite = parseFixture(testSuiteFile.getParent() + File.separator + handler.getAfterSuite());
                result.setAfterSuite(afterSuite);
            }

        } catch (ParserConfigurationException t) {
            Logger.getLogger(ParserHelper.class).error("Error in parseTestSuite", t);
        }

        return result;
    }

    public static DataSet parseDataSet(String path) {
        throw new NotImplementedException();
    }

    public static Fixture parseFixture(String path) throws IOException, SAXException {

        Fixture fixture = null;

        File testFile = checkFile(path);

        TestHandler handler = new TestHandler();

        if (spf == null)
            spf = SAXParserFactory.newInstance();

        try {

            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(testFile, handler);
            Command[] commands = handler.getCommands();
            fixture = new Fixture(commands);
            fixture.setBaseUrl(handler.getBaseUrl());

        } catch (ParserConfigurationException t) {
            Logger.getLogger(ParserHelper.class).error("Error in parsingTest", t);
        }

        return fixture;
    }

    private static File checkFile(String path) throws IOException {
        File testFile = new File(path);

        if (!testFile.exists()) {
            throw new FileNotFoundException("Can't find " + path);
        }

        if (!testFile.canRead()) {
            throw new IOException("Can't read file " + path);
        }
        return testFile;
    }

}
