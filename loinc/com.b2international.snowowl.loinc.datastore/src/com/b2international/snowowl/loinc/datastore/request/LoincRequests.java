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

/**
 * Entry point for building LOINC-specific requests.
 *
 * @since 9.9
 */
public class LoincRequests {

	private LoincRequests() {
		// Prevent instantiation
	}

	/**
	 * Returns a builder for searching LOINC codes.
	 *
	 * @return a search request builder for LOINC codes
	 */
	public static LoincCodeSearchRequestBuilder prepareSearchLoincCode() {
		return new LoincCodeSearchRequestBuilder();
	}

	/**
	 * Returns a request for creating a new LOINC code.
	 *
	 * @return a create request for LOINC codes
	 */
	public static LoincCodeCreateRequest prepareCreateLoincCode() {
		return new LoincCodeCreateRequest();
	}

	/**
	 * Returns a request for updating an existing LOINC code.
	 *
	 * @param loincNum the LOINC number to update
	 * @return an update request for the specified LOINC code
	 */
	public static LoincCodeUpdateRequest prepareUpdateLoincCode(String loincNum) {
		return new LoincCodeUpdateRequest(loincNum);
	}

	/**
	 * Returns a request for deleting a LOINC code.
	 *
	 * @param loincNum the LOINC number to delete
	 * @return a delete request for the specified LOINC code
	 */
	public static LoincCodeDeleteRequest prepareDeleteLoincCode(String loincNum) {
		return new LoincCodeDeleteRequest(loincNum);
	}

	/**
	 * Returns a builder for searching LOINC parts.
	 *
	 * @return a search request builder for LOINC parts
	 */
	public static LoincPartSearchRequestBuilder prepareSearchLoincPart() {
		return new LoincPartSearchRequestBuilder();
	}
}
