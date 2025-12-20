# Dependencies Guide

## Internal Hitorro Dependencies

This module originally depended on the following internal Hitorro modules. These dependencies have been **commented out** in the POM to allow the project to build standalone, but you'll need to resolve them for full functionality.

### Required Modules

1. **hitorro-analysis** (3.0.0)
   - Provides: Link analysis, conversation tracking, NLP analysis tools
   - Used by: Analysis-related UI components

2. **hitorro-basedms** (3.0.0)
   - Provides: Document management system, content types, versioning
   - Used by: Content management UI, document choosers

3. **hitorro-base** (3.0.0)
   - Provides: Core base classes, utilities
   - Used by: Various UI components
   - **Note**: This module may have been split/refactored

4. **hitorro-text-core** (3.0.0)
   - Provides: Text processing, indexing, search functionality
   - Used by: Search UI, text analysis components

## Options for Resolving Dependencies

### Option 1: Install Dependencies Locally

If you have access to the hitorro modules, install them to your local Maven repository:

```bash
cd /path/to/hitorro/hitorro-parent
mvn clean install -pl hitorro-analysis,hitorro-basedms,hitorro-base,hitorro-text-core
```

Then uncomment the dependencies in `pom.xml`:

```xml
<dependency>
    <groupId>com.hitorro</groupId>
    <artifactId>hitorro-analysis</artifactId>
    <version>3.0.0</version>
</dependency>
<!-- ... etc -->
```

### Option 2: Remove Dependent Code

Remove or stub out the code that depends on these modules:

**Files that reference hitorro-analysis:**
- (Add specific files if known)

**Files that reference hitorro-basedms:**
- `com.hitorro.baseui.pages.test.DocChooser`
- `com.hitorro.baseui.pages.test.Main`
- `com.hitorro.baseui.components.FileAdapter`

**Files that reference hitorro-base:**
- `com.hitorro.sampleapp.*`
- Various UI components

### Option 3: Create Stub Implementations

Create minimal stub/mock implementations of the required classes:

```java
package com.hitorro.base.objects;
public class ContentType {
    // Minimal stub implementation
}
```

## Current Status

The POM is configured to **build without these dependencies** by commenting them out. This allows:
- ✅ Project structure to be validated
- ✅ Independent Tapestry components to compile
- ❌ Full application functionality (requires dependencies)

## Next Steps

1. Decide which option above to use
2. If using Option 1: Install dependencies and uncomment in POM
3. If using Option 2: Identify and remove/replace dependent code
4. If using Option 3: Create stub implementations
5. Run `mvn clean compile` to verify
