package com.bnrc.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.HttpEntity;
/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bGzipDecompressingEntity.java 
 * 鎻忚堪锛欻ttp瑙ｅ帇鍐呭
 *
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2014-06-17 涓婂崍10:19:52
 */
public class AbGzipDecompressingEntity extends HttpEntityWrapper{
    
    public AbGzipDecompressingEntity(final HttpEntity entity){
        super(entity);
    }

    public InputStream getContent() throws IOException, IllegalStateException{
        InputStream wrappedin = wrappedEntity.getContent();
        return new GZIPInputStream(wrappedin);
    }

    public long getContentLength(){
        return -1;
    }
}