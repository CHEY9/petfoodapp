package com.example.dogfoodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.dogfoodapp.Domain.ItemsDomain;
import com.example.dogfoodapp.Domain.Order;
import com.example.dogfoodapp.R;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private DatabaseReference ordersRef;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
        this.ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
    }
    private List<Order> orders;

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orders.get(position);
        holder.totalTextView.setText("Total: $" + order.getTotal());
        holder.orderTextView.setText("Order ID: \n" + order.getOrder());

        // Create a string to display all item titles and quantities
        StringBuilder itemsString = new StringBuilder();
        for (ItemsDomain item : order.getItems()) {
            itemsString.append(item.getTitle()).append(" (Qty: ").append(item.getNumberinCart()).append(")\n");
        }
        holder.itemsTextView.setText("Items:\n" + itemsString.toString());

        holder.deleteButton.setOnClickListener(v -> {
            String orderId = order.getOrder();
            if (orderId != null) {
                ordersRef.child(orderId).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            // Notify user of successful deletion
                            Toast.makeText(holder.itemView.getContext(), "Order deleted", Toast.LENGTH_SHORT).show();
                            // Remove the item from the list and notify adapter
                            orders.remove(position);
                            notifyItemRemoved(position);
                        })
                        .addOnFailureListener(e -> {
                            // Notify user of failure
                            Toast.makeText(holder.itemView.getContext(), "Failed to delete order", Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView totalTextView;
        TextView itemsTextView;
        TextView orderTextView;
        Button deleteButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            itemsTextView = itemView.findViewById(R.id.itemsTextView);
            orderTextView = itemView.findViewById(R.id.orderTextView );
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}