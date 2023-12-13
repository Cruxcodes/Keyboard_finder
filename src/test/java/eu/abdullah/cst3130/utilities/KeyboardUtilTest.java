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
    
    @Test
    public void testFormatPrice_validInput() {
        KeyboardUtil keyboardUtilTest = new KeyboardUtil();
        String price = "$123.45";
        float expectedValue = 123.45f;

        float result = keyboardUtilTest.formatPrice(price);

        assertEquals(expectedValue, result, 0.001f);
    }

    @Test
    public void testFormatPrice_invalidInput() {
        KeyboardUtil keyboardUtilTest = new KeyboardUtil(); // Replace 'KeyboardUtilTest' with your actual class name
        String invalidPrice = "$abc"; // Invalid input price

        assertThrows(NumberFormatException.class, () -> {
            keyboardUtilTest.formatPrice(invalidPrice);
        });
    }
}