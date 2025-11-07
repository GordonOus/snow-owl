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
package com.b2international.snowowl.loinc.core.rest.domain;

import java.util.List;

import com.b2international.snowowl.core.rest.domain.ResourceRestSearch;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * REST search parameters for LOINC parts.
 *
 * @since 9.9
 */
public class LoincPartRestSearch extends ResourceRestSearch {

	@Parameter(description = "The part number(s) to match")
	private List<String> partNumber;

	@Parameter(description = "The part type name(s) to match (COMPONENT, PROPERTY, SYSTEM, etc.)")
	private List<String> partTypeName;

	@Parameter(description = "The status to match (ACTIVE, DEPRECATED)")
	private String status;

	public List<String> getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(List<String> partNumber) {
		this.partNumber = partNumber;
	}

	public List<String> getPartTypeName() {
		return partTypeName;
	}

	public void setPartTypeName(List<String> partTypeName) {
		this.partTypeName = partTypeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
