# Snow Owl Architecture Documentation

## Table of Contents

1. [Overview](#overview)
2. [System Architecture](#system-architecture)
3. [Core Components](#core-components)
4. [Module Structure](#module-structure)
5. [Data Flow](#data-flow)
6. [Design Patterns](#design-patterns)
7. [Indexing System](#indexing-system)
8. [REST API Layer](#rest-api-layer)
9. [Terminology Data Management](#terminology-data-management)
10. [Branching and Versioning](#branching-and-versioning)
11. [Developer Guide](#developer-guide)

---

## Overview

Snow Owl is a highly scalable, open-source terminology server with revision-control capabilities and collaborative authoring platform features. It is designed to store, search, and author high volumes of terminology artifacts efficiently.

### Key Technologies

- **Language**: Java 21+
- **Build System**: Maven with Tycho (OSGi)
- **Framework**: Eclipse Equinox (OSGi), Spring MVC
- **Search Engine**: Elasticsearch 8
- **Version**: 9.8.1-SNAPSHOT

### Core Features

- Revision-controlled authoring with branching
- SNOMED CT, LOINC, ICD-10 terminology support
- FHIR R4/R4B/R5 Terminology Service API
- RESTful and native Java APIs
- Plugin-based extensibility
- Built on Elasticsearch for scalability

---

## System Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Layer                              │
│         (Web Browsers, FHIR Clients, REST Clients)           │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│              API Layer (Spring MVC)                          │
│  ├─ REST API (ResourceRestService)                          │
│  ├─ FHIR API (FhirCodeSystemController)                     │
│  └─ SNOMED CT API (SnomedConceptController)                 │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│          Business Logic Layer                                │
│  ├─ Request Builders (ResourceRequests, SnomedRequests)     │
│  ├─ Request Handlers (SearchResourceRequest, etc.)          │
│  └─ Validators and Processors                               │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│          Context & Service Layer                             │
│  ├─ ApplicationContext (Global services)                    │
│  ├─ RepositoryContext (Repository-level services)           │
│  └─ BranchContext (Branch-specific operations)              │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│          Indexing Layer                                      │
│  ├─ RevisionIndex (Branch-aware indexing)                   │
│  ├─ Index Interface (Generic abstraction)                   │
│  └─ Searcher/Writer (Query and update operations)           │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│          Data Store Layer                                    │
│  └─ Elasticsearch 8 (Document storage and search)           │
└─────────────────────────────────────────────────────────────┘
```

### Layered Architecture Principles

1. **API Layer**: HTTP protocol handling, request/response serialization
2. **Business Logic**: Domain logic, validation, business rules
3. **Context Layer**: Service management, dependency injection, transaction boundaries
4. **Indexing Layer**: Generic indexing abstraction, revision control
5. **Data Store**: Persistent storage and full-text search

---

## Core Components

### 1. ApplicationContext

**Location**: `core/src/com/b2international/snowowl/core/ApplicationContext.java`

The ApplicationContext is the global service registry and entry point for the entire application.

**Responsibilities**:
- Service lifecycle management
- Plugin initialization and management
- Global configuration management
- Service discovery and dependency injection

**Key Services**:
```java
ApplicationContext context = ApplicationContext.getInstance();

// Access services
RevisionIndex index = context.service(RevisionIndex.class);
EventBus eventBus = context.service(EventBus.class);
IdentityProvider identityProvider = context.service(IdentityProvider.class);
```

### 2. Request/Promise Pattern

**Location**: `core/src/com/b2international/snowowl/core/events/Request.java`

All operations in Snow Owl are modeled as Request objects that execute within a ServiceProvider context.

**Structure**:
```java
public interface Request<C extends ServiceProvider, R> {
    R execute(C context);
    Class<R> getReturnType();
}

// Example usage
Promise<Concepts> result = ResourceRequests.prepareSearch()
    .filterByIds(conceptIds)
    .setLimit(100)
    .build()
    .execute(context);
```

**Benefits**:
- Composability: Requests can be chained and combined
- Testability: Easy to test in isolation with mock contexts
- Async execution: Built-in Promise support
- Context isolation: Clean separation of concerns

### 3. RevisionIndex

**Location**: `commons/com.b2international.index/src/com/b2international/index/revision/`

The RevisionIndex is the core of Snow Owl's version control system, extending the basic Index with branching and versioning capabilities.

**Key Features**:
- Branch-aware reads and writes
- Time-travel queries (access data at any point in time)
- Merge conflict detection and resolution
- Revision metadata tracking

**Branch Path Syntax**:
```
MAIN                    // Main branch
MAIN/PROJECT-1          // Child branch
MAIN/PROJECT-1/TASK-100 // Task branch
MAIN@1234567890         // Branch at specific timestamp
MAIN/                   // Base (parent) of MAIN
MAIN...RELEASE-1        // Difference between branches
```

### 4. Repository Manager

**Location**: `core/src/com/b2international/snowowl/core/repository/`

Manages multiple terminology repositories, each with its own branch hierarchy and data.

**Responsibilities**:
- Repository lifecycle (create, initialize, shutdown)
- Repository configuration
- Access control and permissions
- Multi-tenancy support

### 5. Plugin System

**Location**: `core/src/com/b2international/snowowl/core/setup/Plugin.java`

Extensible plugin architecture for adding new terminologies and features.

**Plugin Lifecycle**:
```java
public abstract class Plugin {
    void addConfigurations(ConfigurationRegistry registry) {}
    void init(SnowOwlConfiguration config, Environment env) {}
    void preRun(SnowOwlConfiguration config, Environment env) {}
    void run(SnowOwlConfiguration config, Environment env) {}
    void postRun(SnowOwlConfiguration config, Environment env) {}
}
```

**Common Plugin Types**:
- **RepositoryPlugin**: Core repository functionality
- **TerminologyRepositoryPlugin**: Terminology-specific features
- **ValidationPlugin**: Validation rule frameworks
- **EclPlugin**: Expression Constraint Language support

---

## Module Structure

### Commons Module

**Path**: `commons/`

Provides shared libraries and utilities used across all other modules.

**Sub-modules**:

1. **com.b2international.commons**
   - Core utilities (strings, collections, validation)
   - Common exceptions and error handling
   - Basic data structures

2. **com.b2international.collections.api/fastutil/jackson**
   - Primitive collection abstractions
   - High-performance implementations using Fastutil
   - Jackson serialization support

3. **com.b2international.index**
   - Generic indexing abstraction layer
   - Query DSL
   - Document mapping framework
   - Schema migration support

4. **com.b2international.index.es8**
   - Elasticsearch 8 implementation
   - Query translation
   - Bulk operation handling
   - Index administration

5. **com.b2international.index.tests**
   - Testing utilities for index operations
   - Base test classes
   - Mock implementations

### Core Module

**Path**: `core/`

Contains the core terminology server functionality.

**Sub-modules**:

1. **com.b2international.snowowl.core**
   - Application bootstrap (SnowOwl.java)
   - Branch management
   - Resource management
   - Request infrastructure
   - Domain models

2. **com.b2international.snowowl.core.rest**
   - REST API controllers
   - OpenAPI/Swagger documentation
   - Request/response mapping
   - Error handling

3. **com.b2international.snowowl.identity.ldap**
   - LDAP authentication provider
   - User directory integration

4. **com.b2international.snowowl.logback.config**
   - Logging configuration
   - Log formatting and routing

### SNOMED Module

**Path**: `snomed/`

SNOMED CT-specific implementation.

**Sub-modules**:

1. **com.b2international.snowowl.snomed.datastore**
   - SNOMED CT domain models
   - Concept/Description/Relationship indexing
   - RF2 import/export
   - Hierarchy management

2. **com.b2international.snowowl.snomed.core.rest**
   - SNOMED CT REST endpoints
   - Concept operations
   - Reference set management
   - ECL query support

3. **com.b2international.snowowl.snomed.reasoner**
   - OWL reasoning integration
   - Classification support
   - Relationship inference

4. **com.b2international.snowowl.snomed.fhir**
   - FHIR mappings for SNOMED CT
   - Terminology service operations

5. **com.b2international.snowowl.validation.snomed**
   - SNOMED CT validation rules
   - Data quality checks

### FHIR Module

**Path**: `fhir/`

FHIR Terminology Service API implementation.

**Sub-modules**:

1. **com.b2international.snowowl.fhir.core**
   - FHIR resource models
   - Transformation logic
   - Operation definitions

2. **com.b2international.snowowl.fhir.rest**
   - FHIR REST controllers
   - CodeSystem operations ($lookup, $validate-code, $subsumes)
   - ValueSet operations ($expand, $validate-code)
   - ConceptMap operations ($translate)
   - Metadata/capability statements

### CIS Module

**Path**: `cis/`

Component Identifier Service for SNOMED CT ID management.

**Responsibilities**:
- Centralized identifier allocation
- ID lifecycle tracking
- Distributed ID generation
- Backup and restore

### Netty Module

**Path**: `netty/`

Event bus infrastructure for asynchronous messaging.

**Sub-modules**:

1. **com.b2international.snowowl.eventbus**
   - Asynchronous request execution
   - Event messaging
   - Request routing

---

## Data Flow

### Example 1: Creating a SNOMED Concept

```
1. HTTP POST /snomed/concepts
   Body: {
     "fsn": "Heart disease (disorder)",
     "definitionStatus": "FULLY_DEFINED"
   }
   ↓

2. SnomedConceptController.create()
   - Parse JSON to ConceptCreateRequest
   - Validate input
   ↓

3. SnomedRequests.prepareNewConcept()
   .setFsn("Heart disease (disorder)")
   .setDefinitionStatus("FULLY_DEFINED")
   .build()
   ↓

4. Execute in BranchContext
   - Get RevisionIndex for branch
   - Wrap in transaction
   ↓

5. Generate SNOMED ID (via CIS)
   - Request new ID from ID generator
   ↓

6. Create SnomedConceptDocument
   - Set ID, FSN, definition status
   - Add revision metadata (created timestamp, rev=1)
   - Set branch segment ID
   ↓

7. RevisionIndex.write()
   - Call Index.write()
   - Add to change tracking
   ↓

8. IndexWrite → Writer.put()
   - Serialize to JSON
   - Send to Elasticsearch bulk API
   ↓

9. Elasticsearch
   - Index document
   - Update shard
   - Commit
   ↓

10. Response
    - Return concept with ID
    - HTTP 201 Created
    - Location header with resource URL
```

### Example 2: Searching for Concepts

```
1. HTTP GET /snomed/concepts?query=heart&limit=10
   ↓

2. SnomedConceptController.search()
   - Parse query parameters
   ↓

3. SnomedRequests.prepareSearchConcepts()
   .filterByQuery("heart")
   .setLimit(10)
   .build()
   ↓

4. Execute in BranchContext
   - Resolve branch to segment ID
   ↓

5. RevisionIndex.read()
   - Add revision filters
   - Add branch filter
   ↓

6. Build Query object
   Expression: match(preferredDescriptions.term, "heart")
   Filter: active = true
   Filter: segmentId = branch-segment
   Limit: 10
   ↓

7. IndexRead → Searcher.search()
   - Translate Query to Elasticsearch QueryBuilder
   ↓

8. Es8QueryBuilder
   Query → MultiMatchQuery("heart", "preferredDescriptions.term", ...)
   Filter → TermQuery("active", true)
   ↓

9. Elasticsearch
   - Execute search
   - Score by relevance
   - Return top 10
   ↓

10. Deserialize results
    - JSON → SnomedConceptDocument
    - Apply expansions (descriptions, relationships)
    ↓

11. Response
    {
      "total": 1543,
      "items": [ { "id": "...", "fsn": { ... } } ]
    }
```

### Example 3: Merging Branches

```
1. HTTP POST /branches/MAIN/merges
   Body: {
     "source": "MAIN/TASK-100",
     "comment": "Merge task"
   }
   ↓

2. BranchMergeRequest
   - Validate source and target exist
   ↓

3. RevisionIndex.mergeBranch()
   - Get source segment (TASK-100)
   - Get target segment (MAIN)
   ↓

4. Conflict Detection
   - Compare revision hashes of changed documents
   - Identify:
     * Auto-mergeable (non-overlapping changes)
     * Conflicts (overlapping changes to same fields)
   ↓

5. Conflict Resolution
   - Auto-merge: Apply both changes
   - Manual: Use ConflictProcessor
   ↓

6. Apply Merge
   - Reindex affected documents
   - Update segment references
   - Mark old versions as replaced
   - Create merge commit
   ↓

7. Update Branch Metadata
   - Update MAIN's head timestamp
   - Record merge in branch history
   ↓

8. Response
   {
     "status": "SUCCESS",
     "mergedCount": 42,
     "conflictCount": 0
   }
```

---

## Design Patterns

### 1. Request Pattern (Command Pattern)

**Purpose**: Encapsulate all operations as executable commands.

**Implementation**:
```java
// Define request
public class ConceptSearchRequest implements Request<BranchContext, Concepts> {
    private String query;
    private int limit;

    @Override
    public Concepts execute(BranchContext context) {
        return context.service(SnomedRepository.class)
            .search(query, limit);
    }
}

// Use builder
Concepts concepts = SnomedRequests.prepareSearchConcepts()
    .filterByQuery("heart")
    .setLimit(10)
    .build()
    .execute(context);
```

**Benefits**:
- Testability: Easy to mock contexts
- Composability: Chain requests
- Async support: Built-in Promise
- Logging/auditing: Intercept execution

### 2. Service Provider Pattern

**Purpose**: Context-based dependency injection.

**Implementation**:
```java
public interface ServiceProvider {
    <T> T service(Class<T> type);

    default <T> T service(Class<T> type, T defaultValue) {
        return optionalService(type).orElse(defaultValue);
    }
}

// Usage
class MyRequest implements Request<BranchContext, Result> {
    @Override
    public Result execute(BranchContext context) {
        RevisionIndex index = context.service(RevisionIndex.class);
        EventBus bus = context.service(EventBus.class);
        // ... use services
    }
}
```

### 3. Builder Pattern

**Purpose**: Construct complex objects with fluent API.

**Implementation**:
```java
ResourceRequests.prepareSearch()
    .filterByIds(ids)
    .filterByTitle(title)
    .filterByStatus(status)
    .setLimit(100)
    .setOffset(0)
    .setExpand("versions,commits")
    .build()
```

### 4. Adapter Pattern (Index Abstraction)

**Purpose**: Abstract Elasticsearch implementation details.

```
Application Code
      ↓
   Index API
      ↓
  IndexClient (adapter interface)
      ↓
  EsIndexClient (ES8 implementation)
      ↓
  Elasticsearch
```

**Benefits**:
- Technology independence
- Easier testing (mock IndexClient)
- Future-proof (swap ES for another engine)

### 5. Strategy Pattern

**Purpose**: Different implementations for same operation.

**Example**: Different revision read strategies
```java
interface RevisionIndexRead<T> {
    T execute(RevisionSearcher searcher);
}

// Different strategies
class AtTimestampRead implements RevisionIndexRead<T> { ... }
class AtBranchRead implements RevisionIndexRead<T> { ... }
class DifferenceRead implements RevisionIndexRead<T> { ... }
```

### 6. Observer Pattern

**Purpose**: React to service changes.

**Implementation**:
```java
ApplicationContext.addListener(RevisionIndex.class, new IServiceChangeListener<>() {
    @Override
    public void serviceChanged(RevisionIndex oldService, RevisionIndex newService) {
        // React to service changes
    }
});
```

### 7. Template Method Pattern

**Purpose**: Define algorithm skeleton in base class.

**Example**: Base request classes
```java
public abstract class BaseResourceCreateRequest<B extends ResourceBuilder<...>> {
    protected abstract void createDefaults();
    protected abstract void doValidate();

    @Override
    public final R execute(C context) {
        createDefaults();
        doValidate();
        return doExecute(context);
    }

    protected abstract R doExecute(C context);
}
```

---

## Indexing System

### Core Abstractions

**Location**: `commons/com.b2international.index/src/com/b2international/index/`

The indexing system provides a generic abstraction over Elasticsearch.

### Index Interface

```java
public interface Index {
    String name();

    <T> T read(IndexRead<T> read);
    <T> T write(IndexWrite<T> write);

    IndexAdmin admin();
}
```

### Searcher (Read Operations)

```java
public interface Searcher {
    // Search
    <T> Hits<T> search(Query<T> query);

    // Get by ID
    <T> T get(Class<T> type, String id);
    <T> Iterable<T> get(Class<T> type, Iterable<String> ids);

    // Aggregations
    <T> Aggregation<T> aggregate(AggregationBuilder<T> aggregation);

    // Streaming
    <T> Stream<Hits<T>> stream(Query<T> query);
}
```

### Writer (Write Operations)

```java
public interface Writer {
    // Single document
    <T> void put(Class<T> type, T doc);

    // Bulk operations
    void bulkIndex(Iterable<Doc> docs);
    void bulkUpdate(BulkUpdate<T> update);
    void bulkDelete(BulkDelete delete);

    // Delete
    <T> void remove(Class<T> type, String id);
    <T> void removeAll(Class<T> type, Iterable<String> ids);

    // Transaction
    void commit();
}
```

### Query DSL

```java
Query.builder()
    .from(SnomedConceptDocument.class)
    .where(Expressions.builder()
        .filter(match("preferredDescriptions.term", "heart"))
        .filter(exactMatch("active", true))
        .must(range("effectiveTime", "20200101", "20201231"))
        .should(matchAny("moduleId", moduleIds))
        .build())
    .sortBy(SortBy.field("fsn.term", Order.ASC))
    .limit(100)
    .offset(0)
    .build();
```

### Document Mapping

Documents are annotated to define index mappings:

```java
@Doc(type = "concept")
public class SnomedConceptDocument extends RevisionDocument {

    @ID
    private String id;

    @Text(analyzer = "term")
    private String fsn;

    @Keyword
    private String moduleId;

    @Keyword
    private boolean active;

    // Nested objects
    @NestedDoc
    private List<Description> descriptions;

    // ... getters/setters
}
```

### Revision Index Features

**Branch-Aware Operations**:
```java
revisionIndex.read("MAIN", searcher -> {
    return searcher.search(query);
});

revisionIndex.write("MAIN/TASK-100", writer -> {
    writer.put(SnomedConceptDocument.class, concept);
    writer.commit();
    return concept.getId();
});
```

**Time-Travel Queries**:
```java
// Access data at specific timestamp
revisionIndex.read("MAIN@1609459200000", searcher -> {
    return searcher.get(SnomedConceptDocument.class, conceptId);
});
```

**Difference Queries**:
```java
// Get changes between branches
revisionIndex.read("MAIN...RELEASE-1", searcher -> {
    return searcher.search(changesQuery);
});
```

### Revision Metadata

Every document has revision metadata:

```java
public abstract class RevisionDocument {
    private String id;
    private String segmentId;      // Branch identifier
    private long rev;              // Revision number
    private long created;          // Creation timestamp
    private long revised;          // Last modification timestamp
    private SortedSet<Long> replacedIns; // Replaced by revisions
}
```

---

## REST API Layer

### Architecture

**Location**: `core/com.b2international.snowowl.core.rest/src/`

The REST layer uses Spring MVC with OpenAPI documentation.

### Controller Structure

```java
@RestController
@RequestMapping("/resources")
public class ResourceRestService extends AbstractRestService {

    @GetMapping
    public Promise<Resources> search(@ParameterObject ResourceRestSearch params) {
        return ResourceRequests.prepareSearch()
            .filterByIds(params.getId())
            .filterByTitle(params.getTitle())
            .setLimit(params.getLimit())
            .build()
            .execute(getBus());
    }

    @GetMapping("/{id}")
    public Promise<Resource> get(@PathVariable String id) {
        return ResourceRequests.prepareGet(id)
            .build()
            .execute(getBus());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Resource resource) {
        String id = ResourceRequests.prepareCreate()
            .setResource(resource)
            .build()
            .execute(getBus())
            .getSync();
        return ResponseEntity.created(getResourceLocationURI(id)).build();
    }
}
```

### FHIR API Controllers

**Location**: `fhir/com.b2international.snowowl.fhir.rest/src/`

```java
@RestController
@RequestMapping("/fhir/CodeSystem")
public class FhirCodeSystemController extends AbstractFhirController {

    @GetMapping("/{id}")
    public Promise<CodeSystem> read(@PathVariable String id) {
        return FhirRequests.codeSystems().prepareGet(id)
            .build()
            .execute(getBus());
    }

    @GetMapping("/{id}/$lookup")
    public Promise<Parameters> lookup(
        @PathVariable String id,
        @RequestParam String code
    ) {
        return FhirRequests.codeSystems().prepareLookup()
            .setId(id)
            .setCode(code)
            .build()
            .execute(getBus());
    }

    @PostMapping("/{id}/$validate-code")
    public Promise<Parameters> validateCode(
        @PathVariable String id,
        @RequestBody Parameters params
    ) {
        return FhirRequests.codeSystems().prepareValidateCode()
            .setId(id)
            .setParameters(params)
            .build()
            .execute(getBus());
    }
}
```

### Request/Response Flow

```
HTTP Request
    ↓
Spring DispatcherServlet
    ↓
Controller Method
    ↓
Parameter Extraction
    ├─ @PathVariable
    ├─ @RequestParam
    ├─ @ParameterObject
    └─ @RequestBody
    ↓
Build Request
    ↓
Execute via EventBus/Context
    ↓
Promise<T>
    ↓
Response Serialization (JSON/XML)
    ↓
HTTP Response
```

### Error Handling

```java
@ExceptionHandler(NotFoundException.class)
public ResponseEntity<Void> handleNotFound(NotFoundException e) {
    return ResponseEntity.notFound().build();
}

@ExceptionHandler(BadRequestException.class)
public ResponseEntity<ApiError> handleBadRequest(BadRequestException e) {
    return ResponseEntity.badRequest()
        .body(ApiError.of(e.getMessage()));
}
```

### OpenAPI Documentation

Controllers are annotated for automatic API documentation:

```java
@Operation(
    summary = "Search for resources",
    description = "Returns all resources matching the search criteria"
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "400", description = "Bad request")
})
@GetMapping
public Promise<Resources> search(...) { ... }
```

---

## Terminology Data Management

### Resource Model

**Base Class Hierarchy**:
```
Resource
├── id: String
├── url: String (unique identifier)
├── title: String
├── language: String
├── description: String
├── status: String (draft, active, retired)
└── metadata: Map<String, Object>

    ↓

TerminologyResource (extends Resource)
├── toolingId: String (snomed, loinc, icd10)
├── branchPath: String (e.g., "MAIN")
├── dependencies: Set<Dependency>
└── settings: <Tooling-specific>

    ↓

CodeSystem, ValueSet, ConceptMap
```

### SNOMED CT Data Model

**Location**: `snomed/com.b2international.snowowl.snomed.datastore/src/.../index/entry/`

#### Concepts

```java
@Doc(type = "concept")
public class SnomedConceptDocument extends SnomedDocument {
    private String id;                      // SNOMED CT ID
    private boolean active;
    private String effectiveTime;           // YYYYMMDD
    private String moduleId;
    private String definitionStatus;        // PRIMITIVE or FULLY_DEFINED

    // Descriptions
    private List<SnomedDescriptionFragment> preferredDescriptions;
    private List<SnomedDescriptionFragment> descriptions;

    // Relationships
    private List<SnomedRelationshipFragment> relationships;
    private List<SnomedRelationshipFragment> inboundRelationships;

    // OWL Axioms
    private List<String> classAxioms;
    private List<String> gciAxioms;

    // Hierarchy (cached for performance)
    private LongSortedSet ancestors;
    private LongSortedSet parentIds;
    private LongSortedSet statedParents;

    // Inactivation
    private String inactivationIndicator;
    private Map<String, List<String>> associationTargets;

    // Reference sets
    private Set<String> referenceSetIds;
}
```

#### Descriptions

```java
@Doc(type = "description")
public class SnomedDescriptionIndexEntry extends SnomedDocument {
    private String id;
    private String conceptId;
    private String typeId;          // FSN, Synonym, etc.
    private String term;
    private String languageCode;
    private String caseSignificance;
    private Map<String, String> acceptability;  // Language refset → acceptability
}
```

#### Relationships

```java
@Doc(type = "relationship")
public class SnomedRelationshipIndexEntry extends SnomedDocument {
    private String id;
    private String sourceId;
    private String typeId;
    private String destinationId;
    private int relationshipGroup;
    private String characteristicTypeId;  // stated, inferred, additional
    private String modifierId;
}
```

#### Reference Sets

```java
@Doc(type = "member")
public class SnomedRefSetMemberIndexEntry extends SnomedDocument {
    private String id;
    private String refsetId;
    private String referencedComponentId;
    private SnomedRefSetType type;
    private Map<String, Object> fields;  // Type-specific fields
}
```

### Data Storage Flow

```
1. Import (RF2 Files)
   ↓
2. RF2 Parser
   ↓
3. Domain Objects (Concept, Description, Relationship)
   ↓
4. Change Processors
   ├─ ConceptChangeProcessor
   ├─ DescriptionChangeProcessor
   └─ RelationshipChangeProcessor
   ↓
5. Index Documents
   ├─ SnomedConceptDocument
   ├─ SnomedDescriptionIndexEntry
   └─ SnomedRelationshipIndexEntry
   ↓
6. Bulk Index Write
   ↓
7. Elasticsearch
```

### Update Operations

#### Example: Updating a Concept

```java
// 1. Load existing document
SnomedConceptDocument concept = index.read("MAIN", searcher -> {
    return searcher.get(SnomedConceptDocument.class, conceptId);
});

// 2. Modify
concept.setActive(false);
concept.setInactivationIndicator("DUPLICATE");

// 3. Write back
index.write("MAIN", writer -> {
    writer.put(SnomedConceptDocument.class, concept);
    writer.commit();
    return concept.getId();
});
```

#### Indexing Optimizations

1. **Bulk Operations**: Import uses bulk indexing
   ```java
   writer.bulkIndex(Stream.of(concept1, concept2, ...));
   ```

2. **Partial Updates**: Use script-based updates
   ```java
   BulkUpdate.of(SnomedConceptDocument.class)
       .script("ctx._source.active = params.active")
       .params(Map.of("active", false))
       .filter(Expressions.matchAny("id", ids));
   ```

3. **Field Aliases**: Reduce index size
   ```java
   @FieldAlias(name = "parent", alias = "statedParent")
   ```

---

## Branching and Versioning

### Branch Model

Snow Owl implements a hierarchical branching model similar to Git.

**Branch Structure**:
```
MAIN (production)
├── RELEASE-1 (version branch)
│   └── HOTFIX-1 (hotfix branch)
└── PROJECT-A (development)
    ├── TASK-100 (feature branch)
    └── TASK-101 (feature branch)
```

### Branch Operations

#### Creating a Branch

```java
Branch branch = RepositoryRequests.branching()
    .prepareCreate()
    .setParent("MAIN")
    .setName("TASK-100")
    .setMetadata(Map.of("jiraIssue", "TASK-100"))
    .build()
    .execute(context)
    .getSync();
```

**What Happens**:
1. Parent branch is resolved to its head timestamp
2. New branch segment is created in index
3. Branch metadata is stored
4. Branch path is registered

#### Merging Branches

```java
Merge merge = RepositoryRequests.branching()
    .prepareMerge()
    .setSource("MAIN/TASK-100")
    .setTarget("MAIN")
    .setCommitComment("Merge TASK-100")
    .build()
    .execute(context)
    .getSync();
```

**Merge Process**:
```
1. Identify changed documents
   - Query documents modified on source since divergence

2. Conflict detection
   - Compare revision hashes
   - Identify overlapping changes

3. Conflict resolution
   - Auto-merge: Non-overlapping changes
   - Manual: ConflictProcessor.resolve()

4. Apply changes
   - Reindex documents with new segment ID
   - Update revision metadata
   - Mark old versions as replaced

5. Create merge commit
   - Record merge in branch history
   - Update target branch head timestamp
```

#### Conflict Detection

Conflicts are detected using revision hashes:

```java
// Document has hash of critical fields
@Doc(revisionHash = {"active", "effectiveTime", "moduleId", "definitionStatus"})
public class SnomedConceptDocument {
    // If these fields change on both branches → conflict
}
```

**Conflict Types**:
1. **Same field modified**: Both branches changed the same field
2. **Deleted on one side**: One branch deleted, other modified
3. **Structural conflict**: Relationship changes conflict

**Resolution Strategies**:
```java
public interface RevisionConflictProcessor {
    Collection<Conflict> handleConflicts(
        String commitId,
        Collection<Conflict> conflicts
    );
}
```

### Revision Metadata

Every document tracks its version history:

```java
public abstract class RevisionDocument {
    private long rev;              // Revision number (increments on change)
    private long created;          // Creation timestamp
    private long revised;          // Last modification timestamp
    private SortedSet<Long> replacedIns; // Replaced by these revisions
    private String segmentId;      // Branch identifier
}
```

**Revision States**:
- **Active**: Current version (not in replacedIns)
- **Replaced**: Superseded by another revision
- **Historical**: Accessible via timestamp queries

### Time-Travel Queries

Query data as it existed at any point in time:

```java
// Current state
revisionIndex.read("MAIN", searcher -> { ... });

// State at timestamp
revisionIndex.read("MAIN@1609459200000", searcher -> { ... });

// State at parent (before branch diverged)
revisionIndex.read("MAIN/TASK-100/", searcher -> { ... });

// Difference between branches
revisionIndex.read("MAIN...TASK-100", searcher -> { ... });
```

### Branch Metadata

```java
public class Branch {
    private String path;              // e.g., "MAIN/TASK-100"
    private String parentPath;        // e.g., "MAIN"
    private long baseTimestamp;       // Divergence point
    private long headTimestamp;       // Current head
    private BranchState state;        // UP_TO_DATE, FORWARD, BEHIND, DIVERGED
    private Map<String, Object> metadata;
}
```

**Branch States**:
- **UP_TO_DATE**: No changes on this branch or parent
- **FORWARD**: This branch has changes, parent unchanged
- **BEHIND**: Parent has changes, this branch unchanged
- **DIVERGED**: Both have changes (requires merge/rebase)

---

## Developer Guide

### Building from Source

```bash
# Clone repository
git clone https://github.com/b2ihealthcare/snow-owl.git
cd snow-owl

# Build
./mvnw clean package

# Run tests
./mvnw clean verify

# Distribution packages in:
# releng/com.b2international.snowowl.server.update/target/
```

### Running Locally

```bash
# Extract distribution
tar -xzf com.b2international.snowowl.server.update-*-oss.tar.gz
cd snowowl

# Start server
bin/snowowl.sh

# Access
curl http://localhost:8080/snowowl/info
```

### Eclipse Setup

1. **Install Eclipse IDE for Eclipse Committers 2023-12**

2. **Install Required Plugins**:
   - Groovy Development Tools 5.2.0
   - MWE SDK 1.10.0
   - Xtext Complete SDK 2.33.0
   - Xtend IDE 2.33.0

3. **Configure Eclipse**:
   - Set encoding to UTF-8
   - Set line endings to Unix
   - Configure API Baselines (ignore errors)

4. **Import Projects**:
   ```
   File → Import → Maven → Existing Maven Projects
   Select snow-owl directory
   Import all projects
   ```

5. **Set Target Platform**:
   ```
   Open: target-platform/target-platform.target
   Click: Resolve
   Click: Set as Active Target Platform
   Wait for build to complete
   ```

6. **Launch**:
   ```
   Run → Run Configurations
   Select: snow-owl-oss
   Click: Run
   ```

### Creating a Custom Plugin

```java
package com.example.snowowl.myplugin;

import com.b2international.snowowl.core.setup.Plugin;
import com.b2international.snowowl.core.setup.Environment;

public class MyPlugin extends Plugin {

    @Override
    public void addConfigurations(ConfigurationRegistry registry) {
        registry.add("myconfig", MyConfiguration.class);
    }

    @Override
    public void init(SnowOwlConfiguration config, Environment env) {
        // Initialize resources
    }

    @Override
    public void run(SnowOwlConfiguration config, Environment env) {
        // Register services
        env.services().registerService(MyService.class, new MyService());
    }
}
```

**Register Plugin**:
```
META-INF/services/com.b2international.snowowl.core.setup.Plugin
com.example.snowowl.myplugin.MyPlugin
```

### Adding a Custom REST Endpoint

```java
@RestController
@RequestMapping("/my-api")
public class MyRestService extends AbstractRestService {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from custom endpoint!");
    }

    @PostMapping("/concepts")
    public Promise<MyResult> customOperation(@RequestBody MyRequest request) {
        return MyRequests.prepareCustomOperation()
            .setParameter(request.getParameter())
            .build()
            .execute(getBus());
    }
}
```

### Creating Custom Requests

```java
// Request implementation
public class MyCustomRequest implements Request<BranchContext, MyResult> {

    private final String parameter;

    public MyCustomRequest(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public MyResult execute(BranchContext context) {
        // Access services
        RevisionIndex index = context.service(RevisionIndex.class);

        // Perform operation
        return index.read(context.branchPath(), searcher -> {
            // Search, process, return result
            return new MyResult(...);
        });
    }

    @Override
    public Class<MyResult> getReturnType() {
        return MyResult.class;
    }
}

// Request builder
public class MyCustomRequestBuilder
    extends RequestBuilder<BranchContext, MyResult> {

    private String parameter;

    public MyCustomRequestBuilder setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    @Override
    protected Request<BranchContext, MyResult> doBuild() {
        return new MyCustomRequest(parameter);
    }
}

// Request factory
public class MyRequests {
    public static MyCustomRequestBuilder prepareCustomOperation() {
        return new MyCustomRequestBuilder();
    }
}
```

### Testing

#### Unit Tests

```java
@Test
public void testConceptSearch() {
    // Given
    SnomedConceptDocument concept = SnomedConceptDocument.builder()
        .id("123456789")
        .active(true)
        .fsn("Test concept")
        .build();

    indexDocument(concept);

    // When
    Hits<SnomedConceptDocument> results = search(
        Query.select(SnomedConceptDocument.class)
            .where(match("fsn", "test"))
            .build()
    );

    // Then
    assertEquals(1, results.getTotal());
    assertEquals("123456789", results.first().getId());
}
```

#### Integration Tests

```java
@RunWith(SpringRunner.class)
@BootTest(classes = SnowOwlApplication.class)
public class ConceptRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateConcept() throws Exception {
        mockMvc.perform(post("/snomed/concepts")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"fsn\":\"Test\"}"))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }
}
```

### Performance Tuning

#### Elasticsearch Configuration

```yaml
# snowowl.yml
repository:
  index:
    numberOfShards: 5
    numberOfReplicas: 1
    commitInterval: 15000
    translogSyncInterval: 5000
```

#### Bulk Import Optimization

```java
// Use bulk operations
BulkIndexWrite bulk = new BulkIndexWrite(SnomedConceptDocument.class);
concepts.forEach(concept -> bulk.add(concept));
writer.bulkIndex(bulk);
writer.commit();
```

#### Query Optimization

```java
// Use field aliases to reduce index size
@FieldAlias(name = "longFieldName", alias = "short")

// Use exact matching for keywords
exactMatch("field", value)  // vs match("field", value)

// Limit fields returned
Query.builder()
    .select(SnomedConceptDocument.class, "id", "fsn", "active")
    .build();
```

### Debugging

#### Enable Debug Logging

```xml
<!-- logback.xml -->
<logger name="com.b2international.snowowl" level="DEBUG"/>
<logger name="com.b2international.index" level="TRACE"/>
```

#### View Elasticsearch Queries

```java
// Set breakpoint in EsDocumentSearcher.search()
// Or enable query logging:
<logger name="com.b2international.index.es" level="DEBUG"/>
```

#### Inspect Index Contents

```bash
# Via Elasticsearch API
curl http://localhost:9200/snomedStore/_search?pretty

# Via Snow Owl
curl http://localhost:8080/snowowl/admin/indices
```

---

## Summary

Snow Owl is a sophisticated, production-grade terminology server with:

1. **Layered Architecture**: Clear separation of concerns
2. **Revision Control**: Git-like branching and versioning
3. **Scalability**: Built on Elasticsearch for horizontal scaling
4. **Extensibility**: Plugin architecture for custom terminologies
5. **Interoperability**: FHIR compliance and RESTful APIs
6. **Performance**: Optimized indexing and query execution
7. **Testability**: Request pattern enables isolated testing

**Key Design Principles**:
- **Request Pattern**: All operations as executable commands
- **Context-Based Execution**: Service provider pattern
- **Index Abstraction**: Technology-independent data access
- **Branch Isolation**: Independent work streams
- **Document Versioning**: Complete audit trail

This architecture enables Snow Owl to handle millions of terminology concepts while maintaining data integrity, supporting collaborative authoring, and providing sub-second search performance.
