package com.ldw.library.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * A simple text label view that can be applied as a "badge" to any given {@link View}.
 * This class is intended to be instantiated at runtime rather than included in XML layouts.
 * Thank https://github.com/jgilfelt/android-viewbadger very much
 * @author Jeff Gilfelt
 */
public class BadgeView extends android.support.v7.widget.AppCompatTextView {

	public static final int POSITION_TOP_LEFT = 1;
	public static final int POSITION_TOP_RIGHT = 2;
	public static final int POSITION_BOTTOM_LEFT = 3;
	public static final int POSITION_BOTTOM_RIGHT = 4;
	public static final int POSITION_CENTER = 5;
	
	private static final int DEFAULT_MARGIN_DIP = 5;
	private static final int DEFAULT_LR_PADDING_DIP = 5;
	private static final int DEFAULT_CORNER_RADIUS_DIP = 10;
	private static final int DEFAULT_POSITION = POSITION_TOP_RIGHT;
	private static final int DEFAULT_BADGE_COLOR = Color.parseColor("#FFFF0000"); //Color.RED;
	private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

	private static Animation fadeIn;
	private static Animation fadeOut;
	private final int mFontSize;

	private Context context;
	private View target;
	
	private int badgePosition;
	private int badgeMarginH;
	private int badgeMarginV;
	private int badgeColor;
	
	private boolean isShown;
	
	private ShapeDrawable badgeBg;
	
	private float mRadius;

	public BadgeView(Context context) {
		this(context, null, android.R.attr.textViewStyle);
	}
	
	public BadgeView(Context context, AttributeSet attrs) {
		 this(context, attrs, android.R.attr.textViewStyle);
	}

	public BadgeView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs, defStyle, null);
	}
	
	/**
     * Constructor -
     * 
     * create a new BadgeView instance attached to a target {@link View}.
     *
     * @param context context for this view.
     * @param target the View to attach the badge to.
     */
	public BadgeView(Context context, View target) {
		 this(context, null, android.R.attr.textViewStyle, target);
	}
	

	public BadgeView(Context context, AttributeSet attrs, int defStyle, View target) {
		super(context, attrs, defStyle);

		TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, typedValue, true);

		int[] fontAttr = new int[]{android.R.attr.textSize};

		final TypedArray a = context.obtainStyledAttributes(typedValue.resourceId, fontAttr);
		//http://blog.csdn.net/cnmilan/article/details/38339109
		mFontSize = a.getDimensionPixelSize(0, 30);

		a.recycle();

		init(context, target);
	}

	private void init(Context context, View target) {
		
		this.context = context;
		this.target = target;

		// apply defaults
		badgePosition = DEFAULT_POSITION;
		badgeMarginH = dipToPixels(DEFAULT_MARGIN_DIP);
		badgeMarginV = badgeMarginH;
		badgeColor = DEFAULT_BADGE_COLOR;
		
		int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
//		setPadding(paddingPixels, 0, paddingPixels, 0);
		setTextColor(DEFAULT_TEXT_COLOR);

		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(200);

		fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setDuration(200);
		
		isShown = false;
		
		if (this.target != null) {
			applyTo(this.target);
		} else {
			show();
		}
		
	}

	private void applyTo(View target) {
		
		LayoutParams lp = target.getLayoutParams();
		ViewParent parent = target.getParent();
		FrameLayout container = new FrameLayout(context);

		ViewGroup group = (ViewGroup) parent;
		int index = group.indexOfChild(target);

		group.removeView(target);
		group.addView(container, index, lp);

		container.addView(target);

		this.setVisibility(View.GONE);
		container.addView(this);

		group.invalidate();
		
	}
	
	/**
     * Make the badge visible in the UI.
     * 
     */
	public void show() {
		show(false, null);
	}
	
	/**
     * Make the badge visible in the UI.
     *
     * @param animate flag to apply the default fade-in animation.
     */
	public void show(boolean animate) {
		show(animate, fadeIn);
	}
	
	/**
     * Make the badge visible in the UI.
     *
     * @param anim Animation to apply to the view when made visible.
     */
	public void show(Animation anim) {
		show(true, anim);
	}
	
	/**
     * Make the badge non-visible in the UI.
     * 
     */
	public void hide() {
		hide(false, null);
	}
	
	/**
     * Make the badge non-visible in the UI.
     *
     * @param animate flag to apply the default fade-out animation.
     */
	public void hide(boolean animate) {
		hide(animate, fadeOut);
	}
	
	/**
     * Make the badge non-visible in the UI.
     *
     * @param anim Animation to apply to the view when made non-visible.
     */
	public void hide(Animation anim) {
		hide(true, anim);
	}
	
	/**
     * Toggle the badge visibility in the UI.
     * 
     */
	public void toggle() {
		toggle(false, null, null);
	}
	
	/**
     * Toggle the badge visibility in the UI.
     * 
     * @param animate flag to apply the default fade-in/out animation.
     */
	public void toggle(boolean animate) {
		toggle(animate, fadeIn, fadeOut);
	}
	
	/**
     * Toggle the badge visibility in the UI.
     *
     * @param animIn Animation to apply to the view when made visible.
     * @param animOut Animation to apply to the view when made non-visible.
     */
	public void toggle(Animation animIn, Animation animOut) {
		toggle(true, animIn, animOut);
	}
	
	private void show(boolean animate, Animation anim) {
		if (getBackground() == null) {
			if (badgeBg == null) {
				badgeBg = getDefaultBackground();
			}
			setBackgroundDrawable(badgeBg);
		}
		applyLayoutParams();
		
		if (animate) {
			this.startAnimation(anim);
		}
		this.setVisibility(View.VISIBLE);
		isShown = true;
	}
	
	private void hide(boolean animate, Animation anim) {
		this.setVisibility(View.GONE);
		if (animate) {
			this.startAnimation(anim);
		}
		isShown = false;
	}
	
	private void toggle(boolean animate, Animation animIn, Animation animOut) {
		if (isShown) {
			hide(animate && (animOut != null), animOut);	
		} else {
			show(animate && (animIn != null), animIn);
		}
	}
	
	/**
     * Increment the numeric badge label. If the current badge label cannot be converted to
     * an integer value, its label will be set to "0".
     * 
     * @param offset the increment offset.
     */
	public int increment(int offset) {
		CharSequence txt = getText();
		int i;
		if (txt != null) {
			try {
				i = Integer.parseInt(txt.toString());
			} catch (NumberFormatException e) {
				i = 0;
			}
		} else {
			i = 0;
		}
		i = i + offset;
		setText(String.valueOf(i));
		return i;
	}
	
	/**
     * Decrement the numeric badge label. If the current badge label cannot be converted to
     * an integer value, its label will be set to "0".
     * 
     * @param offset the decrement offset.
     */
	public int decrement(int offset) {
		return increment(-offset);
	}
	
	private ShapeDrawable getDefaultBackground() {
		
//		int r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
//		float r = mRadius * 2f;
//		float r = (float) (Math.PI * 0.5f * mRadius);
		float r = 0;
		float[] outerR = new float[] {r, r, r, r, r, r, r, r};
        
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
//		RectShape rs = new RectShape();
		ShapeDrawable drawable = new ShapeDrawable(rr);
//		ShapeDrawable drawable = new ShapeDrawable(rs);
		drawable.getPaint().setColor(badgeColor);
		
		return drawable;
	}

	private void measureText() {
		Paint paint = new Paint();
		String text = getText().toString();
		if (TextUtils.isEmpty(text)) {
//			mBadgeTextRect.right = 0;
//			mBadgeTextRect.bottom = 0;
		} else {
			paint.setTextSize(mFontSize);
			float width = paint.measureText(text);
			int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
			mRadius = (float) (width * 0.5 + paddingPixels);
		}

		badgeBg = getDefaultBackground();
		setBackgroundDrawable(badgeBg);
	}
	
	private void applyLayoutParams() {
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)mRadius, (int)mRadius);

		switch (badgePosition) {
		case POSITION_TOP_LEFT:
			lp.gravity = Gravity.LEFT | Gravity.TOP;
			lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
			break;
		case POSITION_TOP_RIGHT:
			lp.gravity = Gravity.RIGHT | Gravity.TOP;
			lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
			break;
		case POSITION_BOTTOM_LEFT:
			lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
			lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
			break;
		case POSITION_BOTTOM_RIGHT:
			lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
			break;
		case POSITION_CENTER:
			lp.gravity = Gravity.CENTER;
			lp.setMargins(0, 0, 0, 0);
			break;
		default:
			break;
		}
		
		setLayoutParams(lp);
		
	}

	/**
     * Returns the target View this badge has been attached to.
     * 
     */
	public View getTarget() {
		return target;
	}

	/**
     * Is this badge currently visible in the UI?
     * 
     */
	@Override
	public boolean isShown() {
		return isShown;
	}

	/**
     * Returns the positioning of this badge.
     * 
     * one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT, POSTION_CENTER.
     * 
     */
	public int getBadgePosition() {
		return badgePosition;
	}

	/**
     * Set the positioning of this badge.
     * 
     * @param layoutPosition one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT, POSTION_CENTER.
     * 
     */
	public void setBadgePosition(int layoutPosition) {
		this.badgePosition = layoutPosition;
	}

	/**
     * Returns the horizontal margin from the target View that is applied to this badge.
     * 
     */
	public int getHorizontalBadgeMargin() {
		return badgeMarginH;
	}
	
	/**
     * Returns the vertical margin from the target View that is applied to this badge.
     * 
     */
	public int getVerticalBadgeMargin() {
		return badgeMarginV;
	}

	/**
     * Set the horizontal/vertical margin from the target View that is applied to this badge.
     * 
     * @param badgeMargin the margin in pixels.
     */
	public void setBadgeMargin(int badgeMargin) {
		this.badgeMarginH = badgeMargin;
		this.badgeMarginV = badgeMargin;
	}
	
	/**
     * Set the horizontal/vertical margin from the target View that is applied to this badge.
     * 
     * @param horizontal margin in pixels.
     * @param vertical margin in pixels.
     */
	public void setBadgeMargin(int horizontal, int vertical) {
		this.badgeMarginH = horizontal;
		this.badgeMarginV = vertical;
	}
	
	/**
     * Returns the color value of the badge background.
     * 
     */
	public int getBadgeBackgroundColor() {
		return badgeColor;
	}

	/**
     * Set the color value of the badge background.
     * 
     * @param badgeColor the badge background color.
     */
	public void setBadgeBackgroundColor(int badgeColor) {
		this.badgeColor = badgeColor;
		badgeBg = getDefaultBackground();
	}
	
	private int dipToPixels(int dip) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {

		if (TextUtils.isEmpty(text) || text.equals("0")) {
			this.setVisibility(View.GONE);
		} else {
			this.setVisibility(View.VISIBLE);
		}

		super.setText(text, type);

		measureText();
	}
}
