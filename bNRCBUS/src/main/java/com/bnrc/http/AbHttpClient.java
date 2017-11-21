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
package com.bnrc.http;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bnrc.global.AbAppConfig;
import com.bnrc.global.AbAppException;
import com.bnrc.task.AbThreadFactory;
import com.bnrc.util.AbAppUtil;
import com.bnrc.util.AbFileUtil;
import com.bnrc.util.AbLogUtil;

// TODO: Auto-generated Javadoc
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bHttpClient.java 
 * 鎻忚堪锛欻ttp瀹㈡埛绔�
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2013-11-13 涓婂崍9:00:52
 */
public class AbHttpClient {
	
    /** 涓婁笅鏂�. */
	private static Context mContext;
	
	/** 绾跨▼鎵ц鍣�. */
	public static Executor mExecutorService = null;
	
	/** 缂栫爜. */
	private String encode = HTTP.UTF_8;
	
	/** 鐢ㄦ埛浠ｇ悊. */
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 BIDUBrowser/6.x Safari/537.31";
	
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String USER_AGENT = "User-Agent";
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    
    /** CookieStore. */
    private CookieStore mCookieStore;  

    /** 鏈�澶ц繛鎺ユ暟. */
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    
    /** 瓒呮椂鏃堕棿. */
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    
    /** 閲嶈瘯娆℃暟. */
    private static final int DEFAULT_MAX_RETRIES = 3;
    
    /** 缂撳啿澶у皬. */
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    
    /** 缂撳啿澶у皬. */
    private static final int BUFFER_SIZE = 4096;
    
    /** 鎴愬姛. */
    protected static final int SUCCESS_MESSAGE = 0;
    
    /** 澶辫触. */
    protected static final int FAILURE_MESSAGE = 1;
    
    /** 鍜岀綉缁滅浉鍏崇殑澶辫触. */
    protected static final int FAILURE_MESSAGE_CONNECT = 2;
    
    /** 鍜屾湇鍔＄浉鍏崇殑澶辫触. */
    protected static final int FAILURE_MESSAGE_SERVICE = 3;
    
    /** 寮�濮�. */
    protected static final int START_MESSAGE = 4;
    
    /** 瀹屾垚. */
    protected static final int FINISH_MESSAGE = 5;
    
    /** 杩涜涓�. */
    protected static final int PROGRESS_MESSAGE = 6;
    
    /** 閲嶈瘯. */
    protected static final int RETRY_MESSAGE = 7;
    
    /** 瓒呮椂鏃堕棿. */
	private int mTimeout = DEFAULT_SOCKET_TIMEOUT;
	
	/** 閫氱敤璇佷功. 濡傛灉瑕佹眰HTTPS杩炴帴锛屽垯浣跨敤SSL鎵撳紑杩炴帴*/
	private boolean mIsOpenEasySSL = true;
	
	/** HTTP Client*/
	private DefaultHttpClient mHttpClient = null;
	
	/** HTTP 涓婁笅鏂�*/
	private HttpContext mHttpContext = null;
    
    /**
     * 鍒濆鍖�.
     *
     * @param context the context
     */
	public AbHttpClient(Context context) {
	    mContext = context;
		mExecutorService =  AbThreadFactory.getExecutorService();
		mHttpContext = new BasicHttpContext();
	}
	

	/**
	 * 鎻忚堪锛氬甫鍙傛暟鐨刧et璇锋眰.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {
		
		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				doGet(url,params,responseListener);
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}
    		}                 
    	});      
	}
	
	/**
	 * 鎻忚堪锛氬甫鍙傛暟鐨刾ost璇锋眰.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(final String url,final AbRequestParams params,
			final AbHttpResponseListener responseListener) {
		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				doPost(url,params,responseListener);
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}
    		}                 
    	});      
	}
	
	
	/**
	 * 鎻忚堪锛氭墽琛実et璇锋眰.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	private void doGet(String url,AbRequestParams params,AbHttpResponseListener responseListener){
		  try {
			  
			  responseListener.sendStartMessage();
			  
			  if(!AbAppUtil.isNetworkAvailable(mContext)){
					responseListener.sendFailureMessage(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION, new AbAppException(AbAppConfig.CONNECT_EXCEPTION));
			        return;
			  }
			  
			  //HttpGet杩炴帴瀵硅薄  
			  if(params!=null){
				  url += params.getParamString();
			  }
			  HttpGet httpGet = new HttpGet(url);  
			  httpGet.addHeader(USER_AGENT, userAgent);
			  //鍘嬬缉
			  httpGet.addHeader(ACCEPT_ENCODING, "gzip");
			  //鍙栧緱榛樿鐨凥ttpClient
      	      HttpClient httpClient = getHttpClient();  
		      //鍙栧緱HttpResponse
		      String  response = httpClient.execute(httpGet,new RedirectionResponseHandler(url,responseListener),mHttpContext);  
			  AbLogUtil.i(mContext, "[HTTP Request]:"+url+",result锛�"+response);
		} catch (Exception e) {
			e.printStackTrace();
			//鍙戦�佸け璐ユ秷鎭�
			responseListener.sendFailureMessage(AbHttpStatus.UNTREATED_CODE,e.getMessage(),new AbAppException(e));
		}finally{
			responseListener.sendFinishMessage();
		}
	}
	
	/**
	 * 鎻忚堪锛氭墽琛宲ost璇锋眰.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	private void doPost(String url,AbRequestParams params,AbHttpResponseListener responseListener){
		  try {
			  responseListener.sendStartMessage();
			  
			  if(!AbAppUtil.isNetworkAvailable(mContext)){
					responseListener.sendFailureMessage(AbHttpStatus.CONNECT_FAILURE_CODE,AbAppConfig.CONNECT_EXCEPTION, new AbAppException(AbAppConfig.CONNECT_EXCEPTION));
			        return;
			  }
			  
			  //HttpPost杩炴帴瀵硅薄  
		      HttpPost httpPost = new HttpPost(url);  
		      httpPost.addHeader(USER_AGENT, userAgent);
			  //鍘嬬缉
		      httpPost.addHeader(ACCEPT_ENCODING, "gzip");
		      //鏄惁鍖呭惈鏂囦欢
		      boolean isContainFile = false;
		      if(params != null){
		    	  //浣跨敤NameValuePair鏉ヤ繚瀛樿浼犻�掔殑Post鍙傛暟璁剧疆瀛楃闆� 
			      HttpEntity httpentity = params.getEntity(responseListener);
			      //璇锋眰httpRequest  
			      httpPost.setEntity(httpentity); 
			      if(params.getFileParams().size()>0){
			    	  isContainFile = true;
			      }
			  }
		      String  response = null;
		      //鍙栧緱榛樿鐨凥ttpClient
		      DefaultHttpClient httpClient = getHttpClient();  
		      if(isContainFile){
		    	  AbLogUtil.i(mContext, "request锛�"+url+",鍖呭惈鏂囦欢鍩�!");
		      }else{
		      }
		      //鍙栧緱HttpResponse
		      response = httpClient.execute(httpPost,new RedirectionResponseHandler(url,responseListener),mHttpContext);  
		      AbLogUtil.i(mContext, "request锛�"+url+",result锛�"+response);
			  
		} catch (Exception e) {
			e.printStackTrace();
			AbLogUtil.i(mContext, "request锛�"+url+",error锛�"+e.getMessage());
			//鍙戦�佸け璐ユ秷鎭�
			responseListener.sendFailureMessage(AbHttpStatus.UNTREATED_CODE,e.getMessage(),new AbAppException(e));
		}finally{
			responseListener.sendFinishMessage();
		}
	}
	
	
	/**
	 * 鎻忚堪锛氬啓鍏ユ枃浠跺苟鍥炶皟杩涘害.
	 * 
	 * @param context the context
	 * @param entity the entity
	 * @param name the name
	 * @param responseListener the response listener
	 */
    public void writeResponseData(Context context,HttpEntity entity,String name,AbFileHttpResponseListener responseListener){
        
    	if(entity == null){
        	return;
        }
    	
    	if(responseListener.getFile() == null){
    		//鍒涘缓缂撳瓨鏂囦欢
    		responseListener.setFile(context,name);
        }
    	
    	InputStream inStream = null;
    	FileOutputStream outStream = null;
        try {
	        inStream = entity.getContent();
	        long contentLength = entity.getContentLength();
	        outStream = new FileOutputStream(responseListener.getFile());
	        if (inStream != null) {
	          
	              byte[] tmp = new byte[BUFFER_SIZE];
	              int l, count = 0;
	              while ((l = inStream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
	                  count += l;
	                  outStream.write(tmp, 0, l);
	                  responseListener.sendProgressMessage(count, (int) contentLength);
	              }
	        }
	        responseListener.sendSuccessMessage(200);
	    }catch(Exception e){
	        e.printStackTrace();
	        //鍙戦�佸け璐ユ秷鎭�
			responseListener.sendFailureMessage(AbHttpStatus.RESPONSE_TIMEOUT_CODE,AbAppConfig.SOCKET_TIMEOUT_EXCEPTION,e);
	    } finally {
        	try {
        		if(inStream!=null){
        			inStream.close();
        		}
        		if(outStream!=null){
        			outStream.flush();
    				outStream.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        
    }
    
    /**
     * 鎻忚堪锛氳浆鎹负浜岃繘鍒跺苟鍥炶皟杩涘害.
     *
     * @param entity the entity
     * @param responseListener the response listener
     */
    public void readResponseData(HttpEntity entity,AbBinaryHttpResponseListener responseListener){
        
    	if(entity == null){
        	return;
        }
    	
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null; 
       
    	try {
    		inStream = entity.getContent();
			outSteam = new ByteArrayOutputStream(); 
			long contentLength = entity.getContentLength();
			if (inStream != null) {
				  int l, count = 0;
				  byte[] tmp = new byte[BUFFER_SIZE];
				  while((l = inStream.read(tmp))!=-1){ 
					  count += l;
			          outSteam.write(tmp,0,l); 
			          responseListener.sendProgressMessage(count, (int) contentLength);

			     } 
			  }
			 responseListener.sendSuccessMessage(HttpStatus.SC_OK,outSteam.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			//鍙戦�佸け璐ユ秷鎭�
			responseListener.sendFailureMessage(AbHttpStatus.RESPONSE_TIMEOUT_CODE,AbAppConfig.SOCKET_TIMEOUT_EXCEPTION,e);
		}finally{
			try {
				if(inStream!=null){
					inStream.close();
				}
				if(outSteam!=null){
					outSteam.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
        
    }
    
    /**
     * 鎻忚堪锛氳缃繛鎺ヨ秴鏃舵椂闂�.
     *
     * @param timeout 姣
     */
    public void setTimeout(int timeout) {
    	this.mTimeout = timeout;
	}

	/**
	 * 漏 2012 amsoft.cn
	 * 鍚嶇О锛歊esponderHandler.java 
	 * 鎻忚堪锛氳姹傝繑鍥�
	 *
	 * @author 杩樺涓�姊︿腑
	 * @version v1.0
	 * @date锛�2013-11-13 涓嬪崍3:22:30
	 */
    private static class ResponderHandler extends Handler {
		
		/** 鍝嶅簲鏁版嵁. */
		private Object[] response;
		
		/** 鍝嶅簲娑堟伅鐩戝惉. */
		private AbHttpResponseListener responseListener;
		

		/**
		 * 鍝嶅簲娑堟伅澶勭悊.
		 *
		 * @param responseListener the response listener
		 */
		public ResponderHandler(AbHttpResponseListener responseListener) {
			this.responseListener = responseListener;
		}

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case SUCCESS_MESSAGE:
				response = (Object[]) msg.obj;
				
				if (response != null){
					if(responseListener instanceof AbStringHttpResponseListener){
						if(response.length >= 2){
							((AbStringHttpResponseListener)responseListener).onSuccess((Integer) response[0],(String)response[1]);
						}else{
							AbLogUtil.e(mContext, "SUCCESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
						}
						
					}else if(responseListener instanceof AbBinaryHttpResponseListener){
						if(response.length >= 2){ 
						    ((AbBinaryHttpResponseListener)responseListener).onSuccess((Integer) response[0],(byte[])response[1]);
						}else{
							AbLogUtil.e(mContext, "SUCCESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
						}
					}else if(responseListener instanceof AbFileHttpResponseListener){
						
						if(response.length >= 1){ 
							AbFileHttpResponseListener mAbFileHttpResponseListener = ((AbFileHttpResponseListener)responseListener);
							mAbFileHttpResponseListener.onSuccess((Integer) response[0],mAbFileHttpResponseListener.getFile());
						}else{
							AbLogUtil.e(mContext, "SUCCESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
						}
						
					}
				}
                break;
			case FAILURE_MESSAGE:
				 response = (Object[]) msg.obj;
                 if (response != null && response.length >= 3){
                	 //寮傚父杞崲涓哄彲鎻愮ず鐨�
                	 AbAppException exception = new AbAppException((Exception) response[2]);
					 responseListener.onFailure((Integer) response[0], (String) response[1], exception);
                 }else{
                	 AbLogUtil.e(mContext, "FAILURE_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
                 }
	             break;
			case START_MESSAGE:
				responseListener.onStart();
				break;
			case FINISH_MESSAGE:
				responseListener.onFinish();
				break;
			case PROGRESS_MESSAGE:
				 response = (Object[]) msg.obj;
	             if (response != null && response.length >= 2){
	            	 responseListener.onProgress((Integer) response[0], (Integer) response[1]);
			     }else{
			    	 AbLogUtil.e(mContext, "PROGRESS_MESSAGE "+AbAppConfig.MISSING_PARAMETERS);
			     }
	             break;
			case RETRY_MESSAGE:
				responseListener.onRetry();
				break;
			default:
				break;
		   }
		}
		
	}
    
    /**
     * HTTP鍙傛暟閰嶇疆
     * @return
     */
    public BasicHttpParams getHttpParams(){
    	
    	BasicHttpParams httpParams = new BasicHttpParams();
        
        // 璁剧疆姣忎釜璺敱鏈�澶ц繛鎺ユ暟
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(30);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		// 浠庤繛鎺ユ睜涓彇杩炴帴鐨勮秴鏃舵椂闂达紝璁剧疆涓�1绉�
	    ConnManagerParams.setTimeout(httpParams, mTimeout);
	    ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
	    ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
	    // 璇诲搷搴旀暟鎹殑瓒呮椂鏃堕棿
	    HttpConnectionParams.setSoTimeout(httpParams, mTimeout);
	    HttpConnectionParams.setConnectionTimeout(httpParams, mTimeout);
	    HttpConnectionParams.setTcpNoDelay(httpParams, true);
	    HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

	    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setUserAgent(httpParams,userAgent);
	    //榛樿鍙傛暟
        HttpClientParams.setRedirecting(httpParams, false);
        HttpClientParams.setCookiePolicy(httpParams,
                CookiePolicy.BROWSER_COMPATIBILITY);
        httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
		return httpParams;
	      
    }
    
    /**
     * 鑾峰彇HttpClient锛岃嚜绛惧悕鐨勮瘉涔︼紝濡傛灉鎯冲仛绛惧悕鍙傝�傾uthSSLProtocolSocketFactory绫�
     * @return
     */
    public DefaultHttpClient getHttpClient(){
    	
    	if(mHttpClient != null){
    		return mHttpClient;
    	}else{
    		return createHttpClient();
    	}
    }
    
    /**
     * 鑾峰彇HttpClient锛岃嚜绛惧悕鐨勮瘉涔︼紝濡傛灉鎯冲仛绛惧悕鍙傝�傾uthSSLProtocolSocketFactory绫�
     * @param httpParams
     * @return
     */
    public DefaultHttpClient createHttpClient(){
    	BasicHttpParams httpParams = getHttpParams();
    	if(mIsOpenEasySSL){
    		 // 鏀寔https鐨�   SSL鑷鍚嶇殑瀹炵幇绫�
    	     EasySSLProtocolSocketFactory easySSLProtocolSocketFactory = new EasySSLProtocolSocketFactory();
             SchemeRegistry supportedSchemes = new SchemeRegistry();
             SocketFactory socketFactory = PlainSocketFactory.getSocketFactory();
             supportedSchemes.register(new Scheme("http", socketFactory, 80));
             supportedSchemes.register(new Scheme("https",easySSLProtocolSocketFactory, 443));
             //瀹夊叏鐨凾hreadSafeClientConnManager锛屽惁鍒欎笉鑳界敤鍗曚緥鐨凥ttpClient
             ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(
                     httpParams, supportedSchemes);
             //鍙栧緱HttpClient ThreadSafeClientConnManager
             mHttpClient = new DefaultHttpClient(connectionManager, httpParams);
    	}else{
    		 //绾跨▼瀹夊叏鐨凥ttpClient
    		 mHttpClient = new DefaultHttpClient(httpParams);
    	}
    	//鑷姩閲嶈瘯
    	mHttpClient.setHttpRequestRetryHandler(mRequestRetryHandler);
    	mHttpClient.setCookieStore(mCookieStore);
 	    return mHttpClient;
    }

    /**
     * 鏄惁鎵撳紑ssl 鑷鍚�
     */
    public boolean isOpenEasySSL(){
        return mIsOpenEasySSL;
    }


    /**
     * 鎵撳紑ssl 鑷鍚�
     * @param isOpenEasySSL
     */
    public void setOpenEasySSL(boolean isOpenEasySSL){
        this.mIsOpenEasySSL = isOpenEasySSL;
    }
    
    /**
     * 浣跨敤ResponseHandler鎺ュ彛澶勭悊鍝嶅簲,鏀寔閲嶅畾鍚�
     */
    private class RedirectionResponseHandler implements ResponseHandler<String>{
        
    	private AbHttpResponseListener mResponseListener = null;
    	private String mUrl = null;
    	
		public RedirectionResponseHandler(String url,
				AbHttpResponseListener responseListener) {
			super();
			this.mUrl = url;
			this.mResponseListener = responseListener;
		}


		@Override
        public String handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException{
            HttpUriRequest request = (HttpUriRequest) mHttpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            //璇锋眰鎴愬姛  
  			int statusCode = response.getStatusLine().getStatusCode();
  			HttpEntity entity = response.getEntity();
  			String responseBody = null;
            //200鐩存帴杩斿洖缁撴灉
            if (statusCode == HttpStatus.SC_OK) {
                
                // 涓嶆墦绠楄鍙杛esponse body   
                // 璋冪敤request鐨刟bort鏂规硶  
                // request.abort();  
                
                if (entity != null){
	  				  if(mResponseListener instanceof AbStringHttpResponseListener){
	  					  //entity涓殑鍐呭鍙兘璇诲彇涓�娆�,鍚﹀垯Content has been consumed
	  					  //濡傛灉鍘嬬缉瑕佽В鍘�
	                      Header header = entity.getContentEncoding();
	                      if (header != null){
	                          String contentEncoding = header.getValue();
	                          if (contentEncoding != null){
	                              if (contentEncoding.contains("gzip")){
	                                  entity = new AbGzipDecompressingEntity(entity);
	                              }
	                          }
	                      }
	                      String charset = EntityUtils.getContentCharSet(entity) == null ? encode : EntityUtils.getContentCharSet(entity);
	      	              responseBody = new String(EntityUtils.toByteArray(entity), charset);
	  					  
	      	              ((AbStringHttpResponseListener)mResponseListener).sendSuccessMessage(statusCode, responseBody);
	  				  }else if(mResponseListener instanceof AbBinaryHttpResponseListener){
	  					  responseBody = "Binary";
	  					  readResponseData(entity,((AbBinaryHttpResponseListener)mResponseListener));
	  				  }else if(mResponseListener instanceof AbFileHttpResponseListener){
	  					  //鑾峰彇鏂囦欢鍚�
	  					  String fileName = AbFileUtil.getCacheFileNameFromUrl(mUrl, response);
	  					  writeResponseData(mContext,entity,fileName,((AbFileHttpResponseListener)mResponseListener));
	  				  }
	      		      //璧勬簮閲婃斁!!!
	            	  try {
						  entity.consumeContent();
					  } catch (Exception e) {
						  e.printStackTrace();
					  }
	      			  return responseBody;
                }
                
            }
            //301 302杩涜閲嶅畾鍚戣姹�
            else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
                    || statusCode == HttpStatus.SC_MOVED_PERMANENTLY){
                // 浠庡ご涓彇鍑鸿浆鍚戠殑鍦板潃
                Header locationHeader = response.getLastHeader("location");
                String location = locationHeader.getValue();
                if (request.getMethod().equalsIgnoreCase(HTTP_POST)){
                    doPost(location, null, mResponseListener);
                }
                else if (request.getMethod().equalsIgnoreCase(HTTP_GET)){
                    doGet(location, null, mResponseListener);
                }
            }else if(statusCode == HttpStatus.SC_NOT_FOUND){
            	//404
            	mResponseListener.sendFailureMessage(statusCode, AbAppConfig.NOT_FOUND_EXCEPTION, new AbAppException(AbAppConfig.NOT_FOUND_EXCEPTION));
            }else{
  				mResponseListener.sendFailureMessage(statusCode, AbAppConfig.REMOTE_SERVICE_EXCEPTION, new AbAppException(AbAppConfig.REMOTE_SERVICE_EXCEPTION));
            }
            return null;
        }
    }
    
    /**
     * 鑷姩閲嶈瘯澶勭悊
     */
    private HttpRequestRetryHandler mRequestRetryHandler = new HttpRequestRetryHandler(){
        
    	// 鑷畾涔夌殑鎭㈠绛栫暐
        public boolean retryRequest(IOException exception, int executionCount,
                HttpContext context){
            // 璁剧疆鎭㈠绛栫暐锛屽湪鍙戠敓寮傚父鏃跺�欏皢鑷姩閲嶈瘯DEFAULT_MAX_RETRIES娆�
            if (executionCount >= DEFAULT_MAX_RETRIES){
                // 濡傛灉瓒呰繃鏈�澶ч噸璇曟鏁帮紝閭ｄ箞灏变笉瑕佺户缁簡
            	AbLogUtil.d(mContext, "瓒呰繃鏈�澶ч噸璇曟鏁帮紝涓嶉噸璇�");
                return false;
            }
            if (exception instanceof NoHttpResponseException){
                // 濡傛灉鏈嶅姟鍣ㄤ涪鎺変簡杩炴帴锛岄偅涔堝氨閲嶈瘯
            	AbLogUtil.d(mContext, "鏈嶅姟鍣ㄤ涪鎺変簡杩炴帴锛岄噸璇�");
                return true;
            }
            if (exception instanceof SSLHandshakeException){
                // SSL鎻℃墜寮傚父锛屼笉閲嶈瘯
            	AbLogUtil.d(mContext, "ssl 寮傚父 涓嶉噸璇�");
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent){
            	// 濡傛灉璇锋眰琚涓烘槸骞傜瓑鐨勶紝閭ｄ箞灏遍噸璇�
            	AbLogUtil.d(mContext, "璇锋眰琚涓烘槸骞傜瓑鐨勶紝閲嶈瘯");
                return true;
            }
            if (exception != null){
                return true;
            }
            return false;
        }
    };


    /**
     * 鑾峰彇鐢ㄦ埛浠ｇ悊
     * @return
     */
    public String getUserAgent(){
        return userAgent;
    }


    /**
     * 璁剧疆鐢ㄦ埛浠ｇ悊
     * @param userAgent
     */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	
	/**
     * 鑾峰彇缂栫爜
     * @return
     */
	public String getEncode() {
		return encode;
	}

	/**
	 * 璁剧疆缂栫爜
	 * @param encode
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}


	/**
	 * 鍏抽棴HttpClient
	 */
	public void shutdown(){
	    if(mHttpClient != null && mHttpClient.getConnectionManager() != null){
	    	mHttpClient.getConnectionManager().shutdown();
	    }
	}


	public CookieStore getCookieStore() {
		if(mHttpClient!=null){
			mCookieStore = mHttpClient.getCookieStore();
		}
		return mCookieStore;
	}


	public void setCookieStore(CookieStore cookieStore) {
		this.mCookieStore = cookieStore;
	}
	
}
