package la.arya.librarymanagement.util;

import java.io.InputStream;
import java.security.MessageDigest;

public class checksumUtil {

    public static String calculateChecksum(InputStream fileInputStream, String algo) throws Exception {

        MessageDigest md = MessageDigest.getInstance(algo);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            md.update(buffer, 0, bytesRead);
        }

        byte[] hashBytes = md.digest();

        StringBuilder checksum = new StringBuilder();
        for (byte b : hashBytes) {
            checksum.append(String.format("%02x", b));
        }
        return checksum.toString();
    }
}
