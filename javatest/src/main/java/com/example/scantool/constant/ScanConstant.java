package com.example.scantool.constant;

/**
 * Created by shisong on 2019/6/6
 */
public class ScanConstant {

    public final static boolean REAL_DEL = false; // 是否真的删除文件

    // 存在拼接查找资源的逻辑
    public final static String[] HOLD = new String[]{
            "cate_", "music_top_fans_", "player_rank_starrank_", "plugin_", "no_vip_rank", "vip_rank_", "vip_card_level_",
            "qiyi_search_hotwor_rank_icon_", "service_", "player_pp_recommend_item_circle_feed", "player_portrait_rank_starrank_",
            "vip_level_", "vip_upgrade_gift_dialog_logo_", "vip_upgrade_gift_dialog_title_", "gift_num_", "ico_hot"
    };

    public final static String ROOT_DIR = "F:/qiyi_git/qiyivideo/";

    public final static String[] EXCLUDE_DIR = {
            ".git",
            ".gitignore",
            ".gradle",
            ".idea",
            ".navigation",
            "BaiduWalletSDKLib",
            "qywallet",
            "build",
            "gradle",
            "Networklib",
            "tools",
            "WebView",
            "android_support",
            "appstore"
    };

}
