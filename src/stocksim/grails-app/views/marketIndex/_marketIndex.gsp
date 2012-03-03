<g:set var="directionClass" value="marketDown" /> <%-- assume it's gone down :( --%>

<g:if test="${index?.isUp()}">
        <g:set var="directionClass" value="marketUp" />
</g:if>

<div class="marketIndex ${directionClass}">
        <strong>${index?.title}</strong> ${index?.getCurrentValue()} (<span class="${directionClass}">${index?.getPercentChangeText()}</span>)
</div>