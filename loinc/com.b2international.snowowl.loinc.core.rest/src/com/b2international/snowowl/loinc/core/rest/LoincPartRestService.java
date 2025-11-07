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
package com.b2international.snowowl.loinc.core.rest;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.b2international.commons.http.AcceptLanguageHeader;
import com.b2international.snowowl.core.events.util.Promise;
import com.b2international.snowowl.core.request.SearchResourceRequest.Sort;
import com.b2international.snowowl.core.rest.AbstractRestService;
import com.b2international.snowowl.loinc.core.domain.LoincParts;
import com.b2international.snowowl.loinc.core.rest.domain.LoincPartRestSearch;
import com.b2international.snowowl.loinc.datastore.request.LoincRequests;
import com.google.common.collect.ImmutableSet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST service for LOINC parts.
 *
 * @since 9.9
 */
@Tag(description = "LOINC Parts", name = "LoincParts")
@Controller
@RequestMapping(value = "/{path:**}/loinc/parts")
public class LoincPartRestService extends AbstractRestService {

	public LoincPartRestService() {
		super(ImmutableSet.<String>builder()
				.add("partNumber")
				.add("partTypeName")
				.add("partName")
				.add("active")
				.add("status")
				.build());
	}

	@Operation(
		summary = "Retrieve LOINC parts",
		description = "Returns a list with all/filtered LOINC parts from a path."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "Invalid search config"),
		@ApiResponse(responseCode = "404", description = "Branch not found")
	})
	@GetMapping(produces = { AbstractRestService.JSON_MEDIA_TYPE })
	public @ResponseBody Promise<LoincParts> searchByGet(
			@Parameter(description = "The resource path", required = true)
			@PathVariable(value = "path")
			final String path,

			@ParameterObject
			final LoincPartRestSearch params,

			@Parameter(description = "Accepted language tags, in order of preference", example = AcceptLanguageHeader.DEFAULT_ACCEPT_LANGUAGE_HEADER)
			@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = AcceptLanguageHeader.DEFAULT_ACCEPT_LANGUAGE_HEADER, required = false)
			final String acceptLanguage) {

		List<Sort> sorts = extractSortFields(params.getSort());

		return LoincRequests
				.prepareSearchLoincPart()
				.setLimit(params.getLimit())
				.setSearchAfter(params.getSearchAfter())
				.filterByIds(params.getId())
				.filterByActive(params.getActive())
				.filterByStatus(params.getStatus())
				.filterByPartNumbers(params.getPartNumber())
				.filterByPartTypeNames(params.getPartTypeName())
				.setExpand(params.getExpand())
				.setFields(params.getField())
				.setLocales(acceptLanguage)
				.sortBy(sorts)
				.build(path)
				.execute(getBus());
	}

	@Operation(
		summary = "Retrieve LOINC parts",
		description = "Returns a list with all/filtered LOINC parts from a path."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "Invalid search config"),
		@ApiResponse(responseCode = "404", description = "Branch not found")
	})
	@PostMapping(value = "/search", produces = { AbstractRestService.JSON_MEDIA_TYPE })
	public @ResponseBody Promise<LoincParts> searchByPost(
			@Parameter(description = "The resource path", required = true)
			@PathVariable(value = "path")
			final String path,

			@RequestBody(required = false)
			final LoincPartRestSearch params,

			@Parameter(description = "Accepted language tags, in order of preference", example = AcceptLanguageHeader.DEFAULT_ACCEPT_LANGUAGE_HEADER)
			@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = AcceptLanguageHeader.DEFAULT_ACCEPT_LANGUAGE_HEADER, required = false)
			final String acceptLanguage) {

		return searchByGet(path, params, acceptLanguage);
	}
}
