package com.example;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by shisong on 2017/8/24.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private String dexFileName = "target.dex";

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        Log.d(TAG, "attachBaseContext");
        super.attachBaseContext(base);
        loadDex();
    }

    /**
     * 复制SD卡中的补丁文件到dex目录
     */
    public void copyDexFileToAppAndFix() {
        File rootCacheDir = Environment.getExternalStorageDirectory();
        File srcDexFile = new File(rootCacheDir, dexFileName);
        if (!srcDexFile.exists()) {
            Toast.makeText(this, "没有找到补丁文件", Toast.LENGTH_SHORT).show();
            return;
        }

        File dexFile = getDestDexFile();
        if (dexFile.exists()) {
            dexFile.delete();
        }
        //copy
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(srcDexFile);
            os = new FileOutputStream(dexFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
//            srcDexFile.delete();//删除sdcard中的补丁文件，或者你可以直接下载到app的路径中
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    private File getDestDexFile() {
        File dexFilePath = getDir("dex", Context.MODE_PRIVATE);
        return new File(dexFilePath, dexFileName);
    }

    private void loadDex() {
        boolean hasBaseDexClassLoader = true;
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
        } catch (ClassNotFoundException e) {
            hasBaseDexClassLoader = false;
        }

        if (!hasBaseDexClassLoader) {
            return;
        }

        // 获取到包含 class.dex 的 jar 包文件
        File rootCacheDir = Environment.getExternalStorageDirectory();
        File jarFile = new File(rootCacheDir, dexFileName);

//        File jarFile = getDestDexFile();
        // 如果没有读权限,确定你在 AndroidManifest 中是否声明了读写权限
        Log.d(TAG, jarFile.canRead() + "");

        if (!jarFile.exists()) {
            Log.e(TAG, jarFile.getAbsolutePath() + " not exists");
            return;
        }
        DexClassLoader dexClassLoader = new DexClassLoader(jarFile.getAbsolutePath(), getDir("optimize", Context.MODE_PRIVATE).getAbsolutePath(), null, getClassLoader());
//        try {
//            Log.e(TAG, "### " + dexClassLoader.loadClass("com.example.dexloader.Hello_1"));
//            Log.e(TAG, "### " + getClassLoader().loadClass("com.example.dexloader.Hello_1"));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        PathClassLoader pathClassLoader = (PathClassLoader) getClassLoader();
        Object dexElements = combineArray(getDexElements(getPathList(pathClassLoader)), getDexElements(getPathList(dexClassLoader)));
        Object pathList = getPathList(pathClassLoader);
        setField(pathList, pathList.getClass(), "dexElements", dexElements);
    }

    public void setField(Object pathList, Class aClass, String fieldName, Object fieldValue) {
        try {
            Field declaredField = aClass.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(pathList, fieldValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object combineArray(Object object, Object object2) {
        Class<?> aClass = Array.get(object, 0).getClass();
        int length = Array.getLength(object);
        int length2 = Array.getLength(object2);
        Object comObj = Array.newInstance(aClass, length + length2);

        System.arraycopy(object2, 0, comObj, 0, length2);
        System.arraycopy(object, 0, comObj, length2, length);
        return comObj;
    }

    public Object getDexElements(Object object) {
        if (object == null)
            return null;

        Class<?> aClass = object.getClass();
        try {
            Field dexElements = aClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);
            return dexElements.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Object getPathList(BaseDexClassLoader classLoader) {
        Class<? extends BaseDexClassLoader> aClass = classLoader.getClass();
        Class<?> superclass = aClass.getSuperclass();
        try {
            Field pathListField = superclass.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object object = pathListField.get(classLoader);
            return object;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载指定路径的dex
     *
     * @param apkPath
     */
    private void loadClass(String apkPath) {
        //该方法内的以下代码需要注释
        if (true) {
            return;
        }

        ClassLoader classLoader = getClassLoader();

        File file = new File(apkPath);

        File optimizedDirectoryFile = new File(file.getParentFile(), "optimizedDirectory");

        if (!optimizedDirectoryFile.exists())
            optimizedDirectoryFile.mkdir();

        try {

            DexClassLoader dexClassLoader = new DexClassLoader(apkPath, optimizedDirectoryFile.getAbsolutePath(), "", classLoader);
            Class<?> aClass = dexClassLoader.loadClass("com.sahadev.bean.ClassStudent");
            Log.i(TAG, "com.sahadev.bean.ClassStudent = " + aClass);

            Object instance = aClass.newInstance();
            Method method = aClass.getMethod("setName", String.class);
            method.invoke(instance, "Sahadev");

            Method getNameMethod = aClass.getMethod("getName");
            Object invoke = getNameMethod.invoke(instance);

            Log.i(TAG, "invoke result = " + invoke);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
