dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
}

dataSource_temp { // used for ephemeral storage
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
    
    dbCreate = "create-drop"
    url = "jdbc:h2:mem:temp"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://wctp.us/stocksim-dev?useUnicode=yes&characterEncoding=UTF-8"
            username = "stocksim-dev"
            password = "6rcCszF9HqyaprKM"
        }
    }
    test {
        dataSource {
            dbCreate = "create"
            url = "jdbc:mysql://wctp.us/stocksim-dev?useUnicode=yes&characterEncoding=UTF-8"
            username = "stocksim-dev"
            password = "6rcCszF9HqyaprKM"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://wctp.us/stocksim?useUnicode=yes&characterEncoding=UTF-8"
            username = "stocksim"
            password = "Fc5mPSnHtBs4mvmQ"
        }
    }
}
