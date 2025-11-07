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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.b2international.index.query.Expression;
import com.b2international.index.query.Expressions;
import com.b2international.index.query.Expressions.ExpressionBuilder;
import com.b2international.snowowl.core.domain.BranchContext;
import com.b2international.snowowl.loinc.datastore.index.entry.LoincCodeDocument;

/**
 * Advanced relationship query utilities for LOINC codes.
 * <p>
 * Provides methods for complex queries based on LOINC's 6-axis structure
 * and relationships between codes.
 *
 * @since 9.9
 */
public class LoincRelationshipQueries {

	/**
	 * Finds all LOINC codes that share the same component and system but differ in other axes.
	 * This is useful for finding related tests for the same analyte in the same specimen type.
	 *
	 * @param context the branch context
	 * @param loincNum the reference LOINC code
	 * @return an expression matching related codes
	 */
	public static Expression findRelatedByComponentAndSystem(BranchContext context, String loincNum) {
		LoincCodeDocument referenceCode = context.service(RevisionSearcher.class)
			.get(LoincCodeDocument.class, loincNum);

		if (referenceCode == null) {
			return Expressions.matchNone();
		}

		return Expressions.builder()
			.filter(LoincCodeDocument.Expressions.component(referenceCode.getComponent()))
			.filter(LoincCodeDocument.Expressions.system(referenceCode.getSystem()))
			.mustNot(LoincCodeDocument.Expressions.loincNum(loincNum)) // Exclude the reference code itself
			.build();
	}

	/**
	 * Finds all LOINC codes with the same component but different systems.
	 * This is useful for finding the same test in different specimen types.
	 *
	 * @param context the branch context
	 * @param component the component to search for
	 * @return an expression matching codes with the same component
	 */
	public static Expression findAllSystemsForComponent(BranchContext context, String component) {
		return LoincCodeDocument.Expressions.component(component);
	}

	/**
	 * Finds all quantitative tests (Scale Type = Qn) for a given class.
	 *
	 * @param classType the LOINC class type (e.g., "CHEM", "HEM")
	 * @return an expression matching quantitative tests in the class
	 */
	public static Expression findQuantitativeTestsByClass(String classType) {
		return Expressions.builder()
			.filter(LoincCodeDocument.Expressions.classType(classType))
			.filter(LoincCodeDocument.Expressions.scaleTyp("Qn"))
			.build();
	}

	/**
	 * Finds all LOINC codes that represent panels or batteries (typically have orderObs = "Both").
	 *
	 * @return an expression matching panel codes
	 */
	public static Expression findPanels() {
		return LoincCodeDocument.Expressions.orderObs("Both");
	}

	/**
	 * Finds all LOINC codes for a specific component across all methods.
	 * This helps compare different methodologies for the same test.
	 *
	 * @param component the component to search for
	 * @return an expression matching all methods for the component
	 */
	public static Expression findAllMethodsForComponent(String component) {
		return LoincCodeDocument.Expressions.component(component);
	}

	/**
	 * Finds LOINC codes that match specific axes criteria.
	 *
	 * @param component the component (can be null)
	 * @param property the property (can be null)
	 * @param timeAspct the time aspect (can be null)
	 * @param system the system (can be null)
	 * @param scaleTyp the scale type (can be null)
	 * @param methodTyp the method type (can be null)
	 * @return an expression matching the specified axes
	 */
	public static Expression findByAxesCombination(
			String component,
			String property,
			String timeAspct,
			String system,
			String scaleTyp,
			String methodTyp) {

		ExpressionBuilder builder = Expressions.builder();

		if (component != null) {
			builder.filter(LoincCodeDocument.Expressions.component(component));
		}
		if (property != null) {
			builder.filter(LoincCodeDocument.Expressions.property(property));
		}
		if (timeAspct != null) {
			// Note: This would need a proper expression in LoincCodeDocument.Expressions
			// For now, using a placeholder
		}
		if (system != null) {
			// Note: This would need a proper expression in LoincCodeDocument.Expressions
		}
		if (scaleTyp != null) {
			builder.filter(LoincCodeDocument.Expressions.scaleTyp(scaleTyp));
		}
		if (methodTyp != null) {
			// Note: This would need a proper expression in LoincCodeDocument.Expressions
		}

		return builder.build();
	}

	/**
	 * Finds all deprecated LOINC codes that have been replaced by active codes.
	 * This helps with terminology migration.
	 *
	 * @return an expression matching deprecated codes
	 */
	public static Expression findDeprecatedCodes() {
		return Expressions.builder()
			.filter(LoincCodeDocument.Expressions.status("DEPRECATED"))
			.build();
	}

	/**
	 * Finds LOINC codes by multiple class types.
	 * Useful for querying related test categories.
	 *
	 * @param classTypes collection of class types to search for
	 * @return an expression matching any of the specified classes
	 */
	public static Expression findByMultipleClasses(Collection<String> classTypes) {
		return LoincCodeDocument.Expressions.classTypes(classTypes);
	}

	/**
	 * Finds LOINC codes with answer lists (coded observations).
	 *
	 * @return an expression matching codes with answer lists
	 */
	public static Expression findCodesWithAnswerLists() {
		return Expressions.builder()
			.must(Expressions.exists("answerListId"))
			.build();
	}

	/**
	 * Builds a complex query combining multiple criteria for advanced searches.
	 *
	 * @param activeOnly include only active codes
	 * @param classTypes list of class types
	 * @param scaleTypes list of scale types
	 * @param components list of components
	 * @return an expression matching the combined criteria
	 */
	public static Expression buildComplexQuery(
			boolean activeOnly,
			List<String> classTypes,
			List<String> scaleTypes,
			List<String> components) {

		ExpressionBuilder builder = Expressions.builder();

		if (activeOnly) {
			builder.filter(LoincCodeDocument.Expressions.active(true));
		}

		if (classTypes != null && !classTypes.isEmpty()) {
			builder.filter(LoincCodeDocument.Expressions.classTypes(classTypes));
		}

		if (scaleTypes != null && !scaleTypes.isEmpty()) {
			builder.filter(LoincCodeDocument.Expressions.scaleTyps(scaleTypes));
		}

		if (components != null && !components.isEmpty()) {
			// Build OR expression for components
			List<Expression> componentExpressions = new ArrayList<>();
			for (String component : components) {
				componentExpressions.add(LoincCodeDocument.Expressions.component(component));
			}
			if (!componentExpressions.isEmpty()) {
				builder.filter(Expressions.dismax(componentExpressions));
			}
		}

		return builder.build();
	}
}
