import java.io.*;
import java.util.*;
import java.security.*;

public class Blob {
    private String file;
    private String hashedFileContent;
    private File forObjectsFolder;
    private String fileContent;
    public static boolean zip = true; // by default it zips the data

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

    public String toSHA1() { // mostly from stack overflow:
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
        /*
         * if (zip) { //compress the string (this is pseudocode for now bc havent written it yet)
         * contentString = compress(contentString); 
         * }
         */
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

    public static boolean toggleZip() { //this doesnt do anything yet
        if (zip) {
            zip = false;
            return false;
        } else {
            zip = true;
            return true;
        }
    }

}
