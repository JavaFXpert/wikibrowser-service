# wikibrowser-service (ConceptMap)
Facility that browses Wikipedia augmented with semantic capabilities

## Enhancements to consider
* Order of checking for cached image:  By ID using Wikidata image (new), then by article name, then Wikipedia API by ID.  Consider running process on startup to cache all image URLs 
  in the Wikidata dump file.  Use https://commons.wikimedia.org/wiki/Special:Redirect/file/Python-Foot.png?width=100px

* Add spu, spf, spr, exp1, exp* buttons to the two top-level relationship headings
  * Perhaps the spu button could return all shortest paths, or just one shortest path that exclude obvious connections.  Here are a couple of queries for reference:
  
    * The returned collection of paths does not contain any path that contains an Item node whose itemId is equal to “Q5”:
      MATCH p=allShortestPaths( (a:Item {itemId:"Q6294"})-[*]-(b:Item {itemId:"Q359442"}) )
      WHERE NONE(x IN NODES(p) WHERE x:Item AND x.itemId = "Q5")
      RETURN p;
      
    * The returned collection of paths does not contain any path that contains a relationship whose propId is equal to “P31”:
      MATCH p=allShortestPaths( (a:Item {itemId:"Q6294"})-[*]-(b:Item {itemId:"Q359442"}) )
      WHERE NONE(x IN RELATIONSHIPS(p) WHERE x.propId = "P31")
      RETURN p;
      
    * The returned collection of paths does not contain any path that contains a relationship whose propId is equal to “P1343” or whose itemId is equal to “Q5”:
      MATCH p=allShortestPaths( (a:Item {itemId:"Q23"})-[*..2]-(b:Item {itemId:"Q9696"}) )
      WHERE NONE(x IN NODES(p) WHERE x:Item AND x.itemId = "Q5") AND NONE(y IN RELATIONSHIPS(p) WHERE y.propId = "P1343")
      RETURN p;
  
* Sort the languages that appear in the language selection slide-out menu
* Include all items (not just ones with English labels) in WikidataNeo4jProcessor, by using getLabels() if findLabel() returns null
  * http://wikidata.github.io/Wikidata-Toolkit/org/wikidata/wdtk/datamodel/interfaces/TermedDocument.html#getLabels()
* Make query limits (# of rows returned in a given query) configurable, or at least a constant that may be changed one place
* Implement in SPARQL a query similar to the Neo4j query that returns results necessary to disply pinned nodes and their relationship.  This could potentially eliminate the need for such a large Neo4j DB.
* Move Wikidata relationships to left side, and concept map to middle?
* Add ?parent to results from traversal sparql query & merge into neo4j?  (or just do a batch load periodically) 
* [] Implement authentication
  * Use GitHub / OAUTH
  * https://spring.io/guides/tutorials/spring-boot-oauth2/
* [] Enable user to save a concept map for later retrieval
  * Relationship between user and the nodes in the map, as well as an indication of main node and language 
* [] Enable user to associate external resources with Q items
* [] Create utility that loads Neo4j QUICKLY from Wikidata dump
* Create option that displays only those relationship types that were used in the latest traversal (or some other way of displaying a single type of relationship)
* Associate image with each Q item
  * Hovering over a graph node displays image for the Q item and a wikipedia title/snippet
  * Use this example for reference: http://bl.ocks.org/eesur/be2abfb3155a38be4de4
* Put level of detail in properties that enables traversing lists such as US Presidents
* Show tabs for Wikibooks, Wikinews, Wikiquote, Wikisource, Wikivoyage, commons, and Wikiversity when present
* Implement visjs library for directed graph functionality
  * Make the concept map not jump around so much when drawing
* Create Getting Started guide for ConceptMap in Slides
* Put Wikipedia header on right side of app
* Highlight node that is represented by selected Wikipedia article
* Consider showing all relationships for a node when double-clicking it:
  (perhaps showing nodes as dashed line borders that may be pinned/unpinned)
* On a node that represents a person, if there are family relationships then show options to traverse br CHILD relationship (forward, reverse, forward all levels, reverse all levels) 
  * Investigate other such relationship types (e.g. parent taxon, subclass of, child, followed by)   
  * Related task: Consider inferring Neo4j labels from properties (e.g. instance of human is a person)
* Compute and display nodes for shortest path between two selected nodes. Related ideas:
  * Allow specification of relationship types (e.g. CAST_MEMBER, SHARES_BORDER_WITH, MEMBER_OF)
* Investigate techniques like AngryLoki uses in https://github.com/AngryLoki/wikidata-graph-builder like https://wiki.blazegraph.com/wiki/index.php/RDF_GAS_API
* Implement Item and Property autocompletion using Select2 (see https://github.com/AngryLoki/wikidata-graph-builder and https://select2.github.io/)
* Explore idea of associating tags from external systems (e.g. StackOverflow) to Q items, and inferring relationships among them
* Create UI for editing Wikidata items
* Enable user to change panel widths
* Enable full screen concept map, perhaps automatically so on small mobile devices
* Export concept map to PDF
* Implement caching of /claims results
* Import Wikidata data into Neo4j
* Implement erase map button
* Provide ability to associate links to external articles
* Make concept map resize when browser window is resized (without requiring reloading)
* Create embeddable mini concept map viewer
* Create image/snippet component that steps though ordered list of Q items
* In the /claims and /relatedclaims (and /claimsxml) endpoints, consider returning Q-numbers when there isn't an article in the requested language.

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

* Implement patterns like bulkhead and circuit-breaker so that external services like Neo4j, wikimedia REST call, etc. don't cause:
  * services to hang
  * service to require restart
  * give bad messages?

* Implement blue/green deploy for zero downtime


## Items to Fix

* [] Create unit tests
* [] Fix issues that occur when an article title contains an ampersand (e.g. not finding article, and thumbnail lookup problems)
* [] Handle issues like when SPARQL query failed more gracefully
* If relationship is removed from Wikidata, delete it from Neo4j at the same point at which relationships are added (MERGE)
* [] Add a language property to Item nodes stored in Neo4j that aren't currently in English, and use that property to invoke
     the /thumbnail endpoint from WikiVisGraphController.  Perhaps, in addition or instead of, use the item ID to get thumbnails (currently slower though)
* Create process that generates MERGE code for missing properties in ItemRepository.java 
* [] Make the graph not so jumpy, and consider implementing sigmajs or visjs
* WikiSearchController search() method probably needs to do a query to get the article URL for the article title (as it currently returns titles with spaces)
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
* Remove D3js related code

## I18N considerations:
* Make WikiPageController#generateWikiBrowserPage() write the lang in generated links e.g. href=wikipage?name=Terre&lang=fr

## Miscellaneous notes:
* For Neo4j performance, CREATE INDEX ON :Item(itemId)
* To remove all outgoing relationships from a node, MATCH (a:Item {itemId: "Q43274"})-[r]->(b) DELETE r
