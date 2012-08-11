package stocksim.api

import grails.util.BuildSettingsHolder as BSH

class LyricService {
    def cacheService
    def currentSong = "cat-in-the-hat"
    
    def getLine() {
        def lines = cacheService.fetchFromCache("lyrics", currentSong, 0)
        
        if (lines == null) {
            refreshCache(currentSong)
            return getLine()
        }
        
        lines[(int) Math.floor(Math.random() * lines.size())]
    }
    
    def refreshCache(def song) {
        def baseDir = BSH.getSettings().baseDir
        def lyricFile = new File("${baseDir}/grails-app/conf/lyrics/${song}.txt")
        def lines = lyricFile.readLines()
        
        cacheService.storeInCache("lyrics", song, lines)
    }
}
