/*******************************************************************************
 * Copyright 2013 Raymond Chin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.github.raycw.edidom.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.raycw.edidom.common.Document;
import com.github.raycw.edidom.common.GroupEnvelope;
import com.github.raycw.edidom.common.InterchangeEnvelope;
import com.github.raycw.edidom.common.Segment;
import com.github.raycw.edidom.common.Transaction;
import com.github.raycw.edidom.input.EdiBuilder;
import com.github.raycw.edidom.input.X12Builder;

public class X12BuilderTest {
    
    public static final String X12DOC = 
        "ISA*00*          *00*          *01*GITHUB     *ZZ*ACSLTEST       *100716*1228*U*00401*000000004*0*P*:~\n" + 
        "GS*RO*GITHUB*ACSLTEST*20100716*1228*4*X*004010~\n" + 
        "ST*301*40001~\n"+
        "BEG*00*SA*95018017***950118~\n"+
        "SE*3*40001~\n" + 
        "GE*1*4~\n" + 
        "IEA*1*000000004~\n";
    private static String x12_301;
    private Document doc;
    private Document doc_301;

    private static EdiBuilder x12builder;
    
    @BeforeClass
    public static void onlyOnce() throws IOException {
    	x12builder = new X12Builder();
    	InputStream in = X12BuilderTest.class.getResourceAsStream("/X12.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		char[] buffer = new char[1024];
		int nRead;
		StringBuilder content = new StringBuilder();
		while ((nRead = reader.read(buffer, 0, 1024)) != -1) {
			content.append(buffer, 0, nRead);
		}
		x12_301 = content.toString();
    }
    
    @Before
    public void setUp() throws Exception {
        doc = x12builder.buildDocument(X12DOC);
        doc_301 = x12builder.buildDocument(x12_301);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBuildDocumentString() {
        assertEquals("*", doc.getElementSeparator());
        assertEquals("~\n", doc.getSegmentSeparator());
        assertEquals(":", doc.getSubElementSeparator());
        assertNotNull(doc.getInterchangeEnvelope());
        assertNotNull(doc.getInterchangeEnvelope().getGroups());
    }

    @Test
    public void testBuildISASegment() {
        InterchangeEnvelope isa = doc.getInterchangeEnvelope();
        assertEquals("01", isa.getSenderQualifier());
        assertEquals("GITHUB", isa.getSenderId());
        assertEquals("ZZ", isa.getReceiverQualifier());
        assertEquals("ACSLTEST", isa.getReceiverId());
        assertEquals("00401", isa.getVersion());
        assertEquals("000000004", isa.getControlNumber());
        
    }
    
    @Test
    public void testBuildGSSegment() {
    	GroupEnvelope gs = doc.getInterchangeEnvelope().getGroups().get(0);
    	assertEquals(1, doc.getInterchangeEnvelope().getGroups().size());
    	assertEquals("RO", gs.getFunctionalCode());
    	assertEquals("GITHUB", gs.getSenderCode());
    	assertEquals("ACSLTEST", gs.getReceiverCode());
    	assertEquals("4", gs.getControlNumber());
    	assertEquals("004010", gs.getVersion());
    }

    @Test
    public void testBuildSTSegment() {
    	Transaction st = doc.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0);
    	assertEquals(1, doc.getInterchangeEnvelope().getGroups().get(0).getTransactions().size());
    	assertEquals("301", st.getType());
    	assertEquals("40001", st.getControlNumber());
    }
    
    @Test
    public void testSegment() {
    	Segment segment = doc.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0).getSegments().get(0);
    	assertEquals("BEG", segment.getSegmentTag());
    	assertEquals("950118", segment.getField(6).getValue());
    	
    	List<Segment> segments = doc_301.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0).getSegments();
    	assertEquals(27, segments.size());
    	
    }
    
    @Test
    public void testNextSegment() {
        Segment segment = doc.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0).getSegments().get(0);
        assertEquals(segment.nextSegment(), null);
    }
    
    @Test
    public void testPreviousSegment() {
        Segment segment = doc.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0).getSegments().get(0);
        assertEquals(segment.previousSegment(), null);
    }

    @Test
    public void testNextSegments() {
        List<Segment> r4List = doc_301.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0).getSegments("R4");
        assertEquals(4, r4List.get(0).nextSegments("DTM").size());
        assertEquals(1, r4List.get(3).nextSegments("DTM").size());
    }
    
    @Test
    public void testPreviousSegments() {
        List<Segment> r4List = doc_301.getInterchangeEnvelope().getGroups().get(0).getTransactions().get(0).getSegments("R4");
        assertEquals(1, r4List.get(1).previousSegments("DTM").size());
        assertEquals(3, r4List.get(3).previousSegments("DTM").size());
    }
}
