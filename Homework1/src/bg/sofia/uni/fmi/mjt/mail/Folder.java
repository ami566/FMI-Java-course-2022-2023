package bg.sofia.uni.fmi.mjt.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Folder {
    private String path;
    private List<Mail> mails;
    private Map<String, Folder> subfolders;

    private final char forwardSlash = '/';

    public Folder(String name) {
        this.path = forwardSlash + name;
        mails = new ArrayList<>();
        subfolders = new HashMap<>();
    }

    public String getPath() {
        return path;
    }

    public void addMail(Mail m) {
        mails.add(m);
    }

    public void removeMail(Mail m) {
        mails.remove(m);
    }

    public Folder createNewSubfolder(String n) {
        Folder newFolder = new Folder(path + n);
        subfolders.put(n, newFolder);
        return newFolder;
    }

    public Folder checkIfSubfolderExists(String name) {
        return subfolders.get(name);
    }

    public List<Mail> getMails() {
        return mails;
    }
}
