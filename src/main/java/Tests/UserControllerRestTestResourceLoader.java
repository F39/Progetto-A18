package Tests;

import Controllers.UserController;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class UserControllerRestTestResourceLoader extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(UserController.class);
        return classes;
    }
}
