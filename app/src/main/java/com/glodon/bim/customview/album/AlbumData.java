package com.glodon.bim.customview.album;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 描述：选中的图片
 * 作者：zhourf on 2017/11/1
 * 邮箱：zhourf@glodon.com
 */

public class AlbumData implements Serializable{
    public LinkedHashMap<String,TNBImageItem> map;

    public AlbumData(LinkedHashMap<String, TNBImageItem> map) {
        this.map = map;
    }
}
