import org.apache.catalina.startup.Tomcat;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Connect4Application {

    public static void main(String[] args) throws Exception
    {
        Tomcat tomcat = new Tomcat();
        String port = "8080"; // Also change in index.html
        tomcat.setPort(Integer.parseInt(port));
        String webAppDirLocation = setupWebApp();
        tomcat.addWebapp("", new File(webAppDirLocation).getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
   }

   private static String setupWebApp() throws Exception
   {
       Path classesPath = Paths.get(Connect4Application.class.getProtectionDomain().getCodeSource().getLocation().toURI());
       String resourcePath = classesPath.getParent().toString();
       File thisJar = new File(resourcePath + File.separator + "Connect4WebApp.jar");
       String webAppRootPathName = "connect4WebApp";
       File webAppFolder = new File(webAppRootPathName + File.separator + "WEB-INF" + File.separator +"lib");
       webAppFolder.mkdirs();
       Connect4Application.copyFileUsingChannel(thisJar, new File(webAppFolder.getPath() + File.separator + "app.jar"));
       Connect4Application.copyFileUsingChannel( new File(Connect4Application.class.getResource("index.html").getPath()), new File(webAppRootPathName + File.separator + "index.html"));
       return webAppRootPathName;
   }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        finally {
            sourceChannel.close();
            destChannel.close();
        }
    }

}
