package stocksim.dev

import stocksim.*
import org.hibernate.metadata.ClassMetadata
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder
import org.codehaus.groovy.grails.commons.ApplicationHolder

import grails.converters.JSON

class ColumnSandboxController {
    def grailsApplication
    def sessionFactory_temp
    
    
    def test() {
        def metadata = sessionFactory_temp.getClassMetadata(Stock)
        def columnMappings = [:]
        
        def propertyNames = metadata.getPropertyNames()
        
        propertyNames.eachWithIndex { propertyName, i ->
            def columnName = metadata.getPropertyColumnNames(i)[0]
            columnMappings[propertyName] = columnName
        }
        
        println columnMappings
        
        render "ok"
    }
}
