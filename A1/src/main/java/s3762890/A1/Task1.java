package s3762890.A1;

import java.io.IOException;
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

// Word count program based on length of the word

public class Task1 {
	private static final Logger LOG = Logger.getLogger(Task1.class);
	public static final String logPrint = "Task 1 by BODIYABADUGE DEWSRI LALITHI PERERA, S3762890";


	// Mapper class
	public static class Task1Mapper extends Mapper<Object, Text, Text, IntWritable>{

		private static final Logger LOG = Logger.getLogger(Task1Mapper.class);

		private final static IntWritable one = new IntWritable(1);
		private Text category = new Text();

		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			LOG.setLevel(Level.INFO);
			LOG.info("The mapper task of " + logPrint);
			
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				String word = itr.nextToken();
				int length = word.length();
				String c = "";

				if(length >= 1 && length <= 4)
					c = "short" ;
				else if(length >= 5 && length <= 7)
					c = "medium";
				else if (length >= 8 && length <= 10)
					c = "long";
				else
					c = "extra-long";
				
				//Text t = new Text(c);

				category.set(c);
				context.write(category, one);
				
			}
		}
	}

	// Reducer class
	public static class Task1Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {
		private static final Logger LOG = Logger.getLogger(Task1Reducer.class);
		private IntWritable result = new IntWritable();
		
		public void reduce(Text key, Iterable<IntWritable> values,Context context
				) throws IOException, InterruptedException {
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

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");

		job.setJarByClass(Task1.class);
		job.setMapperClass(Task1Mapper.class);
		job.setCombinerClass(Task1Reducer.class);
		job.setReducerClass(Task1Reducer.class);

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
