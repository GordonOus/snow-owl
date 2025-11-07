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
package com.b2international.snowowl.loinc.datastore.request;

import java.util.List;

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;
import com.google.common.base.Strings;

import jakarta.validation.constraints.NotEmpty;

/**
 * Request to create a new LOINC code.
 *
 * @since 9.9
 */
public final class LoincCodeCreateRequest implements Request<TransactionContext, String> {

	@NotEmpty
	private String loincNum;

	@NotEmpty
	private String longCommonName;

	private String component;
	private String property;
	private String timeAspct;
	private String system;
	private String scaleTyp;
	private String methodTyp;
	private String classType;
	private String shortName;
	private String displayName;
	private String consumerName;
	private String orderObs;
	private List<String> relatedNames;
	private String versionFirstReleased;
	private String versionLastChanged;
	private String changeType;
	private String exampleUnits;
	private String exampleUcumUnits;
	private String answerListId;
	private String status = "ACTIVE";
	private Boolean active = Boolean.TRUE;

	LoincCodeCreateRequest() {
	}

	void setLoincNum(String loincNum) {
		this.loincNum = loincNum;
	}

	void setLongCommonName(String longCommonName) {
		this.longCommonName = longCommonName;
	}

	void setComponent(String component) {
		this.component = component;
	}

	void setProperty(String property) {
		this.property = property;
	}

	void setTimeAspct(String timeAspct) {
		this.timeAspct = timeAspct;
	}

	void setSystem(String system) {
		this.system = system;
	}

	void setScaleTyp(String scaleTyp) {
		this.scaleTyp = scaleTyp;
	}

	void setMethodTyp(String methodTyp) {
		this.methodTyp = methodTyp;
	}

	void setClassType(String classType) {
		this.classType = classType;
	}

	void setShortName(String shortName) {
		this.shortName = shortName;
	}

	void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	void setOrderObs(String orderObs) {
		this.orderObs = orderObs;
	}

	void setRelatedNames(List<String> relatedNames) {
		this.relatedNames = relatedNames;
	}

	void setVersionFirstReleased(String versionFirstReleased) {
		this.versionFirstReleased = versionFirstReleased;
	}

	void setVersionLastChanged(String versionLastChanged) {
		this.versionLastChanged = versionLastChanged;
	}

	void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	void setExampleUnits(String exampleUnits) {
		this.exampleUnits = exampleUnits;
	}

	void setExampleUcumUnits(String exampleUcumUnits) {
		this.exampleUcumUnits = exampleUcumUnits;
	}

	void setAnswerListId(String answerListId) {
		this.answerListId = answerListId;
	}

	void setStatus(String status) {
		this.status = status;
	}

	void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String execute(TransactionContext context) {
		final LoincCodeDocument code = LoincCodeDocument.builder()
			.id(loincNum)
			.loincNum(loincNum)
			.active(active)
			.status(status)
			.component(component)
			.property(property)
			.timeAspct(timeAspct)
			.system(system)
			.scaleTyp(scaleTyp)
			.methodTyp(methodTyp)
			.classType(classType)
			.longCommonName(longCommonName)
			.shortName(shortName)
			.displayName(displayName)
			.consumerName(consumerName)
			.orderObs(orderObs)
			.relatedNames(relatedNames)
			.versionFirstReleased(versionFirstReleased)
			.versionLastChanged(versionLastChanged)
			.changeType(changeType)
			.exampleUnits(exampleUnits)
			.exampleUcumUnits(exampleUcumUnits)
			.answerListId(answerListId)
			.released(Boolean.FALSE)
			.build();

		context.add(code);
		return code.getId();
	}
}
