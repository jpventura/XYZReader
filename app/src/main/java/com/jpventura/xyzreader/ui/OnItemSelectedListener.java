/*
 * Copyright 2015 Joao Paulo Fernandes Ventura
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
package com.jpventura.xyzreader.ui;

import android.view.View;

/**
 * Interface definition for a callback to be invoked when
 * an item in this view has been selected.
 *
 * It emulates the {@code View#OnItemSelectedListener} behaviour for a {@code RecyclerView}.
 */
public interface OnItemSelectedListener<VM> {
    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected.</p>
     *
     * @param view      The parent recycler view.
     * @param viewModel The view model related to the selected item.
     */
    void onItemSelected(View view, VM viewModel);
}
