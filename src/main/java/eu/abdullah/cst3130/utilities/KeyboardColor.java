package eu.abdullah.cst3130.utilities;

public class KeyboardColor {

    /***Empty constructor for the keyboard colors*/
    public KeyboardColor() {
    }

    public String getColor(String description) {
        String[] colors = {"Black", "White", "Red", "Pink", "Graphite", "Grey", "Space GREY", "Blue", "Green", "Yellow", "Orange", "Purple", "Silver"}; // Add more colors as needed

        for (String color : colors) {
            if (description.toLowerCase().contains(color.toLowerCase())) {
                return color.toLowerCase();
            }
        }

            return "black";


    }

}
