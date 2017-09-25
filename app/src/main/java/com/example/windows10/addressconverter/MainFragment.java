package com.example.windows10.addressconverter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Windows10 on 2017-09-14.
 */

public class MainFragment extends Fragment {

    private EditText inputAddress=null;
    private Button convertBtn=null;

    private RadioButton jibeonCheck=null;
    private RadioButton loadNameCheck=null;

    private FrameLayout jibeonInputWrapper=null;
    private FrameLayout loadNameWrapper=null;

    private Spinner sido= null;
    private ArrayAdapter<String> sidoAdapter= null;
    private Spinner gugun= null;
    private ArrayAdapter<String> gugunAdapter= null;
    private Spinner dong= null;
    private ArrayAdapter<String> dongAdapter= null;

    private EditText dongSelfInput= null;

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.jibeonCheck:
                case R.id.loadNameCheck:
                    updateInputType();
                    break;
                case R.id.convertToCode:
                    String jibeonJuso="";
                    String code="";

                    AddressSaver addressWrapper=null;
                    if(jibeonCheck.isChecked() && !loadNameCheck.isChecked()){
                        // When Input address is "지번주소"
                        String _sido= null;
                        String _gugun= null;
                        String _dong= null;
                        try{
                            _sido= (String)sido.getSelectedItem();
                            _gugun= (String)gugun.getSelectedItem();
                            _dong= (String)dong.getSelectedItem();
                        }catch (NullPointerException e){
                            Toast.makeText(getActivity().getApplicationContext(), "아직 모든 항목을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(_sido== null || _gugun== null || _dong== null){
                            Toast.makeText(getActivity().getApplicationContext(), "아직 모든 항목을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if(_dong.equals("직접 입력")){
                            if (dongSelfInput.getText().toString().equals("")) {
                                Toast.makeText(getActivity().getApplicationContext(), "동/읍/면을 직접 입력하세요", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            addressWrapper= new AddressSaver(_sido+_gugun+dongSelfInput.getText().toString() ,_sido, _gugun, dongSelfInput.getText().toString());
                            jibeonJuso= _sido+" "+_gugun+" "+dongSelfInput.getText().toString();
                        }else{
                            addressWrapper= new AddressSaver(_sido+_gugun+_dong ,_sido, _gugun, _dong);
                            jibeonJuso= _sido+" "+_gugun+" "+_dong;
                        }
                    }else{
                        String convertedAddress=null;
                        // When Input address is "도로명주소"
                        try {
                            addressWrapper=AddressConverter.getInstance().convertToJibeonjuso(inputAddress.getText().toString());
                            if(addressWrapper==null){
                                final DialogMaker failToLoad= new DialogMaker();
                                DialogMaker.Callback closeDialog=new DialogMaker.Callback() {
                                    @Override
                                    public void callbackMethod() {
                                        failToLoad.dismiss();
                                    }
                                };
                                failToLoad.setValue("잘못된 주소입니다.\n다시 시도하세요.", "확인", null, closeDialog, null);
                                failToLoad.show(getActivity().getSupportFragmentManager(), "");
                                break;
                            }else{
                                convertedAddress= addressWrapper.entireAddress;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            final DialogMaker failToLoad= new DialogMaker();
                            DialogMaker.Callback closeDialog=new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    failToLoad.dismiss();
                                }
                            };
                            failToLoad.setValue("주소를 불러오는 도중 실패하였습니다.\n다시 시도하세요.", "확인", null, closeDialog, null);
                            failToLoad.show(getActivity().getSupportFragmentManager(), "");
                            break;
                        }

                        jibeonJuso= convertedAddress;
                    }

                    String resultCode= AddressConverter.getInstance().convertToCode(addressWrapper);
                    code= resultCode;

                    final DialogMaker result=new DialogMaker();
                    DialogMaker.Callback closeDialog=new DialogMaker.Callback() {
                        @Override
                        public void callbackMethod() {
                            result.dismiss();
                        }
                    };

                    View resultLayout= getActivity().getLayoutInflater().inflate(R.layout.adress_info, null);
                    TextView originAddress= (TextView)resultLayout.findViewById(R.id.entireAddress);
                    TextView convertedCode= (TextView)resultLayout.findViewById(R.id.convertResult);

                    originAddress.setText("지번 주소: "+jibeonJuso);
                    convertedCode.setText("지역 코드: "+code);

                    result.setValue("결과", "닫기", null, closeDialog, null, resultLayout);
                    result.show(getActivity().getSupportFragmentManager(), "");

                    break;

            }
        }
    };
    private AdapterView.OnItemSelectedListener sidoListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            // Initiating...
            dongSelfInput.setVisibility(View.GONE);
            gugun.setSelected(false);
            gugun.setAdapter(null);
            dong.setSelected(false);
            dong.setAdapter(null);

            // 선택된 시/도에 따라서 구/군의 Adapter를 변경
            String selectedSido= (String)adapterView.getItemAtPosition(i);
            if(selectedSido!=null){
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item);
                if(selectedSido.contains("서울")){
                    String elements[]= {"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구",
                    "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구",
                    "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("경기")){
                    String elements[]= {"가평군", "고양시 덕양구", "고양시 일산동구", "고양시 일산서구", "과천시", "광명시"
                    , "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시 오정구", "부천시 원미구"
                    , "성남시 분당구", "성남시 수정구", "성남시 중원구", "수원시 권선구", "수원시 영통구", "수원시 장안구"
                    , "수원시 팔달구", "시흥시", "안산시 단원구", "안산시 상륙구", "안성시", "안양시 동안구", "안양시 만안구"
                    , "양주시", "양평군", "여주시", "연천구", "오산시", "용인시 기흥구", "용인시 수지구", "용인시 처인구"
                    , "의왕시", "이천시", "파주시", "평택시", "포천시", "하남시", "화성시"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("강원")){
                    String elements[]= {"강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군", "양양군", "영월군"
                    , "원주시", "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군", "홍천군", "화천군", "횡성군"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("충청남도")){
                    String elements[]= {"계룡시", "공주시", "금산군", "논산시", "당진시", "보령시", "부여군", "서산시"
                    , "서천군", "아산시", "예산군", "천안시 동남구", "천안시 서북구", "청양군", "태안군", "홍성군"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("세종")){
                    String elements[]= {""};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("충청북도")){
                    String elements[]= {"괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "제천시"
                    , "증평군", "진천군", "청주시 상당구", "청주시 서원구", "청주시 청원구", "청주시 흥덕구"
                    , "충주시"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("경상북도")){
                    String elements[]= {"군위군", "경산시", "경주시", "고령군", "구미시", "김천시", "문경시", "봉화군"
                    , "상주시", "성주군", "안동시", "영덕군", "영양군", "영주시", "영천시", "예천군", "울릉군", "울진군"
                    , "의성군", "청도군", "청송군", "칠곡군", "포항시 남구", "포항시 북구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("광주")){
                    String elements[]= {"광산구", "남구", "동구", "북구", "서구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("전라남도")){
                    String elements[]= {"강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시"
                    , "담양군", "목포시", "무안군", "보성군", "순천시", "신안군", "여수시", "영광군"
                    , "완도군", "장성군", "장흥군", "진도군", "해남군", "화순군"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("전라북도")){
                    String elements[]= {"고창군", "군산시", "김제시", "남원시", "무주군", "부안군"
                    , "순창군", "완주군", "익산시", "익산시", "임실군", "장수군", "전주시 덕진구"
                    , "전주시 완산구", "정읍시", "진안군"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if (selectedSido.contains("대전")) {
                    String elements[]= {"대덕구", "동구", "서구", "유성구", "중구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if (selectedSido.contains("울산")) {
                    String elements[]= {"남구", "동구", "북구", "을주군", "중구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if (selectedSido.contains("부산")) {
                    String elements[]= {"강서구", "금정구", "남구", "동구", "동래구"
                    , "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "기장군"
                    , "영도구", "중구", "해운대구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if (selectedSido.contains("경상남도")) {
                    String elements[]= {"거제시", "거창군", "고성군", "김해시", "남해군", "밀양시"
                    , "사천시", "산청군", "양산시", "의령군", "진주시", "창원시 마산합포", "창원시 마산회"
                    , "창녕군", "창원시 성산구", "창원시 의창구", "창원시 진해구", "동영시", "하동군"
                    , "함안군", "함양군", "함천군"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if (selectedSido.contains("대구")) {
                    String elements[]= {"남구", "달서구", "달성군", "동구", "북구", "서구", "수성구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("인천")) {
                    String elements[]= {"강화군", "계양구", "남구", "남동구", "동구", "부평구", "서구", "연수구", "웅진군"
                    , "중구"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }else if(selectedSido.contains("제주")){
                    String elements[]= {"서귀포시", "제주시"};
                    adapter.addAll(elements);
                    gugun.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener gugunListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            // Initiating...
            dongSelfInput.setVisibility(View.GONE);
            dong.setSelected(false);
            dong.setAdapter(null);
            // 선택된 구/군에 따라서 동/읍/면 Adapter를 변경경
            String selectedGugun= (String)adapterView.getItemAtPosition(i);
            if(selectedGugun!=null){
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item);
                if(((String)(sido.getSelectedItem())).contains("서울")){
                    if(selectedGugun.equals("강남구")){
                        String elements[]= {"개포동", "논현동", "대치동", "도곡동", "삼성동", "신사동", "압구정동", "역삼동", "청담동",
                                "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("강동구")){
                        String elements[]= {"고덕동", "성내동", "길동", "암사동", "천호동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("강북구")){
                        String elements[]= {"번동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("강서구")){
                        String elements[]= {"가양동", "등촌동", "내발산동", "마곡동", "외발산동", "우창산동",
                                "화곡1동", "화곡2동", "화곡4동", "화곡8동", "화곡본동", "화곡3동", "화곡6동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("관악구")){
                        String elements[]= {"난곡동", "미성동", "난향동", "대학동", "삼성동", "보라매동", "성현동",
                                "봉천동", "서림동", "서원동", "신림동", "신사동", "조원동", "신원동", "온천동", "중앙동", "청룡동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("광진구")){
                        String elements[]= {"구의동", "자양동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("구로구")){
                        String elements[]= {"가리봉동", "개봉동", "고척동", "구로1동", "구로2동", "구로4동", "구로5동",
                                "구로3동", "구로동", "천왕동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("금천구")){
                        String elements[]= {"가산동", "독산동", "시흥동"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("노원구")){
                        String elements[]= {"상계동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("도봉구")){
                        String elements[]= {"창동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("동대문구")){
                        String elements[]= {"답십리동", "신설동", "이문동", "장안동", "전농동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("동작구")){
                        String elements[]= {"노량진동", "대방동", "사당동",  "동작동", "본동", "상도동", "신대방동", "흑석동"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("마포구")){
                        String elements[]= {"망원동", "상암동", "성산동", "연남동", "중동", "합정동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("서대문구")){
                        String elements[]= {"남가좌동", "냉천동", "북과좌동", "영천동", "옥천동", "천영동", "현저동", "홍은동", "홍제동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("서초구")){
                        String elements[]= {"반포동", "반포본동", "방배동", "방배본동", "서초동", "잠원동"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("성동구")){
                        String elements[]= {"금호동1가", "금호동2가", "금호동3가", "금호동4가", "도선동", "마장동", "사근동",
                                "상왕십리동", "왕십리2동", "하왕십리동", "행당동", "홍익동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("성북구")){
                        String elements[]= {"돈암동", "성북동", "정릉동", "석관동", "장위동", "종압동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("송파구")){
                        String elements[]= {"가락동", "가락본동", "거여동", "문정동", "삼천동", "석촌동", "시천동",
                                "잠실동", "잠실본동", "풍납동", "장지동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("양천구")){
                        String elements[]= {"목동", "신월동", "신청1동", "신청2동", "신청4동", "신청6동", "신청7동", "신청동",
                                "신청3동"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("영등포구")){
                        String elements[]= {"대림동", "도림동", "신길동", "영등포동1가", "영등포동2가", "영등포동3가",
                                "영등포동4가", "영등포동5가", "영등포동6가", "영등포동7가", "영등포동8가",
                                "영등포본동1가", "영등포본동2가", "영등포본동3가", "영등포본동4가", "영등포본동5가",
                                "영등포본동6가", "영등포본동7가", "영등포본동8가", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("용산구")){
                        String elements[]= {"동빙고동", "서빙고동", "보광동", "용산동1가", "용산동3가", "용산동5가", "용산동6가"
                                , "용산동2가", "용산동4가", "이태원동", "이촌1동", "이촌동", "동부이촌동", "이촌2동", "서부이촌동", "주성동"
                                , "한강로1가", "한강로2가", "한강로3가", "한남동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("은평구")){
                        String elements[]= {"불광동", "신사동", "역촌동", "증산동", "진관동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("종로구")){
                        String elements[]= {"가희동", "견지동", "계동", "공평동", "관수동", "관철동", "동숭동", "숭인동", "이화동"
                                , "명륜1가", "명륜2가", "명륜3가", "명륜4가", "묘동", "봉익동", "송현동", "수송동", "안국동", "운니동"
                                , "원남동", "원서동", "인사동", "안의동", "장사동", "재동", "종로1가", "종로2가", "종로3가", "종로4가"
                                , "종로5가", "중학동", "종로6가", "창신동", "청진동", "혜화동", "훈정동", "충신동", "효제동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("중구")){
                        String elements[]= {"광희동1가", "광희동2가", "남학동", "다산동", "동화동", "목정동", "무학동", "방산동", "수표동"
                                , "신당5동", "신당동", "을지로3가", "쌍림동", "약수동", "예관동", "오장동", "을지로5가", "을지로6가", "을지로7가"
                                , "인현동1가", "인현동2가", "주교동", "장충동1가", "장충동2가", "주자동", "청구동", "초동", "충무로3가"
                                , "충무로4가", "충무로5가", "필동1가", "필동2가", "필동3가", "황학동", "홍인동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }else if(selectedGugun.equals("중랑구")){
                        String elements[]= {"묵동", "상봉동", "신내동", "나머지지역"};
                        adapter.addAll(elements);
                        dong.setAdapter(adapter);
                    }
                }else{
                    String elements[]= {"직접 입력"};
                    adapter.addAll(elements);
                    dong.setAdapter(adapter);

                    dongSelfInput.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener dongListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.main_page, null);
        init(rootView);
        return rootView;
    }

    private void init(ViewGroup rootView){
        inputAddress=(EditText)rootView.findViewById(R.id.inputAddress);
        convertBtn=(Button)rootView.findViewById(R.id.convertToCode);
        convertBtn.setOnClickListener(onClickListener);

        jibeonCheck= (RadioButton)rootView.findViewById(R.id.jibeonCheck);
        jibeonCheck.setOnClickListener(onClickListener);
        loadNameCheck= (RadioButton)rootView.findViewById(R.id.loadNameCheck);
        loadNameCheck.setOnClickListener(onClickListener);

        jibeonInputWrapper= (FrameLayout)rootView.findViewById(R.id.jibeonWrapper);
        loadNameWrapper= (FrameLayout)rootView.findViewById(R.id.loadNameWrapper);

        sido= (Spinner)rootView.findViewById(R.id.sido);
        gugun= (Spinner)rootView.findViewById(R.id.gugun);
        dong= (Spinner)rootView.findViewById(R.id.dong);

        // Setting sido
        sidoAdapter= new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item);
        String sidoElements[]= {"서울시", "경기도", "강원도", "충청남도", "세종시", "충청북도", "경상북도", "광주시", "전라남도", "전라북도", "대전시", "울산시", "부산시", "경상남도", "대구시", "인천시", "제주시"};
        sidoAdapter.addAll(sidoElements);
        sido.setAdapter(sidoAdapter);
        sido.setOnItemSelectedListener(sidoListener);

        // Setting gugun
        //gugun.setOnClickListener(onClickListener);
        gugun.setOnItemSelectedListener(gugunListener);

        // Setting dong
        //dong.setOnClickListener(onClickListener);
        dong.setOnItemSelectedListener(dongListener);

        dongSelfInput= (EditText)rootView.findViewById(R.id.dongSelfInput);

        updateInputType();


    }

    private void updateInputType(){
        if(jibeonCheck.isChecked()){
            jibeonInputWrapper.setVisibility(View.VISIBLE);
            loadNameWrapper.setVisibility(View.GONE);
        }else if(loadNameCheck.isChecked()){
            jibeonInputWrapper.setVisibility(View.GONE);
            loadNameWrapper.setVisibility(View.VISIBLE);
        }
    }

}
