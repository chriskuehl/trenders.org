package stocksim

public class StockCacheFilenameFilter implements FilenameFilter {
    private String startPattern
    
    public StockCacheFilenameFilter(String startPattern) {
        this.startPattern = startPattern
    }
    
    public boolean accept(File dir, String name) {
        name.startsWith(startPattern)
    }
}

