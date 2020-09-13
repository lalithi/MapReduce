package s3762890.A1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

// Word count program with in-mapper combiner

public class Task3 {
    private static final Logger LOG = Logger.getLogger(Task2.class);
    public static final String logPrint = "Task 3 by BODIYABADUGE DEWSRI LALITHI PERERA, S3762890";


    // Mapper class
    public static class Task3Mapper extends Mapper<Object, Text, Text, IntWritable> {
        
        private static final Logger LOG = Logger.getLogger(Task3Mapper.class);
        Map<String, Integer> count = new HashMap<String, Integer>();

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            LOG.setLevel(Level.INFO);
            LOG.info("The mapper task of " + logPrint);
            
            StringTokenizer itr = new StringTokenizer(value.toString());
            
            while (itr.hasMoreTokens()) {
                String token = itr.nextToken();

                if(count.containsKey(token)){
                    int sum = (int) count.get(token) + 1;
                    count.put(token, sum);
                }
                else
                    count.put(token, 1);
            }
        }

        public void cleanup(Context context) throws IOException, InterruptedException{
            Iterator<Map.Entry<String, Integer>> temp = count.entrySet().iterator();

            while(temp.hasNext()){
                Map.Entry<String, Integer> entry = temp.next();
                String keyVal = entry.getKey();
                Integer countVal = entry.getValue();

                context.write(new Text(keyVal), new IntWritable(countVal));
            }
        }
    }


    // Reducer class
    public static class Task3Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {
        private static final Logger LOG = Logger.getLogger(Task3Reducer.class);
		private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            LOG.setLevel(Level.INFO);
            LOG.info("The reducer task of " + logPrint);
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main( String[] args ) throws Exception {
        // TODO Auto-generated method stub
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(Task3.class);

        job.setMapperClass(Task3Mapper.class);
        //job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(Task3Reducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileSystem.get(conf).delete(new Path(args[1]),true);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Set log-level to information
        LOG.setLevel(Level.INFO);
        
    	// Log all the arguments passed to the application
        LOG.info("Input path: " + args[0]);
        LOG.info("Output path: " + args[1]);
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    
}
