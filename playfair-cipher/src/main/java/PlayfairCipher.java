import java.util.HashSet;
import java.util.Set;

public class PlayfairCipher {

    private static final int MATRIX_SIZE = 5;
    private char[][] keyMatrix;

    public PlayfairCipher(String key) {
        this.keyMatrix = generateKeyMatrix(key);
    }

    private char[][] generateKeyMatrix(String key) {
        Set<Character> usedChars = new HashSet<>();
        char[][] matrix = new char[MATRIX_SIZE][MATRIX_SIZE];

        String processedKey = key.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        int keyIndex = 0;
        int charIndex = 0;

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (keyIndex < processedKey.length()) {
                    char c = processedKey.charAt(keyIndex++);
                    while (usedChars.contains(c)) {
                        if (keyIndex < processedKey.length()) {
                            c = processedKey.charAt(keyIndex++);
                        } else {
                            break;
                        }
                    }
                    if (!usedChars.contains(c)) {
                        matrix[i][j] = c;
                        usedChars.add(c);
                    } else {
                        j--;
                    }
                } else {
                    while (charIndex < 26) {
                        char c = (char) ('A' + charIndex++);
                        if (c == 'J') continue;
                        if (!usedChars.contains(c)) {
                            matrix[i][j] = c;
                            usedChars.add(c);
                            break;
                        }
                    }
                }
            }
        }

        return matrix;
    }

    private String prepareText(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder prepared = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            prepared.append(text.charAt(i));
            if (i + 1 < text.length() && text.charAt(i) == text.charAt(i + 1)) {
                prepared.append('X');
            }
        }

        if (prepared.length() % 2 != 0) {
            prepared.append('X');
        }

        return prepared.toString();
    }

    private int[] findPosition(char c) {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (keyMatrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("Character " + c + " not found in key matrix");
    }

    public String encrypt(String plaintext) {
        String preparedText = prepareText(plaintext);
        StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < preparedText.length(); i += 2) {
            char first = preparedText.charAt(i);
            char second = preparedText.charAt(i + 1);

            int[] posFirst = findPosition(first);
            int[] posSecond = findPosition(second);

            int row1 = posFirst[0], col1 = posFirst[1];
            int row2 = posSecond[0], col2 = posSecond[1];

            if (row1 == row2) {
                ciphertext.append(keyMatrix[row1][(col1 + 1) % MATRIX_SIZE]);
                ciphertext.append(keyMatrix[row2][(col2 + 1) % MATRIX_SIZE]);
            } else if (col1 == col2) {
                ciphertext.append(keyMatrix[(row1 + 1) % MATRIX_SIZE][col1]);
                ciphertext.append(keyMatrix[(row2 + 1) % MATRIX_SIZE][col2]);
            } else {
                ciphertext.append(keyMatrix[row1][col2]);
                ciphertext.append(keyMatrix[row2][col1]);
            }
        }

        return ciphertext.toString();
    }

    public String decrypt(String ciphertext) {
        ciphertext = ciphertext.toUpperCase().replaceAll("[^A-Z]", "");
        if (ciphertext.length() % 2 != 0) {
            throw new IllegalArgumentException("Ciphertext length must be even");
        }

        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char first = ciphertext.charAt(i);
            char second = ciphertext.charAt(i + 1);

            int[] posFirst = findPosition(first);
            int[] posSecond = findPosition(second);

            int row1 = posFirst[0], col1 = posFirst[1];
            int row2 = posSecond[0], col2 = posSecond[1];

            if (row1 == row2) {
                plaintext.append(keyMatrix[row1][(col1 - 1 + MATRIX_SIZE) % MATRIX_SIZE]);
                plaintext.append(keyMatrix[row2][(col2 - 1 + MATRIX_SIZE) % MATRIX_SIZE]);
            } else if (col1 == col2) {
                plaintext.append(keyMatrix[(row1 - 1 + MATRIX_SIZE) % MATRIX_SIZE][col1]);
                plaintext.append(keyMatrix[(row2 - 1 + MATRIX_SIZE) % MATRIX_SIZE][col2]);
            } else {
                plaintext.append(keyMatrix[row1][col2]);
                plaintext.append(keyMatrix[row2][col1]);
            }
        }

        String result = plaintext.toString();
        if (result.endsWith("X")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public void printKeyMatrix() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(keyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}