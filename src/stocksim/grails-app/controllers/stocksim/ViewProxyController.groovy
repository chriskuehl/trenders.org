package stocksim

class ViewProxyController {

    def index() {
        render(view: params["proxyView"])
    }
}
