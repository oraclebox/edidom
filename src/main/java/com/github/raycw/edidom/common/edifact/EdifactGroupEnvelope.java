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
package com.github.raycw.edidom.common.edifact;

import java.util.ArrayList;
import java.util.List;

import com.github.raycw.edidom.common.CompositeField;
import com.github.raycw.edidom.common.Envelope;
import com.github.raycw.edidom.common.GroupEnvelope;
import com.github.raycw.edidom.common.InterchangeEnvelope;
import com.github.raycw.edidom.common.Segment;
import com.github.raycw.edidom.common.Transaction;

public class EdifactGroupEnvelope extends Envelope implements GroupEnvelope{

	private InterchangeEnvelope interchangeEnvelope;
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	public EdifactGroupEnvelope(Segment segment) {
		super(segment);
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param orig
	 */
	public EdifactGroupEnvelope(EdifactGroupEnvelope orig) {
		super(new Segment(orig.segment));
	}

	public GroupEnvelope setInterchangeEnvelope(InterchangeEnvelope interchangeEnvelope) {
		this.interchangeEnvelope = interchangeEnvelope;
		return this;
	}
	
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getInterchangeEnvelope()
     */
	public InterchangeEnvelope getInterchangeEnvelope() {
		return interchangeEnvelope;
	}
	
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getField(int)
     */
	public CompositeField getField(int position) {
		return segment.getField(position);
	}
	
	public GroupEnvelope detach() {
		if (interchangeEnvelope != null) {
			interchangeEnvelope.removeGroupEnvelope(this);
		}
		return this;
	}
	
	public GroupEnvelope addTransaction(Transaction txn) {
		transactions.add(txn);
		txn.setGroupEnvelope(this);
		return this;
	}
	
	public GroupEnvelope removeTransaction(Transaction txn) {
		transactions.remove(txn);
		txn.setGroupEnvelope(null);
		return this;
	}
	
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getTransactions()
     */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getFunctionalCode()
     */
	public String getFunctionalCode() {
		return getField(1).getField(1).getValue().trim();
	}
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#setFunctionalCode(java.lang.String)
     */
	public void setFunctionalCode(String code) {
		getField(1).setFieldValue(1, code);
	}

	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getSenderCode()
     */
	public String getSenderCode() {
		return getField(2).getField(1).getValue().trim();
	}
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#setSenderCode(java.lang.String)
     */
	public void setSenderCode(String sender) {
		getField(2).setFieldValue(1, sender);
	}

	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getReceiverCode()
     */
	public String getReceiverCode() {
		return getField(3).getField(1).getValue().trim();
	}
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#setReceiverCode(java.lang.String)
     */
	public void setReceiverCode(String receiver) {
		getField(3).setFieldValue(1, receiver);
	}

	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getControlNumber()
     */
	public String getControlNumber() {
		return getField(5).getField(1).getValue().trim();
	}
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#setControlNumber(java.lang.String)
     */
	public void setControlNumber(String controlNum) {
		getField(5).setFieldValue(1, controlNum);
	}

	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getVersion()
     */
	public String getVersion() {
		return getField(8).getField(2).getValue().trim();
	}
	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#setVersion(java.lang.String)
     */
	public void setVersion(String version) {
		getField(8).setFieldValue(2, version);
	}

	/* (non-Javadoc)
     * @see com.github.raycw.edidom.common.GroupEnvelope#getSegment(java.lang.String)
     */
	public List<Segment> getSegment(String tag) {
		List<Segment> segments = new ArrayList<Segment>();
		for (Transaction txn : transactions) {
			segments.addAll(txn.getSegments(tag));
		}
		return segments;
	}

	public GroupEnvelope copy() {
		return new EdifactGroupEnvelope(this);
	}

}
