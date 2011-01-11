package com.cargosmart.b2b.edi.common.edifact;

import java.util.ArrayList;
import java.util.List;

import com.cargosmart.b2b.edi.common.CompositeField;
import com.cargosmart.b2b.edi.common.Document;
import com.cargosmart.b2b.edi.common.Envelope;
import com.cargosmart.b2b.edi.common.GroupEnvelope;
import com.cargosmart.b2b.edi.common.InterchangeEnvelope;
import com.cargosmart.b2b.edi.common.Segment;

public class EdifactInterchangeEnvelope extends Envelope implements InterchangeEnvelope {

	private Document document;
	private List<GroupEnvelope> groups = new ArrayList<GroupEnvelope>();
	private Segment levelAInterchangeEnvelope;

	public EdifactInterchangeEnvelope(Segment segment) {
	    super(segment);
	}
	
	public EdifactInterchangeEnvelope(Segment levelA, Segment levelB) {
		super(levelB);
		this.levelAInterchangeEnvelope = levelA;
	}

	/**
	 * @return the levelAInterchangeEnvelope
	 */
	public Segment getLevelAInterchangeEnvelope() {
		return levelAInterchangeEnvelope;
	}

	/**
	 * @param levelAInterchangeEnvelope the levelAInterchangeEnvelope to set
	 */
	public void setLevelAInterchangeEnvelope(Segment levelAInterchangeEnvelope) {
		this.levelAInterchangeEnvelope = levelAInterchangeEnvelope;
	}

	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setDocument(com.cargosmart.b2b.edi.common.Document)
     */
	public InterchangeEnvelope setDocument(Document document) {
		if (this.document != document) {
			this.document = document;
			this.document.setInterchangeEnvelope(this);
		}
		return this;
	}
	
	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getDocument()
     */
	public Document getDocument() {
		return document;
	}

	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#addGroupEnvelope(com.cargosmart.b2b.edi.common.GroupEnvelope)
     */
	public InterchangeEnvelope addGroupEnvelope(GroupEnvelope groupEnvelope) {
		groups.add(groupEnvelope);
		groupEnvelope.setInterchangeEnvelope(this);
		return this;
	}
	
	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#removeGroupEnvelope(com.cargosmart.b2b.edi.common.GroupEnvelope)
     */
	public InterchangeEnvelope removeGroupEnvelope(GroupEnvelope groupEnvelope) {
		groups.remove(groupEnvelope);
		groupEnvelope.setInterchangeEnvelope(null);
		return this;
	}
	
	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getGroups()
     */
	public List<GroupEnvelope> getGroups() {
		return groups;
	}

	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getField(int)
     */
	public CompositeField getField(int position) {
	    return segment.getField(position);
	}

    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getSenderQualifier()
     */
    public String getSenderQualifier() {
        return getField(2).getField(2).getValue().trim();
    }
    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setSenderQualifier(java.lang.String)
     */
    public void setSenderQualifier(String qualifier) {
        getField(2).getField(2).setValue(qualifier);
    }

    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getSenderId()
     */
    public String getSenderId() {
        return getField(2).getField(1).getValue().trim();
    }
    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setSenderId(java.lang.String)
     */
    public void setSenderId(String id) {
        getField(6).getField(1).setValue(id);
    }

    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getReceiverQualifier()
     */
    public String getReceiverQualifier() {
        return getField(3).getField(2).getValue().trim();
    }
    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setReceiverQualifier(java.lang.String)
     */
    public void setReceiverQualifier(String qualifier) {
        getField(3).getField(2).setValue(qualifier);
    }

    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getReceiverId()
     */
    public String getReceiverId() {
        return getField(3).getField(1).getValue().trim();
    }
    
    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setReceiverId(java.lang.String)
     */
    public void setReceiverId(String id) {
    	getField(3).getField(2).setValue(id);
    }

    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getVersion()
     */
    public String getVersion() {
        return getField(1).getField(2).getValue().trim();
    }
    
    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setVersion(java.lang.String)
     */
    public void setVersion(String version) {
    	getField(1).getField(2).setValue(version);
    }

    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getControlNumber()
     */
    public String getControlNumber() {
        return getField(5).getValue().trim();
    }
    
    /* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#setControlNumber(java.lang.String)
     */
    public void setControlNumber(String controlNum) {
    	getField(5).setValue(controlNum);
    }

	/* (non-Javadoc)
     * @see com.cargosmart.b2b.edi.common.InterchangeEnvelopeI#getSegment(java.lang.String)
     */
	public List<Segment> getSegment(String tag) {
		List<Segment> segments = new ArrayList<Segment>();
		for (GroupEnvelope group : groups) {
			segments.addAll(group.getSegment(tag));
		}
		return segments;
	}

}
