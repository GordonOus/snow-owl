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
package com.b2international.snowowl.loinc.common;

/**
 * LOINC distribution file field names and common headers.
 *
 * @since 9.9
 */
public class LoincHeaders {

	/**
	 * Common LOINC code fields
	 */
	public static final String LOINC_NUM = "LOINC_NUM";
	public static final String COMPONENT = "COMPONENT";
	public static final String PROPERTY = "PROPERTY";
	public static final String TIME_ASPCT = "TIME_ASPCT";
	public static final String SYSTEM = "SYSTEM";
	public static final String SCALE_TYP = "SCALE_TYP";
	public static final String METHOD_TYP = "METHOD_TYP";
	public static final String CLASS = "CLASS";
	public static final String STATUS = "STATUS";

	/**
	 * Display names
	 */
	public static final String LONG_COMMON_NAME = "LONG_COMMON_NAME";
	public static final String SHORT_NAME = "SHORTNAME";
	public static final String DISPLAY_NAME = "DisplayName";

	/**
	 * Related names and synonyms
	 */
	public static final String RELATED_NAMES2 = "RELATEDNAMES2";
	public static final String CONSUMER_NAME = "CONSUMER_NAME";

	/**
	 * Order/Observation classification
	 */
	public static final String ORDER_OBS = "ORDER_OBS";
	public static final String CLASSTYPE = "CLASSTYPE";

	/**
	 * Version information
	 */
	public static final String VERSION_FIRST_RELEASED = "VersionFirstReleased";
	public static final String VERSION_LAST_CHANGED = "VersionLastChanged";
	public static final String CHANGE_TYPE = "ChangeType";
	public static final String CHANGE_REASON = "ChangeReason";

	/**
	 * Units and examples
	 */
	public static final String EXAMPLE_UNITS = "EXAMPLE_UNITS";
	public static final String EXAMPLE_UCUM_UNITS = "EXAMPLE_UCUM_UNITS";
	public static final String EXAMPLE_SI_UCUM_UNITS = "EXAMPLE_SI_UCUM_UNITS";

	/**
	 * Part fields
	 */
	public static final String PART_NUMBER = "PartNumber";
	public static final String PART_TYPE_NAME = "PartTypeName";
	public static final String PART_NAME = "PartName";
	public static final String PART_DISPLAY_NAME = "PartDisplayName";
	public static final String PART_STATUS = "Status";

	/**
	 * Answer list fields
	 */
	public static final String ANSWER_LIST_ID = "AnswerListId";
	public static final String ANSWER_LIST_NAME = "AnswerListName";
	public static final String ANSWER_STRING_ID = "AnswerStringId";
	public static final String SEQUENCE_NUMBER = "SequenceNumber";

	/**
	 * Hierarchy fields
	 */
	public static final String IMMEDIATE_PARENT = "IMMEDIATE_PARENT";
	public static final String PARENT_GROUP = "ParentGroup";

	/**
	 * External code mappings
	 */
	public static final String EXTERNAL_COPYRIGHT_NOTICE = "EXTERNAL_COPYRIGHT_NOTICE";

	/**
	 * Document ontology fields
	 */
	public static final String DOCUMENT_SECTION = "DOCUMENT_SECTION";

	/**
	 * Survey instrument fields
	 */
	public static final String SURVEY_QUEST_TEXT = "SURVEY_QUEST_TEXT";
	public static final String SURVEY_QUEST_SRC = "SURVEY_QUEST_SRC";

	/**
	 * Common base component fields
	 */
	public static final String ID = "id";
	public static final String ACTIVE = "active";
	public static final String EFFECTIVE_TIME = "effectiveTime";

	private LoincHeaders() {
		// Prevent instantiation
	}
}
