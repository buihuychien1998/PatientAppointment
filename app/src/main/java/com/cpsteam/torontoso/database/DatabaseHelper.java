package com.cpsteam.torontoso.database;

import android.os.StrictMode;
import android.util.Log;

import com.cpsteam.torontoso.bean.Image;
import com.cpsteam.torontoso.bean.Patient;
import com.cpsteam.torontoso.bean.User;
import com.cpsteam.torontoso.common.SexType;
import com.cpsteam.torontoso.utils.CommonUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dung on 4/21/2016.
 */
public class DatabaseHelper {
    private static final String DATABASE_SERVER_ADDRESS = "sql2004.shared-servers.com:1088";
    private static final String DATABASE_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    private static final String DATABASE_NAME = "403480_OPHUB";
    private static final String DATABASE_USER = "403480opMaster";
    private static final String DATABASE_PASSWORD = "ZAQmaster1";

    public static Connection getConnection() throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionUrl = null;
        try {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                    + DATABASE_SERVER_ADDRESS + "/" + DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            throw e;
        }
        return connection;
    }

    public static User getUser(String userName, String password) throws Exception {
        User user = new User();

        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM users WHERE loginname = ? AND loginpassword = ?;");
            statement.setString(1, userName);
            statement.setString(2, password);
            rs = statement.executeQuery();

            if (rs.next()) {
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("UserName"));
                user.setDisabled(rs.getBoolean("Disabled"));
            }
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return user;
    }

    public static List<User> getUsers() throws Exception {
        List<User> users = new ArrayList<>();

        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM users;");
            rs = statement.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("LogInName"));
                user.setPassWord(rs.getString("LogInPassword"));
                users.add(user);
            }
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return users;
    }

    public static void addNewLogin(int userId) throws Exception {
        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("INSERT INTO logIns (LoginDateTime, UserID) VALUES (?, ?);");
            statement.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<Patient> getPatientsByUserId(int userId) throws Exception {
        List<Patient> patients = new ArrayList<>();

        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT PtID, PtLName, PtFName, DOB, Sex, Race, UserID, BraceClass, Hypokyphosis, Service, EntryDate FROM Patients Where userID = ? ORDER BY PtLName, PtFName, PtID;");
            statement.setInt(1, userId);
            rs = statement.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("PtID"));
                patient.setLastName(rs.getString("PtLName"));
                patient.setFirstName(rs.getString("PtFName"));
                Date dob = rs.getDate("DOB");
                if(dob != null) {
                    patient.setDob(CommonUtils.convertDateToString(rs.getDate("DOB")));
                }
                patient.setSex(SexType.fromCharacter(rs.getString("Sex")).name());
                patient.setRace(rs.getString("Race"));
                patient.setUserId(rs.getInt("UserID"));
                patient.setBraClass(rs.getString("BraceClass"));
                patient.setHyphoKypho(rs.getInt("Hypokyphosis"));
                patient.setService(rs.getInt("Service"));
                Date entryDate = rs.getDate("EntryDate");
                if(entryDate != null) {
                    patient.setEntryDate(CommonUtils.convertDateToString(rs.getDate("EntryDate")));
                }
                patients.add(patient);
            }
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return patients;
    }

    public static void addNewPatient(Patient patient) throws Exception {
        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("INSERT INTO Patients (PtLName, PtFName, DOB, Sex, Race, UserID, BraceClass, Hypokyphosis, Service, EntryDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, patient.getLastName());
            statement.setString(2, patient.getFirstName());
            statement.setDate(3, CommonUtils.convertStringToDate(patient.getDob()));
            statement.setString(4, patient.getSex());
            statement.setString(5, patient.getRace());
            statement.setInt(6, patient.getUserId());
            statement.setString(7, patient.getBraClass());
            statement.setInt(8, patient.getHyphoKypho());
            statement.setInt(9, patient.getService());
            statement.setDate(10, new Date(Calendar.getInstance().getTimeInMillis()));
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                patient.setId(newId);
            }
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void addNewMeasurement(Patient patient) throws Exception {
        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Date dob = CommonUtils.convertStringToDate(patient.getDob());
            Date now = new Date(Calendar.getInstance().getTimeInMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            int yearNow = cal.get(Calendar.YEAR);
            cal.setTime(dob);
            int yearOb = cal.get(Calendar.YEAR);
            int age = yearNow - yearOb;

            statement = connection.prepareStatement("INSERT INTO TorsoMeas (PtID,RequestDate , MDate, Age) VALUES (?,?, ?, ?);");
            statement.setInt(1, patient.getId());
            statement.setDate(2, now);
            statement.setDate(3, now);
            statement.setInt(4, age);
            statement.executeUpdate();
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<Image> getImagesByPatientId(int patientId) throws Exception {
        List<Image> images = new ArrayList<>();

        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT ti.ImageID, ti.TorsoID, ti.ImagePath FROM TorsoImages AS ti INNER JOIN TorsoMeas AS tm ON ti.TorsoID=tm.TorsoID WHERE tm.PtID=? ORDER BY ImageID DESC;");
            statement.setInt(1, patientId);
            rs = statement.executeQuery();

            while (rs.next()) {
                Image image = new Image();
                image.setImageId(rs.getInt("ImageID"));
                image.setTorsoId(rs.getInt("TorsoID"));
                image.setImagePath(rs.getString("ImagePath"));
                images.add(image);
            }
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return images;
    }

    public static int getTorsoIdByPatientId(int patientId) throws Exception {
        int torsoId = -1;

        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT TorsoID FROM TorsoMeas WHERE PtID=?;");
            statement.setInt(1, patientId);
            rs = statement.executeQuery();

            while (rs.next()) {
                torsoId = rs.getInt("TorsoID");
            }

            return torsoId;
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void addNewImage(Image image) throws Exception {
        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("INSERT INTO TorsoImages (TorsoID, ImagePath) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, image.getTorsoId());
            statement.setString(2, image.getImagePath());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                image.setImageId(newId);
            }
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void deleteImage(Image image) throws Exception {
        Connection connection = getConnection();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("DELETE FROM TorsoImages WHERE ImageID = ?;");
            statement.setInt(1, image.getImageId());
            statement.executeUpdate();
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
            throw se;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
