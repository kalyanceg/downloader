import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;


public class curler {
	static String Filenames[]=new String[10];
public static void Reader(String urlst,int minrange,int maxrange,String temp)throws Exception{
	URL url=new URL(urlst);
    URLConnection urlc;
    urlc = url.openConnection();
    urlc.addRequestProperty("user-agent", "Firefox");
    urlc.addRequestProperty("Range", "bytes="+minrange+"-"+maxrange);
    InputStream in =urlc.getInputStream();
   
     File f=File.createTempFile("temp", "tst"); 
      String fname=f.getName(); 
    OutputStream outputStream = new FileOutputStream (fname); 
    Filenames[Integer.parseInt(temp)]=fname;
    byte[] byteChunk = new byte[1024];
    int n=0;
    while ( (n = in.read(byteChunk)) > 0 ) {
         ByteArrayOutputStream bais = new ByteArrayOutputStream();
    	
        bais.write(byteChunk, 0, n);
         bais.writeTo(outputStream);
      }
   
    
   
    
   
    outputStream.close();
    
}
static int findContentLength(String urlst)throws Exception{
	HttpURLConnection con = (HttpURLConnection) new URL(urlst).openConnection();
    

    int ret= con.getContentLength();
    con.disconnect();
    return ret;
    
}

public static void main(String args[])throws Exception{
	     ArrayList<split> as=new ArrayList<split>();
	     String url=args[0];
	     int cl=findContentLength(url);
	     int range=cl/10;
	     int min[]=new int[10];
	     int max[]=new int[10];
	     for(int i=0;i<10;i++){
	    	 if(i==0)
	    		min[0]=0;
	    	 else
	    		 min[i]=max[i-1]+1;
	    	 if(i==9)
	    		 max[i]=cl;
	    	 else
                 max[i]=min[i]+range-1;    		 
	    		 
	     }
         for(int i=0;i<10;i++){
        	 as.add(new split(url,min[i],max[i],""+i));
         }
         for(int i=0;i<10;i++){
        	as.get(i).t.join();
         }
         if(url.endsWith("/"))
          url=url.substring(0,url.length()-1);
         String sp[]=url.split("/");
         
         FileOutputStream outputStream = new FileOutputStream (sp[sp.length-1]);
         for(int i=0;i<Filenames.length;i++){
        	 File file = new File(Filenames[i]);
        	 byte [] fileData = new byte[(int)file.length()];
        	 DataInputStream dis = new DataInputStream(new FileInputStream(file));
        	 dis.readFully(fileData);
        	 dis.close();
        	 boolean val=file.delete();
        	
        	 outputStream.write(fileData);
             	 
         }
}
}
class split extends Thread{
	Thread t;
	String url;
	int min;
	int max;
	String num;
	public split(String urlst,int minrange,int maxrange,String temp){
		
		url=urlst;
		min=minrange;
		max=maxrange;
		num=temp;
		t=new Thread(this);
		t.start();
	}
	public void run(){
		
		try {
			curler.Reader(url, min, max, num);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
