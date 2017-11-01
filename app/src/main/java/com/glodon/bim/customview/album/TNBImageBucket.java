package com.glodon.bim.customview.album;

import java.io.Serializable;
import java.util.List;

/**
 * Description:  一个目录的相册对象
 * Created by 周瑞峰 ON ON 2015/5/6
 * Job number:136597
 * Phone:15001340978
 * Email:zhouruifeng@syswin.com
 * Person in charge:周瑞峰
 * Leader:周瑞峰
 */
public class TNBImageBucket implements Serializable {
	/**
	 * @Description serialVersionUID
	 */
	private final long serialVersionUID = -8198419707482282775L;
	/**
	 * 该文件夹内图片总数
	 */
	public int count = 0;
	/**
	 * 文件夹名
	 */
	public String bucketName;
	/**
	 * 该文件夹下所有的图片
	 */
	public List<TNBImageItem> imageList;
	


	
}
