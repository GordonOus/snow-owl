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
import com.b2international.snowowl.loinc.core.domain.LoincCodes;

/**
 * Builder for LOINC code search requests.
 *
 * @since 9.9
 */
public final class LoincCodeSearchRequestBuilder
		extends RevisionIndexRequestBuilder<LoincCodeSearchRequestBuilder, BranchContext, LoincCodes>
		implements SystemRequestBuilder<LoincCodes> {

	LoincCodeSearchRequestBuilder() {
	}

	/**
	 * Filter by LOINC number.
	 *
	 * @param loincNum the LOINC number to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByLoincNum(String loincNum) {
		return addOption(LoincCodeSearchRequest.OptionKey.LOINC_NUM, loincNum);
	}

	/**
	 * Filter by LOINC numbers.
	 *
	 * @param loincNums collection of LOINC numbers to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByLoincNums(Collection<String> loincNums) {
		return addOption(LoincCodeSearchRequest.OptionKey.LOINC_NUM, loincNums);
	}

	/**
	 * Filter by component.
	 *
	 * @param component the component to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByComponent(String component) {
		return addOption(LoincCodeSearchRequest.OptionKey.COMPONENT, component);
	}

	/**
	 * Filter by property.
	 *
	 * @param property the property to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByProperty(String property) {
		return addOption(LoincCodeSearchRequest.OptionKey.PROPERTY, property);
	}

	/**
	 * Filter by scale type.
	 *
	 * @param scaleTyp the scale type to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByScaleTyp(String scaleTyp) {
		return addOption(LoincCodeSearchRequest.OptionKey.SCALE_TYP, scaleTyp);
	}

	/**
	 * Filter by scale types.
	 *
	 * @param scaleTyps collection of scale types to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByScaleTyps(Collection<String> scaleTyps) {
		return addOption(LoincCodeSearchRequest.OptionKey.SCALE_TYP, scaleTyps);
	}

	/**
	 * Filter by class type.
	 *
	 * @param classType the class type to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByClassType(String classType) {
		return addOption(LoincCodeSearchRequest.OptionKey.CLASS_TYPE, classType);
	}

	/**
	 * Filter by class types.
	 *
	 * @param classTypes collection of class types to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByClassTypes(Collection<String> classTypes) {
		return addOption(LoincCodeSearchRequest.OptionKey.CLASS_TYPE, classTypes);
	}

	/**
	 * Filter by order/observation classification.
	 *
	 * @param orderObs the order/observation value to match
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByOrderObs(String orderObs) {
		return addOption(LoincCodeSearchRequest.OptionKey.ORDER_OBS, orderObs);
	}

	/**
	 * Filter by status.
	 *
	 * @param status the status to match (ACTIVE, DEPRECATED, DISCOURAGED, TRIAL)
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterByStatus(String status) {
		return addOption(LoincCodeSearchRequest.OptionKey.STATUS, status);
	}

	/**
	 * Filter by text search across all searchable fields.
	 *
	 * @param searchText the text to search for
	 * @return this builder for method chaining
	 */
	public LoincCodeSearchRequestBuilder filterBySearchText(String searchText) {
		return addOption(LoincCodeSearchRequest.OptionKey.SEARCH_TEXT, searchText);
	}

	@Override
	protected LoincCodeSearchRequest createSearch() {
		return new LoincCodeSearchRequest();
	}
}
