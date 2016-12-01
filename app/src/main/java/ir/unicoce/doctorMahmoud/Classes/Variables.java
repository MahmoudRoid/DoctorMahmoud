package ir.unicoce.doctorMahmoud.Classes;

public class Variables {

    public static final String Tag                  = "MYAPP";

    public static final String TOKEN                = "Z4_ru@9j";

    public static final String catId                = "catId";
    public static final String Id                   = "Id";
    public static final String catIndex             = "catIndex";
    public static final String Index                = "Index";

    public static final String getNews              = "2";
    public static final String getDanestaniha       = "3";
    public static final String getServices          = "4";
    public static final String getAboutUs           = "5";
    public static final String getSlides            = "6";
    public static final String getImages            = "7";
    public static final String getVideos            = "8";
    public static final String getVoices            = "9";
    public static final String getFiles             = "10";
    public static final String getMagazine          = "";
    public static final String getInsurance         = "";
    public static final String getParts             = "";
    public static final String getCareBefore        = "";
    public static final String getCareAfter         = "";
    public static final String getDrugs             = "";
    public static final String getCommonQuestions   = "18";


    public static final String WebSite_Link         = "";
    public static final String Telegram_Channel_Id  = "";
    public static final String Instagram_Id         = "";
    public static final String Instagram_Id_From_App= "";
    public static final String Telegram_chat_Id     = "";
    public static final String Gmail                = "uniced@gmail.com";
    public static final String GooglePlus           = "https://plus.google.com/communities/107847486351510098159";

}// end class

/**
 * Both below two methods do the same thing :

 1 :
 News : (Objects)
 URL +/+ GetItem
 {catId:NEWS_ID,Token=TOKEN}

 News : (Folder)
 URL +/+ GetCategory
 {Id:NEWS_ID,Token=TOKEN}

 _______________________________

 2 :
 News : (Objects)
 URL +/+ GetItem
 {catIndex:NEWS_INDEX,Token=TOKEN}

 News : (Folder)
 URL +/+ GetCategory
 {Index:NEWS_INDEX,Token=TOKEN}


 ************************************
 * this method get all objects of folders and sub folder in the root folder with id or index mentioned

 News : (Objects and Folders)
 URL +/+ GetFullItems
 {Id:NEWS_ID / NEWS_INDEX,Token=TOKEN}


 ************************************
 * this method get all categories inside folder and sub folders of the root folder with id or index mentioned
 News : (Categories inside folder)
 URL +/+ GetFullCategories
 {Id:NEWS_ID / NEWS_INDEX,Token=TOKEN}


 **/
