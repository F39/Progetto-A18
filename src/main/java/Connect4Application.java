
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Connect4Application {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        String port = "8080"; // Also change in index.html
        tomcat.setPort(Integer.parseInt(port));
        String webAppDirLocation = setupWebApp();
        Context context = tomcat.addWebapp("", new File(webAppDirLocation).getAbsolutePath());
        tomcat.addServlet(context, "jersey-container-servlet", resourceConfig());
        context.addServletMapping("/rest/*", "jersey-container-servlet");
        tomcat.start();
        tomcat.getServer().await();
   }

    private static ServletContainer resourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig(new ResourceLoader().getClasses());
        resourceConfig.register(new CORSFilter());
        return new ServletContainer(resourceConfig);
    }

    private static String setupWebApp() throws Exception {
        Path classesPath = Paths.get(Connect4Application.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String resourcePath = classesPath.getParent().toString();
        File thisJar = new File(resourcePath + File.separator + "Connect4WebApp.jar");
        Path UIPath = Paths.get(classesPath.getParent().getParent() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "UI");
        String webAppRootPathName = "connect4WebApp";
        File webAppFolder = new File(webAppRootPathName + File.separator + "WEB-INF" + File.separator + "lib");
        webAppFolder.mkdirs();
        File webAppCSS = new File(webAppRootPathName + File.separator + "WEB-INF" + File.separator + "CSS");
        webAppCSS.mkdirs();
        File webAppIMG = new File(webAppRootPathName + File.separator + "WEB-INF" + File.separator + "Img");
        webAppIMG.mkdirs();
        File webAppJS = new File(webAppRootPathName + File.separator + "WEB-INF" + File.separator + "JS");
        webAppJS.mkdirs();
        File webAppPages = new File(webAppRootPathName + File.separator + "WEB-INF" + File.separator + "Pages");
        webAppPages.mkdirs();
        Connect4Application.copyFolder(new File(UIPath + File.separator + "CSS"), webAppCSS);
        Connect4Application.copyFolder(new File(UIPath + File.separator + "Img"), webAppIMG);
        Connect4Application.copyFolder(new File(UIPath + File.separator + "JS"), webAppJS);
        Connect4Application.copyFolder(new File(UIPath + File.separator + "Pages"), webAppPages);
        Connect4Application.copyFileUsingChannel(thisJar, new File(webAppFolder.getPath() + File.separator + "app.jar"));
        Connect4Application.copyFileUsingChannel(new File(Connect4Application.class.getResource("index.html").getPath()), new File(webAppRootPathName + File.separator + "index.html"));
//       Connect4Application.copyFileUsingChannel( new File(Connect4Application.class.getResource("crossdomain.xml").getPath()), new File(webAppFolder.getPath()));
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
    private static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
        if (sourceFolder.isDirectory()) {
            if (!destinationFolder.exists())
                destinationFolder.mkdirs();
            String files[] = sourceFolder.list();
            for (String file : files) {
                File scrFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);
                copyFolder(scrFile, destFile);
                System.out.println("file copiato" + destFile.toPath());
                Connect4Application.copyFileUsingChannel(scrFile, destFile);
            }
        }
    }
}
