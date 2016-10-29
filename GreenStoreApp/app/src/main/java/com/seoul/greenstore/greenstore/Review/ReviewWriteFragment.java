package com.seoul.greenstore.greenstore.Review;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.DetailFragment;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;

/**
 * Created by X on 2016-10-04.
 */
public class ReviewWriteFragment extends Fragment implements Server.ILoadResult, View.OnClickListener {

    private TextView storeName;
    private EditText reviewContent;
    private Button imgInsertBtn;
    private Button reviewSubmitBtn;
    private Button reviewCancelBtn;
    private Review_item rv;
    private FragmentManager fragmentManager = getFragmentManager();

    //DetailFragment로 받은 sh_id와 sh_name
    private int sh_id;
    private String sh_name;

    public static ReviewWriteFragment newInstance() {
        ReviewWriteFragment fragment = new ReviewWriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

/*    @Override
    public void onStart() {
        super.onStart();
        Log.d("coffee", "Review onstart IN");

        String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_WRITE, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review_write, null);

        Bundle bundle = this.getArguments();
        sh_id = bundle.getInt("sh_id");
        sh_name = bundle.getString("sh_name");

        storeName = (TextView) view.findViewById(R.id.storeName_review_write);
        storeName.setText(sh_name);

        reviewContent = (EditText) view.findViewById(R.id.review_content);
//        imgInsertBtn = (Button) view.findViewById(R.id.insertImg);
        reviewSubmitBtn = (Button) view.findViewById(R.id.review_submit);
        reviewCancelBtn = (Button) view.findViewById(R.id.review_cancel);


//        imgInsertBtn.setOnClickListener(this);
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
//            case R.id.insertImg:
//                Toast.makeText(getActivity(), "이미지 추가버튼을 눌렀음", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.review_submit:
                Log.d("reviewCon.getText():", reviewContent.getText().toString());
                if (reviewContent.getText().toString().equals("")) {

                    Toast.makeText(getActivity(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("reviewCon.getText():", reviewContent.getText().toString());
                    String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_WRITE, "POST", "reviewInsert", User.user.get(3), String.valueOf(sh_id) +
                            "", reviewContent.getText().toString()};
                    Server server = new Server(getActivity(), this);
                    server.execute(gets);
                    Toast.makeText(getActivity(), "리뷰를 등록했습니다.", Toast.LENGTH_SHORT).show();

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Fragment fragment = new DetailFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt("position", sh_id);

                    fragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.llContents, fragment);
                    fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                    fragmentTransaction.commit();

                    Toast.makeText(getActivity(), "전으로 돌아갑니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.review_cancel:
                Log.d("review_cancel click ", "클릭");

//                    fragmentManager.popBackStack();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new DetailFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("position", sh_id);

                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.llContents, fragment);
                fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                fragmentTransaction.commit();

                Toast.makeText(getActivity(), "전으로 돌아갑니다", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
