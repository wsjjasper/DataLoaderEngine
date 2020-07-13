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
        int bytes = fileChannel.read(byteBuf);// ��ȡ��ByteBuffer��
        if (bytes != -1) {
            array = new byte[bytes];// �ֽ����鳤��Ϊ�Ѷ�ȡ����
            list.add(array);
            byteBuf.flip();
            byteBuf.get(array);// ��ByteBuffer�еõ��ֽ�����
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
        ChannelFileReader reader = new ChannelFileReader("D:\\Ѹ������\\movie\\��6v��Ӱwww.dy131.com��ǧ��ǧѰBD��������������1024����.mkv", 665536, "D:\\\\Ѹ������\\\\movie\\\\a.rmvb");
        long start = System.currentTimeMillis();
        while (reader.read() != -1) ;
        long end = System.currentTimeMillis();
        reader.close();
        System.out.println("ChannelFileReader: " + (end - start)/1000);
        while(true) {
        	
        }
    }
}