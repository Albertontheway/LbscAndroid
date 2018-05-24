package huxibianjie.com.lbscandroid.Adapart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import huxibianjie.com.lbscandroid.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<UserNumber.ContentBeanX.ContentBean> data;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView sort;

        public TextView number;
        public TextView area;
        public TextView money;
        public View View;

        public ViewHolder(View v) {
            super(v);
            View = v;
            sort = (TextView) v.findViewById(R.id.xrecyc_text1);
            number = (TextView) v.findViewById(R.id.xrecyc_text2);
            area = (TextView) v.findViewById(R.id.xrecyc_text3);
            money= (TextView) v.findViewById(R.id.xrecyc_text4);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, List<UserNumber.ContentBeanX.ContentBean> data) {
        this.data = data;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        Log.d("test", "onCreateViewHolder  viewType=" + viewType);
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rankings_text, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // ...
        ViewHolder vh = new ViewHolder(v);
        Log.d("test", "onCreateViewHolder  ViewHolder=" + vh);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d("test", "onBindViewHolder  holder=" + holder + " position=" + position);
        holder.sort.setText(data.get(position).getSort()+"");
        String phone = data.get(position).getPhone();
        String maskNumber = phone.substring(0,3)+"****"+phone.substring(7,phone.length());
        holder.number.setText(maskNumber);
        holder.area.setText(data.get(position).getArea());
        holder.money.setText(data.get(position).getMoney()+"");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("test", "getItemCount");
        return data.size();
    }


    @Override
    public long getItemId(int position) {
        Log.d("test", "getItemId   position=" + position);
        return super.getItemId(position);
    }
}

