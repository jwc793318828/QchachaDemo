import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.etoak.crawl.page.Page;

public class TestMain {

	private static List<Company> Clist = new ArrayList<Company>();

	public static void main(String[] args) {
			try {
				yanzhengAli();
			yanzhegn();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int page = 1;
//		while (page < 100) {
//			try {
//				System.out.println("size--------------" + Clist.size());
//				http(page);
//			} catch (Exception e) {
//				// TODO: handle exception
//				System.out.println("--------------");
//				getGenerateExcel();
//				return;
//			}
//			page++;
//
//		}
//		getGenerateExcel();
	}

	private static void http(int page) {
		System.out.println("当前第" + page + "页");
		String url;
		if (page == 1) {
			url = "https://www.qichacha.com/search?key=%E5%B9%BF%E5%B7%9E%E5%88%B6%E9%80%A0%E4%B8%9A";
		} else
			url = "https://www.qichacha.com/search_index?key=%E5%B9%BF%E4%B8%9C%E5%88%B6%E9%80%A0%E4%B8%9A#p:" + page
					+ "&";
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"QCCSESSID=p1ft0tgaorlf4a4cogvpi5v7r3; acw_tc=7793461d15611625336878485e5021a0429ce7314f8c35e55c8ba1be7c; UM_distinctid=16b7c89d6492f0-0629935d1baeb5-3c604504-1fa400-16b7c89d64a28b; CNZZDATA1254842228=85523239-1561157384-%7C1561157384; zg_did=%7B%22did%22%3A%20%2216b7c89d81032-018dc4edb6fa46-3c604504-1fa400-16b7c89d811643%22%7D; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1561162537; _uab_collina=156116253853518634866458; hasShow=1; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561162536979%2C%22updated%22%3A%201561162580177%2C%22info%22%3A%201561162536982%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22cuid%22%3A%20%2230ac091ea78c29c624d3b0afb69997c3%22%7D; Hm_lpvt_3456bee468c83cc63fb5147f119f1075=1561162580");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 3.执行 HTTP GET 请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = br.readLine()) != null) {
				stringBuffer.append(str);
			}

			String data = stringBuffer.toString();
			int index1 = data.indexOf("<body>");
			int index2 = data.lastIndexOf("</body>");

			String data_1 = data.substring(data.indexOf("<body>"), data.lastIndexOf("</body>"));
			String table = data_1.substring(data_1.indexOf("<table"), data_1.lastIndexOf("</table>"));

			Document doc = Jsoup.parseBodyFragment(table);
			Element body = doc.body();
			Elements list = body.getElementsByTag("tr");

			for (Element element : list) {
				String companyName = element.getElementsByTag("a").first().text();

				String name = "";
				String price = "";
				String time = "";
				String email = "";
				String call = "";
				String address = "";
				Elements item1 = element.getElementsByClass("m-t-xs");
				Element urls = element.getElementsByClass("ma_h1").first();
				String statustd = element.getElementsByClass("statustd").first()
						.getElementsByClass("nstatus text-success-lt m-l-xs").first().text();
				if (!statustd.equals("在业")) {

					continue;
				}
				String text = urls.attr("href");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String website = getWebsite(text);
				if (website == "") {
					continue;
				}
				for (Element element2 : item1) {
					// 法定代表人： 欧亚伟 注册资本：14426万元人民币 成立日期：1967-04-15
					/**
					 * <p class="m-t-xs">
					 * 法定代表人： <a onclick="zhugeTrack('查企业-搜索列表页-查看法定代表人',{'人物名称':'欧亚伟'});" class=
					 * "text-primary" href="/pl_p12a2b675f587b5f68ede9a9bede857b.html">欧亚伟</a>
					 * <span class="m-l">注册资本：14426万元人民币</span>
					 * <span class="m-l">成立日期：1967-04-15</span>
					 * </p>
					 * <p class="m-t-xs">
					 * 邮箱：13711436948@126.com <span class="m-l">电话：020-84336972</span>
					 * </p>
					 * <p class="m-t-xs">
					 * 地址：广州市南沙区大岗镇潭新公路362号自编203房
					 * </p>
					 */
					String t = element2.text();
					if (t.contains("法定代表人")) {
						name = t.substring("法定代表人：".length(), t.indexOf("注册资本"));
						price = t.substring(t.indexOf("注册资本：") + "注册资本：".length(), t.indexOf("成立日期"));
						time = t.substring(t.indexOf("成立日期：") + "成立日期：".length());
					}
					if (t.contains("邮箱")) {
						email = t.substring("邮箱：".length(), t.indexOf("电话"));
						call = t.substring(t.indexOf("电话：") + "电话：".length());
						if (call.contains("更多号码"))
							call = call.replace("更多号码", "");
					}
					if (t.contains("地址")) {
						address = t.substring("地址：".length());

					}

					// System.out.println(name);
				}
				// String companyName = element.getElementsByTag("a").first().text();
				if (email.trim().length() != 0) {
					Company v = new Company(companyName, statustd, name, price, time, email, call, address, website);
					Clist.add(v);
				}
				// System.out.println(companyName + "\t" + name + "\t" + price + "\t" + time +
				// "\t" + email + "\t" + call + "\t" + address + "\t" + website);

			}
			//
			// System.out.println(table);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
			getGenerateExcel();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
	}

	private static String getWebsite(String _url) throws IOException {
		String url = "https://www.qichacha.com/" + _url;
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"QCCSESSID=p1ft0tgaorlf4a4cogvpi5v7r3; acw_tc=7793461d15611625336878485e5021a0429ce7314f8c35e55c8ba1be7c; UM_distinctid=16b7c89d6492f0-0629935d1baeb5-3c604504-1fa400-16b7c89d64a28b; CNZZDATA1254842228=85523239-1561157384-%7C1561157384; zg_did=%7B%22did%22%3A%20%2216b7c89d81032-018dc4edb6fa46-3c604504-1fa400-16b7c89d811643%22%7D; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1561162537; _uab_collina=156116253853518634866458; hasShow=1; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561162536979%2C%22updated%22%3A%201561162580177%2C%22info%22%3A%201561162536982%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22cuid%22%3A%20%2230ac091ea78c29c624d3b0afb69997c3%22%7D; Hm_lpvt_3456bee468c83cc63fb5147f119f1075=1561162580");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 3.执行 HTTP GET 请求

		int statusCode = httpClient.executeMethod(getMethod);
		// 判断访问的状态码
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + getMethod.getStatusLine());
		}
		// 4.处理 HTTP 响应内容
		InputStream inputStream = getMethod.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer stringBuffer = new StringBuffer();
		String str = "";
		while ((str = br.readLine()) != null) {
			stringBuffer.append(str);
		}

		Document doc = Jsoup.parseBodyFragment(stringBuffer.toString());
		Element content = doc.getElementsByClass("dcontent").first().getElementsByClass("row").first()
				.getElementsByClass("cvlu ").first();
		getMethod.releaseConnection();
		if (content == null) {
			return "";
		}
		return content.text();

	}
	private static long curl ;
	private static void  yanzhengAli() throws HttpException, IOException {
		curl = System.currentTimeMillis();
		String url = "https://cf.aliyun.com/nocaptcha/analyze.jsonp?a=QNYX&t=QNYX%3A"+curl+"%3A0.4401802408723421&n=118%23ZVWZz%2FbUvTKbUewjOZ2CReZTZe7hngwuZH2kes6TzfQZZZZZc5i2YwbYtif4fHCVZZVCZOiTzeWzZgCuc6qYI99wtZChXHRYze2ZZwqhzTxizZZZXTNVzegVC411VHC%2FZZ2uZYqhzHRZZgCZXoqYZH2zZZChXHhbZZ2uZY6DzqquezbDcoTtGWex7gsDc5d3ngDQYKiGZ6W21oFZg72gsmbLOEjknPgZWicF%2FFCrNUXWSuYyRZQZuYPAPQqZZyj6zZG0pVRZXGtCQmNbHNyRbBzRDFeU9CT30YvYuPcKz3Be7DpKD9ZnLXqFwUdd1ro1Y%2Be5BlqZ1FYpqlj73ht5tS7YpZw2CfaliUhez430u5X4AIyNSPIp9mtXCOgoyXrjcct0CKYWt87QXamRaPxQc18ngVB9eSfdYe1r064HQ2r8XPqv3NILcApmAZWv20QmNOerOcTZcZbxpzVjf9K58Pq4EguewDQMjNI1MlGUBsew3djqxuk1rSIOwKhvvSx6lKeZubdWshTxRhVITgylO9gAbA0Y1JbqujnOGGsQMu4Wjvw%2BKKRQfzk0e%2BI9UgIEK32Mu5x9Mv37yDFLHJS0DObbHHZ8uM7zt2zhd%2Fci2gynTzpzyoQEk%2B8zmE3BAkSAor4OC0Nv%2FWOcV0BiyCcn%2FcVKzZuy0gZtv9V1R3BfZgUIxiCbBxb1xg3xSanb6OaFsN2J31Uzipok37GHGHtHrwx9RNrUF9KQc4i3F8bQTEi97plYOVSjiyNOJr0ebCeNOi%2B7kAdHhIXNlUW%2BRo5H6zsjWp8YF5VYVZwOR1fWsiVaPK0s1EM9xYWMdU%2B%2BLhMdUh1UJuDdvQendBZHUtP%2BGRmF5rsMtpHZt1aidNwi%2FZKyddDY1DPPXRNX2lUeQGFDUExJQIu9P5APOI1atit363y5HN0mfl5zKDq%2BE4jXKQUYYULXEuLR2MrIoTdO1qQKdF%2B90MpJqcl6S%2BGmBfF2q12JPX3zbqB4ZZ0IuAhWdWmGBiSA7of0Y0rZT%2Fg2ng%3D%3D&p=%7B%22ncSessionID%22%3A%225f37688616c3%22%7D&scene=register&asyn=0&lang=cn&v=966&callback=jsonp_" +getNumber(0);
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"cna=igkhE3U3mBsCAXkgiqvCPxKU; cnz=wVQ2E/3Au3kCAXkgi0rrnaJm; _ga=GA1.2.398326477.1521444547; UM_distinctid=1687514184f269-02700a4d507bfe-3c604504-1fa400-168751418501da; isg=BLW1YQdaBZYMTWdnwUlWTQAxxDGvmmggjmdUHTfacSx7DtUA_4J5FMOPWJKdSYH8; l=bBITbZ_uv5m4ubZpBOCw5uI8LobOSIRA_uPRwCYXi_5d56L_6dQOlnPQUFp6Vj5RsYTB4IFj7TJ9-etk2");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//		  NameValuePair[] data = { new NameValuePair("csessionid","01uPtcZ8wErsh34kCS77-5n53CaOycNm15zsJbwmt3tP71OZSz28GFpiM2o42FLRO80guSwCyL-M6JiICtRAJzB0Hzrwy86Xy1xTAvWk_gKd7yRVV5NBHXXAyQfYsSw4o-2PxgIi4iN_xbjpJRCNRxzI3qimr-_g_w-m3mW4nE4pY") ,
//				  					new NameValuePair("scene", "register"),
//				  					new NameValuePair("sig", "05JkRFK6UtwzBZKy3RJfMYNE3xe7Hw_DCcD3j8eTTHtZsNENacZGWG8rY5XvoyDsu055j9mRfqBLmXP6oOov8eyxAx85f5nKKgfdwWSDwH7hx9bm5QnJRgC1wV3-AntCe52EbKIlMFBHWQ3gSn2jpnwH4NrdBzjRgeHq9eKfaH5NKsomcC00Xc3bB7XRNv01vAm8vFRWd60RFXkolf46L9a8iDfDxBnldiIzWh3PemHzNK4tk2tJgmrYA6Ui5R1z06ghd6nDGWIlLuj--7GET-8AJ9GBLCPP4pdCyAIfTCFWO6EF88lMp7UeKFHsFhae6xoD71J8tdByhuCxL59v7QdJqBtVm5FDc6TM9Yn39SQStnPS4AEoYYwQnA3NtiLO1T"),
//				  					new NameValuePair("token", "QNYX:1561186802438:0.6515615862626503"),
//				  					new NameValuePair("type", "companyview")};
//		  getMethod.setRequestBody(data);
		// 3.执行 HTTP GET 请求

		int statusCode = httpClient.executeMethod(getMethod);
		// 判断访问的状态码
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + getMethod.getStatusLine());
		}
		// 4.处理 HTTP 响应内容
		InputStream inputStream = getMethod.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer stringBuffer = new StringBuffer();
		String str = "";
		while ((str = br.readLine()) != null) {
			stringBuffer.append(str);
		}
		
		System.out.println(stringBuffer);

	}

	private static void yanzhegn() throws HttpException, IOException {
		String url = "https://www.qichacha.com/index_verifyAction";
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		PostMethod getMethod = new PostMethod(url);
		getMethod.addRequestHeader("Cookie",
				"QCCSESSID=p1ft0tgaorlf4a4cogvpi5v7r3; acw_tc=7793461d15611625336878485e5021a0429ce7314f8c35e55c8ba1be7c; UM_distinctid=16b7c89d6492f0-0629935d1baeb5-3c604504-1fa400-16b7c89d64a28b; CNZZDATA1254842228=85523239-1561157384-%7C1561157384; zg_did=%7B%22did%22%3A%20%2216b7c89d81032-018dc4edb6fa46-3c604504-1fa400-16b7c89d811643%22%7D; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1561162537; _uab_collina=156116253853518634866458; hasShow=1; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561162536979%2C%22updated%22%3A%201561162580177%2C%22info%22%3A%201561162536982%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22cuid%22%3A%20%2230ac091ea78c29c624d3b0afb69997c3%22%7D; Hm_lpvt_3456bee468c83cc63fb5147f119f1075=1561162580");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		NameValuePair[] data = { new NameValuePair("csessionid",
				"01uPtcZ8wErsh34kCS77-5n53CaOycNm15zsJbwmt3tP71OZSz28GFpiM2o42FLRO80guSwCyL-M6JiICtRAJzB0Hzrwy86Xy1xTAvWk_gKd7yRVV5NBHXXAyQfYsSw4o-2PxgIi4iN_xbjpJRCNRxzI3qimr-_g_w-m3mW4nE4pY"),
				new NameValuePair("scene", "register"),
				new NameValuePair("sig",
						"05JkRFK6UtwzBZKy3RJfMYNE3xe7Hw_DCcD3j8eTTHtZsNENacZGWG8rY5XvoyDsu055j9mRfqBLmXP6oOov8eyxAx85f5nKKgfdwWSDwH7hx9bm5QnJRgC1wV3-AntCe52EbKIlMFBHWQ3gSn2jpnwH4NrdBzjRgeHq9eKfaH5NKsomcC00Xc3bB7XRNv01vAm8vFRWd60RFXkolf46L9a8iDfDxBnldiIzWh3PemHzNK4tk2tJgmrYA6Ui5R1z06ghd6nDGWIlLuj--7GET-8AJ9GBLCPP4pdCyAIfTCFWO6EF88lMp7UeKFHsFhae6xoD71J8tdByhuCxL59v7QdJqBtVm5FDc6TM9Yn39SQStnPS4AEoYYwQnA3NtiLO1T"),
				new NameValuePair("token", "QNYX:"+curl+":0.6515615862626503"),
				new NameValuePair("type", "companyview") };
		getMethod.setRequestBody(data);
		// 3.执行 HTTP GET 请求

		int statusCode = httpClient.executeMethod(getMethod);
		// 判断访问的状态码
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + getMethod.getStatusLine());
		}
		// 4.处理 HTTP 响应内容
		InputStream inputStream = getMethod.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer stringBuffer = new StringBuffer();
		String str = "";
		while ((str = br.readLine()) != null) {
			stringBuffer.append(str);
		}

		System.out.println(stringBuffer);

	}

	// 导出 Excel
	public static void getGenerateExcel() {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("商品表一");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		/* style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式 */

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("公司名称");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("经营状态");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("法定代表人");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("注册资本");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("成立时间");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("邮箱");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("电话");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("地址");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("官网");
		cell.setCellStyle(style);

		for (int i = 0; i < Clist.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Company commodity = (Company) Clist.get(i);
			// 第五步，创建单元格，并设置值

			row.createCell(0).setCellValue(commodity.getCompanyName());// 商品名称
			row.createCell(1).setCellValue(commodity.getStatustd());// 商品类型
			row.createCell(2).setCellValue((commodity.getName()).toString());// 销售价格 setSellPrice
			row.createCell(3).setCellValue((commodity.getPrice()));// 所获积分
			row.createCell(4).setCellValue(commodity.getTime());// 库存数量
			row.createCell(5).setCellValue(commodity.getEmail());// 生产许可证
			row.createCell(6).setCellValue(commodity.getCall());// 厂名
			row.createCell(7).setCellValue(commodity.getAddress());// 厂址
			row.createCell(8).setCellValue(commodity.getWebsite());// 厂家联系电话

		}
		// 第六步，将文件存到指定位置
		try {
			Date date = new Date();
			String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(date); // 需要文件导出带有时间的，请把时间拼接到/杠后面去
			FileOutputStream fout = new FileOutputStream("F:\\wb/" + str + "Table.xls");
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getNumber(int size) {

		if (size == 0) {
			size = 18;
		}
		String s = "";
		Random random = new Random();
		s += random.nextInt(9) + 1;
		for (int i = 0; i < size - 1; i++) {
			s += random.nextInt(10);
		}
		BigInteger bigInteger = new BigInteger(s);
		return bigInteger.toString();
	}

}

class Company {
	String companyName;
	String name = "";
	String price = "";
	String time = "";
	String email = "";
	String call = "";
	String address = "";

	String website;
	String statustd;

	public String getStatustd() {
		return statustd;
	}

	public void setStatustd(String statustd) {
		this.statustd = statustd;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Company(String companyName, String statustd, String name, String price, String time, String email,
			String call, String address, String website) {
		super();
		this.companyName = companyName;
		this.statustd = statustd;

		this.name = name;
		this.name = name;
		this.price = price;
		this.time = time;
		this.email = email;
		this.call = call;
		this.address = address;
		this.website = website;
	}

}