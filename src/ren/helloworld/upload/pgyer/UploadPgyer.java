package ren.helloworld.upload.pgyer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by dafan on 2017/5/4 0004.
 */
public class UploadPgyer {
	public static final String UPLOAD_URL = "https://qiniu-storage.pgyer.com/apiv1/app/upload";

	public static void main(String[] args) {
		printHeaderInfo();
		System.out.println(Arrays.toString(args));

		// 用户帮助说明
		if (args != null && args.length == 1 && args[0].equals("help")) {
			printHelpInfo();
			return;
		}

		// 必须参数检测
		if (args == null || args.length < 4) {
			printHelpInfo();
			return;
		}

		// 提取相关参数
		String uKey = args[0];
		String _api_key = args[1];
		String file = findFile(args[2]);
		String qrcode = args[3];
		String installType = "1";
		String password = "";

		// 检验文件
		if (file == null) {
			System.out.println("文件不存在");
			return;
		}
		File uploadFile = new File(file);
		if (!uploadFile.exists() || !uploadFile.isFile()) {
			System.out.println("文件不存在");
			return;
		}

		// 二维码存储路径校验
		if (StringUtil.isBlank(qrcode)) {
			return;
		}
		File qrcodeFile = new File(qrcode);
		String qrcodeDirPath = qrcodeFile.getParentFile().getAbsolutePath();
		String qrcodeFileName = qrcodeFile.getName();

		// 获取安装方式
		if (args.length >= 5) {
			installType = args[4];
		}

		// 如果密码安装，需要提取密码
		if (installType.equals("2")) {
			if (args.length == 6) {
				password = args[5];
			} else {
				installType = "1";
			}
		}

		System.out.println("\n参数信息：");
		/*System.out.println("uKey：             " + uKey);
		System.out.println("_api_key：         " + _api_key);*/
		System.out.println("file：             " + file);
		System.out.println("qrcode:            " + qrcode);
		System.out.println("qrcodeDirPath:     " + qrcodeDirPath);
		System.out.println("qrcodeFileName:    " + qrcodeFileName);
		System.out.println("installType：      " + installType);
		System.out.println("password：         " + password + "\n");

		try {
			System.out.println("正在上传文件：" + uploadFile.getName() + " 到 " + UPLOAD_URL);
			InputStream is = new FileInputStream(uploadFile);
			Document doc = Jsoup.connect(UPLOAD_URL)
					.ignoreContentType(true)
					.data("uKey", uKey)
					.data("_api_key", _api_key)
					.data("file", uploadFile.getName(), is)
					.data("installType", installType)
					.data("password", password)
					.timeout(1000 * 60 * 10)
					.post();

			is.close();
			String result = doc.body().text();
			System.out.println("文件上传完成");
			Upload upload = new Gson().fromJson(result, new TypeToken<Upload>() {
			}.getType());

			if (upload.getCode() != 0) {
				System.out.println("上传失败\n");
				System.out.println("错误码：" + upload.getCode() + "\n");
				System.out.println("错误日志：" + upload.getMessage() + "\n");
				return;
			}

			System.out.println("正在下载二维码图片 " + upload.getData().getAppQRCodeURL());
			download(upload.getData().getAppQRCodeURL(), qrcodeDirPath, qrcodeFileName);
			System.out.println("图片下载完成");
			System.out.println("正在将图片链接写入到文件中");
			String qrtxt = new File(qrcodeDirPath, qrcodeFileName.replace(".png", ".txt")).getAbsolutePath();
			System.out.println("文件路径：" + qrtxt);
			write(qrtxt, getUploadInfo(upload), "utf-8");

			printResultInfo(upload);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件名找出文件名
	 *
	 * @param file
	 * @return
	 */
	private static String findFile(String file) {
		if (StringUtil.isBlank(file)) return null;
		if (!file.contains("*")) return file;

		String dirPath = file.substring(0, file.lastIndexOf("/"));
		String[] keys = file.substring(file.lastIndexOf("/") + 1).split("\\*");
		String[] files = new File(dirPath).list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean ok = true;
				for (String key : keys) {
					ok &= key.equals("") || name.contains(key);
				}
				return ok;
			}
		});
		return files == null || files.length == 0 ? null : files[0];
	}

	/**
	 * 下载文件
	 *
	 * @param urlString 地址
	 * @param savePath  存储路径
	 * @param fileName  文件名称
	 */
	public static File download(String urlString, String savePath, String fileName) {
		try {
			File sf = new File(savePath);// 输出的文件流
			if (!sf.exists()) sf.mkdirs();
			String filePath = savePath + File.separator + fileName;

			URL url = new URL(urlString);// 构造URL
			URLConnection con = url.openConnection();// 打开连接
			con.setConnectTimeout(60 * 1000);//设置请求超时为5s
			InputStream is = con.getInputStream();// 输入流

			byte[] bs = new byte[1024 * 8];// 8K的数据缓冲
			int len;// 读取到的数据长度

			OutputStream os = new FileOutputStream(filePath);
			while ((len = is.read(bs)) != -1) {// 开始读取
				os.write(bs, 0, len);
			}

			// 完毕，关闭所有链接
			os.close();
			is.close();
			return new File(filePath);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("图片下载失败：" + e.getMessage() + "\n");
			return null;
		}
	}

	/**
	 * 写文件
	 *
	 * @param path     文件路径，重写入
	 * @param content  文件内容
	 * @param encoding 文件编码
	 */
	private static void write(String path, String content, String encoding) {
		try {
			File file = new File(path);
			file.delete();
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), encoding));
			writer.write(content);
			writer.close();
			System.out.println("写入成功");
		} catch (Exception e) {
			System.err.println("文件写入失败");
		}
	}

	/**
	 * 输入帮助信息
	 */
	private static void printHelpInfo() {
		System.out.println("参数说明请参考：https://www.pgyer.com/doc/api#uploadApp");
		System.out.println("java -jar <uKey> <_api_key> <file> <qrcode> [installType] [password]");
		System.out.println("     uKey：         (必填) 用户Key");
		System.out.println("     _api_key：     (必填) API Key");
		System.out.println("     file：         (必填) 需要上传的ipa或者apk文件");
		System.out.println("     qrcode：       (必填) 上传到蒲公英后二维码图片存储的路径，绝对路径");
		System.out.println("     installType：  (选填) 应用安装方式，值为(1,2,3)。1：公开，2：密码安装，3：邀请安装。默认为1公开");
		System.out.println("     password：     (选填) 设置App安装密码，如果不想设置密码，请传空字符串，或不传\n");
	}

	/**
	 * Header
	 */
	private static void printHeaderInfo() {
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println("****************************************************************");
		System.out.println("*****************        蒲公英上传服务        *****************");
		System.out.println("****************************************************************");
		System.out.println("****************************************************************");
	}

	/**
	 * @param upload
	 */
	private static void printResultInfo(Upload upload) {
		System.out.println();
		Upload.DataBean data = upload.getData();
		System.out.println("应用类型：" + data.getAppType());
		System.out.println("是否是最新版：" + data.getAppIsLastest());
		System.out.println("App 文件大小：" + data.getAppFileSize());
		System.out.println("应用名称：" + data.getAppName());
		System.out.println("版本号：" + data.getAppVersion());
		System.out.println("Android的版本编号：" + data.getAppVersionNo());
		System.out.println("build号：" + data.getAppBuildVersion());
		System.out.println("应用程序包名：" + data.getAppIdentifier());
		System.out.println("应用的Icon图标key：" + data.getAppIcon());
		System.out.println("应用介绍：" + data.getAppDescription());
		System.out.println("应用更新说明：" + data.getAppUpdateDescription());
		System.out.println("应用截图的key：" + data.getAppScreenshots());
		System.out.println("应用短链接：" + data.getAppShortcutUrl());
		System.out.println("应用二维码地址：" + data.getAppQRCodeURL());
		System.out.println("应用上传时间：" + data.getAppCreated());
		System.out.println("应用更新时间：" + data.getAppUpdated());
		System.out.println();
	}

	/**
	 * @param upload
	 * @return
	 */
	private static String getUploadInfo(Upload upload) {
		StringBuffer sb = new StringBuffer();
		sb.append("appType").append("=").append(upload.getData().getAppType()).append("\n");
		sb.append("appIsLastest").append("=").append(upload.getData().getAppIsLastest()).append("\n");
		sb.append("appFileSize").append("=").append(upload.getData().getAppFileSize()).append("\n");
		sb.append("appName").append("=").append(upload.getData().getAppName()).append("\n");
		sb.append("appVersion").append("=").append(upload.getData().getAppVersion()).append("\n");
		sb.append("appVersionNo").append("=").append(upload.getData().getAppVersionNo()).append("\n");
		sb.append("appBuildVersion").append("=").append(upload.getData().getAppBuildVersion()).append("\n");
		sb.append("appIdentifier").append("=").append(upload.getData().getAppIdentifier()).append("\n");
		sb.append("appIcon").append("=").append(upload.getData().getAppIcon()).append("\n");
		sb.append("appDescription").append("=").append(upload.getData().getAppDescription()).append("\n");
		sb.append("appUpdateDescription").append("=").append(upload.getData().getAppUpdateDescription()).append("\n");
		sb.append("appScreenshots").append("=").append(upload.getData().getAppScreenshots()).append("\n");
		sb.append("appShortcutUrl").append("=").append(upload.getData().getAppShortcutUrl()).append("\n");
		sb.append("appCreated").append("=").append(upload.getData().getAppCreated()).append("\n");
		sb.append("appUpdated").append("=").append(upload.getData().getAppUpdated()).append("\n");
		sb.append("appQRCodeURL").append("=").append(upload.getData().getAppQRCodeURL()).append("\n");
		sb.append("appPgyerURL").append("=").append("https://www.pgyer.com/" + upload.getData().getAppShortcutUrl()).append("\n");
		return sb.toString();
	}
}
