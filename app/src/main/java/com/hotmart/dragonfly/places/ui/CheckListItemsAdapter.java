/*
 * This file is part of Zum.
 * 
 * Zum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Zum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.hotmart.dragonfly.places.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.ChecklistItemResponseVO;
import com.hotmart.dragonfly.ui.CollectionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class CheckListItemsAdapter extends CollectionRecyclerViewAdapter<CheckListItemsAdapter.ViewHolder, ChecklistItemResponseVO> {

    private Context mContext;

    public CheckListItemsAdapter(Context context,
                                 Collection<ChecklistItemResponseVO> dataset) {
        super(context, dataset);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, ChecklistItemResponseVO object) {

        String label = object.getName().substring(0, 1);

        if (position == 0) {
            holder.showHeader(label);
        }
        else {
            ChecklistItemResponseVO lastItem = getObject(position - 1);

            if (label.equalsIgnoreCase(lastItem.getName().substring(0, 1))) {
                holder.hideHeader();
            }
            else {
                holder.showHeader(label);
            }
        }
        holder.setModel(object);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_linearlayout)
        LinearLayout header_linearlayout;

        @BindView(R.id.header_title)
        TextView header_title;

        @BindView(R.id.textView_name)
        TextView textView_name;

        @BindView(R.id.checkbox_item)
        AppCompatCheckBox checkbox_item;

        @BindView(R.id.image_item)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void showHeader(String label){
            header_linearlayout.setVisibility(View.VISIBLE);
            header_title.setText(label);
        }

        @OnCheckedChanged(R.id.checkbox_item)
        void availableItem(AppCompatCheckBox checkBox){
            ChecklistItemResponseVO item = (ChecklistItemResponseVO) checkBox.getTag();
            item.setAvailable(checkBox.isChecked());
        }

        void hideHeader(){
            header_linearlayout.setVisibility(View.GONE);
        }

        void setModel(ChecklistItemResponseVO model) {
            resetViews();
            textView_name.setText(model.getName());
            checkbox_item.setTag(model);
            checkbox_item.setChecked(model.isAvailable());
            Glide.with(mContext).load(model.getImageUrl()).placeholder(R.drawable.placeholder)
                 .error(R.drawable.placeholder).into(image);
        }

        private void resetViews() {
            textView_name.setText(null);
            image.setImageDrawable(null);
        }
    }

}
