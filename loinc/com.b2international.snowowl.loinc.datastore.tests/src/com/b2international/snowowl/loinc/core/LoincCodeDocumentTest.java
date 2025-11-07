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

import java.util.Arrays;

import org.junit.Test;

import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;

/**
 * Unit tests for LOINC code document.
 *
 * @since 9.9
 */
public class LoincCodeDocumentTest {

	@Test
	public void testLoincCodeDocumentBuilder() {
		LoincCodeDocument code = LoincCodeDocument.builder()
			.id("10334-4")
			.loincNum("10334-4")
			.active(true)
			.status("ACTIVE")
			.component("Glucose")
			.property("MCnc")
			.timeAspct("Pt")
			.system("Ser/Plas")
			.scaleTyp("Qn")
			.methodTyp("Enzymatic")
			.classType("CHEM")
			.longCommonName("Glucose [Mass/volume] in Serum or Plasma")
			.shortName("Glucose SerPl-mCnc")
			.released(Boolean.FALSE)
			.build();

		assertNotNull(code);
		assertEquals("10334-4", code.getLoincNum());
		assertEquals("Glucose", code.getComponent());
		assertEquals("MCnc", code.getProperty());
		assertEquals("Pt", code.getTimeAspct());
		assertEquals("Ser/Plas", code.getSystem());
		assertEquals("Qn", code.getScaleTyp());
		assertEquals("CHEM", code.getClassType());
		assertTrue(code.isActive());
		assertEquals("ACTIVE", code.getStatus());
	}

	@Test
	public void testLoincCodeSearchText() {
		LoincCodeDocument code = LoincCodeDocument.builder()
			.id("10334-4")
			.loincNum("10334-4")
			.active(true)
			.status("ACTIVE")
			.component("Glucose")
			.longCommonName("Glucose [Mass/volume] in Serum or Plasma")
			.shortName("Glucose SerPl-mCnc")
			.relatedNames(Arrays.asList("Blood sugar", "Plasma glucose"))
			.released(Boolean.FALSE)
			.build();

		String searchText = code.getSearchText();
		assertNotNull(searchText);
		assertTrue(searchText.contains("10334-4"));
		assertTrue(searchText.contains("Glucose"));
		assertTrue(searchText.contains("Blood sugar"));
		assertTrue(searchText.contains("Plasma glucose"));
	}

	@Test
	public void testLoincCodeWithNullValues() {
		LoincCodeDocument code = LoincCodeDocument.builder()
			.id("10334-4")
			.loincNum("10334-4")
			.active(true)
			.status("ACTIVE")
			.component(null)
			.property(null)
			.longCommonName("Glucose test")
			.released(Boolean.FALSE)
			.build();

		assertNotNull(code);
		assertEquals("10334-4", code.getLoincNum());
		assertNull(code.getComponent());
		assertNull(code.getProperty());
		assertEquals("Glucose test", code.getLongCommonName());
	}

	@Test
	public void testLoincCodeInactive() {
		LoincCodeDocument code = LoincCodeDocument.builder()
			.id("10334-4")
			.loincNum("10334-4")
			.active(false)
			.status("DEPRECATED")
			.longCommonName("Old test (deprecated)")
			.released(Boolean.TRUE)
			.build();

		assertNotNull(code);
		assertFalse(code.isActive());
		assertEquals("DEPRECATED", code.getStatus());
		assertTrue(code.isReleased());
	}
}
