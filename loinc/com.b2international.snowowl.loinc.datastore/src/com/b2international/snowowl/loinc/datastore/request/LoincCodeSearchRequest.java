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

import com.b2international.index.Hits;
import com.b2international.index.query.Expression;
import com.b2international.index.query.Expressions;
import com.b2international.index.query.Expressions.ExpressionBuilder;
import com.b2international.snowowl.core.domain.BranchContext;
import com.b2international.snowowl.core.request.SearchIndexResourceRequest;
import com.b2international.snowowl.loinc.core.domain.LoincCodes;
import com.b2international.snowowl.loinc.datastore.converter.LoincCodeConverter;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;
import com.google.common.base.Strings;

/**
 * Search request for LOINC codes.
 *
 * @since 9.9
 */
public final class LoincCodeSearchRequest extends SearchIndexResourceRequest<BranchContext, LoincCodes, LoincCodeDocument> {

	private static final long serialVersionUID = 1L;

	/**
	 * Option keys for LOINC code search.
	 */
	public enum OptionKey {
		/**
		 * Filter by LOINC number(s)
		 */
		LOINC_NUM,

		/**
		 * Filter by component
		 */
		COMPONENT,

		/**
		 * Filter by property
		 */
		PROPERTY,

		/**
		 * Filter by scale type(s)
		 */
		SCALE_TYP,

		/**
		 * Filter by class type(s)
		 */
		CLASS_TYPE,

		/**
		 * Filter by order/observation classification
		 */
		ORDER_OBS,

		/**
		 * Filter by status
		 */
		STATUS,

		/**
		 * Full-text search across searchable fields
		 */
		SEARCH_TEXT,
	}

	LoincCodeSearchRequest() {
	}

	@Override
	protected Class<LoincCodeDocument> getFrom() {
		return LoincCodeDocument.class;
	}

	@Override
	protected Expression prepareQuery(BranchContext context) {
		final ExpressionBuilder queryBuilder = Expressions.builder();

		// Add common filters
		addIdFilter(queryBuilder, ids -> LoincCodeDocument.Expressions.loincNums(ids));
		addActiveFilter(queryBuilder);
		addReleasedFilter(queryBuilder);

		// Add LOINC-specific filters
		addLoincNumFilter(queryBuilder);
		addComponentFilter(queryBuilder);
		addPropertyFilter(queryBuilder);
		addScaleTypFilter(queryBuilder);
		addClassTypeFilter(queryBuilder);
		addOrderObsFilter(queryBuilder);
		addStatusFilter(queryBuilder);
		addSearchTextFilter(queryBuilder);

		return queryBuilder.build();
	}

	private void addActiveFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(SearchResourceRequest.OptionKey.ACTIVE)) {
			boolean active = getBoolean(SearchResourceRequest.OptionKey.ACTIVE);
			queryBuilder.filter(LoincCodeDocument.Expressions.active(active));
		}
	}

	private void addReleasedFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(SearchResourceRequest.OptionKey.RELEASED)) {
			boolean released = getBoolean(SearchResourceRequest.OptionKey.RELEASED);
			queryBuilder.filter(LoincCodeDocument.Expressions.released(released));
		}
	}

	private void addLoincNumFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.LOINC_NUM)) {
			final Collection<String> loincNums = getCollection(OptionKey.LOINC_NUM, String.class);
			queryBuilder.filter(LoincCodeDocument.Expressions.loincNums(loincNums));
		}
	}

	private void addComponentFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.COMPONENT)) {
			final String component = getString(OptionKey.COMPONENT);
			queryBuilder.filter(LoincCodeDocument.Expressions.component(component));
		}
	}

	private void addPropertyFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.PROPERTY)) {
			final String property = getString(OptionKey.PROPERTY);
			queryBuilder.filter(LoincCodeDocument.Expressions.property(property));
		}
	}

	private void addScaleTypFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.SCALE_TYP)) {
			final Collection<String> scaleTyps = getCollection(OptionKey.SCALE_TYP, String.class);
			queryBuilder.filter(LoincCodeDocument.Expressions.scaleTyps(scaleTyps));
		}
	}

	private void addClassTypeFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.CLASS_TYPE)) {
			final Collection<String> classTypes = getCollection(OptionKey.CLASS_TYPE, String.class);
			queryBuilder.filter(LoincCodeDocument.Expressions.classTypes(classTypes));
		}
	}

	private void addOrderObsFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.ORDER_OBS)) {
			final String orderObs = getString(OptionKey.ORDER_OBS);
			queryBuilder.filter(LoincCodeDocument.Expressions.orderObs(orderObs));
		}
	}

	private void addStatusFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.STATUS)) {
			final String status = getString(OptionKey.STATUS);
			queryBuilder.filter(LoincCodeDocument.Expressions.status(status));
		}
	}

	private void addSearchTextFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.SEARCH_TEXT)) {
			final String searchText = getString(OptionKey.SEARCH_TEXT);
			if (!Strings.isNullOrEmpty(searchText)) {
				queryBuilder.must(LoincCodeDocument.Expressions.searchText(searchText));
			}
		}
	}

	@Override
	protected LoincCodes toCollectionResource(BranchContext context, Hits<LoincCodeDocument> hits) {
		return new LoincCodeConverter(context, expand(), locales()).convert(hits.getHits(), hits.getSearchAfter(), hits.getLimit(), hits.getTotal());
	}

	@Override
	protected LoincCodes createEmptyResult(int limit) {
		return new LoincCodes(limit, 0);
	}
}
