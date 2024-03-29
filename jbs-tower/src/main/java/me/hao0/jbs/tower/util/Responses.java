package me.hao0.jbs.tower.util;

import com.google.common.base.Throwables;
import me.hao0.jbs.common.log.Logs;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class Responses {

    private Responses(){}


    public static boolean writeJson(HttpServletResponse resp, Object msg){
        return write(resp, "application/json", msg);
    }


    public static boolean writeText(HttpServletResponse resp, Object msg){
        return write(resp, "text/plain", msg);
    }

    public static boolean write(HttpServletResponse resp, String contentType, Object msg){
        resp.setStatus(200);
        resp.setContentType(contentType);
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(msg);
            out.flush();
            return true;
        } catch (Exception e) {
            Logs.error("failed to writeJson response : {}", Throwables.getStackTraceAsString(e));
            return false;
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }

    public static void disableCache(HttpServletResponse resp){
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Cache-Control", "no-cache,no-store");
    }
}
