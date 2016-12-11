package ru.android_studio.check_passport;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.crash.FirebaseCrash;

import ru.android_studio.check_passport.model.Series;
import ru.android_studio.check_passport.model.TypicalResponse;
import ru.android_studio.check_passport.validation.CheckSeriesService;

import java.util.ArrayList;
import java.util.List;


public class PassportActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PassportActivity";
    private HistoryFragment historyFragment;
    private PassportFragment passportFragment;
    private CaptchaFragment captchaFragment;
    private EditText seriesEditText;
    private EditText numberEditText;
    private CheckSeriesService checkSeriesService = CheckSeriesService.getInstance();
    private ViewPagerAdapter adapter;

    private Passport passport;
    private SmevService smevService;

    public SmevService getSmevService() {
        if (smevService == null) {
            smevService = new SmevService(this);
        }
        return smevService;
    }

    public Passport getPassport() {
        if (passport == null) {
            passport = new Passport();
        }
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                Log.i(TAG, CategoryTracker.SHARE_LINK.name());

                String title = getString(R.string.app_name);
                String text = getString(R.string.share_msg);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TITLE, title);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        Log.i(TAG, CategoryTracker.APPLICATION.name());
    }

    /** Returns the referrer who started this Activity. */
    @Override
    public Uri getReferrer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return super.getReferrer();
        }
        return getReferrerCompatible();
    }

    /** Returns the referrer on devices running SDK versions lower than 22. */
    private Uri getReferrerCompatible() {
        Intent intent = this.getIntent();
        Uri referrerUri = intent.getParcelableExtra(Intent.EXTRA_REFERRER);
        if (referrerUri != null) {
            return referrerUri;
        }
        String referrer = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (referrer != null) {
            // Try parsing the referrer URL; if it's invalid, return null
            try {
                return Uri.parse(referrer);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    private void setupViewPager(ViewPager viewPager) {
        passportFragment = new PassportFragment();
        historyFragment = new HistoryFragment();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(passportFragment, getString(R.string.newRequest));
        adapter.addFragment(historyFragment, getString(R.string.history));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                    );
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.request_btn:
                showCaptchaDialogFragment();
                break;
            case R.id.check_btn:
                check();
                break;
            case R.id.new_request_btn:
                newRequest("dlg_result_fragment");
                break;
        }
    }

    private void newRequest(String fragmentTag) {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    private void check() {
        try {
            passportFragment = (PassportFragment) adapter.getItem(0);
            EditText captchaEditText = captchaFragment.getCaptchaEditText();
            seriesEditText = passportFragment.getSeriesEditText();
            numberEditText = passportFragment.getNumberEditText();

            passport = new Passport();
            passport.setSeries(seriesEditText.getText().toString());
            passport.setNumber(numberEditText.getText().toString());
            passport.setCaptcha(captchaEditText.getText().toString());
            passport.setCookies(captchaFragment.getCookies());


            if (passport.getSeries().length() != 4 || passport.getNumber().length() != 6 || passport.getCaptcha().length() != 6) {
                Toast.makeText(this, getString(R.string.validation_error_msg), Toast.LENGTH_LONG).show();
                return;
            }

            String actionRequest = ActionTracker.REQUEST.name() + " " + passport.toString();
            Log.i(TAG, actionRequest);

            /*
            * Делаем запрос в СМЭВ
            * */
            passport = getSmevService().request(passport);

            /*
            * Проверяем ответ
            * */
            if (passport == null || passport.getTypicalResponse() == null ||
                    passport.getTypicalResponse() == TypicalResponse.CAPTCHA_NOT_VALID) {
                Log.i(TAG, ActionTracker.CAPTCHA_NOT_VALID.name());
                Toast.makeText(this, getString(R.string.toast_error_internet_unavailable), Toast.LENGTH_LONG).show();
                return;
            } else {
                String actionResponse = ActionTracker.RESPONSE.name() + " " + getString(passport.getTypicalResponse().getDescription());
                Log.i(TAG, actionResponse);
                showResultDialogFragment();
            }

            historyFragment.addToHistoryList(passport);

            clear(seriesEditText, numberEditText, captchaEditText, captchaFragment);
        } catch (NullPointerException e) {
            Log.e("NPE", e.getMessage());
        }
    }

    private void showResultDialogFragment() {
        newRequest("dlg_captcha_fragment");

        ResultFragment resultFragment = new ResultFragment();
        resultFragment.show(getSupportFragmentManager(), "dlg_result_fragment");
    }

    private void showCaptchaDialogFragment() {
        captchaFragment = new CaptchaFragment();
        captchaFragment.setPassportActivity(this);
        captchaFragment.updateCaptchaImageBitmap();

        if (captchaFragment.getIsConnectionProblem()) {
            Toast.makeText(this, getString(R.string.toast_error_internet_unavailable), Toast.LENGTH_LONG).show();
            return;
        }

        seriesEditText = (EditText) findViewById(R.id.series);
        numberEditText = (EditText) findViewById(R.id.number);

        if (seriesEditText.getText().toString().length() != 4 ||
                numberEditText.getText().toString().length() != 6) {
            Toast.makeText(this, getString(R.string.validation_error_msg), Toast.LENGTH_LONG).show();
            return;
        }

        Series result = checkSeriesService.getCheckedSeries(seriesEditText.getText().toString());
        if (!result.isValid()) {
            Toast.makeText(this, getString(R.string.series_validation_error_msg), Toast.LENGTH_LONG).show();
            return;
        }

        captchaFragment.show(getSupportFragmentManager(), "dlg_captcha_fragment");
    }

    private void clear(EditText seriesEditText, EditText numberEditText, EditText captchaEditText, CaptchaFragment captchaFragment) {
        seriesEditText.setText("");
        numberEditText.setText("");
        captchaEditText.setText("");
        captchaFragment.setCookies("");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}