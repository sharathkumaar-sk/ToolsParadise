package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GoogleDriveManager {
    private static final String FOLDER_NAME = "ToolsParadise";
    private static final String FILE_NAME = "passwords.json";
    private static final Gson gson = new Gson();

    public static boolean isPasswordDuplicate(String plainPassword) {
        try {
            Drive driveService = GoogleDriveService.getDriveService();
            String folderId = getOrCreateFolder(driveService);
            if (folderId == null) {
                System.out.println("Google Drive folder not found.");
                return false;
            }

            String fileId = getOrCreateFile(driveService, folderId);
            if (fileId == null) {
                System.out.println("Google Drive file not found.");
                return false;
            }

            JsonArray passwordArray = readPasswordFile(driveService, fileId);
            if (passwordArray == null || passwordArray.size() == 0) {
                System.out.println("No passwords found in the file.");
                return false;
            }

            for (int i = 0; i < passwordArray.size(); i++) {
                JsonObject entry = passwordArray.get(i).getAsJsonObject();
                String storedEncryptedPassword = entry.get("encryptedPassword").getAsString();
                String decryptedStoredPassword = EncryptionUtil.decrypt(storedEncryptedPassword);

                if (decryptedStoredPassword != null && decryptedStoredPassword.equals(plainPassword)) {
                    return true; // Duplicate found
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking password duplicate: " + e.getMessage());
        }
        return false; // No duplicate found
    }


    public static void savePassword(String appName, String username, String plainPassword) {
        try {
            System.out.println("Saving password...");
            Drive driveService = GoogleDriveService.getDriveService();
            String folderId = getOrCreateFolder(driveService);
            String fileId = getOrCreateFile(driveService, folderId);
            JsonArray passwordArray = readPasswordFile(driveService, fileId);

            String encryptedPassword = EncryptionUtil.encrypt(plainPassword);
            JsonObject newPasswordEntry = new JsonObject();
            newPasswordEntry.addProperty("appName", appName);
            newPasswordEntry.addProperty("username", username);
            newPasswordEntry.addProperty("encryptedPassword", encryptedPassword);
            passwordArray.add(newPasswordEntry);

            updatePasswordFile(driveService, fileId, passwordArray.toString());
            System.out.println("Password saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving password: " + e.getMessage());
        }
    }

    private static String getOrCreateFolder(Drive driveService) throws IOException {
        FileList result = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + FOLDER_NAME + "'")
                .setSpaces("drive")
                .setFields("files(id)")
                .execute();

        List<File> files = result.getFiles();
        if (files != null && !files.isEmpty()) {
            return files.get(0).getId();
        }
        System.out.println("Creating new folder as ToolsParadise...");
        File fileMetadata = new File();
        fileMetadata.setName(FOLDER_NAME);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList("root"));

        File folder = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return folder.getId();
    }

    private static String getOrCreateFile(Drive driveService, String folderId) throws IOException {
        FileList result = driveService.files().list()
                .setQ("name='" + FILE_NAME + "' and '" + folderId + "' in parents")
                .setSpaces("drive")
                .setFields("files(id)")
                .execute();

        List<File> files = result.getFiles();
        if (files != null && !files.isEmpty()) {
            return files.get(0).getId();
        }
        System.out.println("Creating new file as password.json inside toolsparadise...");
        File fileMetadata = new File();
        fileMetadata.setName(FILE_NAME);
        fileMetadata.setMimeType("application/json");
        fileMetadata.setParents(Collections.singletonList(folderId));

        ByteArrayContent content = new ByteArrayContent("application/json", "[]".getBytes(StandardCharsets.UTF_8));

        File file = driveService.files().create(fileMetadata, content)
                .setFields("id")
                .execute();
        return file.getId();
    }

    private static JsonArray readPasswordFile(Drive driveService, String fileId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().get(fileId)
                .executeMediaAndDownloadTo(outputStream);

        String jsonContent = outputStream.toString(StandardCharsets.UTF_8);
        return gson.fromJson(jsonContent, JsonArray.class);
    }

    private static void updatePasswordFile(Drive driveService, String fileId, String jsonContent) throws IOException {
        ByteArrayContent content = new ByteArrayContent("application/json", jsonContent.getBytes(StandardCharsets.UTF_8));
        driveService.files().update(fileId, null, content).execute();
    }
    
    public static List<String[]> getAllPasswords() {
        List<String[]> passwordsList = new ArrayList<>();
        try {
            Drive driveService = GoogleDriveService.getDriveService();
            String fileId = getOrCreateFile(driveService, getOrCreateFolder(driveService));
            JsonArray passwordArray = readPasswordFile(driveService, fileId);
            
            for (int i = 0; i < passwordArray.size(); i++) {
                JsonObject entry = passwordArray.get(i).getAsJsonObject();
                String appName = entry.get("appName").getAsString();
                String username = entry.get("username").getAsString();
                String encryptedPassword = entry.get("encryptedPassword").getAsString();
                String decryptedPassword = EncryptionUtil.decrypt(encryptedPassword);
                passwordsList.add(new String[]{String.valueOf(i + 1), appName, username, decryptedPassword});
            }
        } catch (Exception e) {
            System.err.println("Error retrieving passwords: " + e.getMessage());
        }
        return passwordsList;
    }

    public static JsonObject searchPassword(String appName) {
        try {
            Drive driveService = GoogleDriveService.getDriveService();
            String fileId = getOrCreateFile(driveService, getOrCreateFolder(driveService));
            JsonArray passwordArray = readPasswordFile(driveService, fileId);
            
            for (int i = 0; i < passwordArray.size(); i++) {
                JsonObject entry = passwordArray.get(i).getAsJsonObject();
                if (entry.get("appName").getAsString().equalsIgnoreCase(appName)) {
                    return entry;
                }
            }
        } catch (Exception e) {
            System.err.println("Error searching password: " + e.getMessage());
        }
        return null;
    }

    public static boolean editPassword(int index, String newAppName, String newUsername, String newPassword) {
        try {
            Drive driveService = GoogleDriveService.getDriveService();
            String fileId = getOrCreateFile(driveService, getOrCreateFolder(driveService));
            JsonArray passwordArray = readPasswordFile(driveService, fileId);
            
            int adjustedIndex = index - 1;
            if (adjustedIndex < 0 || adjustedIndex >= passwordArray.size()) {
                System.out.println("Invalid index.");
                return false;
            }
            
            String encryptedPassword = EncryptionUtil.encrypt(newPassword);
            JsonObject updatedEntry = new JsonObject();
            updatedEntry.addProperty("appName", newAppName);
            updatedEntry.addProperty("username", newUsername);
            updatedEntry.addProperty("encryptedPassword", encryptedPassword);
            
            passwordArray.set(adjustedIndex, updatedEntry);
            updatePasswordFile(driveService, fileId, passwordArray.toString());
            
            return true;
        } catch (Exception e) {
            System.err.println("Error editing password: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePassword(int index) {
        try {
            Drive driveService = GoogleDriveService.getDriveService();
            String fileId = getOrCreateFile(driveService, getOrCreateFolder(driveService));
            JsonArray passwordArray = readPasswordFile(driveService, fileId);
            
            int adjustedIndex = index - 1;
            if (adjustedIndex < 0 || adjustedIndex >= passwordArray.size()) {
                System.out.println("Invalid index.");
                return false;
            }
            
            passwordArray.remove(adjustedIndex);
            updatePasswordFile(driveService, fileId, passwordArray.toString());
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting password: " + e.getMessage());
        }
        return false;
    }


    public static void importPasswords(String importedPasswords) {
//        try {
//            Drive driveService = GoogleDriveService.getDriveService();
//            String fileId = getOrCreateFile(driveService, getOrCreateFolder(driveService));
//            JsonArray passwordArray = readPasswordFile(driveService, fileId);
//            for (int i = 0; i < importedPasswords.size(); i++) {
//                passwordArray.add(importedPasswords.get(i));
//            }
//            updatePasswordFile(driveService, fileId, passwordArray.toString());
//        } catch (Exception e) {
//            System.err.println("Error importing passwords: " + e.getMessage());
//        }
    }

    public static JsonArray exportPasswords() {
//        try {
//            return readPasswordFile(GoogleDriveService.getDriveService(), getOrCreateFile(GoogleDriveService.getDriveService(), getOrCreateFolder(GoogleDriveService.getDriveService())));
//        } catch (Exception e) {
//            System.err.println("Error exporting passwords: " + e.getMessage());
//        }
        return new JsonArray();
    }
}
