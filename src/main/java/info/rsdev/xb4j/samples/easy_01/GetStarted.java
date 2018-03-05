/*
 * Copyright 2018 Red Star Development.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.rsdev.xb4j.samples.easy_01;

import info.rsdev.xb4j.domain.school.Grades;
import info.rsdev.xb4j.model.BindingModel;
import info.rsdev.xb4j.model.XmlStreamer;
import info.rsdev.xb4j.model.bindings.Repeater;
import info.rsdev.xb4j.model.bindings.Root;
import info.rsdev.xb4j.model.bindings.Sequence;
import info.rsdev.xb4j.model.bindings.SimpleType;
import info.rsdev.xb4j.model.converter.IntegerConverter;
import info.rsdev.xb4j.util.XmlStreamFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class GetStarted {

    private static final String XML_SNIPPET = 
            "<Subject>\n" +
            "  <Name>Biology</Name>\n" +
            "  <Student>Eve Addams</Student>\n" +
            "  <Results>\n" +
            "    <ExamResult>7</ExamResult>\n" +
            "    <ExamResult>6</ExamResult>\n" +
            "    <ExamResult>9</ExamResult>\n" +
            "    <ExamResult>7</ExamResult>\n" +
            "  </Results>\n" +
            "</Subject>\n";

    private final BindingModel gradesModel = new BindingModel();
    
    private GetStarted() {
        Root rootElement = new Root(new QName("Subject"), Grades.class);
        Sequence content = rootElement.setChild(new Sequence(false));
        content.add(new SimpleType(new QName("Name"), false), "subject");
        content.add(new SimpleType(new QName("Student"), false), "studentFullname");
        Repeater grades = content.add(new Repeater(new QName("Results"), ArrayList.class, true), "examResults");
        grades.setItem(new SimpleType(new QName("ExamResult"), IntegerConverter.POSITIVE, true));
        gradesModel.registerRoot(rootElement);
    }

    private BindingModel getGradesModel() {
        return this.gradesModel;
    }

    public static void main(String[] args) {
        GetStarted getStartedInstance = new GetStarted();
        XmlStreamer streamer = getStartedInstance.getGradesModel().getXmlStreamer(Grades.class, null);
        Grades gradesInstance = (Grades)streamer.toJava(getXmlStreamReaderForSnippet(XML_SNIPPET));
        
        StringWriter stringWriter = new StringWriter();
        streamer.toXml(getXmlStreamWriter(stringWriter), gradesInstance);
        System.out.println(stringWriter.toString());
    }
    
    public static XMLStreamReader getXmlStreamReaderForSnippet(String xmlSnippet) {
        return XmlStreamFactory.makeReader(new StringReader(xmlSnippet));
    }
    
    public static XMLStreamWriter getXmlStreamWriter(StringWriter sink) {
        return XmlStreamFactory.makeWriter(sink);
    }
    
}
