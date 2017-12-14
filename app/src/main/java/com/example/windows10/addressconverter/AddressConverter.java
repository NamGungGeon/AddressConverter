package com.example.windows10.addressconverter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Windows10 on 2017-09-14.
 */

public class AddressConverter implements Runnable{

    //This class is designed as Singleton pattern
    private AddressConverter(){}
    private static AddressConverter instance=null;
    public static AddressConverter getInstance(){
        if(instance==null){
            instance= new AddressConverter();
        }
        return instance;
    }

    private final String certificateKey="U01TX0FVVEgyMDE3MDkxNDIyMjE1NzEwNzM1Mzc=";
    private String apiUrl="";

    private String entireAddress="";
    private String siNm="";
    private String sggNm="";
    private String emdNm="";
    private String liNm="";

    private AddressSaver result=null;
    private Context context= null;
    private LayoutInflater inflater= null;
    private FragmentManager fragmentManager= null;
    public AddressSaver convertToJibeonjuso(String input, Context context, LayoutInflater inflater, FragmentManager fragmentManage) throws Exception{
        //initiating...
        init();
        this.context= context;
        this.inflater= inflater;
        this.fragmentManager= fragmentManage;

        // 요청변수 설정
        String currentPage = "1";
        String countPerPage = "30";
        String confmKey = certificateKey;
        String keyword = input;

        //Ex
        //http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage=1&countPerPage=5&keyword=%EC%B6%A9%EC%B2%AD%EB%82%A8%EB%8F%84%20%EB%8B%B9%EC%A7%84%EC%8B%9C%20%EC%84%9D%EB%AC%B8%EB%A9%B4%20%EB%82%9C%EC%A7%80%EB%8F%84%EB%A6%AC&confmKey=U01TX0FVVEgyMDE3MDkxNDIyMjE1NzEwNzM1Mzc=
        apiUrl = "http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage="+currentPage+"&countPerPage="+countPerPage+"&keyword="+ URLEncoder.encode(keyword,"UTF-8")+"&confmKey="+confmKey;


        startConnection();

        return result;
    }

    // The parameter "address" is must "지번주소"
    // Because this method is converting fiducially as "지번주소"
    public String convertToCode(AddressSaver address){
        String result= "해당 지역의 코드가 없습니다.";

        if(address!=null){
            // "시" Check
            if(address.ssNm.contains("서울")){
                // "구" Check
                if(address.sggNm.equals("강남구")){
                    // "동" Check
                    if(address.emdNm.equals("개포동")){
                        result="1J11-3";
                    }else if(address.emdNm.equals("논현동")){
                        result="1G39-1";
                    }else if(address.emdNm.equals("대치동")){
                        result="3S74-0";
                    }else if(address.emdNm.equals("도곡동")){
                        result="1J11-3";
                    }else if(address.emdNm.equals("삼성동")){
                        result="1G37-1";
                    }else if(address.emdNm.equals("신사동") || address.emdNm.equals("압구정동")){
                        result="1G39-2";
                    }else if(address.emdNm.equals("역삼동")){
                        result="1J13-0";
                    }else if(address.emdNm.equals("청당동")){
                        result="1G37-2";
                    }else{
                        result="1J11-4";
                    }
                }else if(address.sggNm.equals("강동구")){
                    // "동" Check
                    if(address.emdNm.equals("고덕동") || address.emdNm.equals("성내동")){
                        result="2G51-2";
                    }else if(address.emdNm.equals("길동")){
                        result="2G53-2";
                    }else if(address.emdNm.equals("암사동") || address.emdNm.equals("천호동")){
                        result="2G51-1";
                    }else{
                        result="2G53-0";
                    }
                }else if(address.sggNm.equals("강북구")){
                    // "동" Check
                    if(address.emdNm.equals("번동")) {
                        result="2G55-1";
                    }else{
                        result="2G55-2";
                    }
                }else if(address.sggNm.equals("강서구")){
                    // "동" Check
                    if(address.emdNm.equals("가양동") || address.emdNm.equals("등촌동")){
                        result="3T87-2";
                    }else if(address.emdNm.equals("내발산동") || address.emdNm.equals("마곡동") || address.emdNm.equals("외발산동")){
                        result="3T89-2";
                    }else if(address.emdNm.equals("우창산동") || address.emdNm.equals("화곡1동") || address.emdNm.equals("화곡2동") || address.emdNm.equals("화곡4동")
                            || address.emdNm.equals("화곡8동") || address.emdNm.equals("화곡본동") || address.emdNm.equals("화곡동")){
                        result="3T89-1";
                    }else if(address.emdNm.equals("화곡3동") || address.emdNm.equals("화곡6동")){
                        result="3T89-2";
                    }else{
                        result="3T87-1";
                    }
                }else if(address.sggNm.equals("관악구")){
                    // "동" Check
                    if(address.emdNm.equals("난곡동") || address.emdNm.equals("미성동")){
                        result="4H70-2";
                    }else if(address.emdNm.equals("난향동") || address.emdNm.equals("대학동") || address.emdNm.equals("삼성동")){
                        result="4H70-1";
                    }else if(address.emdNm.equals("보라매동") || address.emdNm.equals("성현동")){
                        result="4H71-2";
                    }else if(address.emdNm.equals("봉천동")){
                        result="4H71-3";
                    }else if(address.emdNm.equals("서림동") || address.emdNm.equals("서원동")){
                        result="4H70-3";
                    }else if(address.emdNm.equals("신림동") || address.emdNm.equals("신사동") || address.emdNm.equals("조원동")){
                        result="4H70-1";
                    }else if(address.emdNm.equals("신원동")){
                        result="4H70-2";
                    }else if(address.emdNm.equals("은천동") || address.emdNm.equals("중앙동") || address.emdNm.equals("청룡동")){
                        result="4H71-2";
                    }else{
                        result="4H71-1";
                    }
                }else if(address.sggNm.equals("광진구")){
                    if(address.emdNm.equals("구의동") || address.emdNm.equals("자양동")){
                        result="2R36-0";
                    }else{
                        result="2N99-0";
                    }
                }else if(address.sggNm.equals("구로구")){
                    if(address.emdNm.equals("가리봉동")){
                        result="4D27-1";
                    }else if(address.emdNm.equals("개봉동") || address.emdNm.equals("고척동")){
                        result="4D28-3";
                    }else if(address.emdNm.equals("구로1동") || address.emdNm.equals("구로2동") || address.emdNm.equals("구로4동") || address.emdNm.equals("구로5동")){
                        result="4D27-2";
                    }else if(address.emdNm.equals("구로3동") || address.emdNm.equals("구로동")){
                        result="4D27-1";
                    }else if(address.emdNm.equals("천왕동")){
                        result="4D28-5";
                    }else{
                        result="4D28-4";
                    }
                }else if(address.sggNm.equals("금천구")){
                    if(address.emdNm.equals("가산동")){
                        result="4H79-1";
                    }else if(address.emdNm.equals("독산동")){
                        result="4H79-2";
                    }else if(address.emdNm.equals("시흥동")){
                        result="4H79-3";
                    }
                }else if(address.sggNm.equals("노원구")){
                    if(address.emdNm.equals("상계동")){
                        result="2C07-1";
                    }else{
                        result="2C07-2";
                    }
                }else if(address.sggNm.equals("도봉구")){
                    if(address.emdNm.equals("창동")){
                        result="2N07-1";
                    }else{
                        result="2N07-0";
                    }
                }else if(address.sggNm.equals("동대문구")){
                    if(address.emdNm.equals("답십리동") || address.emdNm.equals("신설동")){
                        result="2C09-2";
                    }else if(address.emdNm.equals("이문동")){
                        result="2C09-3";
                    }else if(address.emdNm.equals("장안동")){
                        result="1J55-3";
                    }else if(address.emdNm.equals("전농동")){
                        result="2C09-2";
                    }else{
                        result="2C09-1";
                    }
                }else if(address.sggNm.equals("동작구")){
                    if(address.emdNm.equals("노량진동")){
                        result="3S60-4";
                    }else if(address.emdNm.equals("대방동")){
                        result="3S60-3";
                    }else if(address.emdNm.equals("동작동") || address.emdNm.equals("사당동")){
                        result="3S60-1";
                    }else if(address.emdNm.equals("본동")){
                        result="3S60-2";
                    }else if(address.emdNm.equals("상도동") || address.emdNm.equals("신대방동") || address.emdNm.equals("흑석동")){
                        result="3T94-0";
                    }
                }else if(address.sggNm.equals("마포구")){
                    if(address.emdNm.equals("망원동") || address.emdNm.equals("상암동")){
                        result="3S65-0";
                    }else if(address.emdNm.equals("성산동") || address.emdNm.equals("연남동")){
                        result="3S65-0";
                    }else if(address.emdNm.equals("중동") || address.emdNm.equals("합정동")){
                        result="3S65-0";
                    }else{
                        result="3S63-0";
                    }
                }else if(address.sggNm.equals("서대문구")){
                    if(address.emdNm.equals("남가좌동") || address.emdNm.equals("냉천동") || address.emdNm.equals("북가좌동")){
                        result="3S67-0";
                    }else if(address.emdNm.equals("영천동") || address.emdNm.equals("옥천동") || address.emdNm.equals("천연동")){
                        result="3S67-0";
                    }else if(address.emdNm.equals("현저동") || address.emdNm.equals("홍은동") || address.emdNm.equals("홍제동")){
                        result="3S67-0";
                    }else{
                        result="3T04-0";
                    }
                }else if(address.sggNm.equals("서초구")){
                    if(address.emdNm.equals("반포동") || address.emdNm.equals("반포본동") || address.emdNm.equals("방배동")
                            || address.emdNm.equals("방배본동")){
                        result="1B04-0";
                    }else if(address.emdNm.equals("서초동")){
                        result="1G48-0";
                    }else if(address.emdNm.equals("잠원동")){
                        result="1B05-0";
                    }else{
                        result="1C93-0";
                    }
                }else if(address.sggNm.equals("성동구")){
                    if(address.emdNm.equals("금호동1가") ||address.emdNm.equals("금호동2가") || address.emdNm.equals("금호동3가") || address.emdNm.equals("금호동4가")
                            || address.emdNm.equals("도선동") || address.emdNm.equals("마장동") || address.emdNm.equals("사근동")){
                        result="1C25-0";
                    }else if(address.emdNm.equals("상왕십리동") || address.emdNm.equals("왕십리2동")){
                        result="1C25-0";
                    }else if(address.emdNm.equals("하왕십리동") || address.emdNm.equals("행당동") || address.emdNm.equals("홍익동")){
                        result="1C25-0";
                    }else{
                        result="1J55-0";
                    }
                }else if(address.sggNm.equals("성북구")){
                    if(address.emdNm.equals("돈암동") ||address.emdNm.equals("성북동") || address.emdNm.equals("정릉동")){
                        result="2R40-2";
                    }else if(address.emdNm.equals("석관동") || address.emdNm.equals("장위동") || address.emdNm.equals("종암동")){
                        result="2R40-1";
                    }else{
                        result="2R43-0";
                    }
                }else if(address.sggNm.equals("송파구")){
                    if(address.emdNm.equals("가락동") ||address.emdNm.equals("가락본동")){
                        result="1A10-2";
                    }else if(address.emdNm.equals("거여동")){
                        result="1A10-1";
                    }else if(address.emdNm.equals("문정동")){
                        result="1A10-3";
                    }else if(address.emdNm.equals("삼전동") || address.emdNm.equals("석촌동") || address.emdNm.equals("신천동")
                            || address.emdNm.equals("잠실동") || address.emdNm.equals("잠실본동") || address.emdNm.equals("풍납동")){
                        result="1G10-0";
                    }else if(address.emdNm.equals("장지동")){
                        result="1A10-0";
                    }else {
                        result="1A12-0";
                    }
                }else if(address.sggNm.equals("양천구")){
                    if(address.emdNm.equals("목동")){
                        result="3S69-1";
                    }else if(address.emdNm.equals("신월동")){
                        result="3S69-2";
                    }else if(address.emdNm.equals("신정1동") || address.emdNm.equals("신정2동") || address.emdNm.equals("신정4동")
                            || address.emdNm.equals("신정6동") || address.emdNm.equals("신정7동") || address.emdNm.equals("신정동")){
                        result="3S69-3";
                    }else if(address.emdNm.equals("신정3동")){
                        result="3S69-5";
                    }
                }else if(address.sggNm.equals("영등포구")){
                    if(address.emdNm.equals("대림동") || address.emdNm.equals("도림동") || address.emdNm.equals("신길동")
                            || address.emdNm.equals("영등포동1가") || address.emdNm.equals("영등포동2가") || address.emdNm.equals("영등포동3가")
                            || address.emdNm.equals("영등포동4가") || address.emdNm.equals("영등포동5가") || address.emdNm.equals("영등포동6가")
                            || address.emdNm.equals("영등포동7가") || address.emdNm.equals("영등포동8가")
                            || address.emdNm.equals("영등포본동1가") || address.emdNm.equals("영등포본동2가") || address.emdNm.equals("영등포본동3가")
                            || address.emdNm.equals("영등포본동4가") || address.emdNm.equals("영등포본동5가") || address.emdNm.equals("영등포본동6가")
                            || address.emdNm.equals("영등포본동7가") || address.emdNm.equals("영등포본동8가")){
                        result="3T74-0";
                    }else{
                        result="3T72-0";
                    }
                }else if(address.sggNm.equals("용산구")){
                    if(address.emdNm.equals("동빙고동") || address.emdNm.equals("서빙고동")){
                        result="1J00-2";
                    }else if(address.emdNm.equals("보광동")){
                        result="1J00-5";
                    }else if(address.emdNm.equals("용산동1가") || address.emdNm.equals("용산동3가") || address.emdNm.equals("용산동5가")
                            || address.emdNm.equals("용산동6가")){
                        result="1J00-2";
                    }else if(address.emdNm.equals("용산동2가") || address.emdNm.equals("용산동4가") || address.emdNm.equals("이태원동")){
                        result="1J00-1";
                    }else if(address.emdNm.equals("이촌1동") || address.emdNm.equals("이촌동") || address.emdNm.equals("동부이촌동")){
                        result="1J00-5";
                    }else if(address.emdNm.equals("이촌2동") || address.emdNm.equals("서부이촌동")){
                        result="1J00-6";
                    }else if(address.emdNm.equals("주성동")){
                        result="1J00-2";
                    }else if(address.emdNm.equals("한강로1가") || address.emdNm.equals("한강로2가")){
                        result="1J00-3";
                    }else if(address.emdNm.equals("한강로3가")){
                        result="1J00-4";
                    }else if(address.emdNm.equals("한남동")){
                        result="1J00-1";
                    }else{
                        result="1A60-0";
                    }
                }else if(address.sggNm.equals("은평구")){
                    if(address.emdNm.equals("불광동") || address.emdNm.equals("신사동")){
                        result="3T08-0";
                    }else if(address.emdNm.equals("역촌동") || address.emdNm.equals("증산동") || address.emdNm.equals("진관동")){
                        result="3T08-0";
                    }else{
                        result="3T06-0";
                    }
                }else if(address.sggNm.equals("종로구")){
                    if(address.emdNm.equals("가회동") || address.emdNm.equals("견지동") || address.emdNm.equals("계동")
                            || address.emdNm.equals("공평동") || address.emdNm.equals("관수동") || address.emdNm.equals("관철동")
                            || address.emdNm.equals("돈의동")){
                        result="3S80-1";
                    }else if(address.emdNm.equals("동숭동") || address.emdNm.equals("숭인동") || address.emdNm.equals("이화동")){
                        result="3S80-2";
                    }else if(address.emdNm.equals("명륜1가") || address.emdNm.equals("명륜2가") || address.emdNm.equals("명륜3가")
                            || address.emdNm.equals("명륜4가") || address.emdNm.equals("묘동") || address.emdNm.equals("봉익동")
                            || address.emdNm.equals("송현동") || address.emdNm.equals("수송동") || address.emdNm.equals("안국동")
                            || address.emdNm.equals("연건동") || address.emdNm.equals("연지동") || address.emdNm.equals("예지동")
                            || address.emdNm.equals("운니동") || address.emdNm.equals("원남동") || address.emdNm.equals("원서동")
                            || address.emdNm.equals("인사동") || address.emdNm.equals("인의동") || address.emdNm.equals("장사동")
                            || address.emdNm.equals("재동") || address.emdNm.equals("종로1가") || address.emdNm.equals("종로2가")
                            || address.emdNm.equals("종로3가") || address.emdNm.equals("종로4가") || address.emdNm.equals("종로5가")
                            || address.emdNm.equals("중학동")){
                        result="3S80-1";
                    }else if(address.emdNm.equals("종로6가") || address.emdNm.equals("창신동")){
                        result="3S80-2";
                    }else if(address.emdNm.equals("청진동") || address.emdNm.equals("혜화동") || address.emdNm.equals("훈정동")){
                        result="3S80-1";
                    }else if(address.emdNm.equals("충신동") || address.emdNm.equals("효제동")){
                        result="3S80-2";
                    }else{
                        result="3T16-1";
                    }
                }else if(address.sggNm.equals("중구")){
                    if(address.emdNm.equals("광희동1가") || address.emdNm.equals("광희동2가") || address.emdNm.equals("남학동")
                            || address.emdNm.equals("다산동") || address.emdNm.equals("동화동") || address.emdNm.equals("목정동")){
                        result="3T13-0";
                    }else if(address.emdNm.equals("무학동") || address.emdNm.equals("방산동") || address.emdNm.equals("수표동")
                            || address.emdNm.equals("신당5동") || address.emdNm.equals("신당동") || address.emdNm.equals("을지로3가")){
                        result="3T14-0";
                    }else if(address.emdNm.equals("쌍림동") || address.emdNm.equals("약수동") || address.emdNm.equals("예관동")
                            || address.emdNm.equals("오장동") || address.emdNm.equals("을지로5가") || address.emdNm.equals("을지로6가")
                            || address.emdNm.equals("을지로7가")){
                        result="3T13-0";
                    }else if(address.emdNm.equals("인현동1가") || address.emdNm.equals("인현동2가") || address.emdNm.equals("주교동")){
                        result="3T14-0";
                    }else if(address.emdNm.equals("장충동1가") || address.emdNm.equals("장충동2가") || address.emdNm.equals("주자동")
                            || address.emdNm.equals("청구동") || address.emdNm.equals("초동") || address.emdNm.equals("충무로3가")
                            || address.emdNm.equals("충무로4가") || address.emdNm.equals("충무로5가") || address.emdNm.equals("필동1가")
                            || address.emdNm.equals("필동2가") || address.emdNm.equals("필동3가")){
                        result="3T13-0";
                    }else if(address.emdNm.equals("황학동") || address.emdNm.equals("홍인동")){
                        result="3T14-0";
                    }else{
                        result="3T17-0";
                    }
                }else if(address.sggNm.equals("중랑구")){
                    if(address.emdNm.equals("묵동") || address.emdNm.equals("상봉동") || address.emdNm.equals("신내동")){
                        result="2C18-2";
                    }else{
                        result="2C18-1";
                    }
                }
            }else if(address.ssNm.contains("경기")){
                if(address.sggNm.equals("가평군")){
                    result= "9B77-0";
                }else if(address.sggNm.equals("고양시 덕양구")){
                    if(address.emdNm.equals("내곡동") || address.emdNm.equals("대장동")){
                        result= "3T92-3";
                    }else if(address.emdNm.equals("성사동")){
                        result= "3T92-2";
                    }else if(address.emdNm.equals("신평동")){
                        result= "3T92-3";
                    }else if(address.emdNm.equals("주교동") || address.emdNm.equals("토당동")){
                        result= "3T92-2";
                    }else if(address.emdNm.equals("행신동") || address.emdNm.equals("행주내동") || address.emdNm.equals("행주외동")){
                        result= "3T92-3";
                    }else if(address.emdNm.equals("화정동")){
                        result="3T92-2";
                    }else{
                        result= "3T92-1";
                    }
                }else if(address.sggNm.equals("고양시 일산동구")){
                    if(address.emdNm.equals("마두동") || address.emdNm.equals("백석동")){
                        result= "3S76-1";
                    }else if(address.emdNm.equals("장항동") || address.emdNm.equals("정발산동")){
                        result= "3S76-1";
                    }else{
                        result= "3S76-2";
                    }
                }else if(address.sggNm.equals("고양시 일산서구")){
                    if(address.emdNm.equals("가좌동") || address.emdNm.equals("구산동") || address.emdNm.equals("덕이동")){
                        result= "3S78-3";
                    }else if(address.emdNm.equals("법곳동") || address.emdNm.equals("주엽동")){
                        result= "3S78-2";
                    }else{
                        result= "3S78-1";
                    }
                }else if(address.sggNm.equals("과천시")){
                    result= "1C20-1";
                }else if(address.sggNm.equals("광명시")){
                    if(address.emdNm.equals("철산동") || address.emdNm.equals("하안동")){
                        result= "4H73-0";
                    }else{
                        result= "4H75-0";
                    }
                }else if(address.sggNm.equals("광주시")){
                    result="2G57-0";
                }else if(address.sggNm.equals("구리시")){
                    if(address.emdNm.equals("갈매동") || address.emdNm.equals("동구동")
                            || address.emdNm.equals("사노동") || address.emdNm.equals("인창동")){
                        result= "2R38-1";
                    }else{
                        result= "2R38-2";
                    }
                }else if(address.sggNm.equals("군포시")){
                    if(address.emdNm.equals("광정동") || address.emdNm.equals("궁내동")
                            || address.emdNm.equals("당정동") || address.emdNm.equals("산본동") || address.emdNm.equals("수리동")){
                        result= "4H77-2";
                    }else{
                        result= "4H77-1";
                    }

                }else if(address.sggNm.equals("김포시")){
                    if(address.emdNm.equals("감정동") || address.emdNm.equals("걸포동")
                            || address.emdNm.equals("고촌읍") || address.emdNm.equals("대곶면") || address.emdNm.equals("북변동")
                            || address.emdNm.equals("사우동") || address.emdNm.equals("풍우동")){
                        result= "3S58-2";
                    }else{
                        result= "3S58-1";
                    }
                }else if(address.sggNm.equals("남양주시")){
                    if(address.emdNm.equals("금곡동") || address.emdNm.equals("평내동") || address.emdNm.equals("화도읍")){
                        result= "2N03-1";
                    }else if(address.emdNm.equals("도농동")){
                        result= "2N01-2";
                    }else if(address.emdNm.equals("별내동") || address.emdNm.equals("별내면")){
                        result= "2N01-1";
                    }else if(address.emdNm.equals("오남읍") || address.emdNm.equals("지금동") || address.emdNm.equals("진건읍")){
                        result= "2N01-2";
                    }else if(address.emdNm.equals("진접읍")){
                        result= "2N01-3";
                    }else if(address.emdNm.equals("퇴계원면")){
                        result= "2N01-2";
                    }else{
                        result= "2N03-2";
                    }
                }else if(address.sggNm.equals("동두천시")){
                    result= "2G62-2";
                }else if(address.sggNm.contains("부천시")){
                    //구 원미구
                    if(address.emdNm.equals("도당동")){
                        result= "3T98-4";
                    }else if(address.emdNm.equals("상동")){
                        result= "3T96-3";
                    }else if(address.emdNm.equals("소사동") || address.emdNm.equals("약대동")){
                        result= "3T97-2";
                    }else if(address.emdNm.equals("심곡동")){
                        result= "3T96-2";
                    }else if(address.emdNm.equals("역곡동") || address.emdNm.equals("원미동")){
                        result= "3T97-1";
                    }else if(address.emdNm.equals("중동")){
                        result= "3T97-3";
                    }else if(address.emdNm.equals("춘의동")){
                        result= "3T98-5";
                    }
                    //구 오정구
                    else if(address.emdNm.contains("성곡") || address.emdNm.contains("원종")
                            || address.emdNm.contains("고강")
                            || address.emdNm.contains("오정") || address.emdNm.contains("신흥")){
                        result= "3S56-0";
                    }
                    //구 소사구
                    else if(address.emdNm.equals("괴안동") || address.emdNm.equals("범박동")){
                        result= "3T02-1";
                    }else if(address.emdNm.equals("옥길동")){
                        result= "3T02-2";
                    }else{
                        result= "3T02-4";
                    }
                }else if(address.sggNm.equals("성남시 분당구")){
                    if(address.emdNm.equals("분당동") || address.emdNm.equals("서현동")){
                        result= "2G59-2";
                    }else if(address.emdNm.equals("수내동")){
                        result= "2G59-1";
                    }else if(address.emdNm.equals("야탑동") || address.emdNm.equals("율동") || address.emdNm.equals("이매동")){
                        result= "2G59-2";
                    }else if(address.emdNm.equals("정자동")){
                        result= "2G59-1";
                    }else{
                        result= "1C12-0";
                    }
                }else if(address.sggNm.equals("성남시 수정구")){
                    if(address.emdNm.equals("산성동") || address.emdNm.equals("수진동") || address.emdNm.equals("신흥동")){
                        result= "2R20-2";
                    }else{
                        result= "2R20-1";
                    }
                }else if(address.sggNm.equals("성남시 중원구")){
                    if(address.emdNm.equals("갈현동")){
                        result= "2R21-1";
                    }else if(address.emdNm.equals("금광동")){
                        result= "1C12-0";
                    }else if(address.emdNm.equals("도촌동") || address.emdNm.equals("상대원동") || address.emdNm.equals("하대원동")){
                        result= "2R21-1";
                    }else{
                        result= "2R21-2";
                    }
                }else if(address.sggNm.equals("수원시 권선구")){
                    if(address.emdNm.equals("서둔동") || address.emdNm.equals("오목천동") || address.emdNm.equals("탑동")){
                        result= "4D36-3";
                    }else{
                        result= "4D32-1";
                    }
                }else if(address.sggNm.equals("수원시 영통구")){
                    if(address.emdNm.equals("망포동")){
                        result= "4H91-5";
                    }else if(address.emdNm.equals("매탄동")){
                        result= "4H91-1";
                    }else if(address.emdNm.equals("신동")){
                        result= "4H91-6";
                    }else if(address.emdNm.equals("영통동")){
                        result= "4H91-2";
                    }else if(address.emdNm.equals("원천동")){
                        result= "4H91-4";
                    }else if(address.emdNm.equals("이의동") || address.emdNm.equals("하동")){
                        result= "4H91-3";
                    }
                }else if(address.sggNm.equals("수원시 장안구")){
                    result="1G20-0";
                }else if(address.sggNm.equals("수원시 팔달구")){
                    if(address.emdNm.equals("우만동") || address.emdNm.equals("인계동") || address.emdNm.equals("지동")){
                        result= "4D36-4";
                    }else{
                        result= "4D36-5";
                    }
                }else if(address.sggNm.equals("시흥시")){
                    if(address.emdNm.equals("거모동")){
                        result= "4H88-2";
                    }else if(address.emdNm.equals("정왕동") || address.emdNm.equals("정왕본동")){
                        result= "4H88-1";
                    }else if(address.emdNm.equals("죽율동")){
                        result= "4H88-2";
                    }else{
                        result= "4H89-0";
                    }
                }else if(address.sggNm.equals("안산시 단원구")){
                    result= "4D41-0";
                }else if(address.sggNm.equals("안산시 상록구")){
                    if(address.emdNm.equals("건건동") || address.emdNm.equals("본오동") || address.emdNm.equals("사사동")
                            || address.emdNm.equals("사동") || address.emdNm.equals("팔곡이동") || address.emdNm.equals("팔곡일동")){
                        result= "4D49-1";
                    }else{
                        result= "4D49-4";
                    }
                }else if(address.sggNm.equals("안성시")){
                    if(address.emdNm.equals("공도읍")){
                        result= "1J24-1";
                    }else{
                        result= "1J24-2";
                    }
                }else if(address.sggNm.equals("안양시 동안구")){
                    if(address.emdNm.equals("관양동") || address.emdNm.equals("귀인동") || address.emdNm.equals("부림동")){
                        result= "4D43-1";
                    }else if(address.emdNm.equals("비산동")){
                        result= "4D43-3";
                    }else if(address.emdNm.equals("평안동") || address.emdNm.equals("평촌동")){
                        result= "4D43-1";
                    }else{
                        result= "4D43-2";
                    }
                }else if(address.sggNm.equals("안양시 만안구")){
                    result= "4H86-0";
                }else if(address.sggNm.equals("양주시")){
                    result= "2G62-1";
                }else if(address.sggNm.equals("양평군")){
                    result ="8Z35-0";
                }else if(address.sggNm.equals("여주시")){
                    result= "8E65-0";
                }else if(address.sggNm.equals("연천군")){
                    result= "2R52-3";
                }else if(address.sggNm.equals("오산시")){
                    result="1C03-0";
                }else if(address.sggNm.equals("용인시 기흥구")){
                    if(address.emdNm.equals("고매동") || address.emdNm.equals("공세동")){
                        result= "4D77-3";
                    }else if(address.emdNm.equals("구갈동")){
                        result= "4D77-1";
                    }else if(address.emdNm.equals("동백동")){
                        result= "4D77-4";
                    }else if(address.emdNm.equals("보라동")){
                        result= "4D77-1";
                    }else if(address.emdNm.equals("보정동")){
                        result= "4D77-5";
                    }else if(address.emdNm.equals("상갈동")){
                        result= "4D77-3";
                    }else if(address.emdNm.equals("상하동") || address.emdNm.equals("중동")){
                        result= "4D77-4";
                    }else if(address.emdNm.equals("언남동")){
                        result= "4H50-2";
                    }else if(address.emdNm.equals("지곡동") || address.emdNm.equals("청덕동")){
                        result= "4D77-3";
                    }else{
                        result= "4D77-2";
                    }
                }else if(address.sggNm.equals("용인시 수지구")){
                    if(address.emdNm.equals("동천동") || address.emdNm.equals("상현동") || address.emdNm.equals("풍덕천동")){
                        result= "4H50-2";
                    }else{
                        result= "4H50-1";
                    }
                }else if(address.sggNm.equals("용인시 처인구")){
                    if(address.emdNm.equals("김량창동") || address.emdNm.equals("삼가동") || address.emdNm.equals("역북동")){
                        result= "1C65-4";
                    }else if(address.emdNm.equals("남사면") || address.emdNm.equals("모현면") || address.emdNm.equals("포곡읍")){
                        result= "1C65-3";
                    }else if(address.emdNm.equals("백암면") || address.emdNm.equals("양지면") || address.emdNm.equals("원삼면")){
                        result= "1C65-2";
                    }else{
                        result= "1C65-1";
                    }
                }else if(address.sggNm.equals("의왕시")){
                    result="1C20-2";
                }else if(address.sggNm.equals("의정부시")){
                    if(address.emdNm.equals("가능동") || address.emdNm.equals("녹양동") || address.emdNm.equals("신곡동")){
                        result= "2N12-2";
                    }else if(address.emdNm.equals("산곡동")){
                        result= "2N12-0";
                    }else if(address.emdNm.equals("의정부동")){
                        result= "2N12-3";
                    }else if(address.emdNm.equals("장암동")){
                        result= "2N12-2";
                    }else if(address.emdNm.equals("호원동")){
                        result= "2N12-3";
                    }else{
                        result= "2N12-0";
                    }
                }else if(address.sggNm.equals("이천시")){
                    result="1J26-0";
                }else if(address.sggNm.equals("파주시")){
                    if(address.emdNm.equals("검산동") || address.emdNm.equals("교하동") || address.emdNm.equals("다율동")
                            || address.emdNm.equals("당하동") || address.emdNm.equals("동패동") || address.emdNm.equals("목동동")
                            || address.emdNm.equals("문발동") || address.emdNm.equals("산남동") || address.emdNm.equals("상지석동")
                            || address.emdNm.equals("서패동") || address.emdNm.equals("송촌동") || address.emdNm.equals("신촌동")
                            || address.emdNm.equals("야당동") || address.emdNm.equals("야동동") || address.emdNm.equals("얀다산동")
                            || address.emdNm.equals("오도동") || address.emdNm.equals("와동동") || address.emdNm.equals("하지석동")){
                        result= "3T19-1";
                    }else{
                        result= "3T19-2";
                    }
                }else if(address.sggNm.equals("평택시")){
                    if(address.emdNm.equals("가재동") || address.emdNm.equals("고덕면") || address.emdNm.equals("도일동")
                            || address.emdNm.equals("독곡동") || address.emdNm.equals("모곡동") || address.emdNm.equals("서정동")
                            || address.emdNm.equals("서탄면") || address.emdNm.equals("신장동") || address.emdNm.equals("오성면")
                            || address.emdNm.equals("이충동") || address.emdNm.equals("장당동") || address.emdNm.equals("장안동")
                            || address.emdNm.equals("지산동") || address.emdNm.equals("진위면")){
                        result= "1D25-0";
                    }else{
                        result= "1D23-0";
                    }
                }else if(address.sggNm.equals("포천시")){
                    if(address.emdNm.equals("가산면")) {
                        result= "2R52-2";
                    }else if(address.emdNm.equals("관인면") || address.emdNm.equals("영북면")){
                        result= "9D73-0";
                    }else if(address.emdNm.equals("내촌면") || address.emdNm.equals("소흘읍")){
                        result= "2R52-2";
                    }else{
                        result= "2R52-1";
                    }
                }else if(address.sggNm.equals("하남시")){
                    result= "2N14-0";
                }else if(address.sggNm.equals("화성시")){
                    if(address.emdNm.equals("기산동") || address.emdNm.equals("기안동") || address.emdNm.equals("능동")
                            || address.emdNm.equals("동탄면") || address.emdNm.equals("반송동")) {
                        result= "2R00-1";
                    }else if(address.emdNm.equals("반월동")){
                        result= "2R01-0";
                    }else if(address.emdNm.equals("반정동") || address.emdNm.equals("배양동")){
                        result= "2R00-1";
                    }else if(address.emdNm.equals("병첨동")){
                        result= "2R01-0";
                    }else if(address.emdNm.equals("봉담읍")){
                        result= "1A15-2";
                    }else if(address.emdNm.equals("석우동") || address.emdNm.equals("송산동") || address.emdNm.equals("안녕동")){
                        result= "2R00-1";
                    }else if(address.emdNm.equals("송산면")){
                        result= "1A15-0";
                    }else if(address.emdNm.equals("영천동") || address.emdNm.equals("오산동")){
                        result= "2R00-1";
                    }else if(address.emdNm.equals("정남면")){
                        result= "1C03-0";
                    }else if(address.emdNm.equals("진안동")){
                        result= "2R01-0";
                    }else if(address.emdNm.equals("청계동") || address.emdNm.equals("황계동")){
                        result= "2R00-1";
                    }else{
                        result= "1A15-0";
                    }
                }
            } else if (address.ssNm.contains("강원")) {
                if (address.sggNm.equals("강릉시")) {
                    if (address.emdNm.equals("교동") || address.emdNm.equals("금학동") || address.emdNm.equals("남문동") || address.emdNm.equals("대전동")
                            || address.emdNm.equals("명주동") || address.emdNm.equals("사천면") || address.emdNm.equals("성남동") || address.emdNm.equals("성내동")
                            || address.emdNm.equals("성산면") || address.emdNm.equals("연곡면")) {
                        result = "1G44-1";
                    } else if (address.emdNm.equals("옥계면")) {
                        result = "8E83-2";
                    } else if (address.emdNm.equals("옥천동") || address.emdNm.equals("왕산면") || address.emdNm.equals("용강동") ||
                            address.emdNm.equals("유천동") || address.emdNm.equals("임당동") || address.emdNm.equals("주문진읍") ||
                            address.emdNm.equals("죽헌동") || address.emdNm.equals("지변동") || address.emdNm.equals("홍제동")) {
                        result = "1G44-1";
                    } else {
                        result = "1G44-2";
                    }
                }else if(address.sggNm.equals("고성군")){
                    result = "8E89-4";
                }else if(address.sggNm.equals("동해시")){
                    result = "8E83-1";
                }else if(address.sggNm.equals("삼척시")){
                    if(address.emdNm.equals("도계읍")){
                        result = "9K82-1";
                    }else if(address.emdNm.equals("하장면")){
                        result = "9K82-0";
                    }else{
                        result="8E83-2";
                    }
                }else if(address.sggNm.equals("속초시")){
                    if(address.emdNm.equals("노학동")||address.emdNm.equals("대포동")||address.emdNm.equals("도문동")||
                            address.emdNm.equals("설악동")||address.emdNm.equals("조양동")||address.emdNm.equals("청호동")){
                        result="8E89-2";
                    }else{
                        result="8E89-1";
                    }

                }else if(address.sggNm.equals("양구군")){
                    result = "9D69-0";
                }else if(address.sggNm.equals("양양군")){
                    result="8E89-3";
                }else if(address.sggNm.equals("영월군")){
                    result="9H04-0";
                }else if(address.sggNm.equals("원주시")){
                    if(address.emdNm.equals("귀래면")||address.emdNm.equals("문막읍")||address.emdNm.equals("반곡동")||
                            address.emdNm.equals("부론면")||address.emdNm.equals("우산동")||address.emdNm.equals("인동")||
                            address.emdNm.equals("지정동")||address.emdNm.equals("판부면")||address.emdNm.equals("행구동")||
                            address.emdNm.equals("호저면")||address.emdNm.equals("흥업면")){
                        result = "1A06-1";
                    }else{
                        result= "1A06-2";
                    }
                }else if(address.sggNm.equals("인제군")){
                    result = "9H06-0";
                }else if(address.sggNm.equals("정선군")){
                    if(address.emdNm.equals("북평면")||address.emdNm.equals("여량면")||address.emdNm.equals("정선읍")){
                        result ="9J75-1";
                    }else if(address.emdNm.equals("신동읍")){
                        result = "9H04-0";
                    }else if(address.emdNm.equals("임계면")){
                        result = "1G44-3";
                    }else{
                        result = "9J76-0";
                    }
                }else if(address.sggNm.equals("철원군")){
                    result = "9D73-0";
                }else if(address.sggNm.equals("춘천시")){
                    if(address.emdNm.equals("동내면")||address.emdNm.equals("삼천동")||address.emdNm.equals("석사동")||
                            address.emdNm.equals("송암동")||address.emdNm.equals("온의동")){
                        result="1A08-1";
                    }else if(address.emdNm.equals("요선동")){
                        result ="1A08-3";
                    }else if(address.emdNm.equals("우두동")||address.emdNm.equals("조양동")||address.emdNm.equals("죽림동")||
                            address.emdNm.equals("칠전동")||address.emdNm.equals("퇴계동")){
                        result = "1A08-1";
                    }else{
                        result = "1A08-2";
                    }
                }else if(address.sggNm.equals("태백시")){
                    result = "9K82-0";
                }else if(address.sggNm.equals("평창군")){
                    result ="9B85-0";
                }else if(address.sggNm.equals("홍천군")){
                    result ="9H08-0";
                }else if(address.sggNm.equals("화천군")){
                    result = "1A08-3";
                }else if(address.sggNm.equals("횡성군")){
                    result = "1A06-3";
                }
            }else if(address.ssNm.contains("인천")){
                if(address.sggNm.equals("강화군")){
                    result = "9D63-0";
                }else if(address.sggNm.equals("계양구")){
                    result = "3S54-0";
                }else if(address.sggNm.equals("남구")){
                    if(address.emdNm.equals("관교동")){
                        result = "4D57-4";
                    }else if(address.emdNm.equals("도화동")){
                        result = "4D57-6";
                    }else if(address.emdNm.equals("문학동")){
                        result = "4D57-4";
                    }else if(address.emdNm.equals("숭의동")){
                        result = "4D57-5";
                    }else if(address.emdNm.equals("용현동")){
                        result = "4D57-3";
                    }else if(address.emdNm.equals("주안동")){
                        result = "4D57-1";
                    }else if(address.emdNm.equals("학익동")){
                        result = "4D57-2";
                    }
                }else if(address.sggNm.equals("남동구")){
                    if(address.emdNm.equals("고잔동")||address.emdNm.equals("구월동")||address.emdNm.equals("남촌동")||
                            address.emdNm.equals("논현동")||address.emdNm.equals("수산동")){
                        result = "4H82-0";
                    }
                    else{
                        result = "4H84-0";
                    }
                }else if(address.sggNm.equals("동구")){
                    result = "4D58-2";
                }else if(address.sggNm.equals("부평구")){
                    if(address.emdNm.equals("산곡동")){
                        result = "4D45-0";
                    }else if(address.emdNm.equals("십정동")){
                        result = "4D61-0";
                    }else if(address.emdNm.equals("청천동")){
                        result = "4D45-0";
                    }else{
                        result = "4D47-0";
                    }

                }else if(address.sggNm.equals("서구")){
                    if(address.emdNm.equals("가정동")||address.emdNm.equals("가좌동")||address.emdNm.equals("공촌동")||
                            address.emdNm.equals("석남동")||address.emdNm.equals("신현동")||
                            address.emdNm.equals("심곡동")||address.emdNm.equals("연희동")){
                        result = "4D45-0";
                    }else{
                        result = "4D61-0";
                    }

                }else if(address.sggNm.equals("연수구")){
                    if(address.emdNm.equals("송도동")){
                        result = "4D52-1";
                    }else{
                        result = "4D52-2";
                    }

                }else if(address.sggNm.equals("옹진군")){
                    if(address.emdNm.equals("영흥면")){
                        result = "4D41-3";
                    }else{
                        result ="4D58-3";
                    }
                }else if(address.sggNm.equals("중구")){
                    if(address.emdNm.equals("남북동")||address.emdNm.equals("덕교동")||address.emdNm.equals("무의동")||
                            address.emdNm.equals("운남동")||address.emdNm.equals("운북동")||address.emdNm.equals("운서동")||
                            address.emdNm.equals("을왕동")||address.emdNm.equals("중산동")){
                        result ="4D58-3";
                    }else{
                        result = "4D58-1";
                    }
                }

                result+= "\n\n[도선료]";
                result+= "\n* 강화군 삼산면, 서도면: 4500원";
                result+= "\n* 옹진군(영흥면 제외): 6000원";
                result+= "\n* 중구 무의동: 6000원";


            } else if (address.ssNm.contains("충청남도")) {
                if (address.sggNm.equals("계룡시")) {
                    result = "2R10-1";
                } else if (address.sggNm.equals("공주시")) {
                    result = "8Z22-0";
                } else if (address.sggNm.equals("금산군")) {
                    result = "0Q22-0";
                } else if (address.sggNm.equals("논산시")) {
                    result = "8Y55-0";
                } else if (address.sggNm.equals("당진시")) {
                    result = "8Y39-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 석문면(난지도리): 4000원";
                    result+= "\n* 신평면(매산리) (행담도만 해당): 6000원";
                } else if (address.sggNm.equals("보령시")) {
                    result = "9J70-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 오천면(고대도리, 녹도리 호도, 삽시도리, 외연도리, " +
                            "외연도리, 원산도리, 장고도리, 효자도리 소도/월도/육도/추도/허육도): 5000원";
                } else if (address.sggNm.equals("부여군")) {
                    result = "0W20-0";
                } else if (address.sggNm.equals("서산시")) {
                    result = "8E26-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 지곡면(도성리 분점도/우도): 7000원";
                } else if (address.sggNm.equals("서천군")) {
                    result = "9J73-0";
                } else if (address.sggNm.equals("아산시")) {
                    if(address.emdNm.equals("남동")||address.emdNm.equals("배방읍")||address.emdNm.equals("법곡동")||
                            address.emdNm.equals("신동")||address.emdNm.equals("온양동")||address.emdNm.equals("온천동")||
                            address.emdNm.equals("읍내동")||address.emdNm.equals("탕정면")||address.emdNm.equals("풍기동")){
                        result = "1H64-2";
                    }else{
                        result = "1H64-1";
                    }
                } else if (address.sggNm.equals("예산군")) {
                    result = "8E70-2";
                } else if (address.sggNm.equals("천안시 동남구")) {
                    if(address.emdNm.equals("광덕면")||address.emdNm.equals("다가동")||address.emdNm.equals("대흥동")||
                            address.emdNm.equals("동면")||address.emdNm.equals("봉명동")||
                            address.emdNm.equals("사직동")||address.emdNm.equals("성황동")){
                        result = "1K60-2";
                    }else if(address.emdNm.equals("신부동")){
                        result = "1K52-0";
                    }else if(address.emdNm.equals("쌍용동")){
                        result = "1K60-2";
                    }else if(address.emdNm.equals("안서동")){
                        result = "1K52-0";
                    }else if(address.emdNm.equals("오룡동")){
                        result ="1K60-2";
                    }else {
                        result = "1K60-1";
                    }

                } else if (address.sggNm.equals("천안시 서북구")) {
                    if(address.emdNm.equals("두정동")||address.emdNm.equals("백석동")||address.emdNm.equals("성성동")){
                        result = "1K51-1";
                    }else if(address.emdNm.equals("성정동")||address.emdNm.equals("쌍용동")){
                        result = "1K60-2";
                    }else if(address.emdNm.equals("업성동")||address.emdNm.equals("차암동")){
                        result = "1K51-1";
                    }else if(address.emdNm.equals("와촌동")){
                        result = "1K60-2";
                    }else{
                        result = "1K51-2";
                    }
                } else if (address.sggNm.equals("청양군")) {
                    result = "0V11-0";
                } else if (address.sggNm.equals("태안군")) {
                    result = "9K44-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 근흥면(가의도리): 5000원";
                } else if (address.sggNm.equals("홍성군")) {
                    result = "8E70-1";
                }
            }
            else if(address.ssNm.contains("세종")){
                if(address.emdNm.equals("고운동")||address.emdNm.equals("다정동")){
                    result = "4H21-1";
                }else if(address.emdNm.equals("대평동")||address.emdNm.equals("반곡동")||address.emdNm.equals("보람동")){
                    result = "4H21-0";
                }else if(address.emdNm.equals("부강면")){
                    result ="4H21-2";
                }else if(address.emdNm.equals("새롬동")){
                    result ="4H21-1";
                }else if(address.emdNm.equals("소담동")){
                    result ="4H21-0";
                }else if(address.emdNm.equals("아름동")){
                    result ="4H21-1";
                }else if(address.emdNm.equals("장군면")){
                    result ="8Z22-0";
                }else{
                    result ="4H20-0";
                }
            }else if(address.ssNm.contains("경상북도")){
                if(address.sggNm.equals("군위군")){
                    result = "0Q54-0";
                }else if(address.sggNm.equals("경산시")){
                    if(address.emdNm.equals("백천동")||address.emdNm.equals("사정동")||address.emdNm.equals("상방동")||address.emdNm.equals("신교동")||
                            address.emdNm.equals("옥곡동")||address.emdNm.equals("옥산동")||address.emdNm.equals("용성면")||
                            address.emdNm.equals("임당동")||address.emdNm.equals("자인면")||address.emdNm.equals("조영동")||address.emdNm.equals("진량읍")){
                        result = "6P18-2";
                    }else{
                        result = "6P18-1";
                    }

                }else if(address.sggNm.equals("경주시")){
                    if(address.emdNm.equals("강동면")||address.emdNm.equals("내남면")||address.emdNm.equals("노서동")||address.emdNm.equals("동부동")||
                            address.emdNm.equals("배동")||address.emdNm.equals("북부동")||address.emdNm.equals("서부동")||address.emdNm.equals("성동동")||
                            address.emdNm.equals("용강동")||address.emdNm.equals("율동")||address.emdNm.equals("천북면")||
                            address.emdNm.equals("충효동")||address.emdNm.equals("탑동")||address.emdNm.equals("현곡면")||address.emdNm.equals("황성동")){
                        result = "6A22-2";
                    }else{
                        result ="6A22-1";
                    }

                }else if(address.sggNm.equals("고령군")){
                    result = "5Y90-2";

                }else if(address.sggNm.equals("구미시")){
                    if(address.emdNm.equals("거의동")||address.emdNm.equals("구평동")||address.emdNm.equals("구포동")||address.emdNm.equals("금전동")||
                            address.emdNm.equals("사곡동")||address.emdNm.equals("산동면")||address.emdNm.equals("상모동")||address.emdNm.equals("시미동")||
                            address.emdNm.equals("신동")||address.emdNm.equals("양호동")||address.emdNm.equals("오태동")||
                            address.emdNm.equals("옥계동")||address.emdNm.equals("인의동")||address.emdNm.equals("임수동")||
                            address.emdNm.equals("임은동")||address.emdNm.equals("장천면")||address.emdNm.equals("진평동")||
                            address.emdNm.equals("해평면")||address.emdNm.equals("황상동")){
                        result ="5Z31-2";
                    }else{
                        result = "5Z31-1";
                    }

                }else if(address.sggNm.equals("김천시")){
                    result = "6M13-0";

                }else if(address.sggNm.equals("문경시")){
                    result = "0I02-0";

                }else if(address.sggNm.equals("봉화군")){
                    if(address.emdNm.equals("법천면")||address.emdNm.equals("석포면")||address.emdNm.equals("소천면")||
                            address.emdNm.equals("춘양면")){
                        result = "9K72-1";
                    }else{
                        result = "9K72-0";
                    }

                }else if(address.sggNm.equals("상주시")){
                    result = "9A51-0";

                }else if(address.sggNm.equals("성주군")){
                    result = "9D82-0";

                }else if(address.sggNm.equals("안동시")){
                    result = "6P60-0";

                }else if(address.sggNm.equals("영덕군")){
                    result = "9B47-0";

                }else if(address.sggNm.equals("영양군")){
                    if(address.emdNm.equals("입양면")){
                        result = "9A58-0";
                    }else{
                        result ="0V86-0";
                    }

                }else if(address.sggNm.equals("영주시")){
                    result = "9K71-0";

                }else if(address.sggNm.equals("영천시")){
                    result = "0I83-0";

                }else if(address.sggNm.equals("예천군")){
                    result = "0W07-0";

                }else if(address.sggNm.equals("울릉군")){
                    result = "6M97-2";

                    result+= "\n\n[도선료]";
                    result+= "\n* 북면, 서면, 울릉읍: 5000원";
                }else if(address.sggNm.equals("울진군")){
                    if(address.emdNm.equals("기성면") || address.emdNm.equals("온정면")
                            || address.emdNm.equals("평해읍") || address.emdNm.equals("지천면")){
                        result="9B86-0";
                    }else{
                        result="9K75-0";
                    }
                }else if(address.sggNm.equals("의성군")){
                    result = "0Q57-0";

                }else if(address.sggNm.equals("청도군")){
                    result = "0V84-0";

                }else if(address.sggNm.equals("청송군")){
                    result = "9A58-0";

                }else if(address.sggNm.equals("칠곡군")){
                    if(address.emdNm.equals("가산면")||address.emdNm.equals("동명면")||address.emdNm.equals("석적읍")||
                            address.emdNm.equals("지천면")){
                        result = "6M11-2";
                    }else{
                        result ="6M11-1";
                    }

                }else if(address.sggNm.equals("포항시 남구")){
                    result = "6M97-1";

                }else if(address.sggNm.equals("포항시 북구")){
                    result = "6A26-0";

                }
            }else if(address.ssNm.contains("광주")){
                if(address.sggNm.equals("광산구")){
                    if(address.emdNm.equals("고룡동")||address.emdNm.equals("두정동")){
                        result = "7W34-1";

                    }else if(address.emdNm.equals("광산동")||address.emdNm.equals("동림동")||address.emdNm.equals("사호동")){
                        result = "7W58-3";

                    }else if(address.emdNm.equals("대산동")||address.emdNm.equals("도산동")||address.emdNm.equals("도천동")||
                            address.emdNm.equals("동산동")||address.emdNm.equals("명화동")||address.emdNm.equals("박호동")){
                        result = "7W58-2";

                    }else if(address.emdNm.equals("비아동")||address.emdNm.equals("산막동")||address.emdNm.equals("산월동")){
                        result = "7W34-1";

                    }else if(address.emdNm.equals("산정동")||address.emdNm.equals("삼도동")||address.emdNm.equals("서봉동")||
                            address.emdNm.equals("선암동")||address.emdNm.equals("소촌동")||address.emdNm.equals("송산동")||
                            address.emdNm.equals("송정동")||address.emdNm.equals("송학동")){
                        result = "7W58-2";

                    }else if(address.emdNm.equals("수완동")||address.emdNm.equals("신가동")){
                        result = "7W58-3";

                    }else if(address.emdNm.equals("신동")||address.emdNm.equals("신촌동")){
                        result = "7W58-2";

                    }else if(address.emdNm.equals("신룡동")||address.emdNm.equals("쌍암동")||address.emdNm.equals("안청동")){
                        result = "7W34-1";

                    }else if(address.emdNm.equals("신창동")||address.emdNm.equals("오산동")){
                        result = "7W58-3";

                    }else if(address.emdNm.equals("오선동")||address.emdNm.equals("옥동")||address.emdNm.equals("우산동")||
                            address.emdNm.equals("운남동")||address.emdNm.equals("운수동")){
                        result = "7W58-2";

                    }else if(address.emdNm.equals("월계동")||address.emdNm.equals("임곡동")||address.emdNm.equals("진곡동")){
                        result = "7W34-1";

                    }else if(address.emdNm.equals("월곡동")||address.emdNm.equals("월전동")){
                        result = "7W58-2";

                    }else if(address.emdNm.equals("장덕동")||address.emdNm.equals("장수동")||address.emdNm.equals("흑석동")){
                        result = "7W58-3";

                    }else if(address.emdNm.equals("지정동")||address.emdNm.equals("지평동")||address.emdNm.equals("하남동")){
                        result = "7W58-2";

                    }else{
                        result = "7W58-1";
                    }
                }else if(address.sggNm.equals("남구")){
                    result = "7W42-0";

                }else if(address.sggNm.equals("동구")){
                    result = "7W43-0";

                }else if(address.sggNm.equals("북구")){
                    if(address.emdNm.equals("신안동")||address.emdNm.equals("각화동")){
                        result = "7W30-2";
                    }else if(address.emdNm.equals("금곡동")||address.emdNm.equals("덕의동")||address.emdNm.equals("망월동")){
                        result = "7W33-1";

                    }else if(address.emdNm.equals("누문동")||address.emdNm.equals("매곡동")){
                        result = "7W33-2";

                    }else if(address.emdNm.equals("문흥동")||address.emdNm.equals("생용동")){
                        result = "7W33-1";

                    }else if(address.emdNm.equals("북동")||address.emdNm.equals("삼각동")){
                        result = "7W33-2";

                    }else if(address.emdNm.equals("오룡동")){
                        result = "7W58-2";

                    }else if(address.emdNm.equals("오치동")||address.emdNm.equals("용봉동")||address.emdNm.equals("우산동")){
                        result = "7W33-2";

                    }else if(address.emdNm.equals("운암동")||address.emdNm.equals("풍향동")){
                        result = "7W30-2";

                    }else if(address.emdNm.equals("운정동")||address.emdNm.equals("일곡동")||address.emdNm.equals("장등동")){
                        result = "7W33-1";

                    }else if(address.emdNm.equals("유동")||address.emdNm.equals("임동")||address.emdNm.equals("중흥동")){
                        result = "7W33-2";

                    }else if(address.emdNm.equals("청풍동")||address.emdNm.equals("충효동")||address.emdNm.equals("화암동")){
                        result = "7W33-1";

                    }else{
                        result = "7W30-1";
                    }

                }else if(address.sggNm.equals("서구")){
                    if(address.emdNm.equals("내방동")||address.emdNm.equals("덕흥동")){
                        result = "8Y33-1";

                    }else if(address.emdNm.equals("농성동")){
                        result = "8F62-1";

                    }else if(address.emdNm.equals("동천동")){
                        result = "7W30-2";

                    }else if(address.emdNm.equals("마륵동")||address.emdNm.equals("매월동")||address.emdNm.equals("치평동")){
                        result = "8F62-2";

                    }else if(address.emdNm.equals("상무동")||address.emdNm.equals("쌍촌동")){
                        result = "8Y33-1";

                    }else if(address.emdNm.equals("양동")||address.emdNm.equals("양3동")||address.emdNm.equals("풍암동")){
                        result = "8F62-1";

                    }else if(address.emdNm.equals("유촌동")||address.emdNm.equals("화정동")){
                        result = "8Y33-1";

                    }else{
                        result = "8Y33-2";

                    }
                }
            }else if(address.ssNm.contains("전라남도")){
                if(address.sggNm.equals("강진군")){
                    result = "9J64-1";
                }else if(address.sggNm.equals("고흥군")){
                    if(address.emdNm.equals("금산면")||address.emdNm.equals("도덕면")||address.emdNm.equals("도양읍")){
                        result = "9B17-0";

                    }else if(address.emdNm.equals("대서면")||address.emdNm.equals("동강면")){
                        result = "0U69-2";

                    }else{
                        result = "9B16-0";
                    }

                    if(address.emdNm.equals("도양읍")){
                        result+= "\n\n[도선료]";
                        result+= "\n* 도양읍(득량리): 4000원";
                        result+= "\n* 도양읍(봉양리, 상화도/하화도): 5000원";
                        result+= "\n* 도양읍(시산리): 5000원";
                        result+= "\n* 도화면(지족리), 봉래면(사양리): 5000원";
                    }
                }else if(address.sggNm.equals("곡성군")){
                    if(address.emdNm.equals("겸면")||address.emdNm.equals("오산면")||address.emdNm.equals("옥과면")||
                            address.emdNm.equals("입면")){
                        result = "0W76-1";

                    }else{
                        result = "0W76-2";

                    }

                }else if(address.sggNm.equals("광양시")){
                    result = "7W78-0";

                }else if(address.sggNm.equals("구례군")){
                    result = "0I34-0";

                }else if(address.sggNm.equals("나주시")){
                    result = "9G25-0";

                }else if(address.sggNm.equals("담양군")){
                    result = "9A80-1";

                }else if(address.sggNm.equals("목포시")){
                    if(address.emdNm.equals("대양동")||address.emdNm.equals("부주동")||address.emdNm.equals("부흥동")||
                            address.emdNm.equals("상동")||address.emdNm.equals("석현동")||address.emdNm.equals("신흥동")||
                            address.emdNm.equals("옥암동")||address.emdNm.equals("하당동")){
                        result = "7V13-1";

                    }else{
                        result = "7V13-2";

                    }
                    result+= "\n\n[도선료]";
                    result+= "\n* 달동(외달도, 달리도), 율도동: 6000원";

                }else if(address.sggNm.equals("무안군")){
                    if(address.emdNm.equals("삼향읍")){
                        result = "9A45-1";

                    }else{
                        result = "9A45-2";

                    }

                }else if(address.sggNm.equals("보성군")){
                    if(address.emdNm.equals("벌교읍")){
                        result = "0U69-1";
                        result+= "\n\n[도선료]";
                        result+= "\n* 벌교읍(장도리): 4000원";
                    }else{
                        result = "0Q68-0";

                    }

                }else if(address.sggNm.equals("순천시")){
                    if(address.emdNm.equals("낙안면")){
                        result = "0U69-1";

                    }else{
                        result = "7V45-0";

                    }

                }else if(address.sggNm.equals("신안군")){
                    if(address.emdNm.equals("임자면")||address.emdNm.equals("증도면")||address.emdNm.equals("지도읍")){
                        result = "9A45-0";
                    }else{
                        result = "7V13-3";
                    }

                    result+= "\n\n[도선료]";
                    result+= "\n* 전체: 7000원";
                    result+= "\n단, 압해읍은 가란리, 고이리, 매화리만 해당";
                    result+= "\n단, 증도면은 병풍리만 해당";
                    result+= "\n단, 지도읍은 선도리, 어의리만 해당";

                }else if(address.sggNm.equals("여수시")){
                    if(address.emdNm.equals("낙포동")||address.emdNm.equals("묘도동")||address.emdNm.equals("봉계동")||
                            address.emdNm.equals("상암동")||address.emdNm.equals("선원동")||address.emdNm.equals("소라면")||
                            address.emdNm.equals("소호동")||address.emdNm.equals("시전동")||address.emdNm.equals("신기동")||
                            address.emdNm.equals("신덕동")||address.emdNm.equals("안산동")||address.emdNm.equals("여천동")||
                            address.emdNm.equals("웅천동")||address.emdNm.equals("월내동")||address.emdNm.equals("월하동")||
                            address.emdNm.equals("율촌면")||address.emdNm.equals("적량동")||address.emdNm.equals("주삼동")||
                            address.emdNm.equals("중흥동")||address.emdNm.equals("평여동")||address.emdNm.equals("학동")||
                            address.emdNm.equals("학용동")||address.emdNm.equals("해산동")||address.emdNm.equals("호명동")||
                            address.emdNm.equals("화양면")||address.emdNm.equals("화장동")||address.emdNm.equals("화치동")){
                        result = "7V66-1";
                    }else{
                        result = "7V66-2";
                    }

                    result+= "\n\n[도선료]";
                    result+= "\n* 경호동, 남면, 삼산면, 화정면(백야리 제외): 8000원";

                }else if(address.sggNm.equals("영광군")){
                    if(address.emdNm.equals("법성면")||address.emdNm.equals("홍농읍")){
                        result = "9G27-2";

                    }else{
                        result = "9G27-1";
                    }

                    result+= "\n\n[도선료]";
                    result+= "\n* 낙월면: 4000원";

                }else if(address.sggNm.equals("영암군")){
                    if(address.emdNm.equals("삼호읍")){
                        result = "0V73-1";

                    }else{
                        result = "0V73-2";

                    }

                }else if(address.sggNm.equals("완도군")){
                    if(address.emdNm.equals("고금면")||address.emdNm.equals("금당면")||address.emdNm.equals("금일읍")||
                            address.emdNm.equals("생일면")||address.emdNm.equals("약산면")){
                        result = "9J64-2";
                    }else{
                        result = "0U63-0";
                    }
                    result+= "\n\n[도선료]";
                    result+= "\n* 군외면(당인리, 불목리, 영풍리, 황진리): 5000원";
                    result+= "\n* 금당면, 금일읍: 7000원";
                    result+= "\n* 노화읍, 보길면, 생일면: 5000원";
                    result+= "\n* 소안면, 청산면: 5000원";
                }else if(address.sggNm.equals("장성군")){
                    result = "0Q77-4";

                }else if(address.sggNm.equals("장흥군")){
                    result = "0W64-0";

                }else if(address.sggNm.equals("진도군")){
                    result = "0I65-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 조도면: 7000원";

                }else if(address.sggNm.equals("함평군")){
                    result = "0V67-2";

                }else if(address.sggNm.equals("해남군")){
                    if(address.emdNm.equals("화원면")){
                        result = "7V13-4";

                    }else{
                        result = "9C14-0";

                    }

                }else if(address.sggNm.equals("화순군")){
                    result = "0I62-0";

                }
            }else if(address.ssNm.contains("전라북도")){
                if(address.sggNm.equals("고창군")){
                    result ="9G23-0";

                }else if(address.sggNm.equals("군산시")){
                    result ="5K00-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 옥도면: 4000원";
                }else if(address.sggNm.equals("김제시")){
                    result ="0V26-0";

                }else if(address.sggNm.equals("남원시")){
                    result ="9G29-0";

                }else if(address.sggNm.equals("무주군")){
                    result ="0U51-0";

                }else if(address.sggNm.equals("부안군")){
                    result ="0U19-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 위도면: 5000원";

                }else if(address.sggNm.equals("순창군")){
                    result ="9J78-3";

                }else if(address.sggNm.equals("완주군")){
                    if(address.emdNm.equals("구이면")||address.emdNm.equals("상관면")||address.emdNm.equals("이서면")){
                        result ="5K35-3";

                    }else{
                        result ="9G08-0";

                    }

                }else if(address.sggNm.equals("익산시")){
                    result ="5B73-0";

                }else if(address.sggNm.equals("임실군")){
                    result ="0W79-0";

                }else if(address.sggNm.equals("장수군")){
                    result ="0I43-0";

                }else if(address.sggNm.equals("전주시 덕진구")){
                    result ="5B39-0";

                }else if(address.sggNm.equals("전주시 완산구")){
                    if(address.emdNm.equals("삼천동")||address.emdNm.equals("용복동")||address.emdNm.equals("원당동")||
                            address.emdNm.equals("중인동")||address.emdNm.equals("평화동")||address.emdNm.equals("효자동")){
                        result ="5K35-1";

                    }else{
                        result ="5K35-2";

                    }

                }else if(address.sggNm.equals("정읍시")){
                    result ="9H90-0";

                }else if(address.sggNm.equals("진안군")){
                    result ="0Q45-0";

                }
            }else if(address.ssNm.contains("제주")){
                if(address.sggNm.equals("서귀포시")){
                    result ="8Z28-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 대정읍 가파도, 마라도: 6000원";
                }else if(address.sggNm.equals("제주시")){
                    if(address.emdNm.equals("구좌읍")||address.emdNm.equals("이월읍")){
                        result ="8Z21-0";

                    }else if(address.emdNm.equals("내도동")||address.emdNm.equals("도남동")){
                        result ="8F41-0";

                    }else if(address.emdNm.equals("노형동")||address.emdNm.equals("도평동")||address.emdNm.equals("봉개동")||
                            address.emdNm.equals("아라이동")||address.emdNm.equals("아라일동")||
                            address.emdNm.equals("연동")||address.emdNm.equals("영평동")||address.emdNm.equals("오등동")||
                            address.emdNm.equals("오라삼동")||address.emdNm.equals("오라이동")||
                            address.emdNm.equals("오라일동")){
                        result ="8Z20-0";

                    }else if(address.emdNm.equals("외도이동")||address.emdNm.equals("외도일동")){
                        result ="8F41-0";

                    }else if(address.emdNm.equals("용강동")){
                        result ="8Z20-0";

                    }else if(address.emdNm.equals("우도면")){
                        result ="8Z28-5";

                    }else if(address.emdNm.equals("월평동")){
                        result ="8Z20-0";

                    }else if(address.emdNm.equals("조천읍")){
                        result ="8Z21-0";

                    }else if(address.emdNm.equals("추자면")){
                        result ="7V13-0";

                    }else if(address.emdNm.equals("한경면")||address.emdNm.equals("한림읍")){
                        result ="8Z21-0";

                    }else if(address.emdNm.equals("해안동")||address.emdNm.equals("화천동")){
                        result ="8Z20-0";

                    }else{
                        result ="8F40-0";

                    }
                    result+= "\n\n[도선료]";
                    result+= "\n* 우도면: 6000원 (제주운임 별도)";
                    result+= "\n* 추자면: 7000원";
                }
            }else if(address.ssNm.contains("충청북도")){
                if(address.sggNm.equals("괴산군")){
                    result ="0V24-0";
                }else if(address.sggNm.equals("단양군")){
                    result ="0Q28-0";

                }else if(address.sggNm.equals("보은군")){
                    result ="9J66-0";

                }else if(address.sggNm.equals("영동군")){
                    if(address.emdNm.equals("매곡면")||address.emdNm.equals("상촌면")||
                            address.emdNm.equals("추풍평면")||address.emdNm.equals("황간면")){
                        result ="0I17-0";

                    }else{
                        result ="0I16-0";

                    }

                }else if(address.sggNm.equals("옥천군")){
                    result ="9D80-0";

                }else if(address.sggNm.equals("음성군")){
                    result ="8F84-0";

                }else if(address.sggNm.equals("제천시")){
                    result= "8F31-0";
                }else if(address.sggNm.equals("증평군")){
                    if(address.emdNm.equals("도안면")){
                        result ="9C47-3";

                    }else if(address.emdNm.equals("증평읍")){
                        result ="9C47-2";

                    }

                }else if(address.sggNm.equals("진천군")){
                    result ="9C45-0";

                }else if(address.sggNm.equals("청주시 상당구")){
                    if(address.emdNm.equals("가덕면")){
                        result ="1B40-2";

                    }else if(address.emdNm.equals("남일면")){
                        result ="1H67-0";

                    }else if(address.emdNm.equals("낭성면")||address.emdNm.equals("문의면")||address.emdNm.equals("미원면")){
                        result ="1B40-2";

                    }else{
                        result ="1B40-0";

                    }

                }else if(address.sggNm.equals("청주시 서원구")){
                    if(address.emdNm.equals("사직동")){
                        result ="1B40-3";

                    }else if(address.emdNm.equals("현도면")){
                        result ="4H21-2";

                    }else{
                        result ="1H67-0";

                    }

                }else if(address.sggNm.equals("청주시 청원구")){
                    if(address.emdNm.equals("내수읍")){
                        result ="9C47-1";

                    }else if(address.emdNm.equals("북이면")){
                        result ="9C47-0";

                    }else if(address.emdNm.equals("오창읍")){
                        result ="1B41-0";

                    }else{
                        result ="1B40-1";

                    }

                }else if(address.sggNm.equals("청주시 흥덕구")){
                    if(address.emdNm.equals("강내면")||address.emdNm.equals("오송읍")||address.emdNm.equals("옥산면")){
                        result ="4H21-2";

                    }else if(address.emdNm.equals("사직동")){
                        result ="1B40-3";

                    }else{
                        result ="1H66-2";

                    }

                }else if(address.sggNm.equals("충주시")){
                    result ="5K25-0";

                }
            }else if(address.ssNm.contains("대전")){
                if(address.sggNm.equals("대덕구")){
                    if(address.emdNm.equals("대화동")||address.emdNm.equals("목상동")||address.emdNm.equals("문평동")||
                            address.emdNm.equals("석봉동")||address.emdNm.equals("송천동")||address.emdNm.equals("신탄진동")||
                            address.emdNm.equals("연축동")||address.emdNm.equals("와동")||address.emdNm.equals("중리동")){
                        result = "2C33-2";
                    }else{
                        result = "2C33-1";

                    }

                }else if(address.sggNm.equals("동구")){
                    if(address.emdNm.equals("가양동")||address.emdNm.equals("삼성동")||address.emdNm.equals("성남동")||
                            address.emdNm.equals("신홍동")||address.emdNm.equals("용운동")||address.emdNm.equals("용전동")){
                        result = "2C30-1";
                    }else{
                        result = "2C30-2";
                    }

                }else if(address.sggNm.equals("서구")){
                    if(address.emdNm.equals("갈마동")){
                        result = "2C34-4";

                    }else if(address.emdNm.equals("관저동")){
                        result = "2R10-2";

                    }else if(address.emdNm.equals("괴정동")){
                        result = "2C34-4";

                    }else if(address.emdNm.equals("둔산동")||address.emdNm.equals("만년동")){
                        result = "2R10-1";

                    }else if(address.emdNm.equals("월평동")){
                        result = "2C34-3";

                    }else if(address.emdNm.equals("탄방동")){
                        result = "2R10-1";

                    }else{
                        result = "2R10-3";

                    }

                }else if(address.sggNm.equals("유성구")){
                    if(address.emdNm.equals("가정동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("관평동")||address.emdNm.equals("구룡동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("구성동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("구암동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("궁동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("금고동")||address.emdNm.equals("대동")||address.emdNm.equals("덕진동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("도룡동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("둔곡동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("문치동")||address.emdNm.equals("방현동")||address.emdNm.equals("봉명동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("봉산동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("상대동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("송강동")||address.emdNm.equals("신동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("신성동")||address.emdNm.equals("어은동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("용산동")){
                        result = "2N20-4";

                    }else if(address.emdNm.equals("원촌동")||address.emdNm.equals("장대동")||address.emdNm.equals("장동")){
                        result = "2N20-1";

                    }else if(address.emdNm.equals("전민동")||address.emdNm.equals("화암동")){
                        result = "2N20-3";

                    }else if(address.emdNm.equals("탑립동")){
                        result = "2N20-4";

                    }else{
                        result = "2N20-2";

                    }

                }else if(address.sggNm.equals("중구")){
                    if(address.emdNm.equals("목동")||address.emdNm.equals("문화동")||address.emdNm.equals("부사동")||
                            address.emdNm.equals("사정동")||address.emdNm.equals("산성동")||address.emdNm.equals("석교동")||
                            address.emdNm.equals("안영동")||address.emdNm.equals("오류동")||address.emdNm.equals("옥계동")||
                            address.emdNm.equals("용두동")||address.emdNm.equals("중촌동")||address.emdNm.equals("호동")){
                        result = "2C35-1";

                    }else{
                        result = "2C35-2";
                    }

                }
            }else if(address.ssNm.contains("울산")){
                if(address.sggNm.equals("남구")){
                    if(address.emdNm.equals("달동")||address.emdNm.equals("대현동")||address.emdNm.equals("무거동")||
                            address.emdNm.equals("목동")||address.emdNm.equals("야음동")||address.emdNm.equals("수암동")){
                        result = "5Z55-1";
                    }else{
                        result = "5B13-0";
                    }
                }else if(address.sggNm.equals("동구")) {
                    result = "8E60-0";

                }else if(address.sggNm.equals("북구")) {
                    result = "5Z56-1";

                }else if(address.sggNm.equals("울주군")){
                    if(address.emdNm.equals("서생면")||address.emdNm.equals("온산읍")||
                            address.emdNm.equals("온양읍")||address.emdNm.equals("청량면")){
                        result = "5Z56-2";
                    }else{
                        result = "8F75-0";
                    }

                }else if(address.sggNm.equals("중구")){
                    result = "5B12-0";
                }
                //여기까지
            }else if(address.ssNm.contains("부산")){
                if(address.sggNm.equals("강서구")){
                    result ="6J35-2";
                }else if(address.sggNm.equals("금정구")){
                    result ="6M88-0";

                }else if(address.sggNm.equals("기장군")){
                    result ="6A34-0";

                }else if(address.sggNm.equals("남구")){
                    if(address.emdNm.equals("대연동")){
                        result ="6J05-1";

                    }else{
                        result ="6J05-2";

                    }

                }else if(address.sggNm.equals("동구")){
                    result ="8F37-0";

                }else if(address.sggNm.equals("동래구")){
                    result ="6A28-0";

                }else if(address.sggNm.equals("부산진구")){
                    if(address.emdNm.equals("가야동")||address.emdNm.equals("개금동")){
                        result ="5Z50-3";

                    }else if(address.emdNm.equals("당감동")||address.emdNm.equals("범전동")||address.emdNm.equals("부전동")){
                        result ="5Z50-2";

                    }else if(address.emdNm.equals("범천동")){
                        result ="8F37-1";

                    }else if(address.emdNm.equals("부암동")||address.emdNm.equals("연지동")||address.emdNm.equals("초읍동")){
                        result ="5Z50-0";

                    }else if(address.emdNm.equals("양정동")||address.emdNm.equals("전포동")){
                        result ="5Z50-1";

                    }

                }else if(address.sggNm.equals("북구")){
                    if(address.emdNm.equals("구포동")){
                        result ="6P29-1";

                    }else{
                        result ="6P29-2";

                    }

                }else if(address.sggNm.equals("사상구")){
                    result ="6J37-0";

                }else if(address.sggNm.equals("사하구")){
                    result ="6J39-0";

                }else if(address.sggNm.equals("서구")){
                    result ="5B22-0";

                }else if(address.sggNm.equals("수영구")){
                    result ="5B21-0";

                }else if(address.sggNm.equals("연제구")){
                    if(address.emdNm.equals("거제동")){
                        result ="5B20-2";

                    }else if(address.emdNm.equals("연산동")){
                        result ="5B20-1";

                    }

                }else if(address.sggNm.equals("영도구")){
                    result ="6P32-1";

                }else if(address.sggNm.equals("중구")){
                    result ="6P32-2";

                }else if(address.sggNm.equals("해운대구")){
                    if(address.emdNm.equals("송정동")||address.emdNm.equals("우동")||address.emdNm.equals("좌동")||address.emdNm.equals("중동")){
                        result ="6A31-0";

                    }else{
                        result ="6A37-0";

                    }

                }
            }else if(address.ssNm.contains("경상남도")){
                if(address.sggNm.equals("거제시")){
                    if(address.emdNm.equals("거제면")||address.emdNm.equals("고현동")||address.emdNm.equals("남부면")||
                            address.emdNm.equals("동부면")||address.emdNm.equals("둔덕면")||
                            address.emdNm.equals("문동동")||address.emdNm.equals("사등면")||address.emdNm.equals("삼거동")||
                            address.emdNm.equals("상동동")||address.emdNm.equals("수월동")||address.emdNm.equals("양정동")||address.emdNm.equals("장평동")){
                        result = "8E76-0";
                    }else{
                        result = "6P16-0";

                    }
                    if(address.emdNm.equals("둔덕면")){
                        result+= "\n\n[도선료]";
                        result+= "\n* 둔덕면(화도리): 4000원";
                    }
                }else if(address.sggNm.equals("거창군")){
                    result = "9C49-0";

                }else if(address.sggNm.equals("고성군")){
                    if(address.emdNm.equals("하이면")){
                        result = "8E10-0";

                    }else{
                        result = "9A49-0";

                    }

                }else if(address.sggNm.equals("김해시")){
                    if(address.emdNm.equals("관동동")||address.emdNm.equals("내덕동")){
                        result = "6J28-2";

                    }else if(address.emdNm.equals("내동")||address.emdNm.equals("외동")||address.emdNm.equals("진례면")||address.emdNm.equals("한림면")){
                        result = "6J28-1";

                    }else if(address.emdNm.equals("대청동")||address.emdNm.equals("무계동")||address.emdNm.equals("부곡동")||address.emdNm.equals("삼문동")||
                            address.emdNm.equals("수가동")||address.emdNm.equals("신문동")||address.emdNm.equals("유하동")||
                            address.emdNm.equals("율하동")||address.emdNm.equals("응달동")||address.emdNm.equals("장유동")||address.emdNm.equals("장유면")){
                        result = "6J28-2";

                    }else if(address.emdNm.equals("진영읍")){
                        result = "6J28-3";

                    }else{
                        result = "6P21-1";

                    }

                }else if(address.sggNm.equals("남해군")){
                    result = "9G04-0";

                }else if(address.sggNm.equals("밀양시")){
                    result = "9K68-0";

                }else if(address.sggNm.equals("사천시")){
                    result = "8E10-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 녹도동 신도, 마도동, 신수동: 3000원";

                }else if(address.sggNm.equals("산청군")){
                    result = "0U50-0";

                }else if(address.sggNm.equals("양산시")){
                    if(address.emdNm.equals("덕계동")||address.emdNm.equals("매곡동")||address.emdNm.equals("명동")||address.emdNm.equals("삼호동")||
                            address.emdNm.equals("서창동")||address.emdNm.equals("소주동")||address.emdNm.equals("용당동")||
                            address.emdNm.equals("주남동")||address.emdNm.equals("주진동")||address.emdNm.equals("평산동")){
                        result = "8Z65-0";

                    }else{
                        result = "6M53-0";

                    }

                }else if(address.sggNm.equals("의령군")){
                    result = "9C82-0";

                }else if(address.sggNm.equals("진주시")){
                    if(address.emdNm.equals("가좌동")||address.emdNm.equals("계동")||address.emdNm.equals("동성동")||address.emdNm.equals("망경동")||
                            address.emdNm.equals("명석면")||address.emdNm.equals("본성동")||address.emdNm.equals("봉곡동")||address.emdNm.equals("상봉동")||
                            address.emdNm.equals("상봉동동")||address.emdNm.equals("상봉서동")||address.emdNm.equals("상평동")||
                            address.emdNm.equals("장대동")||address.emdNm.equals("중안동")||address.emdNm.equals("집현면")||address.emdNm.equals("칠암동")||
                            address.emdNm.equals("평거동")||address.emdNm.equals("평안동")||address.emdNm.equals("하대동")||address.emdNm.equals("호탄동")){
                        result = "5Z40-2";

                    }else{
                        result = "5Z40-1";

                    }

                }else if(address.sggNm.equals("창원시 마산합포")){
                    result = "6J33-2";

                }else if(address.sggNm.contains("창원시 마산회")){
                    if(address.emdNm.equals("구암동")||address.emdNm.equals("봉암동")||address.emdNm.equals("합성동")){
                        result = "6J43-0";
                    }else{
                        result = "6P05-0";
                    }

                }else if(address.sggNm.equals("창녕군")){
                    result = "0Q56-0";

                }else if(address.sggNm.equals("창원시 성산구")){
                    if(address.emdNm.equals("가음동")||address.emdNm.equals("가음정동")||address.emdNm.equals("남산동")||
                            address.emdNm.equals("남양동")||address.emdNm.equals("대방동")||address.emdNm.equals("불모산동")||
                            address.emdNm.equals("사파동")||address.emdNm.equals("사파정동")||address.emdNm.equals("삼정자동")||
                            address.emdNm.equals("상남동")||address.emdNm.equals("성주동")||address.emdNm.equals("토월동")){
                        result = "5Y85-1";
                    }else{
                        result = "6J43-0";
                    }

                }else if(address.sggNm.equals("창원시 의창구")){
                    if(address.emdNm.equals("대산면")||address.emdNm.equals("동읍")||address.emdNm.equals("북면")){
                        result = "0U89-0";

                    }else{
                        result = "6J42-0";

                    }

                }else if(address.sggNm.equals("창원시 진해구")){
                    result = "5Y85-2";

                }else if(address.sggNm.equals("통영시")){
                    result = "8Z24-0";
                    result+= "\n\n[도선료]";
                    result+= "\n* 사랑면, 산양읍(곤리, 연곡리, 저림리, 추도리), 욕지면, 용남면(어의리, 지도리), 한산면: 4000원";
                }else if(address.sggNm.equals("하동군")){
                    result = "0V49-0";

                }else if(address.sggNm.equals("함안군")){
                    result = "0V81-0";

                }else if(address.sggNm.equals("함양군")){
                    result = "0W05-0";

                }else if(address.sggNm.equals("합천군")){
                    result = "0U55-0";

                }
            }else if(address.ssNm.contains("대구")){
                if(address.sggNm.equals("남구")){
                    result = "5Y14-1";

                }else if(address.sggNm.equals("달서구")){
                    if(address.emdNm.equals("갈산동")||address.emdNm.equals("용산동")){
                        result = "5Y17-0";

                    }else if(address.emdNm.equals("대곡동")||address.emdNm.equals("상인동")||address.emdNm.equals("송현동")||
                            address.emdNm.equals("월성동")||address.emdNm.equals("월암동")||address.emdNm.equals("유천동")||
                            address.emdNm.equals("이곡동")||address.emdNm.equals("진천동")){
                        result = "5Y24-2";

                    }else if(address.emdNm.equals("장기동")||address.emdNm.equals("장동")){
                        result = "5Y17-0";

                    }else{
                        result = "5Y24-1";

                    }

                }else if(address.sggNm.equals("달성군")){
                    result = "5Y90-0";

                }else if(address.sggNm.equals("동구")){
                    if(address.emdNm.equals("괴전동")||address.emdNm.equals("금강동")||address.emdNm.equals("매여동")||
                            address.emdNm.equals("방촌동")||address.emdNm.equals("상매동")||address.emdNm.equals("신서동")){
                        result = "5Y11-0";

                    }else if(address.emdNm.equals("신암동")||address.emdNm.equals("신천동")||address.emdNm.equals("효목동")){
                        result = "6M75-1";

                    }else if(address.emdNm.equals("신평동")||address.emdNm.equals("용계동")||address.emdNm.equals("율암동")){
                        result = "5Y11-0";

                    }else{
                        result = "6M75-2";

                    }

                }else if(address.sggNm.equals("북구")){
                    if(address.emdNm.equals("고성동")||address.emdNm.equals("노원동")||address.emdNm.equals("대현동")){
                        result = "5Y44-1";

                    }else if(address.emdNm.equals("검단동")||address.emdNm.equals("동변동")||address.emdNm.equals("북현동")){
                        result = "5Y44-2";

                    }else if(address.emdNm.equals("산격동")||address.emdNm.equals("칠성동")){
                        result = "5Y44-1";

                    }else if(address.emdNm.equals("서변동")||address.emdNm.equals("연경동")||
                            address.emdNm.equals("조야동")||address.emdNm.equals("침산동")){
                        result = "5Y44-2";

                    }else{
                        result = "5Z10-1";

                    }

                }else if(address.sggNm.equals("서구")){
                    if(address.emdNm.equals("비산동")||address.emdNm.equals("편리동")){
                        result = "5Y15-3";

                    }else{
                        result = "5Y15-1";

                    }

                }else if(address.sggNm.equals("수성구")){
                    if(address.emdNm.equals("노변동")||address.emdNm.equals("매호동")){
                        result = "5Y11-1";

                    }else if(address.emdNm.equals("두산동")||address.emdNm.equals("범물동")){
                        result = "5Y11-3";

                    }else if(address.emdNm.equals("만촌동")||address.emdNm.equals("범어동")){
                        result = "5Y11-2";

                    }else if(address.emdNm.equals("상동")){
                        result = "5Y11-3";

                    }else if(address.emdNm.equals("수성동1가")||address.emdNm.equals("수성동2가")){
                        result = "5Y11-1";

                    }else if(address.emdNm.equals("수성동3가")||address.emdNm.equals("수성동4가")){
                        result = "5Y11-2";

                    }else if (address.emdNm.equals("중동") || address.emdNm.equals("지산동") || address.emdNm.equals("황금2동")) {
                        result = "5Y11-1";

                    }else if(address.emdNm.equals("파동")||address.emdNm.equals("황금동")||address.emdNm.equals("황금1동")){
                        result = "5Y11-3";
                    }
                    else{
                        result = "5Y11-0";
                    }

                }else if(address.sggNm.equals("중구")){
                    result="5Z10-0";
                }
            }


        }


        return result;
    }

    private void init(){
        result=null;
        context= null;
        inflater= null;
        fragmentManager= null;
        entireAddress= "";
        siNm= "";
        sggNm= "";
        emdNm= "";
        liNm= "";
    }

    @Override
    public void run() {
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xml = documentBuilder.parse(apiUrl);

            final Element root = xml.getDocumentElement();

            // Request selecting address
            final DialogMaker dialog= new DialogMaker();
            View addressListView= inflater.inflate(R.layout.select_address, null);
            ListView addressList= (ListView)addressListView.findViewById(R.id.addressList);

            // Case: invalid Address
            if(root.getElementsByTagName("jibunAddr").getLength()== 0){
                final DialogMaker failToLoad= new DialogMaker();
                DialogMaker.Callback closeDialog=new DialogMaker.Callback() {
                    @Override
                    public void callbackMethod() {
                        failToLoad.dismiss();
                    }
                };
                failToLoad.setValue("잘못된 주소입니다.\n다시 시도하세요.", "확인", null, closeDialog, null);
                failToLoad.show(fragmentManager, "");
                disConnection();
                return;
            }
            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
            for(int i=0; i<root.getElementsByTagName("jibunAddr").getLength(); i++){
                arrayAdapter.add(root.getElementsByTagName("jibunAddr").item(i).getTextContent());
            }
            addressList.setAdapter(arrayAdapter);
            addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    entireAddress = root.getElementsByTagName("jibunAddr").item(i).getTextContent();
                    siNm= root.getElementsByTagName("siNm").item(i).getTextContent();
                    sggNm= root.getElementsByTagName("sggNm").item(i).getTextContent();
                    emdNm= root.getElementsByTagName("emdNm").item(i).getTextContent();
                    if(root.getElementsByTagName("liNm")!=null){
                        liNm= root.getElementsByTagName("liNm").item(i).getTextContent();
                        result= new AddressSaver(entireAddress, siNm, sggNm, emdNm, liNm);
                    }else{
                        result= new AddressSaver(entireAddress, siNm, sggNm, emdNm);
                    }

                    showResultDialog(result);
                }
            });
            dialog.setValue("주소 선택", "", "", null, null, addressListView);
            dialog.show(fragmentManager, "");

        }catch (Exception e){
            Log.i("Connection Error", e.toString());
        }
    }

    private void showResultDialog(AddressSaver addressData){
        disConnection();



        String resultCode= AddressConverter.getInstance().convertToCode(addressData);

        final DialogMaker resultDialog=new DialogMaker();
        DialogMaker.Callback closeDialog=new DialogMaker.Callback() {
            @Override
            public void callbackMethod() {
                resultDialog.dismiss();
            }
        };

        View resultLayout= inflater.inflate(R.layout.adress_info, null);
        TextView originAddress= (TextView)resultLayout.findViewById(R.id.entireAddress);
        TextView convertedCode= (TextView)resultLayout.findViewById(R.id.convertResult);

        originAddress.setText("지번 주소: "+ addressData.entireAddress);
        convertedCode.setText("지역 코드: "+ convertToCode(addressData));

        resultDialog.setValue("결과", "닫기", null, closeDialog, null, resultLayout);
        resultDialog.show(fragmentManager, "");


    }

    private Thread thread=null;
    private void startConnection() throws InterruptedException {
        //Connect to Network in thread
        Thread thread= new Thread(this);
        thread.start();
        thread.join();
    }
    private void disConnection(){
        if(thread!=null){
            thread.stop();
            thread= null;
        }
    }
}
