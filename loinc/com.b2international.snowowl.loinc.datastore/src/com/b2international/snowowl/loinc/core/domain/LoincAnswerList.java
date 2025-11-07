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
import com.b2international.snowowl.core.terminology.ComponentCategory;
import com.b2international.snowowl.core.terminology.TerminologyComponent;
import com.b2international.snowowl.loinc.common.LoincHeaders;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincAnswerListDocument;

/**
 * Represents a LOINC answer list.
 * <br>
 * LOINC answer lists define the set of valid coded answers for observations.
 *
 * @since 9.9
 */
@TerminologyComponent(
	name = "LOINC Answer List",
	componentCategory = ComponentCategory.CONCEPT,
	docType = LoincAnswerListDocument.class
)
public final class LoincAnswerList extends LoincComponent {

	private static final long serialVersionUID = 1L;

	public static final String TYPE = "answerList";

	/**
	 * Field names for LOINC answer lists.
	 *
	 * @since 9.9
	 */
	public static final class Fields extends LoincComponent.Fields {
		public static final String ANSWER_LIST_ID = LoincHeaders.ANSWER_LIST_ID;
		public static final String ANSWER_LIST_NAME = LoincHeaders.ANSWER_LIST_NAME;
	}

	private String answerListId;        // Answer list identifier
	private String answerListName;      // Answer list name
	private List<Answer> answers;       // List of possible answers

	/**
	 * Represents a single answer within an answer list.
	 */
	public static class Answer {
		private String answerStringId;  // Answer code/identifier
		private String displayText;     // Human-readable text
		private Integer sequenceNumber; // Order within the list
		private String loincCode;       // Associated LOINC code (if any)
		private String extCodeId;       // External code system identifier
		private String extCodeDisplayName; // External code display name
		private String extCodeSystem;   // External code system (e.g., SNOMED CT)

		public String getAnswerStringId() {
			return answerStringId;
		}

		public void setAnswerStringId(String answerStringId) {
			this.answerStringId = answerStringId;
		}

		public String getDisplayText() {
			return displayText;
		}

		public void setDisplayText(String displayText) {
			this.displayText = displayText;
		}

		public Integer getSequenceNumber() {
			return sequenceNumber;
		}

		public void setSequenceNumber(Integer sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}

		public String getLoincCode() {
			return loincCode;
		}

		public void setLoincCode(String loincCode) {
			this.loincCode = loincCode;
		}

		public String getExtCodeId() {
			return extCodeId;
		}

		public void setExtCodeId(String extCodeId) {
			this.extCodeId = extCodeId;
		}

		public String getExtCodeDisplayName() {
			return extCodeDisplayName;
		}

		public void setExtCodeDisplayName(String extCodeDisplayName) {
			this.extCodeDisplayName = extCodeDisplayName;
		}

		public String getExtCodeSystem() {
			return extCodeSystem;
		}

		public void setExtCodeSystem(String extCodeSystem) {
			this.extCodeSystem = extCodeSystem;
		}
	}

	public LoincAnswerList() {
	}

	public LoincAnswerList(String answerListId) {
		setId(answerListId);
		this.answerListId = answerListId;
	}

	@Override
	public String getComponentType() {
		return TYPE;
	}

	public String getAnswerListId() {
		return answerListId;
	}

	public void setAnswerListId(String answerListId) {
		this.answerListId = answerListId;
	}

	public String getAnswerListName() {
		return answerListName;
	}

	public void setAnswerListName(String answerListName) {
		this.answerListName = answerListName;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public Request<TransactionContext, String> toCreateRequest(String containerId) {
		// TODO: Implement when request builders are created
		throw new UnsupportedOperationException("LOINC answer list creation not yet implemented");
	}

	@Override
	public Request<TransactionContext, Boolean> toUpdateRequest() {
		// TODO: Implement when request builders are created
		throw new UnsupportedOperationException("LOINC answer list update not yet implemented");
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LoincAnswerList [answerListId=");
		builder.append(answerListId);
		builder.append(", answerListName=");
		builder.append(answerListName);
		builder.append(", answers count=");
		builder.append(answers != null ? answers.size() : 0);
		builder.append("]");
		return builder.toString();
	}
}
