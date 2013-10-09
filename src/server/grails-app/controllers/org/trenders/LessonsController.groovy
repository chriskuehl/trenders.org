package org.trenders

class LessonsController {
    def index() {
        render(view: "/lessons/index")
    }
    
    def view() {
        def cat = params.cat
        def lesson = params.lesson
        
        render(view: "/lessons/$cat/$lesson") // TODO: fix user-input vulnerability (e.g. inputting slashes or ../)
    }
}
