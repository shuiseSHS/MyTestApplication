package com.example.scantool;

/**
 * Created by shisong on 2017/3/3.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClearUnUsedJar {

    private static boolean REAL_DEL = false; // 是否真的删除文件

    private final static boolean DEL_JAVA = true; // 是否删除java文件

    private final static boolean NEED_READ_JAR = true; // 是否需要扫描jar

    private final static boolean FULL_LOG = false;// 是否显示具体Log

    public final static String ROOT_DIR = "F:/qiyi_git/qiyivideo/";

    public final static String[] EXCLUDE_DIR = {
            ".git",
            ".gitignore",
            ".gradle",
            ".idea",
            ".navigation",
            "BaiduWalletSDKLib",
            "qywallet",
            "build",
            "gradle",
            "Networklib",
            "tools",
            "WebView",
            "android_support",
            "appstore"
    };

    public final static String[] TO_CLEAR_PROJECTS = {
            "QYVideoClient",
//		"CardView",
//		"VideoPlayer",
//		"QYPaySDK",
//		"PlayerLogicSDK",
//		"Download",
//		"QYPassportSDK",
//		"QYPlayerCardExtraView",
//		"Controllerlayer",
//		"QYCoreJar",
//		"Coreplayer",
//		"QYBaseCardView",
//		"QYBaseCore",
//		"QYCardView",
//		"QYCommonCardView",
//		"QYPlayerCardView"
    };

    public static List<String> TO_CLEAR = new ArrayList<>();

    private final static String[] JAR_DIRS = {"/libs/"};

    // 保存所有代码，以文件名做key
    private final static HashMap<String, String> codeMap = new HashMap<String, String>();

    private static long totalBytes = 0;
    private static long delXMLBytes = 0;
    private static long delIMGBytes = 0;
    private static int jarNum = 0;
    private static int delJarNum = 0;
    private static int jarBytes = 0;

    public static void main(String[] args) {
        // 加载所有代码到内存中
        loadCode();
        System.out.println("JARNUM: " + jarNum + ", bytes: " + jarBytes);

        if (!REAL_DEL) {
            totalBytes = 0;
            delXMLBytes = 0;
            delIMGBytes = 0;
        }

        for (String p : TO_CLEAR) {
            for (String dir : JAR_DIRS) {
                File f = new File(ROOT_DIR + p + dir);
                clearJar(f);
            }
        }
//        File f = new File(ROOT_DIR + "Download/libs/");
//        clearJar(f);
        System.out.println("可以删除的数量：" + delJarNum);
    }

    // 加载所有代码（包括xml和java）
    private static void loadCode() {
//        codeMap.clear();
        File root = new File(ROOT_DIR);
        for (File project : root.listFiles()) {
            if (project.isFile()) {
                continue;
            }
            boolean exclude = false;
            for (String s : EXCLUDE_DIR) {
                if (s.equals(project.getName())) {
                    exclude = true;
                    break;
                }
            }
            if (exclude) {
                continue;
            }
            System.out.println(project.getAbsolutePath());
            TO_CLEAR.add(project.getName());

            File dir;
            dir = new File(project, "/src");
            loadDirCode(dir);
//
//            dir = new File(project, "/res");
//            loadDirCode(dir);

            if (NEED_READ_JAR) {
                dir = new File(project, "/libs");
                loadContantsInJar(dir);
            }

//            loadFileCode(new File(project, "/AndroidManifest.xml"));
        }
//        System.out.println("初始文件总数：" + codeMap.size());
//        System.out.println("代码字节数：" + totalBytes);
    }

    private static void logD(File f) {
        System.out.println("D: " + f.getAbsolutePath().substring(12));
    }

    // 只加载jar包中String类型的常量
    private static void loadContantsInJar(File f) {
        if (f == null || !f.exists()) {
            return;
        }
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                loadContantsInJar(file);
            }
        } else {
            if (f.getName().endsWith(".jar")) {
                readJar(f);
            }
        }
    }

    private static void readJar(File f) {
        System.out.println(f.getAbsolutePath());
        jarNum++;
        jarBytes += f.length();

        JarFile jarfile = null;
        StringBuilder sb = new StringBuilder();
        try {
            jarfile = new JarFile(f);
            Enumeration<JarEntry> entryList = jarfile.entries();
            while (entryList.hasMoreElements()) {
                JarEntry jarentry = entryList.nextElement();
                if (!jarentry.getName().endsWith(".class")) {
                    continue;
                }
                try {
                    InputStream is = jarfile.getInputStream(jarentry);
                    readContantsInClass(is, sb);
                    is.close();
                } catch (IOException e) {
                }
            }
            jarfile.close();
        } catch (IOException e) {
        }
        codeMap.put(f.getAbsolutePath(), sb.toString());
    }

    private static void clearJar(File dir) {
        if (codeMap.size() == 0) {
            System.err.println("textMap没有初始化");
        }
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File f : dir.listFiles()) {
            String fileName = f.getName();
            boolean hasUsed = false;
            if (fileName.endsWith(".jar")) {
                System.out.println("xxxxxxxxxxxxxx：" + f.getAbsolutePath());
                JarFile jarfile = null;
                StringBuilder sb = new StringBuilder();
                try {
                    jarfile = new JarFile(f);
                    Enumeration<JarEntry> entryList = jarfile.entries();
                    while (entryList.hasMoreElements()) {
                        JarEntry jarentry = (JarEntry) entryList.nextElement();
                        if (!jarentry.getName().endsWith(".class")) {
                            continue;
                        }

                        String className = jarentry.getName();
                        className = className.replace("/", ".");
                        className = className.replace(".class", "");
                        if (containsString(f, className)) {
                            hasUsed = true;
                            break;
                        }
                    }
                    jarfile.close();
                } catch (IOException e) {
                }
                if (hasUsed) {
                    continue;
                } else {
                    delJarNum ++;
                    System.out.println("可以删除的：" + f.getAbsolutePath());
                }
            }
        }
    }

    // 查找指定字符串
    private static boolean containsString(File currentFile, String className) {
        String currentFilePath = currentFile.getAbsolutePath();
        String currentName = className;
        String fileName = currentName.substring(0, currentName.indexOf("."));

        int c = 0;
        for (String filePath : codeMap.keySet()) {
            if (filePath.equals(currentFilePath)) {
                continue;
            }
            String text = codeMap.get(filePath);
            if (text.contains(fileName)) {
                System.out.println(filePath + "有：" + className);
                c++;
                break;
            }
        }

        return c > 0;
    }

    private static void readContantsInClass(InputStream is, StringBuilder sb) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int magic = 0xCAFEBABE;
        if (!(magic == dis.readInt())) {
            dis.close();
            return;
        }

        dis.readShort(); //minor_version
        dis.readShort();//major_version
        int constant_pool_count = dis.readShort();

	/*		常量池中数据项类型		类型标志 	类型描述
            CONSTANT_Utf8				1		UTF-8编码的Unicode字符串
			CONSTANT_Integer			3		int类型字面值
			CONSTANT_Float				4		float类型字面值
			CONSTANT_Long				5		long类型字面值
			CONSTANT_Double				6		double类型字面值
			CONSTANT_Class				7		对一个类或接口的符号引用
			CONSTANT_String	            8		String类型字面值
			CONSTANT_Fieldref			9		对一个字段的符号引用
			CONSTANT_Methodref			10		对一个类中声明的方法的符号引用
			CONSTANT_InterfaceMethodref	11		对一个接口中声明的方法的符号引用
			CONSTANT_NameAndType		12		对一个字段或方法的部分符号引用
	 */
        for (int i = 1; i < constant_pool_count; i++) { // 常量池
            int tag = dis.readByte();
            switch (tag) {
                case 1: //CONSTANT_Utf8
                    short len = dis.readShort();
                    if (len < 0) {
                        System.out.println("len " + len);
                    }
                    byte[] bs = new byte[len];
                    dis.read(bs);
                    pln(new String(bs), sb);
                    continue;
                case 3: //CONSTANT_Integer
                    int v_int = dis.readInt();
                    pln(v_int, sb);
                    continue;
                case 4: //CONSTANT_Float
                    float v_float = dis.readFloat();
                    pln(v_float, sb);
                    continue;
                case 5: //CONSTANT_Long
                    long v_long = dis.readLong();
                    pln(v_long, sb);
                    continue;
                case 6: //CONSTANT_Double
                    double v_double = dis.readDouble();
                    pln(v_double, sb);
                    continue;
                case 7: //CONSTANT_String
                    dis.readShort();
                    continue;
                case 8: //CONSTANT_String
                    dis.readShort();
                    continue;
                case 9: //CONSTANT_Fieldref_info
                    dis.readShort(); //指向一个CONSTANT_Class_info数据项
                    dis.readShort(); //指向一个CONSTANT_NameAndType_info
                    continue;
                case 10: //CONSTANT_Methodref_info
                    dis.readShort(); //指向一个CONSTANT_Class_info数据项
                    dis.readShort(); //指向一个CONSTANT_NameAndType_info
                    continue;
                case 11: //CONSTANT_InterfaceMethodref_info
                    dis.readShort(); //指向一个CONSTANT_Class_info数据项
                    dis.readShort(); //指向一个CONSTANT_NameAndType_info
                    continue;
                case 12:
                    dis.readShort();
                    dis.readShort();
                    continue;
                default:
                    return;
            }
        }
    }

    private static void pln(Object string, StringBuilder sb) {
        sb.append(string.toString()).append("\n");
    }

    // 加载文件夹中的代码
    private static void loadDirCode(File dir) {
//		System.out.println("加载代码：" + dir.getAbsolutePath());
        if (dir == null || !dir.exists() || dir.isFile() || dir.listFiles() == null) {
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                loadDirCode(f);
            } else {
                if (f.getName().endsWith(".xml") || f.getName().endsWith(".java")) {
                    loadFileCode(f);
                }
            }
        }
    }

    // 加载代码
    private static void loadFileCode(File f) {
        if (f == null || f.isDirectory() || !f.exists()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader sr = new BufferedReader(fr);
            String s = null;
            do {
                s = sr.readLine();
                if (s != null) {
                    sb.append(s).append("\n");
                } else {
                    break;
                }
            } while (true);
//			System.out.println(f.getAbsolutePath());
            codeMap.put(f.getAbsolutePath(), sb.toString());
            totalBytes += sb.toString().length();
            sr.close();
        } catch (Exception e) {
        }
    }
}
