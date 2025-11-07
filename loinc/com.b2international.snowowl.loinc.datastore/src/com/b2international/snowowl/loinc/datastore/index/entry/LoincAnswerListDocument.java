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
package com.b2international.snowowl.loinc.datastore.index.entry;

import static com.b2international.index.query.Expressions.exactMatch;

import com.b2international.index.Doc;
import com.b2international.index.query.Expression;
import com.b2international.snowowl.loinc.core.domain.LoincAnswerList;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * Elasticsearch document representing a LOINC answer list.
 *
 * @since 9.9
 */
@Doc(type = LoincAnswerList.TYPE, revisionHash = {
	LoincDocument.Fields.ACTIVE,
	LoincDocument.Fields.EFFECTIVE_TIME,
	LoincDocument.Fields.RELEASED,
	LoincDocument.Fields.STATUS,
	LoincAnswerListDocument.Fields.ANSWER_LIST_ID,
	LoincAnswerListDocument.Fields.ANSWER_LIST_NAME
})
@JsonDeserialize(builder = LoincAnswerListDocument.Builder.class)
public final class LoincAnswerListDocument extends LoincDocument {

	public static Builder builder() {
		return new Builder();
	}

	public static class Fields extends LoincDocument.Fields {
		public static final String ANSWER_LIST_ID = "answerListId";
		public static final String ANSWER_LIST_NAME = "answerListName";
	}

	public static class Expressions extends LoincDocument.Expressions {

		private Expressions() {
		}

		public static Expression answerListId(String answerListId) {
			return exactMatch(Fields.ANSWER_LIST_ID, answerListId);
		}

		public static Expression answerListName(String answerListName) {
			return exactMatch(Fields.ANSWER_LIST_NAME, answerListName);
		}
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class Builder extends LoincDocument.Builder<Builder, LoincAnswerListDocument> {

		private String answerListId;
		private String answerListName;

		@JsonCreator
		public Builder() {
		}

		@Override
		protected Builder getSelf() {
			return this;
		}

		public Builder answerListId(String answerListId) {
			this.answerListId = answerListId;
			return getSelf();
		}

		public Builder answerListName(String answerListName) {
			this.answerListName = answerListName;
			return getSelf();
		}

		@Override
		public LoincAnswerListDocument build() {
			return new LoincAnswerListDocument(
				id,
				iconId,
				status,
				released,
				active,
				effectiveTime,
				answerListId,
				answerListName
			);
		}
	}

	private final String answerListId;
	private final String answerListName;

	private LoincAnswerListDocument(
			final String id,
			final String iconId,
			final String status,
			final Boolean released,
			final Boolean active,
			final Long effectiveTime,
			final String answerListId,
			final String answerListName) {
		super(id, iconId, status, released, active, effectiveTime);
		this.answerListId = answerListId;
		this.answerListName = answerListName;
	}

	public String getAnswerListId() {
		return answerListId;
	}

	public String getAnswerListName() {
		return answerListName;
	}

	@Override
	protected ToStringHelper doToString() {
		return super.doToString()
			.add("answerListId", answerListId)
			.add("answerListName", answerListName);
	}
}
