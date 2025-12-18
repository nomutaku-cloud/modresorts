package com.acme.modres.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.acme.common.EnvConfig;

public class ModResortsEnv {
  private static final VarHandle ADMIN_ENDPOINT_HANDLE;
  private static final Logger logger = Logger.getLogger(ModResortsEnv.class.getName());

  static {
    try {
      MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(
          EnvConfig.class, MethodHandles.lookup());
      ADMIN_ENDPOINT_HANDLE = lookup.findVarHandle(
          EnvConfig.class, "adminApiEndpoint", String.class);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private EnvConfig envConfig;

  public ModResortsEnv() {
    logger.log(Level.INFO, "Modresorts environment configuration details");
    envConfig = new EnvConfig();
  }

  public void reset() {
    String adminEndpoint = getAdminEndpoint();
    URL url = null;
    try {
      url = URI.create(adminEndpoint).toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) url.openConnection();
      HttpURLConnection http = (HttpURLConnection) con;
      http.setRequestMethod("POST");
      http.setDoOutput(true);
      BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
      httpRequestBodyWriter.write("reset=true");
      httpRequestBodyWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getAdminEndpoint() {
    try {
      return (String) ADMIN_ENDPOINT_HANDLE.get(envConfig);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Failed to access adminApiEndpoint", e);
      return null;
    }
  }

  // Test driver for class
  public static void main(String args[]) {
  }
}
