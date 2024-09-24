import java.io.*;

public class Git {
    File gitFolder;
    File objectsFolder;
    File indexFile;

    public Git() { //creates repository
        gitFolder = new File("git/");
        objectsFolder = new File("git/objects/");
        indexFile = new File("git/index/");
        if (gitFolder.exists() && objectsFolder.exists() && indexFile.exists()) {
            System.out.println("Git Repository already exists");
        } else {
            if (!gitFolder.exists()) {
                gitFolder.mkdir();
            }
            if (!objectsFolder.exists()) {
                objectsFolder.mkdir();
            }
            if (!indexFile.exists()) {
                try {
                    indexFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean deleteGit() { //this is the method u call when u want to delete the git repo for testing
        File git = new File("git/");
        if (git.exists()) {
            deleteFolder(git);
            git.delete();
        }
        if (!git.exists())
            return true;
        else
            return false;
    }

    public void deleteFolder(File folder) { //this is the called method that actually deletes stuff
        if (!folder.exists()){
            return;
        }
        if (!folder.isDirectory())
            folder.delete();
        for (File file : folder.listFiles()) {
            if (file.isDirectory())
                deleteFolder(file);
            file.delete();
        }
    }

}