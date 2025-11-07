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
import static com.b2international.index.query.Expressions.matchAny;

import java.util.Collection;

import com.b2international.index.Doc;
import com.b2international.index.query.Expression;
import com.b2international.snowowl.loinc.core.domain.LoincPart;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * Elasticsearch document representing a LOINC part.
 *
 * @since 9.9
 */
@Doc(type = LoincPart.TYPE, revisionHash = {
	LoincDocument.Fields.ACTIVE,
	LoincDocument.Fields.EFFECTIVE_TIME,
	LoincDocument.Fields.RELEASED,
	LoincDocument.Fields.STATUS,
	LoincPartDocument.Fields.PART_NUMBER,
	LoincPartDocument.Fields.PART_TYPE_NAME,
	LoincPartDocument.Fields.PART_NAME
})
@JsonDeserialize(builder = LoincPartDocument.Builder.class)
public final class LoincPartDocument extends LoincDocument {

	public static Builder builder() {
		return new Builder();
	}

	public static class Fields extends LoincDocument.Fields {
		public static final String PART_NUMBER = "partNumber";
		public static final String PART_TYPE_NAME = "partTypeName";
		public static final String PART_NAME = "partName";
		public static final String PART_DISPLAY_NAME = "partDisplayName";
	}

	public static class Expressions extends LoincDocument.Expressions {

		private Expressions() {
		}

		public static Expression partNumber(String partNumber) {
			return exactMatch(Fields.PART_NUMBER, partNumber);
		}

		public static Expression partNumbers(Collection<String> partNumbers) {
			return matchAny(Fields.PART_NUMBER, partNumbers);
		}

		public static Expression partTypeName(String partTypeName) {
			return exactMatch(Fields.PART_TYPE_NAME, partTypeName);
		}

		public static Expression partTypeNames(Collection<String> partTypeNames) {
			return matchAny(Fields.PART_TYPE_NAME, partTypeNames);
		}
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class Builder extends LoincDocument.Builder<Builder, LoincPartDocument> {

		private String partNumber;
		private String partTypeName;
		private String partName;
		private String partDisplayName;

		@JsonCreator
		public Builder() {
		}

		@Override
		protected Builder getSelf() {
			return this;
		}

		public Builder partNumber(String partNumber) {
			this.partNumber = partNumber;
			return getSelf();
		}

		public Builder partTypeName(String partTypeName) {
			this.partTypeName = partTypeName;
			return getSelf();
		}

		public Builder partName(String partName) {
			this.partName = partName;
			return getSelf();
		}

		public Builder partDisplayName(String partDisplayName) {
			this.partDisplayName = partDisplayName;
			return getSelf();
		}

		@Override
		public LoincPartDocument build() {
			return new LoincPartDocument(
				id,
				iconId,
				status,
				released,
				active,
				effectiveTime,
				partNumber,
				partTypeName,
				partName,
				partDisplayName
			);
		}
	}

	private final String partNumber;
	private final String partTypeName;
	private final String partName;
	private final String partDisplayName;

	private LoincPartDocument(
			final String id,
			final String iconId,
			final String status,
			final Boolean released,
			final Boolean active,
			final Long effectiveTime,
			final String partNumber,
			final String partTypeName,
			final String partName,
			final String partDisplayName) {
		super(id, iconId, status, released, active, effectiveTime);
		this.partNumber = partNumber;
		this.partTypeName = partTypeName;
		this.partName = partName;
		this.partDisplayName = partDisplayName;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public String getPartTypeName() {
		return partTypeName;
	}

	public String getPartName() {
		return partName;
	}

	public String getPartDisplayName() {
		return partDisplayName;
	}

	@Override
	protected ToStringHelper doToString() {
		return super.doToString()
			.add("partNumber", partNumber)
			.add("partTypeName", partTypeName)
			.add("partName", partName);
	}
}
