# MapReduce
A map-reduce program
___  
<br/>

## **Task 0**

A Mapreduce program for general word count task.

<br/>

## 1. **Task 1**

A MapReduce program to count number of short words (1-4 letters), medium words (5-7 letters) words, long words (8-10 letters) and extra-long words (More than 10 letters).
 
**Execution command:** `$ hadoop jar A1.jar s3762890.A1.Task1 /user/<username>/input /user/<username>/output1 ~/`

**Execution command with S3 dataset:** `$ hadoop jar A1.jar s3762890.A1.Task1 s3a://commoncrawl/crawl-data/CC-MAIN-2018-17/segments/1524125936833.6/wet/CC-MAIN-20180419091546-20180419111546-00036.warc.wet.gz /user/<username>/outputS3_1 ~/`

<br/>

### **Performance Analysis** 
<br/>
  
| Nodes in the cluster |                  | CPU_MILLISECONDS |           |
|----------------------|:----------------:|:----------------:| :-------: |
|                      | **Map Task**     | **Reducer Task** | **Total** |
|    **3 Nodes**       |  76990 (97.16%)  |  2250 (2.84%)    |   79240   |
|    **5 Nodes**       |  76020 (97.3%)   |  2110 (2.7%)     |   78130   |
|    **7 Nodes**       |  75990 (97.55%)  |  1910 (2.45%)    |   77900   |

<br/>

#### **Observation 1:**

- Map tasks consume around 97% of the total time while reduce tasks takes only 2%-3% of the total time.

**Explanation:** 

In this particular task the mappers process a high volume of data in a CPU intensive fashion, but output comparatively little data (4 keys) to the reducers. This explains, the high deviation in the time taken to Map tasks (high) and Reducer tasks (low). And as there are only very few reducer tasks, there is no significant effect in adding new nodes.
<br/>
<br/>

#### **Observation 2:**

- Adding more nodes (i.e doubling the number of slave nodes) has increased performance, but very insignificantly. When increasing 2 slave nodes to 4 slave nodes performance has increased from 1110-milliseconds (1.4%) and from 4 nodes to 6 nodes 230-millisecond increment (0.29%).

**Explanation:** 

It is obvious that the increasing number of nodes does speed up the processing of a job by providing more slots to execute the tasks. However, the decrease of the time is not that significant as a proportion.
In a Hadoop job, it's tasks, mapper and reducer, are assigned to slots. And each node is allocated a certain number of slots for map tasks and slots for reducer tasks. Further, reducers get to read a lot of their data from the mappers running on the local node, and adding more nodes shifts more of the data to being pulled over the much slower network resulting in lower performance.

<br/>
<br/>

#### **Observation 3:**

- Adding more nodes has increased performance of reducer tasks specifically with respect to the total time, but the inverse effect on mapper tasks. i.e When increasing nodes, only the reducer tasks have gain better performance in this scenario.

**Explanation:** 

Reducers get to read a most of their data from the mappers running on the local node, and adding more nodes shifts more of the data to being pulled over towards a much slower network.

<br/>

<br/>
___
<br/>

## 2. **Task 2**

A MapReduce program that outputs a count of all words that begin with a vowel and count of all how many words that begin with a consonant


**Execution command:** `$ hadoop jar A1.jar s3762890.A1.Task2 /user/<username>/input /user/<username>/output2 ~/`

___
<br/>

## 3. **Task 3**

A MapReduce program to count the number of each word where the in-mapper combining is implemented rather than an independent combiner.

**Execution command:** `$ hadoop jar A1.jar s3762890.A1.Task3 /user/<username>/input /user/<username>/output3 ~/`

___
<br/>

## 4. **Task 4**

A MapReduce program with a partitioner (An extention of Task 1)

**Execution command:** `$ hadoop jar A1.jar s3762890.A1.Task4 /user/<username>/input /user/<username>/output4 ~/`

