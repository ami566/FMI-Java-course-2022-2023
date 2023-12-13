package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OutlookTest {
    private static String accName1 = "ami566";
    private static String accName2 = "sunshine02";
    private static String email1 = "test_email01@gmail.com";
    private static String email2 = "test_email02@gmail.com";
    private static String inbox = "inbox";
    private static String sent = "sent";
    private static String subFolderToInboxAcc1name = "cats";
    private static String subFolderToInboxAcc1path = "/inbox/cats";
    private static Account acc1 = new Account(email1, accName1);
    private static Account acc2 = new Account(email2, accName2);
    private static Folder subFolderToInboxOfAcc1;
    private Map<Account, Folder> data;
    private Outlook outlook;

    @BeforeEach
    public void setUp() {
        outlook = new Outlook();
        data = new HashMap<>();

        Folder f1 = new Folder(accName1);
        subFolderToInboxOfAcc1 = f1.createNewSubfolder(inbox).createNewSubfolder(subFolderToInboxAcc1name);
        f1.createNewSubfolder(sent);

        Folder f2 = new Folder(accName2);
        f2.createNewSubfolder(inbox);
        f2.createNewSubfolder(sent);

        data.put(acc1, f1);
        data.put(acc2, f2);

    }

    @Test
    public void testAddNewAccountInvalidAccountNameParams() {
        assertThrows(IllegalArgumentException.class, () -> outlook.addNewAccount("", email1),
                "Account name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.addNewAccount("   ", email1),
                "Account name cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.addNewAccount(null, email1),
                "Account name cannot be null");
    }

    @Test
    public void testAddNewAccountInvalidEmailParams() {
        assertThrows(IllegalArgumentException.class, () -> outlook.addNewAccount(accName1, ""),
                "Email cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.addNewAccount(accName1, "   "),
                "Email cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.addNewAccount(accName1, null),
                "Email cannot be null");
    }

    @Test
    public void testAddNewAccountAlreadyExistingName() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        assertEquals(acc1, newAcc, "Account is not created correctly");
        assertThrows(AccountAlreadyExistsException.class, () -> outlook.addNewAccount(accName1, email2),
                "Cannot create account with the same name");
        assertThrows(AccountAlreadyExistsException.class, () -> outlook.addNewAccount(accName1, email1),
                "Cannot create account with the same name and email");
    }

    @Test
    public void testCreateFolderInvalidAccountNameParams() {
        assertThrows(IllegalArgumentException.class, () -> outlook.createFolder("", subFolderToInboxAcc1path),
                "Account name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.createFolder("   ", subFolderToInboxAcc1path),
                "Account name cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.createFolder(null, subFolderToInboxAcc1path),
                "Account name cannot be null");
    }

    @Test
    public void testCreateFolderInvalidPathParams() {
        assertThrows(IllegalArgumentException.class, () -> outlook.createFolder(accName1, ""),
                "Path cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.createFolder(accName1, "   "),
                "Path cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.createFolder(accName1, null),
                "Path cannot be null");
    }

    @Test
    public void testCreateFolderWithNoNExistingAccount() {
        assertThrows(AccountNotFoundException.class, () -> outlook.createFolder(accName1, subFolderToInboxAcc1path),
                "Cannot create folder to not existing account");
    }

    @Test
    public void testCreateFolderWithInvalidPath() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        assertThrows(InvalidPathException.class, () -> outlook.createFolder(accName1, "/cats"),
                "Cannot create folder with path that does not start from the root folder");
        assertThrows(InvalidPathException.class, () -> outlook.createFolder(accName1, "/catsAreMyLove"),
                "Cannot create folder with path that does not start from the root folder");
        assertThrows(InvalidPathException.class, () -> outlook.createFolder(accName1, "/inbox/cats/myCat"),
                "Cannot create folder with path that contains non-existing intermediate folder");
    }

    @Test
    public void testCreateFolderWithPathForAlreadyExistingFolder() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        outlook.createFolder(accName1, subFolderToInboxAcc1path);
        assertThrows(FolderAlreadyExistsException.class, () -> outlook.createFolder(accName1, subFolderToInboxAcc1path),
                "Cannot create folder with absolute same path");
    }

    @Test
    public void testAddRuleInvalidAccountName() {

        assertThrows(IllegalArgumentException.class, () -> outlook.addRule("", subFolderToInboxAcc1path, sent, 5),
                "Account name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule("   ", subFolderToInboxAcc1path, sent, 5),
                "Account name cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(null, subFolderToInboxAcc1path, sent, 5),
                "Account name cannot be null");
    }

    @Test
    public void testAddRuleInvalidFolderPath() {
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, "", sent, 5),
                "Path cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, "   ", sent, 5),
                "Path cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, null, sent, 5),
                "Path cannot be null");
    }

    @Test
    public void testAddRuleInvalidRuleDefinitionParam() {
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, "", 5),
                "Rule definition cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, "   ", 5),
                "Rule definition cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, null, 5),
                "Rule definition cannot be null");
    }

    @Test
    public void testAddRuleInvalidPriority() {
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, sent, -1),
                "Priority cannot be negative number");
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, sent, 12),
                "Priority cannot be greater than 10");
    }

    @Test
    public void testAddRuleWithNoNExistingAccount() {
        assertThrows(AccountNotFoundException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, sent, 4),
                "Cannot add rule to non-existing account");
    }

    @Test
    public void testAddRuleWithNonExistingFolder() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        assertThrows(FolderNotFoundException.class, () -> outlook.addRule(accName1, subFolderToInboxAcc1path, sent, 4),
                "Cannot add rule to non-existing folder");
    }

    @Test
    public void testGetMailsFromFolderInvalidAccountParams() {
        assertThrows(IllegalArgumentException.class, () -> outlook.getMailsFromFolder("", subFolderToInboxAcc1path),
                "Account name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.getMailsFromFolder("   ", subFolderToInboxAcc1path),
                "Account name cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.getMailsFromFolder(null, subFolderToInboxAcc1path),
                "Account name cannot be null");
    }

    @Test
    public void testGetMailsFromFolderInvalidFolderPath() {
        assertThrows(IllegalArgumentException.class, () -> outlook.getMailsFromFolder(accName1, ""),
                "Path cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.getMailsFromFolder(accName1, "   "),
                "Path cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.getMailsFromFolder(accName1, null),
                "Path cannot be null");
    }

    @Test
    public void testGetMailsFromFolderWithNoNExistingAccount() {
        assertThrows(AccountNotFoundException.class, () -> outlook.getMailsFromFolder(accName1, subFolderToInboxAcc1path),
                "Cannot get mails from non-existing account");
    }

    @Test
    public void testGetMailsFromFolderWithNonExistingFolder() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        assertThrows(FolderNotFoundException.class, () -> outlook.getMailsFromFolder(accName1, subFolderToInboxAcc1path),
                "Cannot get mails from non-existing folder");
    }

   /* @Test
    public void testGetMailsFromFolderCorrect() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        assertThrows(FolderNotFoundException.class, () -> outlook.getMailsFromFolder(accName1, subFolderToInboxAcc1path),
                "Cannot get mails from non-existing folder");
    } */

    @Test
    public void testReceiveMailInvalidAccountParams() {
        assertThrows(IllegalArgumentException.class, () -> outlook.receiveMail("", "subFolderToInboxAcc1path", "df"),
                "Account name cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> outlook.receiveMail("   ", "sdf", "sdf"),
                "Account name cannot be blank");
        assertThrows(IllegalArgumentException.class, () -> outlook.receiveMail(null, subFolderToInboxAcc1path, "sfdg"),
                "Account name cannot be null");
    }

    @Test
    public void testaddRule() {
        Account newAcc = outlook.addNewAccount(accName1, email1);
        outlook.createFolder(accName1, subFolderToInboxAcc1path);
        Rule ne = new Rule();
        outlook.addRule(accName1,subFolderToInboxAcc1path, "hello", 3 );
       assertEquals(1,1);
    }
}
