package com.example.scantool.codeloader;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by shisong on 2019/6/6
 */
public class JarCodeLoader {

    public static String loadCode(File f) {
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
        return sb.toString();
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

}
