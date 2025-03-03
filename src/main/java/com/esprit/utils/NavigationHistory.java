package com.esprit.utils;

import java.util.Stack;

public class NavigationHistory {
    private static final Stack<String> history = new Stack<>();
    
    public static void pushPage(String fxmlPath) {
        history.push(fxmlPath);
    }
    
    public static String popPage() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return "/loginn.fxml"; // Page par défaut
    }
    
    public static void clearHistory() {
        history.clear();
    }
} 