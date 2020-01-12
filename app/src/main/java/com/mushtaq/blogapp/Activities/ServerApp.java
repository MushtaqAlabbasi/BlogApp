package com.mushtaq.blogapp.Activities;

public class ServerApp {

//    192.168.1.104 my network
    static String IP = "http://10.0.0.169/";


    static String ADD_USER_URL = IP + "blog/add_user.php";
    static String GET_INFO_URL = IP + "blog/get_user_info.php";

    static String GET_POST_URL = IP + "blog/get_all_posts.php";
    static String ADD_POST_URL = IP + "blog/add_post.php";
    static String DELET_POST_URL = IP + "blog/delete_post.php";


    static String GET_PROG_CAT = IP + "blog/get_prog_post.php";
    static String GET_DES_CAT = IP + "blog/get_design_post.php";
    static String GET_ENG_CAT = IP + "blog/get_eng_post.php";


    static String ADD_COMMENT_URL = IP + "blog/add_comment.php";
    static String GET_COMMENTS_URL = IP + "blog/get_all_comments.php";


    static String UPLOAD_IMG_URL = IP + "blog/upload_img.php";


}
