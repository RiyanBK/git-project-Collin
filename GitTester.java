import java.io.*;
import java.util.*;

public class GitTester {
    public static void main (String [] args) {
        Git git = new Git();
        Blob blob = new Blob ("test.txt");
        System.out.println (blob.toSHA1());
        Blob blob2 = new Blob ("test3.txt");
        System.out.println (blob2.toSHA1());
        // git.deleteGit();
        //can comment out above code to make it delete files or not
    }

}
