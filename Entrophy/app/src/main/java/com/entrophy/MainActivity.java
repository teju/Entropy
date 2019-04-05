package com.entrophy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.helper.Constants;
import com.entrophy.helper.SharedPreference;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener ,View.OnClickListener{

    private ViewPager pager_introduction;
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    public int currentimageindex = 0;
    private int[] images = {R.drawable.introduction_image_1, R.drawable.introduction_image_2,
            R.drawable.introduction_image_3, R.drawable.introduction_image_4};
    String[] description = {"Never miss an opportunity to hangout with your friends in person.",
            "Know where your friends are at any point in time. No more guessing.",
            "Keep in touch only with those whom you care and be up-to-date about your friends.",
            "No worries. Just enter “Stealth Mode” and you are invisible for 24 hours."};
    String[] titles = {"Get Notified When Your Friend Is Around", "Know Your Friends’ Whereabouts",
            "Keep Friends At Your Finger Tips","Too Busy To Hangout?"};
    Animation animation;
    private Timer timer;
    private TextView title, desc;
    private Button done;

    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static boolean continue_animation = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();
    }

    public void init() {
        Constants.checkContactsPermission(this);
        pager_introduction = (ViewPager)findViewById(R.id.pager_introduction);
        pager_introduction.setOnPageChangeListener(this);
        pager_indicator = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView)findViewById(R.id.desc);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);
    }

    public void initData() {
        NUM_PAGES =images.length;

        AnimationSet set = new AnimationSet(true);
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShow();

            }
        };
        final Handler mHandler = new Handler();

        int delay = 000; // delay for 1 sec.

        int period = 3000; // repeat every 4 sec.

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }
        }, delay, period);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );

        animation.setDuration(900);
        set.addAnimation(animation);
        setUiPageViewController();
        for (int i = 0; i < images.length; i++)
            ImagesArray.add(images[i]);

        pager_introduction.setAdapter(new SlidingImage_Adapter(MainActivity.this, ImagesArray));

        // Auto start of viewpager
        final Handler handler = new Handler();
        // Auto start of viewpager
        final Runnable Update = new Runnable() {
            public void run() {
                System.out.println("Handler " + currentPage + " continue_animation " + continue_animation);
                if(continue_animation) {
                    pager_introduction.setCurrentItem(currentPage++, true);
                    if (currentPage == NUM_PAGES ) {
                        currentPage = 0;
                        continue_animation = false;

                    }
                }

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(continue_animation) {
                    System.out.println("Handler swipeTimer " + " continue_animation " +
                            continue_animation + " currentPage " + currentPage);
                    handler.post(Update);
                }


            }
        }, delay, period);
    }

    private void AnimateandSlideShow() {
        try {
            if(currentimageindex == NUM_PAGES - 1) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentimageindex = 0;
                    }
                }, 100);
                done.setVisibility(View.VISIBLE);
            } else if(continue_animation) {
                done.setVisibility(View.GONE);

            }
            if(continue_animation) {

                title.setText(titles[currentimageindex]);
                desc.setText(description[currentimageindex]);
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                dots[currentimageindex % images.length].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

                Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.left_in);

                currentimageindex++;
                pager_introduction.startAnimation(rotateimage);
                desc.startAnimation(rotateimage);
                title.startAnimation(rotateimage);
            }

        } catch (Exception e){

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            title.setText(titles[position]);
            desc.setText(description[i]);
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        title.setText(titles[position]);
        desc.setText(description[position]);
        if(position == NUM_PAGES - 1) {
            done.setVisibility(View.VISIBLE);
        } else {
            done.setVisibility(View.GONE);

        }

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            title.setText(titles[position]);
            desc.setText(description[i]);
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        title.setText(titles[position]);
        desc.setText(description[position]);
        if(position == NUM_PAGES - 1) {

            done.setVisibility(View.VISIBLE);
        } else if(continue_animation) {
            done.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.done) {
            if(SharedPreference.getBool(this, Constants.KEY_IS_LOGGEDIN)) {
                Intent intent = new Intent(this,Contacts.class);
                intent.putExtra("Type",Constants.ConnectionsContacts);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this,WelcomePage.class);
                startActivity(intent);
                finish();
            }

        }
    }

    public class SlidingImage_Adapter extends PagerAdapter {


        private ArrayList<Integer> IMAGES;
        private LayoutInflater inflater;
        private Context context;


        public SlidingImage_Adapter(Context context,ArrayList<Integer> IMAGES) {
            this.context = context;
            this.IMAGES=IMAGES;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGES.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);


            imageView.setImageResource(IMAGES.get(position));

            view.addView(imageLayout, 0);

            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }
    private void setUiPageViewController() {

        dotsCount = images.length;
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(20, 0, 20, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

}
