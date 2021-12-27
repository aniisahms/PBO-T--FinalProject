package finalprojectpbo.database;

import java.sql.*;

public class DBHelper {
    private static final String SQCONN = "jdbc:sqlite:/Users/suharso/Documents/SQLite/Koperasi.sqlite";
    
    public static Connection getConnection() {
        Connection conn;
        
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(SQCONN);
            System.out.println("Database berhasi terhubung");
            createTable(conn);
        }catch (ClassNotFoundException ex) {
            System.out.println(ex);
            System.out.println("Class tidak ditemukan");
            conn = null;
        }catch (SQLException e) {
            System.out.println(e);
            System.out.println("Database tidak berhasi terhubung");
            conn = null;
        }
        
        return conn;
    }

    public static void createTable(Connection connection) {
        String sql = "CREATE TABLE IF NOT EXISTS Nasabah (" +
                "    id_nasabah INT (10)      PRIMARY KEY," +
                "    nama       VARCHAR (50)," +
                "    alamat     VARCHAR (100) " +
                ");" +
                "CREATE TABLE IF NOT EXISTS individual (" +
                "    id_nasabah INT (10) PRIMARY KEY" +
                "                        REFERENCES Nasabah (id_nasabah) ON DELETE RESTRICT" +
                "                                                        ON UPDATE CASCADE," +
                "    nik        INT," +
                "    npwp       INT" +
                ");" +
                "CREATE TABLE IF NOT EXISTS perusahaan (" +
                "    id_nasabah INT (10)     PRIMARY KEY" +
                "                            REFERENCES Nasabah (id_nasabah) ON DELETE RESTRICT" +
                "                                                            ON UPDATE CASCADE," +
                "    nib        VARCHAR (20) " +
                ");" +
                "CREATE TABLE IF NOT EXISTS Rekening (" +
                "    noRekening INT (20) PRIMARY KEY," +
                "    saldo      DOUBLE," +
                "    id_nasabah INT (10) REFERENCES Nasabah (id_nasabah) ON DELETE RESTRICT" +
                "                                                        ON UPDATE CASCADE" +
                ");";
        
        try {
            String[] sqls = sql.split(";");
            for (String sfor : sqls) {
                PreparedStatement preparedStatement = connection.prepareStatement(sfor);
                preparedStatement.execute();
            }
        }catch (SQLException E) {
            System.out.println(E);
        }
        
    }
}
