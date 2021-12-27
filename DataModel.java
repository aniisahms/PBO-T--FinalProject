package finalprojectpbo.database;

import finalprojectpbo.Individu;
import finalprojectpbo.Perusahaan;
import finalprojectpbo.Rekening;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.sql.*;



public class DataModel {
    public final Connection conn ;

    public DataModel() {
        this.conn = DBHelper.getConnection();
    }

    public void addIndvidual(Individu individu) {
        try {
            String insertNasabah = "INSERT INTO Nasabah (id_nasabah,nama,alamat) "
                    + "VALUES (?,?,?)";
            
            String insertIndividual = "INSERT INTO Individual (id_nasabah,nik,npwp)"
                    + " VALUES (?,?,?)";
            
            String insertRekening = "INSERT INTO Rekening (noRekening,saldo,id_nasabah)"
                    + " VALUES (?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(insertNasabah);
            preparedStatement.setInt(1,individu.getId());
            preparedStatement.setString(2,individu.getNama());
            preparedStatement.setString(3,individu.getAlamat());
            preparedStatement.execute();
            preparedStatement.close();

            PreparedStatement preparedIndividual = conn.prepareStatement(insertIndividual);
            preparedIndividual.setInt(1,individu.getId());
            preparedIndividual.setLong(2,individu.getNik());
            preparedIndividual.setLong(3,individu.getNpwp());
            preparedIndividual.execute();
            preparedIndividual.close();

            PreparedStatement preparedRekening = conn.prepareStatement(insertRekening);
            preparedRekening.setInt(1,individu.getRekening().get(0).getNoRekening());
            preparedRekening.setDouble(2,individu.getRekening().get(0).getSaldo());
            preparedRekening.setInt(3,individu.getId());
            preparedRekening.execute();
            preparedRekening.close();
        }catch (SQLException X) {
            System.out.println(X);
        }
    }
    
    public void addPerusahaan(Perusahaan perusahaan) {
        try {
            String insertNasabah = "INSERT INTO Nasabah(id_nasabah,nama,alamat) "
                    + "VALUES" + "(?,?,?)";
            
            String insertPerusahaan = "INSERT INTO Perusahaan(id_nasabah,nib) "
                    + "VALUES " + "(?,?)";
            
            String insertRekening = "INSERT INTO Rekening(noRekening,saldo,id_nasabah) "
                    + "VALUES " + "(?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(insertNasabah);
            preparedStatement.setInt(1,perusahaan.getId());
            preparedStatement.setString(2,perusahaan.getNama());
            preparedStatement.setString(3,perusahaan.getAlamat());
            preparedStatement.execute();
            preparedStatement.close();

            PreparedStatement preparedPerusahaan = conn.prepareStatement(insertPerusahaan);
            preparedPerusahaan.setInt(1,perusahaan.getId());
            preparedPerusahaan.setString(2,perusahaan.getNib());
            preparedPerusahaan.execute();
            preparedPerusahaan.close();

            PreparedStatement preparedRekening = conn.prepareStatement(insertRekening);
            preparedRekening.setInt(1,perusahaan.getRekening().get(0).getNoRekening());
            preparedRekening.setDouble(2,perusahaan.getRekening().get(0).getSaldo());
            preparedRekening.setInt(3,perusahaan.getId());
            preparedRekening.execute();
            preparedRekening.close();
        }catch (SQLException X) {
            System.out.println(X);
        }
    }
    
    public void addRekening(int holderId, Rekening rek) throws SQLException {
        String insertNasabah = "INSERT INTO Rekening (id_nasabah, noRekening, saldo)"
                             + " VALUES (?, ?, ?)";

        PreparedStatement preparedRekening = conn.prepareStatement(insertNasabah);
        preparedRekening.setInt(1, holderId);
        preparedRekening.setInt(2, rek.getNoRekening());
        preparedRekening.setDouble(3, rek.getSaldo());
        preparedRekening.execute();
    }
    
    public ObservableList<Individu> getIndividu() {
        ObservableList<Individu> data = FXCollections.observableArrayList();
        String sql = "SELECT id_nasabah, nama, alamat, nik, npwp " +
                     "FROM Nasabah NATURAL JOIN Individual " +
                     "ORDER BY nama";
        
        try {
           ResultSet resultSet = conn.createStatement().executeQuery(sql);
           
           while (resultSet.next()) {
               String selectRekening = "SELECT noRekening, saldo, id_nasabah " +
                                       "FROM Rekening WHERE id_nasabah = " + resultSet.getInt(1);
               
               ResultSet rsRekening = conn.createStatement().executeQuery(selectRekening);
               ArrayList<Rekening> rekenings = new ArrayList<>();
               
               while (rsRekening.next()) {
                   rekenings.add(new Rekening(rsRekening.getInt(1),rsRekening.getDouble(2)));
               }
               
               data.add(new Individu(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),
                       resultSet.getLong(4),resultSet.getLong(5),rekenings ));
           }
       }catch (SQLException throwables) {
           throwables.printStackTrace();
       }
        
        return data;
    }
    
    public ObservableList<Perusahaan> getPerusahaan(){
        ObservableList<Perusahaan> data = FXCollections.observableArrayList();
        
        try{
            String sql = "SELECT id_nasabah, nama, alamat, nib " + 
                         "FROM Nasabah NATURAL JOIN Perusahaan " +
                         "ORDER BY nama";
            
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            
            while (resultSet.next()) {
                String selectRekening = "SELECT noRekening, saldo, id_nasabah " +
                                        "FROM Rekening WHERE id_nasabah = " + resultSet.getInt(1);
                
                ResultSet rsRekening = conn.createStatement().executeQuery(selectRekening);
                ArrayList<Rekening> rekenings = new ArrayList<>();
                
                while (rsRekening.next()) {
                    rekenings.add(new Rekening(rsRekening.getInt(1),rsRekening.getDouble(2)));
                }
                
                data.add(new Perusahaan(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                ,resultSet.getString(4), rekenings));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        return data;
    }
    
    public ObservableList<Rekening>getRekening(int ID) {
        ObservableList<Rekening> data = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT noRekening, saldo " +
                         "FROM Rekening " +
                         "WHERE id_nasabah = " + ID;
            ResultSet resultSet  = conn.createStatement().executeQuery(sql);
            
            while (resultSet.next()){
                data.add(new Rekening(resultSet.getInt(1),resultSet.getDouble(2)));
            }
            
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        return data;
    }

    public int nextNasabahID() throws SQLException {
        String sql = "SELECT MAX (id_nasabah) FROM Nasabah";
        ResultSet resultSet  = conn.createStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            return resultSet.getInt(1) == 0 ? 1000001 : resultSet.getInt(1) + 1;
        }
        
        return 1000001;
    }

    public int nextNoRekening(int ID) {
        try {
            String sql = "SELECT MAX (noRekening) FROM Rekening WHERE id_nasabah = " + ID;
            
            ResultSet resultSet  = conn.createStatement().executeQuery(sql);
            
            while (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            }
            
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        return 0;
    }
}
