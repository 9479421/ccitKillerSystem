package vip.wqby.ccitserver.util;

import java.io.ByteArrayInputStream;
import java.util.zip.GZIPInputStream;

public class ByteUtils {
    public static String gzip(byte[] bytes){
        try {
            GZIPInputStream  gzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
            StringBuffer  szBuffer = new StringBuffer ();
            byte  tByte [] = new byte [1024];
            while (true)
            {
                int  iLength = gzip.read (tByte, 0, 1024); // <-- Error comes here
                if (iLength < 0)
                    break;
                szBuffer.append (new String (tByte, 0, iLength));
            }
            return szBuffer.toString();
        }catch (Exception e){
            return "";
        }
    }

}
