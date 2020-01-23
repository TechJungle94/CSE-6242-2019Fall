package edu.gatech.cse6242;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q1 {

    public static class TokenizerMapper
    extends Mapper<Object, Text, Text, Text>{
        

        public void map(Object key, Text value, Context context
                        ) throws IOException, InterruptedException {
            //StringTokenizer itr = new StringTokenizer(value.toString());
            String[] fields = value.toString().split("\\t");
            if(fields.length ==3) {
                String src = fields[0];
                String tgt = fields[1];
		String wei = fields[2];

                context.write(new Text(src),new Text(tgt+","+wei));
		//System.out.println(value.toString());
            }
        }
    }
    
    public static class TextSumReducer
    extends Reducer<Text,Text,Text,Text> {
        private Text result = new Text();
        
        public void reduce(Text key, Iterable<Text> values,
                           Context context
                           ) throws IOException, InterruptedException {
            int largest = 0;
            int index=100000000;
            for (Text value : values) {
            	String[] tokens = value.toString().split(",");
            	//if(tokens.length==2)
            	//{
            	//	int target=Integer.parseInt(tokens[0]);
            	//	int weight=Integer.parseInt(tokens[1]);
            	//}
            	if(Integer.parseInt(tokens[1])>largest) {
                    largest = Integer.parseInt(tokens[1]);
                    index=Integer.parseInt(tokens[0]);
                }else if(Integer.parseInt(tokens[1])==largest) {
			if (Integer.parseInt(tokens[0])<index){
				index=Integer.parseInt(tokens[0]);
			}
		}
	}
            context.write(key, new Text(Integer.toString(index)+","+Integer.toString(largest)));
        
    }
}


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");

    /* TODO: Needs to be implemented */
        job.setJarByClass(Q1.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(TextSumReducer.class);
        job.setReducerClass(TextSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);



    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
