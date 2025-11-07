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
 * REST search parameters for LOINC codes.
 *
 * @since 9.9
 */
public class LoincCodeRestSearch extends ResourceRestSearch {

	@Parameter(description = "The LOINC number(s) to match")
	private List<String> loincNum;

	@Parameter(description = "The component to match")
	private String component;

	@Parameter(description = "The property to match")
	private String property;

	@Parameter(description = "The scale type(s) to match (e.g., Qn, Ord, Nom)")
	private List<String> scaleTyp;

	@Parameter(description = "The class type(s) to match (e.g., CHEM, HEM)")
	private List<String> classType;

	@Parameter(description = "The order/observation classification to match")
	private String orderObs;

	@Parameter(description = "The status to match (ACTIVE, DEPRECATED, DISCOURAGED, TRIAL)")
	private String status;

	@Parameter(description = "Full-text search across all searchable fields")
	private String searchText;

	public List<String> getLoincNum() {
		return loincNum;
	}

	public void setLoincNum(List<String> loincNum) {
		this.loincNum = loincNum;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public List<String> getScaleTyp() {
		return scaleTyp;
	}

	public void setScaleTyp(List<String> scaleTyp) {
		this.scaleTyp = scaleTyp;
	}

	public List<String> getClassType() {
		return classType;
	}

	public void setClassType(List<String> classType) {
		this.classType = classType;
	}

	public String getOrderObs() {
		return orderObs;
	}

	public void setOrderObs(String orderObs) {
		this.orderObs = orderObs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
}
