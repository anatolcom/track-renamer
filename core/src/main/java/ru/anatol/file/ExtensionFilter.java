/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import static ru.anatol.file.FileUtils.getExt;

/**
 *
 * @author Anatol
 */
public class ExtensionFilter implements FileFilter
{
    private final Set<String> extensions;

    public ExtensionFilter(final Set<String> extensions)
    {
        this.extensions = extensions;
    }

    @Override
    public boolean accept(File file)
    {
        if (file == null && !file.isFile()) return false;
        if (extensions == null) return true;
        String ext = getExt(file.getName());
        if (ext == null) return false;
        for (String mask : extensions) if (ext.equalsIgnoreCase(mask)) return true;
        return false;
    }
};
