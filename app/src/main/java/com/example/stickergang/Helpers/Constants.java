package com.example.stickergang.Helpers;

import com.example.stickergang.BuildConfig;

public class Constants {

    public static class ImageConstants{

        public static final String INTERMEDIATE_FILE = "intermediate.png";
        public static final float IMAGE_PADDING = 1.25f;


    }


    public static class WAP {
        public static final String AUTHORITY_QUERY_PARAM = "authority";
        public static final String IDENTIFIER_QUERY_PARAM = "identifier";
        public static final String STICKER_APP_AUTHORITY = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        public static final String CONSUMER_WHATSAPP_PACKAGE_NAME = "com.whatsapp";
        public static final String SMB_WHATSAPP_PACKAGE_NAME = "com.whatsapp.w4b";
        public static final String CONTENT_PROVIDER = ".provider.sticker_whitelist_check";
        public static final String QUERY_PATH = "is_whitelisted";
        public static final String QUERY_RESULT_COLUMN_NAME = "result";


        public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
        public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
        public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
        public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
        public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
        public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
        public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
        public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
        public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
        public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
        public static final String IMAGE_DATA_VERSION = "image_data_version";
        public static final String AVOID_CACHE = "whatsapp_will_not_cache_stickers";

        public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
        public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";

    }

    public static class SqlHelper{
        public static final String MASTER_DETAILS = "master_detail";
        public static final String STICKER_PACK_IDENTIFIER = "sticker_pack_identifier";
        public static final String STICKER_PACK_NAME = "sticker_pack_name";
        public static final String STICKER_PACK_ENABLED = "sticker_pack_enabled";
        public static final String STICKER_PACK_TRAY = "sticker_pack_tray_image";


    }


}
