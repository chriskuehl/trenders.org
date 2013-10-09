package org.trenders

class ViewProxyController {

    def index() {
        render(view: params["proxyView"])
    }
}
