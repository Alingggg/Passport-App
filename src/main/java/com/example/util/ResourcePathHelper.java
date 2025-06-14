package com.example.util;

import com.example.Main;
import java.net.URL;

public class ResourcePathHelper {

    /**
     * Gets the correct URL for a resource given its path
     */
    public static URL getResourceURL(String path) {
        return Main.class.getResource(path);
    }
    
    /**
     * Gets the correct path for an FXML file based on its name
     */
    public static String getFXMLPath(String name) {
        if (name.contains("/")) {
            return "fxml/" + name + ".fxml";
        } else if (name.startsWith("User")) {
            return "fxml/User/" + name + ".fxml";
        } else if (name.startsWith("Admin")) {
            return "fxml/Admin/" + name + ".fxml";
        } else {
            return "fxml/" + name + ".fxml";
        }
    }
}
