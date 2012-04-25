package stocksim

class LessonsController {
    def index() {
        render(view: "/lessons/index")
    }
    
    def view() {
        def lesson = params.lesson
        render(view: "/lessons/$lesson") // TODO: fix user-input vulnerability (e.g. inputting slashes or ../)
    }
}
