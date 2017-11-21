/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bnrc.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.bnrc.model.AbAppProcessInfo;
import com.bnrc.model.AbCPUInfo;
import com.bnrc.model.AbProcessInfo;
import com.bnrc.model.AbPsRow;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bAppUtil.java 
 * 鎻忚堪锛氬簲鐢ㄥ伐鍏风被.
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2011-11-10 涓嬪崍11:52:13
 */
public class AbAppUtil {
	
	public static List<String[]> mProcessList = null;
	
	/**
	 * 鎻忚堪锛氭墦寮�骞跺畨瑁呮枃浠�.
	 *
	 * @param context the context
	 * @param file apk鏂囦欢璺緞
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 鎻忚堪锛氬嵏杞界▼搴�.
	 *
	 * @param context the context
	 * @param packageName 鍖呭悕
	 */
	public static void uninstallApk(Context context,String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		Uri packageURI = Uri.parse("package:" + packageName);
		intent.setData(packageURI);
		context.startActivity(intent);
	}


	/**
	 * 鐢ㄦ潵鍒ゆ柇鏈嶅姟鏄惁杩愯.
	 *
	 * @param context the context
	 * @param className 鍒ゆ柇鐨勬湇鍔″悕瀛� "com.xxx.xx..XXXService"
	 * @return true 鍦ㄨ繍琛� false 涓嶅湪杩愯
	 */
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			if (className.equals(si.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	/**
	 * 鍋滄鏈嶅姟.
	 *
	 * @param context the context
	 * @param className the class name
	 * @return true, if successful
	 */
	public static boolean stopRunningService(Context context, String className) {
		Intent intent_service = null;
		boolean ret = false;
		try {
			intent_service = new Intent(context, Class.forName(className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (intent_service != null) {
			ret = context.stopService(intent_service);
		}
		return ret;
	}
	

	/** 
	 * Gets the number of cores available in this device, across all processors. 
	 * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu" 
	 * @return The number of cores, or 1 if failed to get result 
	 */ 
	public static int getNumCores() { 
		try { 
			//Get directory containing CPU info 
			File dir = new File("/sys/devices/system/cpu/"); 
			//Filter to only list the devices we care about 
			File[] files = dir.listFiles(new FileFilter(){

				@Override
				public boolean accept(File pathname) {
					//Check if filename is "cpu", followed by a single digit number 
					if(Pattern.matches("cpu[0-9]", pathname.getName())) { 
					   return true; 
				    } 
				    return false; 
				}
				
			}); 
			//Return the number of cores (virtual CPU devices) 
			return files.length; 
		} catch(Exception e) { 
			e.printStackTrace();
			return 1; 
		} 
	} 
	
	
	/**
	 * 鎻忚堪锛氬垽鏂綉缁滄槸鍚︽湁鏁�.
	 *
	 * @param context the context
	 * @return true, if is network available
	 */
	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * Gps鏄惁鎵撳紑
	 * 闇�瑕�<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />鏉冮檺
	 *
	 * @param context the context
	 * @return true, if is gps enabled
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
	    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}


	/**
	 * 鍒ゆ柇褰撳墠缃戠粶鏄惁鏄Щ鍔ㄦ暟鎹綉缁�.
	 *
	 * @param context the context
	 * @return boolean
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}
	
	/**
	 * 瀵煎叆鏁版嵁搴�.
	 *
	 * @param context the context
	 * @param dbName the db name
	 * @param rawRes the raw res
	 * @return true, if successful
	 */
    public static boolean importDatabase(Context context,String dbName,int rawRes) {
		int buffer_size = 1024;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean flag = false;
		
		try {
			String dbPath = "/data/data/"+context.getPackageName()+"/databases/"+dbName; 
			File dbfile = new File(dbPath);
			//鍒ゆ柇鏁版嵁搴撴枃浠舵槸鍚﹀瓨鍦紝鑻ヤ笉瀛樺湪鍒欐墽琛屽鍏ワ紝鍚﹀垯鐩存帴鎵撳紑鏁版嵁搴�
			if (!dbfile.exists()) {
				//娆插鍏ョ殑鏁版嵁搴�
				if(!dbfile.getParentFile().exists()){
					dbfile.getParentFile().mkdirs();
				}
				dbfile.createNewFile();
				is = context.getResources().openRawResource(rawRes); 
				fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[buffer_size];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
				   fos.write(buffer, 0, count);
				}
				fos.flush();
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return flag;
	}
    
    /**
     * 鑾峰彇灞忓箷灏哄涓庡瘑搴�.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null){
            mResources = Resources.getSystem();
            
        }else{
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }
    
    /**
     * 鎵撳紑閿洏.
     *
     * @param context the context
     */
    public static void showSoftInput(Context context){
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    /**
     * 鍏抽棴閿洏浜嬩欢.
     *
     * @param context the context
     */
    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity)context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 鑾峰彇鍖呬俊鎭�.
     *
     * @param context the context
     */
    public static PackageInfo getPackageInfo(Context context) {
    	PackageInfo info = null;
	    try {
	        String packageName = context.getPackageName();
	        info = context.getPackageManager().getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return info;
    }
    
    
    /**
     * 
     * 鎻忚堪锛氳幏鍙栬繍琛岀殑杩涚▼鍒楄〃.
     * @param context
     * @return
     */
    public static List<AbAppProcessInfo> getRunningAppProcesses(Context context) {
    	ActivityManager activityManager = null;
    	List<AbAppProcessInfo> list = null;
    	PackageManager packageManager = null;
	    try {
	    	activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    	packageManager = context.getApplicationContext().getPackageManager();
	    	list = new ArrayList<AbAppProcessInfo>();
	    	//鎵�鏈夎繍琛岀殑杩涚▼
	    	List<RunningAppProcessInfo> appProcessList = activityManager.getRunningAppProcesses();
	    	ApplicationInfo appInfo = null;
	    	AbAppProcessInfo abAppProcessInfo = null;
	    	PackageInfo packageInfo = getPackageInfo(context);
	    	
	    	if(mProcessList != null){
	    		mProcessList.clear();
	    	}
	    	mProcessList = getProcessRunningInfo();
	    	
	    	for(RunningAppProcessInfo appProcessInfo:appProcessList){
	    		abAppProcessInfo = new AbAppProcessInfo(appProcessInfo.processName,appProcessInfo.pid,appProcessInfo.uid);
	    		appInfo = getApplicationInfo(context,appProcessInfo.processName);
	    		if(appInfo != null){
	    			Drawable icon = appInfo.loadIcon(packageManager);
		    		String appName = appInfo.loadLabel(packageManager).toString();
		    		abAppProcessInfo.icon = icon;
		    		abAppProcessInfo.appName = appName;
	    		}else{
	    			//:鏈嶅姟鐨勫懡鍚�
	    			if(appProcessInfo.processName.indexOf(":")!=-1){
	    				appInfo = getApplicationInfo(context,appProcessInfo.processName.split(":")[0]);
	    				Drawable icon = appInfo.loadIcon(packageManager);
	    				abAppProcessInfo.icon = icon;
	    			}
	    			abAppProcessInfo.appName = appProcessInfo.processName;
	    		}
	    		
	    		/*AbPsRow psRow = getPsRow(appProcessInfo.processName);
	    		if(psRow!=null){
	    			abAppProcessInfo.memory = psRow.mem;
	    		}*/
	    		
	    		AbProcessInfo processInfo = getMemInfo(appProcessInfo.processName);
	    		abAppProcessInfo.memory = processInfo.memory;
	    		abAppProcessInfo.cpu = processInfo.cpu;
	    		abAppProcessInfo.status = processInfo.status;
	    		abAppProcessInfo.threadsCount = processInfo.threadsCount;
	    		list.add(abAppProcessInfo);	
	        }
	    	
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return list;
    }
    
    /**
     * 
     * 鎻忚堪锛氭牴鎹繘绋嬪悕杩斿洖搴旂敤绋嬪簭.
     * @param context
     * @param processName
     * @return
     */
    public static ApplicationInfo getApplicationInfo(Context context,String processName) {
        if (processName == null) {
            return null;
        }
    	
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> appList = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : appList) {
        	if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }
    
    /**
     * 
     * 鎻忚堪锛歬ill杩涚▼.
     * @param context
     * @param pid
     */
    public static void killProcesses(Context context,int pid,String processName) {
    	/*String cmd = "kill -9 "+pid;
    	Process process = null;
	    DataOutputStream os = null;
    	try {
			process = Runtime.getRuntime().exec("su"); 
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	AbLogUtil.d(AbAppUtil.class, "#kill -9 "+pid);*/
    	
    	ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	String packageName = null;
    	try {
    		if(processName.indexOf(":")==-1){
    			packageName = processName;
		    }else{
		    	packageName = processName.split(":")[0];
		    }
    		
			activityManager.killBackgroundProcesses(packageName);
			
			//
			Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(activityManager, packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
    /**
	 * 
	 * 鎻忚堪锛氭墽琛孭S.
	 */
    public static List<AbPsRow> ps() {
    	List<AbPsRow> psRowlist = new ArrayList<AbPsRow>();
        String ps = runScript("ps");
        String[] lines = ps.split("\n");
        psRowlist = new ArrayList<AbPsRow>();
        for (String line : lines) {
        	AbPsRow row = new AbPsRow(line);
            if (row.pid != null) psRowlist.add(row);
        }
        return psRowlist;
    }
    
    /**
     * 
     * 鎻忚堪锛氳幏寰楄繖涓繘绋嬬殑ps淇℃伅.
     * @param processName
     * @return
     */
    public static AbPsRow getPsRow(String processName) {
    	List<AbPsRow> psRowlist = ps();
        for (AbPsRow row : psRowlist) {
            if (processName.equals(row.cmd)) {
                return row;
            }
        }
        return null;
    }
    
    /**
     * 
     * 鎻忚堪锛氭牴鎹繘绋嬪悕鑾峰彇CPU鍜屽唴瀛樹俊鎭�.
     * @param processName
     * @return
     */
    public static AbProcessInfo getMemInfo(String processName) {
    	AbProcessInfo process = new AbProcessInfo();
    	if(mProcessList == null){
    		mProcessList = getProcessRunningInfo();
    	}
        String processNameTemp = "";
        
        for (Iterator<String[]> iterator = mProcessList.iterator(); iterator.hasNext();) {
            String[] item = (String[]) iterator.next();
            processNameTemp = item[9];
            //AbLogUtil.d(AbAppUtil.class, "##"+item[9]+",NAME:"+processNameTemp);
            if (processNameTemp != null && processNameTemp.equals(processName)) {
            	//AbLogUtil.d(AbAppUtil.class, "##"+item[9]+","+process.memory);
            	//Process ID
            	process.pid = Integer.parseInt(item[0]);
            	//CPU
            	process.cpu = item[2];
            	//S
            	process.status = item[3];
            	//thread
            	process.threadsCount = item[4];
            	//Mem
            	long mem = 0;
    			if(item[6].indexOf("M")!=-1){
    				mem = Long.parseLong(item[6].replace("M", ""))*1000*1024;
    			}else if(item[6].indexOf("K")!=-1){
    				mem = Long.parseLong(item[6].replace("K", ""))*1000;
    			}else if(item[6].indexOf("G")!=-1){
    				mem = Long.parseLong(item[6].replace("G", ""))*1000*1024*1024;
    			}
    			process.memory = mem;
            	//UID
            	process.uid = item[8];
            	//Process Name
            	process.processName = item[9];
                break;
            }
        }
        if(process.memory == 0){
        	AbLogUtil.d(AbAppUtil.class, "##"+processName+",top -n 1鏈壘鍒�");
        }
        return process;
    }

    /**
     * 
     * 鎻忚堪锛氭牴鎹繘绋婭D鑾峰彇CPU鍜屽唴瀛樹俊鎭�.
     * @param pid
     * @return
     */
    public static AbProcessInfo getMemInfo(int pid) {
    	AbProcessInfo process = new AbProcessInfo();
    	if(mProcessList == null){
    		mProcessList = getProcessRunningInfo();
    	}
        String tempPidString = "";
        int tempPid = 0;
        int count = mProcessList.size();
        for (int i = 0; i < count; i++) {
            String[] item = mProcessList.get(i);
            tempPidString = item[0];
            if (tempPidString == null) {
                continue;
            }
            //AbLogUtil.d(AbAppUtil.class, "##"+item[9]+",PID:"+tempPid);
            tempPid = Integer.parseInt(tempPidString);
            if (tempPid == pid) {
            	//AbLogUtil.d(AbAppUtil.class, "##"+item[9]+","+process.memory);
            	//Process ID
            	process.pid = Integer.parseInt(item[0]);
            	//CPU
            	process.cpu = item[2];
            	//S
            	process.status = item[3];
            	//thread
            	process.threadsCount = item[4];
            	//Mem
            	long mem = 0;
    			if(item[6].indexOf("M")!=-1){
    				mem = Long.parseLong(item[6].replace("M", ""))*1000*1024;
    			}else if(item[6].indexOf("K")!=-1){
    				mem = Long.parseLong(item[6].replace("K", ""))*1000;
    			}else if(item[6].indexOf("G")!=-1){
    				mem = Long.parseLong(item[6].replace("G", ""))*1000*1024*1024;
    			}
    			process.memory = mem;
            	//UID
            	process.uid = item[8];
            	//Process Name
            	process.processName = item[9];
                break;
            }
        }
        return process;
    }
    
	/**
	 * 
	 * 鎻忚堪锛氭墽琛屽懡浠�.
	 * @param command
	 * @param workdirectory
	 * @return
	 */
	public static String runCommand(String[] command, String workdirectory){
		String result = "";
		AbLogUtil.d(AbAppUtil.class, "#"+command);
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			// set working directory
			if (workdirectory != null){
				builder.directory(new File(workdirectory));
			}
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream in = process.getInputStream();
			byte[] buffer = new byte[1024];
			while(in.read(buffer)!=-1){
				String str = new String(buffer);
				result = result + str;
			}
			in.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * 鎻忚堪锛氳繍琛岃剼鏈�.
	 * @param script
	 * @return
	 */
	public static String runScript(String script){
		String sRet = "";
		try {
			final Process m_process = Runtime.getRuntime().exec(script);
			final StringBuilder sbread = new StringBuilder();
			Thread tout = new Thread(new Runnable() {
				public void run() {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(m_process.getInputStream()),
							8192);
					String ls_1 = null;
					try {
						while ((ls_1 = bufferedReader.readLine()) != null) {
							sbread.append(ls_1).append("\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			tout.start();
			
			final StringBuilder sberr = new StringBuilder();
			Thread terr = new Thread(new Runnable() {
				public void run() {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(m_process.getErrorStream()),
							8192);
					String ls_1 = null;
					try {
						while ((ls_1 = bufferedReader.readLine()) != null) {
							sberr.append(ls_1).append("\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			terr.start();
			
			int retvalue = m_process.waitFor();
			while (tout.isAlive()) {
				Thread.sleep(50);
			}
			if (terr.isAlive())
				terr.interrupt();
			String stdout = sbread.toString();
			String stderr = sberr.toString();
			sRet = stdout + stderr;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sRet;
	}
	
	/**
	 * 搴旂敤绋嬪簭杩愯鍛戒护鑾峰彇 Root鏉冮檺锛岃澶囧繀椤诲凡鐮磋В(鑾峰緱ROOT鏉冮檺)
	 * @return 搴旂敤绋嬪簭鏄�/鍚﹁幏鍙朢oot鏉冮檺
	 */
	public static boolean getRootPermission(Context context) {
		String packageCodePath = context.getPackageCodePath();  
	    Process process = null;
	    DataOutputStream os = null;
	    try {
	        String cmd="chmod 777 " + packageCodePath;
	        //鍒囨崲鍒皉oot甯愬彿
	        process = Runtime.getRuntime().exec("su"); 
	        os = new DataOutputStream(process.getOutputStream());
	        os.writeBytes(cmd + "\n");
	        os.writeBytes("exit\n");
	        os.flush();
	        process.waitFor();
	    } catch (Exception e) {
	        return false;
	    } finally {
	        try {
	            if (os != null) {
	                os.close();
	            }
	            process.destroy();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	    return true;
	}
	
	/**
	 * 
	 * 鎻忚堪锛氳幏鍙栬繘绋嬭繍琛岀殑淇℃伅.
	 * @return
	 */
	public static List<String[]> getProcessRunningInfo() {        
		List<String[]> processList = null;     
		try {            
			String result = runCommandTopN1(); 
			processList = parseProcessRunningInfo(result);
		} catch (Exception e) {   
			e.printStackTrace();
		}       
		return processList;    
    }
	
	/**
	 * 
	 * 鎻忚堪锛歵op -n 1.
	 * @return
	 */
	public static String runCommandTopN1() {        
		String result = null;        
		try {            
			String[] args = {"/system/bin/top", "-n", "1"};            
			result = runCommand(args, "/system/bin/");        
		} catch (Exception e) {   
			e.printStackTrace();
		}       
		return result;    
    }
	
	/**
	 * 
	 * 鎻忚堪锛氳幏鍙栬繘绋嬭繍琛岀殑淇℃伅.
	 * @return
	 */
	public static AbCPUInfo getCPUInfo() {        
		AbCPUInfo CPUInfo = null;        
		try {            
			String result = runCommandTopN1();  
			CPUInfo = parseCPUInfo(result);
		} catch (Exception e) {   
			e.printStackTrace();
		}       
		return CPUInfo;    
    }
	
	/**
	 * 
	 * 鎻忚堪锛氳В鏋愭暟鎹�.
	 * @param info
	 * User 39%, System 17%, IOW 3%, IRQ 0%
	 * PID    PR CPU% S   #THR     VSS     RSS    PCY    UID        Name
     * 31587  0  39%  S    14    542288K  42272K  fg   u0_a162  cn.amsoft.process
     * 313    1  17%  S    12    68620K   11328K  fg   system   /system/bin/surfaceflinger
     * 32076  1   2%  R     1    1304K    604K    bg   u0_a162  /system/bin/top
	 * @return
	 */
	public static List<String[]> parseProcessRunningInfo(String info) {
		 List<String[]> processList = new ArrayList<String[]>();
		 int Length_ProcStat = 10;
		 String tempString = "";
		 boolean bIsProcInfo = false;
		 String[] rows = null;
		 String[] columns = null;
		 rows = info.split("[\n]+");
		 // 浣跨敤姝ｅ垯琛ㄨ揪寮忓垎鍓插瓧绗︿覆               
		 for (int i = 0; i < rows.length; i++) {
			 tempString = rows[i];
			 //AbLogUtil.d(AbAppUtil.class, tempString);
			 if (tempString.indexOf("PID") == -1) {
				 if (bIsProcInfo == true) {
					 tempString = tempString.trim();
					 columns = tempString.split("[ ]+");
					 if (columns.length == Length_ProcStat) {
						 //鎶�/system/bin/鐨勫幓鎺�
						 if(columns[9].startsWith("/system/bin/")){
							continue;
						 }
						 //AbLogUtil.d(AbAppUtil.class, "#"+columns[9]+",PID:"+columns[0]);
						 processList.add(columns);
					 }                
				 }            
			 } else {
				bIsProcInfo = true;
			 }
		}                
		return processList;    
	}
	
	/**
	 * 
	 * 鎻忚堪锛氳В鏋愭暟鎹�.
	 * @param info
	 * User 39%, System 17%, IOW 3%, IRQ 0%
	 * @return
	 */
	public static AbCPUInfo parseCPUInfo(String info) {
		 AbCPUInfo CPUInfo = new AbCPUInfo();
		 String tempString = "";
		 String[] rows = null;
		 String[] columns = null;
		 rows = info.split("[\n]+");
		 // 浣跨敤姝ｅ垯琛ㄨ揪寮忓垎鍓插瓧绗︿覆               
		 for (int i = 0; i < rows.length; i++) {
			 tempString = rows[i];
			 //AbLogUtil.d(AbAppUtil.class, tempString);
			 if (tempString.indexOf("User") != -1 && tempString.indexOf("System") != -1) {
				 tempString = tempString.trim();
				 columns = tempString.split(",");
				 for(int j = 0; j < columns.length; j++){
					String col = columns[j].trim();
					String[] cpu = col.split(" ");
					if(j == 0){
						CPUInfo.User = cpu[1];
				    }else if(j == 1){
				    	CPUInfo.System = cpu[1];
				    }else if(j == 2){
				    	CPUInfo.IOW = cpu[1];
				    }else if(j == 3){
				    	CPUInfo.IRQ = cpu[1];
				    }
			     }            
			 }
		}                
		return CPUInfo;    
	}

    /**
     * 
     * 鎻忚堪锛氳幏鍙栧彲鐢ㄥ唴瀛�.
     * @param context
     * @return
     */
	public static long getAvailMemory(Context context){  
        //鑾峰彇android褰撳墠鍙敤鍐呭瓨澶у皬  
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
        MemoryInfo memoryInfo = new MemoryInfo();  
        activityManager.getMemoryInfo(memoryInfo);  
        //褰撳墠绯荤粺鍙敤鍐呭瓨 ,灏嗚幏寰楃殑鍐呭瓨澶у皬瑙勬牸鍖�  
        return memoryInfo.availMem;  
    }  
	
	/**
	 * 
	 * 鎻忚堪锛氭�诲唴瀛�.
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context){  
		//绯荤粺鍐呭瓨淇℃伅鏂囦欢  
        String file = "/proc/meminfo";
        String memInfo;  
        String[] strs;  
        long memory = 0;  
          
        try{  
            FileReader fileReader = new FileReader(file);  
            BufferedReader bufferedReader = new BufferedReader(fileReader,8192);  
            //璇诲彇meminfo绗竴琛岋紝绯荤粺鍐呭瓨澶у皬 
            memInfo = bufferedReader.readLine(); 
            strs = memInfo.split("\\s+");  
            for(String str:strs){  
                AbLogUtil.d(AbAppUtil.class,str+"\t");  
            }  
            //鑾峰緱绯荤粺鎬诲唴瀛橈紝鍗曚綅KB  
            memory = Integer.valueOf(strs[1]).intValue()*1024;
            bufferedReader.close();  
        }catch(Exception e){  
            e.printStackTrace();
        }  
        //Byte杞綅KB鎴朚B
        return memory;  
    }  
    

}
