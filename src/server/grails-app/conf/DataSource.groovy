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
    
    dbCreate = "create-drop"
    url = "jdbc:h2:mem:temp"
    //url = "jdbc:hsqldb:mem:devDB"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
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
        /*
        // this datasource is for testing with a backup of the
        // prod data on the prod MySQL server (but on a separate DB)
        dataSource {
            dbCreate = "update" //create-drop"
            url = "jdbc:mysql://trenders.org/stocksim_test-2012-08-25?useUnicode=yes&characterEncoding=UTF-8"
            username = "stocksim-dev"
            password = "6rcCszF9HqyaprKM"
        }*/
    }
    test {
        dataSource {
            dbCreate = "create"
            url = "jdbc:mysql://trenders.org/stocksim-dev?useUnicode=yes&characterEncoding=UTF-8"
            username = "stocksim-dev"
            password = "6rcCszF9HqyaprKM"
        }
    }
    production {
        dataSource {
            // we are at the stage where production updates should be made
            // using migration scripts rather than letting Hibernate attempt
            // to work magic (which fails too often to be useful with valuable
            // data, i.e. student data)
            // 
            // be sure to backup data first, though, as the daily server backups
            // are taken late at night and you're probably going to lose the
            // day's data otherwise
            dbCreate = "update" //create-drop"
            url = "jdbc:mysql://trenders.org/stocksim?useUnicode=yes&characterEncoding=UTF-8"
            username = "stocksim"
            password = "Fc5mPSnHtBs4mvmQ"
        }
    }
}
