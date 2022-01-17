package com.university.finalwork.content;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.university.finalwork.database.Orders;
import com.university.finalwork.database.Products;
import com.university.finalwork.database.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderContent {

    public static final List<OrderItem> ITEMS = new ArrayList<OrderItem>();
    public final Map<String, OrderItem> ITEM_MAP = new HashMap<String, OrderItem>();

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference productRef;
    private DatabaseReference orderRef;

    public OrderContent() {

        setFirebase();
        getData();
    }

    private void setFirebase() {

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        productRef = FirebaseDatabase.getInstance().getReference("Products");
        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        userRef.keepSynced(true);
        productRef.keepSynced(true);
        orderRef.keepSynced(true);
    }

    private void getData() {

        orderRef.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotList) {

                ITEMS.clear();
                ITEM_MAP.clear();

                for(DataSnapshot dataSnapshot : snapshotList.getChildren()){

                    Orders order = (Orders) dataSnapshot.getValue();

                    //if(!order.isWasDelivered()) {
                        final String[] orderData = new String[6];
                        orderData[0] = order.getOrderId();
                        orderData[4] = String.valueOf(order.getProductQuantity());

                        userRef.child(order.getUserId()).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Users user = (Users) snapshot.getValue();
                                orderData[1] = user.getUserName();
                                orderData[2] = user.getUserAddress();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        productRef.child(order.getProductId()).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Products product = (Products) snapshot.getValue();
                                orderData[3] = product.getProductName() + ", " + product.getProductSize();
                                orderData[5] = String.valueOf(product.getProductPrice());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        addItem(createOrderItem(orderData[0], orderData[1], orderData[2], orderData[3], Long.parseLong(orderData[4]), Double.parseDouble(orderData[5])));
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addItem(OrderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private OrderItem createOrderItem(String orderId, String userName, String userAddress, String productDesc, Long productQtty, Double productPrice) {
        return new OrderItem(orderId, userName, String.valueOf(productQtty * productPrice), userAddress, productDesc, String.valueOf(productQtty));
    }

    public class OrderItem {
        public final String id;
        public final String mOrderUser;
        public final String mOrderFinalPrice;
        public final String mOrderAddress;
        public final String mOrderItem;
        public final String mOrderQuantity;

        public OrderItem(String id, String mOrderUser, String mOrderFinalPrice, String mOrderAddress, String mOrderItem, String mOrderQuantity) {
            this.id = id;
            this.mOrderUser = mOrderUser;
            this.mOrderFinalPrice = mOrderFinalPrice;
            this.mOrderAddress = mOrderAddress;
            this.mOrderItem = mOrderItem;
            this.mOrderQuantity = mOrderQuantity;
        }
    }
}
