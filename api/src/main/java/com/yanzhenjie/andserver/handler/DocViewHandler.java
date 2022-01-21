package com.yanzhenjie.andserver.handler;

import android.content.Context;
import android.webkit.MimeTypeMap;

import com.yanzhenjie.andserver.DispatcherHandler;
import com.yanzhenjie.andserver.cache.WebdocCache;
import com.yanzhenjie.andserver.config.WebDocConfig;
import com.yanzhenjie.andserver.entity.DocEntry;
import com.yanzhenjie.andserver.framework.body.StreamBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.util.DateUtil;
import com.yanzhenjie.andserver.util.GroupByExtensionUtil;
import com.yanzhenjie.andserver.util.MediaType;
import com.yanzhenjie.andserver.util.MimeType;
import com.yanzhenjie.andserver.util.MimeTypeUtil;
import com.yanzhenjie.andserver.util.RequestLineUtil;
import com.yanzhenjie.andserver.util.StringUtils;
import com.yanzhenjie.andserver.util.ZipUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static com.yanzhenjie.andserver.http.HttpHeaders.LOCATION;
import static com.yanzhenjie.andserver.http.StatusCode.SC_FOUND;

public class DocViewHandler extends DispatcherHandler {

    public static final String NAME = "doc-view";
    private static final String PATH = "/doc-view/";
    private URLDecoder decoder = new URLDecoder();

    private final Context mContext;

    public DocViewHandler(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response){
        try {
            String str1 = RequestLineUtil.getRequestURI(request, response);
            String str2 = request.getURI();
            str2 = str2.replace("scheme:", "");
            str2 = URLDecoder.decode(str2, "UTF-8");
            DocEntry docEntry = RequestLineUtil.processDocEntry(str2);
            System.out.println(docEntry.getDoc());
            System.out.println(docEntry.getEntry());
            String str3 = WebDocConfig.getBasePath(mContext);
            String str4 = str3 + docEntry.getDoc();
            List<String> list = WebdocCache.getCache(docEntry.getDoc());
            boolean bool = true;
            if (list.isEmpty()) {
                list = ZipUtil.entryList2(str4);
                if (list.isEmpty()) {
                    Date date = DateUtil.bowerCache();
                    response.setHeader("Content-Type", "text/html; charset=utf-8");
                    response.addHeader("Expires", DateUtil.dateFormat(date, TimeZone.getTimeZone("GMT+8")));
                    response.addHeader("Cache-Control", Long.toString(date.getTime() - Calendar.getInstance().getTime().getTime()));
                    response.setStatus(404);
                    return;
                }
            }
            WebdocCache.addCache(docEntry.getDoc(), list);
            bool = list.contains(docEntry.getEntry());
            if (!bool) {
                indexEntry(list, docEntry);
                bool = list.contains(docEntry.getEntry());
            }
            if (!bool)
                defaultEntry(list, docEntry);
            String str5 = "%s%s/%s!/%s";
            if (str2.indexOf("!/" + docEntry.getEntry()) == -1) {
                String str = String.format(str5, new Object[] { str1, "doc-view", URLEncoder.encode(docEntry.getDoc(), "UTF-8"), docEntry.getEntry() });
                System.out.println("DocViewHandler Redirect:" + str);
                response.setHeader(LOCATION, str);
                response.setStatus(SC_FOUND);
                return;
            }
//            String str6 = MimeTypeMap.getFileExtensionFromUrl(docEntry.getEntry());
            String str6 = MimeTypeUtil.getMimeTypeForExtension(StringUtils.getFilenameExtension(docEntry.getEntry()));
            response.setHeader("Content-Type",String.format("%s; charset=utf-8", new Object[] { str6 }));
            response.setStatus(200);
            writeResource(response, str4, docEntry.getEntry());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeResource(HttpResponse response, String path, String entryName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(path)));
        ZipEntry entry = null;
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((entry = zis.getNextEntry()) != null){
            if(entry.getName().equals(entryName)){
                while((len = zis.read(buffer, 0, buffer.length)) != -1){
                    baos.write(buffer, 0, len);
                }
                break;
            }
        }*/
        int len = -1;
        byte[] buffer = new byte[1024];
        InputStream is = ZipUtil.getResource(new ZipFile(new File(path)), entryName);
        while((len = is.read(buffer, 0, buffer.length)) != -1){
            baos.write(buffer, 0, len);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        response.setBody(new StreamBody(bais, baos.toByteArray().length));
    }


    public static void defaultEntry(List<String> paramList, DocEntry paramDocEntry) {
        Map map = GroupByExtensionUtil.groupByDoc(paramList);
        List<String> list = (List)map.get("html");
        if (list != null && !list.isEmpty()) {
            GroupByExtensionUtil.sort(list);
            paramDocEntry.setEntry(list.get(0));
            return;
        }
        Set set = map.entrySet();
        Iterator<Map.Entry> iterator = set.iterator();
        if (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            GroupByExtensionUtil.sort((List)entry.getValue());
            paramDocEntry.setEntry(((List<String>)entry.getValue()).get(0));
        }
    }

    public static void indexEntry(List<String> paramList, DocEntry paramDocEntry) {
        GroupByExtensionUtil.sort(paramList);
        for (String str : paramList) {
            if (str.indexOf("index.html") != -1) {
                paramDocEntry.setEntry(str);
                break;
            }
        }
    }
}
