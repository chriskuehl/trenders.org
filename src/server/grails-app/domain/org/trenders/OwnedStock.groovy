package org.trenders

class OwnedStock {
    static belongsTo = [user: User]
    
    String ticker
    int quantity = 0
    double totalSpent = 0
}
