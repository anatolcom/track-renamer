/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.file;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Anatol
 */
public class FileFinder
{
    private final File path;
    private final OnFile found;
    private boolean subdir = true;
    private FileFilter filter;

    public FileFinder(final File path, final OnFile found)
    {
        this.path = path;
        this.found = found;
    }

    public FileFinder(final String path, final OnFile found)
    {
        this.path = new File(path);
        this.found = found;
    }

    public FileFinder subdir(boolean value)
    {
        this.subdir = value;
        return this;
    }

    public FileFinder filter(FileFilter filter)
    {
        this.filter = filter;
        return this;
    }

    public void find()
    {
        findFiles(path);
    }

    private void findFiles(File path)
    {
        if (subdir)
        {
            File dirList[] = path.listFiles(f -> f.isDirectory());
            for (File dir : dirList) findFiles(dir);
        }
        File fileList[] = path.listFiles(filter);
        for (File file : fileList) if (file.isFile()) found.on(file);
    }

}
