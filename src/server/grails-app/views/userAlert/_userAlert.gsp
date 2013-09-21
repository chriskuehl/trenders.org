<%-- TODO: add different types of alert boxes --%>
<g:if test="${alert != null && alert.type != null}">
	<div class="alert alert${alert?.type[0].toUpperCase() + alert?.type[1..-1]}">
        <g:img dir="images" file="alert-${alert?.type}.png" alt="Warning" />
        <strong>${alert?.title}</strong> ${alert?.message}
	</div>
</g:if>
