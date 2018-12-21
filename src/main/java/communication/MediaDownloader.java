package communication;


import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaDownloader {


    public MediaDownloader()
    {
        resourceDirectory();
    }

    private void download (final String url) {
        new Thread(new Runnable() {
            public void run() {

            InputStream in = null;
        try {
            HttpURLConnection conn = null;
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(true);
            conn.connect();
            in = conn.getInputStream();
            Byte[] result;
            int readBytes = 0;
            while (true) {
              /*  int length = in.read(result, readBytes, out.length - readBytes);
                if (length == -1) break;
                readBytes += length;
                Logger.getLogger("downloading");*/
            }
           // return readBytes;
        } catch (Exception ex) {
            Logger.getLogger("Exception: "+ex.getMessage());
            //return 0;
        } finally {
        }
            }
        }).start();
    }

    public void downloadImage(final String imageURL)
    {
        Image image = null;
        try {
         //   URL url = new URL("http://www.yahoo.com/image_to_read.jpg");
          //  image = ImageIO.read(url);

            URL url = new URL(imageURL);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = null;
            fos = new FileOutputStream(resourceDirectory()+"/borrowed_image.jpg");
            fos.write(response);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String resourceDirectory()
    {
        String current = System.getProperty("user.dir");
        System.out.println(current);
        return current+"/src/main/resources";
    }



}
