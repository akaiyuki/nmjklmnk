package com.fasionparade.fasionparadeApp.Functions.Service;

/**
 * Created by ksuresh on 4/4/2016.
 */
public class ResourceManager {


    public static final String SERVER_NAME = "ec2-52-40-5-6.us-west-2.compute.amazonaws.com/mfp/";

  //  http://staunchire.com/clients/mfp/webservice/
    //http://ec2-52-40-5-6.us-west-2.compute.amazonaws.com/mfp/


    public static String getHost() {

        return "http://" + SERVER_NAME;
    }
    public static String getHostHttps() {
        return "https://" + SERVER_NAME;
    }



    public static String login() {
        String tmp = getHost();
        tmp += "login.php?";
        return tmp;
    }
    public static String signUp() {
        String tmp = getHost();
        tmp += "registration.php?";
        return tmp;
    }
    public static String getPublicParade() {
        String tmp = getHost();
        tmp += "publicparadedetails.php?";
        return tmp;
    }
    public static String getMyProfile() {
        String tmp = getHost();
        tmp += "myprofile.php?";
        return tmp;
    }
    public static String getfollow() {
        String tmp = getHost();
        tmp += "follow.php?";
        return tmp;
    }
    public static String voteParade() {
        String tmp = getHost();
        tmp += "voteparade.php?";
        return tmp;
    }
    public static String newParade() {
        String tmp = getHost();
        tmp += "newparade.php?";
        return tmp;
    }
    public static String editParade() {
        String tmp = getHost();
        tmp += "editparade.php?";
        return tmp;
    }
    public static String imageUpload() {
        String tmp = getHost();
        tmp += "paradeimages.php?";
        return tmp;
    }
    public static String myActiveParade() {
        String tmp = getHost();
        tmp += "myactiveparades.php?";
        return tmp;
    }
    public static String myWinnerParade() {
        String tmp = getHost();
        tmp += "mywinnerlist.php?";
        return tmp;
    }
    public static String myInboxParade() {
        String tmp = getHost();
        tmp += "inbox.php?";
        return tmp;
    }
    public static String myFollowerList() {
        String tmp = getHost();
        tmp += "followers.php?";
        return tmp;
    }
    public static String myUpdateProfile() {
        String tmp = getHost();
        tmp += "updateprofile.php?";
        return tmp;
    }

    public static String addfriends() {
        String tmp = getHost();
        tmp += "addfriends.php?";
        return tmp;
    }

    public static String myfriends() {
        String tmp = getHost();
        tmp += "myfriends.php?";
        return tmp;
    }


    public static String getGroup() {
        String tmp = getHost();
        tmp += "groupdetails.php?";
        return tmp;
    }

    public static String createGroup() {
        String tmp = getHost();
        tmp += "creategroup.php?";
        return tmp;
    }

    public static String updateGroupName() {
        String tmp = getHost();
        tmp += "updategroupname.php?";
        return tmp;
    }

    public static String deleteGroup() {
        String tmp = getHost();
        tmp += "deletegroup.php?";
        return tmp;
    }

    public static String addgroupmembers() {
        String tmp = getHost();
        tmp += "addgroupmembers.php?";
        return tmp;
    }
    //addgroupmembers.php  userIds ,groupId   (User id seperated by comma)

    public static String deletecontact() {
        String tmp = getHost();
        tmp += "deletecontact.php?";
        return tmp;
    }

    public static String groupcompletedata(){
        String tmp = getHost();
        tmp += "groupcompletedata.php?";
        return tmp;
    }
    //    follow.php userId,followingId
    public static String Followmtpuser() {
        String tmp = getHost();
        tmp += "follow.php?";
        return tmp;
    }

    //unfollow.php userId,followingId
    public static String Unfollowmtpuser() {
        String tmp = getHost();
        tmp += "unfollow.php?";
        return tmp;
    }

    public static String ReportAbuse() {
        String tmp = getHost();
        tmp += "reportabuse.php?";
        return tmp;
    }

    public static String favouriteParade() {
        String tmp = getHost();
        tmp += "favourite.php?";
        return tmp;
    }

    public static String stopParade() {
        String tmp = getHost();
        tmp += "stopparade.php?";
        return tmp;
    }
    //   myfavourites.php userId
    public static String myfavourites()
    {
        String tmp =getHost();
        tmp += "myfavourites.php?";
        return  tmp;
    }


    //    latestparades.php  userId,tag
    public static String latestparades()
    {
        String tmp =getHost();
        tmp += "latestparades.php?";
        return  tmp;
    }

    //passing parameter is userid,tag
    public static String latestwinners()
    {
        String tmp =getHost();
        tmp += "latestwinners.php?";
        return  tmp;
    }


    //searchfriend passing parameter is userid,searchtext
    public static String searchfriend()
    {
        String tmp=getHost();
        tmp += "searchfriends.php?";
        return tmp;
    }


//    //    follow.php userId,followingId
//    public static  String Followmtpuser()
//    {
//        String tmp=getHost();
//        tmp += "follow.php?";
//        return tmp;
//    }
//
//
//    //    unfollow.php userId,followingId
//    public static  String Unfollowmtpuser()
//    {
//        String tmp=getHost();
//        tmp += "unfollow.php?";
//        return tmp;
//    }


    public static  String Privacyterms()
    {
        String tmp=getHost();
        tmp += "privacyterms.php?";
        return tmp;
    }

    //    deletefavourite.php userId,imageId
    public static  String DeleteFav()
    {
        String tmp=getHost();
        tmp += "deletefavourite.php?";
        return tmp;
    }

    public static  String Addcontact()
    {
        String tmp=getHost();
        tmp += "addcontact.php?";
        return tmp;
    }


    public  static  String Changepassword()
    {

        String tmp =getHost();
        tmp +="changepassword.php?";
        return tmp;



    }


    public  static  String Reportproblem()
    {

        String tmp =getHost();
        tmp +="reportproblem.php?";
        return tmp;

    }


    public static  String AllowChart()
    {
        String tmp =getHost();
        tmp +="allowchat.php?";
        return tmp;
    }


    public static  String Logout()
    {
        String tmp =getHost();
        tmp +="logout.php?";
        return tmp;
    }


    public static  String Notification_setting()
    {
        String tmp =getHost();
        tmp +="notificationsettings.php?";
        return tmp;
    }



    public static  String Privateaccount()
    {
        String tmp =getHost();
        tmp +="privateaccount.php?";
        return tmp;
    }


    //friendfollower.php userid,friendid
    public static  String Friendfollower()
    {
        String tmp =getHost();
        tmp +="friendfollowers.php?";
        return tmp;
    }

    public static  String DeleteUser()
    {
        String tmp =getHost();
        tmp +="deleteuser.php?";
        return tmp;
    }

    public static  String acceptContact()
    {
        String tmp =getHost();
        tmp +="acceptcontact.php?";
        return tmp;
    }

    public static  String deleteInbox()
    {
        String tmp =getHost();
        tmp +="deleteinbox.php?";
        return tmp;
    }

    public static String deleteParade() {

        String tmp = getHost();
        tmp += "deleteparade.php?";
        return tmp;

    }

    public static String sendChat() {

        String tmp = getHost();
        tmp += "chat.php?";
        return tmp;

    }
    public static String usertypeupdate() {

        String tmp = getHost();
        tmp += "usertypeupdate.php?";
        return tmp;

    }

    public static String blockUser() {

        String tmp = getHost();
        tmp += "blockuser.php?";
        return tmp;

    }

    public static String blockChat() {

        String tmp = getHost();
        tmp += "blockchat.php?";
        return tmp;

    }

}
