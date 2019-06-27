import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
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
import com.google.gson.Gson;

class Student {
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student(String num, String name) {
		super();
		this.num = num;
		this.name = name;
	}

	private String num;
	private String name;

	@Override
	public boolean equals(Object obj) {
		Student s = (Student) obj;
		return num.equals(s.num) && name.equals(s.name);
	}

	@Override
	public int hashCode() {
		String in = num + name;
		return in.hashCode();
	}

}

public class TestMain {

	private static List<Company> Clist = new ArrayList<Company>();

	public static void main(String[] args) {

//		try {
//			yanzhengAli1();
//			yanzhengAli2();
//
//		    yanzhegn();
//		} catch (HttpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (true) {
//			
//			return;
//		}
 		int page = 45;
		while (page < 251) {
			try {
				System.out.println("size--------------" + Clist.size());
				http(page);
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				try {
					
					getGenerateExcel();

				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				return;
			}
			page++;

		}
		getGenerateExcel();
	}

	private static void http(int page) {
		System.out.println("当前第" + page + "页");
		String url = "https://www.qichacha.com/search_index?key=%E5%B9%BF%E5%B7%9E%E5%88%B6%E9%80%A0%E4%B8%9A&ajaxflag=1&p="
				+ page + "&ajaxflag=1";
//		if (page == 1) {
//			url 
//		} else
//			url = "https://www.qichacha.com/search_index?key=%E5%B9%BF%E4%B8%9C%E5%88%B6%E9%80%A0%E4%B8%9A#p:" + page
//					+ "&";
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"UM_distinctid=16b72c0ff50555-0d50f17e5c8578-3c604504-1fa400-16b72c0ff51660; zg_did=%7B%22did%22%3A%20%2216b72c0ff5f572-011d0e55575c9-3c604504-1fa400-16b72c0ff603ad%22%7D; _uab_collina=156099837952298970109224; acw_tc=7793462615609983826941398e8cdd0239ea250a3904a4f429fad0e6cf; QCCSESSID=mc2p8ji3nsptae2g1uab251ki2; zg_63e87cf22c3e4816a30bfbae9ded4af2=%7B%22sid%22%3A%201561098941206%2C%22updated%22%3A%201561099006894%2C%22info%22%3A%201561098941207%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%7D; hasShow=1; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1561357674,1561366594,1561426571,1561609861; CNZZDATA1254842228=1382632714-1560996794-%7C1561613490; Hm_lpvt_3456bee468c83cc63fb5147f119f1075=1561615529; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561615383414%2C%22updated%22%3A%201561615573768%2C%22info%22%3A%201561609857021%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22www.qichacha.com%22%2C%22cuid%22%3A%20%22d3da367ce98638adec75c748c78c6c89%22%7D");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
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
			String dataString = stringBuffer.toString();
			Document doc = Jsoup.parseBodyFragment(stringBuffer.toString());

			Element body = doc.body();
			Element itemElement = body.getElementsByTag("tbody").first();
			Elements list = itemElement.getElementsByTag("tr");

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
				String statustd = element.getElementsByClass("statustd").first().getElementsByTag("span").first()
						.text();
				if (!statustd.equals("在业")) {

					continue;
				}
				String text = urls.attr("href");
				String website = "";
				try {
					website = getWebsite(text, false);
					if (website == "") {
						continue;
					}
				} catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("进入验证模式--------");
						yanzhengAli1();
						yanzhengAli2();
						yanzhegn();
						website = getWebsite(text, true);
						if (website == "") {
							continue;
						}
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println("验证存在问题-------");
					}
				}

				for (Element element2 : item1) {

					String t = element2.text();
					if (t.contains("注册资本") || t.contains("成立日期")) {
						try {
							name = element2.getElementsByTag("a").first().text();
						} catch (Exception e) {
							// TODO: handle exception
							name = t.substring(t.indexOf("：")+1, t.indexOf("注册资本："));
						}

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
				System.out.println(companyName + "\t" + name + "\t" + price + "\t" + time + "\t" + email + "\t" + call
						+ "\t" + address + "\t" + website);

			}

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

	private static String getWebsite(String _url, boolean isYanZ) throws IOException {
		String url = "https://www.qichacha.com/" + _url;
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"UM_distinctid=16b72c0ff50555-0d50f17e5c8578-3c604504-1fa400-16b72c0ff51660; zg_did=%7B%22did%22%3A%20%2216b72c0ff5f572-011d0e55575c9-3c604504-1fa400-16b72c0ff603ad%22%7D; _uab_collina=156099837952298970109224; acw_tc=7793462615609983826941398e8cdd0239ea250a3904a4f429fad0e6cf; QCCSESSID=mc2p8ji3nsptae2g1uab251ki2; zg_63e87cf22c3e4816a30bfbae9ded4af2=%7B%22sid%22%3A%201561098941206%2C%22updated%22%3A%201561099006894%2C%22info%22%3A%201561098941207%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%7D; CNZZDATA1254842228=1382632714-1560996794-%7C1561336093; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1560998380,1561098787,1561108279,1561340558; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561340557360%2C%22updated%22%3A%201561340630175%2C%22info%22%3A%201560998379364%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22cuid%22%3A%20%22382bbd2e7e716de67d7c57bdbbe585ab%22%7D; Hm_lpvt_3456bee468c83cc63fb5147f119f1075="
						+ yanTime);
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		// 设置 get 请求超时 5s
		if (isYanZ) {
			System.out.println("------------是验证模式");
			getMethod.addRequestHeader("Referer",
					"https://www.qichacha.com/index_verify?type=companyview&back=" + _url);
		}

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

	private static void yanzhengAli1() throws HttpException, IOException {
		curl = System.currentTimeMillis();
		String url = "https://cf.aliyun.com/nocaptcha/initialize.jsonp?a=QNYX&t=QNYX%3A" + curl
				+ "%3A0.21306380947982229&scene=register&lang=cn&v=v1.2.17&href=https%3A%2F%2Fwww.qichacha.com%2Findex_verify&comm=&callback=initializeJsonp_"
				+ getNumber(0);
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"cna=zINpEyavAUUCAQ6Rkd5nz/Fy; _ga=GA1.2.978792435.1526268887; consoleRecentVisit=oss%2Cram; login_aliyunid_pk=1983119870622318; login_aliyunid_pks=\"BG+raN6/2ekXd/yNGiJ3FeoM7ukdLdmlZHaZECtwOSTD/M=\"; aliyun_site=CN; CLOSE_HELP_GUIDE=true; aliyun_choice=CN; CLOSE_HELP_GUIDE_V2=true; cps=zwYSodrSlqydp2c00b0qdPkTSZAQXEL9FpTYA6z6%2FoMSy7P0Q%2FcJRLz%2FjSCKOhDa; aliyun_lang=zh; UM_distinctid=16b4a747cbf562-0feed63827f77e-3c604504-1fa400-16b4a747cc0748; login_aliyunid_sc=74u48x24xL7xCj1SQ9*cYL0T_GM6j755NjwLgFPERfcvy4Sm4N36mjzUyO2r8J*i56CirX_*9VBpfkTFdJTd51hE7x1b2Bt8IwxHfRayq5XrmJK*H1vT5ERPp2356A*R; isg=BHNzD-ihKgqZHed6ETgZ6gPkAnddAAcYfL1SGyURoRLwJJTGrXwiuIE13hRvhF9i; l=bBLyDJGev_E7Fck5BOfgmuI8aK7OCIOffsPzw4_GlICPOcjJk12FWZHxWepvC3GVa6dDR3RYGVWyB58-zPaTQ");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
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

		System.out.println(stringBuffer.toString());

	}

	private static long curl;

	private static void yanzhengAli2() throws HttpException, IOException {
		String url = "https://cf.aliyun.com/nocaptcha/analyze.jsonp?a=QNYX&t=QNYX%3A" + curl
				+ "%3A0.21306380947982229&n=118%23ZVWZzwLE%2BQCDSZSQre2CcZZTZeDh6HwzZHVfCnqhzZ1eZbHZ6Gq4ZHZTZeChVHZVmc7yYJOmawbVzeCZ%2BfqVZe2zZZZTXHC%2FzXucZheTzeRZzDCZVoqYZHY3TeFhVHhHZZZ%2Fwj53CuZxuHCZXoqVzHZzZeZTVHRVzZ2ZZzqTzeRZTHCZXoqVde%2FOZeVe0gAxfgwkz0GTihMbuAAazwHVpgv%2BYkgvZ2%2BD2CK9bBqh7dl%2FmelCtgVHz2Su0jBN6JC7ZZe3L4PXZZxqGggXUFoUZYiP%2BaHdapRop8tbJR5WbI0OsaguMbuSZ6IRgjAjP%2BohWkgLo%2FrlHZxaJF4Y%2FkAWqNR5H%2BQ25p1kKeC5GcRXQXxCvnw2%2BX5fC162TRxDIzSC%2BRY5jmXdEOM0SYvz%2FR25dHpcH65UMr2clQVrSU1EbKHGD%2FPXjGSABDlOZfQfuTDiauJmv0g0g9J41uhDEdelr%2FcG5j5stoReUAD88kzhqkmIcUqVr4GwdMI9gplhFvG%2F5bAlpUTXfy0anbIacC9xshnWV3InwtV7Fbld9P4KfDCA4WMTpIhTxYDrblymwHZ56xgPx2v70PdQluwCj5GZoUsrx0vll0mt0Z6FIeLtBUDFyuvwGk%2FNMjM6wwMfpIlJuaBFQwu%2FqIhheLdpWiW1l3xB20XCK4gQ9Oiv3UHncizqU5S8aJGmG3M7dZcq8xX8KxfhEfB8po19SeOutlSSXiJAzwsFHMWZ4wdhOrbAfO1yFA77dtxw1cNX8NXPHyG2cAO0swq%2Fve3GrjFsJFfgc47RZnI%2FcMvYnU%2BxrC50d8ebimCcLpDhlYIBgRPhScIfkIrHa8Wz2J4AOSfMCWcEzsvIFDv2uF32XJ4iRAJFaXj%2BqVZ12ZkKflOHX95X9IbJ4BHR5CFr3aRE88SdLBM%2BxF1w9Ft8KxsapyYiQ9ptktPFv7O1x%2FbT7Fn00iziPGsqz3HLB2iPasVak%2FvqvM5IJqEziDM3dOHYaCa8SshisICa2zdU7srrSTtN5UHPqCHz3N%2FkCyROlN9mAbU%2BN8znlNoc1wpfCsLCAP18rpYajIQ4q7kwPaQudV1kJfoj%2FI7b&p=%7B%22ncSessionID%22%3A%225f3768888bde%22%7D&scene=register&asyn=0&lang=cn&v=966&callback=jsonp_"
				+ getNumber(0);
 		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Cookie",
				"cna=zINpEyavAUUCAQ6Rkd5nz/Fy; _ga=GA1.2.978792435.1526268887; consoleRecentVisit=oss%2Cram; login_aliyunid_pk=1983119870622318; login_aliyunid_pks=\"BG+raN6/2ekXd/yNGiJ3FeoM7ukdLdmlZHaZECtwOSTD/M=\"; aliyun_site=CN; CLOSE_HELP_GUIDE=true; aliyun_choice=CN; CLOSE_HELP_GUIDE_V2=true; cps=zwYSodrSlqydp2c00b0qdPkTSZAQXEL9FpTYA6z6%2FoMSy7P0Q%2FcJRLz%2FjSCKOhDa; aliyun_lang=zh; UM_distinctid=16b4a747cbf562-0feed63827f77e-3c604504-1fa400-16b4a747cc0748; login_aliyunid_sc=74u48x24xL7xCj1SQ9*cYL0T_GM6j755NjwLgFPERfcvy4Sm4N36mjzUyO2r8J*i56CirX_*9VBpfkTFdJTd51hE7x1b2Bt8IwxHfRayq5XrmJK*H1vT5ERPp2356A*R; isg=BHNzD-ihKgqZHed6ETgZ6gPkAnddAAcYfL1SGyURoRLwJJTGrXwiuIE13hRvhF9i; l=bBLyDJGev_E7Fck5BOfgmuI8aK7OCIOffsPzw4_GlICPOcjJk12FWZHxWepvC3GVa6dDR3RYGVWyB58-zPaTQ");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
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

		str = stringBuffer.toString();
		if (str.contains("(")) {
			String data = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
			System.out.println(data);
			Gson gson = new Gson();
			AliResultBean bean = gson.fromJson(data, AliResultBean.class);

			if (bean.isSuccess()) {
				csessionid = bean.getResult().getCsessionid();
				value = bean.getResult().getValue();
			}
		}

		// System.out.println(stringBuffer);

	}

	private static String csessionid;
	private static String value;

	private static long yanTime;

	private static void yanzhegn() throws HttpException, IOException {
		String url = "https://www.qichacha.com/index_verifyAction";
		yanTime = System.currentTimeMillis();
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		PostMethod getMethod = new PostMethod(url);
		getMethod.addRequestHeader("Cookie",
				" UM_distinctid=16b72c0ff50555-0d50f17e5c8578-3c604504-1fa400-16b72c0ff51660; zg_did=%7B%22did%22%3A%20%2216b72c0ff5f572-011d0e55575c9-3c604504-1fa400-16b72c0ff603ad%22%7D; _uab_collina=156099837952298970109224; acw_tc=7793462615609983826941398e8cdd0239ea250a3904a4f429fad0e6cf; QCCSESSID=mc2p8ji3nsptae2g1uab251ki2; zg_63e87cf22c3e4816a30bfbae9ded4af2=%7B%22sid%22%3A%201561098941206%2C%22updated%22%3A%201561099006894%2C%22info%22%3A%201561098941207%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%7D; hasShow=1; CNZZDATA1254842228=1382632714-1560996794-%7C1561624290; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1561366594,1561426571,1561609861,1561626633; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561625393920%2C%22updated%22%3A%201561626635166%2C%22info%22%3A%201561609857021%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22cuid%22%3A%20%22d3da367ce98638adec75c748c78c6c89%22%7D; Hm_lpvt_3456bee468c83cc63fb5147f119f1075="
						+ yanTime);
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		NameValuePair[] data = { new NameValuePair("csessionid", csessionid), new NameValuePair("scene", "register"),
				new NameValuePair("sig", value), new NameValuePair("token", "QNYX:" + curl + ":0.21306380947982229"),
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
		System.out.println("生成表格 去除重复前----size " + Clist.size());
		Set<Company> ts = new HashSet<Company>();
		ts.addAll(Clist);
		System.out.println("生成表格 去除重复后----size " + ts.size());

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

		int i = 0;
		for (Company commodity : ts) {
			row = sheet.createRow((int) i + 1);

			row.createCell(0).setCellValue(commodity.getCompanyName());// 商品名称
			row.createCell(1).setCellValue(commodity.getStatustd());// 商品类型
			row.createCell(2).setCellValue((commodity.getName()).toString());// 销售价格 setSellPrice
			row.createCell(3).setCellValue((commodity.getPrice()));// 所获积分
			row.createCell(4).setCellValue(commodity.getTime());// 库存数量
			row.createCell(5).setCellValue(commodity.getEmail());// 生产许可证
			row.createCell(6).setCellValue(commodity.getCall());// 厂名
			row.createCell(7).setCellValue(commodity.getAddress());// 厂址
			row.createCell(8).setCellValue(commodity.getWebsite());// 厂家联系电话
			i++;

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
	@Override
	public boolean equals(Object obj) {
		Company s = (Company) obj;
		return companyName.trim().equals(s.companyName.trim()) && name.trim().equals(s.name.trim());
	}

	@Override
	public int hashCode() {
		String in = companyName.trim() + name.trim();
		return in.hashCode();
	}
}
