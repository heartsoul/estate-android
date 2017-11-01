package com.glodon.bim.customview.album;

import java.io.Serializable;

/**
 * Description:  一个图片对象
 * Created by 周瑞峰 ON ON 2015/5/6
 * Job number:136597
 * Phone:15001340978
 * Email:zhouruifeng@syswin.com
 * Person in charge:周瑞峰
 * Leader:周瑞峰
 */
public class TNBImageItem implements Serializable {
	/**
	 * 图片id
	 */
	public String imageId;
	/**
	 * 图片缩略图路径
	 */
	public String thumbnailPath;
	/**
	 * 图片的原图片路径
	 */
	public String imagePath;
}
