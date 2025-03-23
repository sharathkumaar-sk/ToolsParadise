package toolsparadise;

import util.GoogleDriveManager;
import java.security.SecureRandom;
import java.util.Scanner;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";
    private static final int MIN_PASSWORD_LENGTH = 8;

    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
    private static final SecureRandom random = new SecureRandom();

    public void generatePasswordTool(Scanner scanner) {
        System.out.println("\n=== Password Generator ===");

        System.out.print("Enter App/Site Name: ");
        String appName = scanner.nextLine();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        String option;
        while (true) {
            System.out.print("Do you want to generate a password (G) or enter your own (E)? ");
            option = scanner.nextLine().trim().toUpperCase();
            
            if (option.equals("G") || option.equals("E")) {
                break;
            }
            
            System.out.println("Invalid choice! Please enter 'G' to generate a password or 'E' to enter your own.");
        }

        String password = "";
        boolean isDuplicate;
        if (option.equals("E")) {
            while (true) {
                System.out.print("Enter your password: ");
                password = scanner.nextLine();
                isDuplicate = GoogleDriveManager.isPasswordDuplicate(password);
                if (!isDuplicate) {
                    break;
                }
                System.out.println("Password already exists! Please enter a new password.");
            }
            
            while (true) {
                System.out.print("Do you want to (S)ave or (E)xit? ");
                String choice = scanner.nextLine().trim().toUpperCase();

                switch (choice) {
                    case "S":
                        GoogleDriveManager.savePassword(appName, username, password);
                        return;
                    case "E":
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter S or E.");
                }
            }
        } else {
            while (true) {
                int passwordLength = getValidPasswordLength(scanner);
                password = generatePassword(passwordLength);
                System.out.println("Generated Password: " + password);
                
                isDuplicate = GoogleDriveManager.isPasswordDuplicate(password);
                if (!isDuplicate) {
                    break;
                }
                System.out.println("Generated password already exists! Generating a new one...");
            }

            while (true) {
                System.out.print("Do you want to (R)etry, (S)ave, or (E)xit? ");
                String choice = scanner.nextLine().trim().toUpperCase();

                switch (choice) {
                    case "R":
                        password = generatePassword(getValidPasswordLength(scanner));
                        System.out.println("Generated Password: " + password);
                        break;
                    case "S":
                        GoogleDriveManager.savePassword(appName, username, password);
                        return;
                    case "E":
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter R, S, or E.");
                }
            }
        }
    }

    private int getValidPasswordLength(Scanner scanner) {
        int length;
        while (true) {
            System.out.print("Enter Desired Password Length (min " + MIN_PASSWORD_LENGTH + "): ");
            try {
                length = Integer.parseInt(scanner.nextLine());
                if (length >= MIN_PASSWORD_LENGTH) {
                    return length;
                } else {
                    System.out.println("Password length must be at least " + MIN_PASSWORD_LENGTH + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[index];
            passwordArray[index] = temp;
        }

        return new String(passwordArray);
    }
}
