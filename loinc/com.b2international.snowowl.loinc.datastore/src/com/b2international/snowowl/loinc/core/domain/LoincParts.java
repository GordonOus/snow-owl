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

import java.util.Collections;
import java.util.List;

import com.b2international.snowowl.core.domain.PageableCollectionResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A pageable collection of LOINC parts.
 *
 * @see LoincPart
 * @since 9.9
 */
public final class LoincParts extends PageableCollectionResource<LoincPart> {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates an empty pageable collection for LOINC parts.
	 *
	 * @param limit - limit of items for a single page
	 * @param total - total number of items in the resultset
	 */
	public LoincParts(int limit, int total) {
		super(Collections.emptyList(), null, limit, total);
	}

	/**
	 * Instantiates a pageable collection of LOINC parts.
	 *
	 * @param items       - list of {@link LoincPart}s
	 * @param searchAfter - searchAfter for paging the result set with a live cursor
	 * @param limit       - limit of items for a single page
	 * @param total       - total number of items in the result set
	 */
	@JsonCreator
	public LoincParts(
			@JsonProperty("items") List<LoincPart> items,
			@JsonProperty("searchAfter") String searchAfter,
			@JsonProperty("limit") int limit,
			@JsonProperty("total") int total) {
		super(items, searchAfter, limit, total);
	}
}
