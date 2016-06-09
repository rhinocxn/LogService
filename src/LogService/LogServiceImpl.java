package LogService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.Histogram;
import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetHistogramsRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.request.ListLogStoresRequest;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import com.aliyun.openservices.log.response.GetLogsResponse;

public class LogServiceImpl implements LogService{
	public long getPV(int from, int to) throws  LogException,InterruptedException {
		String endpoint = "cn-hangzhou.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的
		// Endpoint
		String accessKeyId = "7EK29AVBiS0JZN1k"; // 使用你的阿里云访问秘钥AccessKeyId
		String accessKeySecret = "2Vc97PajW8fMYwPIQI1349MrOsHDMJ"; // 使用你的阿里云访问秘钥AccessKeySecret
		String project = "dashboard-demo"; // 上面步骤创建的项目名称
		String logstore = "access-log"; // 上面步骤创建的日志库名称

		// 构建一个客户端实例
		Client client = new Client(endpoint, accessKeyId, accessKeySecret);

		// 查询日志分布情况
		String topic = "";
		String query = "";
		/*int from = (int) (new Date().getTime() / 1000 - 300);
		int to = (int) (new Date().getTime() / 1000);*/
		GetHistogramsResponse res3 = null;
		while (true) {
			GetHistogramsRequest req3 = new GetHistogramsRequest(project,
					logstore, topic, query, from, to);
			res3 = client.GetHistograms(req3);
			if (res3 != null && res3.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
			{
				break;
			}
			Thread.sleep(200);
		}

		System.out.println("Total count of logs is " + res3.GetTotalCount());
		return res3.GetTotalCount();
    }
	
	public int getUV(int from,int to) throws  LogException,InterruptedException {
    	String endpoint = "cn-hangzhou.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的
		// Endpoint
		String accessKeyId = "7EK29AVBiS0JZN1k"; // 使用你的阿里云访问秘钥AccessKeyId
		String accessKeySecret = "2Vc97PajW8fMYwPIQI1349MrOsHDMJ"; // 使用你的阿里云访问秘钥AccessKeySecret
		String project = "dashboard-demo"; // 上面步骤创建的项目名称
		String logstore = "access-log"; // 上面步骤创建的日志库名称

		// 构建一个客户端实例
		Client client = new Client(endpoint, accessKeyId, accessKeySecret);

		// 查询日志分布情况
		String topic = "";
		String query = "";
		/*int from = (int) (new Date().getTime() / 1000 - 3600);
		int to = (int) (new Date().getTime() / 1000);*/
		GetLogsResponse res = null;
		
		int tmpcount = -1;
		int totalcount = 0;
		int keyoffset = -1;
		ArrayList<String> ary = new ArrayList<String>();
		
		while (true) {
			GetLogsRequest req = new GetLogsRequest(project,
					logstore, from, to, topic, query);
			res = client.GetLogs(req);
			if (res != null && res.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
			{
				break;
			}
			Thread.sleep(200);
		}
		
		
		System.out.println("Tmp count of logs is " + res.GetCount());
		tmpcount = res.GetCount();
		
		//输出
		/*for(QueriedLog log : res.GetLogs()){
			for(LogContent logcontent : log.GetLogItem().GetLogContents()){
				System.out.printf("Key:%s Value:%s ",logcontent.GetKey(),logcontent.GetValue());
			}
			System.out.printf("\n");
		}*/
		
		for(QueriedLog log : res.GetLogs()){
			if(keyoffset == -1){
				int tmpi=0;
				for(LogContent logcontent : log.GetLogItem().GetLogContents()){
					if(logcontent.GetKey().equals("ip")){
						keyoffset = tmpi;
					}
					tmpi++;
				}
				if(keyoffset == -1){
					System.out.printf("ERROR:No Such Key\n");
					break;
				}
			}
			if(!ary.contains(log.GetLogItem().GetLogContents().get(keyoffset).GetValue())){
				ary.add(log.GetLogItem().GetLogContents().get(keyoffset).GetValue());
			}
		}
		
		for(int i=1; tmpcount >0; i++){
			while (true) {
				GetLogsRequest req = new GetLogsRequest(project,
						logstore, from, to, topic, query, 100*i , 100, false);
				res = client.GetLogs(req);
				if (res != null && res.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
				{
					break;
				}
				Thread.sleep(200);
			}
			
			System.out.println("Tmp count of logs is " + res.GetCount());
			tmpcount = res.GetCount();
			
			//输出
			/*for(QueriedLog log : res.GetLogs()){
				for(LogContent logcontent : log.GetLogItem().GetLogContents()){
					System.out.printf("Key:%s Value:%s ",logcontent.GetKey(),logcontent.GetValue());
				}
				System.out.printf("\n");
			}*/
			
			for(QueriedLog log : res.GetLogs()){
				if(keyoffset == -1){
					System.out.printf("ERROR:No Such Key\n");
					break;
				}
				if(!ary.contains(log.GetLogItem().GetLogContents().get(keyoffset).GetValue())){
					ary.add(log.GetLogItem().GetLogContents().get(keyoffset).GetValue());
				}
			}
			
			totalcount = ary.size();
			
		}
		System.out.println("Total count of ips is " + totalcount);
		return totalcount;
    }
	
	
	public void getPath1day(String[] pathparm, int[] count) throws  LogException,InterruptedException {
    	String endpoint = "cn-hangzhou.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的
		// Endpoint
		String accessKeyId = "7EK29AVBiS0JZN1k"; // 使用你的阿里云访问秘钥AccessKeyId
		String accessKeySecret = "2Vc97PajW8fMYwPIQI1349MrOsHDMJ"; // 使用你的阿里云访问秘钥AccessKeySecret
		String project = "dashboard-demo"; // 上面步骤创建的项目名称
		String logstore = "access-log"; // 上面步骤创建的日志库名称

		// 构建一个客户端实例
		Client client = new Client(endpoint, accessKeyId, accessKeySecret);

		// 查询日志分布情况
		String topic = "";
		String query = "";
		int from = (int) (new Date().getTime() / 1000 - 3600);
		int to = (int) (new Date().getTime() / 1000);
		GetLogsResponse res = null;
		
		int tmpcount = -1;
		int keyoffset = -1;
		Map<String,Integer> pathmap = new HashMap<String,Integer>();
		Map<String,Integer> parmmap = new HashMap<String,Integer>();
		
		while (true) {
			GetLogsRequest req = new GetLogsRequest(project,
					logstore, from, to, topic, query);
			res = client.GetLogs(req);
			if (res != null && res.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
			{
				break;
			}
			Thread.sleep(200);
		}
		
		
		//System.out.println("Tmp count of logs is " + res.GetCount());
		tmpcount = res.GetCount();
		
		//输出
		for(QueriedLog log : res.GetLogs()){
			for(LogContent logcontent : log.GetLogItem().GetLogContents()){
				System.out.printf("Key:%s Value:%s ",logcontent.GetKey(),logcontent.GetValue());
			}
			System.out.printf("\n");
		}
		
		for(QueriedLog log : res.GetLogs()){
			if(keyoffset == -1){
				int tmpi=0;
				for(LogContent logcontent : log.GetLogItem().GetLogContents()){
					if(logcontent.GetKey().equals("path")){
						keyoffset = tmpi;
					}
					tmpi++;
				}
				if(keyoffset == -1){
					System.out.printf("ERROR:No Such Key\n");
					break;
				}
			}
			String[] pathsplit = log.GetLogItem().GetLogContents().get(keyoffset).GetValue().split("\\?", 2);
			//System.out.printf(pathsplit[0] + ":" + pathsplit[1] + "\n");
			//path count
			if(!pathmap.containsKey(pathsplit[0])){
				pathmap.put(pathsplit[0],1);
			}
			else{
				int tmp = pathmap.get(pathsplit[0]);
				pathmap.remove(pathsplit[0]);
				pathmap.put(pathsplit[0], ++tmp);
			}
			//parameter count
			if(!parmmap.containsKey(pathsplit[1])){
				parmmap.put(pathsplit[1],1);
			}
			else{
				int tmp = parmmap.get(pathsplit[1]);
				parmmap.remove(pathsplit[1]);
				parmmap.put(pathsplit[1], ++tmp);
			}
		}
		
		for(int i=1; tmpcount >0; i++){
			while (true) {
				GetLogsRequest req = new GetLogsRequest(project,
						logstore, from, to, topic, query, 100*i , 100, false);
				res = client.GetLogs(req);
				if (res != null && res.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
				{
					break;
				}
				Thread.sleep(200);
			}
			
			//System.out.println("Tmp count of logs is " + res.GetCount());
			tmpcount = res.GetCount();
			
			//输出
			for(QueriedLog log : res.GetLogs()){
				for(LogContent logcontent : log.GetLogItem().GetLogContents()){
					System.out.printf("Key:%s Value:%s ",logcontent.GetKey(),logcontent.GetValue());
				}
				System.out.printf("\n");
			}
			
			for(QueriedLog log : res.GetLogs()){
				if(keyoffset == -1){
					System.out.printf("ERROR:No Such Key\n");
					break;
				}
				String[] pathsplit = log.GetLogItem().GetLogContents().get(keyoffset).GetValue().split("\\?", 2);
				//System.out.printf(pathsplit[0] + ":" + pathsplit[1] + "\n");
				//path count
				if(!pathmap.containsKey(pathsplit[0])){
					pathmap.put(pathsplit[0],1);
				}
				else{
					int tmp = pathmap.get(pathsplit[0]);
					pathmap.remove(pathsplit[0]);
					pathmap.put(pathsplit[0], ++tmp);
				}
				//parameter count
				if(!parmmap.containsKey(pathsplit[1])){
					parmmap.put(pathsplit[1],1);
				}
				else{
					int tmp = parmmap.get(pathsplit[1]);
					parmmap.remove(pathsplit[1]);
					parmmap.put(pathsplit[1], ++tmp);
				}
			}
			
		}
		
		System.out.println(pathmap);
		System.out.println(parmmap);
		
		String maxpath = null;
		int maxpathcount = 0;
		String maxparm = null;
		int maxparmcount = 0;
		for(String tmpkey: pathmap.keySet()){
			if(maxpathcount < pathmap.get(tmpkey)){
				maxpathcount = pathmap.get(tmpkey);
				maxpath = tmpkey;
			}
		}
		for(String tmpkey: parmmap.keySet()){
			if(maxparmcount < parmmap.get(tmpkey)){
				maxparmcount = parmmap.get(tmpkey);
				maxparm = tmpkey;
			}
		}
		System.out.println(maxpath + ":" + maxpathcount);
		System.out.println(maxparm + ":" + maxparmcount);
		pathparm[0] = maxpath;
		pathparm[1] = maxparm;
		count[0] = maxpathcount;
		count[1] = maxparmcount;
    }
	
	public Map<String,Integer> getMethod(int from,int to) throws  LogException,InterruptedException {
    	String endpoint = "cn-hangzhou.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的
		// Endpoint
		String accessKeyId = "7EK29AVBiS0JZN1k"; // 使用你的阿里云访问秘钥AccessKeyId
		String accessKeySecret = "2Vc97PajW8fMYwPIQI1349MrOsHDMJ"; // 使用你的阿里云访问秘钥AccessKeySecret
		String project = "dashboard-demo"; // 上面步骤创建的项目名称
		String logstore = "access-log"; // 上面步骤创建的日志库名称

		// 构建一个客户端实例
		Client client = new Client(endpoint, accessKeyId, accessKeySecret);

		// 查询日志分布情况
		String topic = "";
		String query = "";
		/*int from = (int) (new Date().getTime() / 1000 - 3600);
		int to = (int) (new Date().getTime() / 1000);*/
		GetLogsResponse res = null;
		
		int tmpcount = -1;
		int keyoffset = -1;
		Map<String,Integer> methodmap = new HashMap<String,Integer>();
		
		while (true) {
			GetLogsRequest req = new GetLogsRequest(project,
					logstore, from, to, topic, query);
			res = client.GetLogs(req);
			if (res != null && res.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
			{
				break;
			}
			Thread.sleep(200);
		}
		
		
		//System.out.println("Tmp count of logs is " + res.GetCount());
		tmpcount = res.GetCount();
		
		for(QueriedLog log : res.GetLogs()){
			if(keyoffset == -1){
				int tmpi=0;
				for(LogContent logcontent : log.GetLogItem().GetLogContents()){
					if(logcontent.GetKey().equals("method")){
						keyoffset = tmpi;
					}
					tmpi++;
				}
				if(keyoffset == -1){
					System.out.printf("ERROR:No Such Key\n");
					break;
				}
			}
			
			String method = log.GetLogItem().GetLogContents().get(keyoffset).GetValue();
			//path count
			if(!methodmap.containsKey(method)){
				methodmap.put(method,1);
			}
			else{
				int tmp = methodmap.get(method);
				methodmap.remove(method);
				methodmap.put(method, ++tmp);
			}
		}
		
		for(int i=1; tmpcount >0; i++){
			while (true) {
				GetLogsRequest req = new GetLogsRequest(project,
						logstore, from, to, topic, query, 100*i , 100, false);
				res = client.GetLogs(req);
				if (res != null && res.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
				{
					break;
				}
				Thread.sleep(200);
			}
			
			//System.out.println("Tmp count of logs is " + res.GetCount());
			tmpcount = res.GetCount();
			
			//输出
			
			
			for(QueriedLog log : res.GetLogs()){
				if(keyoffset == -1){
					System.out.printf("ERROR:No Such Key\n");
					break;
				}
				String method = log.GetLogItem().GetLogContents().get(keyoffset).GetValue();
				//path count
				if(!methodmap.containsKey(method)){
					methodmap.put(method,1);
				}
				else{
					int tmp = methodmap.get(method);
					methodmap.remove(method);
					methodmap.put(method, ++tmp);
				}
			}
			
		}
		
		System.out.println(methodmap);
		return methodmap;
    }
	
	public void test() throws LogException,InterruptedException {
		String endpoint = "cn-hangzhou.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的
		                                            // Endpoint
		String accessKeyId = "7EK29AVBiS0JZN1k"; // 使用你的阿里云访问秘钥AccessKeyId
		String accessKeySecret = "2Vc97PajW8fMYwPIQI1349MrOsHDMJ"; // 使用你的阿里云访问秘钥AccessKeySecret
		String project = "dashboard-demo"; // 上面步骤创建的项目名称
		String logstore = "access-log"; // 上面步骤创建的日志库名称
		
		
		// 构建一个客户端实例
		Client client = new Client(endpoint, accessKeyId, accessKeySecret);
		
		// 列出当前Project下的所有日志库名称
		int offset = 0;
		int size = 100;
		String logStoreSubName = "";
		ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset,
		        size, logStoreSubName);
		ArrayList<String> logStores = client.ListLogStores(req1).GetLogStores();
		System.out.println("ListLogs:" + logStores.toString() + "\n");
		
		// 写入日志
		String topic = "";
		String source = "";
		
		
		
		
		// 查询日志分布情况
		String query = "get";
		int from = (int) (new Date().getTime() / 1000 - 300);
		int to = (int) (new Date().getTime() / 1000);
		GetHistogramsResponse res3 = null;
		while (true) {
		    GetHistogramsRequest req3 = new GetHistogramsRequest(project,
		            logstore, topic, query, from, to);
		    res3 = client.GetHistograms(req3);
		    if (res3 != null && res3.IsCompleted()) // IsCompleted()返回true，表示查询结果是准确的，如果返回false，则重复查询
		    {
		        break;
		    }
		    Thread.sleep(200);
		}
		
		System.out.println("Total count of logs is " + res3.GetTotalCount());
		for (Histogram ht : res3.GetHistograms()) {
		    System.out.printf("from %d, to %d, count %d.\n", ht.GetFrom(),
		            ht.GetTo(), ht.GetCount());
		}
		
		// 查询日志数据
		long total_log_lines = res3.GetTotalCount();
		int log_offset = 0;
		int log_line = 10;
		while (log_offset <= total_log_lines) {
		    GetLogsResponse res4 = null;
		    // 对于每个log offset,一次读取10行log，如果读取失败，最多重复读取3次。
		    for (int retry_time = 0; retry_time < 3; retry_time++) {
		        GetLogsRequest req4 = new GetLogsRequest(project, logstore,
		                from, to, topic, query, log_offset, log_line, false);
		        res4 = client.GetLogs(req4);
		        if (res4 != null && res4.IsCompleted()) {
		            break;
		        }
		        Thread.sleep(200);
		    }
		    System.out.println("Read log count:"
		            + String.valueOf(res4.GetCount()));
		    log_offset += log_line;
		}
	
	}
}