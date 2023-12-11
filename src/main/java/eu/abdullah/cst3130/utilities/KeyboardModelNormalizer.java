package eu.abdullah.cst3130.utilities;

import java.util.Arrays;

public class KeyboardModelNormalizer {

//    public  String normalizeModelName(String modelName) {
//        if (modelName != null && !modelName.isEmpty()) {
//            String[] words = modelName.split("\\s+");
//            if (words.length > 0) {
//                String lastWord = words[words.length - 1];
//                if (lastWord.endsWith("(") || lastWord.endsWith(")")) {
//                    words[words.length - 1] = lastWord.substring(0, lastWord.length() - 1);
//                    modelName = String.join(" ", words);
//                }
//            }
//        }
//        return modelName;
//    }

    public String normalizeModelName(String modelName) {
        if (modelName != null && !modelName.isEmpty()) {
            String[] words = modelName.split("\\s+");
            if (words.length > 0) {
                String lastWord = words[words.length - 1];
                while (lastWord.matches(".*[^a-zA-Z0-9].*")) {
                    lastWord = lastWord.substring(0, lastWord.length() - 1);
                    if (lastWord.isEmpty()) {
                        words = Arrays.copyOf(words, words.length - 1);
                        if (words.length == 0) {
                            return ""; // All words removed
                        }
                        lastWord = words[words.length - 1];
                    }
                }
                words[words.length - 1] = lastWord;
                modelName = String.join(" ", words);
            }
        }
        return modelName;
    }
}
