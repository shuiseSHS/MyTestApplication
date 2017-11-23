package com.example.scantool;

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

public class ClearUnUsedResource {

    private static boolean REAL_DEL = false; // 是否真的删除文件

    private final static boolean DEL_JAVA = true; // 是否删除java文件

    private final static boolean NEED_READ_JAR = false; // 是否需要扫描jar

    private final static boolean FULL_LOG = false;// 是否显示具体Log

    private final static boolean DEL_ALL = true;

    // 存在拼接查找资源的逻辑
    private final static String[] HOLD = new String[]{
            "cate_", "music_top_fans_", "player_rank_starrank_", "plugin_", "no_vip_rank", "vip_rank_", "vip_card_level_",
            "qiyi_search_hotwor_rank_icon_", "service_", "player_pp_recommend_item_circle_feed", "player_portrait_rank_starrank_",
            "vip_level_", "vip_upgrade_gift_dialog_logo_", "vip_upgrade_gift_dialog_title_", "gift_num_", "ico_hot"
    };

    private final static String ROOT_DIR = "F:/qiyi_git/qiyivideo/";

    private final static String[] EXCLUDE_DIR = {
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

    private final static String[] TO_CLEAR_PROJECTS = {
            "F:\\qiyi_git\\qiyivideo\\app\\QYVideoClient",
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

    private static List<String> TO_CLEAR = new ArrayList<>();

    private final static String[] XML_DIRS = {"/res/layout", "/res/menu", "/res/xml", "/res/anim", "/res/drawable"};
    private final static String[] IMG_DIRS = {"/res/drawable", "/res/drawable-hdpi", "/res/drawable-xhdpi", "/res/drawable-xxhdpi", "/res/drawable-xxxhdpi"};

    // 保存所有代码，以文件名做key
    private final static HashMap<String, String> codeMap = new HashMap<>();

    private static int delFiles = 0;
    private static long totalBytes = 0;
    private static long delXMLBytes = 0;
    private static long delIMGBytes = 0;

    public static void main(String[] args) {

        // 加载所有代码到内存中
        loadCodeinModules();

        int lastDelFiles;
        while (true) {
            lastDelFiles = delFiles;
            if (!REAL_DEL) {
                delFiles = 0;
                totalBytes = 0;
                delXMLBytes = 0;
                delIMGBytes = 0;
            }

            if (DEL_ALL) {
                // 代码
                if (DEL_JAVA) {
                    for (String p : TO_CLEAR_PROJECTS) {
                        File f = new File(p + "/src");
                        delUnusedJava(f);
                    }
                }

                // 扫描所有XML
                for (String p : TO_CLEAR_PROJECTS) {
                    for (String dir : XML_DIRS) {
                        File f = new File(p + dir);
                        delUnusedXml(f);
                    }
                }

                // Image
                for (String p : TO_CLEAR_PROJECTS) {
                    for (String dir : IMG_DIRS) {
                        File f = new File(p + dir);
                        delUnusedImage(f);
                    }
                }
            } else {
                // 代码
                if (DEL_JAVA) {
                    for (String p : TO_CLEAR_PROJECTS) {
                        File f = new File(p + "/src");
                        delUnusedJava(f);
                    }
                }

                // 扫描所有XML
                for (String p : TO_CLEAR_PROJECTS) {
                    for (String dir : XML_DIRS) {
                        File f = new File(p + dir);
                        delUnusedXml(f);
                    }
                }

                // Image
                for (String p : TO_CLEAR_PROJECTS) {
                    for (String dir : IMG_DIRS) {
                        File f = new File(p + dir);
                        delUnusedImage(f);
                    }
                }

            }

            System.out.println("可删除文件数：" + delFiles);
            if (delFiles == lastDelFiles) {
                break;
            }
        }

        System.out.println("最终删除文件数：" + delFiles);
        System.out.println("删除的XML字节数：" + delXMLBytes);
        System.out.println("删除的IMG字节数：" + delIMGBytes);
    }

    // 加载所有代码（包括xml和java）
    private static void loadCode() {
        codeMap.clear();
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
            File dir = new File(project, "/src");
            loadDirCode(dir);

            dir = new File(project, "/res");
            loadDirCode(dir);

            if (NEED_READ_JAR) {
                dir = new File(project, "/libs");
                loadContantsInJar(dir);
            }

            loadFileCode(new File(project, "/AndroidManifest.xml"));
        }
        System.out.println("初始文件总数：" + codeMap.size());
        System.out.println("代码字节数：" + totalBytes);
    }

    // 加载所有代码（包括xml和java）
    private static void loadCodeinModules() {
        codeMap.clear();
        File root = new File(ROOT_DIR);
        for (File project : root.listFiles()) {
            if (project == null || project.isFile()) {
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
            loadDirs(project);
        }
        System.out.println("初始文件总数：" + codeMap.size());
        System.out.println("代码字节数：" + totalBytes);
    }

    private static void loadDirs(File moduleDir) {
        if (!moduleDir.isDirectory()) {
            return;
        }
        boolean isModule = false;
        for (File file : moduleDir.listFiles()) {
            if (file.getName().endsWith("build.gradle")) {
                isModule = true;
                break;
            }
        }

        if (isModule) {
            loadModule(moduleDir);
        } else {
            for (File file : moduleDir.listFiles()) {
                loadDirs(file);
            }
        }
    }


    private static void loadModule(File moduleDir) {
        System.out.println(moduleDir.getAbsolutePath());

        TO_CLEAR.add(moduleDir.getName());
        File dir = new File(moduleDir, "/src");
        loadDirCode(dir);

        dir = new File(moduleDir, "/res");
        loadDirCode(dir);

        if (NEED_READ_JAR) {
            dir = new File(moduleDir, "/libs");
            loadContantsInJar(dir);
        }

        loadFileCode(new File(moduleDir, "/AndroidManifest.xml"));
    }

    // 筛选出没有引用的java文件并且删除
    private static void delUnusedJava(File dir) {
        if (codeMap.size() == 0) {
            System.err.println("textMap没有初始化");
        }
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                delUnusedJava(f);
            } else {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".java")) {
                    if (!containsStringByJava(f)) {
                        logD(f);
                        codeMap.remove(f.getAbsolutePath());
                        delXMLBytes += f.length();
                        delFiles++;
                        if (REAL_DEL) {
                            f.delete();
                        }
                    }
                }
            }
        }
    }

    private static void logD(File f) {
        System.out.println("D: " + f.getAbsolutePath().substring(12));
    }

    // 筛选出没有引用的xml文件并且删除
    private static void delUnusedXml(File dir) {
        if (codeMap.size() == 0) {
            System.err.println("textMap没有初始化");
        }
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File f : dir.listFiles()) {
            String fileName = f.getAbsolutePath();
            if (fileName.endsWith(".xml")) {
                if (!containsString(f)) {
                    logD(f);
                    codeMap.remove(f.getAbsolutePath());
                    delXMLBytes += f.length();
                    delFiles++;
                    if (REAL_DEL) {
                        f.delete();
                    }
                }
            }
        }
    }

    // 筛选出没有引用的图片文件并且删除
    private static void delUnusedImage(File dir) {
        if (codeMap.size() == 0) {
            System.err.println("textMap没有初始化");
        }
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File f : dir.listFiles()) {
            String fileName = f.getName();
            if (fileName.endsWith(".9.png") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                if (!containsString(f)) {
                    logD(f);
                    codeMap.remove(f.getAbsolutePath());
                    delIMGBytes += f.length();
                    delFiles++;
                    if (REAL_DEL) {
                        f.delete();
                    }
                }
            }
        }
    }

    // 查找指定字符串
    private static boolean containsString(File currentFile) {
        String currentName = currentFile.getName();
        String currentFilePath = currentFile.getAbsolutePath();
        String fileName = currentName.substring(0, currentName.indexOf("."));

        // TODO; 存在拼接查找资源的逻辑
        for (String s : HOLD) {
            if (fileName.startsWith(s)) {
                return true;
            }
        }

        int matchCount = 0;
        for (String filePath : codeMap.keySet()) {
            if (filePath.equals(currentFilePath)) {
                continue;
            }
            String text = codeMap.get(filePath);
            if (text.contains("\"" + fileName + "\"") || text.contains("." + fileName) || text.contains("/" + fileName)) {
                matchCount++;
                if (filePath.contains(".jar")) {
                    if (FULL_LOG) {
                        System.err.println("###" + filePath + "包含" + currentFilePath);
                    }
                }
                break;
            }
        }

        return matchCount > 0;
    }

    // 查找指定字符串
    private static boolean containsStringByJava(File currentFile) {
        String currentName = currentFile.getName();
        String currentFilePath = currentFile.getAbsolutePath();
        String fileName = currentName.substring(0, currentName.indexOf("."));

        int c = 0;
        for (String filePath : codeMap.keySet()) {
            if (!filePath.endsWith(".java") && !filePath.endsWith("AndroidManifest.xml")) {
                continue;
            }
            if (filePath.equals(currentFilePath)) {
                continue;
            }
            String text = codeMap.get(filePath);
            if (text.contains(fileName)) {
                c++;
                break;
            }
        }

        return c != 0;
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
        JarFile jarfile;
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
                } catch (IOException ignored) {
                }
            }
            jarfile.close();
        } catch (IOException ignored) {
        }
        codeMap.put(f.getAbsolutePath(), sb.toString());
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
            String s;
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
        } catch (Exception ignored) {
        }
    }
}
