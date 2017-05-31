
##### 须知2333
- 在移动平台使用Jenkins持续集成，极大的方便了开发人员和测试人员之间的工作配合。是极大的，是极大的，是极大的。
- 蒲公英作为一个优秀的集应用分发和测试的平台，方便了在开发阶段和测试阶段应用的分发繁琐问题，并且保证了测试包的统一性和安全性。
- But，目前蒲公英官方没有提供Jenkins相关插件！！！蒲公英官方提供的开发API中也只是简单的用crul做了一个示例！如果官方提供的方法，那么提取二维码，获取应用短连接等其他功能的实现就相对来说比较麻烦。
![示例](http://oqsydf96n.bkt.clouddn.com/pgyer_upload.png)
- So，如果定制化较强，那么上传后蒲公英返回的结果是很重要的。我们可以根据返回的信息获取二维码链接，应用短连接，版本信息等如下信息：
![](http://oqsydf96n.bkt.clouddn.com/upload_pgyer_info.png)
- 例如，我们可以在Jenkins中使用[`Description Setter Plugin`](https://wiki.jenkins-ci.org/display/JENKINS/Description+Setter+Plugin)插件将应用二维码显示到构建历史栏目中，这样测试人员用手机扫码下载即可测试最新的开发任务。如下图：![构建历史二维码](http://oqsydf96n.bkt.clouddn.com/pgyer_build_qrcode.png)
- So，既然官方不提供上传插件，那我们就自己动手吧！动手吧！动手吧！

##### 定制需求
- 控制台可以输出帮助信息
- 实现APP上传到蒲公英平台，这是最基本的
- 将二维码下载到本地
- 上传文件路径要支持路径通配
- 将蒲公英返回的信息保存到本地，方便以后扩展和查看上传详情
- 友好的日志提示
- 全面的异常处理

##### 实现需求
> 选择开源框架Jsoup，Gson。Jsoup虽然最擅长的是解析Html但是内部封装了网络传输，可以方便的提交Post参数和文本；Gson可以快速解析蒲公英返回的信息！

- 帮助信息实现

	```Java
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
	```
- 上传文件到蒲公英实现代码片段

	```Java
	Document doc = Jsoup.connect(UPLOAD_URL)
						.ignoreContentType(true)
						.data("uKey", uKey)
						.data("_api_key", _api_key)
						.data("file", uploadFile.getName(), is)
						.data("installType", installType)
						.data("password", password)
						.timeout(1000 * 60 * 10)
						.post();
	```

- 下载二维码实现方法

	```Java
	public static File download(String urlString, String savePath, String fileName) {
			try {
				File sf = new File(savePath);
				if (!sf.exists()) sf.mkdirs();
				String filePath = savePath + File.separator + fileName;
				
				URL url = new URL(urlString);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(60 * 1000);
				InputStream is = con.getInputStream();
				
				byte[] bs = new byte[1024 * 8];
				int len;
				
				OutputStream os = new FileOutputStream(filePath);
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				os.close();
				is.close();
				return new File(filePath);
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("图片下载失败：" + e.getMessage() + "\n");
				return null;
			}
		}
	```
- 文件上传文件路径通配找出具体的上传文件实现

	```Java
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
	```
- 以上是一些重要步骤的实现，其他实现请查阅[代码](https://github.com/myroid/upload-pgyer)

##### 后续优化事项
- 上传文件进度
- 下载二维码进度

