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

import java.time.LocalDate;

import com.b2international.snowowl.core.date.DateFormats;
import com.b2international.snowowl.core.domain.BaseComponent;
import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.loinc.common.LoincHeaders;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Base class for LOINC components.
 *
 * @since 9.9
 */
public abstract class LoincComponent extends BaseComponent {

	private static final long serialVersionUID = 1L;

	/**
	 * Expandable property keys for LOINC components.
	 *
	 * @since 9.9
	 */
	public static abstract class Expand {
		// Common expandable properties
	}

	/**
	 * Field names for LOINC components.
	 *
	 * @since 9.9
	 */
	public static abstract class Fields extends BaseComponent.Fields {
		public static final String ACTIVE = LoincHeaders.ACTIVE;
		public static final String EFFECTIVE_TIME = LoincHeaders.EFFECTIVE_TIME;
		public static final String STATUS = LoincHeaders.STATUS;
	}

	private Boolean active;
	private LocalDate effectiveTime;
	private String status;
	private Float score;

	/**
	 * Returns the component's current status as a boolean value.
	 *
	 * @return {@code true} if the component is active, {@code false} if it is inactive
	 */
	public Boolean isActive() {
		return active;
	}

	/**
	 * Returns the date at which the current state of the component becomes effective.
	 *
	 * @return the component's effective time
	 */
	@JsonFormat(shape = Shape.STRING, pattern = DateFormats.SHORT, timezone = "UTC")
	public LocalDate getEffectiveTime() {
		return effectiveTime;
	}

	/**
	 * Returns the status of the LOINC component (ACTIVE, DEPRECATED, DISCOURAGED, TRIAL).
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the score associated with this component if it's a match in a query, can be <code>null</code>
	 */
	public Float getScore() {
		return score;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	@JsonFormat(shape = Shape.STRING, pattern = DateFormats.SHORT, timezone = "UTC")
	public void setEffectiveTime(final LocalDate effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	/**
	 * Returns the component type identifier.
	 *
	 * @return the component type
	 */
	public abstract String getComponentType();

	/**
	 * Creates an update {@link Request} to update the component to the state represented by this instance.
	 *
	 * @return the update request
	 */
	public abstract Request<TransactionContext, Boolean> toUpdateRequest();

	/**
	 * Creates a create {@link Request} to create the component represented by this instance.
	 *
	 * @return the create request
	 */
	public final Request<TransactionContext, String> toCreateRequest() {
		return toCreateRequest(null);
	}

	/**
	 * Creates a create {@link Request} to create the component represented by this instance.
	 *
	 * @param containerId the container component identifier to enforce attachment to it, may be <code>null</code>
	 * @return the create request
	 */
	public abstract Request<TransactionContext, String> toCreateRequest(String containerId);
}
