/*
 * Copyright 2011-2025 B2i Healthcare, https://b2ihealthcare.com
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
package com.b2international.snowowl.loinc.core.domain;

import java.util.List;

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.core.request.IndexResourceRequestBuilder;
import com.b2international.snowowl.core.terminology.ComponentCategory;
import com.b2international.snowowl.core.terminology.TerminologyComponent;
import com.b2international.snowowl.loinc.common.LoincHeaders;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a LOINC code.
 * <br>
 * LOINC codes returned by search requests are populated based on the expand parameters passed into the
 * {@link IndexResourceRequestBuilder#setExpand(String)} methods.
 * <p>
 * The supported expand parameters are:
 * <ul>
 * <li>{@code parts()} - returns the parts that compose this LOINC code</li>
 * <li>{@code answerList()} - returns the answer list if this is a coded observation</li>
 * </ul>
 *
 * @since 9.9
 */
@TerminologyComponent(
	name = "LOINC Code",
	componentCategory = ComponentCategory.CONCEPT,
	docType = LoincCodeDocument.class
)
public final class LoincCode extends LoincComponent {

	private static final long serialVersionUID = 1L;

	public static final String TYPE = "code";

	/**
	 * Enumerates expandable property keys.
	 *
	 * @since 9.9
	 */
	public static final class Expand extends LoincComponent.Expand {
		public static final String PARTS = "parts";
		public static final String ANSWER_LIST = "answerList";
	}

	/**
	 * Field names for LOINC codes.
	 *
	 * @since 9.9
	 */
	public static final class Fields extends LoincComponent.Fields {
		public static final String LOINC_NUM = LoincHeaders.LOINC_NUM;
		public static final String COMPONENT = LoincHeaders.COMPONENT;
		public static final String PROPERTY = LoincHeaders.PROPERTY;
		public static final String TIME_ASPCT = LoincHeaders.TIME_ASPCT;
		public static final String SYSTEM = LoincHeaders.SYSTEM;
		public static final String SCALE_TYP = LoincHeaders.SCALE_TYP;
		public static final String METHOD_TYP = LoincHeaders.METHOD_TYP;
		public static final String CLASS = LoincHeaders.CLASS;
		public static final String LONG_COMMON_NAME = LoincHeaders.LONG_COMMON_NAME;
		public static final String SHORT_NAME = LoincHeaders.SHORT_NAME;
	}

	// Core LOINC axes
	private String loincNum;           // LOINC identifier (e.g., "10334-4")
	private String component;          // Component/Analyte (e.g., "Glucose")
	private String property;           // Property (e.g., "MCnc")
	private String timeAspct;          // Time Aspect (e.g., "Pt")
	private String system;             // System/Sample (e.g., "Ser/Plas")
	private String scaleTyp;           // Scale Type (e.g., "Qn")
	private String methodTyp;          // Method Type (e.g., "Enzymatic")

	// Display names
	private String longCommonName;     // Full display name
	private String shortName;          // Abbreviated name
	private String displayName;        // Preferred display name
	private String consumerName;       // Consumer-friendly name

	// Classification
	private String classType;          // LOINC class (e.g., "CHEM")
	private String orderObs;           // Order/Observation classification
	private List<String> relatedNames; // Alternative names

	// Version information
	private String versionFirstReleased;
	private String versionLastChanged;
	private String changeType;

	// Example units
	private String exampleUnits;
	private String exampleUcumUnits;

	// Answer list (for coded observations)
	private String answerListId;
	private LoincAnswerList answerList;

	// Related parts (expandable)
	private LoincParts parts;

	public LoincCode() {
	}

	public LoincCode(String loincNum) {
		setId(loincNum);
		this.loincNum = loincNum;
	}

	@Override
	public String getComponentType() {
		return TYPE;
	}

	public String getLoincNum() {
		return loincNum;
	}

	public void setLoincNum(String loincNum) {
		this.loincNum = loincNum;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTimeAspct() {
		return timeAspct;
	}

	public void setTimeAspct(String timeAspct) {
		this.timeAspct = timeAspct;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getScaleTyp() {
		return scaleTyp;
	}

	public void setScaleTyp(String scaleTyp) {
		this.scaleTyp = scaleTyp;
	}

	public String getMethodTyp() {
		return methodTyp;
	}

	public void setMethodTyp(String methodTyp) {
		this.methodTyp = methodTyp;
	}

	public String getLongCommonName() {
		return longCommonName;
	}

	public void setLongCommonName(String longCommonName) {
		this.longCommonName = longCommonName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getOrderObs() {
		return orderObs;
	}

	public void setOrderObs(String orderObs) {
		this.orderObs = orderObs;
	}

	public List<String> getRelatedNames() {
		return relatedNames;
	}

	public void setRelatedNames(List<String> relatedNames) {
		this.relatedNames = relatedNames;
	}

	public String getVersionFirstReleased() {
		return versionFirstReleased;
	}

	public void setVersionFirstReleased(String versionFirstReleased) {
		this.versionFirstReleased = versionFirstReleased;
	}

	public String getVersionLastChanged() {
		return versionLastChanged;
	}

	public void setVersionLastChanged(String versionLastChanged) {
		this.versionLastChanged = versionLastChanged;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getExampleUnits() {
		return exampleUnits;
	}

	public void setExampleUnits(String exampleUnits) {
		this.exampleUnits = exampleUnits;
	}

	public String getExampleUcumUnits() {
		return exampleUcumUnits;
	}

	public void setExampleUcumUnits(String exampleUcumUnits) {
		this.exampleUcumUnits = exampleUcumUnits;
	}

	public String getAnswerListId() {
		return answerListId;
	}

	public void setAnswerListId(String answerListId) {
		this.answerListId = answerListId;
	}

	public LoincAnswerList getAnswerList() {
		return answerList;
	}

	public void setAnswerList(LoincAnswerList answerList) {
		this.answerList = answerList;
	}

	public LoincParts getParts() {
		return parts;
	}

	public void setParts(LoincParts parts) {
		this.parts = parts;
	}

	@Override
	public Request<TransactionContext, String> toCreateRequest(String containerId) {
		// TODO: Implement when request builders are created
		throw new UnsupportedOperationException("LOINC code creation not yet implemented");
	}

	@Override
	public Request<TransactionContext, Boolean> toUpdateRequest() {
		// TODO: Implement when request builders are created
		throw new UnsupportedOperationException("LOINC code update not yet implemented");
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LoincCode [loincNum=");
		builder.append(loincNum);
		builder.append(", longCommonName=");
		builder.append(longCommonName);
		builder.append(", isActive()=");
		builder.append(isActive());
		builder.append(", status=");
		builder.append(getStatus());
		builder.append("]");
		return builder.toString();
	}
}
