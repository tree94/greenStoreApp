package com.seoul.greenstore.greenstore.Review;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.MainActivity;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Server.Server;

/**
 * Created by X on 2016-10-04.
 */
public class ReviewUpdateFragment extends Fragment implements Server.ILoadResult, View.OnClickListener {

    private TextView storeName;
    private EditText reviewContent;
    private Button imgInsertBtn;
    private Button reviewSubmitBtn;
    private Button reviewCancelBtn;
    private Review_item rv;
    private FragmentManager fragmentManager = getFragmentManager();
    private String rkey;
    private String sh_id;


    public static ReviewUpdateFragment newInstance() {
        ReviewUpdateFragment fragment = new ReviewUpdateFragment();
        return fragment;
    }

    /*  public ReviewWriteFragment newInstance(String con, int rkey){
          ReviewWriteFragment fragment = new ReviewWriteFragment();
  //        String con = rv.getRcontents();

          reviewContent.setText(con);
          return fragment;
      }
  */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review_update, null);

        Bundle bundle = this.getArguments();
        rkey = bundle.getString("review_Rkey");
        sh_id = bundle.getString("review_sh_id");

        storeName = (TextView) view.findViewById(R.id.storeName_review_write_u);
        reviewContent = (EditText) view.findViewById(R.id.review_content_u);
        imgInsertBtn = (Button) view.findViewById(R.id.insertImg_u);
        reviewSubmitBtn = (Button) view.findViewById(R.id.review_submit_u);
        reviewCancelBtn = (Button) view.findViewById(R.id.review_cancel_u);

        //Adater가 가지고 있던 값을 가져와서 Fragment를 열 때 해당 값으로 미리 지정
        storeName.setText(bundle.getString("review_StoreName"));
        reviewContent.setText(bundle.getString("review_Rcontents"));


        imgInsertBtn.setOnClickListener(this);
        reviewSubmitBtn.setOnClickListener(this);
        reviewCancelBtn.setOnClickListener(this);

        return view;
    }


    @Override
    public void customAddList(String result) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.insertImg_u:
                Toast.makeText(getActivity(), "이미지 추가버튼을 눌렀음", Toast.LENGTH_SHORT).show();
                break;
            case R.id.review_submit_u:

                if (reviewContent.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_UPDATE, "POST", "reviewUpdate", String.valueOf(rkey), reviewContent.getText().toString()};
                    Server server = new Server(getActivity(), this);
                    server.execute(gets);
                    Toast.makeText(getActivity(), "리뷰를 등록했습니다.", Toast.LENGTH_SHORT).show();
//                    fragmentManager.popBackStack();
                    MainActivity.changeFragment("Review");
                }
                break;
            case R.id.review_cancel_u:
                Log.d("review_cancel click ", "클릭");
                Toast.makeText(getActivity(), "전으로 돌아갑니다", Toast.LENGTH_SHORT).show();
//                    fragmentManager.popBackStack();
                MainActivity.changeFragment("Review");
                break;

        }
    }
}
