// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.iko.wordparty.moments;

import com.netflix.mediaclient.ui.iko.wordparty.model.WPInteractiveMomentsModel$WPImage;
import com.netflix.mediaclient.util.ThreadUtils;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.widget.ImageView$ScaleType;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.servicemgr.UIViewLogging$UIViewCommandName;
import com.netflix.mediaclient.util.ViewUtils;
import java.util.Collection;
import android.animation.AnimatorSet;
import android.animation.Animator;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.BitmapDrawable;
import java.util.Iterator;
import android.animation.Animator$AnimatorListener;
import android.animation.TimeInterpolator;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.ui.iko.BaseInteractiveMomentsManager$PlaybackCompleteListener;
import com.netflix.mediaclient.ui.iko.wordparty.WPConstants;
import com.netflix.mediaclient.util.DeviceUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.view.View;
import com.netflix.mediaclient.ui.iko.wordparty.model.WPInteractiveMomentsModel$WPItem;
import com.netflix.mediaclient.ui.iko.wordparty.model.WPInteractiveMomentsModel$WPAudio;
import android.view.ViewGroup;
import android.os.Handler;
import java.util.ArrayList;
import com.netflix.mediaclient.ui.iko.wordparty.model.WPInteractiveMomentsModel$WPMoment;
import java.util.List;
import android.view.View$OnClickListener;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class WPMomentScreen implements CardClickListener
{
    private static final String TAG = "WPMomentScreen";
    private final int ANTIALIAS_BORDER;
    private Bitmap bgImageBitmap;
    private ImageView bgView;
    private WPCardView card1;
    private WPCardView card2;
    private WPCardView card3;
    private WPCardView card4;
    private View$OnClickListener cardClickListener;
    private List<Bitmap> cardClosedBitmapList;
    private List<Bitmap> cardOpenBitmapList;
    private List<WPCardView> cardViewsList;
    private boolean cardsEnabled;
    private List<WPCardView> cardsList;
    private int colorBlue;
    private int colorGreen;
    private int colorRed;
    private int colorWhite;
    private int colorYellow;
    private WPCardView currentCard;
    private WPInteractiveMomentsModel$WPMoment currentMoment;
    private WPMomentScreen$WordPartyMomentState currentState;
    private ArrayList<String> currentlyPlayingAudioList;
    private final float deviceHeight;
    private float deviceWidth;
    private Bitmap fgImageBitmap;
    private ImageView fgView;
    private Handler handler;
    private ViewGroup ikoContainer;
    private List<WPInteractiveMomentsModel$WPAudio> instructionVOList;
    private List<WPInteractiveMomentsModel$WPAudio> introVOList;
    private boolean isPendingStart;
    private List<WPInteractiveMomentsModel$WPItem> itemList;
    private final WPInteractiveMomentsManager manager;
    private boolean momentClosed;
    private boolean openPanel;
    private View panel1;
    private View panel2;
    private View panel3;
    private View panel4;
    private LinearLayout panelContainer;
    private List<View> panelList;
    private List<WPInteractiveMomentsModel$WPAudio> passiveExitVOList;
    private List<WPInteractiveMomentsModel$WPAudio> positiveLineVOList;
    private final Interpolator quintOutInterpolator;
    private List<Bitmap> recapBitmapList;
    private List<WPInteractiveMomentsModel$WPItem> recapList;
    private List<WPInteractiveMomentsModel$WPAudio> recapVOList;
    private boolean resourcesLoaded;
    private boolean screenBackgrounded;
    private boolean screenPaused;
    private List<WPInteractiveMomentsModel$WPAudio> summaryVOList;
    private List<WPInteractiveMomentsModel$WPAudio> timeout2VOList;
    private int timeoutCounter;
    private Runnable timeoutRunnable;
    private List<WPInteractiveMomentsModel$WPAudio> timeoutVOList;
    private float wallyCardHeight;
    private float wallyCardWidth;
    private float wordWallyCardHeightScale;
    private float wordWallyCardWidthScale;
    private ViewGroup wpContainer;
    
    public WPMomentScreen(final WPInteractiveMomentsManager manager) {
        this.currentState = WPMomentScreen$WordPartyMomentState.INTRODUCTION;
        this.panelList = new ArrayList<View>();
        this.openPanel = true;
        this.currentlyPlayingAudioList = new ArrayList<String>();
        this.ANTIALIAS_BORDER = 1;
        this.cardOpenBitmapList = new ArrayList<Bitmap>();
        this.recapBitmapList = new ArrayList<Bitmap>();
        this.cardClosedBitmapList = new ArrayList<Bitmap>();
        this.cardsList = new ArrayList<WPCardView>();
        this.cardViewsList = new ArrayList<WPCardView>();
        this.handler = new Handler();
        this.timeoutCounter = 0;
        this.timeoutRunnable = new WPMomentScreen$7(this);
        this.manager = manager;
        this.deviceWidth = DeviceUtils.getScreenWidthInPixels(manager.getContext());
        this.deviceHeight = DeviceUtils.getScreenHeightInPixels(manager.getContext());
        this.quintOutInterpolator = WPConstants.getQuintOutInterpolator();
    }
    
    private void animateCardReset(final WPCardView wpCardView) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "animateCardReset: animation start");
        }
        wpCardView.animate().alpha(0.0f).setInterpolator((TimeInterpolator)this.quintOutInterpolator).setDuration(500L).setListener((Animator$AnimatorListener)new WPMomentScreen$6(this, wpCardView)).start();
    }
    
    private void animateContainerReset(final WPCardView wpCardView) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "animateContainerReset: animation start");
        }
        this.wpContainer.animate().x(0.0f).y(0.0f).scaleX(1.0f).scaleY(1.0f).rotation(0.0f).setInterpolator((TimeInterpolator)this.quintOutInterpolator).setDuration(500L).setListener((Animator$AnimatorListener)new WPMomentScreen$5(this, wpCardView)).start();
    }
    
    private void animationRecapStartValues() {
        if (this.cardViewsList == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "animationRecapStartValues: cardViewsList is null");
            }
            return;
        }
        for (final WPCardView wpCardView : this.cardViewsList) {
            wpCardView.setRotation(0.0f);
            wpCardView.setRotationY(0.0f);
            wpCardView.setTranslationX(-this.deviceWidth);
        }
        this.showHideCards(true);
    }
    
    private void animationStartValues(final boolean b) {
        this.resetCards(b);
        this.card1.setRotation(-90.0f);
        this.card2.setRotation(90.0f);
        this.card3.setRotation(-90.0f);
        this.card4.setRotation(-90.0f);
        this.card1.setTranslationX(-this.deviceWidth);
        this.card2.setTranslationX(this.deviceWidth);
        this.card3.setTranslationX(-this.deviceWidth);
        this.card4.setTranslationX(this.deviceWidth);
    }
    
    private void arrangeCardsForRecap() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "arrangeCardsForRecap: started");
        }
        this.cardsList.add(this.cardsList.size() - 1, this.cardsList.remove(0));
    }
    
    private BitmapDrawable bitmapWithBorder(final Bitmap bitmap) {
        if (bitmap == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "bitmapWithBorder: received a null drawable");
            }
            return null;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "bitmapWithBorder: Adding border to bitmap");
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth() + 2, bitmap.getHeight() + 2, Bitmap$Config.ARGB_8888);
        new Canvas(bitmap2).drawBitmap(bitmap, 1.0f, 1.0f, (Paint)null);
        return new BitmapDrawable(this.manager.getContext().getResources(), bitmap2);
    }
    
    private void cancelCurrentAudioPlaybacks() {
        if (this.currentlyPlayingAudioList.isEmpty()) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "cancelCurrentAudioPlaybacks: list is empty.");
            }
        }
        else {
            final Iterator<String> iterator = this.currentlyPlayingAudioList.iterator();
            while (iterator.hasNext()) {
                this.manager.stopAudioPlayback(iterator.next());
            }
        }
    }
    
    private void cardClickAnimationComplete(final WPCardView wpCardView) {
        if (wpCardView == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "cardClickAnimationComplete: card is null");
            }
            return;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "cardClickAnimationComplete: card = " + wpCardView);
        }
        if (this.isLearnMoment()) {
            if (this.cardsList == null || this.cardsList.size() <= 1) {
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "animateCardReset: cardsList is empty.");
                }
                this.cardsList.remove(wpCardView);
                this.discardAnimationComplete();
                return;
            }
            this.animateCardReset(wpCardView);
        }
        else {
            if (this.isRevealMoment()) {
                this.discardAnimation(wpCardView);
                return;
            }
            this.animateContainerReset(wpCardView);
        }
    }
    
    private void discardAnimation(final WPCardView wpCardView) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "discardAnimation: started");
        }
        this.cardsList.remove(wpCardView);
        final AnimatorSet cardAnimation = wpCardView.getCardAnimation(this.deviceWidth, 0.0f, 90.0f, 1.0f);
        final List<Animator> revealCardAnimations = this.getRevealCardAnimations(this.cardsList, false);
        revealCardAnimations.add((Animator)cardAnimation);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether((Collection)revealCardAnimations);
        set.setDuration(1000L);
        set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
        set.addListener((Animator$AnimatorListener)new WPMomentScreen$15(this));
        set.start();
    }
    
    private void enableCards() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "enableCards: cardsEnabled = true");
        }
        this.cardsEnabled = true;
    }
    
    private void flipCard(final WPCardView wpCardView) {
        if (wpCardView == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "flipCard: card is null");
            }
        }
        else {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "flipCard: cardsEnabled = " + this.cardsEnabled);
            }
            if (this.cardsEnabled) {
                this.timeoutCounter = 0;
                this.startStopTimeoutTimer(false);
                wpCardView.flip();
            }
        }
    }
    
    private int getLearnMomentPanelColor(final int n) {
        final int colorWhite = this.colorWhite;
        switch (n) {
            default: {
                return colorWhite;
            }
            case 0: {
                return this.colorYellow;
            }
            case 1: {
                return this.colorGreen;
            }
            case 2: {
                return this.colorBlue;
            }
            case 3: {
                return this.colorRed;
            }
        }
    }
    
    private List<Animator> getRecapAnimations(final List<WPCardView> list) {
        if (list == null) {
            Log.d("WPMomentScreen", "getRecapAnimations: cardsList is null");
            return null;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "getRecapAnimations: started");
        }
        final int size = list.size();
        final ArrayList<AnimatorSet> list2 = (ArrayList<AnimatorSet>)new ArrayList<Animator>();
        for (int i = 0; i < size; ++i) {
            final WPCardView wpCardView = list.get(i);
            if (wpCardView == null) {
                Log.d("WPMomentScreen", "CardView is null. returning without animation");
                return null;
            }
            final AnimatorSet calculateRecapAnimation = wpCardView.calculateRecapAnimation(i, size);
            calculateRecapAnimation.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
            list2.add((Animator)calculateRecapAnimation);
        }
        return (List<Animator>)list2;
    }
    
    private List<Animator> getRecapEntryAnimations(final List<WPCardView> list) {
        if (list == null) {
            Log.d("WPMomentScreen", "getRecapEntryAnimations: cardsList is null");
            return null;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "getRecapEntryAnimations: started");
        }
        final int size = list.size();
        final ArrayList<AnimatorSet> list2 = (ArrayList<AnimatorSet>)new ArrayList<Animator>();
        for (int i = 0; i < size; ++i) {
            final WPCardView wpCardView = list.get(i);
            if (wpCardView == null) {
                Log.d("TAG", "CardView is null. returning without animation");
                return null;
            }
            final AnimatorSet calculateRecapInitAnimation = wpCardView.calculateRecapInitAnimation(i, size);
            calculateRecapInitAnimation.setStartDelay((long)((size - i) * 200));
            calculateRecapInitAnimation.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
            list2.add((Animator)calculateRecapInitAnimation);
        }
        return (List<Animator>)list2;
    }
    
    private List<Animator> getRecapExitAnimations(final List<WPCardView> list) {
        if (list == null) {
            Log.d("WPMomentScreen", "getRecapExitAnimations: cardsList is null");
            return null;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "getRecapExitAnimations: started");
        }
        final int size = list.size();
        final ArrayList<AnimatorSet> list2 = (ArrayList<AnimatorSet>)new ArrayList<Animator>();
        for (int i = 0; i < size; ++i) {
            final WPCardView wpCardView = list.get(i);
            if (wpCardView == null) {
                Log.d("WPMomentScreen", "CardView is null. returning without animation");
                return null;
            }
            final AnimatorSet calculateRecapExitAnimation = wpCardView.calculateRecapExitAnimation(i, size);
            calculateRecapExitAnimation.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
            list2.add((Animator)calculateRecapExitAnimation);
        }
        return (List<Animator>)list2;
    }
    
    private List<Animator> getRevealCardAnimations(final List<WPCardView> list, final boolean b) {
        if (list == null) {
            Log.d("WPMomentScreen", "getRevealCardAnimations: cardsList is null");
            return null;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "getRevealCardAnimations: started");
        }
        final int size = list.size();
        final ArrayList<AnimatorSet> list2 = (ArrayList<AnimatorSet>)new ArrayList<Animator>();
        for (int i = 0; i < size; ++i) {
            final WPCardView wpCardView = list.get(i);
            if (wpCardView == null) {
                Log.d("WPMomentScreen", "CardView is null. returning without animation");
                return null;
            }
            final AnimatorSet calculateRevealAnimation = wpCardView.calculateRevealAnimation(i, size);
            if (b) {
                calculateRevealAnimation.setStartDelay((long)(i * 333));
            }
            calculateRevealAnimation.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
            list2.add((Animator)calculateRevealAnimation);
        }
        return (List<Animator>)list2;
    }
    
    private int getStatusBarHeight() {
        return ViewUtils.getStatusBarHeight(this.manager.getContext());
    }
    
    private void handleCardClicked(final WPCardView currentCard) {
        if (currentCard == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "handleCardClicked: card is null");
            }
        }
        else {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "handleCardClicked: card = " + currentCard);
            }
            this.manager.reportCommandEvent(UIViewLogging$UIViewCommandName.select);
            this.currentCard = currentCard;
            if (this.cardsEnabled) {
                this.manager.playItemSelectSound();
                this.cancelCurrentAudioPlaybacks();
                this.moveToState(WPMomentScreen$WordPartyMomentState.ITEM_SELECTION);
                if (this.isLearnMoment()) {
                    this.scaleUpCard(currentCard);
                    return;
                }
                if (this.isRevealMoment()) {
                    this.flipCard(currentCard);
                    return;
                }
                this.zoomInCard(currentCard);
            }
        }
    }
    
    private boolean isRevealMoment() {
        return "REVEAL".equalsIgnoreCase(this.currentMoment.getSceneType());
    }
    
    private void moveToState(WPMomentScreen$WordPartyMomentState currentState) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "moveToState: state = " + currentState);
        }
        if (!this.isMomentClosed() && currentState != null) {
            this.currentState = currentState;
            switch (WPMomentScreen$16.$SwitchMap$com$netflix$mediaclient$ui$iko$wordparty$moments$WPMomentScreen$WordPartyMomentState[currentState.ordinal()]) {
                default: {}
                case 1: {
                    this.startEntryAnimation();
                    this.playVOList(this.introVOList, WPMomentScreen$WordPartyMomentState.INSTRUCTION);
                }
                case 2: {
                    this.enableCards();
                    if (this.isLearnMoment()) {
                        this.startPanelAnimation(true);
                    }
                    this.playVOList(this.instructionVOList, WPMomentScreen$WordPartyMomentState.ITEM_SELECTION);
                }
                case 3: {
                    this.startStopTimeoutTimer(true);
                }
                case 4: {
                    this.manager.playVictorySound();
                    if (this.isLearnMoment()) {
                        currentState = WPMomentScreen$WordPartyMomentState.SUMMARY;
                    }
                    else {
                        currentState = WPMomentScreen$WordPartyMomentState.RECAP;
                    }
                    this.playVOList(this.positiveLineVOList, currentState);
                    if (!this.isLearnMoment()) {
                        this.startRecapAnimation();
                        return;
                    }
                    break;
                }
                case 5: {
                    this.playVOList(this.recapVOList, WPMomentScreen$WordPartyMomentState.RECAP_ITEMS);
                }
                case 6: {
                    this.playRecapItems();
                }
                case 7: {
                    if (this.isLearnMoment()) {
                        this.manager.setActiveExit(true);
                    }
                    this.playVOList(this.summaryVOList, WPMomentScreen$WordPartyMomentState.OUTRO);
                }
                case 8: {
                    IClientLogging$CompletionReason clientLogging$CompletionReason;
                    if (this.timeoutCounter >= 2) {
                        clientLogging$CompletionReason = IClientLogging$CompletionReason.canceled;
                    }
                    else {
                        clientLogging$CompletionReason = IClientLogging$CompletionReason.success;
                    }
                    String s;
                    if (this.timeoutCounter >= 2) {
                        s = "PASSIVE_EXIT";
                    }
                    else {
                        s = "ACTIVE_EXIT";
                    }
                    this.manager.reportMomentEnded(clientLogging$CompletionReason, s);
                    this.manager.hide();
                }
            }
        }
    }
    
    private void playAudioList(final List<WPInteractiveMomentsModel$WPAudio> list, final int n, final WPMomentScreen$WordPartyMomentState wpMomentScreen$WordPartyMomentState) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "playVOList: currentState = " + this.currentState + " nextState = " + wpMomentScreen$WordPartyMomentState);
        }
        if (this.isMomentClosed()) {
            return;
        }
        if (list == null || list.isEmpty()) {
            Log.d("WPMomentScreen", "Unable to play audio for given empty or null VO list");
            this.moveToState(wpMomentScreen$WordPartyMomentState);
            return;
        }
        final int size = list.size();
        if (n >= size) {
            this.moveToState(wpMomentScreen$WordPartyMomentState);
            return;
        }
        this.playVO(list.get(n), new WPMomentScreen$13(this, wpMomentScreen$WordPartyMomentState, n, size, list));
    }
    
    private void playRecapItem(final int n) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "playRecapItem: Counter = " + n);
        }
        if (!this.isMomentClosed()) {
            if (this.cardsList == null || this.cardsList.isEmpty()) {
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "playRecapItem: cardsList is null or empty");
                }
            }
            else {
                if (n > 0) {
                    this.arrangeCardsForRecap();
                }
                if (n < this.cardsList.size()) {
                    this.startRecapAnimation(n);
                    return;
                }
                this.startRecapExitAnimation();
                this.moveToState(WPMomentScreen$WordPartyMomentState.SUMMARY);
            }
        }
    }
    
    private void playRecapItems() {
        this.playRecapItem(0);
    }
    
    private void playVO(final WPInteractiveMomentsModel$WPAudio wpInteractiveMomentsModel$WPAudio, final BaseInteractiveMomentsManager$PlaybackCompleteListener baseInteractiveMomentsManager$PlaybackCompleteListener) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "playVO: invoked");
        }
        if (this.isMomentClosed()) {
            return;
        }
        if (wpInteractiveMomentsModel$WPAudio == null || StringUtils.isEmpty(wpInteractiveMomentsModel$WPAudio.getUrl())) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "playVO: audio is null or url is empty");
            }
            baseInteractiveMomentsManager$PlaybackCompleteListener.onComplete(null);
            return;
        }
        this.manager.playAudio(wpInteractiveMomentsModel$WPAudio.getUrl(), wpInteractiveMomentsModel$WPAudio.getVolume(), false, baseInteractiveMomentsManager$PlaybackCompleteListener);
        this.currentlyPlayingAudioList.add(wpInteractiveMomentsModel$WPAudio.getUrl());
    }
    
    private void playVOList(final List<WPInteractiveMomentsModel$WPAudio> list, final WPMomentScreen$WordPartyMomentState wpMomentScreen$WordPartyMomentState) {
        this.playAudioList(list, 0, wpMomentScreen$WordPartyMomentState);
    }
    
    private void releaseBitmapList(final List<Bitmap> list) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "releaseBitmapList: start");
        }
        if (list == null || list.isEmpty()) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "releaseBitmapList: input list is either null or empty");
            }
        }
        else {
            final Iterator<Bitmap> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.manager.releaseBitmaps(iterator.next());
            }
        }
    }
    
    private void releaseBitmaps() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "releaseBitmaps for card open, closed and recap list bitmaps");
        }
        this.releaseBitmapList(this.cardOpenBitmapList);
        this.releaseBitmapList(this.cardClosedBitmapList);
        this.releaseBitmapList(this.recapBitmapList);
    }
    
    private void resetCards(final boolean b) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "resetCards: closeCard = " + b);
        }
        final Iterator<WPCardView> iterator = this.cardViewsList.iterator();
        while (iterator.hasNext()) {
            iterator.next().reset(b);
        }
    }
    
    private void scaleUpCard(final WPCardView wpCardView) {
        if (wpCardView == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "scaleUpCard: card is null");
            }
        }
        else {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "scaleUpCard: cardsEnabled = " + this.cardsEnabled);
            }
            if (this.cardsEnabled) {
                this.timeoutCounter = 0;
                this.startStopTimeoutTimer(false);
                final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)wpCardView, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(View.SCALE_X, new float[] { wpCardView.getScaleX() * 1.1f }), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[] { wpCardView.getScaleY() * 1.1f }) });
                ofPropertyValuesHolder.addListener((Animator$AnimatorListener)new WPMomentScreen$3(this, wpCardView));
                ofPropertyValuesHolder.setDuration(500L);
                ofPropertyValuesHolder.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
                ofPropertyValuesHolder.start();
                this.onCardClickStart(wpCardView);
            }
        }
    }
    
    private void showCurrentLearnMomentCard() {
        if (this.cardsList == null || this.cardsList.isEmpty()) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "showCurrentLearnMomentCard: cardsList is null or empty.");
            }
        }
        else {
            this.currentCard = this.cardsList.get(0);
            if (this.currentCard != null) {
                this.currentCard.setTranslationX(0.0f);
                this.currentCard.setTranslationY(0.0f);
                this.currentCard.setRotation(0.0f);
                final int cardHeight = this.currentCard.getCardHeight();
                final float n = (this.deviceHeight - this.getStatusBarHeight()) * 0.62f / cardHeight;
                final float translationY = cardHeight * n / 10.0f;
                this.currentCard.setScaleX(n);
                this.currentCard.setScaleY(n);
                this.currentCard.setTranslationY(translationY);
                AnimationUtils.startViewAppearanceAnimation((View)this.currentCard, true, 500);
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "showCurrentLearnMomentCard: cardsEnabled = true ");
                }
                this.cardsEnabled = true;
            }
        }
    }
    
    private void showHideCards(final boolean b) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "showHideCards: show = " + b);
        }
        final Iterator<WPCardView> iterator = this.cardsList.iterator();
        while (iterator.hasNext()) {
            ViewUtils.setVisibleOrGone((View)iterator.next(), b);
        }
    }
    
    private void startEntryAnimation() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startEntryAnimation: Start check if resources are already loaded.");
        }
        if (this.isMomentClosed() || this.currentMoment == null) {
            return;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startEntryAnimation: start. cardsEnabled = false");
        }
        if (this.currentMoment.getBackgroundImage() == null) {
            ViewUtils.setVisibleOrGone((View)this.bgView, false);
        }
        else {
            this.bgView.setImageBitmap(this.bgImageBitmap);
            ViewUtils.setVisibleOrGone((View)this.bgView, true);
        }
        if (this.currentMoment.getForegroundImage() == null) {
            ViewUtils.setVisibleOrGone((View)this.fgView, false);
        }
        else {
            this.fgView.setImageBitmap(this.fgImageBitmap);
            ViewUtils.setVisibleOrGone((View)this.fgView, true);
        }
        this.cardsEnabled = false;
        if (this.isLearnMoment()) {
            final int statusBarHeight = this.getStatusBarHeight();
            final float deviceHeight = this.deviceHeight;
            int navigationBarHeight;
            if (this.manager.isNavigationBarBelowContent()) {
                navigationBarHeight = ViewUtils.getNavigationBarHeight(this.manager.getContext(), false);
            }
            else {
                navigationBarHeight = 0;
            }
            final float n = navigationBarHeight + deviceHeight - statusBarHeight;
            final int height = (int)Math.ceil(n * 0.56f);
            final int width = (int)Math.ceil(n * 0.56f * 1.778f);
            this.panelContainer.getLayoutParams().height = height;
            this.panelContainer.getLayoutParams().width = width;
            ViewUtils.setVisibleOrGone((View)this.panelContainer, true);
            this.fgView.setPadding(0, statusBarHeight, 0, 0);
            this.panelContainer.setTranslationY((float)(statusBarHeight / 2));
            this.fgView.setImageBitmap(this.fgImageBitmap);
            this.fgView.setScaleType(ImageView$ScaleType.CENTER_CROP);
            ViewUtils.setVisibleOrGone((View)this.fgView, true);
            return;
        }
        this.showHideCards(true);
        this.animationStartValues(this.isRevealMoment());
        final AnimatorSet set = new AnimatorSet();
        set.playTogether((Collection)this.getRevealCardAnimations(this.cardsList, true));
        set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
        set.setDuration(1000L);
        set.addListener((Animator$AnimatorListener)new WPMomentScreen$10(this));
        set.start();
    }
    
    private void startPanelAnimation(final boolean b) {
        if (this.panelList == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "showPanelAnimation: PanelList is null.");
            }
            return;
        }
        final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
        for (int size = this.panelList.size(), i = 0; i < size; ++i) {
            final int learnMomentPanelColor = this.getLearnMomentPanelColor(i);
            final View view = this.panelList.get(i);
            final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(View.ROTATION_Y, new float[] { view.getRotationY() + 180.0f }), PropertyValuesHolder.ofFloat(View.ALPHA, WPConstants.CARD_FLIP_ALPHA_VALUE_LIST) });
            ofPropertyValuesHolder.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new WPMomentScreen$11(this, b, view, learnMomentPanelColor, i, size, ofPropertyValuesHolder));
            ofPropertyValuesHolder.setDuration(500L);
            ofPropertyValuesHolder.setStartDelay((i + 1) * 166L);
            list.add(ofPropertyValuesHolder);
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether((Collection)list);
        set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
        set.addListener((Animator$AnimatorListener)new WPMomentScreen$12(this, b));
        set.start();
        this.manager.playPanelShuffleSound();
    }
    
    private void startRecapAnimation() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startRecapAnimation: started cardsEnabled = false");
        }
        this.cardsEnabled = false;
        this.showHideCards(true);
        this.prepareRecapScreen();
        this.showHideCards(false);
        this.animationRecapStartValues();
        this.startRecapEntryAnimation();
    }
    
    private void startRecapAnimation(final int n) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startRecapAnimation: counter = " + n);
        }
        if (this.isMomentClosed()) {
            return;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startRecapAnimation: animation started");
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether((Collection)this.getRecapAnimations(this.cardsList));
        set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
        set.addListener((Animator$AnimatorListener)new WPMomentScreen$14(this, n));
        set.start();
    }
    
    private void startRecapEntryAnimation() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startRecapEntryAnimation: started");
        }
        if (this.isMomentClosed()) {
            return;
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether((Collection)this.getRecapEntryAnimations(this.cardsList));
        set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
        set.setDuration(1500L);
        set.start();
    }
    
    private void startRecapExitAnimation() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startRecapExitAnimation: started");
        }
        if (this.isMomentClosed()) {
            return;
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether((Collection)this.getRecapExitAnimations(this.cardsList));
        set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
        set.start();
    }
    
    private void startStopTimeoutTimer(final boolean b) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "startStopTimeoutTimer: start = " + b);
        }
        this.handler.removeCallbacks(this.timeoutRunnable);
        if (b && this.currentState == WPMomentScreen$WordPartyMomentState.ITEM_SELECTION) {
            this.handler.postDelayed(this.timeoutRunnable, 15000L);
        }
    }
    
    private void startWiggleAnimation() {
        if (!this.isMomentClosed()) {
            if (this.cardsList == null) {
                Log.d("WPMomentScreen", "cardsList is null");
                return;
            }
            final int size = this.cardsList.size();
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            for (int i = 0; i < size; ++i) {
                final WPCardView wpCardView = this.cardsList.get(i);
                if (wpCardView == null) {
                    Log.d("TAG", "CardView is null. returning without animation");
                    return;
                }
                list.add(wpCardView.getWiggleAnimation(i));
            }
            final AnimatorSet set = new AnimatorSet();
            set.setDuration(500L);
            set.playTogether((Collection)list);
            set.addListener((Animator$AnimatorListener)new WPMomentScreen$8(this));
            set.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
            set.start();
            this.cardsEnabled = false;
            this.manager.playWiggleSound();
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "startWiggleAnimation: started cardsEnabled = false");
            }
        }
    }
    
    private void zoomInCard(final WPCardView wpCardView) {
        if (wpCardView == null && Log.isLoggable()) {
            Log.d("WPMomentScreen", "zoomInCard: card is null");
        }
        if (!this.isMomentClosed()) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "zoomInCard: cardsEnabled = " + this.cardsEnabled);
            }
            if (this.cardsEnabled) {
                this.timeoutCounter = 0;
                this.startStopTimeoutTimer(false);
                this.wpContainer.setPivotX(wpCardView.getX() + wpCardView.getPivotX());
                this.wpContainer.setPivotY(wpCardView.getY() + wpCardView.getPivotY());
                final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)this.wpContainer, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(View.ROTATION, new float[] { this.wpContainer.getRotation(), -1.0f * wpCardView.getRotation() }), PropertyValuesHolder.ofFloat(View.SCALE_X, new float[] { this.wpContainer.getScaleX(), 2.0f }), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[] { this.wpContainer.getScaleY(), 2.0f }), PropertyValuesHolder.ofFloat(View.X, new float[] { this.wpContainer.getX(), this.wpContainer.getWidth() / 2 - (wpCardView.getX() + wpCardView.getPivotX()) }), PropertyValuesHolder.ofFloat(View.Y, new float[] { this.wpContainer.getY(), this.wpContainer.getHeight() / 2 - (wpCardView.getY() + wpCardView.getPivotY()) }) });
                ofPropertyValuesHolder.addListener((Animator$AnimatorListener)new WPMomentScreen$4(this, wpCardView));
                ofPropertyValuesHolder.setDuration(500L);
                ofPropertyValuesHolder.setInterpolator((TimeInterpolator)this.quintOutInterpolator);
                ofPropertyValuesHolder.start();
                this.onCardClickStart(wpCardView);
            }
        }
    }
    
    public void configureCards(final WPInteractiveMomentsModel$WPMoment wpInteractiveMomentsModel$WPMoment) {
        ThreadUtils.assertNotOnMain();
        if (wpInteractiveMomentsModel$WPMoment == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "configureCards: moment is null");
            }
        }
        else {
            this.itemList = wpInteractiveMomentsModel$WPMoment.getItems();
            this.recapList = wpInteractiveMomentsModel$WPMoment.getRecapItems();
            if (this.itemList != null) {
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "configureCards: Clearing bitmaps list");
                }
                final int size = this.itemList.size();
                this.cardOpenBitmapList.clear();
                this.cardClosedBitmapList.clear();
                this.recapBitmapList.clear();
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "configureCards: itemsList size = " + size);
                }
                for (final WPInteractiveMomentsModel$WPItem wpInteractiveMomentsModel$WPItem : this.itemList) {
                    if (wpInteractiveMomentsModel$WPItem == null) {
                        if (!Log.isLoggable()) {
                            continue;
                        }
                        Log.d("WPMomentScreen", "configureCards: card is null");
                    }
                    else {
                        Bitmap bitmap = this.manager.getBitmapFromCache(wpInteractiveMomentsModel$WPItem.getCardClosedImage());
                        this.cardClosedBitmapList.add(bitmap);
                        final WPInteractiveMomentsModel$WPImage cardOpenImage = wpInteractiveMomentsModel$WPItem.getCardOpenImage();
                        if (cardOpenImage != null) {
                            bitmap = this.manager.getBitmapFromCache(cardOpenImage);
                        }
                        this.cardOpenBitmapList.add(bitmap);
                    }
                }
                if (this.recapList != null) {
                    for (final WPInteractiveMomentsModel$WPItem wpInteractiveMomentsModel$WPItem2 : this.recapList) {
                        if (wpInteractiveMomentsModel$WPItem2 != null) {
                            this.recapBitmapList.add(this.manager.getBitmapFromCache(wpInteractiveMomentsModel$WPItem2.getCardClosedImage()));
                        }
                    }
                }
                final WPInteractiveMomentsModel$WPImage backgroundImage = wpInteractiveMomentsModel$WPMoment.getBackgroundImage();
                if (backgroundImage != null) {
                    this.bgImageBitmap = this.manager.getBitmapFromCache(backgroundImage);
                }
                final WPInteractiveMomentsModel$WPImage foregroundImage = wpInteractiveMomentsModel$WPMoment.getForegroundImage();
                if (foregroundImage != null) {
                    this.fgImageBitmap = this.manager.getBitmapFromCache(foregroundImage);
                }
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "configureCards: resourcesLoaded = true");
                }
                this.resourcesLoaded = true;
                this.prepareAndStartIfPending();
                return;
            }
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "configureCards: itemList is null");
            }
        }
    }
    
    public void discardAnimationComplete() {
        if (this.cardsList != null && this.cardsList.isEmpty()) {
            this.moveToState(WPMomentScreen$WordPartyMomentState.POSITIVE_LINE);
            this.startStopTimeoutTimer(false);
            return;
        }
        this.startStopTimeoutTimer(true);
    }
    
    public void findViews(final View view) {
        this.wpContainer = (ViewGroup)view.findViewById(2131690320);
        this.ikoContainer = (ViewGroup)view.findViewById(2131689840);
        this.card1 = (WPCardView)view.findViewById(2131690331);
        this.card2 = (WPCardView)view.findViewById(2131690332);
        this.card3 = (WPCardView)view.findViewById(2131690333);
        this.card4 = (WPCardView)view.findViewById(2131690334);
        ViewUtils.setVisibleOrGone((View)(this.panelContainer = (LinearLayout)view.findViewById(2131690326)), false);
        this.panel1 = view.findViewById(2131690327);
        this.panel2 = view.findViewById(2131690328);
        this.panel3 = view.findViewById(2131690329);
        this.panel4 = view.findViewById(2131690330);
        this.bgView = (ImageView)view.findViewById(2131690325);
        this.fgView = (ImageView)view.findViewById(2131690335);
        this.cardClickListener = (View$OnClickListener)new WPMomentScreen$9(this);
        this.cardViewsList.add(this.card1);
        this.cardViewsList.add(this.card2);
        this.cardViewsList.add(this.card3);
        this.cardViewsList.add(this.card4);
        this.panelList.add(this.panel1);
        this.panelList.add(this.panel2);
        this.panelList.add(this.panel3);
        this.panelList.add(this.panel4);
        this.colorYellow = this.manager.getContext().getResources().getColor(2131624167);
        this.colorGreen = this.manager.getContext().getResources().getColor(2131624165);
        this.colorRed = this.manager.getContext().getResources().getColor(2131624166);
        this.colorBlue = this.manager.getContext().getResources().getColor(2131624164);
        this.colorWhite = this.manager.getContext().getResources().getColor(2131624162);
        for (final WPCardView wpCardView : this.cardViewsList) {
            wpCardView.setOnClickListener(this.cardClickListener);
            wpCardView.setCardClickListener(this);
        }
    }
    
    public WPMomentScreen$WordPartyMomentState getCurrentState() {
        return this.currentState;
    }
    
    public void hideScreen() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "hideScreen: stopping timeout timer and hiding cards");
        }
        this.timeoutCounter = 0;
        this.momentClosed = true;
        this.startStopTimeoutTimer(this.isPendingStart = false);
        ViewUtils.setVisibleOrGone((View)this.bgView, false);
        ViewUtils.setVisibleOrGone((View)this.fgView, false);
        ViewUtils.setVisibleOrGone((View)this.panelContainer, false);
        final Iterator<WPCardView> iterator = this.cardViewsList.iterator();
        while (iterator.hasNext()) {
            ViewUtils.setVisibleOrGone((View)iterator.next(), false);
        }
    }
    
    public boolean isLearnMoment() {
        return this.currentMoment != null && "LEARN".equalsIgnoreCase(this.currentMoment.getSceneType());
    }
    
    public boolean isMomentClosed() {
        final boolean b = this.screenBackgrounded || this.momentClosed;
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "isMomentClosed - " + b);
        }
        return b;
    }
    
    @Override
    public void onCardClickEnd(final WPCardView wpCardView) {
        if (wpCardView == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "onCardClickEnd: card is null");
            }
            return;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onCardClickEnd: playing VO for card - " + wpCardView);
        }
        this.playVO(wpCardView.getAudio(), new WPMomentScreen$1(this, wpCardView));
    }
    
    @Override
    public void onCardClickStart(final WPCardView wpCardView) {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onCardClickStart: cardsEnabled = false");
        }
        this.cardsEnabled = false;
    }
    
    public void onDestroy() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onDestroy: invoked on PlayerFragment");
        }
        if (this.cardViewsList != null && !this.cardViewsList.isEmpty()) {
            final Iterator<WPCardView> iterator = this.cardViewsList.iterator();
            while (iterator.hasNext()) {
                iterator.next().releaseResources();
            }
        }
        this.releaseBitmaps();
    }
    
    public void onPause() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onPause: invoked on PlayerFragment");
        }
        this.screenPaused = true;
    }
    
    public void onResume() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onResume: invoked on PlayerFragment");
        }
        this.screenPaused = false;
    }
    
    public void onStart() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onStart: invoked on PlayerFragment");
        }
        final boolean screenBackgrounded = this.screenBackgrounded;
        this.screenBackgrounded = false;
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onStop: screenBackgrounded = false");
        }
        if (!this.isMomentClosed() && screenBackgrounded && this.resourcesLoaded) {
            this.manager.playBgAudio();
            this.moveToState(this.currentState);
            if (this.currentState == WPMomentScreen$WordPartyMomentState.ITEM_SELECTION) {
                final boolean cardsEnabled = this.cardsEnabled;
                if (Log.isLoggable()) {
                    Log.d("WPMomentScreen", "onStart: in Item selection state. Current card animation complete = " + cardsEnabled);
                }
                if (cardsEnabled) {
                    this.cardClickAnimationComplete(this.currentCard);
                }
                else {
                    if (this.cardsList == null || this.cardsList.isEmpty()) {
                        this.moveToState(WPMomentScreen$WordPartyMomentState.POSITIVE_LINE);
                        return;
                    }
                    if (Log.isLoggable()) {
                        Log.d("WPMomentScreen", "onStart: Setting cardsEnabled to true");
                    }
                    this.onCardClickEnd(this.currentCard);
                }
            }
        }
    }
    
    public void onStop() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onStop: invoked on PlayerFragment");
        }
        this.startStopTimeoutTimer(false);
        this.screenBackgrounded = true;
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "onStop: screenBackgrounded = true");
        }
        if (this.currentState == WPMomentScreen$WordPartyMomentState.ITEM_SELECTION) {
            final boolean cardsEnabled = this.cardsEnabled;
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "onStop: in Item selection state. Current card animation complete = " + cardsEnabled);
            }
        }
    }
    
    public boolean prepareAndStart(final WPInteractiveMomentsModel$WPMoment currentMoment) {
        if (currentMoment == null) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "prepareAndStart: moment is null");
            }
            return false;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "prepareAndStart: moment = " + currentMoment.getSceneType());
        }
        this.showHideCards(false);
        this.currentMoment = currentMoment;
        this.momentClosed = false;
        final List<WPInteractiveMomentsModel$WPItem> items = currentMoment.getItems();
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "prepareAndStart: are resources loaded = " + this.resourcesLoaded);
        }
        if (items == null || !this.resourcesLoaded) {
            if (Log.isLoggable()) {
                Log.d("WPMomentScreen", "prepareAndStart: resources not loaded");
            }
            this.isPendingStart = true;
            return false;
        }
        this.isPendingStart = false;
        this.cardsList.clear();
        this.currentlyPlayingAudioList.clear();
        for (int size = items.size(), i = 0; i < size; ++i) {
            final WPCardView wpCardView = this.cardViewsList.get(i);
            final WPInteractiveMomentsModel$WPItem wpInteractiveMomentsModel$WPItem = items.get(i);
            if (wpInteractiveMomentsModel$WPItem != null) {
                wpCardView.setAudio(wpInteractiveMomentsModel$WPItem.getItemAudio());
            }
            this.cardsList.add(wpCardView);
            if (this.cardOpenBitmapList.size() > i && this.cardClosedBitmapList.size() > i) {
                final Bitmap bitmap = this.cardClosedBitmapList.get(i);
                final BitmapDrawable bitmapWithBorder = this.bitmapWithBorder(this.cardOpenBitmapList.get(i));
                BitmapDrawable bitmapWithBorder2;
                if ("REVEAL".equalsIgnoreCase(currentMoment.getSceneType())) {
                    bitmapWithBorder2 = this.bitmapWithBorder(bitmap);
                }
                else {
                    bitmapWithBorder2 = bitmapWithBorder;
                }
                wpCardView.setDrawables(bitmapWithBorder2, bitmapWithBorder);
            }
        }
        if (this.isLearnMoment() && this.panelList != null) {
            for (int j = 0; j < this.panelList.size(); ++j) {
                this.panelList.get(j).setBackgroundColor(this.getLearnMomentPanelColor(j));
            }
        }
        this.introVOList = currentMoment.getIntroductionAudioList();
        this.instructionVOList = currentMoment.getInstructionAudioList();
        this.timeoutVOList = currentMoment.getTimeoutAudioList();
        this.timeout2VOList = currentMoment.getTimeout2AudioList();
        this.passiveExitVOList = currentMoment.getPassiveExitAudioList();
        this.positiveLineVOList = currentMoment.getPositiveLineAudioList();
        this.recapVOList = currentMoment.getRecapAudioList();
        this.summaryVOList = currentMoment.getSummaryAudioList();
        this.start();
        return true;
    }
    
    public void prepareAndStartIfPending() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "prepareAndStartIfPending: is pending start? = " + this.isPendingStart);
        }
        if (this.isPendingStart) {
            this.handler.post((Runnable)new WPMomentScreen$2(this));
        }
    }
    
    public boolean prepareRecapScreen() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "prepareRecapScreen: start");
        }
        if (this.recapList != null && !this.recapList.isEmpty()) {
            this.cardsList.clear();
            for (int i = 0; i < this.recapList.size(); ++i) {
                final WPCardView wpCardView = this.cardViewsList.get(i);
                final WPInteractiveMomentsModel$WPItem wpInteractiveMomentsModel$WPItem = this.recapList.get(i);
                if (wpInteractiveMomentsModel$WPItem != null) {
                    wpCardView.setAudio(wpInteractiveMomentsModel$WPItem.getRecapAudio());
                }
                this.cardsList.add(wpCardView);
                if (this.recapBitmapList.size() <= i || this.cardClosedBitmapList.size() <= i) {
                    return false;
                }
                final BitmapDrawable bitmapWithBorder = this.bitmapWithBorder(this.recapBitmapList.get(i));
                wpCardView.setDrawables(bitmapWithBorder, bitmapWithBorder);
            }
            return true;
        }
        return false;
    }
    
    public void start() {
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "starting moment intro animation");
        }
        if (this.screenBackgrounded) {
            return;
        }
        if (Log.isLoggable()) {
            Log.d("WPMomentScreen", "start: Playing bg audio in loop");
        }
        this.moveToState(WPMomentScreen$WordPartyMomentState.INTRODUCTION);
        this.manager.playBgAudio();
    }
}