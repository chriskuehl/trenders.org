dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
    
    // resolve an issue with stale connections and Grails due to automatic
    // connection pooling done by Grails (enabled above)
    // http://stackoverflow.com/questions/2740987/mysql-connection-timeout-issue-grails-application-on-tomcat-using-hibernate-an
    
    // run the evictor every 30 minutes and evict any connections older than 30 minutes.
    minEvictableIdleTimeMillis = 1800000
    timeBetweenEvictionRunsMillis = 1800000
    numTestsPerEvictionRun = 3
    
    // test the connection while its idle, before borrow and return it
    testOnBorrow = true
    testWhileIdle = true
    testOnReturn = true
    validationQuery = "SELECT 1"
}

dataSource_temp { // used for ephemeral storage
    pooled = true
    driverClassName = "org.h2.Driver"
    //driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
    
    dbCreate = "update"
    url = "jdbc:h2:mem:temp"
    //url = "jdbc:hsqldb:mem:devDB"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        // this datasource is just an empty testing DB (using H2)
         dataSource {
            driverClassName = "org.h2.Driver"
            dialect = "org.hibernate.dialect.H2Dialect"
            
            username = "sa"
            password = ""

            dbCreate = "update"
            url = "jdbc:h2:mem:dev"
        }
    }
    production {
        dataSource {
            dbCreate = "update" // probably we should do updates manually instead...
            url = System.env.JDBC_URL
            username = System.env.JDBC_USERNAME
            password = System.env.JDBC_PASSWORD
        }
    }
}
