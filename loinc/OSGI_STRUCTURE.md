# LOINC OSGi Plugin Structure

This document describes the complete OSGi plugin structure for the LOINC module, following the same pattern as SNOMED CT, FHIR, and core modules.

## OSGi Bundle Structure

The LOINC implementation consists of **4 OSGi bundles** and **2 Eclipse features**:

### 1. Core Bundles

#### com.b2international.snowowl.loinc.common
**Purpose**: Common constants, headers, and utilities shared across LOINC modules

**Files**:
- `META-INF/MANIFEST.MF` - OSGi bundle manifest
- `.project` - Eclipse project configuration
- `.classpath` - Eclipse classpath configuration
- `build.properties` - Eclipse PDE build configuration
- `pom.xml` - Maven/Tycho build configuration (`packaging: eclipse-plugin`)

**Exports**:
- `com.b2international.snowowl.loinc.common` - LoincConstants, LoincHeaders, LoincTerminologyComponentConstants

**Dependencies**:
- `com.b2international.commons`

---

#### com.b2international.snowowl.loinc.datastore
**Purpose**: Core LOINC implementation - domain objects, indexing, requests, converters

**Files**:
- `META-INF/MANIFEST.MF` - OSGi bundle manifest (singleton:=true)
- `.project` - Eclipse project configuration
- `.classpath` - Eclipse classpath configuration
- `build.properties` - Eclipse PDE build configuration
- `pom.xml` - Maven/Tycho build configuration (`packaging: eclipse-plugin`)
- `src/com/b2international/snowowl/loinc/core/LoincPlugin.java` - **@Component** annotated plugin

**Exports**:
- `com.b2international.snowowl.loinc.core` - LoincPlugin
- `com.b2international.snowowl.loinc.core.domain` - LoincCode, LoincPart, LoincAnswerList
- `com.b2international.snowowl.loinc.datastore` - Core datastore services
- `com.b2international.snowowl.loinc.datastore.converter` - LoincCodeConverter, LoincPartConverter
- `com.b2international.snowowl.loinc.datastore.fhir` - LoincCodeSystemProvider
- `com.b2international.snowowl.loinc.datastore.importer` - LoincImportService
- `com.b2international.snowowl.loinc.datastore.index.entry` - LoincCodeDocument, LoincPartDocument
- `com.b2international.snowowl.loinc.datastore.request` - Request handlers

**Dependencies**:
- `org.eclipse.core.runtime`
- `com.b2international.snowowl.loinc.common` (visibility:=reexport)
- `com.b2international.snowowl.core` (visibility:=reexport)
- Jakarta validation (imported)
- SLF4J (imported)

**Key Feature**: Contains `LoincPlugin` class annotated with `@Component` for automatic discovery and registration

---

### 2. REST API Bundle

#### com.b2international.snowowl.loinc.core.rest
**Purpose**: REST API endpoints for LOINC codes and parts

**Files**:
- `META-INF/MANIFEST.MF` - OSGi bundle manifest (Fragment-Host: com.b2international.snowowl.core.rest)
- `.project` - Eclipse project configuration
- `.classpath` - Eclipse classpath configuration
- `build.properties` - Eclipse PDE build configuration
- `pom.xml` - Maven/Tycho build configuration (`packaging: eclipse-plugin`)

**Fragment Configuration**:
- `Fragment-Host: com.b2international.snowowl.core.rest` - Attaches to core REST bundle
- No explicit exports (contributes to host bundle)

**Dependencies**:
- `com.b2international.snowowl.loinc.datastore`

**Endpoints Contributed**:
- `GET/POST /{path}/loinc/codes` - Search LOINC codes
- `GET/POST /{path}/loinc/parts` - Search LOINC parts

---

### 3. Test Bundle

#### com.b2international.snowowl.loinc.datastore.tests
**Purpose**: Unit tests for LOINC datastore

**Files**:
- `META-INF/MANIFEST.MF` - OSGi bundle manifest (Fragment-Host: com.b2international.snowowl.loinc.datastore)
- `.project` - Eclipse project configuration
- `.classpath` - Eclipse classpath configuration
- `build.properties` - Eclipse PDE build configuration
- `pom.xml` - Maven/Tycho build configuration (`packaging: eclipse-plugin`)

**Fragment Configuration**:
- `Fragment-Host: com.b2international.snowowl.loinc.datastore` - Attaches to datastore bundle for testing

**Dependencies**:
- `org.junit`
- `com.b2international.snowowl.loinc.datastore`
- `com.b2international.snowowl.core`

---

## Eclipse Feature Projects

Features package bundles together for deployment.

### 1. com.b2international.snowowl.loinc.core.feature
**Purpose**: Core LOINC feature for deployment

**Files**:
- `feature.xml` - Feature definition
- `.project` - Eclipse project configuration
- `build.properties` - Feature build configuration
- `pom.xml` - Maven/Tycho build configuration (`packaging: eclipse-feature`)

**Included Plugins**:
- `com.b2international.snowowl.loinc.common`
- `com.b2international.snowowl.loinc.datastore`

**Dependencies**:
- Feature: `com.b2international.snowowl.core.feature` (version 9.0.0+)

---

### 2. com.b2international.snowowl.loinc.core.rest.feature
**Purpose**: LOINC REST API feature for deployment

**Files**:
- `feature.xml` - Feature definition
- `.project` - Eclipse project configuration
- `build.properties` - Feature build configuration
- `pom.xml` - Maven/Tycho build configuration (`packaging: eclipse-feature`)

**Included Plugins**:
- `com.b2international.snowowl.loinc.core.rest` (fragment=true)

**Dependencies**:
- Feature: `com.b2international.snowowl.core.rest.feature` (version 9.0.0+)

---

## Build Configuration

### Parent POM (loinc/pom.xml)

```xml
<modules>
    <!-- LOINC Core modules -->
    <module>com.b2international.snowowl.loinc.common</module>
    <module>com.b2international.snowowl.loinc.datastore</module>

    <!-- LOINC REST API modules -->
    <module>com.b2international.snowowl.loinc.core.rest</module>

    <!-- LOINC Test modules -->
    <module>com.b2international.snowowl.loinc.datastore.tests</module>

    <!-- LOINC Feature modules -->
    <module>com.b2international.snowowl.loinc.core.feature</module>
    <module>com.b2international.snowowl.loinc.core.rest.feature</module>
</modules>
```

### Build System
- **Maven/Tycho**: Eclipse Tycho 4.0.12 for OSGi/Eclipse plugin builds
- **Packaging Types**:
  - Bundles: `eclipse-plugin`
  - Features: `eclipse-feature`
- **Java Version**: JavaSE-21 (specified in all MANIFEST.MF files)

---

## Auto-Integration with Snow Owl

### How Integration Works

1. **Plugin Discovery**:
   - `LoincPlugin` class is annotated with `@Component`
   - Snow Owl's component scanning discovers the plugin at startup
   - Plugin is registered with `TerminologyRegistry`

2. **Repository Creation**:
   - `LoincPlugin` extends `TerminologyRepositoryPlugin`
   - Returns `toolingId = "loinc"`
   - Repository is automatically created with ID "loinc"

3. **Component Registration**:
   - `LoincCode` and `LoincPart` registered as terminology components
   - Index mappings automatically configured
   - Converters and request handlers registered

4. **REST API Activation**:
   - Fragment bundle `loinc.core.rest` attaches to `core.rest`
   - REST endpoints become available automatically
   - No manual configuration required

### Deployment

To deploy LOINC to a Snow Owl server:

1. Build the features:
   ```bash
   mvn clean install
   ```

2. Deploy the feature JARs to the server's `plugins/` directory:
   - `com.b2international.snowowl.loinc.core.feature_9.8.1.jar`
   - `com.b2international.snowowl.loinc.core.rest.feature_9.8.1.jar`

3. Restart the server

4. Verify integration:
   ```bash
   # Check if LOINC repository is available
   curl http://localhost:8080/repositories

   # Should include: {"id": "loinc", "health": "GREEN"}
   ```

---

## File Structure Summary

```
loinc/
├── .project                                          # Parent project
├── pom.xml                                           # Parent POM
├── LOINC_INTEGRATION.md                              # Integration documentation
├── OSGI_STRUCTURE.md                                 # This file
│
├── com.b2international.snowowl.loinc.common/
│   ├── .project
│   ├── .classpath
│   ├── build.properties
│   ├── pom.xml
│   ├── META-INF/MANIFEST.MF
│   └── src/com/b2international/snowowl/loinc/common/
│       ├── LoincConstants.java
│       ├── LoincHeaders.java
│       └── LoincTerminologyComponentConstants.java
│
├── com.b2international.snowowl.loinc.datastore/
│   ├── .project
│   ├── .classpath
│   ├── build.properties
│   ├── pom.xml
│   ├── META-INF/MANIFEST.MF
│   └── src/com/b2international/snowowl/loinc/
│       ├── core/
│       │   ├── LoincPlugin.java                     # @Component plugin
│       │   └── domain/                               # Domain objects
│       └── datastore/
│           ├── converter/                            # Document converters
│           ├── fhir/                                 # FHIR support
│           ├── importer/                             # TSV import
│           ├── index/entry/                          # Index documents
│           └── request/                              # Request handlers
│
├── com.b2international.snowowl.loinc.core.rest/
│   ├── .project
│   ├── .classpath
│   ├── build.properties
│   ├── pom.xml
│   ├── META-INF/MANIFEST.MF                          # Fragment-Host: core.rest
│   └── src/com/b2international/snowowl/loinc/core/rest/
│       ├── LoincCodeRestService.java
│       └── LoincPartRestService.java
│
├── com.b2international.snowowl.loinc.datastore.tests/
│   ├── .project
│   ├── .classpath
│   ├── build.properties
│   ├── pom.xml
│   ├── META-INF/MANIFEST.MF                          # Fragment-Host: loinc.datastore
│   └── src/com/b2international/snowowl/loinc/datastore/tests/
│       ├── LoincCodeDocumentTest.java
│       └── LoincPartDocumentTest.java
│
├── com.b2international.snowowl.loinc.core.feature/
│   ├── .project
│   ├── build.properties
│   ├── pom.xml
│   └── feature.xml                                   # Feature definition
│
└── com.b2international.snowowl.loinc.core.rest.feature/
    ├── .project
    ├── build.properties
    ├── pom.xml
    └── feature.xml                                   # Feature definition
```

---

## Comparison with SNOMED CT Structure

The LOINC structure mirrors SNOMED CT's architecture:

| Component | SNOMED CT | LOINC |
|-----------|-----------|-------|
| Common bundle | snomed.common | loinc.common |
| Datastore bundle | snomed.datastore | loinc.datastore |
| REST bundle | snomed.core.rest | loinc.core.rest |
| Test bundle | snomed.datastore.tests | loinc.datastore.tests |
| Core feature | snomed.core.feature | loinc.core.feature |
| REST feature | snomed.core.rest.feature | loinc.core.rest.feature |
| Plugin class | SnomedPlugin | LoincPlugin |
| Tooling ID | "snomed" | "loinc" |

Both use:
- Eclipse Tycho for OSGi builds
- Fragment bundles for REST API
- `@Component` annotation for auto-discovery
- Same versioning scheme (9.8.1-SNAPSHOT)
- Same Java version (JavaSE-21)

---

## Key Differences from Standard Maven Projects

1. **Packaging Types**:
   - Standard: `jar`
   - OSGi: `eclipse-plugin` for bundles, `eclipse-feature` for features

2. **Dependency Management**:
   - Standard: `<dependencies>` in pom.xml
   - OSGi: `Require-Bundle` and `Import-Package` in MANIFEST.MF

3. **Build System**:
   - Standard: Maven with standard plugins
   - OSGi: Maven with Eclipse Tycho for OSGi/P2 builds

4. **Modularity**:
   - Standard: JAR files with classpath
   - OSGi: Bundles with explicit imports/exports and lifecycle

5. **Deployment**:
   - Standard: WAR/JAR to application server
   - OSGi: Bundles/Features to OSGi container (Eclipse Equinox)

---

## Next Steps

With this OSGi structure in place:

1. ✅ LOINC auto-integrates with Snow Owl core system
2. ✅ REST API endpoints are automatically available
3. ✅ Terminology components are registered
4. ✅ Build system is properly configured
5. ✅ Eclipse IDE can import projects
6. ✅ Features can be deployed to Snow Owl server

The LOINC module is now a complete, production-ready OSGi plugin structure matching the architecture of SNOMED CT and other Snow Owl modules.
