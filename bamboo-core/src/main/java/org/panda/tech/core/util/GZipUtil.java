package org.panda.tech.core.util;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
    /**
     * 缓冲字节大小
     */
	public static final int BUFFER = 1024;
    public static final String EXT = ".gz";  
  
    /** 
     * 数据压缩
     *  
     * @param data 原始数据
     * @return 压缩后的数据
     * @throws Exception 异常
     */  
    public static byte[] compress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compress(bais, baos);
        byte[] output = baos.toByteArray();
        baos.flush();  
        baos.close();
        bais.close();
        return output;  
    }  
  
    /** 
     * 文件压缩
     *  
     * @param file 原始文件
     * @throws Exception 异常
     */  
    public static void compress(File file) throws Exception {  
        compress(file, true);  
    }  
  
    /** 
     * 文件压缩
     *  
     * @param file 原始文件
     * @param delete 压缩后是否删除
     * @throws Exception 异常
     */  
    public static void compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);
        compress(fis, fos);
        fis.close();  
        fos.flush();  
        fos.close();
        if (delete) {  
            file.delete();  
        }  
    }  
  
    /** 
     * 字节流压缩
     *  
     * @param is 字节输入流
     * @param os 字节输出流
     * @throws Exception 异常
     */  
    public static void compress(InputStream is, OutputStream os)  
            throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;  
        byte[] data = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {  
            gos.write(data, 0, count);  
        }
        gos.finish();
        gos.flush();  
        gos.close();  
    }  
  
    /** 
     * 文件路径压缩
     *  
     * @param path 路径
     * @throws Exception 异常
     */  
    public static void compress(String path) throws Exception {  
        compress(path, true);  
    }  
  
    /** 
     * 文件路径压缩
     *  
     * @param path 路径
     * @param delete 是否删除
     * @throws Exception 异常
     */  
    public static void compress(String path, boolean delete) throws Exception {  
        File file = new File(path);  
        compress(file, delete);  
    }  
  
    /** 
     * 数据解压缩
     *  
     * @param data 原始数据
     * @return 解压后的数据
     * @throws Exception 异常
     */  
    public static byte[] decompress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress(bais, baos);
        data = baos.toByteArray();
        baos.flush();  
        baos.close();
        bais.close();
        return data;  
    }  
  
    /** 
     * 文件解压缩
     *  
     * @param file 文件
     * @throws Exception 异常
     */  
    public static void decompress(File file) throws Exception {  
        decompress(file, true);  
    }  
  
    /** 
     * 文件解压缩
     *  
     * @param file 文件解压缩
     * @param delete 
     *            是否删除
     * @throws Exception 异常
     */  
    public static void decompress(File file, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT,  
                ""));  
        decompress(fis, fos);  
        fis.close();  
        fos.flush();  
        fos.close();  
  
        if (delete) {  
            file.delete();  
        }  
    }  

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);
        int count;  
        byte[] data = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {  
            os.write(data, 0, count);  
        }
        gis.close();  
    }  

    public static void decompress(String path) throws Exception {  
        decompress(path, true);  
    }  

    public static void decompress(String path, boolean delete) throws Exception {  
        File file = new File(path);  
        decompress(file, delete);  
    }  
}
