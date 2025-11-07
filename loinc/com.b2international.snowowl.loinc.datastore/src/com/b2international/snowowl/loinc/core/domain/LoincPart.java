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

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.core.terminology.ComponentCategory;
import com.b2international.snowowl.core.terminology.TerminologyComponent;
import com.b2international.snowowl.loinc.common.LoincHeaders;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincPartDocument;

/**
 * Represents a LOINC part.
 * <br>
 * LOINC parts are reusable elements that compose LOINC codes (components, properties, time aspects, etc.).
 *
 * @since 9.9
 */
@TerminologyComponent(
	name = "LOINC Part",
	componentCategory = ComponentCategory.CONCEPT,
	docType = LoincPartDocument.class
)
public final class LoincPart extends LoincComponent {

	private static final long serialVersionUID = 1L;

	public static final String TYPE = "part";

	/**
	 * Field names for LOINC parts.
	 *
	 * @since 9.9
	 */
	public static final class Fields extends LoincComponent.Fields {
		public static final String PART_NUMBER = LoincHeaders.PART_NUMBER;
		public static final String PART_TYPE_NAME = LoincHeaders.PART_TYPE_NAME;
		public static final String PART_NAME = LoincHeaders.PART_NAME;
		public static final String PART_DISPLAY_NAME = LoincHeaders.PART_DISPLAY_NAME;
	}

	private String partNumber;          // Part identifier (e.g., "LP123-4")
	private String partTypeName;        // Type of part (COMPONENT, PROPERTY, etc.)
	private String partName;            // Name of the part
	private String partDisplayName;     // Display name

	public LoincPart() {
	}

	public LoincPart(String partNumber) {
		setId(partNumber);
		this.partNumber = partNumber;
	}

	@Override
	public String getComponentType() {
		return TYPE;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartTypeName() {
		return partTypeName;
	}

	public void setPartTypeName(String partTypeName) {
		this.partTypeName = partTypeName;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartDisplayName() {
		return partDisplayName;
	}

	public void setPartDisplayName(String partDisplayName) {
		this.partDisplayName = partDisplayName;
	}

	@Override
	public Request<TransactionContext, String> toCreateRequest(String containerId) {
		// TODO: Implement when request builders are created
		throw new UnsupportedOperationException("LOINC part creation not yet implemented");
	}

	@Override
	public Request<TransactionContext, Boolean> toUpdateRequest() {
		// TODO: Implement when request builders are created
		throw new UnsupportedOperationException("LOINC part update not yet implemented");
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LoincPart [partNumber=");
		builder.append(partNumber);
		builder.append(", partName=");
		builder.append(partName);
		builder.append(", partTypeName=");
		builder.append(partTypeName);
		builder.append("]");
		return builder.toString();
	}
}
