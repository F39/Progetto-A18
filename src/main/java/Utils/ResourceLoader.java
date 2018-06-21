package Utils;

import Controllers.RestGameController;
import Controllers.RestStatsController;
import Controllers.UserController;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class ResourceLoader extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();

        // register root resource
        classes.add(UserController.class);
        classes.add(RestGameController.class);
        classes.add(RestStatsController.class);
        return classes;
    }
}