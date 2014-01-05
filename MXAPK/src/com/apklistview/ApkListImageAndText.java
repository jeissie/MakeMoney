package com.apklistview;
/**
 * @author jie
 *
 */
public class ApkListImageAndText {

	 private String imageUrl;    //Icon��ַ
	 private String apk_zh_name; //������
	 private String apk_en_name; //Ӣ����
	 private String time;        //����ʱ��
	 private String apk_size;    //�����С
	 private String downloads;   //���ش���
	 private String url;         //���ص�ַ
	 private String jumpUrl;     //��ϸҳ���ַ
	 private int star;
	 
	 public ApkListImageAndText(String imageUrl, String apk_zh_name, 
			 String apk_en_name,String time, String apk_size,String downloads,int star ,String url, String jumpUrl) {
		 this.imageUrl = imageUrl;
		 this.apk_zh_name = apk_zh_name;
		 this.apk_en_name = apk_en_name;
		 this.time = time;
		 this.apk_size = apk_size;
		 this.downloads = downloads;
		 this.star = star;
		 this.url = url;
		 this.jumpUrl = jumpUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getApk_zh_name() {
		return apk_zh_name;
	}
	
	public String getApk_en_name() {
		return apk_en_name;
	}

	public String getTime() {
		return time;
	}

	public String getApk_size() {
		return apk_size;
	}
	
	public String getDownloads() {
		return downloads;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getJumpUrl() {
		
		return jumpUrl;
	}

	public int getStar(){
		return star;
	}

}


