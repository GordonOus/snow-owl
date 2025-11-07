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
package com.b2international.snowowl.loinc.datastore.converter;

import java.util.List;

import com.b2international.commons.http.ExtendedLocale;
import com.b2international.commons.options.Options;
import com.b2international.snowowl.core.domain.BranchContext;
import com.b2international.snowowl.core.request.BaseRevisionResourceConverter;
import com.b2international.snowowl.loinc.core.domain.LoincCode;
import com.b2international.snowowl.loinc.core.domain.LoincCodes;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;

/**
 * Converter for transforming {@link LoincCodeDocument} index documents to {@link LoincCode} domain objects.
 *
 * @since 9.9
 */
public final class LoincCodeConverter extends BaseRevisionResourceConverter<LoincCodeDocument, LoincCode, LoincCodes> {

	public LoincCodeConverter(final BranchContext context, Options expand, List<ExtendedLocale> locales) {
		super(context, expand, locales);
	}

	@Override
	protected LoincCodes createCollectionResource(List<LoincCode> results, String searchAfter, int limit, int total) {
		return new LoincCodes(results, searchAfter, limit, total);
	}

	@Override
	protected LoincCode toResource(final LoincCodeDocument doc) {
		final LoincCode code = new LoincCode();

		// Base component properties
		code.setId(doc.getId());
		code.setActive(doc.isActive());
		code.setReleased(doc.isReleased());
		code.setEffectiveTime(toEffectiveTime(doc.getEffectiveTime()));
		code.setStatus(doc.getStatus());
		code.setScore(doc.getScore());

		// LOINC-specific properties
		code.setLoincNum(doc.getLoincNum());
		code.setComponent(doc.getComponent());
		code.setProperty(doc.getProperty());
		code.setTimeAspct(doc.getTimeAspct());
		code.setSystem(doc.getSystem());
		code.setScaleTyp(doc.getScaleTyp());
		code.setMethodTyp(doc.getMethodTyp());

		// Display names
		code.setLongCommonName(doc.getLongCommonName());
		code.setShortName(doc.getShortName());
		code.setDisplayName(doc.getDisplayName());
		code.setConsumerName(doc.getConsumerName());

		// Classification
		code.setClassType(doc.getClassType());
		code.setOrderObs(doc.getOrderObs());
		code.setRelatedNames(doc.getRelatedNames());

		// Version information
		code.setVersionFirstReleased(doc.getVersionFirstReleased());
		code.setVersionLastChanged(doc.getVersionLastChanged());
		code.setChangeType(doc.getChangeType());

		// Example units
		code.setExampleUnits(doc.getExampleUnits());
		code.setExampleUcumUnits(doc.getExampleUcumUnits());

		// Answer list reference
		code.setAnswerListId(doc.getAnswerListId());

		return code;
	}

	@Override
	public void expand(List<LoincCode> results) {
		if (expand().isEmpty()) {
			return;
		}

		// Expand parts if requested
		// expandParts(results);

		// Expand answer lists if requested
		// expandAnswerLists(results);

		// TODO: Implement expansion logic when search/request infrastructure is ready
	}
}
