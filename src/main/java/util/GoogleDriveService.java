package util;

        import java.io.*;
        import java.util.Collections;

        import com.google.api.client.auth.oauth2.Credential;
        import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
        import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
        import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
        import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
        import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
        import com.google.api.client.http.HttpTransport;
        import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.gson.GsonFactory;
        import com.google.api.client.util.store.FileDataStoreFactory;
        import com.google.api.services.drive.Drive;
        import com.google.api.services.drive.DriveScopes;

        public class GoogleDriveService {
            private static final String APPLICATION_NAME = "ToolsParadise";
            private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
            private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(System.getProperty("user.home"), "credentials");

            public static Drive getDriveService() throws Exception {
                HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                Credential credential = getCredentials(HTTP_TRANSPORT);
                
                return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
            }

            private static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws Exception {
                // Load client secrets
                InputStream in = GoogleDriveService.class.getClassLoader()
                        .getResourceAsStream("credentials.json");  // Load from resources
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

                // Set up authorization code flow
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                        Collections.singletonList(DriveScopes.DRIVE_FILE))
                        .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                        .setAccessType("offline")
                        .build();

                return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
            }
        }

