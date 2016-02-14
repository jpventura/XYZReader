/*
 * Copyright 2015 JP Ventura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jpventura.xyzreader.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jpventura.xyzreader.R;
import com.squareup.picasso.Callback;

public class GetThumbnailPaletteCommand
        implements AnimatorUpdateListener, Callback, PaletteAsyncListener {
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private int mColorFrom;
    private int mPrimaryDarkColor;

    public GetThumbnailPaletteCommand(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.list_item_layout);
        mImageView = (ImageView) view.findViewById(R.id.thumbnail);
        mColorFrom = view.getResources().getColor(android.R.color.white);
        mPrimaryDarkColor = view.getResources().getColor(R.color.primary_dark);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mLinearLayout.setBackgroundColor((Integer) animation.getAnimatedValue());
    }

    @Override
    public void onGenerated(Palette palette) {
        int colorTo = palette.getDarkMutedColor(mPrimaryDarkColor);
        ValueAnimator colorAnimation =
                ValueAnimator.ofObject(new ArgbEvaluator(), mColorFrom, colorTo);
        colorAnimation.addUpdateListener(this);
        colorAnimation.start();
    }

    @Override
    public void onSuccess() {
        Palette.from(((BitmapDrawable) mImageView.getDrawable()).getBitmap()).generate(this);
    }

    @Override
    public void onError() {
    }
}
