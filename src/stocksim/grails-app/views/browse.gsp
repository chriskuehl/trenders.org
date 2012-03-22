<g:set var="resultsPerPage" value="${10}" />
<g:set var="query" value="${params.s}" />
<g:set var="page" value="${params.p ? params.p.toInteger() : 0}" />
<g:set var="offset" value="${page * resultsPerPage}" />

<!doctype html>
<html>
  <head>
    <r:require modules="page_search" />
    <meta name="layout" content="main" />
    <content tag="noRobots">true</content>
    
    <g:if test="${query.length() > 0}">
      <content tag="canonical">${createLink(params: [q: query.toLowerCase(), p: page], absolute: true)}</content>
    </g:if>
    
    <title>${query ? query + " &ndash; " : ""} Search</title>
  </head>
  <body>
    <h1>${query ? "Browse by Sector: $query" : "Search"}</h1>

    <g:if test="${query}">
      <search:results sector="${query}" offset="${offset}">
        <g:if test="${search.getNumResults() > 0}">
          <finance:stocks tickers="${search.getResultTickers(max: resultsPerPage)}">
            <p>Displaying results ${offset + 1} to ${Math.min(offset + resultsPerPage, search.getNumResults())} of ${search.getNumResults()}.</p>
            <ul id="searchResults">
              <search:eachResult max="${resultsPerPage}">
                <li>
                  <a href="${createLink(mapping: "stock", params: [ticker: search.resultReturn(req: "ticker")])}">
                    <span class="searchTitle"><search:result req="name" /></span><br />
                    <search:result req="ticker" /> &ndash; <finance:stock ticker="${search.result(req: "ticker").toLowerCase()}" req="value" /> &ndash; <search:result req="sector" />
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
                  <a id="prevPage" href="${createLink(action: "sector", params: [s: query, p: (page - 1)])}">&laquo; Previous Page</a>
                </g:if>

                <g:if test="${hasNext}">
                  <a id="nextPage" href="${createLink(action: "sector", params: [s: query, p: (page + 1)])}">Next Page &raquo;</a>
                </g:if>
              </div>
            </g:if>
          </finance:stocks>
        </g:if>
        <g:else>
          <p>We didn't find any results for your query. Try again?</p>
        </g:else>
      </search:results>
    </g:if>

    <g:else>
      <p>Use the search bar located at the top-right of the page to search for companies.</p>
    </g:else>
  </body>
</html>