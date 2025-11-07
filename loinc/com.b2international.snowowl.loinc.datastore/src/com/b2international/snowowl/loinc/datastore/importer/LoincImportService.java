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
package com.b2international.snowowl.loinc.datastore.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.loinc.common.LoincHeaders;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincPartDocument;
import com.google.common.base.Strings;

/**
 * Service for importing LOINC distribution files.
 * <p>
 * LOINC distribution files are typically provided as tab-delimited text files (TSV format).
 * This service parses these files and creates LOINC codes and parts.
 *
 * @since 9.9
 */
public class LoincImportService {

	/**
	 * Imports LOINC codes from a TSV input stream.
	 *
	 * @param context the transaction context
	 * @param input the input stream containing LOINC codes
	 * @return the number of codes imported
	 * @throws IOException if an I/O error occurs
	 */
	public int importLoincCodes(TransactionContext context, InputStream input) throws IOException {
		int count = 0;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
			// Read header line
			String headerLine = reader.readLine();
			if (headerLine == null) {
				return 0;
			}

			String[] headers = headerLine.split("\t");
			Map<String, Integer> columnMap = buildColumnMap(headers);

			// Read data lines
			String line;
			while ((line = reader.readLine()) != null) {
				if (Strings.isNullOrEmpty(line.trim())) {
					continue;
				}

				String[] values = line.split("\t", -1); // -1 to keep trailing empty strings
				LoincCodeDocument code = parseLoincCode(columnMap, values);
				if (code != null) {
					context.add(code);
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Imports LOINC parts from a TSV input stream.
	 *
	 * @param context the transaction context
	 * @param input the input stream containing LOINC parts
	 * @return the number of parts imported
	 * @throws IOException if an I/O error occurs
	 */
	public int importLoincParts(TransactionContext context, InputStream input) throws IOException {
		int count = 0;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
			// Read header line
			String headerLine = reader.readLine();
			if (headerLine == null) {
				return 0;
			}

			String[] headers = headerLine.split("\t");
			Map<String, Integer> columnMap = buildColumnMap(headers);

			// Read data lines
			String line;
			while ((line = reader.readLine()) != null) {
				if (Strings.isNullOrEmpty(line.trim())) {
					continue;
				}

				String[] values = line.split("\t", -1);
				LoincPartDocument part = parseLoincPart(columnMap, values);
				if (part != null) {
					context.add(part);
					count++;
				}
			}
		}

		return count;
	}

	private Map<String, Integer> buildColumnMap(String[] headers) {
		Map<String, Integer> map = new HashMap<>();
		for (int i = 0; i < headers.length; i++) {
			map.put(headers[i].trim(), i);
		}
		return map;
	}

	private LoincCodeDocument parseLoincCode(Map<String, Integer> columnMap, String[] values) {
		String loincNum = getValue(columnMap, values, LoincHeaders.LOINC_NUM);
		if (Strings.isNullOrEmpty(loincNum)) {
			return null;
		}

		String status = getValue(columnMap, values, LoincHeaders.STATUS);
		if (Strings.isNullOrEmpty(status)) {
			status = "ACTIVE";
		}

		boolean active = "ACTIVE".equalsIgnoreCase(status) || "TRIAL".equalsIgnoreCase(status);

		List<String> relatedNames = new ArrayList<>();
		String relatedNames2 = getValue(columnMap, values, LoincHeaders.RELATED_NAMES2);
		if (!Strings.isNullOrEmpty(relatedNames2)) {
			for (String name : relatedNames2.split(";")) {
				if (!Strings.isNullOrEmpty(name.trim())) {
					relatedNames.add(name.trim());
				}
			}
		}

		return LoincCodeDocument.builder()
			.id(loincNum)
			.loincNum(loincNum)
			.active(active)
			.status(status)
			.component(getValue(columnMap, values, LoincHeaders.COMPONENT))
			.property(getValue(columnMap, values, LoincHeaders.PROPERTY))
			.timeAspct(getValue(columnMap, values, LoincHeaders.TIME_ASPCT))
			.system(getValue(columnMap, values, LoincHeaders.SYSTEM))
			.scaleTyp(getValue(columnMap, values, LoincHeaders.SCALE_TYP))
			.methodTyp(getValue(columnMap, values, LoincHeaders.METHOD_TYP))
			.classType(getValue(columnMap, values, LoincHeaders.CLASS))
			.longCommonName(getValue(columnMap, values, LoincHeaders.LONG_COMMON_NAME))
			.shortName(getValue(columnMap, values, LoincHeaders.SHORT_NAME))
			.displayName(getValue(columnMap, values, LoincHeaders.DISPLAY_NAME))
			.consumerName(getValue(columnMap, values, LoincHeaders.CONSUMER_NAME))
			.orderObs(getValue(columnMap, values, LoincHeaders.ORDER_OBS))
			.relatedNames(relatedNames.isEmpty() ? null : relatedNames)
			.versionFirstReleased(getValue(columnMap, values, LoincHeaders.VERSION_FIRST_RELEASED))
			.versionLastChanged(getValue(columnMap, values, LoincHeaders.VERSION_LAST_CHANGED))
			.changeType(getValue(columnMap, values, LoincHeaders.CHANGE_TYPE))
			.exampleUnits(getValue(columnMap, values, LoincHeaders.EXAMPLE_UNITS))
			.exampleUcumUnits(getValue(columnMap, values, LoincHeaders.EXAMPLE_UCUM_UNITS))
			.released(Boolean.TRUE)
			.build();
	}

	private LoincPartDocument parseLoincPart(Map<String, Integer> columnMap, String[] values) {
		String partNumber = getValue(columnMap, values, LoincHeaders.PART_NUMBER);
		if (Strings.isNullOrEmpty(partNumber)) {
			return null;
		}

		String status = getValue(columnMap, values, LoincHeaders.PART_STATUS);
		if (Strings.isNullOrEmpty(status)) {
			status = "ACTIVE";
		}

		boolean active = "ACTIVE".equalsIgnoreCase(status);

		return LoincPartDocument.builder()
			.id(partNumber)
			.partNumber(partNumber)
			.active(active)
			.status(status)
			.partTypeName(getValue(columnMap, values, LoincHeaders.PART_TYPE_NAME))
			.partName(getValue(columnMap, values, LoincHeaders.PART_NAME))
			.partDisplayName(getValue(columnMap, values, LoincHeaders.PART_DISPLAY_NAME))
			.released(Boolean.TRUE)
			.build();
	}

	private String getValue(Map<String, Integer> columnMap, String[] values, String columnName) {
		Integer index = columnMap.get(columnName);
		if (index == null || index >= values.length) {
			return null;
		}
		String value = values[index];
		return Strings.isNullOrEmpty(value) ? null : value.trim();
	}
}
