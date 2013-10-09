package org.trenders

class UserAlert {
    def type
    def title
    def message
    
    def add(def flash) {
        if (! flash.alerts) {
            flash.alerts = []
        }
        
        flash.alerts.add(this)
    }
}
