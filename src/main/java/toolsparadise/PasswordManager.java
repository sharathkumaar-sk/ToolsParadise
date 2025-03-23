package toolsparadise;

import java.util.List;
import java.util.Scanner;

import util.EncryptionUtil;
import util.GoogleDriveManager;

public class PasswordManager {
    public static void managePasswords(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Password Manager ===");
            displayPasswordTable();
            
            System.out.println("\nOptions:");
            System.out.println("1. Search Password");
            System.out.println("2. Edit Password");
            System.out.println("3. Delete Password");
            System.out.println("4. Import Passwords");
            System.out.println("5. Export Passwords");
            System.out.println("6. Exit");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    searchPassword(scanner);
                    break;
                case "2":
                    editPassword(scanner);
                    break;
                case "3":
                    deletePassword(scanner);
                    break;
                case "4":
                    importPasswords(scanner);
                    break;
                case "5":
                    exportPasswords();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid choice! Please enter a valid option.");
            }
        }
    }

    private static void displayPasswordTable() {
        List<String[]> passwords = GoogleDriveManager.getAllPasswords();
        System.out.println("------------------------------------------------------");
        System.out.printf("%-5s %-20s %-20s %-20s\n", "Index", "App Name", "Username", "Password");
        System.out.println("------------------------------------------------------");
        
        int index = 0;
        for (String[] entry : passwords) {
            System.out.printf("%-5s %-20s %-20s %-20s\n", entry[0], entry[1], entry[2],entry[3]);
        }
        System.out.println("------------------------------------------------------");
    }

    private static void searchPassword(Scanner scanner) {
        System.out.print("Enter App Name to search: ");
        String appName = scanner.nextLine().trim();
        
        var result = GoogleDriveManager.searchPassword(appName);
        if (result != null) {
            System.out.println("Password found:");
            System.out.println("App Name: " + result.get("appName").getAsString());
            System.out.println("Username: " + result.get("username").getAsString());
            System.out.println("Password: " + EncryptionUtil.decrypt(result.get("encryptedPassword").getAsString()));
        } else {
            System.out.println("No password found for the given app name.");
        }
    }

    private static void editPassword(Scanner scanner) {
        System.out.print("Enter index of password to edit: ");
        int index = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Enter new App Name: ");
        String newAppName = scanner.nextLine().trim();
        
        System.out.print("Enter new Username: ");
        String newUsername = scanner.nextLine().trim();
        
        System.out.print("Enter new Password: ");
        String newPassword = scanner.nextLine().trim();
        
        boolean success = GoogleDriveManager.editPassword(index, newAppName, newUsername, newPassword);
        if (success) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Failed to update password.");
        }
    }

    private static void deletePassword(Scanner scanner) {
        System.out.print("Enter index of password to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        boolean success = GoogleDriveManager.deletePassword(index);
        if (success) {
            System.out.println("Password Deleted successfully.");
        } else {
            System.out.println("Failed to delete password.");
        }
    }


    private static void importPasswords(Scanner scanner) {
        System.out.print("Enter file path for import: ");
        String filePath = scanner.nextLine();
        GoogleDriveManager.importPasswords(filePath);
    }

    private static void exportPasswords() {
        GoogleDriveManager.exportPasswords();
        System.out.println("Passwords exported successfully.");
    }
}
