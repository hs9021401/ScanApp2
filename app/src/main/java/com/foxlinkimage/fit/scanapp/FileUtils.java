package com.foxlinkimage.fit.scanapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Alex on 2015/6/2.
 */
public class FileUtils {
    private static boolean deleteDir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (!deleteDir(file.getAbsolutePath()))
                    return false;
            }
        }
        return dir.delete();
    }

    private static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists())
            file.delete();
    }

    public static ArrayList<File> getFiles(String DirectoryPath) {
        ArrayList<File> MyFiles = new ArrayList<>();
        File f = new File(DirectoryPath);
        if (!f.exists())
            f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            Collections.addAll(MyFiles, files);
        }
        return MyFiles;
    }

    public static void deleteFolder(FolderAdapter folderAdapter)
    {
        for (int i = 0; i < folderAdapter.alSelectedFolder.size(); i++) {
            deleteDir(folderAdapter.alSelectedFolder.get(i));    //刪除縮圖資料夾
            deleteDir(folderAdapter.alSelectedFolder.get(i).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath));    //刪除實際資料夾
            folderAdapter.Delete(folderAdapter.alSelectedFolder.get(i));
        }
    }

    public static void deleteFile(GalleryAdapter galleryAdapter)
    {
        for (int i = 0; i < galleryAdapter.alSelectedPics.size(); i++) {
            deleteFile(galleryAdapter.alSelectedPics.get(i));    //刪除縮圖資料夾
            deleteFile(galleryAdapter.alSelectedPics.get(i).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath));    //刪除實際資料夾
            galleryAdapter.Delete(galleryAdapter.alSelectedPics.get(i));
        }
    }
}
