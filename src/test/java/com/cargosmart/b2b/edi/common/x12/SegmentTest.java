package com.cargosmart.b2b.edi.common.x12;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cargosmart.b2b.edi.common.CompositeField;
import com.cargosmart.b2b.edi.common.Document;
import com.cargosmart.b2b.edi.common.Field;
import com.cargosmart.b2b.edi.common.GroupEnvelope;
import com.cargosmart.b2b.edi.common.Segment;
import com.cargosmart.b2b.edi.common.Transaction;
import com.cargosmart.b2b.edi.common.x12.X12Document;
import com.cargosmart.b2b.edi.common.x12.X12GroupEnvelope;
import com.cargosmart.b2b.edi.common.x12.X12InterchangeEnvelope;

public class SegmentTest {

	private Document document;
	private X12InterchangeEnvelope interchange;
	private GroupEnvelope group;
    private Transaction txn;
    private Segment segment;
    private Field field;
    private CompositeField cField;
    private static final String[] X12_FIELDS = { 
    	    "ISA", "00", "          ", "00",
            "          ", "01", "CARGOSMART     ", "ZZ", "ACSLTEST       ",
            "100716", "1228", "U", "00401", "000000004", "0", "P", ":" };
    private static final String[] TXN_FIELDS = {"ST","301","40001"};
    private static final String[] GRP_FIELDS = {"GS","RO","CARGOSMART","ACSLTEST","20100716","1228","4","X","004010"};
    
    @Before
    public void setUp() throws Exception {
    	document = new X12Document();
    	interchange = new X12InterchangeEnvelope(new Segment(X12_FIELDS));
        group = new X12GroupEnvelope(new Segment(GRP_FIELDS));
    	txn = new X12Transaction(new Segment(TXN_FIELDS));
        segment = new Segment(X12_FIELDS);
        field = new Field();
        cField = new CompositeField();
        txn.addSegment(segment);
        group.addTransaction(txn);
        interchange.addGroupEnvelope(group);
        document.setInterchangeEnvelope(interchange);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddField() {
        segment.addField(field);
        assertEquals(18, segment.getFields().size());

        segment.addCompositeField(cField);
        assertEquals(19, segment.getFields().size());
    }
    
    @Test
    public void testGetField() {
        assertEquals("00", segment.getField(1).getValue());
        assertEquals("          ", segment.getField(2).getValue());
        assertEquals("00", segment.getField(3).getValue());
        assertEquals("          ", segment.getField(4).getValue());
        assertEquals("01", segment.getField(5).getValue());
        assertEquals("CARGOSMART     ", segment.getField(6).getValue());
        assertEquals("P", segment.getField(15).getValue());
        assertEquals(":", segment.getField(16).getValue());
    }
    
    @Test
    public void testGetSegmentFromTransaction() {
    	List<Segment> segments = txn.getSegments("ISA");
    	assertEquals(1, segments.size());
    }
    
    @Test
    public void testGetSegmentFromGroupEnvelop() {
    	List<Segment> segments = group.getSegment("ISA");
    	assertEquals(1, segments.size());
    }
    
    @Test
    public void testGetSegmentFromInterchangeEnvelop() {
    	List<Segment> segments = interchange.getSegment("ISA");
    	assertEquals(1, segments.size());
    }

    @Test
    public void testGetSegmentFromDocument() {
    	List<Segment> segments = document.getSegment("ISA");
    	assertEquals(1, segments.size());
    }

}