package eu.abdullah.cst3130.utilities;


import org.openqa.selenium.By;

import java.util.List;

/**
 * The Keyboard Util class
 */
public class KeyboardUtil {

    /***Empty constructor for the keyboard colors*/
    public KeyboardUtil() {
    }


    /**
     * This method extracts color from the description
     *
     * @param description The description string to analyze
     * @return The extracted color
     */
    public String getColor(String description) {
        String[] colors = {"Black", "White", "Red", "Pink", "Graphite", "Grey", "Space GREY", "Blue", "Green", "Yellow", "Orange", "Purple", "Silver", "Skyline","Brown"}; // Some Colors to scrape against

        for (String color : colors) {
            if (description.toLowerCase().contains(color.toLowerCase())) {
                return color.toLowerCase();
            }
        }
        return "black";     // Default color if no match is found
    }


    /**
     * This method verifies the end of the keyboard against these values.
     *
     * @param beginning The beginning part of the model name
     * @param model     The model name to analyze
     * @return True if the model contains any of the specified endings, false otherwise
     */
    public boolean getEnding(String beginning, String model) {
        String[] modelEndings = {"PRO", "TKL", "MINI"};

        for (String color : modelEndings) {
            if (model.toLowerCase().contains(color.toLowerCase())) {
                return true;
            }
        }

        return false;     // Default color if no match is found
    }


    public float formatPrice(String price) {
        List<String> priceValues = List.of(price.split(""));
        priceValues = priceValues.subList(1, priceValues.size());
        return Float.parseFloat(String.join("", priceValues));
    }

}
