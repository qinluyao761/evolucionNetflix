// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.view.menu.MenuBuilder$Callback;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.appcompat.R$id;
import android.support.v7.view.menu.MenuPresenter$Callback;
import android.support.v7.content.res.AppCompatResources;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Menu;
import android.content.Context;
import android.view.ViewGroup$LayoutParams;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.appcompat.R$drawable;
import android.support.v7.appcompat.R$string;
import android.view.Window$Callback;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ToolbarWidgetWrapper implements DecorToolbar
{
    private ActionMenuPresenter mActionMenuPresenter;
    private View mCustomView;
    private int mDefaultNavigationContentDescription;
    private Drawable mDefaultNavigationIcon;
    private int mDisplayOpts;
    private CharSequence mHomeDescription;
    private Drawable mIcon;
    private Drawable mLogo;
    boolean mMenuPrepared;
    private Drawable mNavIcon;
    private int mNavigationMode;
    private CharSequence mSubtitle;
    private View mTabView;
    CharSequence mTitle;
    private boolean mTitleSet;
    Toolbar mToolbar;
    Window$Callback mWindowCallback;
    
    public ToolbarWidgetWrapper(final Toolbar toolbar, final boolean b) {
        this(toolbar, b, R$string.abc_action_bar_up_description, R$drawable.abc_ic_ab_back_material);
    }
    
    public ToolbarWidgetWrapper(final Toolbar mToolbar, final boolean b, final int defaultNavigationContentDescription, int n) {
        this.mNavigationMode = 0;
        this.mDefaultNavigationContentDescription = 0;
        this.mToolbar = mToolbar;
        this.mTitle = mToolbar.getTitle();
        this.mSubtitle = mToolbar.getSubtitle();
        this.mTitleSet = (this.mTitle != null);
        this.mNavIcon = mToolbar.getNavigationIcon();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(mToolbar.getContext(), null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
        this.mDefaultNavigationIcon = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_homeAsUpIndicator);
        if (b) {
            final CharSequence text = obtainStyledAttributes.getText(R$styleable.ActionBar_title);
            if (!TextUtils.isEmpty(text)) {
                this.setTitle(text);
            }
            final CharSequence text2 = obtainStyledAttributes.getText(R$styleable.ActionBar_subtitle);
            if (!TextUtils.isEmpty(text2)) {
                this.setSubtitle(text2);
            }
            final Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_logo);
            if (drawable != null) {
                this.setLogo(drawable);
            }
            final Drawable drawable2 = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_icon);
            if (drawable2 != null) {
                this.setIcon(drawable2);
            }
            if (this.mNavIcon == null && this.mDefaultNavigationIcon != null) {
                this.setNavigationIcon(this.mDefaultNavigationIcon);
            }
            this.setDisplayOptions(obtainStyledAttributes.getInt(R$styleable.ActionBar_displayOptions, 0));
            n = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_customNavigationLayout, 0);
            if (n != 0) {
                this.setCustomView(LayoutInflater.from(this.mToolbar.getContext()).inflate(n, (ViewGroup)this.mToolbar, false));
                this.setDisplayOptions(this.mDisplayOpts | 0x10);
            }
            n = obtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_height, 0);
            if (n > 0) {
                final ViewGroup$LayoutParams layoutParams = this.mToolbar.getLayoutParams();
                layoutParams.height = n;
                this.mToolbar.setLayoutParams(layoutParams);
            }
            n = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.ActionBar_contentInsetStart, -1);
            final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.ActionBar_contentInsetEnd, -1);
            if (n >= 0 || dimensionPixelOffset >= 0) {
                this.mToolbar.setContentInsetsRelative(Math.max(n, 0), Math.max(dimensionPixelOffset, 0));
            }
            n = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_titleTextStyle, 0);
            if (n != 0) {
                this.mToolbar.setTitleTextAppearance(this.mToolbar.getContext(), n);
            }
            n = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_subtitleTextStyle, 0);
            if (n != 0) {
                this.mToolbar.setSubtitleTextAppearance(this.mToolbar.getContext(), n);
            }
            n = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_popupTheme, 0);
            if (n != 0) {
                this.mToolbar.setPopupTheme(n);
            }
        }
        else {
            this.mDisplayOpts = this.detectDisplayOptions();
        }
        obtainStyledAttributes.recycle();
        this.setDefaultNavigationContentDescription(defaultNavigationContentDescription);
        this.mHomeDescription = this.mToolbar.getNavigationContentDescription();
        this.mToolbar.setNavigationOnClickListener((View$OnClickListener)new ToolbarWidgetWrapper$1(this));
    }
    
    private int detectDisplayOptions() {
        int n = 11;
        if (this.mToolbar.getNavigationIcon() != null) {
            n = 15;
            this.mDefaultNavigationIcon = this.mToolbar.getNavigationIcon();
        }
        return n;
    }
    
    private void setTitleInt(final CharSequence charSequence) {
        this.mTitle = charSequence;
        if ((this.mDisplayOpts & 0x8) != 0x0) {
            this.mToolbar.setTitle(charSequence);
        }
    }
    
    private void updateHomeAccessibility() {
        if ((this.mDisplayOpts & 0x4) != 0x0) {
            if (!TextUtils.isEmpty(this.mHomeDescription)) {
                this.mToolbar.setNavigationContentDescription(this.mHomeDescription);
                return;
            }
            this.mToolbar.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
        }
    }
    
    private void updateNavigationIcon() {
        if ((this.mDisplayOpts & 0x4) != 0x0) {
            final Toolbar mToolbar = this.mToolbar;
            Drawable navigationIcon;
            if (this.mNavIcon != null) {
                navigationIcon = this.mNavIcon;
            }
            else {
                navigationIcon = this.mDefaultNavigationIcon;
            }
            mToolbar.setNavigationIcon(navigationIcon);
            return;
        }
        this.mToolbar.setNavigationIcon(null);
    }
    
    private void updateToolbarLogo() {
        Drawable logo = null;
        if ((this.mDisplayOpts & 0x2) != 0x0) {
            if ((this.mDisplayOpts & 0x1) != 0x0) {
                if (this.mLogo != null) {
                    logo = this.mLogo;
                }
                else {
                    logo = this.mIcon;
                }
            }
            else {
                logo = this.mIcon;
            }
        }
        this.mToolbar.setLogo(logo);
    }
    
    @Override
    public boolean canShowOverflowMenu() {
        return this.mToolbar.canShowOverflowMenu();
    }
    
    @Override
    public void collapseActionView() {
        this.mToolbar.collapseActionView();
    }
    
    @Override
    public void dismissPopupMenus() {
        this.mToolbar.dismissPopupMenus();
    }
    
    @Override
    public Context getContext() {
        return this.mToolbar.getContext();
    }
    
    @Override
    public int getDisplayOptions() {
        return this.mDisplayOpts;
    }
    
    @Override
    public Menu getMenu() {
        return this.mToolbar.getMenu();
    }
    
    @Override
    public int getNavigationMode() {
        return this.mNavigationMode;
    }
    
    @Override
    public CharSequence getTitle() {
        return this.mToolbar.getTitle();
    }
    
    @Override
    public ViewGroup getViewGroup() {
        return this.mToolbar;
    }
    
    @Override
    public int getVisibility() {
        return this.mToolbar.getVisibility();
    }
    
    @Override
    public boolean hasExpandedActionView() {
        return this.mToolbar.hasExpandedActionView();
    }
    
    @Override
    public boolean hideOverflowMenu() {
        return this.mToolbar.hideOverflowMenu();
    }
    
    @Override
    public void initIndeterminateProgress() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }
    
    @Override
    public void initProgress() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }
    
    @Override
    public boolean isOverflowMenuShowPending() {
        return this.mToolbar.isOverflowMenuShowPending();
    }
    
    @Override
    public boolean isOverflowMenuShowing() {
        return this.mToolbar.isOverflowMenuShowing();
    }
    
    @Override
    public void setBackgroundDrawable(final Drawable drawable) {
        ViewCompat.setBackground((View)this.mToolbar, drawable);
    }
    
    @Override
    public void setCollapsible(final boolean collapsible) {
        this.mToolbar.setCollapsible(collapsible);
    }
    
    @Override
    public void setCustomView(final View mCustomView) {
        if (this.mCustomView != null && (this.mDisplayOpts & 0x10) != 0x0) {
            this.mToolbar.removeView(this.mCustomView);
        }
        if ((this.mCustomView = mCustomView) != null && (this.mDisplayOpts & 0x10) != 0x0) {
            this.mToolbar.addView(this.mCustomView);
        }
    }
    
    public void setDefaultNavigationContentDescription(final int mDefaultNavigationContentDescription) {
        if (mDefaultNavigationContentDescription != this.mDefaultNavigationContentDescription) {
            this.mDefaultNavigationContentDescription = mDefaultNavigationContentDescription;
            if (TextUtils.isEmpty(this.mToolbar.getNavigationContentDescription())) {
                this.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
            }
        }
    }
    
    @Override
    public void setDisplayOptions(final int mDisplayOpts) {
        final int n = this.mDisplayOpts ^ mDisplayOpts;
        this.mDisplayOpts = mDisplayOpts;
        if (n != 0) {
            if ((n & 0x4) != 0x0) {
                if ((mDisplayOpts & 0x4) != 0x0) {
                    this.updateHomeAccessibility();
                }
                this.updateNavigationIcon();
            }
            if ((n & 0x3) != 0x0) {
                this.updateToolbarLogo();
            }
            if ((n & 0x8) != 0x0) {
                if ((mDisplayOpts & 0x8) != 0x0) {
                    this.mToolbar.setTitle(this.mTitle);
                    this.mToolbar.setSubtitle(this.mSubtitle);
                }
                else {
                    this.mToolbar.setTitle(null);
                    this.mToolbar.setSubtitle(null);
                }
            }
            if ((n & 0x10) != 0x0 && this.mCustomView != null) {
                if ((mDisplayOpts & 0x10) == 0x0) {
                    this.mToolbar.removeView(this.mCustomView);
                    return;
                }
                this.mToolbar.addView(this.mCustomView);
            }
        }
    }
    
    @Override
    public void setEmbeddedTabView(final ScrollingTabContainerView mTabView) {
        if (this.mTabView != null && this.mTabView.getParent() == this.mToolbar) {
            this.mToolbar.removeView(this.mTabView);
        }
        if ((this.mTabView = (View)mTabView) != null && this.mNavigationMode == 2) {
            this.mToolbar.addView(this.mTabView, 0);
            final Toolbar$LayoutParams toolbar$LayoutParams = (Toolbar$LayoutParams)this.mTabView.getLayoutParams();
            toolbar$LayoutParams.width = -2;
            toolbar$LayoutParams.height = -2;
            toolbar$LayoutParams.gravity = 8388691;
            mTabView.setAllowCollapse(true);
        }
    }
    
    @Override
    public void setHomeButtonEnabled(final boolean b) {
    }
    
    @Override
    public void setIcon(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setIcon(drawable);
    }
    
    @Override
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
        this.updateToolbarLogo();
    }
    
    @Override
    public void setLogo(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setLogo(drawable);
    }
    
    @Override
    public void setLogo(final Drawable mLogo) {
        this.mLogo = mLogo;
        this.updateToolbarLogo();
    }
    
    @Override
    public void setMenu(final Menu menu, final MenuPresenter$Callback callback) {
        if (this.mActionMenuPresenter == null) {
            (this.mActionMenuPresenter = new ActionMenuPresenter(this.mToolbar.getContext())).setId(R$id.action_menu_presenter);
        }
        this.mActionMenuPresenter.setCallback(callback);
        this.mToolbar.setMenu((MenuBuilder)menu, this.mActionMenuPresenter);
    }
    
    @Override
    public void setMenuCallbacks(final MenuPresenter$Callback menuPresenter$Callback, final MenuBuilder$Callback menuBuilder$Callback) {
        this.mToolbar.setMenuCallbacks(menuPresenter$Callback, menuBuilder$Callback);
    }
    
    @Override
    public void setMenuPrepared() {
        this.mMenuPrepared = true;
    }
    
    @Override
    public void setNavigationContentDescription(final int n) {
        CharSequence string;
        if (n == 0) {
            string = null;
        }
        else {
            string = this.getContext().getString(n);
        }
        this.setNavigationContentDescription(string);
    }
    
    public void setNavigationContentDescription(final CharSequence mHomeDescription) {
        this.mHomeDescription = mHomeDescription;
        this.updateHomeAccessibility();
    }
    
    public void setNavigationIcon(final Drawable mNavIcon) {
        this.mNavIcon = mNavIcon;
        this.updateNavigationIcon();
    }
    
    @Override
    public void setSubtitle(final CharSequence charSequence) {
        this.mSubtitle = charSequence;
        if ((this.mDisplayOpts & 0x8) != 0x0) {
            this.mToolbar.setSubtitle(charSequence);
        }
    }
    
    @Override
    public void setTitle(final CharSequence titleInt) {
        this.mTitleSet = true;
        this.setTitleInt(titleInt);
    }
    
    @Override
    public void setVisibility(final int visibility) {
        this.mToolbar.setVisibility(visibility);
    }
    
    @Override
    public void setWindowCallback(final Window$Callback mWindowCallback) {
        this.mWindowCallback = mWindowCallback;
    }
    
    @Override
    public void setWindowTitle(final CharSequence titleInt) {
        if (!this.mTitleSet) {
            this.setTitleInt(titleInt);
        }
    }
    
    @Override
    public ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int n, final long duration) {
        final ViewPropertyAnimatorCompat animate = ViewCompat.animate((View)this.mToolbar);
        float n2;
        if (n == 0) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        return animate.alpha(n2).setDuration(duration).setListener(new ToolbarWidgetWrapper$2(this, n));
    }
    
    @Override
    public boolean showOverflowMenu() {
        return this.mToolbar.showOverflowMenu();
    }
}
