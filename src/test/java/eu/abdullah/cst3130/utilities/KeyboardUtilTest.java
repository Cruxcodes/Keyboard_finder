package eu.abdullah.cst3130.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardUtilTest {

    @Test
    public void getColor_WhenDescriptionContainsColor_ShouldReturnColor() {
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        String description = "This is a Black keyboard with LED backlighting.";
        String extractedColor = keyboardUtil.getColor(description);
        assertEquals("black", extractedColor);
    }

    @Test
    public void getColor_WhenDescriptionDoesNotContainColor_ShouldReturnDefaultColor() {
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        String description = "This is a keyboard without any specific color mentioned.";
        String extractedColor = keyboardUtil.getColor(description);
        assertEquals("black", extractedColor);
    }

    @Test
    public void getEnding_WhenModelContainsEnding_ShouldReturnTrue() {
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        String beginning = "Logitech G";
        String model = "G Pro Mechanical Keyboard";
        boolean containsEnding = keyboardUtil.getEnding(beginning, model);
        assertTrue(containsEnding);
    }

    @Test
    public void getEnding_WhenModelDoesNotContainEnding_ShouldReturnFalse() {
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        String beginning = "Corsair K";
        String model = "K95 RGB Platinum XT Keyboard";
        boolean containsEnding = keyboardUtil.getEnding(beginning, model);
        assertFalse(containsEnding);
    }
}