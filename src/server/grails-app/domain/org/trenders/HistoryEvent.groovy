package org.trenders

class HistoryEvent {
    static belongsTo = [user: User]
    
    Date date
    String ticker
    boolean wasPurchase
    int quantity
    double money
}
