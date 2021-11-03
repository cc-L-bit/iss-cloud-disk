package com.iss.cloud.disk.tools;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import com.obs.services.model.LifecycleConfiguration.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

public class ObsTools
{

	public static final String AK = "YTCIP9GULTRG7KJHN3QF";

	public static final String SK = "vLgUmXZLQkluSEcFRjpsmDmWjiEwVr8LDvXh9iO7";
	// obs.cn-north-4.myhuaweicloud.com
	public static final String ENDPOINT = "http://obs.cn-north-4.myhuaweicloud.com/";


	public static String bucketName = "kp-disk-lcc";


	public static String location = "cn-north-4";

	public static String localFilePath = "C:\\Users\\Administrator\\Desktop\\123\\";

	public static ObsClient getObsClient()
	{
		return new ObsClient(AK, SK, ENDPOINT);
	}

	public static void main(String[] args) throws Exception
	{
		// ObsTools.createBucket();

		// ObsTools.listBuckets();

		// ObsTools.deleteBucket();

		// File file = new File("C:\\Users\\Administrator\\Desktop\\obs.md");
		// ObsTools.uploadFile(file);

		// downloadFile("obs.md");

		// pointUpload("jdk-1.8-windows-x64.exe");
		LifecycleConfiguration config = ObsTools.getObsClient().getBucketLifecycle(bucketName);

		for (Rule rule : config.getRules())
		{
		    System.out.println(rule.getId());
		    System.out.println(rule.getPrefix());
		    for(Transition transition : rule.getTransitions()){
		        System.out.println(transition.getDays());
		        System.out.println(transition.getStorageClass());
		    }
		    System.out.println(rule.getExpiration() != null ? rule.getExpiration().getDays() : "aaaa");
		    for(NoncurrentVersionTransition noncurrentVersionTransition : rule.getNoncurrentVersionTransitions()){
		        System.out.println(noncurrentVersionTransition.getDays());
		        System.out.println(noncurrentVersionTransition.getStorageClass());
		    }
		    System.out.println(rule.getNoncurrentVersionExpiration() != null ? rule.getNoncurrentVersionExpiration().getDays() : "aaaaaaaaaa");
		}
	}

	/**
	 * 创建桶
	 * 
	 */
	public static void createBucket()
	{
		try
		{
			// 创建ObsClient实例
			ObsClient obsClient = getObsClient();

			ObsBucket obsBucket = new ObsBucket();

			obsBucket.setBucketName(bucketName);

			// 设置桶的存储类型为标准存储
			obsBucket.setBucketStorageClass(StorageClassEnum.STANDARD);

			// 设置桶访问权限为公共读，默认是私有读写
			obsBucket.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);

			// 设置桶区域位置
			obsBucket.setLocation(location);

			// 创建桶成功
			HeaderResponse response = obsClient.createBucket(obsBucket);
			System.out.println(response.getRequestId());

			// 关闭
			obsClient.close();
		} catch (ObsException e)
		{
			// 创建桶失败
			printError(e);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void deleteBucket()
	{
		try
		{
			// 创建ObsClient实例
			ObsClient obsClient = getObsClient();

			// 删除桶
			HeaderResponse response = obsClient.deleteBucket(bucketName);
			System.out.println(response.getRequestId());

			// 关闭
			obsClient.close();
		} catch (ObsException e)
		{
			// 删除桶失败
			printError(e);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean deleteObject(String objectKey)
	{
		boolean result = true;
		try
		{
			// 创建ObsClient实例
			ObsClient obsClient = getObsClient();

			// 删除
			obsClient.deleteObject(bucketName, objectKey);

			// 关闭
			obsClient.close();
		} catch (ObsException e)
		{
			// 删除桶失败
			printError(e);
			result = false;
		} catch (IOException e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 列举桶
	 */
	public static List<ObsBucket> listBuckets()
	{
		List<ObsBucket> list = null;
		try
		{
			// 创建ObsClient实例
			ObsClient obsClient = getObsClient();

			// 列举桶
			ListBucketsRequest request = new ListBucketsRequest();
			request.setQueryLocation(true);

			list = obsClient.listBuckets(request);

			// 关闭
			obsClient.close();

		} catch (ObsException e)
		{
			// 列举桶失败
			printError(e);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询文件
	 */
	public static ObsObject getObsObject(String objectKey)
	{
		ObsObject oobj = null;
		try
		{
			// 创建ObsClient实例
			ObsClient obsClient = getObsClient();

			oobj = obsClient.getObject(bucketName, objectKey);
			// 关闭
			obsClient.close();

		} catch (ObsException e)
		{
			// 上传版式文件失败
			printError(e);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return oobj;
	}

	/**
	 * 上传版式文件
	 */
	public static boolean uploadFile(String objectKey, InputStream input)
	{
		boolean result = true;
		try
		{
			// 创建ObsClient实例
			ObsClient obsClient = getObsClient();

			// 删除桶
			HeaderResponse response = obsClient.putObject(bucketName, objectKey, input);

			System.out.println("文件上传成功：" + response.getRequestId());

			// 关闭
			obsClient.close();

		} catch (ObsException e)
		{
			// 上传版式文件失败
			printError(e);
			result = false;
		} catch (IOException e)
		{
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/*
	 * 断点续传
	 */
	public static void pointUpload(String objectKey)
	{
		// 创建ObsClient实例
		ObsClient obsClient = getObsClient();
		UploadFileRequest request = new UploadFileRequest(bucketName, objectKey);
		// 设置待上传的本地文件，localfile为待上传的本地文件路径，需要指定到具体的文件名
		request.setUploadFile("F:\\安装软件\\" + objectKey);
		// 设置分段上传时的最大并发数
		request.setTaskNum(5);
		// 设置分段大小为10MB
		request.setPartSize(10 * 1024 * 1024);
		// 开启断点续传模式
		request.setEnableCheckpoint(true);
		request.setProgressListener(new ProgressListener() {

			@Override
			public void progressChanged(ProgressStatus status)
			{
				// 获取上传进度百分比
				System.out.println("TransferPercentage:" + status.getTransferPercentage());
			}
		});
		try
		{
			// 进行断点续传上传
			CompleteMultipartUploadResult result = obsClient.uploadFile(request);
		} catch (ObsException e)
		{
			printError(e);
			// 发生异常时可再次调用断点续传上传接口进行重新上传
			pointUpload(objectKey);
		}
	}

	/**
	 * 流式下载
	 */
	public static void downloadFile(String objectKey)
	{
		// 创建ObsClient实例
		ObsClient obsClient = getObsClient();

		try
		{
			ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
			ReadableByteChannel rchannel = Channels.newChannel(obsObject.getObjectContent());

			ByteBuffer buffer = ByteBuffer.allocate(4096);
			WritableByteChannel wchannel = Channels
					.newChannel(new FileOutputStream(new File(localFilePath + objectKey.split("/")[2])));

			while (rchannel.read(buffer) != -1)
			{
				buffer.flip();
				wchannel.write(buffer);
				buffer.clear();
			}
			rchannel.close();
			wchannel.close();

		} catch (ObsException e)
		{
			// 流式下载失败
			printError(e);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void removeBucket() {

		// 创建ObsClient实例
		ObsClient obsClient = new ObsClient(AK, SK, ENDPOINT);

		LifecycleConfiguration config = new LifecycleConfiguration();

		Rule rule = config.new Rule();
		rule.setEnabled(true);
		rule.setId("rule1");
		rule.setPrefix("prefix");
		Expiration expiration = config.new Expiration();
		// 指定满足前缀的对象创建60天后过期
		expiration.setDays(1);
		// 直接指定满足前缀的对象过期时间
		// expiration.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-12-31")); 
		rule.setExpiration(expiration);

		NoncurrentVersionExpiration noncurrentVersionExpiration = config.new NoncurrentVersionExpiration();
		// 指定满足前缀的对象成为历史版本60天后过期
		noncurrentVersionExpiration.setDays(60);
		rule.setNoncurrentVersionExpiration(noncurrentVersionExpiration);
		config.addRule(rule);

		obsClient.setBucketLifecycle(bucketName, config);
	}

	public static void printError(ObsException e)
	{
		System.out.println("HTTP Code: " + e.getResponseCode());
		System.out.println("Error Code:" + e.getErrorCode());
		System.out.println("Error Message: " + e.getErrorMessage());
		System.out.println("Request ID:" + e.getErrorRequestId());
		System.out.println("Host ID:" + e.getErrorHostId());
	}

}
