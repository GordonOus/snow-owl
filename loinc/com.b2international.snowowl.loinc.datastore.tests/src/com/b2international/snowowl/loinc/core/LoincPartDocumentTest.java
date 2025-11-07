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

import static org.junit.Assert.*;

import org.junit.Test;

import com.b2international.snowowl.loinc.datastore.index.entry.LoincPartDocument;

/**
 * Unit tests for LOINC part document.
 *
 * @since 9.9
 */
public class LoincPartDocumentTest {

	@Test
	public void testLoincPartDocumentBuilder() {
		LoincPartDocument part = LoincPartDocument.builder()
			.id("LP123-4")
			.partNumber("LP123-4")
			.active(true)
			.status("ACTIVE")
			.partTypeName("COMPONENT")
			.partName("Glucose")
			.partDisplayName("Glucose")
			.released(Boolean.FALSE)
			.build();

		assertNotNull(part);
		assertEquals("LP123-4", part.getPartNumber());
		assertEquals("COMPONENT", part.getPartTypeName());
		assertEquals("Glucose", part.getPartName());
		assertEquals("Glucose", part.getPartDisplayName());
		assertTrue(part.isActive());
		assertEquals("ACTIVE", part.getStatus());
	}

	@Test
	public void testLoincPartComponentType() {
		LoincPartDocument part = LoincPartDocument.builder()
			.id("LP456-7")
			.partNumber("LP456-7")
			.active(true)
			.status("ACTIVE")
			.partTypeName("PROPERTY")
			.partName("Mass concentration")
			.released(Boolean.FALSE)
			.build();

		assertNotNull(part);
		assertEquals("PROPERTY", part.getPartTypeName());
		assertEquals("Mass concentration", part.getPartName());
	}

	@Test
	public void testLoincPartInactive() {
		LoincPartDocument part = LoincPartDocument.builder()
			.id("LP789-0")
			.partNumber("LP789-0")
			.active(false)
			.status("DEPRECATED")
			.partTypeName("COMPONENT")
			.partName("Old component")
			.released(Boolean.TRUE)
			.build();

		assertNotNull(part);
		assertFalse(part.isActive());
		assertEquals("DEPRECATED", part.getStatus());
		assertTrue(part.isReleased());
	}
}
