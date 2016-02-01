# wikibrowser-service (ConceptMap)
Facility that browses Wikipedia augmented with semantic capabilities

## Enhancements to consider
* [] Implement authentication
* [] Enable user to save a concept map for later retrieval
* [] Enable user to associate external resources with Q items
* Implement visjs library for directed graph functionality
  * Make the concept map not jump around so much when drawing
* Create Getting Started guide for ConceptMap in Slides
* Put Wikipedia header on right side of app
* Highlight node that is represented by selected Wikipedia article
* Consider showing all relationships for a node when double-clicking it:
  (perhaps showing nodes as dashed line borders that may be pinned/unpinned)
* Compute and display nodes for shortest path between two selected nodes
* Create UI for editing Wikidata items
* Enable user to change panel widths
* Enable full screen concept map
* Export concept map to PDF
* Implement caching of /claims results
* Import Wikidata data into Neo4j
* Implement erase map button
* Provide ability to associate links to external articles
* Consider inferring Neo4j labels from properties (e.g. instance of human is a person)
* Make concept map resize when browser window is resized (without requiring reloading)
* Performance suggestion from Brian Underwood Neo4j advocate: Consider querying Neo4j by labels and have an index on Label(itemId)
* Associate image with each Q item
* Create embeddable mini concept map viewer
* Create image/snippet component that steps though ordered list of Q items

## [] Cloud-native related features to consider implementing
* Implement configuration server (override configurable items such as those in application.properties, and make starting Q item a configurable item in application.properties)

* Break up the one microservice into several, each implementing one or more of the following endpoints?
  1. /articlesearch, /idlocator, /langlinks (calls https://xx.wikipedia.org/w/api.php?action=query&...)
  2. /claims, /claimsxml, /relatedclaims (calls https://query.wikidata.org/bigdata/namespace/wdq/sparql?... and populates Neo4j database using Spring Data Neo4j)
  3. /locator (calls https://www.wikidata.org/w/api.php?action=wbgetentities&...)
  4. /wikipage (retrieves mobile wikipedia page https://xx.m.wikipedia.org/...)
  5. /bitly (calls https://api-ssl.bitly.com/v3/shorten?...)  
  6. /graph (calls hosted Neo4j Cypher transactional endpoint)

* Implement an API gateway?
  (perhaps not necessary or even advisable if endpoint aren't broken up into several microservices)?
  
* Implement service discovery?

* Implement bulkhead / circuit-breaker patterns?

* Implement blue/green deploy for zero downtime


## Items to Fix

* [] Create unit tests
* [] Handle issues like when SPARQL query failed more gracefully
* [] Make the graph not so jumpy, and consider implementing sigmajs or visjs
* WikiSearchController search() method probably needs to do a query to get the article URL for the article title (as it currently returns titles with spaces)
* If relationship is removed from Wikidata, delete it from Neo4j at the same point at which relationships are added (MERGE)
* Make iFrame height resize when new web page loads
* Draw a line for each relationship in the same direction between two nodes
* Diagnose issue on Safari/Mac where bitly link isn't created when app started with conceptmap.cfapps.io but it did when started with conceptmap.io
* Handle multiple languages in Neo4j database
* handle case when typing fast (use timer?)
* Consider comparing nearmatch with similar entry in dropdown, and use the latter for the search
* Create unit tests
* Make it so that hitting Enter key doesn't trigger search when Search button isn't enabled?
* Render TOC entries in mobile Wikipedia
* Watch for duplicated language code (e.g. frfr) in console.
* Check out following warnings:
02:25:53.431 [main] WARN  o.s.d.n.m.Neo4jPersistentProperty - No identity field found for class of type: com.javafxpert.wikibrowser.model.conceptmap.GraphRelationFar when creating persistent property for field: private java.lang.String com.javafxpert.wikibrowser.model.conceptmap.GraphRelationFar.startNode

## Code refactoring
* Organize/eliminate duplication in classes that hold deserialized objects
  * Start with renaming idlocator package to wpquery and IdLocatorResponse to WpQueryResponse
  * Standardize on Near/Far naming?
  * Add @JsonProperty("foo") to all JSON deserialize classes, adjusting singular/plural as makes sense
* Change IdLocator endpoint parameter from name to title
* Move WIKIDATA_ITEM_BASE, etc. to WikiBrowserProperties class

## I18N considerations:
* Make WikiPageController#generateWikiBrowserPage() write the lang in generated links e.g. href=wikipage?name=Terre&lang=fr

