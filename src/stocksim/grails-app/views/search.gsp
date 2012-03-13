<g:set var="resultsPerPage" value="${10}" />
<g:set var="query" value="${params.q}" />
<g:set var="page" value="${params.p ? params.p.toInteger() : 0}" />
<g:set var="offset" value="${page * resultsPerPage}" />

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
      <h1>${query ? "Search Results: $query" : "Search"}</h1>
      
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
          
          <%-- previous and next page --%>
          <g:set var="hasPrev" value="${offset > 0}" />
          <g:set var="hasNext" value="${search.getNumResults() > offset + resultsPerPage}" />
          
          <g:if test="${hasPrev || hasNext}">
            <div id="pageNavHolder">
              <g:if test="${hasPrev}">
                <a id="prevPage" href="${createLink(params: [q: query, p: (page - 1)])}">&laquo; Previous Page</a>
              </g:if>
              
              <g:if test="${hasNext}">
                <a id="nextPage" href="${createLink(params: [q: query, p: (page + 1)])}">Next Page &raquo;</a>
              </g:if>
            </div>
          </g:if>
        </search:results>
      </g:if>
      
      <g:else>
        <p>Use the search bar located at the top-right of the page to search for companies.</p>
      </g:else>
    </div>
  </body>
</html>