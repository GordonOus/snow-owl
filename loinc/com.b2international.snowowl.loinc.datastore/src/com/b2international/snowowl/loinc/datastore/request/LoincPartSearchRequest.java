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
import com.b2international.snowowl.loinc.core.domain.LoincParts;
import com.b2international.snowowl.loinc.datastore.converter.LoincPartConverter;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincPartDocument;

/**
 * Search request for LOINC parts.
 *
 * @since 9.9
 */
public final class LoincPartSearchRequest extends SearchIndexResourceRequest<BranchContext, LoincParts, LoincPartDocument> {

	private static final long serialVersionUID = 1L;

	public enum OptionKey {
		/**
		 * Filter by active status
		 */
		ACTIVE,

		/**
		 * Filter by part number(s)
		 */
		PART_NUMBER,

		/**
		 * Filter by part type name(s)
		 */
		PART_TYPE_NAME,

		/**
		 * Filter by status
		 */
		STATUS,
	}

	LoincPartSearchRequest() {
	}

	@Override
	protected Class<LoincPartDocument> getFrom() {
		return LoincPartDocument.class;
	}

	@Override
	protected Expression prepareQuery(BranchContext context) {
		final ExpressionBuilder queryBuilder = Expressions.bool();

		addIdFilter(queryBuilder, ids -> LoincPartDocument.Expressions.partNumbers(ids));
		addActiveFilter(queryBuilder);
		addPartNumberFilter(queryBuilder);
		addPartTypeNameFilter(queryBuilder);
		addStatusFilter(queryBuilder);

		return queryBuilder.build();
	}

	private void addActiveFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.ACTIVE)) {
			boolean active = getBoolean(OptionKey.ACTIVE);
			queryBuilder.filter(LoincPartDocument.Expressions.active(active));
		}
	}

	private void addPartNumberFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.PART_NUMBER)) {
			final Collection<String> partNumbers = getCollection(OptionKey.PART_NUMBER, String.class);
			queryBuilder.filter(LoincPartDocument.Expressions.partNumbers(partNumbers));
		}
	}

	private void addPartTypeNameFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.PART_TYPE_NAME)) {
			final Collection<String> partTypeNames = getCollection(OptionKey.PART_TYPE_NAME, String.class);
			queryBuilder.filter(LoincPartDocument.Expressions.partTypeNames(partTypeNames));
		}
	}

	private void addStatusFilter(ExpressionBuilder queryBuilder) {
		if (containsKey(OptionKey.STATUS)) {
			final String status = getString(OptionKey.STATUS);
			queryBuilder.filter(LoincPartDocument.Expressions.status(status));
		}
	}

	@Override
	protected LoincParts toCollectionResource(BranchContext context, Hits<LoincPartDocument> hits) {
		return new LoincPartConverter(context, expand(), locales()).convert(hits.getHits(), hits.getSearchAfter(), hits.getLimit(), hits.getTotal());
	}

	@Override
	protected LoincParts createEmptyResult(int limit) {
		return new LoincParts(limit, 0);
	}
}
