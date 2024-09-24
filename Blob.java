import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.security.*;
import java.util.zip.*;

public class Blob {
    private String file;
    private String hashedFileContent;
    private File forObjectsFolder;
    private String fileContent;
    public static boolean zip = false; // by default it doesn't zip the data

    public Blob(String fileName) {
        file = fileName;
        fileContent = getFileContent(file);
        hashedFileContent = toSHA1();
        forObjectsFolder = new File("git/objects/" + hashedFileContent);
        try {
            if (!forObjectsFolder.exists()) {
                forObjectsFolder.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter("git/index/", true);
            writer.write(hashedFileContent + " " + file + "\n");
            writer.close();

            FileWriter writer2 = new FileWriter(forObjectsFolder);
            writer2.write(fileContent);
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toSHA1() { 
        if (zip) {
            compress(); //compresses fileContent so compressed file content is hashed
        }
        byte[] b = fileContent.getBytes(StandardCharsets.UTF_8);
        fileContent = b.toString();
        // mostly from stack overflow:
        // https://stackoverflow.com/questions/4895523/java-string-to-sha1
        String sha1 = "";
        try {
            MessageDigest encrypter = MessageDigest.getInstance("SHA-1");
            encrypter.reset();
            encrypter.update(fileContent.getBytes("UTF-8"));
            sha1 = byteToHex(encrypter.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public String getFileContent(String fileName) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.ready()) {
                content.append((char) reader.read());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentString = content.toString();
        return contentString;
    }

    private static String byteToHex(final byte[] hash) { // shamelessly copied from stack overflow:
                                                         // https://stackoverflow.com/questions/4895523/java-string-to-sha1
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static boolean toggleZip() {
        if (zip) {
            zip = false;
            return zip;
        } else {
            zip = true;
            return zip;
        }
    }

    public static boolean getZip() {
        return zip;
    }

    public void compress() {
        try {
            File compressed = File.createTempFile("compressedFile", null);
            FileInputStream in = new FileInputStream(file);
            DeflaterOutputStream out = new DeflaterOutputStream(new FileOutputStream(compressed));
            int data = in.read();
            while (data != -1) {
                out.write(data);
                data = in.read();
            }
            in.close();
            out.close();
            fileContent = getFileContent("" + compressed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
