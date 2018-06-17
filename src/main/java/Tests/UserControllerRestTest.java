package Tests;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class UserControllerRestTest implements Runnable {

    private Tomcat tomcat;

    public UserControllerRestTest(Tomcat tomcat) {
        this.tomcat = tomcat;
    }

    @Override
    public void run() {
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

}
