package com.yonyougov.http.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @ClassName BufferedServletRequestWrapper
 * @Description
 * @Author playboy
 * @Date 2020/12/3 2:58 下午
 * @Version 1.0
 **/
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {
    private static Logger log = LoggerFactory.getLogger(BufferedServletRequestWrapper.class);

    private byte[] buffer;

    public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = request.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int read;
        while ((read = is.read(tmp)) > 0) {
            os.write(tmp, 0, read);
        }
        this.buffer = os.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new BufferedServletInputStream(this.buffer);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream(), this.getCharacterEncoding()));
        return reader;
    }
}
