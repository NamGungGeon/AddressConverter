package com.example.windows10.addressconverter;

/**
 * Created by Windows10 on 2017-09-14.
 */

public class AddressSaver {
    //Entire Address
    public String entireAddress="";
    // ex: 서울시
    public String ssNm="";
    // ex: 중랑구
    public String sggNm="";
    // ex: 면목동
    public String emdNm="";
    // ex: 난지도리
    public String liNm="";

    public AddressSaver(String entireAddress, String ssNm, String sggNm, String emdNm){
        this.entireAddress= entireAddress;
        this.ssNm= ssNm;
        this.sggNm= sggNm;
        this.emdNm= emdNm;
    }

    public AddressSaver(String entireAddress, String ssNm, String sggNm, String emdNm, String liNm){
        this.entireAddress= entireAddress;
        this.ssNm= ssNm;
        this.sggNm= sggNm;
        this.emdNm= emdNm;
        this.liNm= liNm;
    }
}
