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
package com.b2international.snowowl.loinc.datastore.fhir;

import com.b2international.snowowl.loinc.common.LoincConstants;
import com.b2international.snowowl.loinc.core.domain.LoincCode;

/**
 * Provider for FHIR CodeSystem resources for LOINC.
 * <p>
 * This class helps integrate LOINC with FHIR terminology services by providing
 * mappings between LOINC concepts and FHIR CodeSystem/ValueSet resources.
 *
 * @since 9.9
 */
public class LoincCodeSystemProvider {

	/**
	 * Official LOINC URI as defined by the LOINC organization.
	 */
	public static final String LOINC_URI = "http://loinc.org";

	/**
	 * LOINC code system name.
	 */
	public static final String LOINC_NAME = "LOINC";

	/**
	 * LOINC code system title.
	 */
	public static final String LOINC_TITLE = "Logical Observation Identifiers Names and Codes";

	/**
	 * LOINC copyright notice.
	 */
	public static final String LOINC_COPYRIGHT = "This material contains content from LOINC (http://loinc.org). "
			+ "LOINC is copyright © 1995-2024, Regenstrief Institute, Inc. and the Logical Observation Identifiers Names and Codes (LOINC) Committee "
			+ "and is available at no cost under the license at http://loinc.org/license.";

	/**
	 * LOINC publisher.
	 */
	public static final String LOINC_PUBLISHER = "Regenstrief Institute, Inc.";

	/**
	 * Returns the FHIR CodeSystem URL for LOINC.
	 *
	 * @return the LOINC URI
	 */
	public String getCodeSystemUrl() {
		return LOINC_URI;
	}

	/**
	 * Returns the FHIR system identifier for a LOINC code.
	 *
	 * @param loincCode the LOINC code
	 * @return the system URI
	 */
	public String getSystemForCode(LoincCode loincCode) {
		return LOINC_URI;
	}

	/**
	 * Returns the code value for FHIR Coding representation.
	 *
	 * @param loincCode the LOINC code
	 * @return the LOINC number
	 */
	public String getCodeValue(LoincCode loincCode) {
		return loincCode.getLoincNum();
	}

	/**
	 * Returns the display value for FHIR Coding representation.
	 *
	 * @param loincCode the LOINC code
	 * @return the long common name or short name
	 */
	public String getDisplayValue(LoincCode loincCode) {
		String display = loincCode.getLongCommonName();
		if (display == null) {
			display = loincCode.getShortName();
		}
		if (display == null) {
			display = loincCode.getDisplayName();
		}
		return display;
	}

	/**
	 * Checks if a LOINC code is active for FHIR purposes.
	 *
	 * @param loincCode the LOINC code
	 * @return true if active, false otherwise
	 */
	public boolean isActive(LoincCode loincCode) {
		return Boolean.TRUE.equals(loincCode.isActive())
			&& LoincConstants.Status.ACTIVE.equals(loincCode.getStatus());
	}

	/**
	 * Returns the definition for a LOINC code.
	 *
	 * @param loincCode the LOINC code
	 * @return the definition (long common name)
	 */
	public String getDefinition(LoincCode loincCode) {
		return loincCode.getLongCommonName();
	}

	/**
	 * Returns additional properties for FHIR CodeSystem.concept.property elements.
	 *
	 * @param loincCode the LOINC code
	 * @return a description of LOINC-specific properties
	 */
	public String getPropertiesDescription(LoincCode loincCode) {
		StringBuilder props = new StringBuilder();

		if (loincCode.getComponent() != null) {
			props.append("Component: ").append(loincCode.getComponent()).append("; ");
		}
		if (loincCode.getProperty() != null) {
			props.append("Property: ").append(loincCode.getProperty()).append("; ");
		}
		if (loincCode.getTimeAspct() != null) {
			props.append("Time: ").append(loincCode.getTimeAspct()).append("; ");
		}
		if (loincCode.getSystem() != null) {
			props.append("System: ").append(loincCode.getSystem()).append("; ");
		}
		if (loincCode.getScaleTyp() != null) {
			props.append("Scale: ").append(loincCode.getScaleTyp()).append("; ");
		}
		if (loincCode.getMethodTyp() != null) {
			props.append("Method: ").append(loincCode.getMethodTyp()).append("; ");
		}
		if (loincCode.getClassType() != null) {
			props.append("Class: ").append(loincCode.getClassType()).append("; ");
		}

		return props.length() > 0 ? props.toString() : null;
	}

	/**
	 * Creates a FHIR-compatible designation for alternative names.
	 *
	 * @param loincCode the LOINC code
	 * @return the alternative designation (short name or consumer name)
	 */
	public String getAlternativeDesignation(LoincCode loincCode) {
		if (loincCode.getShortName() != null) {
			return loincCode.getShortName();
		}
		if (loincCode.getConsumerName() != null) {
			return loincCode.getConsumerName();
		}
		return null;
	}
}
