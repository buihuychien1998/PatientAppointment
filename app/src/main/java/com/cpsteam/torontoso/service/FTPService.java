package com.cpsteam.torontoso.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cpsteam.torontoso.utils.CommonUtils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dung on 4/7/2016.
 */
public class FTPService {
    private static final String FTP_HOST = "74.124.24.19";
    private static final String FTP_USER = "torsocad";
    private static final String FTP_PASSWORD = "Tcad644";

    private FTPClient mFTPClient;

    public FTPService(String path) throws IOException {
        setWorkingDirectory(path);
    }

    private void setWorkingDirectory(String path) throws IOException {
        mFTPClient = new FTPClient();
        mFTPClient.connect(FTP_HOST);
        if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
            mFTPClient.login(FTP_USER, FTP_PASSWORD);
            mFTPClient.enterLocalPassiveMode();
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            String fullPath = "images/" + path;
            String[] directories = fullPath.split("/");
            for (String directory: directories) {
                if(!checkDirectoryExists(directory)) {
                    mFTPClient.makeDirectory(directory);
                    mFTPClient.changeWorkingDirectory(directory);
                }
            }
        }
    }

    public Bitmap loadImage(String imageName) throws IOException {
        InputStream inStream = mFTPClient.retrieveFileStream(imageName);
        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
        mFTPClient.completePendingCommand();
        inStream.close();
        mFTPClient.logout();
        mFTPClient.disconnect();
        return bitmap;
    }

    public List<String> getImageLst() throws IOException {
        String[] listNames = mFTPClient.listNames();
        mFTPClient.logout();
        mFTPClient.disconnect();
        return Arrays.asList(listNames);
    }

    public boolean uploadImage(String imagePath) throws IOException {
        File image = new File(imagePath);
        Bitmap bitmap = CommonUtils.decodeFile(image);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        boolean isUploaded = mFTPClient.storeFile(image.getName(), inputStream);
        inputStream.close();
        mFTPClient.logout();
        mFTPClient.disconnect();
        return isUploaded;
    }

    public boolean deleteImage(String imageName) throws IOException {
        boolean result = mFTPClient.deleteFile(imageName);
        mFTPClient.logout();
        mFTPClient.disconnect();
        return result;
    }

    /**
     * Determines whether a directory exists or not
     * @param dirPath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    public boolean checkDirectoryExists(String dirPath) throws IOException {
        mFTPClient.changeWorkingDirectory(dirPath);
        int returnCode = mFTPClient.getReplyCode();
        if (returnCode == 550) {
            return false;
        }
        return true;
    }
}
