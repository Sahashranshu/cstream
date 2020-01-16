package datafeeder;

import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import static java.lang.System.exit;


public class hdfsInterface {

    private String localPath;
    private String hdfsPath;

    public static final String FS_PARAM_NAME = "fs.defaultFs";

    private boolean initCommonProcess(FileSystem fs, Path locationObject, Configuration conf) throws IOException {
        if(localPath==null || hdfsPath == null){
            exit(1);
        }
        System.out.println("Configured filesystem: "+ conf.get(FS_PARAM_NAME));
        if(fs.exists(locationObject)){
            System.err.println("Output path exists.");
            exit(1);
        }
        return true;
    }


    public boolean LocalToHdfs() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path hdfsLocationObject = new Path(hdfsPath);
        initCommonProcess(fs, hdfsLocationObject, conf);
        OutputStream hdfsStream = fs.create(hdfsLocationObject);
        InputStream localStream = new BufferedInputStream(new FileInputStream(localPath));
        IOUtils.copyBytes(localStream, hdfsStream, conf);
        return true;
    }

    public boolean HdfsToLocal() throws IOException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path hdfsLocationObject = new Path(hdfsPath);

        initCommonProcess(fs, hdfsLocationObject, conf);
        InputStream hdfsStream = fs.open(hdfsLocationObject);
        OutputStream localStream = new BufferedOutputStream(new FileOutputStream(localPath));
        IOUtils.copyBytes(hdfsStream, localStream, conf);

        return true;
    }

    public hdfsInterface setParams(String localPath,
                                          String hdfsPath){
        localPath = this.localPath;
        hdfsPath = this.hdfsPath;
        return this;
    }

}
