/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Класс с набором статичных методов работающих с системой ввода/вывода.
 *
 * @author Anatoly
 * @version 0.1.0.3
 */
public final class AIO
{
//---------------------------------------------------------------------------

    private AIO()
    {
    }
//---------------------------------------------------------------------------

    public static final byte[] BOM =
    {
        (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
    };
//---------------------------------------------------------------------------

    public static byte[] readBytesFromInputStream(InputStream in) throws IOException
    {
        if (in == null) throw new IllegalArgumentException("in = null");
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) byteBuffer.write(buf, 0, len);
        byteBuffer.flush();
        return byteBuffer.toByteArray();
    }
//---------------------------------------------------------------------------

    public static byte[] readBytesFromFile(File file) throws IOException
    {
        if (file == null) throw new IllegalArgumentException("file = null");
        try (InputStream inputStream = new FileInputStream(file))
        {
            return readBytesFromInputStream(inputStream);
        }
    }
//---------------------------------------------------------------------------

    public static byte[] readBytesFromFile(String fileName) throws IOException
    {
        if (fileName == null) throw new IllegalArgumentException("fileName = null");

        File file = new File(fileName);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + fileName + "\" not exists");
        if (!file.canRead()) throw new IllegalArgumentException("File \"" + fileName + "\" cannot read");
        return readBytesFromFile(file);
    }
//---------------------------------------------------------------------------

    public static String readStringFromReader(Reader reader) throws IOException
    {
        if (reader == null) throw new IllegalArgumentException("reader = null");
        StringBuilder strBuff = new StringBuilder();
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            strBuff.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return strBuff.toString();
    }
//---------------------------------------------------------------------------

    public static String readStringFromInputStream(InputStream in) throws IOException
    {
        if (in == null) throw new IllegalArgumentException("in = null");
        return new String(removeBOM(readBytesFromInputStream(in)), "UTF-8");
    }
//---------------------------------------------------------------------------

    public static String readStringFromFile(File file) throws IOException
    {
        if (file == null) throw new IllegalArgumentException("file = null");
        try (InputStream inputStream = new FileInputStream(file))
        {
            return readStringFromInputStream(inputStream);
        }
    }
//---------------------------------------------------------------------------

    public static String readStringFromFile(String fileName) throws IOException
    {
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        File file = new File(fileName);
        if (!file.exists()) throw new IllegalArgumentException("File \"" + fileName + "\" not exists");
        if (!file.canRead()) throw new IllegalArgumentException("File \"" + fileName + "\" cannot read");
        return readStringFromFile(file);
    }
//---------------------------------------------------------------------------

    public static String readStringFromURL(URL url) throws IOException
    {
        if (url == null) throw new IllegalArgumentException("url = null");
        InputStream inputStream;
        inputStream = url.openStream();
        String value = readStringFromInputStream(inputStream);
        inputStream.close();
        return value;
    }
//---------------------------------------------------------------------------

    public static String readStringFromURL(String urlName) throws IOException
    {
        if (urlName == null) throw new IllegalArgumentException("urlName = null");
        try
        {
            return readStringFromURL(new URL(urlName));
        }
        catch (MalformedURLException ex)
        {
            throw new IllegalArgumentException("URL \"" + urlName + "\" malformed");
        }
    }
//---------------------------------------------------------------------------

    public static byte[] readBytesFromResource(Object resource, String name, boolean required) throws IOException
    {
        if (name == null) throw new IllegalArgumentException("name = null");
        if (resource == null)
            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(name))
            {
                if (inputStream == null)
                {
                    if (required) throw new IllegalArgumentException("Resource \"" + name + "\" not found");
                    return null;
                }
                return readBytesFromInputStream(inputStream);
            }
        else
            try (InputStream inputStream = resource.getClass().getResourceAsStream(name))
            {
                if (inputStream == null)
                {
                    if (required) throw new IllegalArgumentException("Resource \"" + name + "\" not found");
                    return null;
                }
                return readBytesFromInputStream(inputStream);
            }
    }
//---------------------------------------------------------------------------

    public static String readStringFromResource(Object resource, String name, boolean required) throws IOException
    {
        if (name == null) throw new IllegalArgumentException("name = null");
        byte[] buf = readBytesFromResource(resource, name, required);
        if (buf == null) return null;
        return new String(removeBOM(buf), "UTF-8");
    }
//---------------------------------------------------------------------------
// public static byte[] readBytesFromFile(InputStream in) throws IOException
// {
//  ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//  try(OutputStream bufOStream=new BufferedOutputStream(byteBuffer);)
//  {
//   //use pdfOStream
//   bufOStream.flush();
//  }
//  catch(Exception ex)
//  {
//   ex.printStackTrace(System.err);
//  }
//  return byteBuffer.toByteArray();

//*****
//   ByteArrayOutputStream byteBuffer=new ByteArrayOutputStream();
//   PrintStream ps=new PrintStream(byteBuffer);
//   ex.printStackTrace(ps);
//   ps.close();
//   String str=byteBuffer.toString();
//*****
// }
//---------------------------------------------------------------------------
    public static InputStream openInputStreamFromBytes(byte[] data) throws IOException
    {
        if (data == null) throw new IllegalArgumentException("data = null");
        return new ByteArrayInputStream(data);
    }
//---------------------------------------------------------------------------

    public static InputStream openInputStreamFromString(String data) throws IOException
    {
        if (data == null) throw new IllegalArgumentException("data = null");
        return openInputStreamFromBytes(data.getBytes("UTF-8"));
    }
//---------------------------------------------------------------------------

    public static void writeBytesToOutputStream(byte[] data, OutputStream out) throws IOException
    {
        if (data == null) throw new IllegalArgumentException("data = null");
        if (out == null) throw new IllegalArgumentException("out = null");
        for (byte b : data) out.write(b);
        out.flush();
    }
//---------------------------------------------------------------------------

    public static void writeStringToOutputStream(String text, OutputStream out) throws IOException
    {
        if (text == null) throw new IllegalArgumentException("text = null");
        writeBytesToOutputStream(text.getBytes("UTF-8"), out);
    }
//---------------------------------------------------------------------------

    public static void writeBytesToFile(byte[] data, File file) throws IOException
    {
        if (data == null) throw new IllegalArgumentException("data = null");
        if (file == null) throw new IllegalArgumentException("file = null");
        try (OutputStream out = new FileOutputStream(file))
        {
            writeBytesToOutputStream(data, out);
        }
    }
//---------------------------------------------------------------------------

    public static void writeStringToFile(String text, File file) throws IOException
    {
        if (text == null) throw new IllegalArgumentException("text = null");
        if (file == null) throw new IllegalArgumentException("file = null");
        writeBytesToFile(text.getBytes("UTF-8"), file);
    }
//---------------------------------------------------------------------------

    public static void writeBytesToFile(byte[] data, String fileName) throws IOException
    {
        if (data == null) throw new IllegalArgumentException("data = null");
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        File file = new File(fileName);
        if (!file.exists()) file.createNewFile();
        writeBytesToFile(data, file);
    }
//---------------------------------------------------------------------------

    public static void writeStringToFile(String text, String fileName) throws IOException
    {
        if (text == null) throw new IllegalArgumentException("text = null");
        if (fileName == null) throw new IllegalArgumentException("fileName = null");
        writeBytesToFile(text.getBytes("UTF-8"), fileName);
    }
//---------------------------------------------------------------------------

    public static boolean haveBOM(byte[] buf)
    {
        if (buf == null || buf.length < 3) return false;
        return buf[0] == BOM[0] && buf[1] == BOM[1] && buf[2] == BOM[2];
    }
//---------------------------------------------------------------------------

    public static byte[] removeBOM(byte[] buf)
    {
        if (buf == null) return null;
        if (!haveBOM(buf)) return buf;
        byte b[] = new byte[buf.length - 3];
        for (int q = 0; q < b.length; q++) b[q] = buf[q + 3];
        return b;
    }
//---------------------------------------------------------------------------

    public static byte[] addBOM(byte[] buf)
    {
        if (haveBOM(buf)) return buf;
        byte b[] = new byte[buf.length + 3];
        b[0] = BOM[0];
        b[1] = BOM[1];
        b[2] = BOM[2];
        System.arraycopy(buf, 0, b, 3, buf.length);
        return b;
    }
//---------------------------------------------------------------------------
}
