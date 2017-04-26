/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Anatol
 * @version 0.1.0.0
 */
public final class FileUtils
{
//---------------------------------------------------------------------------

    private FileUtils()
    {
    }
//---------------------------------------------------------------------------

    /**
     * Метод создаёт новый файл. Если файл с указанным именем уже существует, то
     * происходит исключение.<br />
     *
     * @param fileName имя файла
     * @return ссылка на файл
     * @throws AException в случае ошибки
     */
    public static File createFile(String fileName) throws IOException
    {
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        File file = new File(fileName);
        if (file.exists()) throw new IllegalArgumentException("File \"" + fileName + "\" alredy exists");
        if (!file.createNewFile()) throw new IllegalArgumentException("File \"" + fileName + "\" not created");
//        info("createFile", "\"" + fileName + "\"");
        return file;
    }
//---------------------------------------------------------------------------

    /**
     * Метод возвращает ссылку на файл. Если файла с указанным именем не
     * существует, то происходит исключение.<br />
     *
     * @param fileName имя файла
     * @return ссылка на файл
     * @throws AException в случае ошибки
     */
    public static File openFile(String fileName)
    {
//        warning("openFile", "This function is not developed");
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        File file = new File(fileName);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + fileName + "\" not exists");
//        info("openFile", "\"" + fileName + "\"");
        return file;
    }
//---------------------------------------------------------------------------

    /**
     * Метод возвращает ссылку на файл. Если файла с указанным именем не
     * существует, то он создаётся.<br />
     *
     * @param fileName
     * @return
     * @throws AException
     */
    public static File openOrCreateFile(String fileName) throws IOException
    {
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        File file = new File(fileName);
        if (!file.exists()) if (!file.createNewFile()) throw new IllegalArgumentException("File \"" + fileName + "\" not created");
//            info("openOrCreateFile", "\"" + fileName + "\"");
        return file;
    }
//---------------------------------------------------------------------------

    /**
     * Метод удаляет файл. Если файла с указанным именем не существует, то
     * происходит исключение.<br />
     *
     * @param fileName
     * @throws AException
     */
    public static void deleteFile(String fileName)
    {
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        File file = new File(fileName);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + fileName + "\" not exists");
        if (!file.delete()) throw new IllegalArgumentException("File \"" + fileName + "\"don't delete");
//        info("deleteFile", "\"" + fileName + "\"");
    }
//---------------------------------------------------------------------------

    /**
     * Метод создаёт папку и все промежуточные папки по указанному пути.<br />
     *
     * @param dirName путь
     * @return ссылка на папку
     * @throws AException
     */
    public static File forceDir(String dirName)
    {
        if (dirName == null) throw new IllegalArgumentException("dirName = null");
        File dir = new File(dirName);
        if (!dir.mkdirs()) throw new IllegalArgumentException("Dir \"" + dirName + "\" don't create");
//        info("forceDir", "\"" + dirName + "\"");
        return dir;
    }
//---------------------------------------------------------------------------

    public static void deleteEmptyDir(String dirName)
    {
//        warning("deleteEmptyDir", "This function is not developed");
        if (dirName == null) throw new IllegalArgumentException("dirName = null");
        File dir = new File(dirName);
        if (!dir.exists()) throw new IllegalArgumentException("Dir \"" + dirName + "\" not exists");

//        info("deleteEmptyDir", "\"" + dirName + "\"");
    }
//---------------------------------------------------------------------------

    public static void deleteDirAndContent(String dirName)
    {
//        warning("deleteDirAndContent", "This function is not developed");
        if (dirName == null) throw new IllegalArgumentException("dirName = null");
        File dir = new File(dirName);
        if (!dir.exists()) return;
        deleteDirContent(dir);
//        info("deleteDirAndContent", "Dir:" + dir.getTitle());
        dir.delete();
//        info("deleteDirAndContent", "\"" + dirName + "\"");
    }
//---------------------------------------------------------------------------

    private static void deleteDirContent(File dir)
    {
        File fileList[] = dir.listFiles();
        for (File file : fileList)
        {
            if (file.isDirectory()) deleteDirContent(file);
//            info("deleteDirContent", file.getTitle());
            file.delete();
        }
    }
//---------------------------------------------------------------------------

    public static List<String> getFilePaths(String pathName, boolean subdir, final Set<String> exts)
    {
        List<String> filePaths = new LinkedList<>();
        File path = new File(pathName);
        processFilePach(path, subdir, exts, filePaths);
        return filePaths;
    }
//---------------------------------------------------------------------------

    private static void processFilePach(File path, boolean subdir, final Set<String> exts, List<String> filePaths)
    {
        if (subdir)
        {
            File pathList[] = path.listFiles(f -> f.isDirectory());
            for (File p : pathList) processFilePach(p, subdir, exts, filePaths);
        }
        File fileList[] = path.listFiles(new ExtensionFilter(exts));
        for (File file : fileList) if (file.isFile()) filePaths.add(file.getPath());
    }
//---------------------------------------------------------------------------

    public static void findFiles(File currentPath, boolean subdir, final Set<String> exts, OnFile found)
    {
        if (subdir)
        {
            File pathList[] = currentPath.listFiles(f -> f.isDirectory());
            for (File path : pathList) findFiles(path, subdir, exts, found);
        }
        File fileList[] = currentPath.listFiles(new ExtensionFilter(exts));
        for (File file : fileList) if (file.isFile()) found.on(file);
    }
//---------------------------------------------------------------------------

    public static List<String> getDirPathList(String pathName, boolean subdir)
    {
        List<String> dirPathList = new ArrayList();
        File path = new File(pathName);
        if (path == null) throw new IllegalArgumentException("path \"" + pathName + "\" not found");
        processDirPach(path, subdir, dirPathList);
        return dirPathList;
    }
//---------------------------------------------------------------------------

    private static void processDirPach(File path, boolean subdir, List<String> filePaths)
    {
        if (path == null) return;
        File list[] = path.listFiles(f -> f.isDirectory());
        if (list == null) return;
        if (subdir) for (File item : list) processDirPach(item, subdir, filePaths);
        for (File item : list) filePaths.add(item.getPath());
    }
//---------------------------------------------------------------------------

    public static String getExt(String fileName)
    {
        String ext = "";
        char buf[] = fileName.toCharArray();
        int index = buf.length - 1;
        while (buf[index] != '.')
        {
            if (index <= 0) return null;
            ext = buf[index] + ext;
            index--;
        }
        return ext;
    }
//---------------------------------------------------------------------------

    /**
     * Метод разделяет имя файла на путь, имя и расширение. Если какая либо
     * часть имени файла не определена, она заменяется пустой сторкой. <br />
     *
     * @param fileName имя файла
     * @return массив, в котором [0] - путь, [1] - имя, [2] - расширение.
     */
    public static String[] splitFileName(String fileName)
    {
        String[] list =
        {
            "", "", ""
        };//new String[3];
        int s = fileName.lastIndexOf('/');
        if (s == -1) s = fileName.lastIndexOf('\\');
        if (s != -1)
        {
            list[0] = fileName.substring(0, s);
            fileName = fileName.substring(s + 1);
        }
        s = fileName.lastIndexOf('.');
        if (s != -1)
        {
            list[1] = fileName.substring(0, s);
            list[2] = fileName.substring(s + 1);
        }
        else list[1] = fileName;
        return list;
    }
//---------------------------------------------------------------------------
}
