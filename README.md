# Hitorro Base UI

A web UI framework built on Apache Tapestry 5.4.x with components for data visualization, natural language processing interfaces, and RDF/SPARQL integration.

## Description

This module provides web UI components and pages built with Apache Tapestry, including:

- **UI Components**: Reusable Tapestry components for data visualization
- **Mixins**: CKEditor, Drag & Drop functionality
- **Tree Adapters**: Category trees, file browsers, OpenNLP chunker visualization
- **Sample Pages**: Test pages demonstrating various UI capabilities
- **Integration**: Apache Jena (RDF/SPARQL), Apache OpenNLP

## Technology Stack

- **Framework**: Apache Tapestry 5.4.5
- **Java**: 19
- **RDF/SPARQL**: Apache Jena 3.0.1
- **NLP**: Apache OpenNLP 1.9.4
- **jQuery**: tapestry5-jquery 4.0.0

## Building

This is a standalone Maven project that builds as a WAR file for deployment to a servlet container.

```bash
# Compile
mvn clean compile

# Package as WAR
mvn package

# Run with Jetty (development)
mvn jetty:run
```

Then access the application at: `http://localhost:8080`

## Requirements

- Java 19 or higher
- Maven 3.6+
- Servlet 3.1+ compatible container (Tomcat 8+, Jetty 9+)

## Project Structure

```
hitorro-baseui/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/hitorro/
        │       ├── baseui/
        │       │   ├── components/      (Tapestry components)
        │       │   ├── mixins/          (Tapestry mixins)
        │       │   ├── pages/           (Tapestry pages)
        │       │   ├── services/        (IoC services)
        │       │   ├── context/         (Application context)
        │       │   └── treeadapters/    (Tree view adapters)
        │       ├── sampleapp/           (Sample application)
        │       └── ui/
        │           ├── tapestry/        (Tapestry utilities)
        │           └── *.java           (Servlets)
        └── resources/
            ├── com/hitorro/baseui/      (Component templates, CSS, JS)
            └── META-INF/                (Configuration)
```

## Key Features

### Components

- **D3NodeLinkTree**: D3.js-based tree visualization
- **GMeter**: Gauge meter component
- **Carousel**: Image carousel (jQuery plugin)
- **Layout**: Page layout components
- **Sparkline**: Mini charts for data visualization
- **Brat**: Biological entity annotation visualization

### Mixins

- **CKEditor**: Rich text editor integration
- **DragMixin/DropMixin**: Drag and drop functionality

### Tree Adapters

- **CategoryAdapter**: Category tree navigation
- **FileAdapter**: File system browsing
- **OpenNLPChunkerAdapter**: NLP parse tree visualization

## Important Notes

⚠️ **Dependencies**: This module originally depended on internal Hitorro modules:
- `hitorro-analysis`
- `hitorro-basedms`
- `hitorro-base`
- `hitorro-text-core`

To use as a standalone project, you'll need to either:
1. Install those modules to your local Maven repository first, OR
2. Remove/comment out code that depends on them

## Development

Run locally with Jetty:

```bash
mvn jetty:run
```

The application will be available at `http://localhost:8080`

To stop: `Ctrl+C` or run `mvn jetty:stop`

## Deployment

Build the WAR file:

```bash
mvn clean package
```

Deploy `target/hitorro-baseui-3.0.0.war` to your servlet container (Tomcat, Jetty, etc.)

## License

[Add appropriate license information]
