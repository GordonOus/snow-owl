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
package com.b2international.snowowl.loinc.core;

import java.util.List;

import com.b2international.snowowl.core.domain.IComponent;
import com.b2international.snowowl.core.plugin.Component;
import com.b2international.snowowl.core.repository.ContentAvailabilityInfoProvider;
import com.b2international.snowowl.core.repository.TerminologyRepositoryPlugin;
import com.b2international.snowowl.loinc.common.LoincTerminologyComponentConstants;
import com.b2international.snowowl.loinc.core.domain.LoincCode;
import com.b2international.snowowl.loinc.core.domain.LoincPart;
import com.b2international.snowowl.loinc.datastore.request.LoincRequests;
import com.google.common.collect.ImmutableList;

/**
 * LOINC terminology repository plugin.
 * <p>
 * This plugin registers the LOINC terminology with Snow Owl and configures
 * the LOINC repository with all necessary components and services.
 *
 * @since 9.9
 */
@Component
public final class LoincPlugin extends TerminologyRepositoryPlugin {

	/**
	 * Unique identifier of the bundle.
	 */
	public static final String PLUGIN_ID = "com.b2international.snowowl.loinc.datastore";

	@Override
	public String getToolingId() {
		return LoincTerminologyComponentConstants.TOOLING_ID;
	}

	@Override
	public String getName() {
		return "LOINC";
	}

	@Override
	public boolean isEffectiveTimeSupported() {
		// LOINC uses version-based releases rather than effective time
		return false;
	}

	@Override
	public List<Class<? extends IComponent>> getTerminologyComponents() {
		return ImmutableList.<Class<? extends IComponent>>of(
			LoincCode.class,
			LoincPart.class
		);
	}

	@Override
	protected ContentAvailabilityInfoProvider getContentAvailabilityInfoProvider() {
		return context -> {
			// Check if any LOINC codes exist in the repository
			return LoincRequests.prepareSearchLoincCode()
				.setLimit(0)
				.build()
				.execute(context)
				.getTotal() > 0;
		};
	}
}
