package stocksim

class CacheService {
    def cache = [:]
    
    def initService(def service) {
        if (! cache.containsKey(service)) {
            cache[service] = [:]
        }
    }
    
    // normally just call fetchFromCache and check if it's null
    def hasExpired(def service, def key, def minutes) {
        initService(service)
        
        if (! cache[service].containsKey(key)) {
            return true
        }
        
        ((new Date().getTime() - cache[service][key]["date"].getTime()) / (1000 * 60)) > minutes
    }
    
    def storeInCache(def service, def key, def value) {
        initService(service)
        cache[service][key] = [value: value, date: new Date()]
    }
    
    def fetchFromCache(def service, def key, def minutes) {
        initService(service)
            
        if (minutes <= 0 || ! hasExpired(service, key, minutes)) {
            return cache[service][key]["value"]
        }
        
        cache[service].remove(key) // it's expired, let's save some memory
        null
    }
}
