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

import java.util.Collection;

import com.b2international.snowowl.core.domain.BranchContext;
import com.b2international.snowowl.core.request.RevisionIndexRequestBuilder;
import com.b2international.snowowl.core.request.SystemRequestBuilder;
import com.b2international.snowowl.loinc.core.domain.LoincParts;

/**
 * Builder for LOINC part search requests.
 *
 * @since 9.9
 */
public final class LoincPartSearchRequestBuilder
		extends RevisionIndexRequestBuilder<LoincPartSearchRequestBuilder, BranchContext, LoincParts>
		implements SystemRequestBuilder<LoincParts> {

	LoincPartSearchRequestBuilder() {
	}

	/**
	 * Filter by part number.
	 *
	 * @param partNumber the part number to match
	 * @return this builder for method chaining
	 */
	public LoincPartSearchRequestBuilder filterByPartNumber(String partNumber) {
		return addOption(LoincPartSearchRequest.OptionKey.PART_NUMBER, partNumber);
	}

	/**
	 * Filter by part numbers.
	 *
	 * @param partNumbers collection of part numbers to match
	 * @return this builder for method chaining
	 */
	public LoincPartSearchRequestBuilder filterByPartNumbers(Collection<String> partNumbers) {
		return addOption(LoincPartSearchRequest.OptionKey.PART_NUMBER, partNumbers);
	}

	/**
	 * Filter by part type name.
	 *
	 * @param partTypeName the part type name to match
	 * @return this builder for method chaining
	 */
	public LoincPartSearchRequestBuilder filterByPartTypeName(String partTypeName) {
		return addOption(LoincPartSearchRequest.OptionKey.PART_TYPE_NAME, partTypeName);
	}

	/**
	 * Filter by part type names.
	 *
	 * @param partTypeNames collection of part type names to match
	 * @return this builder for method chaining
	 */
	public LoincPartSearchRequestBuilder filterByPartTypeNames(Collection<String> partTypeNames) {
		return addOption(LoincPartSearchRequest.OptionKey.PART_TYPE_NAME, partTypeNames);
	}

	/**
	 * Filter by status.
	 *
	 * @param status the status to match (ACTIVE, DEPRECATED, etc.)
	 * @return this builder for method chaining
	 */
	public LoincPartSearchRequestBuilder filterByStatus(String status) {
		return addOption(LoincPartSearchRequest.OptionKey.STATUS, status);
	}

	@Override
	protected LoincPartSearchRequest createSearch() {
		return new LoincPartSearchRequest();
	}
}
