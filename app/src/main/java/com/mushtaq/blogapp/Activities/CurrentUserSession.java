package com.mushtaq.blogapp.Activities;

public class CurrentUserSession {


    private static String UID;
    private static String UNAME;
    private static String UEMAIL;
    private static String UPHOTO;



    public static void setUID(String UID) {
        CurrentUserSession.UID = UID;
    }


    public static void setUNAME(String UNAME) {
        CurrentUserSession.UNAME = UNAME;
    }

    public static void setUEMAIL(String UEMAIL) {
        CurrentUserSession.UEMAIL = UEMAIL;
    }

    public static String getUID() {
        return UID;
    }


    public static String getUEMAIL() {
        return UEMAIL;
    }

    public static String getUNAME() {
        return UNAME;
    }

    public static String getUPHOTO() {

        if (UPHOTO != null)
        return ServerApp.IP + "blog/image/" + UPHOTO;
        else
            return null;
    }

    public static void setUPHOTO(String UPHOTO) {

        CurrentUserSession.UPHOTO = UPHOTO;
    }
}
