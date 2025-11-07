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
import static com.b2international.index.query.Expressions.matchTextAll;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.b2international.index.Doc;
import com.b2international.index.Text;
import com.b2international.index.query.Expression;
import com.b2international.snowowl.loinc.core.domain.LoincCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * Elasticsearch document representing a LOINC code.
 *
 * @since 9.9
 */
@Doc(type = LoincCode.TYPE, revisionHash = {
	LoincDocument.Fields.ACTIVE,
	LoincDocument.Fields.EFFECTIVE_TIME,
	LoincDocument.Fields.RELEASED,
	LoincDocument.Fields.STATUS,
	LoincCodeDocument.Fields.LOINC_NUM,
	LoincCodeDocument.Fields.COMPONENT,
	LoincCodeDocument.Fields.PROPERTY,
	LoincCodeDocument.Fields.TIME_ASPCT,
	LoincCodeDocument.Fields.SYSTEM,
	LoincCodeDocument.Fields.SCALE_TYP,
	LoincCodeDocument.Fields.METHOD_TYP
})
@JsonDeserialize(builder = LoincCodeDocument.Builder.class)
public final class LoincCodeDocument extends LoincDocument {

	public static Builder builder() {
		return new Builder();
	}

	public static class Fields extends LoincDocument.Fields {
		public static final String LOINC_NUM = "loincNum";
		public static final String COMPONENT = "component";
		public static final String PROPERTY = "property";
		public static final String TIME_ASPCT = "timeAspct";
		public static final String SYSTEM = "system";
		public static final String SCALE_TYP = "scaleTyp";
		public static final String METHOD_TYP = "methodTyp";
		public static final String CLASS_TYPE = "classType";
		public static final String LONG_COMMON_NAME = "longCommonName";
		public static final String SHORT_NAME = "shortName";
		public static final String ORDER_OBS = "orderObs";
		public static final String ANSWER_LIST_ID = "answerListId";
		public static final String SEARCH_TEXT = "searchText";
	}

	public static class Expressions extends LoincDocument.Expressions {

		private Expressions() {
		}

		public static Expression loincNum(String loincNum) {
			return exactMatch(Fields.LOINC_NUM, loincNum);
		}

		public static Expression loincNums(Collection<String> loincNums) {
			return matchAny(Fields.LOINC_NUM, loincNums);
		}

		public static Expression component(String component) {
			return exactMatch(Fields.COMPONENT, component);
		}

		public static Expression property(String property) {
			return exactMatch(Fields.PROPERTY, property);
		}

		public static Expression scaleTyp(String scaleTyp) {
			return exactMatch(Fields.SCALE_TYP, scaleTyp);
		}

		public static Expression scaleTyps(Collection<String> scaleTyps) {
			return matchAny(Fields.SCALE_TYP, scaleTyps);
		}

		public static Expression classType(String classType) {
			return exactMatch(Fields.CLASS_TYPE, classType);
		}

		public static Expression classTypes(Collection<String> classTypes) {
			return matchAny(Fields.CLASS_TYPE, classTypes);
		}

		public static Expression orderObs(String orderObs) {
			return exactMatch(Fields.ORDER_OBS, orderObs);
		}

		public static Expression answerListId(String answerListId) {
			return exactMatch(Fields.ANSWER_LIST_ID, answerListId);
		}

		public static Expression searchText(String searchText) {
			return matchTextAll(Fields.SEARCH_TEXT, searchText);
		}
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class Builder extends LoincDocument.Builder<Builder, LoincCodeDocument> {

		private String loincNum;
		private String component;
		private String property;
		private String timeAspct;
		private String system;
		private String scaleTyp;
		private String methodTyp;
		private String classType;
		private String longCommonName;
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

		@JsonCreator
		public Builder() {
		}

		@Override
		protected Builder getSelf() {
			return this;
		}

		public Builder loincNum(String loincNum) {
			this.loincNum = loincNum;
			return getSelf();
		}

		public Builder component(String component) {
			this.component = component;
			return getSelf();
		}

		public Builder property(String property) {
			this.property = property;
			return getSelf();
		}

		public Builder timeAspct(String timeAspct) {
			this.timeAspct = timeAspct;
			return getSelf();
		}

		public Builder system(String system) {
			this.system = system;
			return getSelf();
		}

		public Builder scaleTyp(String scaleTyp) {
			this.scaleTyp = scaleTyp;
			return getSelf();
		}

		public Builder methodTyp(String methodTyp) {
			this.methodTyp = methodTyp;
			return getSelf();
		}

		public Builder classType(String classType) {
			this.classType = classType;
			return getSelf();
		}

		public Builder longCommonName(String longCommonName) {
			this.longCommonName = longCommonName;
			return getSelf();
		}

		public Builder shortName(String shortName) {
			this.shortName = shortName;
			return getSelf();
		}

		public Builder displayName(String displayName) {
			this.displayName = displayName;
			return getSelf();
		}

		public Builder consumerName(String consumerName) {
			this.consumerName = consumerName;
			return getSelf();
		}

		public Builder orderObs(String orderObs) {
			this.orderObs = orderObs;
			return getSelf();
		}

		public Builder relatedNames(List<String> relatedNames) {
			this.relatedNames = relatedNames;
			return getSelf();
		}

		public Builder versionFirstReleased(String versionFirstReleased) {
			this.versionFirstReleased = versionFirstReleased;
			return getSelf();
		}

		public Builder versionLastChanged(String versionLastChanged) {
			this.versionLastChanged = versionLastChanged;
			return getSelf();
		}

		public Builder changeType(String changeType) {
			this.changeType = changeType;
			return getSelf();
		}

		public Builder exampleUnits(String exampleUnits) {
			this.exampleUnits = exampleUnits;
			return getSelf();
		}

		public Builder exampleUcumUnits(String exampleUcumUnits) {
			this.exampleUcumUnits = exampleUcumUnits;
			return getSelf();
		}

		public Builder answerListId(String answerListId) {
			this.answerListId = answerListId;
			return getSelf();
		}

		@Override
		public LoincCodeDocument build() {
			final LoincCodeDocument doc = new LoincCodeDocument(
				id,
				iconId,
				status,
				released,
				active,
				effectiveTime,
				loincNum,
				component,
				property,
				timeAspct,
				system,
				scaleTyp,
				methodTyp,
				classType,
				longCommonName,
				shortName,
				displayName,
				consumerName,
				orderObs,
				relatedNames,
				versionFirstReleased,
				versionLastChanged,
				changeType,
				exampleUnits,
				exampleUcumUnits,
				answerListId
			);
			return doc;
		}
	}

	private final String loincNum;
	private final String component;
	private final String property;
	private final String timeAspct;
	private final String system;
	private final String scaleTyp;
	private final String methodTyp;
	private final String classType;
	private final String longCommonName;
	private final String shortName;
	private final String displayName;
	private final String consumerName;
	private final String orderObs;
	private final List<String> relatedNames;
	private final String versionFirstReleased;
	private final String versionLastChanged;
	private final String changeType;
	private final String exampleUnits;
	private final String exampleUcumUnits;
	private final String answerListId;

	@Text(analyzer = "standard")
	@JsonIgnore
	private final String searchText;

	private LoincCodeDocument(
			final String id,
			final String iconId,
			final String status,
			final Boolean released,
			final Boolean active,
			final Long effectiveTime,
			final String loincNum,
			final String component,
			final String property,
			final String timeAspct,
			final String system,
			final String scaleTyp,
			final String methodTyp,
			final String classType,
			final String longCommonName,
			final String shortName,
			final String displayName,
			final String consumerName,
			final String orderObs,
			final List<String> relatedNames,
			final String versionFirstReleased,
			final String versionLastChanged,
			final String changeType,
			final String exampleUnits,
			final String exampleUcumUnits,
			final String answerListId) {
		super(id, iconId, status, released, active, effectiveTime);
		this.loincNum = loincNum;
		this.component = component;
		this.property = property;
		this.timeAspct = timeAspct;
		this.system = system;
		this.scaleTyp = scaleTyp;
		this.methodTyp = methodTyp;
		this.classType = classType;
		this.longCommonName = longCommonName;
		this.shortName = shortName;
		this.displayName = displayName;
		this.consumerName = consumerName;
		this.orderObs = orderObs;
		this.relatedNames = relatedNames == null ? Collections.emptyList() : relatedNames;
		this.versionFirstReleased = versionFirstReleased;
		this.versionLastChanged = versionLastChanged;
		this.changeType = changeType;
		this.exampleUnits = exampleUnits;
		this.exampleUcumUnits = exampleUcumUnits;
		this.answerListId = answerListId;

		// Build searchable text
		this.searchText = buildSearchText();
	}

	private String buildSearchText() {
		StringBuilder sb = new StringBuilder();
		if (loincNum != null) sb.append(loincNum).append(" ");
		if (longCommonName != null) sb.append(longCommonName).append(" ");
		if (shortName != null) sb.append(shortName).append(" ");
		if (component != null) sb.append(component).append(" ");
		if (consumerName != null) sb.append(consumerName).append(" ");
		if (relatedNames != null) {
			for (String name : relatedNames) {
				sb.append(name).append(" ");
			}
		}
		return sb.toString().trim();
	}

	public String getLoincNum() {
		return loincNum;
	}

	public String getComponent() {
		return component;
	}

	public String getProperty() {
		return property;
	}

	public String getTimeAspct() {
		return timeAspct;
	}

	public String getSystem() {
		return system;
	}

	public String getScaleTyp() {
		return scaleTyp;
	}

	public String getMethodTyp() {
		return methodTyp;
	}

	public String getClassType() {
		return classType;
	}

	public String getLongCommonName() {
		return longCommonName;
	}

	public String getShortName() {
		return shortName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public String getOrderObs() {
		return orderObs;
	}

	public List<String> getRelatedNames() {
		return relatedNames;
	}

	public String getVersionFirstReleased() {
		return versionFirstReleased;
	}

	public String getVersionLastChanged() {
		return versionLastChanged;
	}

	public String getChangeType() {
		return changeType;
	}

	public String getExampleUnits() {
		return exampleUnits;
	}

	public String getExampleUcumUnits() {
		return exampleUcumUnits;
	}

	public String getAnswerListId() {
		return answerListId;
	}

	public String getSearchText() {
		return searchText;
	}

	@Override
	protected ToStringHelper doToString() {
		return super.doToString()
			.add("loincNum", loincNum)
			.add("longCommonName", longCommonName)
			.add("component", component)
			.add("scaleTyp", scaleTyp);
	}
}
