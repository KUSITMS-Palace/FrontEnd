package com.example.palace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
    private ISessionCallback mSessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //카카오 로그인 기능 시작
        mSessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                // 로그인 요청
                UserManagement.getInstance().me(new MeV2ResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        // 로그인 요청했는데 실패했을 때
                        Toast.makeText(LoginActivity.this, "로그인 도중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        // 세션이 닫힘..
                        Toast.makeText(LoginActivity.this, "세션이 닫혔습니다.. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    //로그인에 성공했을 시
                    @Override
                    public void onSuccess(MeV2Response result) {
                        // 로그인 성공
                        AccessToken accessToken;
                        accessToken = Session.getCurrentSession().getTokenInfo();
                        String kakaoAccessToken = accessToken.getAccessToken();

                        Log.e("Login Access Token : ",kakaoAccessToken);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
                        intent.putExtra("profileImg", result.getKakaoAccount().getProfile().getProfileImageUrl());
                        intent.putExtra("accessToken", kakaoAccessToken);

                        startActivity(intent);

                        Toast.makeText(LoginActivity.this, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Toast.makeText(LoginActivity.this, "세션 열기에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        };

        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen(); //세션 유지
        getAppKeyHash();
    }
    private void getAppKeyHash()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }
}