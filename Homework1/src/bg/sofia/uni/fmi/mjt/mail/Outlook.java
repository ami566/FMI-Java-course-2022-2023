package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.*;

import java.time.LocalDateTime;
import java.util.*;

public class Outlook implements MailClient {
    // should have made new class to hold the account, the rules and the folders
    //because now, nothing would work, since what i am working with does not reference the right objects
    Map<Account, Folder> accountFolderMap;
    Map<Account, List<Rule>> accountRulesMap;
    private final String inbox = "inbox";
    private final String sent = "sent";

    public Outlook() {
        accountFolderMap = new HashMap<>();
        accountRulesMap = new HashMap<>();
    }

    private Account findIfAccountExists(String accName) {
        for (Account a : accountFolderMap.keySet()) {
            if (a.name().equals(accName)) {
                return a;
            }
        }

        return null;
    }

    private Folder findFolder(Account account, String path) {
        String folderName = "";
        Folder currFolder = accountFolderMap.get(account).checkIfSubfolderExists(inbox);
        int counter = inbox.length() + 1;
        counter++;
        while (counter < path.length() && currFolder != null) {
            char c = path.charAt(counter);
            if (c != '/') {
                folderName += c;
            } else {
                currFolder = currFolder.checkIfSubfolderExists(folderName);
                folderName = "";
            }
            counter++;
        }
        return currFolder.checkIfSubfolderExists(folderName);
    }

    private boolean isStringInvalid(String s) {
        return s == null || s.isEmpty() || s.isBlank();
    }

    @Override
    public Account addNewAccount(String accountName, String email) {
        if (isStringInvalid(accountName) || isStringInvalid(email)) {
            throw new IllegalArgumentException("String parameters cannot be null, empty or blank");
        }
        Account a = new Account(email, accountName);

        if (accountFolderMap.containsKey(a)) {
            throw new AccountAlreadyExistsException("Account with that name and email already exists");
        }

        Folder f = new Folder(accountName);
        f.createNewSubfolder(inbox);
        f.createNewSubfolder(sent);
        accountFolderMap.put(a, f);
        accountRulesMap.put(a, new ArrayList<>());
        return a;
    }

    @Override
    public void createFolder(String accountName, String path) {
        if (isStringInvalid(accountName) || isStringInvalid(path)) {
            throw new IllegalArgumentException("String parameters cannot be null, empty or blank");
        }

        Account account = findIfAccountExists(accountName);
        if (account == null) {
            throw new AccountNotFoundException("Account with the given name does not exist");
        }

        int counter = inbox.length() + 1;
        if (path.length() < counter || !path.substring(0, counter).equals("/" + inbox)) {
            throw new InvalidPathException("Given path does not start from the root folder");
        }

        String folderName = "";
        Folder currFolder = accountFolderMap.get(account).checkIfSubfolderExists(inbox);
        counter = inbox.length() + 1;
        counter++;
        while (counter < path.length() && currFolder != null) {
            char c = path.charAt(counter);
            if (c != '/') {
                folderName += c;
            } else {
                currFolder = currFolder.checkIfSubfolderExists(folderName);
                folderName = "";
            }
            counter++;
        }

        if (currFolder == null) { //|| path.charAt(counter) < path.length()
            throw new InvalidPathException("Given path does not contain some of the intermediate folders");
        }

        if (currFolder.checkIfSubfolderExists(folderName) != null) {
            throw new FolderAlreadyExistsException("Folder with the given path already exists");
        }

        currFolder.createNewSubfolder(folderName);
    }


    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        if (isStringInvalid(accountName) || isStringInvalid(folderPath) || isStringInvalid(ruleDefinition)) {
            throw new IllegalArgumentException("String parameters cannot be null, empty or blank");
        }

        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Priority should be within range [1, 10]");
        }

        Account account = findIfAccountExists(accountName);
        if (account == null) {
            throw new AccountNotFoundException("Account with the given name does not exist");
        }

        Folder currFolder = findFolder(account, folderPath);
        if (currFolder == null) {
            throw new FolderNotFoundException("Given folder does not exist");
        }

        Rule newRule = new Rule(ruleDefinition, currFolder, priority);
        accountRulesMap.get(account).add(newRule);
        Folder accInbox = accountFolderMap.get(account).checkIfSubfolderExists(inbox);

        moveEmails(accInbox, currFolder, newRule);
    }

    private boolean checkIfRuleApplies(Mail mail, Rule rule) {
        return false;
    }

    // won't work, as i realized that I am not referencing the right objects ....
    private void moveEmails(Folder from, Folder to, Rule rule) {

        List<Mail> mails = from.getMails();
        for (Mail m : mails) {
            if (checkIfRuleApplies(m, rule)) {
                to.addMail(m);
            }
        }

        from.getMails().removeAll(to.getMails());

    }

    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {
        if (isStringInvalid(accountName) || isStringInvalid(mailContent) || isStringInvalid(mailMetadata)) {
            throw new IllegalArgumentException("String params cannot be null, empty or blank");
        }
        Account acc = findIfAccountExists(accountName);
        if (acc == null) {
            throw new AccountNotFoundException("Account with the given name does not exist");
        }

        String sender = "";
        String subject = "";
        LocalDateTime date = null;
        String dateString = null;
        Set<String> recipients = new HashSet<>();
        String[] conditions = mailMetadata.split(System.lineSeparator());
        for (String c : conditions) {
            String[] elements = c.split(":");
            if (elements[0].equals("sender")) {
                sender = elements[1].strip();
            }
            if (elements[0].equals("subject")) {
                subject = elements[1].strip();
            }
            if (elements[0].equals("recipients")) {
                String[] keywords = elements[1].split(",");
                for (String word : keywords) {
                    recipients.add(word.strip());
                }
            }
            if (elements[0].equals("received")) {
                subject = elements[1].strip();
            }

            if (dateString != null) {
                dateString += ":" + conditions[conditions.length - 1];
            }
        }
        if (dateString != null) {
            // date = dateString;
        }
        Mail m = new Mail(acc, recipients, subject, mailContent, date);
        accountFolderMap.get(acc).checkIfSubfolderExists(inbox).addMail(m);
    }

    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {
        if (isStringInvalid(account) || isStringInvalid(folderPath)) {
            throw new IllegalArgumentException("String params cannot be null, empty or blank");
        }

        Account acc = findIfAccountExists(account);
        if (acc == null) {
            throw new AccountNotFoundException("Account with the given name does not exist");
        }
        Folder folder = findFolder(acc, folderPath);
        if (folder == null) {
            throw new FolderNotFoundException("Given folder does not exist");
        }
        return folder.getMails();
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {

    }
}
