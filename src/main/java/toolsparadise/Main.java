package toolsparadise;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        while (true) {
            System.out.println("\n===== ToolsParadise =====");
            System.out.println("1. Password Generator");
            System.out.println("2. Password Manager");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    passwordGenerator.generatePasswordTool(scanner);
                    break;
                case 2:
                	PasswordManager.managePasswords(scanner);
                	break;
                case 3:
                    System.out.println("Exiting ToolsParadise...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }

	}

}
