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
import com.b2international.snowowl.loinc.core.domain.LoincPart;
import com.b2international.snowowl.loinc.core.domain.LoincParts;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincPartDocument;

/**
 * Converter for transforming {@link LoincPartDocument} index documents to {@link LoincPart} domain objects.
 *
 * @since 9.9
 */
public final class LoincPartConverter extends BaseRevisionResourceConverter<LoincPartDocument, LoincPart, LoincParts> {

	public LoincPartConverter(final BranchContext context, Options expand, List<ExtendedLocale> locales) {
		super(context, expand, locales);
	}

	@Override
	protected LoincParts createCollectionResource(List<LoincPart> results, String searchAfter, int limit, int total) {
		return new LoincParts(results, searchAfter, limit, total);
	}

	@Override
	protected LoincPart toResource(final LoincPartDocument doc) {
		final LoincPart part = new LoincPart();

		// Base component properties
		part.setId(doc.getId());
		part.setActive(doc.isActive());
		part.setReleased(doc.isReleased());
		part.setEffectiveTime(toEffectiveTime(doc.getEffectiveTime()));
		part.setStatus(doc.getStatus());
		part.setScore(doc.getScore());

		// LOINC part-specific properties
		part.setPartNumber(doc.getPartNumber());
		part.setPartTypeName(doc.getPartTypeName());
		part.setPartName(doc.getPartName());
		part.setPartDisplayName(doc.getPartDisplayName());

		return part;
	}

	@Override
	public void expand(List<LoincPart> results) {
		if (expand().isEmpty()) {
			return;
		}

		// LOINC parts don't have expandable properties currently
		// This can be extended in the future if needed
	}
}
