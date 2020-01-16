package hbase;

import logger.CSLogger;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@InterfaceAudience.Public
public class HbaseAdapter extends Configured {

    private final String HBASE_PATH = "/etc/hbase/conf/";
    private final String HADOOP_PATH = "/etc/hadoop/conf/";
    private final String TABLE_NAME = "CDATA";
    private final String COLUMN_FAMILY = "1";
    private final String COLUMN_QUALIFIER = "1";

    private final TableName TABLE = TableName.valueOf(TABLE_NAME);
    private final int POOL_SIZE = 10;
    private final int TASK_COUNT = 100;
    private final byte[] FAMILY = Bytes.toBytes("f");


    private TableName tableElement = TableName.valueOf(TABLE_NAME);

    Connection conn;
    public HbaseAdapter() {
        CSLogger.l.info("Initializing HbaseAdapter.");
        Configuration conf = HBaseConfiguration.create();

        conf.addResource(HBASE_PATH + "hbase-site.xml");
        conf.addResource(HADOOP_PATH + "core-site.xml");
        conf.addResource(HADOOP_PATH + "hdfs-site.xml");
        try {
            conn = ConnectionFactory.createConnection();
            if (conn.isClosed() || conn.isAborted()) {
                CSLogger.l.error("Connection could not be established.");
                throw new IOException();
            }
        }catch(IOException e){
            CSLogger.l.error("Failed to get connection with Hbase.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Table getTable(String tableName){
        Table t = null;
        try {
            Admin admin = conn.getAdmin();
            if (!admin.tableExists(tableElement)) {
                admin.createTable(new HTableDescriptor(tableName).addFamily(new HColumnDescriptor(COLUMN_FAMILY)));
            }
            t = conn.getTable(tableElement);
        }catch(IOException e){
            System.err.println("Could not get table element. "+e);
            e.printStackTrace();
        }
        return t;
    }



    public int putThroughBufferedMutator(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        final BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator bufferedMutator) throws RetriesExhaustedWithDetailsException {
                for (int i = 0; i < e.getNumExceptions(); i++) {
                    CSLogger.l.error("Failed to sent put " + e.getRow(i) + ".");
                }
            }
        };
        BufferedMutatorParams params = new BufferedMutatorParams(TABLE).listener(listener);

        try {
            final Connection conn = ConnectionFactory.createConnection(getConf());
            final BufferedMutator mutator = conn.getBufferedMutator(params);

            final ExecutorService workerPool = Executors.newFixedThreadPool(POOL_SIZE);
            List<Future<Void>> futures = new ArrayList<Future<Void>>(TASK_COUNT);

            for (int i = 0; i < TASK_COUNT; i++) {
                futures.add(workerPool.submit(new Callable<Void>() {
                    public Void call() throws Exception {
                        Put p = new Put(Bytes.toBytes("someRow"));
                        p.addColumn(FAMILY, Bytes.toBytes("someQualifier"), Bytes.toBytes("some values"));
                        mutator.mutate(p);
                        return null;
                    }
                }));
            }

            for (Future<Void> f : futures) {
                f.get(5, TimeUnit.MINUTES);
            }

            workerPool.shutdown();
        } catch (IOException e) {
            CSLogger.l.error("exception while creating / destroying Connection or BufferedMutator", e);

        }
        return 0;
    }



    public boolean putTable(String rowKey, String json, Table table){
        Put p = new Put(Bytes.toBytes(rowKey));
        p.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(COLUMN_QUALIFIER), Bytes.toBytes(json));

        try {
            table.put(p);
        } catch (IOException e) {
            CSLogger.l.error("Error in putting data to hbase.");
            e.printStackTrace();
        }
        return true;
    }

}
