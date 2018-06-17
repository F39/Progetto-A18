//package DatabaseManagement;
//
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.dao.DaoManager;
//import com.j256.ormlite.support.ConnectionSource;
//
//import java.sql.SQLException;
//
//public class UserLogRepository implements CrudRepository {
//
//    ConnectionSource connectionSource;
//    Dao<UserLog, String> UserLogDao;
//    UserLog ul;
//
//    public UserLogRepository(ConnectionSource connectionSource) throws SQLException {
//        this.connectionSource = connectionSource;
//        this.UserLogDao = DaoManager.createDao(connectionSource, UserLog.class);
//    }
//
//    @Override
//    public boolean create(User user) throws SQLException {
//
//        UserLogDao.create(ul);
//        ul.setUsername(user.getUsername());
//        UserLogDao.update(ul);
//        UserLogDao.refresh(ul);
//
//        return false;
//    }
//
//    @Override
//    public boolean update(String[] parameters) throws SQLException {
//        int v = 0 , p = 0, s = 0, punti = 0, partite = 0;
//
//        for(UserLog ul : UserLogDao) {
//
//            if(ul.getUsername().equals(parameters[1])) {
//
//                partite = ul.getPartite();
//                partite++;
//                ul.setPartite(partite);
//
//                if (parameters[0] == "vittoria") {
//                    v = ul.getVittorie();
//                    v++;
//                    ul.setVittorie(v);
//                    punti = ul.getPunti();
//                    punti = punti + Integer.parseInt(parameters[2]);
//                    ul.setPunti(punti);
//                    UserLogDao.update(ul);
//                    UserLogDao.refresh(ul);
//                }
//                if (parameters[0] == "pareggio") {
//                    p = ul.getPareggi();
//                    p++;
//                    ul.setPareggi(p);
//                    punti = ul.getPunti();
//                    punti = punti + Integer.parseInt(parameters[2]);
//                    ul.setPunti(punti);
//                    UserLogDao.update(ul);
//                    UserLogDao.refresh(ul);
//                }
//                if (parameters[0] == "sconfitta") {
//                    s = ul.getSconfitte();
//                    s++;
//                    ul.setSconfitte(s);
//                    punti = ul.getPunti();
//                    punti = punti + Integer.parseInt(parameters[2]);
//                    ul.setPunti(punti);
//                    UserLogDao.update(ul);
//                    UserLogDao.refresh(ul);
//                }
//            }
//        }
//        return false;
//    }
//    @Override
//    public boolean delete(String[] parameters) throws SQLException {
//
//        for (UserLog u : UserLogDao) {
//            if (u.getUsername().equals(parameters[0])) {
//                UserLogDao.delete(u);
//            }
//        }
//
//        return false;
//    }
//}
