package stocksim

class LessonService {
    def grailsApplication
    def wikipediaService
    
    def getLessonCategories() {
        [
            generalFinance: [
                title: "General Personal Finance",
                fileID: "general",
                
                lessons: [
                    whatIsInvesting: [
                        title: "What is Investing?",
                        fileID: "what-is-investing"
                    ],
                    
                    savingMoney: [
                        title: "The Importance of Saving",
                        fileID: "saving-money"
                    ],
                    
                    otherOptions: [
                        title: "Your Other Options",
                        fileID: "other-options"
                    ]
                ]
            ],
            
            stockMarket: [
                title: "The Stock Market",
                fileID: "stocks",
                
                lessons: [
                    stockMarketIntro: [
                        title: "Introduction to the Stock Market",
                        fileID: "stock-market-intro"
                    ],
                    
                    choosingStocks: [
                        title: "Choosing the Right Stocks",
                        fileID: "choosing-stocks"
                    ]
                ]
            ]
        ]
    }
    
    def getLessonMeta(def requestedLessonID) {
        def meta = null
        def categories = getLessonCategories()
        
        categories.each { categoryID, category ->
            category.lessons.each { lessonID, lesson ->
                if (lessonID == requestedLessonID) {
                    meta = [
                        title: lesson.title,
                        categoryFileID: category.fileID,
                        lessonFileID: lesson.fileID
                    ]
                }
            }
        }
        
        meta
    }
    
    def getLessonContent(def lessonID) {
        def rootResourcePath = grailsApplication.parentContext.getResource("/").file.getAbsolutePath()
        def rootPath = rootResourcePath.substring(0, rootResourcePath.lastIndexOf("/")) + "/grails-app/"
        
        def lessonMeta = getLessonMeta(lessonID)
        
        if (lessonMeta == null) {
            return null
        }
        
        def lessonPath = rootPath + "/views/lessons/${lessonMeta.categoryFileID}/${lessonMeta.lessonFileID}.gsp"
        def lessonFile = new File(lessonPath)
        
        def content = lessonFile.text // get content from GSP file
        
        content = wikipediaService.between(content, "</h1>", "<g:link mapping=\"lessons\">") // cut down to the actual content
        content = (content =~ /<g:img(.*?) \/>/).replaceAll("") // remove images
        content = content.trim()
        
        return [
            title: lessonMeta.title,
            body: content
        ]
    }
}