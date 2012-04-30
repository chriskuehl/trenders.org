package filters

class CacheFilters {
    def filters = {
        noCachingFilter(uri: "/**") {
            before = {
                // some browsers are eager to cache pages (especially on
                // the iPad and some other mobile devices we've tested)
                // so we do this to specifically tell them not to in order to
                // prevent weird results (e.g. signing up and still appearing
                // to not be logged in)
                response.setHeader("Expires", "-1")
            }
        }
    }
}

