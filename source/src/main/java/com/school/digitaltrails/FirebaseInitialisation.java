package com.school.digitaltrails;

import java.io.InputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


public class FirebaseInitialisation {

  private FirebaseApp instance;

  public void initialize() {
    if(instance == null){
      try {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("credentials_google.json");
        System.out.println(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();
          instance = FirebaseApp.initializeApp(options);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}