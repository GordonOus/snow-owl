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

	// TODO: Add additional request builders when needed:
	// public static LoincCodeGetRequestBuilder prepareGetLoincCode(String loincNum)
	// public static LoincCodeCreateRequestBuilder prepareCreateLoincCode()
	// public static LoincCodeUpdateRequestBuilder prepareUpdateLoincCode(String loincNum)
	// public static LoincPartSearchRequestBuilder prepareSearchLoincPart()
	// public static LoincAnswerListSearchRequestBuilder prepareSearchLoincAnswerList()
}
