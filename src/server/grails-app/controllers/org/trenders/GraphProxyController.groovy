package org.trenders

import java.net.URLEncoder

class GraphProxyController {
	def graph() {
		def p = [
			duration: params.duration,
			width: params.width,
			height: params.height,
			symbol: params.symbol,
			headertype: params.headertype
		]

		def url = "http://charts.reuters.com/reuters/enhancements/chartapi/chart_api.asp"
		url += "?"
		url += p.collect { k, v ->
			encode(k) + "=" + encode(v)
		}.join("&")	

		println url

		response.contentType = "image/png"
		response.outputStream << url.toURL().openStream()
		response.outputStream.flush()
	}

	private def encode(str) {
		return URLEncoder.encode(str)
	}
}
