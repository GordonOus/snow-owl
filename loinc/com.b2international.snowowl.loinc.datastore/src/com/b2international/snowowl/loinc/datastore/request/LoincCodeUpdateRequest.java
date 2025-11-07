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

/**
 * Request to update an existing LOINC code.
 *
 * @since 9.9
 */
public final class LoincCodeUpdateRequest implements Request<TransactionContext, Boolean> {

	private final String loincNum;

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
	private String versionLastChanged;
	private String changeType;
	private String exampleUnits;
	private String exampleUcumUnits;
	private String answerListId;
	private String status;
	private Boolean active;

	LoincCodeUpdateRequest(String loincNum) {
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
	public Boolean execute(TransactionContext context) {
		final LoincCodeDocument existingCode = context.lookup(loincNum, LoincCodeDocument.class);
		final LoincCodeDocument.Builder updatedCode = LoincCodeDocument.builder(existingCode);

		boolean changed = false;

		if (active != null && !active.equals(existingCode.isActive())) {
			updatedCode.active(active);
			changed = true;
		}

		if (status != null && !status.equals(existingCode.getStatus())) {
			updatedCode.status(status);
			changed = true;
		}

		if (longCommonName != null && !longCommonName.equals(existingCode.getLongCommonName())) {
			updatedCode.longCommonName(longCommonName);
			changed = true;
		}

		if (component != null && !component.equals(existingCode.getComponent())) {
			updatedCode.component(component);
			changed = true;
		}

		if (property != null && !property.equals(existingCode.getProperty())) {
			updatedCode.property(property);
			changed = true;
		}

		if (timeAspct != null && !timeAspct.equals(existingCode.getTimeAspct())) {
			updatedCode.timeAspct(timeAspct);
			changed = true;
		}

		if (system != null && !system.equals(existingCode.getSystem())) {
			updatedCode.system(system);
			changed = true;
		}

		if (scaleTyp != null && !scaleTyp.equals(existingCode.getScaleTyp())) {
			updatedCode.scaleTyp(scaleTyp);
			changed = true;
		}

		if (methodTyp != null && !methodTyp.equals(existingCode.getMethodTyp())) {
			updatedCode.methodTyp(methodTyp);
			changed = true;
		}

		if (classType != null && !classType.equals(existingCode.getClassType())) {
			updatedCode.classType(classType);
			changed = true;
		}

		if (shortName != null && !shortName.equals(existingCode.getShortName())) {
			updatedCode.shortName(shortName);
			changed = true;
		}

		if (displayName != null && !displayName.equals(existingCode.getDisplayName())) {
			updatedCode.displayName(displayName);
			changed = true;
		}

		if (consumerName != null && !consumerName.equals(existingCode.getConsumerName())) {
			updatedCode.consumerName(consumerName);
			changed = true;
		}

		if (orderObs != null && !orderObs.equals(existingCode.getOrderObs())) {
			updatedCode.orderObs(orderObs);
			changed = true;
		}

		if (relatedNames != null) {
			updatedCode.relatedNames(relatedNames);
			changed = true;
		}

		if (versionLastChanged != null && !versionLastChanged.equals(existingCode.getVersionLastChanged())) {
			updatedCode.versionLastChanged(versionLastChanged);
			changed = true;
		}

		if (changeType != null && !changeType.equals(existingCode.getChangeType())) {
			updatedCode.changeType(changeType);
			changed = true;
		}

		if (exampleUnits != null && !exampleUnits.equals(existingCode.getExampleUnits())) {
			updatedCode.exampleUnits(exampleUnits);
			changed = true;
		}

		if (exampleUcumUnits != null && !exampleUcumUnits.equals(existingCode.getExampleUcumUnits())) {
			updatedCode.exampleUcumUnits(exampleUcumUnits);
			changed = true;
		}

		if (answerListId != null && !answerListId.equals(existingCode.getAnswerListId())) {
			updatedCode.answerListId(answerListId);
			changed = true;
		}

		if (changed) {
			context.update(existingCode, updatedCode.build());
		}

		return changed;
	}
}
