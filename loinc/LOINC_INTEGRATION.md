# LOINC Integration with Snow Owl

This document describes how the LOINC implementation integrates with Snow Owl and how to use the API to create and manage LOINC CodeSystems.

## Integration Architecture

The LOINC implementation follows Snow Owl's modular architecture pattern, similar to SNOMED CT:

### OSGi Bundles

The LOINC implementation consists of four Eclipse OSGi bundles:

1. **com.b2international.snowowl.loinc.common** - Common constants and utilities
2. **com.b2international.snowowl.loinc.datastore** - Core domain, indexing, and request handling
3. **com.b2international.snowowl.loinc.core.rest** - REST API endpoints (fragment attached to core.rest)
4. **com.b2international.snowowl.loinc.datastore.tests** - Unit tests

Each bundle has a `META-INF/MANIFEST.MF` file that defines its dependencies and exports.

### Auto-Integration

**Yes, the LOINC implementation auto-integrates with the core system out of the box!**

The integration happens automatically through:

1. **LoincPlugin Class** (`com.b2international.snowowl.loinc.core.LoincPlugin`)
   - Annotated with `@Component` for automatic discovery
   - Extends `TerminologyRepositoryPlugin`
   - Defines tooling ID: `"loinc"`
   - Registers LOINC terminology components (LoincCode, LoincPart)
   - Configures repository services and converters

2. **Terminology Registry**
   - On startup, Snow Owl's TerminologyRegistry automatically discovers all `@Component` annotated plugins
   - LoincPlugin is registered as a terminology with toolingId="loinc"
   - A repository is created for LOINC with all necessary services

3. **REST API Endpoints**
   - Fragment bundle `loinc.core.rest` attaches to `core.rest`
   - REST endpoints are automatically available at `/{path}/loinc/codes` and `/{path}/loinc/parts`

## Creating a LOINC CodeSystem via API

### Step 1: Understanding CodeSystem Creation

A CodeSystem in Snow Owl is a terminology resource that represents a complete code system instance. To create a LOINC CodeSystem, you need to:

1. Use the CodeSystem API
2. Specify the `toolingId` as `"loinc"`
3. Provide metadata (id, url, title, etc.)

### Step 2: REST API Endpoint

**Endpoint**: `POST /codesystems`

**Required Fields**:
- `id` - Unique identifier for the CodeSystem (e.g., "loinc-2.78")
- `url` - Canonical URL (e.g., "http://loinc.org")
- `title` - Display title (e.g., "LOINC 2.78")
- `toolingId` - Must be **"loinc"**

**Optional Fields**:
- `description` - Textual description
- `oid` - Object Identifier (OID) if required
- `branchPath` - Custom branch path (auto-created if not specified)
- `dependencies` - Array of resource dependencies
- `settings` - Custom configuration settings

### Step 3: Example API Request

```bash
curl -X POST "http://localhost:8080/codesystems" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "id": "loinc",
    "url": "http://loinc.org",
    "title": "LOINC",
    "toolingId": "loinc",
    "description": "Logical Observation Identifiers Names and Codes",
    "oid": "2.16.840.1.113883.6.1",
    "language": "en",
    "status": "active",
    "copyright": "This material contains content from LOINC (http://loinc.org). LOINC is copyright © 1995-2024, Regenstrief Institute, Inc. and the Logical Observation Identifiers Names and Codes (LOINC) Committee and is available at no cost under the license at http://loinc.org/license."
  }'
```

### Step 4: Java API Example

```java
import com.b2international.snowowl.core.ServiceProvider;
import com.b2international.snowowl.core.codesystem.CodeSystemRequests;
import com.b2international.snowowl.core.events.util.Promise;

public class LoincCodeSystemExample {

    public void createLoincCodeSystem(ServiceProvider context) {
        String codeSystemId = CodeSystemRequests.prepareNewCodeSystem()
            .setId("loinc")
            .setUrl("http://loinc.org")
            .setTitle("LOINC")
            .setToolingId("loinc")  // CRITICAL: Must be "loinc"
            .setDescription("Logical Observation Identifiers Names and Codes")
            .setOid("2.16.840.1.113883.6.1")
            .setLanguage("en")
            .setStatus("active")
            .setCopyright("This material contains content from LOINC...")
            .build()
            .execute(context)
            .getSync();

        System.out.println("Created LOINC CodeSystem: " + codeSystemId);
    }
}
```

### Step 5: Importing LOINC Data

After creating the CodeSystem, import LOINC distribution files:

```java
import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.loinc.datastore.importer.LoincImportService;
import java.io.FileInputStream;

public class LoincImportExample {

    public void importLoincData(TransactionContext context) throws Exception {
        LoincImportService importService = new LoincImportService();

        // Import LOINC codes from Loinc.csv
        try (FileInputStream loincCodes = new FileInputStream("Loinc.csv")) {
            int codeCount = importService.importLoincCodes(context, loincCodes);
            System.out.println("Imported " + codeCount + " LOINC codes");
        }

        // Import LOINC parts from LoincPart.csv
        try (FileInputStream loincParts = new FileInputStream("LoincPart.csv")) {
            int partCount = importService.importLoincParts(context, loincParts);
            System.out.println("Imported " + partCount + " LOINC parts");
        }
    }
}
```

## Available REST API Endpoints

Once the CodeSystem is created, the following endpoints become available:

### LOINC Code Search

```bash
# GET: Search LOINC codes
GET /{path}/loinc/codes?loincNum=10334-4&active=true

# POST: Advanced search
POST /{path}/loinc/codes/search
{
  "loincNum": ["10334-4", "10335-1"],
  "component": "Glucose",
  "scaleTyp": "Qn",
  "active": true,
  "limit": 50
}
```

### LOINC Part Search

```bash
# GET: Search LOINC parts
GET /{path}/loinc/parts?partTypeName=COMPONENT&active=true

# POST: Advanced part search
POST /{path}/loinc/parts/search
{
  "partTypeName": ["COMPONENT", "PROPERTY"],
  "status": "ACTIVE",
  "limit": 100
}
```

## FHIR CodeSystem Support

The implementation includes FHIR R4 CodeSystem support via `LoincCodeSystemProvider`:

```java
import com.b2international.snowowl.loinc.datastore.fhir.LoincCodeSystemProvider;

public class FhirExample {
    public void getFhirCodeSystem() {
        LoincCodeSystemProvider provider = new LoincCodeSystemProvider();

        // FHIR CodeSystem URI
        String systemUri = LoincCodeSystemProvider.LOINC_URI; // "http://loinc.org"

        // FHIR CodeSystem name
        String systemName = LoincCodeSystemProvider.LOINC_NAME; // "LOINC"

        // Copyright statement
        String copyright = LoincCodeSystemProvider.LOINC_COPYRIGHT;
    }
}
```

## Terminology Components

The LOINC implementation registers these terminology components:

1. **LoincCode** (ComponentCategory.CONCEPT)
   - Represents individual LOINC codes
   - Includes 6-axis model: Component, Property, Time, System, Scale, Method

2. **LoincPart** (ComponentCategory.CONCEPT)
   - Represents reusable LOINC parts
   - Used to build LOINC codes

## Key Features

✅ **Auto-Integration**: Automatically discovered and registered via `@Component`
✅ **REST API**: Automatically available at `/loinc/codes` and `/loinc/parts`
✅ **Search**: Full-text search with Elasticsearch
✅ **CRUD Operations**: Create, Read, Update, Delete support
✅ **Import**: TSV file import from LOINC distribution
✅ **FHIR**: R4 CodeSystem resource support
✅ **Advanced Queries**: Multi-axis relationship queries

## Important Notes

1. **ToolingId is "loinc"**: Always use `toolingId: "loinc"` when creating LOINC CodeSystems
2. **Official LOINC URI**: Use `http://loinc.org` as the canonical URL
3. **License Requirements**: LOINC is copyrighted by Regenstrief Institute - include proper copyright notice
4. **OID**: The official LOINC OID is `2.16.840.1.113883.6.1`
5. **Version-based**: LOINC uses version releases (e.g., "2.78") rather than effective time

## Troubleshooting

### CodeSystem Creation Fails

**Error**: `ToolingId 'loinc' is not supported by this server`

**Solution**: Ensure the `loinc.datastore` bundle is deployed and LoincPlugin is loaded

### REST Endpoints Not Available

**Error**: 404 on `/loinc/codes`

**Solution**:
1. Verify `loinc.core.rest` fragment bundle is deployed
2. Check that a LOINC CodeSystem exists with `toolingId="loinc"`
3. Restart the server to reload fragments

### Import Fails

**Error**: Issues during TSV import

**Solution**:
1. Ensure TSV files are UTF-8 encoded
2. Verify column headers match LoincHeaders constants
3. Check that a valid branch/path is specified
