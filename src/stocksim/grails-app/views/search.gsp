<g:set var="resultsPerPage" value="${10}" />
<g:set var="query" value="${params.q}" />
<g:set var="offset" value="${(params.p ? params.p.toInteger() : 0) * resultsPerPage}" />

<!doctype html>
<html>
  <head>
    <r:require modules="page_search" />
    <meta name="layout" content="main" />
    <content tag="noRobots">true</content>
    
    <title>${query ? query + " &ndash; " : ""} Search</title>
  </head>
  <body>
    <div id="page-body" role="main">
      <h1>${query ? "Search Results: $query" : "Search"}</h2>
      
      <g:if test="${query}">
        <search:results query="${query}" offset="${offset}">
          <ul id="searchResults">
            <search:eachResult max="${resultsPerPage}">
              <li>
                <a href="${createLink(mapping: "stock", params: [ticker: search.resultReturn(req: "ticker")])}">
                  <search:result req="name" />
                </a>
              </li>
            </search:eachResult>
          </ul>
        </search:results>
      </g:if>
      
      <g:else>
        <p>Use the search bar located at the top-right of the page to search for companies.</p>
      </g:else>
    </div>
  </body>
</html>