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
package com.b2international.snowowl.loinc.datastore.index.entry;

import static com.b2international.index.query.Expressions.exactMatch;
import static com.b2international.index.query.Expressions.match;
import static com.b2international.index.query.Expressions.matchAny;
import static com.b2international.index.query.Expressions.matchRange;

import com.b2international.index.query.Expression;
import com.b2international.snowowl.core.date.EffectiveTimes;
import com.b2international.snowowl.core.repository.RevisionDocument;
import com.b2international.snowowl.loinc.common.LoincHeaders;
import com.google.common.base.Predicate;

/**
 * Base class for all LOINC document types stored in the index.
 *
 * @since 9.9
 */
public abstract class LoincDocument extends RevisionDocument {

	public static abstract class Expressions extends RevisionDocument.Expressions {

		protected Expressions() {
		}

		public static final Expression active() {
			return active(true);
		}

		public static final Expression inactive() {
			return active(false);
		}

		public static Expression active(boolean active) {
			return match(Fields.ACTIVE, active);
		}

		public static final Expression status(String status) {
			return exactMatch(Fields.STATUS, status);
		}

		public static Expression statuses(Iterable<String> statuses) {
			return matchAny(Fields.STATUS, statuses);
		}

		public static final Expression released() {
			return released(true);
		}

		public static final Expression unreleased() {
			return released(false);
		}

		public static final Expression released(boolean released) {
			return match(Fields.RELEASED, released);
		}

		public static final Expression effectiveTime(long effectiveTime) {
			return effectiveTime(effectiveTime, effectiveTime);
		}

		public static final Expression effectiveTime(long from, long to) {
			return matchRange(Fields.EFFECTIVE_TIME, from, to);
		}

		public static final Expression effectiveTime(long from, long to, boolean minInclusive, boolean maxInclusive) {
			return matchRange(Fields.EFFECTIVE_TIME, from, to, minInclusive, maxInclusive);
		}
	}

	public static final Predicate<LoincDocument> ACTIVE_PREDICATE = new Predicate<LoincDocument>() {
		@Override
		public boolean apply(LoincDocument input) {
			return input.isActive();
		}
	};

	public static abstract class Builder<B extends Builder<B, T>, T extends LoincDocument> extends RevisionDocumentBuilder<B, T> {

		protected String status;
		protected Boolean active;
		protected Boolean released;
		protected Long effectiveTime = EffectiveTimes.UNSET_EFFECTIVE_TIME;

		public B status(final String status) {
			this.status = status;
			return getSelf();
		}

		public B active(final Boolean active) {
			this.active = active;
			return getSelf();
		}

		public B released(final Boolean released) {
			this.released = released;
			return getSelf();
		}

		public B effectiveTime(final Long effectiveTime) {
			this.effectiveTime = effectiveTime;
			return getSelf();
		}
	}

	public static class Fields extends RevisionDocument.Fields {
		public static final String STATUS = LoincHeaders.STATUS;
		public static final String ACTIVE = LoincHeaders.ACTIVE;
		public static final String EFFECTIVE_TIME = LoincHeaders.EFFECTIVE_TIME;
		public static final String RELEASED = "released";
	}

	private final Boolean released;
	private final String status;
	private final Boolean active;
	private final Long effectiveTime;

	protected LoincDocument(
			final String id,
			final String iconId,
			final String status,
			final Boolean released,
			final Boolean active,
			final Long effectiveTime) {
		super(id, iconId);
		this.released = released;
		this.status = status;
		this.active = active;
		this.effectiveTime = effectiveTime;
	}

	public Boolean isReleased() {
		return released;
	}

	public String getStatus() {
		return status;
	}

	public Boolean isActive() {
		return active;
	}

	public Long getEffectiveTime() {
		return effectiveTime;
	}
}
