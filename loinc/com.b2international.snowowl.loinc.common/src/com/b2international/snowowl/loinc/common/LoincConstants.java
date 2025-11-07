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
 * LOINC-specific constants.
 *
 * @since 9.9
 */
public class LoincConstants {

	/**
	 * The terminology identifier for LOINC.
	 */
	public static final String TOOLING_ID = "loinc";

	/**
	 * LOINC component types
	 */
	public static final class ComponentTypes {
		public static final String CODE = "code";
		public static final String PART = "part";
		public static final String ANSWER_LIST = "answerList";
	}

	/**
	 * LOINC status values
	 */
	public static final class Status {
		public static final String ACTIVE = "ACTIVE";
		public static final String DEPRECATED = "DEPRECATED";
		public static final String DISCOURAGED = "DISCOURAGED";
		public static final String TRIAL = "TRIAL";
	}

	/**
	 * LOINC scale types
	 */
	public static final class ScaleTypes {
		public static final String QUANTITATIVE = "Qn";      // Quantitative
		public static final String ORDINAL = "Ord";          // Ordinal
		public static final String NOMINAL = "Nom";          // Nominal
		public static final String NARRATIVE = "Nar";        // Narrative
		public static final String DOCUMENT = "Doc";         // Document
		public static final String SET = "Set";              // Set
		public static final String MULTI = "Multi";          // Multi (deprecated)
	}

	/**
	 * LOINC property types (second axis of LOINC codes)
	 */
	public static final class PropertyTypes {
		public static final String MASS_CONCENTRATION = "MCnc";        // Mass concentration
		public static final String SUBSTANCE_CONCENTRATION = "SCnc";   // Substance concentration
		public static final String ARBITRARY_CONCENTRATION = "ACnc";   // Arbitrary concentration
		public static final String PRESENCE = "Prid";                  // Presence/Identity
		public static final String THRESHOLD = "Thres";                // Threshold
		public static final String TYPE = "Type";                      // Type
		public static final String NUM = "Num";                        // Number
		public static final String MASS = "Mass";                      // Mass
		public static final String VOLUME = "Vol";                     // Volume
		public static final String TIME = "Time";                      // Time
	}

	/**
	 * LOINC time aspects (third axis)
	 */
	public static final class TimeAspects {
		public static final String POINT_IN_TIME = "Pt";               // Point in time
		public static final String TIME_PERIOD = "24H";                // 24 hour period
		public static final String TWELVE_HOUR = "12H";                // 12 hour period
		public static final String BASELINE = "Baseline";              // Baseline
		public static final String POST_DOSE = "post dose";            // Post dose
	}

	/**
	 * LOINC class types
	 */
	public static final class ClassTypes {
		public static final String CHEMISTRY = "CHEM";
		public static final String HEMATOLOGY = "HEM";
		public static final String TOXICOLOGY = "TOX";
		public static final String MICROBIOLOGY = "MICRO";
		public static final String SEROLOGY = "SERO";
		public static final String BLOOD_BANK = "BLDBK";
		public static final String MOLECULAR_PATHOLOGY = "MOLPATH";
		public static final String LABORATORY = "LABORATORY";
		public static final String CLINICAL = "CLINICAL";
		public static final String SURVEY = "SURVEY";
		public static final String DOCUMENT = "DOC";
	}

	/**
	 * LOINC part types
	 */
	public static final class PartTypes {
		public static final String COMPONENT = "COMPONENT";
		public static final String PROPERTY = "PROPERTY";
		public static final String TIME_ASPECT = "TIME";
		public static final String SYSTEM = "SYSTEM";
		public static final String SCALE = "SCALE";
		public static final String METHOD = "METHOD";
		public static final String CLASS = "CLASS";
		public static final String CHALLENGE = "CHALLENGE";
		public static final String ADJUSTMENT = "ADJUSTMENT";
		public static final String COUNT = "COUNT";
	}

	private LoincConstants() {
		// Prevent instantiation
	}
}
