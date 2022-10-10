package com.labrace.torontoso.service;

import android.app.Activity;

import com.labrace.torontoso.bean.Image;
import com.labrace.torontoso.bean.Patient;
import com.labrace.torontoso.bean.User;
import com.labrace.torontoso.common.Constants;
import com.labrace.torontoso.common.LoginStatus;
import com.labrace.torontoso.database.DatabaseHelper;
import com.labrace.torontoso.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by Dung on 4/7/2016.
 */
public class TorsoCADService {
    public static LoginStatus logIn(Activity activity, String userName, String password) {
        try {
            User user = DatabaseHelper.getUser(userName, password);
            List<User> users = DatabaseHelper.getUsers();
            if(user.getUserId() == 0 || user.getUserName() == null) {
                return LoginStatus.INVALID_USERNAME_OR_PASSWORD;
            } else if(user.isDisabled()) {
                return LoginStatus.ACCOUNT_DISABLED;
            }

            DatabaseHelper.addNewLogin(user.getUserId());
            PreferencesUtils.saveData(activity, Constants.USER_ID, String.valueOf(user.getUserId()));
            PreferencesUtils.saveData(activity, Constants.USER_NAME, String.valueOf(user.getUserName()));
        } catch (Exception e) {
            e.printStackTrace();
            return LoginStatus.EXCEPTION;
        }

        return LoginStatus.SUCCESS;
    }

    public static void logOut(Activity activity) {
        PreferencesUtils.saveData(activity, Constants.USER_ID, null);
        PreferencesUtils.saveData(activity, Constants.USER_NAME, null);
    }

    public static List<Patient> loadPatientsForUser(int userId) {
        try {
            List<Patient> patients = DatabaseHelper.getPatientsByUserId(userId);
            return patients;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getTorsoIdByPatientId(int patientId) {
        try {
            int torsoId = DatabaseHelper.getTorsoIdByPatientId(patientId);
            return torsoId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean addNewPatient(Patient patient) {
        try {
            DatabaseHelper.addNewPatient(patient);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addNewMeasurement(Patient patient) {
        try {
            DatabaseHelper.addNewMeasurement(patient);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void loadImagesForPatient(Patient patient) {
        try {
            List<Image> images = DatabaseHelper.getImagesByPatientId(patient.getId());
            patient.setImages(images);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addNewImage(Image image) {
        try {
            DatabaseHelper.addNewImage(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteImage(Image image) {
        try {
            DatabaseHelper.deleteImage(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
