import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayfairCipherTest {

    @Test
    @DisplayName("encrypt method test")
    public void testEncrypt() {
        PlayfairCipher cipher = new PlayfairCipher("MONARCHY");
        String plaintext = "INSTRUMENTS";
        String expectedCiphertext = "GATLMZCLRQXA";
        String actualCiphertext = cipher.encrypt(plaintext);

        assertEquals(expectedCiphertext, actualCiphertext);
    }

    @Test
    @DisplayName("decrypt method test")
    public void testDecrypt() {
        PlayfairCipher cipher = new PlayfairCipher("MONARCHY");
        String ciphertext = "GATLMZCLRQXA";
        String expectedPlaintext = "INSTRUMENTS";
        String actualPlaintext = cipher.decrypt(ciphertext);

        assertEquals(expectedPlaintext, actualPlaintext);
    }

}
