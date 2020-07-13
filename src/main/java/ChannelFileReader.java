import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ChannelFileReader {
    private FileInputStream fileIn;
    private ByteBuffer byteBuf;
    private long fileLength;
    private int arraySize;
    private byte[] array;
    private FileOutputStream fop = null;
    private File file;
    
    private List<byte[]> list = new ArrayList<>();
    
	public ChannelFileReader(String fileName, int arraySize, String targetFileName) throws IOException {
        this.fileIn = new FileInputStream(fileName);
        this.fileLength = fileIn.getChannel().size();
        this.arraySize = arraySize;
        this.byteBuf = ByteBuffer.allocate(arraySize);
        this.file = new File(targetFileName);
        this.fop = new FileOutputStream(file);
    }

    public int read() throws IOException {
        FileChannel fileChannel = fileIn.getChannel();
        int bytes = fileChannel.read(byteBuf);// 读取到ByteBuffer中
        if (bytes != -1) {
            array = new byte[bytes];// 字节数组长度为已读取长度
            list.add(array);
            byteBuf.flip();
            byteBuf.get(array);// 从ByteBuffer中得到字节数组
            fop.write(array);
            fop.flush();
//            System.out.println(Runtime.getRuntime().freeMemory()/1024/1024 + "MB");
            byteBuf.clear();
            return bytes;
        }
        return -1;
    }

    public void close() throws IOException {
        fileIn.close();
        fop.close();
        array = null;
    }

    public byte[] getArray() {
        return array;
    }

    public long getFileLength() {
        return fileLength;
    }

    public static void main(String[] args) throws IOException {
        ChannelFileReader reader = new ChannelFileReader("D:\\迅雷下载\\movie\\【6v电影www.dy131.com】千与千寻BD国粤日三语中字1024高清.mkv", 665536, "D:\\\\迅雷下载\\\\movie\\\\a.rmvb");
        long start = System.currentTimeMillis();
        while (reader.read() != -1) ;
        long end = System.currentTimeMillis();
        reader.close();
        System.out.println("ChannelFileReader: " + (end - start)/1000);
        while(true) {
        	
        }
    }
}